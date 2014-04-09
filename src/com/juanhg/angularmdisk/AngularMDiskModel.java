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


package com.juanhg.angularmdisk;

import com.juanhg.model.Model;
import com.juanhg.util.PolarPoint2D;
import com.juanhg.util.Time;

/**
 *   @file   AngularMomentumModel.java
 *   @author Juan Hernandez Garcia 
 *   @brief Model that represents the loss of angular momentum 
 */
public class AngularMDiskModel extends Model {
	
	//Stages or phases of the model
	static  final int PHASE_0 = 0;
	static final int PHASE_1 = 1;
	static final int PHASE_2 = 2;
	static final int PHASE_3 = 3;
	static final int PHASE_4 = 4;
	
	private int phase;
	private Time t;
	double lastT = 0.0;
	
	//Constants
	private final double M = 200;
	private final double R = 0.2;
	private final double g = 9.8;
	private final double durationPhase1 = 3;
	
	//Input parameters
	private double W; //Disk velocity
	private double v; //Bug velocity
	private double r0; //Fall radius
	private double m; //Bug mass
	private double mu; //Friction coefficient 
	
	//Calculated parameters
	private double phi0, phi1, phi2, phi3, phi4, phiAux;
	private double lastPhi = 0;
	double lastphi2 = 0.0;
	private double r; //Actual radius
	private double criticRadius; //Radius that indicate the phase 3
	private double a;
	private double Wf;
	private double WDisk;
	private double x,y;
	
	
	public AngularMDiskModel(double m, double r0, double v, double W, double mu){ 
	
		double temp1, temp2;
		
		this.m = m;
		this.r0 = r0;
		this.v = v;
		this.W = 2*Math.PI*W;
		this.mu = mu;
		
		this.phase = 0;
		this.t = new Time();
		
		
		this.r = r0;
		
		this.calculateRPhase3();
		
		temp1 = 0.5*M*Math.pow(R, 2.0);
		temp2 = m*Math.pow(r, 2.0);
		
		Wf = (temp1/(temp1 + temp2))*W;
		
		a = 0.5*(M/this.m)*(Math.pow(R, 2));
		
		
		
	}
	
	/**
	 * Calculate de R that shows the begin of phase3
	 */
	private void calculateRPhase3 (){
		double temp1, temp2, temp3, temp4, fx, fx1, xAux;
		double times = 50;
		double A;
		
		A = (2*m)/(M*Math.pow(R, 2));

		xAux = 1 + (A*Math.pow((mu*g),2))/Math.pow(W, 4);


		for(int i = 0; i < times; i++){
			temp1 = Math.pow(mu*g,2.0)*Math.pow(xAux, 4.0);
			temp2 = 16*Math.pow(a, 2.0)*Math.pow(W, 2)*Math.pow(v, 2.0)*Math.pow(xAux, 2.0);
			temp3 = xAux*Math.pow(W, 2)*(16*Math.pow(v, 2) - Math.pow(W, 2.0)/A);
			temp4 = Math.pow(W, 2.0)*(4*Math.pow(v, 2.0) - Math.pow(W, 2)/A);

			fx = temp1 - temp2 + temp3 - temp4;

			temp1 = 4*Math.pow(M*g, 2.0)*Math.pow(xAux, 3.0);
			temp2 = 32*Math.pow(W, 2.0)*Math.pow(v,2)*xAux;
			temp3 = Math.pow(W, 2)*(16*Math.pow(v, 2) - Math.pow(W, 2.0)/A);

			fx1 = temp1 - temp2 + temp3;

			xAux = xAux - (fx/fx1);
		}
		
		this.criticRadius = -(Math.sqrt((xAux-1.0)/A));
//		System.out.println("radio:" +  criticRadius);
	}
	
	/**
	 * Actualizes the simulation according the actual time
	 */
	@Override
	public void simulate() {
		
		double temp1, temp2, temp3, temp4, temp5;
		double t = ((double)this.t.getTime())/1000.0;
		double aux = 0;
		double centerPhi;
		
			
		switch(phase){
		case PHASE_0:
			this.phi0 = (W*t);
			
			WDisk = (this.phi0-lastPhi)/(t - lastT);
			lastPhi = this.phi0;
			lastT = t;
			
			break;
		case PHASE_1:
			
			if(r0*Math.pow(W, 2.0) > mu*g){
				this.phi1 = W*t + phi0;
				
				WDisk = (this.phi1-lastPhi)/(t - lastT);
				lastPhi = this.phi1;
				lastT = t;

				if(t >= durationPhase1){
					this.tofinalPhase();
				}
			}
			else if(r0*Math.pow(W, 2.0) < mu*g){
				temp1 = W*t;
				temp2 = 1;
				temp3 = 2*(m/M)*Math.pow(r/R, 2.0);
				temp4 = this.phi0;
				this.phi1 =  ((temp1/(temp2 + temp3)) + temp4);
		
				WDisk = (this.phi1-lastPhi)/(t - lastT);
				lastPhi = this.phi1;
				lastT = t;
				
				if(t >= durationPhase1){
					this.nextPhase();
				}
			}
			else{
				this.nextPhase();
				this.nextPhase();
			}
			
			break;
		case PHASE_2:
			
			r = r0 - (v*t);
			
			temp1 = (-Math.sqrt(a))/v;
			temp2 = Math.atan(Math.abs(r)/(Math.sqrt(a)));
			temp3 = Math.atan(r0/(Math.sqrt(a)));
			temp4 = W;
			temp5 = phi1;
				
			
			phi2 = (temp1 *(temp2 - temp3)*temp4) + temp5;
//			System.out.println(phi2);
			
			if(r > 0){
				
				WDisk = (this.phi2-lastPhi)/(t - lastT);
				lastPhi = this.phi2;
				lastT = t;
			}
			else{
				
				temp1 = (-Math.sqrt(a))/v;
				temp2 = 0;
				temp3 = Math.atan(r0/(Math.sqrt(a)));
				temp4 = W;
				temp5 = phi1;
					
				
				centerPhi = (temp1 *(temp2 - temp3)*temp4) + temp5;
				aux = centerPhi - phi2;
				phi2 = centerPhi + aux;
				if(aux < 0){
					System.err.print("lastphi2 - phi2 es negativo");
					System.exit(1);
				}
			   
				
			    //Velocity
			    WDisk = (this.phi2-lastPhi)/(t - lastT);
				lastPhi = this.phi2;
				lastT = t;
			    
				if(r <= criticRadius){
					this.nextPhase();
					this.simulate();
				}
				else if(r <= -R){
					this.tofinalPhase();
				}	
				
			}
			
			if(WDisk < 0){
				System.err.println("Velocidad Negativa");
				System.exit(1);
			}
			
			break;
		case PHASE_3:
			
			
			temp1 = M*Math.pow(R, 2.0);
			temp2 = 2*m*Math.pow(criticRadius, 2.0);
			
			//DiskDesplacement
			phi3 = ((temp1/(temp1 + temp2))*W*t) + phi2;  
			
			temp1 = v*Math.cos(phi2 - phi0);
			temp2 = (criticRadius*Math.sin(phi2- phi0))*Wf;
			temp3 = criticRadius*Math.cos(phi2- phi0);
			
			x = (temp1 - temp2)*t + temp3;
			
			temp1 = v*Math.sin(phiAux);
			temp2 = criticRadius*Math.cos(phi2- phi0)*Wf;
			temp3 = criticRadius*Math.sin(phi2- phi0);
			
			y = (temp1 + temp2)*t + temp3;
			
			r = (PolarPoint2D.cartesianToPolar(x, y)).getRadius();
			
			  //Velocity
		    WDisk = (this.phi3-lastPhi)/(t - lastT);
			lastPhi = this.phi3;
			lastT = t;
			
			if(Math.abs(x*100) >= 30 || Math.abs(y*100) >= 30){
				this.tofinalPhase();
			}
			 
			break;
		case PHASE_4:
			break;
		}
		
		System.out.println(lastPhi);
	}
	
	/**
	 * Jump to the final phase
	 */
	private void tofinalPhase(){
		this.phase = PHASE_4;
	}
	
	/**
	 * Advances to the next phase, and reset time.
	 */
	public void nextPhase(){
		
		switch(phase){
		case PHASE_0:
			phi1 = phi0;
			break;
		case PHASE_1:
			phi2 = phi1;
			break;
		case PHASE_2:
			phi3 = phi2;
			break;
		case PHASE_3:
			break;
		case PHASE_4:
			System.err.println("¡Final phase reached!");
			System.exit(2);
		}
		
		lastT = 0;
		phase++;
		t = new Time();
		t.start();
	}
	
	/** GETTERS & SETTERS **/
	
	public double getBugPhi(boolean cam1){
		double actualPhi = phi0;

		switch(phase){
		case PHASE_0:
			System.err.println("In Phase 0 there is no bug");
			System.exit(3);
		case PHASE_1:
			if(r0*Math.pow(W, 2.0) > mu*g){
				if(cam1 == true){
					actualPhi = 0;
				}
				else{
					actualPhi = (-phi1);
				}
			}
			else{
				if(cam1 == true){
					actualPhi = phi1 - phi0;
				}
				else{
					actualPhi = -phi0;
				}
			}
			break;
		case PHASE_2:
			if(cam1 == true){
				actualPhi = phi2 - (phi0);
			}
			else{
				actualPhi = -phi0;
			}
			break;
		case PHASE_3:
			if(cam1 == true){
				actualPhi = PolarPoint2D.cartesianToPolar(x,y).getPhi();
			}
			else{
				actualPhi = PolarPoint2D.cartesianToPolar(x,y).getPhi() - (phi2);
			}
			break;
		case PHASE_4:
			actualPhi = phi4;
			break;
		default:
			System.err.println("¡Final phase reached!");
			System.exit(2);
			break;
		}
		return actualPhi;
	}
	
	public double getDiskPhi(){
		
		double actualPhi = phi0;
		
		switch(phase){
			case PHASE_0:
				actualPhi = phi0;
				break;
			case PHASE_1:
				actualPhi = phi1;
				break;
			case PHASE_2:
				actualPhi = phi2;
				break;
			case PHASE_3:
				actualPhi = phi3;
				break;
			case PHASE_4:
				actualPhi = phi3;
				break;
			default:
				System.err.println("¡Final phase reached!");
				System.exit(2);
				return -1;
		}
		return actualPhi;
	}
	
	

	public int getPhase() {
		return phase;
	}

	public Time getT() {
		return t;
	}

	public double get_r() {
		return r;
	}
	
	public double getCriticPhi() {
		return phi2 - phi0;
	}

	public void setPhi2(double phi2) {
		this.phi2 = phi2;
	}

	public double getCriticRadius() {
		return criticRadius;
	}

	@Override
	public boolean finalTimeReached() {
		return false;
	}

	public double getWDisk() {
		return WDisk;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getPhi0() {
		return phi0;
	}

	public double getPhi2() {
		return phi2;
	}	
	
	
}