package com.lgz.grace.api.utils.poi;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgz on 2019/3/14.
 */
@Controller
@RequestMapping("/poi/")
public class PoiTestController {



    @RequestMapping("pdf")
    @ResponseBody
    public String downloadPdf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成的文件路径前缀
        String filePrefix = PoiTestController.class.getResource("/").getPath();
        String filePath = filePrefix+"template/month_payment.docx";

        File f = new File(filePath);
        Map<String,String> map = new HashMap<>();
        map.put("saleNo","123456");
        map.put("companyName","lgz");
        map.put("memberName","gzx");
        OutputStream outputStream = render(filePath, f, map,response);
        //String enName = new String(pdfTitle.getBytes("gbk"),"ISO-8859-1");
        //设置响应头
        response.setHeader("content-disposition","attachment;filename=" + ".pdf");
        response.setHeader("content-type", "application/pdf");
        outputStream.close();
        return "success";
    }

    /**
     * 渲染
     *
     * @param inputUrl
     * @param template
     * @param data
     * @return
     */
    public static OutputStream render(final String inputUrl, final File template, Map<String, String> data,HttpServletResponse response) {
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

                        String fontPath = PoiTestController.class.getResource("/").getPath() + "template" + File.separator + "SimSun.ttf";
                        Font font = FontFactory.getFont(fontPath, encoding, BaseFont.NOT_EMBEDDED);
                        return font;
                    } catch (Exception e) {
                        //LOGGER.error("获取项目字体文件 error:", e);
                        return FontFactory.getFont(familyName, encoding, size, style, color);
                    }
                }
            });
//            options.subOptions(pdfOptions);
            OutputStream outputStream = response.getOutputStream();
            XWPFDocument doc = WorderToNewWordUtils.changWord(inputUrl, data, null);
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
}
