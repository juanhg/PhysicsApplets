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
import java.awt.geom.Point2D.Double;

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
	static final int PHASE_12 = 12;
	static final int PHASE_13 = 13;
	static final int PHASE_14 = 14;
	
	final double X0Box = 25;
	final double Y0Box = 13;
	final double X0Person = 160;
	final double Y0Person = 223;
	final double X0Pulley = 25;
	final double Y0Pulley = 246;
	
	private int currentPhase;
	private int previousPhase;
	private Time t;
	double lastT = 0.0;
	
	//Constants
	private final double M = 60.0;
	private final double m = 25.0;
	private final double L = 0.25;
	private final double Ro = 1000;
	private final double g = 9.8;
	private final double deep = 1.20;
	private final double yTopLimit = 2.0;
	private final double xfall = -0.70;
	private final double r = 0.6;
	private final double deepIce = 1.8;
	private final double xFallFix = 0.90;
	
	
	//Input parameters
	private double F;
	private double mu;
	private double mud;
	
	//Calculated parameters
	private double x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14;
	private double oldX4, oldX5, oldX6, oldX7, oldX8, oldX9, oldX10, oldX12, oldX13, oldX14;
	double phiPerson;
	private double v4, v5, v6, v7, v8, v9, v10, v12;
	private double Froz, Mt, Wo, phi, eta;
	private double A, B, C, D, E, J, h;
	private double x3Max;
	private double y13Modifier = 0;
	
	public IceWalkerModel(double F, double mu, double mud){ 
	
		double temp1, temp2, temp3;
		
		t = new Time();
		currentPhase = 1;
		previousPhase = 0;
		
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
	
			
		switch(currentPhase){
		//Estado Inicial
		case PHASE_1:
			lastT = t;
			if(F < Froz && A > 0){
				jumpToPhase(PHASE_2);
			}
			else{
				jumpToPhase(PHASE_11);
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
			
			System.out.println("X3: " + x3);
			
			if(x3Max > (h+L)){
				if(x3 >= h+L){
					jumpToPhase(PHASE_7);
				}
			}
			else{
				if(x3 >= x3Max){
					//Esto es un ajuste para los graficos
//					x3 = x3Max;
					jumpToPhase(PHASE_4);
				}
			}
			break;
		//No consigue salir, comienza a hundirse
		case PHASE_4:
			x4 = h + ((1/B)*(E - E*Math.cos(Math.sqrt(B/Mt)*t) + B*(x3-h)*Math.cos(Math.sqrt(B/Mt)*t)));
			v4 = (x4 - oldX4)/(t - lastT);
			lastT = t;
			
//			System.out.println("X4: " + x4);
			
			if(x4 <= h){
				jumpToPhase(PHASE_5);
			}
			
			oldX4 = x4;
			break;
		//Está sumergido por completo, sigue bajando
		case PHASE_5:
			double vUtil = 0; 
			double xUtil = 0;
			
			if(previousPhase == PHASE_4){
				vUtil = v4;
				xUtil = x4;
			}
			else if(previousPhase == PHASE_8){
				vUtil = v8;
				xUtil = x8;
			}
			else{
				System.err.println("Pasas a fase 5 desde fase " + currentPhase);
				System.exit(5);
			}
			
			x5 = ((E*Math.pow(t, 2.0))/(Mt*2)) + vUtil*t + xUtil;
			v5 = (x5 - oldX5)/(t - lastT);
			lastT = t;
			
			
			if(x5 <= 0){
				x5 = 0;
				jumpToPhase(PHASE_6);
			}
			
			oldX5 = x5;
			break;
		//Ya ha tocado el suelo. El esquimal sigue con la incercia
		case PHASE_6:
			x6 = (mud*g*Math.pow(t, 2.0)/2) + v5*t + x5;
			v6 = mud*g*t + v5;
			lastT = t;
						
			System.out.print("V:" + v6);
			if(x6 <= xfall){
				jumpToPhase(PHASE_13);
			}
			else if(v6 >= 0){
				jumpToPhase(PHASE_11);			
			}
			break;
		//Sí consigue salir de la interfaz aire agua
		case PHASE_7:
			x7 = ((C*Math.pow(t, 2.0))/(Mt * 2.0)) 
			+ eta*Wo*t*Math.cos(Math.asin((L-D)/eta)) 
			+ x3;

			System.out.print("X7: " + x7);
		
			v7 = (x7 -oldX7)/(t - lastT); 
			lastT = t;
			
			if(x7 < oldX7){
				jumpToPhase(PHASE_12);
			}
			else if(x7 >= yTopLimit){
				jumpToPhase(PHASE_11);			}

			oldX7 = x7;
			break;
		case PHASE_12:
			x12 = ((-m*g*Math.pow(t, 2.0))/(Mt*2)) + x7;
			v12 = (x12 -oldX12)/(t - lastT);
			lastT = t;
			
			if(x12 <= h+L){
				jumpToPhase(PHASE_8);
			}
			break;
		//Vuelve  a la interfaz
		case PHASE_8:
			double phi8 = Math.atan((-E)/(Mt*Wo*v12));
			double eta8 = v12/(Wo*Math.cos(phi8));
			
			x8 = (eta8*Math.sin(Wo*t + phi8) + J + x12) + h;
			v8 = (x8 - oldX8)/(t - lastT);
			lastT = t;
			
			if(x8 <= h){
				jumpToPhase(PHASE_5);
			}
			
			oldX8 = x8;
			break;
		case PHASE_13:
			phiPerson = ((v6/r)*t)*4;
			y13Modifier =  (phiPerson/(Math.PI*2.0))*xFallFix;
			x13 = y13Modifier + x6;
			if(phiPerson <= (-Math.PI/2.0)){
				jumpToPhase(PHASE_14);
			}
			break;
		case PHASE_14:
			x14 = ((-M*g*Math.pow(t, 2.0))/2.0) + v6*t + deepIce;
			if(x14 <= h+L){
				jumpToPhase(PHASE_11);
			}
			break;
		}
		
//		System.out.println("Fase:" + phase);
//		System.out.println("X:" + getX());
		
	}
	
	
	
	
	/** GETTERS & SETTERS **/
	
	public double getX(){
		double x = 0;
		
		switch(currentPhase){
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
		case PHASE_12:
			x = x12;
			break;
		case PHASE_13:
			x = x13;
			break;
		case PHASE_14:
			x = x14;
			break;
		}
		
		return x;
	}

	
	public double getActualTime() {
		return actualTime;
	}

	public void jumpToPhase(int phase) {
		double lastX = getX();
		this.previousPhase = this.currentPhase;
		this.currentPhase = phase;
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
			x5 = lastX;
			oldX5 = lastX;
			break;
		case PHASE_6:
			x6 = lastX;
			oldX6 = lastX;
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
		case PHASE_12:
			x12 = lastX;
			oldX12 = lastX;
			break;
		case PHASE_13:
			x13 = lastX;
			oldX13 = lastX;
			break;
		case PHASE_14:
			x14 = Y0Person;
			oldX14 = Y0Person;
			break;
		}
		
		
		t = new Time();
	}


	public int getPhase() {
		return currentPhase;
	}

	public Time getT() {
		return t;
	}

	@Override
	public boolean finalTimeReached() {
		return false;
	}	
	
	public Point2D getBoxPoint(){
		
		double x, y;
		
		switch(currentPhase){
		case PHASE_11:
			//Caja arriba
			if(previousPhase == PHASE_7){
				x = X0Box;
				y = Y0Box + yTopLimit*100;
			}
			//Caja tocando el suelo
			else{
				x = X0Box;
				y = Y0Box;
			}
			break;
		//Caja tocando el suelo;
		case PHASE_6:
		case PHASE_13:
		case PHASE_14:
			x = X0Box;
			y = Y0Box;
			break;
		default:
			x = X0Box;
			y = Y0Box + getX()*100;
			break;
		}
		
		return new Point2D.Double(x,y);
	}
	
	public Point2D getPersonPoint(){
		double x = 0;
		double y = 0;
		
		switch(currentPhase){
		case PHASE_14:
			x = X0Person + (xfall-xFallFix/3)*100;
			y = getX()*100;
			break;
		case PHASE_13:
			x = X0Person + getX()*100;
			y = Y0Person + y13Modifier*100;
			break;
		case PHASE_11:
			if(previousPhase == PHASE_14){
				x = X0Person + (xfall-xFallFix/2.9)*100;
				y = (h+L)*100;
			}
			else{
				x = X0Person + getX()*100;
				y = Y0Person;
			}
			break;
		default:
			x = X0Person + getX()*100;
			y = Y0Person;
			break;
		}
		
		return new Point2D.Double(x,y);
	}
	
	public double getPhiPerson(){
		double phi = 0;
		
		switch(currentPhase){
		case PHASE_14:
		case PHASE_13:
			phi = -phiPerson;
			break;
		case PHASE_11:
			if(previousPhase == PHASE_14){
				phi = -phiPerson;
			}
			else{
				phi = 0;
			}
			break;
		default:
			phi = 0;
			break;
		}
		return phi;
	}
	
	public Point2D getPulleyPoint(){
		return new Point2D.Double(X0Pulley, Y0Pulley);
	}
	
	public Point2D [] getRopeToPerson(){
		Point2D [] ropeToPerson = new Point2D[2];
		ropeToPerson[0] = getPulleyPoint();
		
		switch(currentPhase){
		case PHASE_13:
		case PHASE_14:
			ropeToPerson[1] = getPulleyPoint();
			break;
		case PHASE_11:
			if(previousPhase == PHASE_14){
				ropeToPerson[1] = getPulleyPoint();
				break;
			}
			ropeToPerson[1] = getPersonPoint();
			ropeToPerson[1].setLocation(ropeToPerson[1].getX(), Y0Pulley);
			break;
		default:
			ropeToPerson[1] = getPersonPoint();
			ropeToPerson[1].setLocation(ropeToPerson[1].getX(), Y0Pulley);
			break;
		}
				
		return ropeToPerson;
	}
	
	public Point2D [] getRopeToBox(){
		Point2D [] ropeToBox = new Point2D[2];
		ropeToBox[0] = getPulleyPoint();
		ropeToBox[1] = getBoxPoint();
		
		return ropeToBox;
		
	}
	
	public boolean manInMovement(){
		switch(currentPhase){
		case PHASE_13:
		case PHASE_14:
		case PHASE_11:
			return false;
		default:
			return true;
		}
	}
}