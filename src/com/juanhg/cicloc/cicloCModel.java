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


package com.juanhg.cicloc;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import com.juanhg.model.Model;
import com.juanhg.util.Time;

/**
 *   @file   AngularMomentumModel.java
 *   @author Juan Hernandez Garcia 
 *   @brief Model that represents the loss of angular momentum 
 */
public class cicloCModel extends Model {

	//Stages or phases of the model
	static final int PHASE_1 = 1;
	static final int PHASE_2 = 2;
	static final int PHASE_3 = 3;
	static final int PHASE_4 = 4;


	private int currentPhase;
	private Time t;
	private double lastT;

	//Constants
	private double R = 8.31;
	private double gamma = 5.0/3.0;
	private double k1 = 0.00005;
	private double k2 = 0.0001;


	//Input parameters
	private double T1, T2, Vmin, Vmax, N;

	//Calculated parameters
	private double V, U, T, S, P;

	private List<Point2D> fPV, fUV, fTV, fPT, fST, fUT;
	private List<Point2D> pPV, pUV, pTV, pPT, pST, pUT;

	//Aux
	double temp1, temp2, temp3, temp4, temp5;

	private boolean firstCompleted;
	private boolean rCicle;


	public cicloCModel(double T1, double T2, double Vmin, double Vmax, double N){ 
		this.T1 = T1;
		this.T2 = T2;
		this.Vmin = Vmin;
		this.Vmax = Vmax;
		this.N = N;

		fPV = new ArrayList<Point2D>();
		fUV = new ArrayList<Point2D>();
		fTV = new ArrayList<Point2D>();
		fPT = new ArrayList<Point2D>();
		fST = new ArrayList<Point2D>();
		fUT = new ArrayList<Point2D>();

		pPV = new ArrayList<Point2D>();
		pUV = new ArrayList<Point2D>();
		pTV = new ArrayList<Point2D>();
		pPT = new ArrayList<Point2D>();
		pST = new ArrayList<Point2D>();
		pUT = new ArrayList<Point2D>();

		pPV.add(new Point2D.Double(0,0));
		pUV.add(new Point2D.Double(0,0));
		pTV.add(new Point2D.Double(0,0));
		pPT.add(new Point2D.Double(0,0));
		pST.add(new Point2D.Double(0,0));
		pUT.add(new Point2D.Double(0,0));

		firstCompleted = false;
		rCicle = false;
		currentPhase = PHASE_4;
		t = new Time();
	}


	/**
	 * Actualizes the simulation according the actual time
	 */
	@Override
	public void simulate() {

		nextPhaseReached();

		calculateV();
		calculateP();
		calculateT();
		calculateU();
		calculateS();

		double Vout = V*N*1000;

		if(!firstCompleted){
			fPV.add(new Point2D.Double(Vout,P));
			fUV.add(new Point2D.Double(Vout,U));
			fTV.add(new Point2D.Double(Vout,T));
			fPT.add(new Point2D.Double(T,P));
			fST.add(new Point2D.Double(T,S));
			fUT.add(new Point2D.Double(T,U));
		}

		pPV.set(0 , new Point2D.Double(Vout,P));
		pUV.set(0 , new Point2D.Double(Vout,U));
		pTV.set(0 , new Point2D.Double(Vout,T));
		pPT.set(0 , new Point2D.Double(T,P));
		pST.set(0 , new Point2D.Double(T,S));
		pUT.set(0 , new Point2D.Double(T,U));

		if(nextPhaseReached()){
			jumpToNextPhase();
		}
	}




	private boolean nextPhaseReached() {
		if(rCicle){
			switch(currentPhase){
			case PHASE_1:
				temp2 = R*T1;
				temp3 = R*T2*(Math.pow(Vmax, gamma - 1)/Math.pow(R*T1, gamma));
				temp4 = 1/(1-gamma);
				temp5 = temp2/Math.pow(temp3, temp4);
				if(V >= temp5){
					V = temp5;
					return true;
				}
				break;
			case PHASE_2:
				if(V >= Vmax){
					V = Vmax;
					return true;
				}
				break;
			case PHASE_3:
				temp2 = R*T2;
				temp3 = R*T1*(Math.pow(Vmin, gamma - 1)/Math.pow(R*T2, gamma));
				temp4 = 1/(1-gamma);
				temp5 = temp2/Math.pow(temp3, temp4);
				if(V <= temp5){
					V = temp5;
					return true;
				}
				break;
			case PHASE_4:
				if(V <= Vmin){
					V = Vmin;
					return true;
				}
				break;
			}
		}
		else{
			switch(currentPhase){
			case PHASE_1:
				temp2 = R*T1;
				temp3 = R*T2*(Math.pow(Vmax, gamma - 1)/Math.pow(R*T1, gamma));
				temp4 = 1/(1-gamma);
				temp5 = temp2/Math.pow(temp3, temp4);
				if(V <= Vmin){
					V = Vmin;
					return true;
				}
				break;
			case PHASE_2:
				temp2 = R*T1;
				temp3 = R*T2*(Math.pow(Vmax, gamma - 1)/Math.pow(R*T1, gamma));
				temp4 = 1/(1-gamma);
				temp5 = temp2/Math.pow(temp3, temp4);
				if(V <= temp5){
					V = temp5;
					return true;
				}
				break;
			case PHASE_3:
				if(V >= Vmax){
					V = Vmax;
					return true;
				}
				break;
			case PHASE_4:
				temp2 = R*T2;
				temp3 = R*T1*(Math.pow(Vmin, gamma - 1)/Math.pow(R*T2, gamma));
				temp4 = 1/(1-gamma);
				temp5 = temp2/Math.pow(temp3, temp4);
				if(V >= temp5){
					V = temp5;
					return true;
				}
				break;
			}
		}
		return false;
	}

	private void jumpToNextPhase(){
		if(rCicle){
			switch(currentPhase){
			case PHASE_1:
				jumpToPhase(PHASE_2);
				break;
			case PHASE_2:
				jumpToPhase(PHASE_3);
				break;
			case PHASE_3:
				jumpToPhase(PHASE_4);
				break;
			case PHASE_4:
				firstCompleted = true;
				jumpToPhase(PHASE_1);
				break;
			}
		}
		else{
			switch(currentPhase){
			case PHASE_1:
				firstCompleted = true;
				jumpToPhase(PHASE_4);
				break;
			case PHASE_2:
				jumpToPhase(PHASE_1);
				break;
			case PHASE_3:
				jumpToPhase(PHASE_2);
				break;
			case PHASE_4:
				jumpToPhase(PHASE_3);
				break;
			}
		}
	}



	public void jumpToPhase(int phase) {

		this.currentPhase = phase;
		lastT = 0;

		t = new Time();
		t.start();
	}


	private void calculateV(){
		double t = (double)this.t.getTime()/1000.0;
		if(rCicle){
			switch(currentPhase){
			case PHASE_1:
				V = t*k1 + Vmin;
				break;
			case PHASE_2:
				temp1 = t*k2;
				temp2 = R*T1;
				temp3 = R*T2*(Math.pow(Vmax, gamma - 1.0)/Math.pow(R*T1, gamma));
				temp4 = 1.0/(1.0-gamma);
				V = temp1 + temp2/Math.pow(temp3, temp4);
				break;
			case PHASE_3:
				V = (-t*k1) + Vmax;
				break;
			case PHASE_4:
				temp1 = -t*k2;
				temp2 = R*T2;
				temp3 = R*T1*(Math.pow(Vmin, gamma - 1.0)/Math.pow(R*T2, gamma));
				temp4 = 1.0/(1-gamma);
				V = temp1 + temp2/Math.pow(temp3, temp4);
				break;
			}
		}
		else{
			switch(currentPhase){
			case PHASE_1:
				temp1 = (-t*k1);
				temp2 = R*T1;
				temp3 = R*T2*(Math.pow(Vmax, gamma - 1.0)/Math.pow(R*T1, gamma));
				temp4 = 1.0/(1-gamma);
				V = temp1 + temp2/Math.pow(temp3, temp4);
				break;
			case PHASE_2:
				V = (-t*k2) + Vmax;
				break;
			case PHASE_3:
				temp1 = t*k1;
				temp2 = R*T2;
				temp3 = R*T1*(Math.pow(Vmin, gamma - 1.0)/Math.pow(R*T2, gamma));
				temp4 = 1.0/(1-gamma);
				V = temp1 + temp2/Math.pow(temp3, temp4);
				break;
			case PHASE_4:
				V = t*k2 + Vmin;
				break;
			}
		}
	}

	private void calculateP(){

		switch(currentPhase){
		case PHASE_1:
			P = (R*T1)/V;
			break;
		case PHASE_2:
			P = (R*T2*Math.pow(Vmax, gamma-1.0)/Math.pow(V, gamma));
			break;
		case PHASE_3:
			P = (R*T2)/V;
			break;
		case PHASE_4:
			P = (R*T1*Math.pow(Vmin, gamma-1.0)/Math.pow(V, gamma));
			break;
		}
		P /= 101325;
	}

	private void calculateT(){

		switch(currentPhase){
		case PHASE_1:
			T = T1;
			break;
		case PHASE_2:
			T = (T2*Math.pow(Vmax, 2.0/3.0))/Math.pow(V, 2.0/3.0);
			break;
		case PHASE_3:
			T = T2;
			break;
		case PHASE_4:
			T = (T1*Math.pow(Vmin, 2.0/3.0))/Math.pow(V, 2.0/3.0);
			break;
		}
	}

	private void calculateU(){

		switch(currentPhase){
		case PHASE_1:
			U = (3.0*R*T1)/2.0;
			break;
		case PHASE_2:
			U = Math.pow(Vmax/V, 2.0/3.0)*(3.0/2.0)*R*T2;
			break;
		case PHASE_3:
			U = (3.0*R*T2)/2.0;
			break;
		case PHASE_4:
			U = Math.pow(Vmin/V, 2.0/3.0)*(3.0/2.0)*R*T1;
			break;
		}
	}

	//TODO ask paco
	private void calculateS(){
		S = R*Math.log(Math.pow(U, 3.0/2.0)*V) -  R*Math.log(Math.pow(((3.0/2.0)*R*T1), 3.0/2.0)*Vmin);
	}


	//	private void calculateV(){
	//		double t = this.t.getTime();
	//		
	//		switch(currentPhase){
	//		case PHASE_1:
	//			break;
	//		case PHASE_2:
	//			break;
	//		case PHASE_3:
	//			break;
	//		case PHASE_4:
	//			break;
	//		}
	//	}
	//	
	//	private void calculateV(){
	//		double t = this.t.getTime();
	//		
	//		switch(currentPhase){
	//		case PHASE_1:
	//			break;
	//		case PHASE_2:
	//			break;
	//		case PHASE_3:
	//			break;
	//		case PHASE_4:
	//			break;
	//		}
	//	}

	/** GETTERS & SETTERS **/

	public double getActualTime() {
		return actualTime;
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

	public Point2D [] getFPV(){
		return fPV.toArray(new Point2D[fPV.size()]);
	}

	public Point2D [] getFUV(){
		return fUV.toArray(new Point2D[fUV.size()]);
	}

	public Point2D [] getFTV(){
		return fTV.toArray(new Point2D[fTV.size()]);
	}

	public Point2D [] getFPT(){
		return fPT.toArray(new Point2D[fPT.size()]);
	}

	public Point2D [] getFST(){
		return fST.toArray(new Point2D[fST.size()]);
	}

	public Point2D [] getFUT(){
		return fUT.toArray(new Point2D[fUT.size()]);
	}

	public Point2D [] getPPV(){
		return pPV.toArray(new Point2D[pPV.size()]);
	}

	public Point2D [] getPUV(){
		return pUV.toArray(new Point2D[pUV.size()]);
	}

	public Point2D [] getPTV(){
		return pTV.toArray(new Point2D[pTV.size()]);
	}

	public Point2D [] getPPT(){
		return pPT.toArray(new Point2D[pPT.size()]);
	}

	public Point2D [] getPST(){
		return pST.toArray(new Point2D[pST.size()]);
	}

	public Point2D [] getPUT(){
		return pUT.toArray(new Point2D[pUT.size()]);
	}

	public Point2D getVLimits(){
		double inf = Vmin*N*1000;
		double sup = Vmax*N*1000;
		double modif = 0.3;

		return new Point2D.Double(inf - inf*modif, sup + sup*modif);
	}

	public Point2D getPLimits(){
		double sup = (R*T1)/(Vmin*101325);
		double inf = (R*T2)/(Vmax*101325);
		double modif = 0.1;

		return new Point2D.Double(inf - inf*modif, sup + sup*modif);
	}

	public Point2D getULimits(){
		double sup = (R*T1*3)/2;
		double inf = (R*T2*3)/2;
		double modif = 0.1;

		return new Point2D.Double(inf - inf*modif, sup + sup*modif);
	}

	public Point2D getTLimits(){
		double sup = T1;
		double inf = T2;
		double modif = 0.1;

		return new Point2D.Double(inf - inf*modif, sup + sup*modif);
	}

	public Point2D getSLimits(){
		double sup = R*Math.log(Math.pow(((3.0/2.0)*R*T2), 3.0/2.0)*Vmax) -  R*Math.log(Math.pow(((3.0/2.0)*R*T1), 3.0/2.0)*Vmin);
		double inf = -1;
		double modif = 0.5;

		return new Point2D.Double(inf, sup + sup*modif);
	}


	public boolean isFirstCompleted() {
		return firstCompleted;
	}




}