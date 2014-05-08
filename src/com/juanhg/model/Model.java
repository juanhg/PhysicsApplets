/*  -----------------------------------------------------------------
 	 @file   Model.java
     @author Juan Hernandez Garcia 
     @brief Abstract class that represents any Model
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


package com.juanhg.model;



/**
 * Abstract class that represent a Fisic Model
 * @author Juan Hernandez Garcia
 */
public abstract class Model {

	//Initial time
	public double initTime;

	//Final time
	public double finalTime;

	//Actual time
	public double currentTime;
	
	public double lastTime;
	
	//Increment of Time
	public double dt;
	
	public  int totalSimulations;
	public int actualSimulation;

	/**
	 * Function that runs the simulation
	 */
	public abstract void simulate();
	
	/**
	 * @return True if the final Time has been reached.
	 * False in other case.
	 */
	public boolean finalTimeReached(){
		if(getCurrentTime() > finalTime){
			return true;
		}
		return false;
	}
	
	public void incrementCurrentTime(){
		currentTime += dt;
	}
 
	public int getActualSimulation() {
		return actualSimulation;
	}

	public int getTotalSimulations() {
		return totalSimulations;
	}

	public double getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(double currentTime) {
		this.currentTime = currentTime;
	}

	public double getLastTime() {
		return lastTime;
	}

	public void setLastTime(double lastTime) {
		this.lastTime = lastTime;
	}
	
	
	
}
