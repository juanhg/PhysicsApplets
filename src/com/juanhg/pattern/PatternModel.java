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


package com.juanhg.pattern;

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
public class PatternModel extends Model {

	//Stages or phases of the model
	static final int PHASE_1 = 1;
	static final int PHASE_2 = 2;
	static final int PHASE_3 = 3;
	static final int PHASE_4 = 4;


	private int currentPhase;
	private Time t;

	//Constants


	//Input parameters
	private double I1, I2, I3, I4, I5;

	//Calculated parameters

	//Aux
	double temp1, temp2, temp3, temp4, temp5;


	public PatternModel(double T1, double T2, double Vmin, double Vmax, double N, boolean hot){ 
		this.I1 = T1;
		this.I2 = T2;
		this.I3 = Vmin;
		this.I4 = Vmax;
		this.I5 = N;

		t = new Time();

		//Parametros calculados iniciales
	}


	/**
	 * Actualizes the simulation according the actual time
	 */
	@Override
	public void simulate() {}


	private boolean nextPhaseReached() {
		switch(currentPhase){
		case PHASE_1:
			break;
		case PHASE_2:
			break;
		case PHASE_3:
			break;
		case PHASE_4:
			break;
		}
		return false;
	}

	private void jumpToNextPhase(){
		switch(currentPhase){
		case PHASE_1:
			//jumpToPhase(PHASE_2);
			break;
		case PHASE_2:
			//jumpToPhase(PHASE_3);
			break;
		case PHASE_3:
			//jumpToPhase(PHASE_4);
			break;
		case PHASE_4:
			//jumpToPhase(PHASE_1);
			break;
		}
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
}