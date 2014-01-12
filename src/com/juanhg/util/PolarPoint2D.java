package com.juanhg.util;

import java.awt.geom.Point2D;

/**
 * Allows to work with Polar coordinates,
 * and make easier the transformations between 
 * Cartesian & polar Coordinates
 * 
 * @author "Juan Hernandez Garcia"
 *
 */

public class PolarPoint2D {
	
	private double radius;
	private double phi;
	
	/** CONSTRUCTORS **/
	
	public PolarPoint2D(){
		radius = 0;
		phi = 0;
	}
	
	public PolarPoint2D(double radius, double phi){
		this.radius = radius;
		this.phi = phi;
	}
	
	/**
	 * Transforms the Polar Point in a cartesianPoint
	 * @return A Point2D that is the point represented in Cartesian coordinates
	 */
	public Point2D toCartesianPoint(){
		Point2D cartesian = new Point2D.Double(radius * Math.cos(phi), radius * Math.sin(phi));
		return cartesian;
	}
	
	/**
	 * Transforms the Polar Point given by the parameters in a cartesianPoint
	 * @return A Point2D that is the point represented in Cartesian coordinates
	 */
	public static Point2D polarToCartesian(double radius, double phi){
		return (new PolarPoint2D(radius, phi)).toCartesianPoint();
	}
		
	/**
	 * Transforms the Cartesian Coordinates in a PolarPoint2D
	 * @return A Point2D that is the point represented in Cartesian coordinates
	 */
	public static PolarPoint2D cartesianToPolar(double x, double y){
		double radius = Math.sqrt(x*x+y*y);
		double phi = Math.atan2(y,x);
		return new PolarPoint2D(radius,phi);
	}
	
	/**
	 * Transforms the Point2D in a PolarPoint2D
	 * @return A Point2D that is the point represented in Cartesian coordinates
	 */
	public static PolarPoint2D cartesianToPolar(Point2D cartesian){
		return cartesianToPolar(cartesian.getX(), cartesian.getY());
	}
	
	/** GETTERS & SETTERS **/
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getPhi() {
		return phi;
	}

	public void setPhi(double phi) {
		this.phi = phi;
	}
}
