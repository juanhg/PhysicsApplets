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


package com.juanhg.fridge;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

import com.juanhg.model.Model;

/**
 *   @file   AngularMomentumModel.java
 *   @author Juan Hernandez Garcia 
 *   @brief Model that represents the loss of angular momentum 
 */
public class FridgeModel extends Model {

	//Stages or phases of the model
	static final int PHASE_1 = 1;
	static final int PHASE_2 = 2;
	static final int PHASE_3 = 3;
	static final int PHASE_4 = 4;


	private int currentPhase;


	//Constants
	double E,t, Qtotal, Qe, Qc, m, Cv, cv, L;

	//Input parameters
	private double T1, T2, P, Qo, Td;
	private int type, n;

	//Calculated parameters



	public FridgeModel(double T1, double T2, double P, double Qo, int type, int n, int phase){ 
		this.T1 = T1;
		this.T2 = T2;
		this.P = P;
		this.Qo = Qo;
		this.type = type;
		this.n = n;
		
		m = getPeso()*n;

		if(phase == PHASE_1){
			currentPhase = PHASE_1;
		}
		else{
			if(type == 3){
				currentPhase = PHASE_3;
			}
			else{
				currentPhase = PHASE_2;
			}
		}

		//Parametros calculados iniciales
		this.initVariables();
	}
	
	public void restart(double T1, double T2, double P, double Qo, int type, int n, int phase){
		this.T1 = T1;
		this.T2 = T2;
		this.P = P;
		this.Qo = Qo;
		this.type = type;
		this.n = n;
		
		m = getPeso()*n;

		if(phase == PHASE_1){
			currentPhase = PHASE_1;
		}
		else{
			if(type == 3){
				currentPhase = PHASE_3;
			}
			else{
				currentPhase = PHASE_2;
			}
		}

		//Parametros calculados iniciales
		this.initVariables();
	}

	public double getTd() {
		return Td;
	}

	public void setTd(double td) {
		Td = td;
	}

	private void initVariables(){
		switch(type){
		case 1:
			Cv = 1770;
			cv = 954;
			L = 239000;
			break;
		case 2:
			Cv = 2163;
			cv = 1140;
			L = 288000;
			break;
		case 3:
			Cv = 126;
			break;
		}
	}

	private double getPeso() {
		switch(type){
		case 1:
			return 0.5;
		case 2:
		case 3:
			return 1;
		default:
			return -1;
		}
	}
	
	
	
	public double getE() {
		return E;
	}

	public void setE(double e) {
		E = e;
	}

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	/**
	 * Actualizes the simulation according the actual time
	 */
	@Override
	public void simulate() {
		
		switch(currentPhase){
		case PHASE_1:
			Qtotal = 10000;
			Td = 10000.0/Qo;
			break;
		case PHASE_2:
			Qc = m*Cv*T1 + m*L - m*cv*T2;
			Qtotal =  Qc;
			Td = 0;
			break;
		case PHASE_3:
			Qe = m*Cv*(T1-T2);
			Qtotal = Qe;
			Td = 0;
			break;
		}
		
		E = (T2+273)/(T1 - T2);
		t = Qtotal/P;
	}


	/** GETTERS & SETTERS **/
}