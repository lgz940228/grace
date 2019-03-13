package com.lgz.grace.api.utils.other;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * Created by lgz on 2018/6/27.
 */
public class QrcodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(QrcodeUtil.class);
    /**
     * 生成包含字符串信息的二维码图片
     * @param content      二维码携带信息
     * @param qrCodeSize   二维码图片大小
     * @param qrCodeMarginSize
     * @throws WriterException
     * @throws IOException
     */
    public static BufferedImage createQrCode(String content, Integer qrCodeSize,Integer qrCodeMarginSize) throws WriterException, IOException {
        //设置二维码纠错级别ＭＡＰ
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);  // 矫错级别
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //创建比特矩阵(位矩阵)的QR码编码的字符串
        BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
        // 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth - qrCodeMarginSize, matrixWidth - qrCodeMarginSize, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // 使用比特矩阵画并保存图像
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i - qrCodeMarginSize/2, j - qrCodeMarginSize/2, 1, 1);
                }
            }
        }
        //URL url = new URL("http://statictest.maimaiche.com/wap/static/images/favicon96_f0410ef.png");
        String fontPath = QrcodeUtil.class.getResource("/").getPath()+"template"+ File.separator+"timg.jpg";
        int width = image.getWidth();
        int height = image.getHeight();
            // 构建绘图对象
            Graphics2D g = image.createGraphics();
            // 读取Logo图片
            BufferedImage logo = ImageIO.read(new File(fontPath));
            // 开始绘制logo图片
            g.drawImage(logo, width * 2 / 5, height * 2 / 5, width * 2 / 10, height * 2 / 10, null);
            g.dispose();
            logo.flush();
        return image;
    }

    /**
     * 读二维码并输出携带的信息
     */
    public static Result readQrCode(InputStream inputStream) throws IOException {
        //从输入流中获取字符串信息
        BufferedImage image = ImageIO.read(inputStream);
        //将图像转换为二进制位图源
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result = null;
        try {
            result = reader.decode(bitmap);
        } catch (ReaderException e) {
            e.printStackTrace();
        }
        //System.out.println(result.getText());
        return result;
    }

    /**
     * 测试代码
     *
     * @throws WriterException
     */
    public static void main(String[] args) throws IOException, WriterException {

        BufferedImage qrCode = createQrCode("http://www.maimaiche.com", 300,30);
        ImageIO.write(qrCode, "JPEG", new File("d:\\qrcode.jpg"));
        //readQrCode(new FileInputStream(new File("d:\\qrcode.jpg")));
       /* String fontPath = QrcodeUtil.class.getResource("/").getPath()+"template"+ File.separator+"favicon.ico";
        System.out.println(fontPath);*/

    }
}
