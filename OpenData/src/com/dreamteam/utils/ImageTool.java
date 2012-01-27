package com.dreamteam.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Random;

import javax.swing.ImageIcon;

public class ImageTool {
	

    public static final int ALPHA = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int BLUE = 3;

    public static final int HUE = 0;
    public static final int SATURATION = 1;
    public static final int BRIGHTNESS = 2;

    public static final int TRANSPARENT = 0;

    
    public static void forceColor(BufferedImage image, int from,int to)
    {
    	for(int y=0;y<image.getHeight();y++)
    		for(int x=0;x<image.getWidth();x++)
    			if(image.getRGB(x, y)==from) image.setRGB(x, y, to);
//    			else image.setRGB(x, y, image.getRGB(x, y));
    	
    }
    
    public static void force(BufferedImage image, BufferedImage mapa)
    {
    	for(int y=0;y<image.getHeight();y++)
    		for(int x=0;x<image.getWidth();x++)
    			image.setRGB(x, y, mapa.getRGB(x, y));
//    			else image.setRGB(x, y, image.getRGB(x, y));
    	
    }

	public static BufferedImage cloneImage(BufferedImage mapa) {
		BufferedImage image = new BufferedImage(mapa.getWidth(), mapa.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int y=0;y<image.getHeight();y++)
    		for(int x=0;x<image.getWidth();x++)
    			 image.setRGB(x, y, mapa.getRGB(x, y));
		return image;
	}	
    
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
	
    public static BufferedImage changeColor(BufferedImage image, Color mask,
            Color replacement) {
        BufferedImage destImage = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = destImage.createGraphics();
        g.drawImage(image, null, 0, 0);
        g.dispose();
        
        for (int i = 0; i < destImage.getWidth(); i++) {
            for (int j = 0; j < destImage.getHeight(); j++) {

                int destRGB = destImage.getRGB(i, j);

                if (matches(mask.getRGB(), destRGB)) {
                    int rgbnew = getNewPixelRGB(replacement.getRGB(), destRGB);
                    destImage.setRGB(i, j, rgbnew);
                }
            }
        }

        return destImage;
    }

    private static int getNewPixelRGB(int replacement, int destRGB) {
        float[] destHSB = getHSBArray(destRGB);
        float[] replHSB = getHSBArray(replacement);

        int rgbnew = Color.HSBtoRGB(replHSB[HUE],
                replHSB[SATURATION], destHSB[BRIGHTNESS]);
        return rgbnew;
    }

    private static boolean matches(int maskRGB, int destRGB) {
        float[] hsbMask = getHSBArray(maskRGB);
        float[] hsbDest = getHSBArray(destRGB);

        if (hsbMask[HUE] == hsbDest[HUE]
                && hsbMask[SATURATION] == hsbDest[SATURATION]
                && getRGBArray(destRGB)[ALPHA] != TRANSPARENT) {

            return true;
        }
        return false;
    }
    
    public static Color getRandomColor() {
    			Random a = new Random();
    	      return new Color(a.nextInt(256), a.nextInt(256), a.nextInt(256));
    	   }


    public static BufferedImage resizeImage(BufferedImage originalImage, int type,int IMG_WIDTH,int IMG_HEIGHT)
	{
    	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
    	Graphics2D g = resizedImage.createGraphics();
    	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
    	g.dispose();	
    	g.setComposite(AlphaComposite.Src);
     
    	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
    	RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    	g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
    	RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
    	RenderingHints.VALUE_ANTIALIAS_ON);

	return resizedImage;
	}

 // This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see Determining If an Image Has Transparent Pixels
        boolean hasAlpha = true;

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }
    
    private static int[] getRGBArray(int rgb) {
        return new int[] { (rgb >> 24) & 0xff, (rgb >> 16) & 0xff,
                (rgb >> 8) & 0xff, rgb & 0xff };
    }

    private static float[] getHSBArray(int rgb) {
        int[] rgbArr = getRGBArray(rgb);
        return Color.RGBtoHSB(rgbArr[RED], rgbArr[GREEN], rgbArr[BLUE], null);
    }
}
