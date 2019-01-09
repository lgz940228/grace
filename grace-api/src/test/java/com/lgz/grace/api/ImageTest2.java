package com.lgz.grace.api;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by lgz on 2019/1/9.
 */
public class ImageTest2 {
    public static void main(String[] args) throws Exception{

        BufferedImage image = ImageIO.read(new File("E:\\11.jpg"));
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)
        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        //g2.setComposite(AlphaComposite.SrcOut);
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST, 0.0f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //g2.setColor(new Color(0,0,0));
        //g2.setBackground(Color.black);
        //g2.setPaint(new Color(0,0,0));
        g2.fill(new RoundRectangle2D.Float(w * 2 / 5, h * 2 / 5, w * 2 / 10, h * 2 / 10,w/10,
                h/10));
        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
        g2.drawImage(image, w * 2 / 5, h * 2 / 5, w * 2 / 10, h * 2 / 10,null);
        g2.dispose();
        ImageIO.write(output, "png", new File("E:\\test2.png"));
    }
}
