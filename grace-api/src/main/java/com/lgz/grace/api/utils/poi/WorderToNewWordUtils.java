package com.lgz.grace.api.utils.poi;

import com.lgz.grace.api.utils.collection.ListUtil;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 通过word模板生成新的word工具类
 *
 * @author zhiheng
 *
 */
public class WorderToNewWordUtils {

    private static Map<String, Integer> pictureType = ListUtil.emptyMap();

    {
        pictureType.put(".jpeg", XWPFDocument.PICTURE_TYPE_JPEG);
        pictureType.put(".png", XWPFDocument.PICTURE_TYPE_PNG);
    }


    /**
     * 渲染
     *
     * @param inputUrl
     * @param template
     * @param data
     * @return
     */
    public static OutputStream render(final String inputUrl, final File template, Map<String, String> data) {
        InputStream in = null;
        try {
            in = new FileInputStream(template);
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
            report.setFieldsMetadata(new FieldsMetadata());

//            Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
            PdfOptions pdfOptions = PdfOptions.create().fontProvider(new IFontProvider() {
                //设置字体
                @Override
                public com.lowagie.text.Font getFont(String familyName, String encoding, float size, int style, Color color) {
                    try {

                        String fontPath = WorderToNewWordUtils.class.getResource("/").getPath() + "template" + File.separator + "SimSun.ttf";
                        com.lowagie.text.Font font = FontFactory.getFont(fontPath, encoding, BaseFont.NOT_EMBEDDED);
                        return font;
                    } catch (Exception e) {
                        //LOGGER.error("获取项目字体文件 error:", e);
                        return FontFactory.getFont(familyName, encoding, size, style, color);
                    }
                }
            });
//            options.subOptions(pdfOptions);
            OutputStream outputStream = new ByteArrayOutputStream();
            XWPFDocument doc = changWord(inputUrl, data, null);
            PdfConverter.getInstance().convert(doc, outputStream, pdfOptions);
//            report.convert(data, options, outputStream);
            return outputStream;
        } catch (Exception e) {
            //LOGGER.error("render error:", e);
            return null;
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 根据模板生成新word文档
     * 判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
     * @param inputUrl 模板存放地址
     * @param textMap 需要替换的信息集合
     * @param tableList 需要插入的表格信息集合
     * @return 成功返回true,失败返回false
     */
    public static XWPFDocument changWord(String inputUrl, Map<String, String> textMap, List<String[]> tableList) {

        //模板转换默认成功
        boolean changeFlag = true;
        try {
            //获取docx解析对象
            XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
//            CustomXWPFDocument document = new CustomXWPFDocument(POIXMLDocument.openPackage(inputUrl));
            //解析替换文本段落对象
            WorderToNewWordUtils.changeText(document, textMap);
            //解析替换表格对象
            WorderToNewWordUtils.changeTable(document, textMap, tableList);
            return document;

            /*//生成新的word
            File file = new File(outputUrl);
            FileOutputStream stream = new FileOutputStream(file);
            document.write(stream);
            stream.close();*/

        } catch (IOException e) {
            e.printStackTrace();
            changeFlag = false;
        }

        return null;

    }


    /**
     * 替换段落文本
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     */
    public static void changeText(XWPFDocument document, Map<String, String> textMap){
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            if(checkText(text)) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    // 替换模板原来位置
                    run.setText(changeValue(run.toString(), textMap),0);
                }
            }
        }

    }

    /**
     * 替换表格对象方法
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     * @param tableList 需要插入的表格信息集合
     */
    public static void changeTable(XWPFDocument document, Map<String, String> textMap,
                                   List<String[]> tableList){
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            //只处理行数大于等于1的表格，且不循环表头
            XWPFTable table = tables.get(i);
            if(table.getRows().size()>=1){
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if(checkText(table.getText())){
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(rows, textMap);
                }else{
//                  System.out.println("插入"+table.getText());
                    insertTable(table, tableList);
                }
            }
        }
    }





    /**
     * 遍历表格
     * @param rows 表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows , Map<String, String> textMap){
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if(checkText(cell.getText())){
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            run.setText(changeValue(run.toString(), textMap),0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 为表格插入数据，行数不够添加新行
     * @param table 需要插入数据的表格
     * @param tableList 插入数据集合
     */
    public static void insertTable(XWPFTable table, List<String[]> tableList){
        //创建行,根据需要插入的数据添加新行，不处理表头
        for(int i = 1; i < tableList.size(); i++){
            XWPFTableRow row =table.createRow();
        }
        //遍历表格插入数据
        List<XWPFTableRow> rows = table.getRows();
        for(int i = 1; i < rows.size(); i++){
            XWPFTableRow newRow = table.getRow(i);
            List<XWPFTableCell> cells = newRow.getTableCells();
            for(int j = 0; j < cells.size(); j++){
                XWPFTableCell cell = cells.get(j);
                cell.setText(tableList.get(i-1)[j]);
            }
        }

    }



    /**
     * 判断文本中时候包含$
     * @param text 文本
     * @return 包含返回true,不包含返回false
     */
    public static boolean checkText(String text){
        boolean check  =  false;
        if(text.indexOf("$")!= -1){
            check = true;
        }
        return check;

    }

    /**
     * 判断文本中时候包含#
     * @param text 文本
     * @return 包含返回true,不包含返回false
     */
    public static boolean checkPicture(String text){
        boolean check  =  false;
        if(text.contains("#{")){
            check = true;
        }
        return check;

    }

    /**
     * 匹配传入信息集合与模板
     * @param value 模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String value, Map<String, String> textMap){
        Set<Entry<String, String>> textSets = textMap.entrySet();
        for (Entry<String, String> textSet : textSets) {
            //匹配模板与替换值 格式$key
            String key = "${"+textSet.getKey()+"}";
            if(value.contains(key)){
                value = value.replace(key, textSet.getValue());
            }
        }
        //模板未匹配到区域替换为空
        if(checkText(value)){
            value = "";
        }
        return value;
    }




    /*public static void main(String[] args) {
        //模板文件地址
        String inputUrl = "C:\\Users\\jason\\Desktop\\ucar_purchase_contract.docx";
        //新生产的模板文件
        String outputUrl = "C:\\Users\\jason\\Desktop\\test222.docx";

        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("agencyName", "小明");
        testMap.put("sex", "男");
        testMap.put("saleNo", "软件园");
        testMap.put("phone", "88888888");

        List<String[]> testList = new ArrayList<String[]>();
        testList.add(new String[]{"1","1AA","1BB","1CC"});
        testList.add(new String[]{"2","2AA","2BB","2CC"});
        testList.add(new String[]{"3","3AA","3BB","3CC"});
        testList.add(new String[]{"4","4AA","4BB","4CC"});

        WorderToNewWordUtils.createWord(inputUrl, outputUrl, testMap, testList);

    }

    *//**
     * 生成word文档
     * @author linhj
     * @date 2018/7/3
     **//*
    public static void createWord(String inputUrl, String  outputUrl, Map<String, String> textMap, List<String[]> tableList) {

        //模板转换默认成功
        boolean changeFlag = true;
        try {
            //获取docx解析对象
            XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
            //解析替换文本段落对象
            WorderToNewWordUtils.changeText(document, textMap);
            //解析替换表格对象
            WorderToNewWordUtils.changeTable(document, textMap, tableList);

            //生成新的word
            File file = new File(outputUrl);
            FileOutputStream stream = new FileOutputStream(file);
            document.write(stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
            changeFlag = false;
        }

    }*/
}
