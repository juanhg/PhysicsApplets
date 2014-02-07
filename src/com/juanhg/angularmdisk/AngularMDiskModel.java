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
	private double phi1B,phi2B,phi3B;
	private double r; //Actual radius
	private double x;
	private double a;
	private double finalR;
	private double Wf;
	
	
	public AngularMDiskModel(double m, double r0, double v, double W, double mu){ 
	
		final int recursion = 3;
		double temp1, temp2, temp3, temp4;
		
		this.m = m;
		this.r0 = r0;
		this.v = v;
		this.W = 2*Math.PI*W;
		this.mu = mu;
		
		this.phase = 0;
		this.t = new Time();
		
		this.r = r0;
		this.x = 0.5*M*Math.pow(R, 2);
		
		for(int i = 0; i < recursion; i++){
			this.x = calculateX(x);
		}
		
		finalR = - Math.sqrt((1/m)*(this.x - (0.5*M*Math.pow(R, 2.0))));
		
		temp1 = 0.5*M*Math.pow(R, 2.0);
		temp2 = m*Math.pow(r, 2.0);
		temp3 = W;
		
		Wf = (temp1/(temp1 + temp2))*W;
		
		a = 0.5*(M/this.m)*(Math.pow(R, 2)/this.v);
		
		
		
	}
	
	private double calculateX (double x){
		double result = 0;
		double temp0, temp1, temp2, temp3, temp4;
		
		temp0 = 0.5*M*Math.pow(R, 2.0);
		
		temp1 = mu*g*Math.pow(x, 4.0);
		temp2 = 4*Math.pow(v, 2) * Math.pow(temp0,2.0) * Math.pow(x, 2.0);
		temp3 = temp0/m;
		temp4 = m;
		
		result = (temp1 - temp2 + temp3)*temp4;
		return result;
	}
	
	@Override
	public void simulate() {
		
		double temp1, temp2, temp3, temp4, temp5;
		double t = ((double)this.t.getTime())/1000.0;
		
		switch(phase){
		case PHASE_0:
			this.phi0 = (W*t);
			break;
		case PHASE_1:
			if(this.phase1Case1()){
				this.phi1 = W*t;

				if(t >= durationPhase1){
					this.tofinalPhase();
				}
			}
			else{
				temp1 = W*t;
				temp2 = 1;
				temp3 = 2*(m/M)*Math.pow(r/R, 2.0);
				temp4 = this.phi0;
				this.phi1 =  ((temp1/(temp2 + temp3)) + temp4);
		
				if(t >= durationPhase1){
					this.nextPhase();
				}
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
			
			if(r <= 0){
				phi2 = -phi2;
			
				if(r <= finalR){
					this.nextPhase();
				}
				else if(r <= -R){
					this.tofinalPhase();
				}	
			}
		
			
//			System.out.println("Phi1:" + phi1);
//			System.out.println("Phi2:" + phi2);
			
			break;
		case PHASE_3:
			
			temp1 = M*Math.pow(R, 2.0);
			temp2 = 2*m*Math.pow(r, 2.0);
			
			phi3 = ((temp1/(temp1 + temp2))*W*t) + phi2; //phiAux????? 
			
			temp1 = (-Math.sqrt(a))/v;
			temp2 = Math.atan(Math.abs(r)/(Math.sqrt(a)));
			temp3 = Math.atan(r0/(Math.sqrt(a)));
			temp4 = W;
			temp5 = phi2;
				
			phiAux = (temp1 *(temp2 - temp3)*temp4) + temp5;
			
			 
			break;
		case PHASE_4:
			break;
		}
	}
	
	public double getActualTime() {
		return actualTime;
	}


	public int getPhase() {
		return phase;
	}

	public Time getT() {
		return t;
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
			break;
		case PHASE_3:
			break;
		case PHASE_4:
			System.err.println("¡Final phase reached!");
			System.exit(2);
		}
		
		phase++;
		t = new Time();
		t.start();
	}
	
	private void tofinalPhase(){
		this.phase = PHASE_4;
	}
	
	public boolean phase1Case1(){
		if(phase == this.PHASE_1){
			if(r0*Math.pow(W, 2.0) <= mu*g){
				return true;
			}
		}
		return false;
	}
	
	public double getBugPhi(){
		double actualPhi = phi0;

		switch(phase){
		case PHASE_0:
			System.err.println("In Phase 0 there is no bug");
			System.exit(3);
		case PHASE_1:
			actualPhi = phi1 - phi0;
			break;
		case PHASE_2:
			actualPhi = phi2 - (phi0);
			break;
		case PHASE_3:
			actualPhi = phi3;
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
				actualPhi = phi4;
				break;
			default:
				System.err.println("¡Final phase reached!");
				System.exit(2);
				return -1;
		}
		return actualPhi;
	}
	

	public double get_r() {
		return r;
	}

	@Override
	public boolean finalTimeReached() {
		// TODO Auto-generated method stub
		return false;
	}	
}
