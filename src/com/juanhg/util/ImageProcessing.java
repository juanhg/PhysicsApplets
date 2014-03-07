package com.juanhg.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageProcessing {
	
	/**
	 * Load a image in the especified path
	 * @param fileName Absolute or relative path to the image
	 * @return BufferedImage that contains the image
	 */
	public BufferedImage loadImage(String fileName){

		BufferedImage buff = null;
		try {
			buff = ImageIO.read(getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return buff;
	}

	/**
	 * Rotate an square image (degrees).
	 * @Pre The BufferedImage must have equal width & height
	 * @param image Image to be rotated
	 * @param degrees Degrees to rotate.
	 * @Post If BufferedImage is not a perfect square, the image returned 
	 * could have different size than the original one
	 * @return A new BufferedImage rotate respect the original
	 */
	public static BufferedImage rotateDegrees(BufferedImage image, double degrees){
		return rotateRadians(image, Math.toRadians(degrees));
	}
	
	/**
	 * Rotate an square image (radians).
	 * @Pre The BufferedImage must have equal width & height
	 * @param image Image to be rotated
	 * @param radians Radians to rotate.
	 * @Post If BufferedImage is not a perfect square, the image returned 
	 * could have different size than the original one
	 * @return A new BufferedImage rotate respect the original
	 */
	public static BufferedImage rotateRadians(BufferedImage image, double radians){
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(-radians, w/2, h/2);
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
	    
	    return (op.filter(image, null)).getSubimage(0, 0, w, h);
	}
	
	/**
	 * Rotate an square image (radians).
	 * @Pre The BufferedImage must have equal width & height
	 * @param image Image to be rotated
	 * @param radians Radians to rotate.
	 * @Post If BufferedImage is not a perfect square, the image returned 
	 * could have different size than the original one
	 * @return A new BufferedImage rotate respect the original
	 */
	public static BufferedImage rotateRadians2(BufferedImage image, double radians){
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(-radians, w/4, h);
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);
	    
	    return (op.filter(image, null));
	}
}
