package com.juanhg.util;

public class Utilities {
	public static double normalize(double x, double a, double b, double c, double d){
		return (x*(d-c))/(b-a) + (c*b - a*d)/(b-a);
	}
}
