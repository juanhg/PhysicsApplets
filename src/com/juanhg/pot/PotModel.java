/*  -----------------------------------------------------------------
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
public class PotModel {

	//Stages or phases of the model
	static final int PHASE_0 = 0;
	static final int PHASE_1 = 1;
	static final int PHASE_2 = 2;
	static final int PHASE_3 = 3;
	static final int PHASE_4 = 4;

	static final int WOOD = 1;
	static final int COAL = 2;
	static final int GASOLINE = 3;
	static final int GAS = 4;
	double currentTime = 0.1;
	double finalTime = 0;
	double dt = 0.1;
	boolean paused = true;


	boolean end = false;
	private int currentPhase;
	private int selectedPhase;

	//Constants
	private final double r = 0.15;
	private final double N = 1;
	private final double R = 8.31;
	private final double g = 9.8;
	private final double Pa = Math.pow(10.0, 5.0);

	private int combustible;

	//Calculated parameters
	private double T, P, V, W, Q, U, h;
	private double Wo, Qo, Uo, Vo, Po, To, mo, ho, mc,c, alpha;
	private double Wf, Qf, Qfinal, Uf, Vf, Pf, Tf, m, A, q,hf, b;

	//Aux
	double temp1, temp2, temp3, temp4, temp5;

	Time time0;


	public PotModel(double T, double V, double mo, double m, int combustible, double mc, int phase){ 
		super();
		this.T = this.To = T;
		this.V = this.Vo = V/1000.0;
		this.mo = mo;
		this.m = m;
		this.combustible = combustible;
		this.mc = mc;
		this.currentPhase = phase;

		A = Math.PI*Math.pow(r, 2.0);
		this.alpha = (2.0/3.0)*(Pa + m*g/A);
		
		this.setDt();

		//Set the initTime, finalTime, and dt
		initTime();
		initValues();
	}

	public double getT() {
		if(currentPhase == PHASE_1){
			return T;
		}
		return T-dt;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void setT(double t) {
		T = t;
	}

	public double getP() {
		return P;
	}

	public int getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(int currentPhase) {
		this.currentPhase = currentPhase;
	}

	public void setP(double p) {
		P = p;
	}

	public double getV() {
		return V*1000.0;
	}

	public void setV(double v) {
		V = v;
	}

	public double getW() {
		return W;
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

	private void setDt(){
		switch(combustible){
		case WOOD:
			dt = 0.1;
			break;
		case COAL:
			dt = 0.2;
			break;
		case GASOLINE:
			dt = 0.5;
			break;
		case GAS:
			dt = 0.6;
			break;
		}
	}

	void calculateq(){
		switch(combustible){
		case WOOD:
			q = 15;
			break;
		case COAL:
			q = 27;
			break;
		case GASOLINE:
			q = 47;
			break;
		case GAS:
			q = 54;
			break;
		}
	}


	public void initTime(){
		switch(currentPhase){
		case PHASE_1:
			currentTime =  0.1;
			finalTime = Math.PI/2.0;
			break;
		case PHASE_2:
			calculateq();
			Qfinal = q * mc;
			currentTime =  To;
			finalTime = Tf = (Qfinal/20.8) + To;
			break;
		case PHASE_3:
			calculateq();
			Qfinal = q*mc;
			Po =  Pa + (m*g)/A;
			ho = ((N*R*To)/(m*g + Pa*A));
			Vo = ho*A;
			hf = ((Po*Vo + Qfinal + (3.0/2.0)*N*R*To)/((5.0/2.0)*Po))/A;
			Vf = hf*A;
			currentTime =  To;
			finalTime = Tf = (Vf*Po)/(N*R);
			break;
		}
	}

	public void initValues(){
		switch(currentPhase){
		case PHASE_1:
			currentTime =  0.1;
			finalTime = Math.PI/2.0;

			ho = ((N*R*To)/(mo*g + Pa*A));
			V = Vo = A*ho;
			Q = Qo = Qf = 0;
			Po = (N*R*To)/Vo;
			Pf = Pa + (m*g)/A;
			T = To;
			Tf = (((alpha/Po) + 1.0)*To)/((alpha/Pf)+1.0);
			hf = (N*R*Tf)/(Pf*A);

			h = (hf-ho)*Math.sin(finalTime)+ho;
			Vf = A*h;
			temp1 = h*((-m*g)-Pa*A);
			temp2 = m*g*ho;
			temp3 = Pa*Vo;
			Wf = -(temp1 + temp2  + temp3);
			Uf = -Wf + (3.0/2.0)*N*R*To;

			h = (hf-ho)*Math.sin(currentTime)+ho;
			temp1 = h*((-m*g)-Pa*A);
			temp2 = m*g*ho;
			temp3 = Pa*Vo;
			Wo = -(temp1 + temp2  + temp3);
			Uo = -W + (3.0/2.0)*N*R*To;

			break;
		case PHASE_2:
			calculateq();
			Qfinal = q * mc;
			currentTime =  To;
			finalTime = Tf = (Qfinal/20.8) + To;

			W = Wo = Wf = 0;
			V = Vo = Vf = Vo;
			h = Vo/A;

			Po = (N*R/Vo)*To;
			Uo = (3.0/2.0)*N*R*To;
			Qo = 0;

			Pf = (N*R/Vo)*Tf;
			Uf = (3.0/2.0)*N*R*Tf;
			Qf = 20.8*(Tf-To);
			break;
		case PHASE_3:
			calculateq();
			Qfinal = q*mc;
			Po = Pf = P =  Pa + (m*g)/A;
			ho = ((N*R*To)/(m*g + Pa*A));
			Vo = ho*A;
			hf = ((Po*Vo + Qfinal + (3.0/2.0)*N*R*To)/((5.0/2.0)*Po))/A;
			Vf = hf*A;
			currentTime =  To;
			finalTime = Tf = (Vf*Po)/(N*R);


			Uo = (3.0/2.0)*N*R*To;
			Qo = 0;
			Wo = Q;

			Uf = (3.0/2.0)*N*R*Tf;
			Qf = 20.8*(Tf-To);
			Wf = (3.0/2.0)*N*R*(Tf-To) + Qf;
			b = (hf - ho)/(Tf - To);
			h = b*(Tf-To)+ ho;
			Vf = h*A;

			break;
		}
	}



	public double getWo() {
		return Wo;
	}

	public void setWo(double wo) {
		Wo = wo;
	}

	public double getQo() {
		return Qo;
	}

	public void setQo(double qo) {
		Qo = qo;
	}

	public double getUo() {
		return Uo;
	}

	public void setUo(double uo) {
		Uo = uo;
	}

	public double getVo() {
		return Vo*1000.0;
	}

	public double getPeso(){
		switch(currentPhase){
		case PHASE_1:
			if(paused){
				return mo;
			}
			else{
				return m;
			}
		}
		return m;
		
	}

	public void setVo(double vo) {
		Vo = vo;
	}

	public double getPo() {
		return Po;
	}

	public void setPo(double po) {
		Po = po;
	}

	public double getTo() {
		return To;
	}

	public void setTo(double to) {
		To = to;
	}

	public double getWf() {
		return Wf;
	}

	public void setWf(double wf) {
		Wf = wf;
	}

	public double getQf() {
		return Qf;
	}

	public void setQf(double qf) {
		Qf = qf;
	}

	public double getUf() {
		return Uf;
	}

	public void setUf(double uf) {
		Uf = uf;
	}

	public double getVf() {
		return Vf*1000.0;
	}

	public void setVf(double vf) {
		Vf = vf;
	}

	public double getPf() {
		return Pf;
	}

	public void setPf(double pf) {
		Pf = pf;
	}

	public double getTf() {
		return Tf;
	}

	public void setTf(double tf) {
		Tf = tf;
	}

	/**
	 * Actualizes the simulation according the actual time
	 */
	public void simulate() {		

		if(!end){
			if(finalTimeReached()){
				setToFinal();
			}

			switch(currentPhase){
			case PHASE_1:
				if(time0 == null){
					time0 = new Time();
					time0.start();

					currentTime += dt;
					h = (hf-ho)*Math.sin(currentTime)+ho;
					V = A*h;
					temp1 = h*((-m*g)-Pa*A);
					temp2 = m*g*ho;
					temp3 = Pa*Vo;
					W = -(temp1 + temp2  + temp3);
					U = -W + (3.0/2.0)*N*R*To;
				}
				else{
					double wait = 2000;
					time0.pause();
					double t0 = time0.getTime();
					time0.start();
					if(t0 >= wait){
						paused = false;
						h = (hf-ho)*Math.sin(currentTime)+ho;
						V = A*h;
						temp1 = h*((-m*g)-Pa*A);
						temp2 = m*g*ho;
						temp3 = Pa*Vo;
						W = -(temp1 + temp2  + temp3);
						U = -W + (3.0/2.0)*N*R*To;
					}
				}
				break;
			case PHASE_2:
				//T ya ha sido calculada
				P = (N*R/Vo)*T;
				U = (3.0/2.0)*N*R*T;
				Q = 20.8*(T-To);

				break;
			case PHASE_3:
				//T ya ha sido calculada
				P = m*g + Pa*A;
				U = (3.0/2.0)*N*R*T;
				Q = 20.8*(T-To);
				W = (3.0/2.0)*N*R*(T-To) + Q;
				b = (hf - ho)/(Tf - To);
				h = b*(T-To)+ ho;
				V = h*A;
				break;
			}

			if(finalTimeReached()){
				end = true;
			}

			this.incrementCurrentTime();
		}
	}

	public boolean getEnd(){
		return end;
	}

	public void incrementCurrentTime(){
		switch(currentPhase){
		case PHASE_1:
			if(!paused){
				currentTime += dt;
			}
			break;
		case PHASE_2:
		case PHASE_3:
			currentTime += dt;
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

	public double getH() {
		return h*100;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getHo() {
		return ho;
	}

	public void setHo(double ho) {
		this.ho = ho;
	}

	public boolean finalTimeReached(){
		if(currentTime >= finalTime){
			return true;
		}
		return false;
	}

	/** GETTERS & SETTERS **/

	public int getPhase() {
		return currentPhase;
	}
}