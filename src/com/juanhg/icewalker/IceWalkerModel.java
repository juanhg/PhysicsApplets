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


package com.juanhg.icewalker;

import java.awt.geom.Point2D;

import com.juanhg.model.Model;
import com.juanhg.util.Time;

/**
 *   @file   AngularMomentumModel.java
 *   @author Juan Hernandez Garcia 
 *   @brief Model that represents the loss of angular momentum 
 */
public class IceWalkerModel extends Model {
	
	//Stages or phases of the model
	static final int PHASE_0 = 0;
	static final int PHASE_1 = 1;
	static final int PHASE_2 = 2;
	static final int PHASE_3 = 3;
	static final int PHASE_4 = 4;
	static final int PHASE_5 = 5;
	static final int PHASE_6 = 6;
	static final int PHASE_7 = 7;
	static final int PHASE_8 = 8;
	static final int PHASE_9 = 9;
	static final int PHASE_10 = 10;
	static final int PHASE_11 = 11;
	
	final double X0Box = 62.5;
	final double Y0Box = 15;
	final double X0Person = 160;
	final double Y0Person = 223;
	
	private int phase;
	private Time t;
	double lastT = 0.0;
	
	//Constants
	private final double M = 60.0;
	private final double m = 10.0;
	private final double L = 0.25;
	private final double Ro = 1000;
	private final double g = 9.8;
	private final double deep = 1.20;
	
	
	//Input parameters
	private double F;
	private double mu;
	private double mud;
	
	//Calculated parameters
	private double x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11;
	private double oldX4, oldX5, oldX7, oldX8, oldX9, oldX10;
	private double v4, v5, v6, v7, v8, v9, v10;
	private double Froz, Mt, Wo, phi, eta;
	private double A, B, C, D, E, J, h;
	private double x3Max, xTopLimit;
	
	
	public IceWalkerModel(double F, double mu, double mud){ 
	
		double temp1, temp2, temp3;
		
		t = new Time();
		phase = 1;
		
		x1 = x2 = x3 = x4 = x5 = x6 = x7 = x8 = x9 = x10 = 0;
		
		this.F = F;
		this.mu = mu;
		this.mud = mud;
		
		Mt = M+m;
		Froz = this.mu*M*g;
		A = (Math.pow(L, 3)*Ro*g) + F - (m*g);
		B = Math.pow(L, 2.0)*Ro*g;
		C = this.F - m*g;
		D = A/B;
		E = this.mud*M*g + Math.pow(L, 3)*Ro*g - m*g;
		J = E/B;
		
		//TODO asignar valor a XtopLimit
		xTopLimit = 2.0;
		h = deep - L;
		
		Wo = Math.sqrt(B/Mt);
		phi =  Math.atan((-1/Wo)*Math.sqrt(A/(2*Mt*h)));
		
		temp1 = 1/Wo;
		temp2 = Math.sqrt((2*h*A)/Mt);
		temp3 = 1/(Math.cos(Math.atan((-temp1)*Math.sqrt(A/(2*Mt*h)))));
		eta = temp1*temp2*temp3;
		
		x3Max = eta + D;
		
	}
	
	
	
	/**
	 * Actualizes the simulation according the actual time
	 */
	@Override
	public void simulate() {
		
		//TODO simulacion
		double t = ((double)this.t.getTime())/1000.0;
	
			
		switch(phase){
		//Estado Inicial
		case PHASE_1:
			lastT = t;
			if(F < Froz && A > 0){
				jumpToPhase(PHASE_2);
			}
			else{
				jumpToFinalPhase();
			}
			break;
		//Hay movimiento ascendente
		case PHASE_2:
			lastT = t;
			x2 = (A*Math.pow(t, 2))/(Mt*2);
			if(x2 >= h){
				jumpToPhase(PHASE_3);
			}
			break;
		//Interfaz agua-aire	
		case PHASE_3:	
			lastT = t;
			x3 = eta*Math.sin(Wo*t + phi) + D + x2;
			if(x3Max > (h+L)){
				if(x3 >= h+L){
					jumpToPhase(PHASE_7);
				}
			}
			else{
				if(x3 >= x3Max){
					jumpToPhase(PHASE_4);
				}
			}
			break;
		//No consigue salir, comienza a hundirse
		case PHASE_4:
			x4 = h + ((1/B)*(E - E*Math.cos(Math.sqrt(B/Mt)*t) + B*x3*Math.cos(Math.sqrt(B/Mt)*x3)));
			v4 = Math.abs(x4 - oldX4)/(t - lastT);
			lastT = t;
			
			if(x4 <= h){
				jumpToPhase(PHASE_5);
			}
			
			oldX4 = x4;
			break;
		//Está sumergido por completo, sigue bajando
		case PHASE_5:
			x5 = ((E*Math.pow(t, 2.0))/(Mt*2)) + v4*t + x4;
			v5 = Math.abs(x5 - oldX5)/(t - lastT);
			lastT = t;
			
			if(x5 <= 0){
				jumpToPhase(PHASE_6);
			}
			
			oldX5 = x5;
			break;
		//Ya ha tocado el suelo. El esquimal sigue con la incercia
		case PHASE_6:
			x6 = (mud*g*Math.pow(t, 2.0)/2) - v5*t + x5;
			v6 = mud*g*t - v5;
			lastT = t;
			
			if(v6 <= 0){
				jumpToFinalPhase();
			}
			break;
		//Sí consigue salir de la interfaz aire agua
		case PHASE_7:
			x7 = ((C*Math.pow(t, 2.0))/(Mt * 2.0)) 
			+ eta*Wo*t*Math.cos(Math.asin((L-D)/eta)) 
			+ x3;

			if(x7 > 50){
				System.out.print("Pero qué...");
			}
			System.out.print("X7: " + x7);
		
			v7 = (x7 -oldX7)/(t - lastT); 
			lastT = t;
			
			if(x7 < oldX7){
				jumpToPhase(PHASE_8);
			}
			else if(x7 >= xTopLimit){
				jumpToFinalPhase();
			}

			oldX7 = x7;
			break;
		//Vuelve  a la interfaz
		case PHASE_8:
			double phi8 = Math.atan((-E)/(Mt*Wo*v7));
			double eta8 = v7/(Wo*Math.cos(phi8));
			
			x8 = (eta8*Math.sin(Wo*t + phi8) + J + x7) + h;
			v8 = Math.abs(x8 - oldX8)/(t - lastT);
			lastT = t;
			
			if(x8 <= h){
				jumpToPhase(PHASE_9);
			}
			
			oldX8 = x8;
			break;
		//Esta sumergido por completo descendiendo
		case PHASE_9:
			x9 = ((E*Math.pow(t, 2.0))/(Mt*2) + v8*t + x8);
			lastT = t;
			v9 = Math.abs(x9 - oldX9)/(t - lastT);
			
			if(x9 <= 0){
				jumpToPhase(PHASE_10);
			}

			oldX9 = x9;
			break;
		//Ya ha tocado el suelo. El esquimal sigue con la incercia
		case PHASE_10:
			x10 = (mud*g*Math.pow(t, 2.0)/2) - v9*t + x9;
			v10 = mud*g*t - v9;
			lastT = t;
			
			if(v10 >= 0){
				jumpToFinalPhase();
			}
			break;
		}
	}
	
	
	
	
	/** GETTERS & SETTERS **/
	
	public double getX(){
		double x = 0;
		
		switch(phase){
		case PHASE_1:
			x = x1;
			break;
		case PHASE_2:
			x = x2;
			break;
		case PHASE_3:	
			x = x3;
			break;
		case PHASE_4:
			x = x4;
			break;
		case PHASE_5:
			x = x5;
			break;
		case PHASE_6:
			x = x6;
			break;
		case PHASE_7:
			x = x7;
			break;
		case PHASE_8:
			x = x8;
			break;
		case PHASE_9:
			x = x9;
			break;
		case PHASE_10:
			x = x10;
			break;
		case PHASE_11:
			x = x11;
			break;
		}
		
		return x;
	}

	
	public double getActualTime() {
		return actualTime;
	}

	public void jumpToPhase(int phase) {
		double lastX = getX();
		this.phase = phase;
		lastT = t.getTime();
		
		switch(phase){
		case PHASE_3:
			x3 = lastX;
			break;
		case PHASE_4:
			x4 = lastX;
			oldX4 = lastX;
			break;
		case PHASE_5:
			oldX5 = lastX;
			break;
		case PHASE_7:
			oldX7 = lastX;
			x7 = lastX;
			break;
		case PHASE_8:
			oldX8 = lastX;
			x8 = lastX;
			break;
		case PHASE_9:
			oldX9 = lastX;
			x9 = lastX;
			break;
		case PHASE_10:
			oldX10 = lastX;
			x10 = lastX;
			break;
		case PHASE_11:
			x11 = lastX;
			break;
		}
		t = new Time();
	}
	
	/**
	 * Jump to the final phase
	 */
	private void jumpToFinalPhase(){
		x11 = getX();
		this.phase = PHASE_11;
	}

	public int getPhase() {
		return phase;
	}

	public Time getT() {
		return t;
	}

	@Override
	public boolean finalTimeReached() {
		return false;
	}	
	
	public Point2D getBoxPoint(){
		double x = X0Box;
		double y = Y0Box + getX()*100;
		
		return new Point2D.Double(x,y);
	}
	
	public Point2D getPersonPoint(){
		double x = X0Person + getX()*100;
		double y = Y0Person;
		
		return new Point2D.Double(x,y);
	}
}