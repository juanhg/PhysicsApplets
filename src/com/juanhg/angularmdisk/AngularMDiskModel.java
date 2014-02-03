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


package com.juanhg.angularmdisk;

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
public class AngularMDiskModel extends Model {
	
	static final int INIT_PHASE = 0;
	static final int BUG_FALLS_PHASE = 1;
	static final int FINAL_PHASE = 2;
	
	final double diskMass = 200; //M
	final double diskRadius = 0.2; //R
	
	PolarPoint2D bugCoordinates;

	double bugMass; //m
	double bugVelocity; //v
	double bugInitRadius; //ro
	double bugFinalRadius; //rf
	
	double diskInitVelocity; //Wo
	double diskPhi;
	
	int phase;
	
	double Wf, Wff, tf0, tf1;
	double finalDiskPhi;
	
	
	public AngularMDiskModel(){
		bugCoordinates = new PolarPoint2D();
	}
	
	public AngularMDiskModel(double bugMass, double bugInitRadius, double bugVelocity, double diskInitVelocity){ 
	
		
		this.bugMass = bugMass;
		this.bugInitRadius = bugInitRadius;
		this.bugVelocity = bugVelocity;
		this.diskInitVelocity = 2.0*Math.PI*diskInitVelocity;
		this.diskPhi = 0;
		
		this.bugCoordinates = new PolarPoint2D(bugInitRadius, 0);
		
		this.actualTime = this.initTime = 0;
		
		this.bugFinalRadius = 0;
		
		this.calculateWf();
		this.calculateWff();
			
		//tf
		tf1 = (Math.abs(this.bugInitRadius-this.bugFinalRadius))/this.bugVelocity;
		
	}
	
	@Override
	public void simulate() {
		
		double temp1, temp2, temp3, temp4;
		
		switch(phase){
		case INIT_PHASE:
			this.diskPhi = (this.diskInitVelocity*this.actualTime) % (Math.PI*2);
			this.tf0 = this.actualTime;
			this.finalDiskPhi = this.diskPhi;
			break;
		case BUG_FALLS_PHASE:
			
			temp1 = (Wf-Wff)/tf1;
			temp2 = Math.pow(this.actualTime, 2.0)/2.0;
			temp3 = Wf*this.actualTime;
			temp4 = this.diskInitVelocity*tf0;
			this.diskPhi = ((temp1*temp2) + temp3 + temp4) % (Math.PI*2);
			
			this.bugCoordinates.setPhi(finalDiskPhi-this.diskPhi);
			double auxRadius = this.bugInitRadius - this.bugVelocity*this.actualTime;
			if(auxRadius > 0 && auxRadius <= this.diskRadius){
				this.bugCoordinates.setRadius(auxRadius);
			}
			else{
				this.phase = FINAL_PHASE;
			}
			break;
		case FINAL_PHASE:
			break;
		}
	}
	
	/**
	 * Gets the bug coordinates as an array of Point2Ds object
	 * @return An array of one point that is the planet
	 */
	public Point2D [] getBugsCoordinatesAsArray(){
		Point2D bug [] = new Point2D[1];
		bug[0] = new Point2D.Double();
		bug[0] = this.bugCoordinates.toCartesianPoint();
		return bug;
	}
	

		
	public boolean finalTimeReached(){
		return (this.actualTime >= this.finalTime);
	}

	public double getFinalTime() {
		return finalTime;
	}

	public double getActualTime() {
		return actualTime;
	}

	public double getDiskPhi() {
		return diskPhi;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public PolarPoint2D getBugCoordinates() {
		return bugCoordinates;
	}

	public double getBugInitRadius() {
		return bugInitRadius;
	}

	public void setBugInitRadius(double bugInitRadius) {
		this.bugInitRadius = bugInitRadius;
	}

	public void setBugCoordinates(PolarPoint2D bugCoordinates) {
		this.bugCoordinates = bugCoordinates;
	}
	
	

	public double getBugVelocity() {
		return bugVelocity;
	}

	public void setBugVelocity(double bugVelocity) {
		this.bugVelocity = bugVelocity;
	}

	public void calculateWf(){
		
		double temp1, temp2, temp3, temp4;
		//Wf
		temp1 = this.diskInitVelocity;
		temp2 = 1;
		temp3 = (2*this.bugMass/this.diskMass);
		temp4 = Math.pow(this.bugInitRadius/this.diskRadius, 2.0);
		Wf = temp1/(temp2 + (temp3*temp4));
		

	}
	
	public void calculateWff(){
		
		double temp1, temp2, temp3, temp4;
		//Wff
		temp1 = this.bugMass*(Math.pow(this.bugInitRadius,2));
		temp2 = 0.5*this.diskMass*(Math.pow(this.diskRadius, 2));
		temp3 = this.bugMass*(Math.pow(this.bugFinalRadius,2));
		Wff = (temp1 + temp2)/(temp2+temp3);
	}
	
	
	
	

	
	
	
	
	
	
	

}
