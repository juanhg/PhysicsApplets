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
	
	final double X0Box = 65;
	final double Y0Box = 13;
	final double X0Person = 160;
	final double Y0Person = 223;
	final double X0Pulley = 65;
	final double Y0Pulley = 230;
	final double X0Energy = 33.7;
	final double Y0Energy = 313.0;
	final double X0Base = 83;
	final double Y0Base = 191;
	final double XMaxEnergy = 370;
	final double maxEnergy = 800;
	
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
	double resetX = 0;
	
	
	//Input parameters
	private double F;
	private double mu;
	private double mud;
	
	//Calculated parameters
	private double lastPhaseX, lastPhaseV, lastX, x, v;
	double phiPerson;
	private double Froz, Mt, Wo, phi, eta;
	private double A, B, C, D, E, J, h;
	private double x3Max;
	private double y13Modifier = 0;
	private double Eo;
	private double Et = 0;
	
	public IceWalkerModel(double F, double mu, double mud){ 
	
		double temp1, temp2, temp3;
		
		t = new Time();
		currentPhase = 1;
		previousPhase = 0;
		
		lastPhaseX = lastPhaseV = lastX = x = v = 0;
		
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
	
		h = deep - L;
		
		Wo = Math.sqrt(B/Mt);
		phi =  Math.atan((-1/Wo)*Math.sqrt(A/(2*Mt*h)));
		
		temp1 = 1/Wo;
		temp2 = Math.sqrt((2*h*A)/Mt);
		temp3 = 1/(Math.cos(Math.atan((-temp1)*Math.sqrt(A/(2*Mt*h)))));
		eta = temp1*temp2*temp3;
		
		x3Max = eta + D;
		Et = Eo = 0;
	}
	
	public void reset(){
		jumpToPhase(PHASE_1);
		resetX = x;
		x = 0;
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
			if(F < Froz && A > 0 && Eo > 0){
				jumpToPhase(PHASE_2);
			}
			else{
				jumpToPhase(PHASE_11);
			}
			break;
		//Hay movimiento ascendente
		case PHASE_2:
			
			x = (A*Math.pow(t, 2))/(Mt*2);
			v = (x - lastX)/(t - lastT);
			Et = Eo - (F*x);
			lastT = t;
			
			if(Et <= 0){
				Et = 0;
				v = 0;
				jumpToPhase(PHASE_5);
			}
			else if(x >= h){
				jumpToPhase(PHASE_3);
			}
			break;
		//Interfaz agua-aire	
		case PHASE_3:	
			x = eta*Math.sin(Wo*t + phi) + D + lastPhaseX;
			v = (x - lastX)/(t - lastT);
			lastT = t;
			Et = Eo - (F*x);
			if(Et <= 0){
				Et = 0;
				v = 0;
				jumpToPhase(PHASE_4);
			}		
			else if(x3Max > (h+L)){
				if(x >= h+L){
					jumpToPhase(PHASE_7);
				}
			}
			else{
				if(x >= x3Max){
					//Esto es un ajuste para los graficos
//					x3 = x3Max;
					jumpToPhase(PHASE_4);
				}
			}
			break;
		//No consigue salir, comienza a hundirse
		case PHASE_4:
			x = h + ((1/B)*(E - E*Math.cos(Math.sqrt(B/Mt)*t) + B*(lastPhaseX-h)*Math.cos(Math.sqrt(B/Mt)*t)));
			v = (x - lastX)/(t - lastT);
			lastT = t;
			
//			System.out.println("X4: " + x4);
			
			if(x <= h){
				jumpToPhase(PHASE_5);
			}
			break;
		//Está sumergido por completo, sigue bajando
		case PHASE_5:
			
			x = ((E*Math.pow(t, 2.0))/(Mt*2)) + lastPhaseV*t + lastPhaseX;
			v = (x - lastX)/(t - lastT);
			lastT = t;
			
			
			if(x <= 0){
				x = 0;
				jumpToPhase(PHASE_6);
			}
			break;
		//Ya ha tocado el suelo. El esquimal sigue con la incercia
		case PHASE_6:
			x = (mud*g*Math.pow(t, 2.0)/2) + lastPhaseV*t + lastPhaseX;
			v = mud*g*t + lastPhaseV;
			lastT = t;
						
			if(x <= (xfall - resetX)){
				jumpToPhase(PHASE_13);
			}
			else if(v >= 0){
				jumpToPhase(PHASE_11);			
			}
			break;
		//Sí consigue salir de la interfaz aire agua
		case PHASE_7:
			x = ((C*Math.pow(t, 2.0))/(Mt * 2.0)) 
			+ eta*Wo*t*Math.cos(Math.asin((L-D)/eta)) 
			+ lastPhaseX;
		
			v = (x - lastX)/(t - lastT); 
			lastT = t;
			
			Et = Eo - (F*x);
			if(Et <= 0){
				Et = 0;
				v = 0;
				jumpToPhase(PHASE_5);
			}
			else if(x < lastX){
				jumpToPhase(PHASE_12);
			}
			else if(x >= yTopLimit){
				jumpToPhase(PHASE_11);			}
			break;
		case PHASE_12:
			x = ((-m*g*Math.pow(t, 2.0))/(Mt*2)) + lastPhaseX;
			v = (x - lastX)/(t - lastT);
			lastT = t;
			
			if(x <= h+L){
				jumpToPhase(PHASE_8);
			}
			break;
		//Vuelve  a la interfaz
		case PHASE_8:
			double phi8 = Math.atan((-E)/(Mt*Wo*lastPhaseV));
			double eta8 = lastPhaseV/(Wo*Math.cos(phi8));
			
			x = (eta8*Math.sin(Wo*t + phi8) + J + lastPhaseX) + h;
			v = (x - lastX)/(t - lastT);
			lastT = t;
			
			if(x <= h){
				jumpToPhase(PHASE_5);
			}
			break;
		case PHASE_13:
			phiPerson = ((lastPhaseV/r)*t)*4;
			y13Modifier =  (phiPerson/(Math.PI*2.0))*xFallFix;
			x = y13Modifier + lastPhaseX;
			v = (x - lastX)/(t - lastT);
			if(phiPerson <= (-Math.PI/2.0)){
				jumpToPhase(PHASE_14);
			}
			break;
		case PHASE_14:
			x = ((-M*g*Math.pow(t, 2.0))/2.0) + lastPhaseV*t + deepIce;
			v = (x - lastX)/(t - lastT);
			if(x <= h+L){
				jumpToPhase(PHASE_11);
			}
			break;
		}
		if(v < 0){
			System.out.println();
		}
		lastX = x;
	}
	
	
	
	
	/** GETTERS & SETTERS **/
	

	
	public double getActualTime() {
		return actualTime;
	}

	public void jumpToPhase(int phase) {
		lastPhaseX = x;
		lastPhaseV = v;
		
		this.previousPhase = this.currentPhase;
		this.currentPhase = phase;
		lastT = 0;
	
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
		
		double boxX, boxY;
		
		switch(currentPhase){
		case PHASE_11:
			//Caja arriba
			if(previousPhase == PHASE_7){
				boxX = X0Box;
				boxY = Y0Box + yTopLimit*100;
			}
			//Caja tocando el suelo
			else{
				boxX = X0Box;
				boxY = Y0Box;
			}
			break;
		//Caja tocando el suelo;
		case PHASE_6:
		case PHASE_13:
		case PHASE_14:
			boxX = X0Box;
			boxY = Y0Box;
			break;
		default:
			boxX = X0Box;
			boxY = Y0Box + x*100;
			break;
		}
		
		return new Point2D.Double(boxX,boxY);
	}
	
	public Point2D getPersonPoint(){
		double personX = 0;
		double personY = 0;
		
		switch(currentPhase){
		case PHASE_14:
			personX = X0Person + ((xfall-resetX)-xFallFix/3)*100;
			personY = x*100;
			break;
		case PHASE_13:
			personX = X0Person + x*100;
			personY = Y0Person + y13Modifier*100;
			break;
		case PHASE_11:
			if(previousPhase == PHASE_14){
				personX = X0Person + ((xfall-resetX)-xFallFix/2.9)*100;
				personY = (h+L)*100;
			}
			else{
				personX = X0Person + x*100;
				personY = Y0Person;
			}
			break;
		default:
			personX = X0Person + x*100;
			personY = Y0Person;
			break;
		}
		
		personX += resetX*100;
		return new Point2D.Double(personX,personY);
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
	
	public Point2D getBase(){
		return new Point2D.Double(X0Base, Y0Base);
	}
	
	public boolean remainEnergy(){
		if(Et > 0){
			return true;
		}
		return false;
	}
	
	public Point2D [] getEnergy(){
		Point2D [] energy = new Point2D[2];
		double aux;
		
		aux = Et*((XMaxEnergy-X0Energy)/maxEnergy) + X0Energy;
		
		energy[0] = new Point2D.Double(X0Energy, Y0Energy);
		energy[1] = new Point2D.Double (aux, Y0Energy);
		
		return energy;
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
	
	public void eatBanana(){
		Et += ((maxEnergy*0.5) - 35);
		if(Et > maxEnergy){
			Et = maxEnergy;
		}
		Eo = Et;
	}
	
	public void eatBurger(){
		Et += maxEnergy;
		if(Et > maxEnergy){
			Et = maxEnergy;
		}
		Eo = Et;
	}
	
	public void eatCookie(){
		Et += ((maxEnergy*0.75)-15);
		if(Et > maxEnergy){
			Et = maxEnergy;
		}
		Eo = Et;
	}
	
	public void eatCarrot(){
		Et += ((maxEnergy*0.25) - 35);
		if(Et > maxEnergy){
			Et = maxEnergy;
		}
		Eo = Et;
	}
	
	public boolean readyToEat(){
		switch(currentPhase){
		case PHASE_11:
			switch(previousPhase){
			case PHASE_1:
			case PHASE_6:
				return true;
			}
			break;
		}
		return false;
	}
	
	public double getEnergyValue(){
		return Et;
	}
	
	public double getV(){
		switch(currentPhase){
		case PHASE_11:
		case PHASE_13:
		case PHASE_14:
			return 0;
		default:
			return v;
		}
	}
}