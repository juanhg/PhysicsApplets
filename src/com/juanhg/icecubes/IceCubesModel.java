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


package com.juanhg.icecubes;

import java.util.ArrayList;
import java.util.List;

import com.juanhg.model.Model;
import com.juanhg.util.Time;

import java.awt.geom.Point2D;

/**
 *   @file   AngularMomentumModel.java
 *   @author Juan Hernandez Garcia 
 *   @brief Model that represents the loss of angular momentum 
 */
public class IceCubesModel extends Model {

	static final int CASE_1 = 1;
	static final int CASE_2 = 2;
	static final int CASE_3 = 3;
	static final int CASE_4 = 4;

	//Stages or phases of the model
	static final int PHASE_1 = 1;
	static final int PHASE_2 = 2;
	static final int PHASE_3 = 3;
	static final int PHASE_4 = 4; //END PHASE

	static final int WATER = 0;
	static final int MILK = 1;
	static final int ORANGE = 2;
	static final int LIMONADE = 3;
	final double K = 0.2;
	final double lo = 2;

	final double Lc = 80;
	final double cc = 0.5;
	final double Cc = 1;
	final double roA = 1;
	final double roc = 0.917;
	double roCong;

	private int currentCase = 0;
	private int currentPhase;
	private Time time;
	double lastt = 0;
	double lastT = 0;

	List<Point2D> chartTQ = new ArrayList<Point2D>();
	List<Point2D> charttQ = new ArrayList<Point2D>();

	//Constants


	//Input parameters
	private double vol, T, t;
	private int type, N;

	//Calculated parameters
	private double V, M, m, l, roL, LL, cL, CL; 
	private double Vo, Tf, Q, to, To, duration;
	private double lastQ = 0;

	//Aux
	double temp1, temp2, temp3, temp4, temp5;


	public IceCubesModel(double vol, double T, double t, int type, int N){ 

		this.vol = vol;
		this.T = T;
		this.t = t;
		this.to = t;
		this.To = T;
		this.type = type;
		this.N = N;
		this.l = lo;

		lastt = to;

		this.calculateLs();
		m = this.N*20;
		V = Math.pow(10*vol + m/roc, 1.0/3.0);
		Vo = V;
		M = roL*vol*10;


		currentPhase = PHASE_1;
		time = new Time();

		detecCase();
		calculateTf();
		calculateDuration();
	}


	/**
	 * Actualizes the simulation according the actual time
	 */
	@Override
	public void simulate() {

		if(!finalPhaseReached()){
			calculateQ();
			calculatet();
			calculateT();
			calculateV();
			calculatel();
			
			if(T <= Tf ){
				T = Tf;
				
			}
			if(t >= Tf){
				t = Tf;
			}
		
			//			System.out.println("Q:" + Q + " t:" + t + " T: " + T);
			//			System.out.println("l:" + l);

			if(nextPhaseReached()){
				chartTQ.add(new Point2D.Double(Q + lastQ, T));
				charttQ.add(new Point2D.Double(Q + lastQ, t));
				jumpToNextPhase();
			}
			else{
				chartTQ.add(new Point2D.Double(Q + lastQ, T));
				charttQ.add(new Point2D.Double(Q + lastQ, t));
			}
		}
	}


	private boolean nextPhaseReached() {
		switch(currentCase){
		case CASE_1:
			switch(currentPhase){
			case PHASE_1:
				if(t >= 0){
					t = 0;
					return true;
				}
				break;
			case PHASE_2:
				if(Q >= m*Lc){
					return true;
				}
				break;
			case PHASE_3:
				if(t >= Tf){
					t = Tf;
					return true;
				}
				break;
			}
			break;
		case CASE_2:
			switch(currentPhase){
			case PHASE_1:
				if(T <= 0){
					T = 0;
					return true;
				}
				break;
			case PHASE_2:
				if(Q >= M*LL){
					return true;
				}
				break;
			case PHASE_3:
				if(T <= Tf){
					T = Tf;
					return true;
				}
				break;
			}
			break;
		case CASE_3:
			switch(currentPhase){
			case PHASE_1:
				if(t >= 0){
					t = 0;
					return true;
				}
				break;
			case PHASE_2:
				if(Q >= (M*CL*To + m*cc*to)){
					return true;
				}
				break;
			}
			break;
		case CASE_4:
			switch(currentPhase){
			case PHASE_1:
				if(T <= 0){
					T = 0;
					return true;
				}
				break;
			case PHASE_2:
				if(Q >= (-m*cc*to - M*CL*To)){
					return true;
				}
				break;
			}
			break;
		}
		return false;
	}

	private void jumpToNextPhase(){
		switch(currentPhase){
		case PHASE_1:
			jumpToPhase(PHASE_2);
			break;
		case PHASE_2:
			switch(currentCase){
			case CASE_1:
			case CASE_2:
				jumpToPhase(PHASE_3);
				break;
			case CASE_3:
			case CASE_4:
				jumpToPhase(PHASE_4);
			}
			break;
		case PHASE_3:
			jumpToPhase(PHASE_4);
			break;
		}
	}



	public void jumpToPhase(int phase) {
		this.currentPhase = phase;
		lastQ += Q;
		lastt = t;
		lastT = T;

		time = new Time();
		time.start();
	}

	private void calculateLs(){

		switch(type){
		case WATER:
			roL = 1;
			LL = 80;
			cL = 0.5;
			CL = 1;
			roCong = 0.917;
			break;
		case MILK:
			roL = 1.017;
			roCong = 0.934;
			cL = 0.47;
			CL = 0.9;
			LL = 69; 
			break;
		case ORANGE:
			roL = 1.038;
			roCong = 0.955;
			cL = 0.42;
			CL = 0.93;
			LL = 70.937;
			break;
		case LIMONADE:
			roL = 1.11;
			roCong = 1.027;
			cL = 0.41;
			CL = 0.95;
			LL = 73.8;
			break;
		}
	}

	private void calculateTf(){
		switch(currentCase){
		case CASE_1:
			Tf = -((-m*cc*t + m*Lc - M*CL*T)/(M*CL + m*Cc));
			break;
		case CASE_2:
			Tf = (M*cL*T + M*LL + m*cc*t)/((cL*M) + cc*m);
			break;
		case CASE_3:
		case CASE_4:
			Tf = 0;
			break;
		default:
			System.err.println("Caso imposible!");
			break;
		}
	}

	private void calculateDuration(){
		switch(currentCase){
		case CASE_1:
		case CASE_3:
			duration = (m*Lc)/K;
			break;
		case CASE_2:
		case CASE_4:
			duration = (M*LL)/K;
			break;
		}
	}

	private void calculateQ(){
		Q = K*((double)time.getTime());
	}

	private void calculatet(){
		switch(currentCase){
		case CASE_1:
		case CASE_3:
			switch(currentPhase){
			case PHASE_1:
				t = (Q/(m*cc)) + to;
				break;
			case PHASE_2:
				t = 0;
				break;
			case PHASE_3:
				t = Q/(m*Cc);
				break;
			}
			break;
		case CASE_2:
		case CASE_4:
			switch(currentPhase){
			case PHASE_1:
				t = (Q/(m*cc)) + to;
				break;
			case PHASE_3:	
			case PHASE_2:
				t = (Q/(m*cc)) + lastt;
				break;
			}
			break;
		}
	}

	private void calculateT(){
		switch(currentCase){
		case CASE_1:
		case CASE_3:
			switch(currentPhase){
			case PHASE_1:
				T = (-Q/(M*CL)) + To;
				break;
			case PHASE_2:
			case PHASE_3:
				T = (-Q/(M*CL)) + lastT;
				break;
			}
			break;
		case CASE_2:
		case CASE_4:
			switch(currentPhase){
			case PHASE_1:
				T = (-Q/(M*CL)) + To;
				break;
			case PHASE_2:
				T = 0;
				break;
			case PHASE_3:
				T = -Q/(M*cL);
				break;
			}
			break;
		}
	}

	private void calculatel(){
		switch(currentCase){
		case CASE_2:
		case CASE_4:
			l = lo;
			break;
		case CASE_3:
		case CASE_1:
			switch(currentPhase){
			case PHASE_1:
				l = lo;
				break;
			case PHASE_2:
				l = ((-lo/duration)*((double)time.getTime())) + lo;
				break;
			case PHASE_3:
				l = 0;
				break;
			}
			break;
		}
	}

	private void calculateV(){

		double tiempo = ((double)time.getTime())/1000.0;

		switch(currentCase){
		case CASE_1:
		case CASE_3:
			switch(currentPhase){
			case PHASE_1:
				V = Vo;
				break;
			case PHASE_2:
				temp1 = Math.pow(10*vol + m/roA, 1.0/3.0);
				temp2 = Math.pow(10*vol + m/roc, 1.0/3.0);
				temp3 = (temp1 - temp2)/duration;
				V = temp3*tiempo + Math.pow(10.0*vol + m/roc, 1.0/3.0);
				break;
			}
			break;
		case CASE_2:
		case CASE_4:
			switch(currentPhase){
			case PHASE_1:
				V = Vo;
				break;
			case PHASE_2:
				temp1 = Math.pow((M/roCong) + m/roA, 1.0/3.0);
				temp2 = Math.pow(10*vol + m/roc, 1.0/3.0);
				temp3 = (temp1 - temp2)/duration;
				V = temp3*tiempo + Math.pow(10*vol + m/roc, 1.0/3.0);
				break;
			}
			break;
		}
	}

	private void detecCase(){
		if(M*CL*T > (-t*m*cc)+(m*Lc)){
			currentCase = CASE_1;
		}
		else if(-m*cc*t > (M*CL*T + M*LL)){
			currentCase = CASE_2;
		}
		else if(M*CL*T >= -t*m*cc){
			currentCase = CASE_3;
		}
		else{
			currentCase = CASE_4;
		}
	}

	public Point2D [] getChartTQ(){
		return chartTQ.toArray(new Point2D[chartTQ.size()]);
	}

	public Point2D [] getCharttQ(){
		return charttQ.toArray(new Point2D[charttQ.size()]);
	}
	
	public boolean finalPhaseReached(){
		if(currentPhase == PHASE_4){
			return true;
		}
		return false;
	}



	public double getT() {
		return T;
	}


	public double gett() {
		return t;
	}


	public double getLo() {
		return lo;
	}


	public double getV() {
		return V;
	}

	public double getVol(){
		return Math.pow(V, 3.0)/10.0;
	}

	public double getL() {
		return l;
	}


	/** GETTERS & SETTERS **/

	public int getCurrentPhase() {
		return currentPhase;
	}

	public int getCurrentCase() {
		return currentCase;
	}

	public double getDuration() {
		return duration;
	}


	public Time getTime() {
		return time;
	}
	
	public double getSecond(){
		double tiempo;
		
		time.pause();
		tiempo = (double)time.getTime()/1000.0;
		time.start();
		
		return tiempo;
	}

	public static double getV(double vol, double N){
		double m = N*20;
		double roc = 0.917;
		double V = Math.pow(10.0*vol + m/roc, 1.0/3.0);
		return V;
	}
}