/*  -----------------------------------------------------------------
 	 @file   PoModel.java
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


package com.juanhg.pot;

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
public class PotModel extends Model {

	//Stages or phases of the model
	static final int PHASE_1 = 1;
	static final int PHASE_2 = 2;
	static final int PHASE_3 = 3;
	static final int PHASE_4 = 4;

	static final int WOOD = 0;
	static final int COAL = 1;
	static final int GASOLINE = 2;
	static final int GAS = 3;


	boolean end = false;
	private int currentPhase;

	//Constants
	private final double r = 0.15;
	private final double N = 1;
	private final double R = 8.31;
	private final double g = 9.8;
	private final double Pa = Math.pow(10.0, 5.0);

	//Input parameters
	private double T, P, V, I4;
	private int combustible;

	//Calculated parameters
	private double W, Q, U, h;
	private double Vo, To, mo, ho, Po, mc,c;
	private double hf, Qf, Tf, Vf, m, A, Pf, q, b;

	//Aux
	double temp1, temp2, temp3, temp4, temp5;


	public PotModel(double T, double P, double V, double Vmax, int combustible){ 
		this.T = this.To = T;
		this.P = this.Po = P;
		this.V = this.Vo = V;
		this.I4 = Vmax;
		this.combustible = combustible;

		//Set the initTime, finalTime, and dt
		initTime();

		A = Math.PI*Math.pow(r, 2.0);

		//mc
		//mo = numInicial * pesoMaterial;
		//mf = numfinal * pesoMaterial;


		//Parametros calculados iniciales
	}

	public double getT() {
		return T;
	}

	public void setT(double t) {
		T = t;
	}

	public double getP() {
		return P;
	}

	public void setP(double p) {
		P = p;
	}

	public double getV() {
		return V;
	}

	public void setV(double v) {
		V = v;
	}

	public double getW() {
		return 5;
	}

	public void setW(double w) {
		W = w;
	}

	public double getQ() {
		return Q;
	}

	public void setQ(double q) {
		Q = q;
	}

	public double getU() {
		return U;
	}

	public void setU(double u) {
		U = u;
	}

	void calculateq(){
		switch(combustible){
		case WOOD:
			q = 15000;
			break;
		case COAL:
			q = 27000;
			break;
		case GASOLINE:
			q = 47000;
			break;
		case GAS:
			q = 54000;
			break;
		}
	}


	public void initTime(){
		switch(currentPhase){
		case PHASE_1:
			initTime = 0;
			finalTime = Math.PI/2;
			break;
		case PHASE_2:
			calculateq();
			Qf = q *mc;
			initTime = To;
			finalTime = Tf = (Qf/m*c) + To;
			break;
		case PHASE_3:
			calculateq();
			Qf = q *mc;
			hf = (Po*Vo + Qf + (3.0/2.0)*N*R*To)/((5.0/2.0)*Po);
			Vf = hf*A;
			P = m*g + Pa*A;
			initTime = To;
			finalTime = Tf = (Vf*P)/(N*R);
			break;
		}
		dt = 0.1;
	}

	/**
	 * Actualizes the simulation according the actual time
	 */
	@Override
	public void simulate() {		

		if(!end){
			if(finalTimeReached()){
				setToFinal();
			}

			switch(currentPhase){
			case PHASE_1:
				Q = 0;
				Po = (N*R*To)/Vo;
				Pf = Pa + (m*g)/A;
				T = To;
				ho = ((N*R*To)/(m*g + Pa*A))*(1/A);
				Tf = (m*g*ho + Pa*Vo + To)/(1.0 - (2.0/(3.0*Pf*A))*((-m*g)-Pa*A));
				hf = (N*R*Tf)/(Pf*A);
				h = (hf-ho)*Math.sin(currentTime)+ho;
				W = h*((-m*g)-Pa*A) + m*g*ho + Pa*Vo;
				U = -W;
				break;
			case PHASE_2:
				//T ya ha sido calculada
				P = (N*R/Vo)*T;
				U = (3.0/2.0)*N*R*T;
				Q = m*c*(T-To);
				W = 0;
				h = Vo/A;
				break;
			case PHASE_3:
				//T ya ha sido calculada
				P = m*g + Pa*A;
				U = (3.0/2.0)*N*R*T;
				Q = m*c*(T-To);
				W = (3.0/2.0)*N*R*(T-To) + Q;
				ho = ((N*R*To)/(m*g + Pa*A))*(1/A);
				Vo = ho*A;
				Qf = m*c; //Esto es asi????? o es q*mc
				hf = (Po*Vo + Qf + (3.0/2.0)*N*R*To)/(2.5*Po);
				b = (hf - ho)/(Tf - To);
				h = b*(T-To)+ ho;
				break;
			}

			if(finalTimeReached()){
				end = true;
			}

			this.incrementCurrentTime();
		}
	}

	@Override
	public void incrementCurrentTime(){
		currentTime += dt;
		switch(currentPhase){
		case PHASE_2:
		case PHASE_3:
			T += dt;
			break;
		}
	}

	public void setToFinal(){
		currentTime = finalTime;
		switch(currentPhase){
		case PHASE_2:
		case PHASE_3:
			T = finalTime;
			break;
		}
	}

	public void calculatePf(){
		switch(currentPhase){
		case PHASE_1:
			Pf = Pa + (m*g)/A;
			break;
		case PHASE_2:

			break;
		case PHASE_3:
			break;
		}
	}

	/** GETTERS & SETTERS **/

	public int getPhase() {
		return currentPhase;
	}
}