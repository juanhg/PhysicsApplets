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


package com.juanhg.angularmplanet;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.juanhg.model.Model;
import com.juanhg.util.PolarPoint2D;

/**
 *   @file   AngularMomentumModel.java
 *   @author Juan Hernandez Garcia 
 *   @brief Model that represents the loss of angular momentum 
 */
public class AngularMPlanetModel extends Model {
	
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
	
	//Increment of Time
	double dt;
	
	//Decreemnt of Mass for each simulation
	double dMass;
	
	//Initial velocity of the planet
	double Vo;
	
	double ActualPeriod;
	
	double velocityModifier;
	
	//Trajectory followed by the planet along the simulation
	List<Point2D> trajectory;
	
	//Point 2D that represents the star
	Point2D star;
	
	public AngularMPlanetModel(){
		star = new Point2D.Double(0,0);
		
		//Inputs values
		this.initMass = this.actualMass = 0;
		this.finalMass = 0;
		this.velocity = 0;
		this.initDistance = this.actualDistance  = previousDistance= 0;
		this.totalSimulations = 1000;
	
		
		// Initial point of trajectory (where the planet begins) 
		trajectory = new ArrayList<Point2D>();
		trajectory.add(new Point2D.Double(initDistance, 0));
	}
	
	public AngularMPlanetModel(double initMass, double finalMass, double velocity, double distance, int simulations){
		
		final double VoModifier = 5.9*Math.pow(10.0, 24);
		double temp1, temp2, temp3, temp4;
		
		if(initMass < 0.1 || initMass > 80){
			System.err.println("Initial Mass must be in the range [0.1,30]");
			System.exit(1);
		}
		
		if(velocity < 0.001 || velocity > 15){
			System.err.println("Velocity must be in the range [0.001, 1]");
			System.exit(2);
		}
		
		if(finalMass < 0.6 || finalMass > 0.95){
			System.err.println("The % of final Mass must be in the range [0.05, 0.6]");
			System.exit(3);
		}
		
		if(distance < 0.3 || distance > 50){
			System.err.println("The initial radio must be in the range [0.3, 50]");
			System.exit(3);
		}
		
		//Inputs values
		this.initMass = this.actualMass = initMass;
		this.finalMass = finalMass;
		
		double auxFinalTime = (1 - this.finalMass)/(velocity);
		velocityModifier = 10.0;
		
		
		if(auxFinalTime > 1){
			velocityModifier = 10.0;
		}
		else if(auxFinalTime < 1.5 && auxFinalTime > 0.5){
			velocityModifier = 9.0;
		}
		else if(auxFinalTime < 0.5 && auxFinalTime > 0.01){
			velocityModifier = 8.0;
		}
		else{
			velocityModifier = 7.0;
		}
		
		this.velocity = velocity*Math.pow(10.0, velocityModifier);
		this.initDistance = this.actualDistance = distance;
		this.totalSimulations = simulations;
		
		/** Calculated Values **/
		this.initTime = this.actualTime =  0;
		this.phi = 0;
		this.finalTime = (1 - this.finalMass)/this.velocity;
		this.dt = finalTime/this.totalSimulations;
		this.dMass = ((this.initMass -(this.initMass*this.finalMass)))/this.totalSimulations;
		this.Vo = Math.sqrt(VoModifier*(this.initMass/this.initDistance));
		
		/* rf */
		temp1 = this.initDistance;
		temp2 = 1;
		temp3 = this.velocity/(this.initMass);
		temp4 = this.finalTime;
		this.finalDistance = temp1/(temp2 - (temp3*temp4));
		
		
		temp1 = (2.0*Math.PI);
		temp2 = Math.pow((1-((this.velocity*this.actualTime)/this.initMass)),2.0);
		temp3 = this.Vo/(this.initDistance* 1.5*Math.pow(10, 11));
		temp4 = 1.157*Math.pow(10.0, -5.0);
		this.ActualPeriod = (temp1/(temp2*temp3))*temp4;
				
		// Initial point of trajectory (where the planet begins) 
		trajectory = new ArrayList<Point2D>();
		trajectory.add(new Point2D.Double(initDistance, 0));
		
		star = new Point2D.Double(0,0);
	}
	
	@Override
	public void simulate() {
		double term1, term2, term3, term4, term5;
		double temp1, temp2, temp3, temp4;
		PolarPoint2D polarCoordinates;
		actualSimulation++;
		
		//Increments the actual time
		actualTime += dt;
		
		//Calculates the new radius and distance
		if(!finalTimeReached()){
			//Actualizes Mass
			this.actualMass -= this.dMass;
			
			
			/* r */
			temp1 = this.initDistance;
			temp2 = 1;
			temp3 = this.velocity/(this.initMass);
			temp4 = this.actualTime;
			this.actualDistance = temp1/(temp2 - (temp3*temp4));
			
			temp1 = (2.0*Math.PI);
			temp2 = Math.pow((1-((this.velocity*this.actualTime)/this.initMass)),2.0);
			temp3 = this.Vo/(this.initDistance* 1.5*Math.pow(10, 11));
			temp4 = 1.157*Math.pow(10.0, -5.0);
			this.ActualPeriod = (temp1/(temp2*temp3))*temp4;
			
				
			//System.out.print("Phi: " + this.phi);
		}
		
		//phi
		term1 = this.Vo/this.initDistance;
		term2 = this.actualTime;
		term3 = (this.velocity * Math.pow(this.actualTime, 2)) / this.initMass;
		term4 = (Math.pow(this.velocity,2) * Math.pow(this.actualTime, 3)) / this.initMass;
		term5 = (Math.pow(this.velocity,2) * Math.pow(this.actualTime, 3)) / (3 * Math.pow(this.initMass,2));
		this.phi = term1 * (term2 - term3 + term4 + term5);
		this.finalPhi = this.phi;	
		

	
		
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
	public Point2D getPlanet(){
		return this.trajectory.get(this.trajectory.size()-1);
	}
	
	/**
	 * Gets the planet as an array of Point2Ds object
	 * @return An array of one point that is the planet
	 */
	public Point2D [] getPlanetAsArray(){
		Point2D planet [] = new Point2D[1];
		planet[0] = new Point2D.Double();
		planet[0] = this.getPlanet();
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

	public double getActualPeriod() {
		return ActualPeriod;
	}

	public void setActualPeriod(double actualPeriod) {
		ActualPeriod = actualPeriod;
	}

	public double getActualDistance() {
		return actualDistance;
	}

	public double getVelocityModifier() {
		return velocityModifier;
	}
	
	
	
	
	
	

}
