package com.juanhg.model;


/**
 * Abstract class that represent a Fisic Model
 * @author Juan Hernandez Garcia
 */
public abstract class Model {

	//Initial time
	double initTime;

	//Final time
	double finalTime;

	//Actual time
	double actualTime;

	//Increment of Time
	double dt;

	/**
	 * Function that runs the simulation
	 */
	public abstract void simulate();
	
	/**
	 * @return True if the final Time has been reached.
	 * False in other case.
	 */
	public abstract boolean finalTimeReached();

}
