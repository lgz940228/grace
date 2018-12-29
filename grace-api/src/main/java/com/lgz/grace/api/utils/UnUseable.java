package com.lgz.grace.api.utils;

/**
 * Created by lgz on 2018/12/29.
 */
public class UnUseable {

    /*
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
<!--XDocReport 主要是操作word，如动态插入数字、汉字、图片，以指令形式去执行程序输出结果-->
        <!--usage: https://github.com/opensagres/xdocreport-->
        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.template.velocity</artifactId>
            <version>1.0.4</version>
            <exclusions>
                <exclusion>
                    <artifactId>commons-lang</artifactId>
                    <groupId>commons-lang</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.document.docx</artifactId>
            <version>1.0.4</version>
        </dependency>
        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.converter.docx.xwpf</artifactId>
            <version>1.0.4</version>
        </dependency>
        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.itext.extension</artifactId>
            <version>1.0.4</version>
        </dependency>
        <dependency>
            <groupId>com.lowagie</groupId>
            <artifactId>itext</artifactId>
            <version>2.1.7</version>
        </dependency>
        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.core</artifactId>
            <version>1.0.4</version>
        </dependency>
        <!--apache poi-->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>3.11</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>3.11</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>ooxml-schemas</artifactId>
            <version>1.1</version>
        </dependency>
    * */
    /**
     * 生成合同附件，模板工具生成
     *
     * @param saleDetail
     * @return
     */
    /*public static Res<List<FileDTO>> getPDFSaleOrderContract(SaleDetailDTO saleDetail) {

        List<FileDTO> fileDtoList = ListUtil.emptyList();
        Map<String, String> map = getSaleContractDataMap(saleDetail);
        map.put("prefix", "saleDetail");

        LOGGER.info("生成合同: saleNo="+saleDetail.getSaleNo()+", data:"+ JSONUtil.toString(map));

        // 资金来源
        int capitalSource = saleDetail.getCapitalSource();

        Map<String, String> nameAndPathMap = generateNameAndPath(saleDetail.getFinBusLine(), capitalSource);

        for (Map.Entry<String, String> nameAndPath : nameAndPathMap.entrySet()) {
            String fileName = nameAndPath.getKey();
            String filePath = nameAndPath.getValue();
            LOGGER.error("处理合同文件：" + fileName);
            if (!fileName.equals(NO_CONTRACT_KEY)) {
                try {
                    File f = new File(filePath);

                    OutputStream outputStream = render(filePath, f, map);
                    if (outputStream == null) {
                        return Res.err("处理PDF文件转换时出现异常，数据为空");
                    }
                    LOGGER.info("生成合同成功:fileName=" + fileName + ", 模板Path=" + filePath);
                    FileDTO fileDTO = new FileDTO(fileName, ((ByteArrayOutputStream) outputStream).toByteArray());
                    fileDtoList.add(fileDTO);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            } else {
                return Res.err("销售协议维护中，请联系您的销售顾问");
            }
        }

        return Res.suc(fileDtoList);
    }*/


    /**
     * 渲染
     *
     * @param inputUrl
     * @param template
     * @param data
     * @return
     */
    /*public static OutputStream render(final String inputUrl, final File template, Map<String, String> data) {
        InputStream in = null;
        try {
            in = new FileInputStream(template);
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
            report.setFieldsMetadata(new FieldsMetadata());

//            Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
            PdfOptions pdfOptions = PdfOptions.create().fontProvider(new IFontProvider() {
                //设置字体
                @Override
                public Font getFont(String familyName, String encoding, float size, int style, Color color) {
                    try {

                        String fontPath = VehicleProcUtil.class.getResource("/").getPath() + "template" + File.separator + "SimSun.ttf";
                        Font font = FontFactory.getFont(fontPath, encoding, BaseFont.NOT_EMBEDDED);
                        return font;
                    } catch (Exception e) {
                        LOGGER.error("获取项目字体文件 error:", e);
                        return FontFactory.getFont(familyName, encoding, size, style, color);
                    }
                }
            });
//            options.subOptions(pdfOptions);

            OutputStream outputStream = new ByteArrayOutputStream();
            XWPFDocument doc = WorderToNewWordUtils.changWord(inputUrl, data, null);
            PdfConverter.getInstance().convert(doc, outputStream, pdfOptions);
//            report.convert(data, options, outputStream);
            return outputStream;
        } catch (Exception e) {
            LOGGER.error("render error:", e);
            return null;
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}
