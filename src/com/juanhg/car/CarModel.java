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


package com.juanhg.car;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import com.juanhg.model.Model;
import com.juanhg.util.Time;

/**
 *   @file   CarModel.java
 *   @author Juan Hernandez Garcia 
 */
public class CarModel extends Model {

	//Stages or phases of the model
	static final int PHASE_1 = 1;
	static final int PHASE_2 = 2;
	static final int PHASE_3 = 3;
	static final int PHASE_4 = 4;

	List<Point2D> chart1 = new ArrayList<Point2D>();
	List<Point2D> chart2 = new ArrayList<Point2D>();
	List<Point2D> chart3 = new ArrayList<Point2D>();
	List<Point2D> chart4 = new ArrayList<Point2D>();
	Point2D currentPoint = new Point2D.Double();

	boolean isRound1 = true;


	private int currentPhase;
	private Time t;

	//Increments
	double K = 5000;
	double k = 10000;
	
	//Constants

	int vueltas = 0;
	
	//Input parameters
	private double Va, r, mg, Vcar, Qc;

	//Outputs
	private double Wc, Wt, eta, nciclos, Tmax, h, x, F, tiempo;

	//Calculated parameters
	double P, Pa, Pmax; //Pa es de entrada o cual es?
	double V, V1, V2;
	
	double a = 0.20;

	//Aux
	double temp1, temp2, temp3, temp4, temp5;

	//Salidas eta, Tmax, F, x,

	public CarModel(double Va, double r, double mg, double Vcar, double Qc){ 
		this.Va = Va/1000.0;
		this.r = r;
		this.mg = mg;
		this.Vcar = Vcar;
		this.Qc = Qc;

		//Parametros calculados iniciales
		Pa = Math.pow(10.0, 5.0);
		P = Pa;
		V1 = this.Va;
		V2 = V1/r;
		eta = 1 - (1/(Math.pow(r, 0.4)));
		Wc = 1.225*V1*eta*Qc*1000;
		Tmax = 300 + (Qc*28.97)/(2.5*8.31);
		nciclos = 47000000*mg/(Qc*1000*1.225*V1);
		Wt = nciclos * Wc;
		F = 0.5*1.225*2.13*0.32*Vcar + 120;
		x = Wt/F;
		tiempo = x/Vcar;
		Pmax = r*Pa*Tmax/300.0;
		
		currentPhase = PHASE_1;
		
		Point2D point = new Point2D.Double(V1*1000, Pa/Math.pow(10.0, 5.0));
		chart1.add(point);
	}


	public double getEta() {
		return eta;
	}


	public double getTmax() {
		return Tmax;
	}


	public double getF() {
		return F;
	}


	public double getWc() {
		return Wc;
	}

	

	public double getPa() {
		return Pa/Math.pow(10.0, 5.0);
	}


	public double getPmax() {
		return Pmax/Math.pow(10.0, 5.0);
	}


	public void setWc(double wc) {
		Wc = wc;
	}


	public double getNciclos() {
		return nciclos;
	}


	public void setNciclos(double nciclos) {
		this.nciclos = nciclos;
	}


	public double getH() {
		return h = V/(Math.PI*Math.pow(a, 2.0));
	}
	
	public double getHMax(){
		return Va/(Math.PI*Math.pow(a, 2.0));
	}

	public double getHMin(){
		return V2/(Math.PI*Math.pow(a, 2.0));
	}

	public void setH(double h) {
		this.h = h;
	}


	public double getP() {
		return P/Math.pow(10.0, 5.0);
	}


	public void setP(double p) {
		P = p;
	}


	public double getV() {
		return V*1000;
	}


	public void setV(double v) {
		V = v;
	}

	
	
	@Override
	public void simulate() {
		if(incrementTime()){
			jumpToNextPhase();
		}
		else{
			calculateV();
			addPoint();
		}
	}

	public double getV1() {
		return V1*1000;
	}


	public void setV1(double v1) {
		V1 = v1;
	}


	public double getV2() {
		return V2*1000;
	}


	public void setV2(double v2) {
		V2 = v2;
	}


	private void calculateV(){
		switch(currentPhase){
		case PHASE_1:
			V = Math.pow((P /(Pa*Math.pow(V1,1.4))), 1.0/-1.4);
			break;
		case PHASE_2:
			V = V2;
			break;
		case PHASE_3:
			V = Math.pow((P /(Pmax*Math.pow(V2,1.4))), 1.0/-1.4);
			break;
		case PHASE_4:
			V = V1;
			break;
		}
	}
	
	private void addPoint(){
		Point2D point = new Point2D.Double(V*1000, P/Math.pow(10.0, 5.0));
		currentPoint = point;
		
		if(isRound1){
			switch(currentPhase){
			case PHASE_1:
				chart1.add(point);
				break;
			case PHASE_2:
				chart2.add(point);
				break;
			case PHASE_3:
				chart3.add(point);
				break;
			case PHASE_4:
				chart4.add(point);
				break;
			}
		}
	}


	/**
	 * Increment the time and check if the next phase has reached
	 * @return
	 */
	private boolean incrementTime() {
		double limit = 0;
		switch(currentPhase){
		case PHASE_1:
			P += k;
			limit = (Pa*Math.pow(V1,1.4))/(Math.pow(V2,1.4));
			if(P >= limit){
				P = limit;
				return true;
			}
			break;
		case PHASE_2:
			P += K;
			limit = Pmax;
			if(P >= Pmax){
				P = Pmax;
				return true;
			}
			break;
		case PHASE_3:
			P -= k;
			limit = (Pmax*Math.pow(V2,1.4))/(Math.pow(V1,1.4));
			if(P <= limit){
				P = limit;
				return true;
			}
			break;
		case PHASE_4:
			P -= K;
			limit = Pa;
			if(P <= limit){
				P = limit;
				return true;
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
			jumpToPhase(PHASE_3);
			break;
		case PHASE_3:
			jumpToPhase(PHASE_4);
			break;
		case PHASE_4:
			jumpToPhase(PHASE_1);
			vueltas++;
			break;
		}
		addPoint();
	}

	


	public int getCurrentPhase() {
		return currentPhase;
	}


	public void setCurrentPhase(int currentPhase) {
		this.currentPhase = currentPhase;
	}


	public int getVueltas() {
		return vueltas;
	}


	public void setVueltas(int vueltas) {
		this.vueltas = vueltas;
	}


	public double getX() {
		return x;
	}


	public void jumpToPhase(int phase) {
		this.currentPhase = phase;
		lastTime = 0;

		t = new Time();
		t.start();
	}

	/** GETTERS & SETTERS **/

	public int getPhase() {
		return currentPhase;
	}

	public Time getTime() {
		return t;
	}
	
	public Point2D [] currentPointAsArray(){
		Point2D [] point = new Point2D.Double[1];
		point[0] = currentPoint;
		return point;
	}
	
	public Point2D [] chart1AsArray(){
		return chart1.toArray(new Point2D[chart1.size()]);
	}
	public Point2D [] chart2AsArray(){
		return chart2.toArray(new Point2D[chart2.size()]);
	}
	public Point2D [] chart3AsArray(){
		return chart3.toArray(new Point2D[chart3.size()]);
	}
	public Point2D [] chart4AsArray(){
		return chart4.toArray(new Point2D[chart4.size()]);
	}
}