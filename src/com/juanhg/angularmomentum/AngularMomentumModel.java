package com.juanhg.angularmomentum;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.juanhg.util.*;

/**
 *   @file   AngularMomentumModel.java
 *   @author Juan Hernandez Garcia 
 *   @brief Model that represents the loss of angular momentum 
 */
public class AngularMomentumModel extends Model {
	
//	final static double simulaciones = 21000000;
	final static double simulaciones = 500;

	//Initial Mass of the star
	double initMass;
	
	//Actual Mass of the star
	double actualMass;
	
	//Final Mass
	double finalMass;
	
	//Velocity that weight is lost
	double velocity;
	
	//Initial distance of the planet to the star
	double initDistance;
	
	double previousDistance;
	
	//Actual distance of the planet to the star
	double actualDistance;
	
	//Final distance of the planet to the star
	double finalDistance;
	
	//Angle
	double phi;
	
	//Final Angle
	double finalPhi; 
	
	//Initial time
	double initTime;
	
	//Final time
	double finalTime;
	
	//Actual time
	double actualTime;
	
	//Increment of Time
	double dt;
	
	//Decreemnt of Mass for each simulation
	double dMass;
	
	//Initial velocity of the planet
	double Vo;
	
	//Trajectory followed by the planet along the simulation
	List<Point2D> trajectory;
	
	//Point 2D that represents the star
	Point2D star;
	
	public AngularMomentumModel(){
		star = new Point2D.Double(0,0);
		
		//Inputs values
		this.initMass = this.actualMass = 0;
		this.finalMass = 0;
		this.velocity = 0;
		this.initDistance = this.actualDistance  = previousDistance= 0;
		
		// Initial point of trajectory (where the planet begins) 
		trajectory = new ArrayList<Point2D>();
		trajectory.add(new Point2D.Double(initDistance, 0));
	}
	
	public AngularMomentumModel(double initMass, double finalMass, double velocity, double distance){
		
		final double VoModifier = 5.9*Math.pow(10.0, 24);
		
		if(initMass <= 0 || velocity <= 0){
			System.err.println("Initial Mass and Velocity must be positive values");
			System.exit(1);
		}
		
		if(finalMass >= 1){
			System.err.println("The % of final Mass can't be 1 or superior!!");
			System.exit(3);
		}
		
		//Inputs values
		this.initMass = this.actualMass = initMass;
		this.finalMass = finalMass;
		this.velocity = velocity;
		this.initDistance = this.actualDistance = distance;
		
		/** Calculated Values **/
		this.initTime = this.actualTime =  0;
		this.phi = 0;
		this.finalTime = (1 - this.finalMass)/this.velocity;
		this.dt = finalTime/simulaciones;
		this.dMass = (this.initMass - (this.initMass*this.finalMass))/simulaciones;
		this.Vo = Math.sqrt(VoModifier*(this.initMass/this.initDistance));
				
		// Initial point of trajectory (where the planet begins) 
		trajectory = new ArrayList<Point2D>();
		trajectory.add(new Point2D.Double(initDistance, 0));
		
		star = new Point2D.Double(0,0);
	}
	
	@Override
	public void simulate() {
		double term1, term2, term3, term4, term5;
		PolarPoint2D polarCoordinates;
		
		//Increments the actual time
		actualTime += dt;
		
		//Calculates the new radius and distance
		if(!finalTimeReached()){
			//Actualizes Mass
			this.actualMass -= this.dMass;
			
			//r
			this.actualDistance = this.initDistance/(1-(this.velocity/this.actualMass)*this.actualTime);
			
			if(this.actualDistance < this.previousDistance){
				System.err.println("Function out of range!!");
			}
			previousDistance = actualDistance;
			
			//phi
			term1 = this.Vo/this.initDistance;
			term2 = this.actualTime;
			term3 = (this.velocity * Math.pow(this.actualTime, 2)) / this.initMass;
			term4 = (Math.pow(this.velocity,2) * Math.pow(this.actualTime, 3)) / this.initMass;
			term5 = (Math.pow(this.velocity,2) * Math.pow(this.actualTime, 3)) / (3 * Math.pow(this.initMass,2));
			this.phi = term1 * (term2 - term3 + term4 + term5);
			this.finalPhi = this.phi;
		}
		else{
			
			System.out.println("Final Time Reached!!");
			this.finalDistance = this.actualDistance;
			
			term1 = Math.pow((1 -((this.velocity/this.initMass)*this.finalTime)),2.0);
			term2 = (this.Vo/this.initDistance)*this.actualTime;
			term3 = this.finalPhi;
			
			this.phi = term1*term2+term3;
		}
		
		polarCoordinates = new PolarPoint2D(this.actualDistance,this.phi);
		trajectory.add(polarCoordinates.toCartesianPoint());
	}

	public List<Point2D> getTrajectory() {
		return trajectory;
	}
	
	/**
	 * Gets the trajectory as an array of Point2Ds object
	 * @return An array that is the trajectory
	 */
	public Point2D [] getTrajectoryAsArray(){
		return trajectory.toArray(new Point2D[trajectory.size()]);
	}
	
	/**
	 * Obtains the actual coordinates of the planet
	 * @return A Point2D that indicates the actual position of the planet
	 */
	private Point2D planetCoordinates(){
		return this.trajectory.get(this.trajectory.size()-1);
	}
	
	/**
	 * Gets the planet as an array of Point2Ds object
	 * @return An array of one point that is the planet
	 */
	public Point2D [] getPlanetAsArray(){
		Point2D planet [] = new Point2D[1];
		planet[0] = new Point2D.Double();
		planet[0] = this.planetCoordinates();
		return planet;
	}
	
	/**
	 * Gets the star as an array of Point2Ds object
	 * @return An array of one point that is the star
	 */
	public Point2D [] getStarAsArray(){
		Point2D star [] = new Point2D[1];
		star[0] = new Point2D.Double();
		star[0] = this.star;
		return star;
	}
	
	
	public boolean finalTimeReached(){
		return (this.actualTime >= this.finalTime);
	}

	public double getFinalMass() {
		return finalMass;
	}

	public double getFinalTime() {
		return finalTime;
	}

	public double getActualTime() {
		return actualTime;
	}

	public double getFinalDistance() {
		return finalDistance;
	}
	
	

}
