/*  -----------------------------------------------------------------
 	 @file   AngularMomentumModel.java
     @author Juan Hernandez Garcia 
 	-----------------------------------------------------------------	
    Copyright (C) 2014  Modesto Modesto T Lopez-Lopez
    					Francisco Nogueras Lara
    					Juan Hernandez Garcia



						University of Granada
	--------------------------------------------------------------------					
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.juanhg.triangle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.juanhg.model.Model;
import com.juanhg.util.Time;

/**
 *   @file   AngularMomentumModel.java
 *   @author Juan Hernandez Garcia 
 *   @brief Model that represents the loss of angular momentum 
 */
public class TriangleModel extends Model {

	private final int CILINDRO = 1;
	private final int ESFERA = 2;
	private final int CUBO = 3;

	private double dt = 0.01;
	private double currentTime = 0;

	//Constants
	final double r = 0.5;
	final double l = 0.5;
	final double M = 20;
	final double g = 9.5;
	final double A = 5;
	

	//Input parameters
	private double mue, mud, m, z;
	private int type;
	private double W, V, T, tfinal, x, y, X, Y, xf, phi,a;

	//Calculated parameters
	boolean movement = false;
	double I;
	double B, C;
	
	//Aux
	double temp1, temp2, temp3, temp4, temp5;


	public TriangleModel(double mue, double mud, double m, double z, int type){ 
		this.mue = mue;
		this.mud = mud;
		this.m = m;
		this.z = 2.0*Math.PI*z/360.0;
		this.type = type;

		if(m*g*Math.sin(z) - mue*m*g*Math.cos(z) > 0){
			movement = true;
		}
		
		switch(type){
		case CILINDRO:
			I = M*Math.pow(r, 2.0)/2.0;
			break;
		case ESFERA:
			I = (2.0/5.0)*M*r;
			break;
		case CUBO:
			I = M*Math.pow(l, 2.0)/6.0;
			break;
		}

		C = A/Math.cos(this.z);
		xf = C-1;
		B = C*Math.sin(this.z);
		
		a = (1/(m + (I/Math.pow(r, 2.0))))*(m*g*Math.sin(z) - mud*m*g*Math.cos(z));
		T = I*a/Math.pow(r, 2.0);
	}


	public double getPhi() {
		return phi;
	}


	/**
	 * Actualizes the simulation according the actual time
	 */
	@Override
	public void simulate() {
		
		if(X < xf){
			V = a*currentTime;
			phi = (a/(2.0*r))*Math.pow(currentTime, 2.0);
			W = V/r;
		
			x = a*Math.pow(currentTime, 2.0)/2.0;
			X = x*Math.cos(z);
			Y = B + (l/2.0)*(Math.cos(z) - Math.sin(z)) - x*Math.sin(z);
			
			incrementTime();
		}
		else{
			X = xf;
			V = a*currentTime;
			phi = (a/(2.0*r))*Math.pow(currentTime, 2.0);
			W = V/r;
		
			x = a*Math.pow(currentTime, 2.0)/2.0;
			X = x*Math.cos(z);
			Y = B + (l/2.0)*(Math.cos(z) - Math.sin(z)) - x*Math.sin(z);
			tfinal = currentTime;
		}
		
	}
	
	public double getX() {
		return X;
	}


	public double getY() {
		return Y;
	}


	public void incrementTime(){
		currentTime += dt;
	}

	/** GETTERS & SETTERS **/
	
	public double getA() {
		return A;
	}

	public double getB() {
		return B;
	}

	public double getC() {
		return C;
	}
	
	public double getW() {
		return W;
	}


	public double getV() {
		return V;
	}


	public double getT() {
		return T;
	}


	public double getTfinal() {
		return tfinal;
	}


	public double getA(double z){
		return getA();
	}
	
	public double getC(double z){
		z = 2.0*Math.PI*z/360.0;
		return A/Math.cos(z);
	}
	
	public void calculatePhi(){
		phi = (a/(2.0*r))*Math.pow(currentTime, 2.0);
	}
	
	public void calculateW(){
		W = V/r;
	}
	
	public double getB(double z){
		double C = getC(z);
		xf = C-1;
		z = 2.0*Math.PI*z/360.0;
		return C*Math.sin(z);
	}
	
}