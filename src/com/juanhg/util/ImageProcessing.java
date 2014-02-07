package com.juanhg.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageProcessing {

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
}
