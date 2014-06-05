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
	private double T1, T2, P, Qo;
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
	}

	private void initVariables(){
		switch(type){
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		}
	}

	private int getPeso() {
		switch(type){
		case 1:
			return -1;
		case 2:
			return -1;
		case 3:
			return -1;
		default:
			return -1;
		}
	}
	
	/**
	 * Actualizes the simulation according the actual time
	 */
	@Override
	public void simulate() {
		
		switch(currentPhase){
		case PHASE_1:
			Qtotal = Qo;
			break;
		case PHASE_2:
			Qc = m*Cv*T1 + m*L - m*cv*T2;
			Qtotal = Qo + Qc;
			break;
		case PHASE_3:
			Qe = m*Cv*(T1-T2);
			Qtotal = Qo + Qc;
			break;
		}
		
		E = T2/(T1 - T2);
		t = Qtotal/P;
	}


	/** GETTERS & SETTERS **/
}