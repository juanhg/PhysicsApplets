/*  -----------------------------------------------------------------
 	 @file   AngularMomentumApplet.java
     @author Juan Hernandez Garcia 
     @brief Applet that simulates the movement of a planet around a star. 
 	 The star loses weight, and the orbit of the planet grows.
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
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.annotations.XYAnnotation;

import com.juanhg.util.ImageProcessing;
import com.juanhg.util.PolarPoint2D;
import com.juanhg.util.Time;
import com.raccoon.easyjchart.Grafica;
import com.raccoon.easyjchart.JPanelGrafica;
import javax.swing.JTabbedPane;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.SwingConstants;


public class AngularMDiskApplet extends JApplet implements Runnable {
	
	private static final long serialVersionUID = -3261548917574875054L;

	//Time variables

	double sleepTime = 50;	
	boolean end = false;
	private final double bugOrientation = -(1.5*Math.PI);
	
	double m, r0, W, v, mu;
	
	//Thread that executed the simlation
    private Thread flujo = null;
      
    //Model
    private AngularMDiskModel model;
    
    //Charts
    private Grafica chart; 
    
    //ChartPanels
	private JPanelGrafica panelSimulation;
	
	int supXLimit = 23;
	int infXLimit = -23;
	int supYLimit = 23;
	int infYLimit = -23;
	
	//Annotations
	XYAnnotation diskAnnotation = null;
	XYAnnotation bugAnnotation = null; 
	XYAnnotation targetAnnotation = null;
	
	BufferedImage bugImage, diskImage,  roseImage, targetImage;
	BufferedImage rotatedBugImage, rotatedDiskImage; 
	BufferedImage backgroundImage; 

	//Labels
    private JLabel lblDiskWValue;  
    private JLabel lblFallRadiusValue, lblBugVelocityValue, lblVelocityValue, lblInitMassValue;
    private JLabel lblFrictionValue, lblDiskW;
    
    //Sliders
    private JSlider sliderBugInitMass, sliderFallRadius, sliderBugVelocity; 
    private JSlider sliderDiskVelocity, sliderFriction;

	//Buttons
	JButton btnPhase1, btnLaunchSimulation, btnPauseContinue;
    
   
	public AngularMDiskApplet() {}

	public void init(){
		try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        repaint();
	}
	
	
	public void initComponents(){
		
		setSize(1040,610);
		
		this.autogeneratedCode();
		
		
//		Thread musicThread  = new AePlayWave("./com/juanhg/diskangularmomentum/win.wav");
//		musicThread.start();
	
	
		//Obtain values from interface
		this.readInputs();
		this.initSimulation();	
	}
	
	

	void sliderBugInitMassEvent(){

		if(sliderBugInitMass.getValueIsAdjusting()){
			lblInitMassValue.setText(Double.toString((double)sliderBugInitMass.getValue()));
			repaint();
		}
	}
	void sliderDiskVelocityEvent(){

		if(sliderDiskVelocity.getValueIsAdjusting()){
			lblVelocityValue.setText(Double.toString(((double)sliderDiskVelocity.getValue()/10.0)));
			repaint();
		}
	}
	void sliderBugVelocityEvent(){

		if(sliderBugVelocity.getValueIsAdjusting()){
			lblBugVelocityValue.setText(Double.toString((double)sliderBugVelocity.getValue()/10.0));
		}
	}
	
	void sliderFallRadiusEvent(){

		if(sliderFallRadius.getValueIsAdjusting() && model.getPhase() == AngularMDiskModel.PHASE_0){
						
			chart.deleteImage(targetAnnotation);
			targetAnnotation = chart.setImageAtPoint(targetImage, sliderFallRadius.getValue(), 0);
			lblFallRadiusValue.setText(Double.toString((double)sliderFallRadius.getValue()));
			
			end = false;
			repaint();
		}
	}
	
	void sliderFrictionEvent(){
		if(sliderFriction.getValueIsAdjusting()){
			lblFrictionValue.setText( Double.toString((double)sliderFriction.getValue()/100.0));
		}
	}
	
	void btnLaunchSimulationEvent(ActionEvent event){
		
		boolean buttonsOn = false;
		
		if(flujo != null && flujo.isAlive()) {
			end = true;
			buttonsOn = true;

			while(flujo.isAlive()) {}
			
			model.getT().stop();

			chart.deleteImage(bugAnnotation);
			chart.deleteImage(diskAnnotation);
			
			this.readInputs();
			this.initSimulation();
			
			btnLaunchSimulation.setText("Iniciar");
			btnPhase1.setEnabled(false);
			
			sliderFriction.setEnabled(buttonsOn);
			sliderBugInitMass.setEnabled(buttonsOn);
			sliderBugVelocity.setEnabled(buttonsOn);
			sliderDiskVelocity.setEnabled(buttonsOn);
			sliderFallRadius.setEnabled(buttonsOn);
			
			repaint();
		
		}
		else{
			
			buttonsOn = false;
			btnLaunchSimulation.setText("Finalizar");
			btnPauseContinue.setText("Pausar");
			btnPhase1.setEnabled(true);

			//Obtain values from interface
			this.readInputs();
			this.initSimulation();

			//Initializes and runs the thread (Run())
			flujo = new Thread();
			flujo = new Thread(this);
			
			sliderFriction.setEnabled(buttonsOn);
			sliderBugInitMass.setEnabled(buttonsOn);
			sliderBugVelocity.setEnabled(buttonsOn);
			sliderDiskVelocity.setEnabled(buttonsOn);
			sliderFallRadius.setEnabled(buttonsOn);

			model.getT().start();
			flujo.start();
		}
	}
	
	void btnPauseContinueEvent(ActionEvent event){
		   if(flujo != null && flujo.isAlive()) {
			   btnPauseContinue.setText("Continuar");
			    end = true;
	            while(flujo.isAlive()) {}
	            model.getT().pause();
	            btnPhase1.setEnabled(false);
	           
	        }
	        else {

	            end = false;
	            btnPauseContinue.setText("Pausar");
	            flujo = new Thread(this);
	            model.getT().start();
	            flujo.start();
	            btnPhase1.setEnabled(true);
	            
	        }
	}
	
	void btnPhase1Event() {

		if (flujo != null && flujo.isAlive()) {

			end = true;
			while(flujo.isAlive()) {}

			sliderFriction.setEnabled(false);
			sliderBugInitMass.setEnabled(false);
			sliderBugVelocity.setEnabled(false);
			sliderDiskVelocity.setEnabled(false);
			sliderFallRadius.setEnabled(false);

			chart.deleteImage(targetAnnotation);

			btnPhase1.setEnabled(false);

			end = false;
//			repaint();

			//Time is reset.
			model.nextPhase();

			flujo = new Thread(this);
			flujo.start();
		}
	}
	

	    
	@Override
	public void run() {

		PolarPoint2D bug;
		end = false;


		while(!end){

	
			this.rotatedDiskImage = ImageProcessing.rotateRadians(this.diskImage, model.getDiskPhi());
			chart.deleteImage(diskAnnotation);
			
			diskAnnotation = chart.setImageAtPoint(this.rotatedDiskImage, 0, 0); 
					
			switch(model.getPhase()){
				case AngularMDiskModel.PHASE_0:
					chart.deleteImage(targetAnnotation);
					targetAnnotation = chart.setImageAtPoint(targetImage, sliderFallRadius.getValue(), 0);
					break;
				case AngularMDiskModel.PHASE_1:
					bug = new PolarPoint2D(model.get_r()*100, model.getBugPhi());
					this.rotatedBugImage = ImageProcessing.rotateRadians(this.bugImage, model.getBugPhi() + this.bugOrientation);
					chart.deleteImage(bugAnnotation);
					bugAnnotation = chart.setImageAtPoint(this.rotatedBugImage, bug.toCartesianPoint()); 
					break;
				case AngularMDiskModel.PHASE_2:
					bug = new PolarPoint2D(model.get_r()*100, model.getBugPhi());
					this.rotatedBugImage = ImageProcessing.rotateRadians(this.bugImage, model.getBugPhi() + this.bugOrientation);
					chart.deleteImage(bugAnnotation);
					bugAnnotation = chart.setImageAtPoint(this.rotatedBugImage, bug.toCartesianPoint());					
					break;
				case AngularMDiskModel.PHASE_3:
					break;
				case AngularMDiskModel.PHASE_4:
					chart.deleteImage(bugAnnotation);
					end = true;
					
					sliderFriction.setEnabled(true);
					sliderBugInitMass.setEnabled(true);
					sliderBugVelocity.setEnabled(true);
					sliderDiskVelocity.setEnabled(true);
					sliderFallRadius.setEnabled(true);
					
					btnLaunchSimulation.setText("Iniciar");
					
					break;
			}
			
			panelSimulation.actualizaGrafica(chart);
			
			repaint();

			//Begin step of simulation
			model.getT().pause();
			model.simulate();
			
			String WDisk = String.valueOf((model.getWDisk()));
			if(WDisk.length() > 6){
				lblDiskWValue.setText(WDisk.substring(0,8));
			}
			else{
				lblDiskWValue.setText(WDisk);
			}
			model.getT().start();
			//End Step of simulation
	

			try {
				Thread.sleep((long)sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(AngularMDiskApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	/**
	 * Read the input values from the interface and loads its 
	 * in the variable of the class 
	 */
	private void readInputs(){
		this.m = this.sliderBugInitMass.getValue();
		this.r0 = this.sliderFallRadius.getValue()/100.0;
		this.W = this.sliderDiskVelocity.getValue()/10.0;
		this.v = this.sliderBugVelocity.getValue()/1000.0;
		this.mu = this.sliderFriction.getValue()/100.0;
	}
	
	//Init the elements of the simulation
	private void initSimulation(){
		
		Point2D [] nullArray = new Point2D[0];
		
		//Crear modelo
		model = new AngularMDiskModel(m, r0, v, W, mu);
		
		chart = new Grafica(nullArray,"Conservaci�n del Momento Angular", "Bug", "Coordenada X", "Coordenada Y", false, Color.BLUE,1f,false);
		chart.setRangeAxis(this.infXLimit, this.supXLimit, this.infYLimit, this.supYLimit);
		
		
		backgroundImage = this.loadImage("wood.jpg");
		try {
			chart.setBackGroundImage(backgroundImage, 0.9f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Load Images
		
		this.diskImage = loadImage("vinilo4.png");
		this.bugImage = loadImage("ladyBug1.png");
		this.targetImage = loadImage("target642.png");
		
		//Set Images 
		diskAnnotation = chart.setImageAtPoint(this.diskImage, 0, 0); 
		targetAnnotation = chart.setImageAtPoint(targetImage, sliderFallRadius.getValue(),0);
		
		
		//Actualize panels
		panelSimulation.actualizaGrafica(chart);
	}
	

	public BufferedImage loadImage(String fileName){

		BufferedImage buff = null;
		try {
			buff = ImageIO.read(getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return buff;
	}
	
	
	int obtainExponent(double number){
		int exponent = 0;
		
		while(0 == (int)number){
			exponent--;
			number *= 10;
		}
		return exponent;
		
	}
	
	private void autogeneratedCode(){
		JPanel panel_control = new JPanel();
		panel_control.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		
		JPanel panelInputs = new JPanel();
		panelInputs.setToolTipText("");
		panelInputs.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JPanel panelTiempo = new JPanel();
		panelTiempo.setToolTipText("");
		panelTiempo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		btnPhase1 = new JButton("Lanzar Bicho");
		btnPhase1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnPhase1Event();
			}
		});
		btnPhase1.setEnabled(false);
		
		JPanel panelButtons = new JPanel();
		panelButtons.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel panelOutputs = new JPanel();
		panelOutputs.setToolTipText("");
		panelOutputs.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JPanel panelTitleOutputs = new JPanel();
		panelTitleOutputs.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		JLabel labelOutputData = new JLabel("Datos de la Simulaci\u00F3n");
		labelOutputData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTitleOutputs.add(labelOutputData);
		
		lblDiskW = new JLabel("Velocidad Disco:");
		lblDiskW.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblDiskWValue = new JLabel();
		lblDiskWValue.setText("0");
		lblDiskWValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDiskW, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblDiskWValue, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
					.addGap(132))
		);
		gl_panelOutputs.setVerticalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDiskW)
						.addComponent(lblDiskWValue))
					.addGap(95))
		);
		panelOutputs.setLayout(gl_panelOutputs);
		GroupLayout gl_panel_control = new GroupLayout(panel_control);
		gl_panel_control.setHorizontalGroup(
			gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_control.createParallelGroup(Alignment.TRAILING)
						.addComponent(panelInputs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(panelTiempo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(panelButtons, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(panelOutputs, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 404, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_control.setVerticalGroup(
			gl_panel_control.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelInputs, GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panelOutputs, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panelTiempo, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		GroupLayout gl_panelTiempo = new GroupLayout(panelTiempo);
		gl_panelTiempo.setHorizontalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTiempo.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnPhase1, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(204, Short.MAX_VALUE))
		);
		gl_panelTiempo.setVerticalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelTiempo.createSequentialGroup()
					.addContainerGap(47, Short.MAX_VALUE)
					.addComponent(btnPhase1, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panelTiempo.setLayout(gl_panelTiempo);
		panelButtons.setLayout(null);
		
		btnLaunchSimulation = new JButton("Iniciar");
		btnLaunchSimulation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLaunchSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnLaunchSimulationEvent(event);
			}
		});
		btnLaunchSimulation.setBounds(10, 11, 191, 62);
		panelButtons.add(btnLaunchSimulation);
		
		btnPauseContinue = new JButton("Pausar");
		btnPauseContinue.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnPauseContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnPauseContinueEvent(event);
			}
		});
		btnPauseContinue.setBounds(211, 11, 190, 62);
		panelButtons.add(btnPauseContinue);
		
		JLabel LabelBugMass = new JLabel("Masa del Bicho");
		LabelBugMass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelFallRadio = new JLabel("Radio de Ca\u00EDda");
		labelFallRadio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelBugVelocity = new JLabel("Velocidad del Bicho");
		labelBugVelocity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelDiskVelocity = new JLabel("Velocidad del Disco");
		labelDiskVelocity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		lblFallRadiusValue = new JLabel("10");
		lblFallRadiusValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblBugVelocityValue = new JLabel("1");
		lblBugVelocityValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblVelocityValue = new JLabel("0.5");
		lblVelocityValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		lblInitMassValue = new JLabel("30");
		lblInitMassValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		sliderBugInitMass = new JSlider();
		sliderBugInitMass.setValue(30);
		sliderBugInitMass.setMinimum(20);
		sliderBugInitMass.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderBugInitMassEvent();
			}
		});
		sliderBugInitMass.setMaximum(70);
		
		sliderFallRadius = new JSlider();
		sliderFallRadius.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderFallRadiusEvent();
			}
		});
		sliderFallRadius.setValue(10);
		sliderFallRadius.setMinorTickSpacing(1);
		sliderFallRadius.setMaximum(20);
		
		sliderBugVelocity = new JSlider();
		sliderBugVelocity.setValue(10);
		sliderBugVelocity.setMaximum(20);
		sliderBugVelocity.setMinimum(5);
		sliderBugVelocity.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderBugVelocityEvent();
			}
		});
		sliderBugVelocity.setMinorTickSpacing(1);
		
		sliderDiskVelocity = new JSlider();
		sliderDiskVelocity.setMaximum(10);
		sliderDiskVelocity.setMinimum(1);
		sliderDiskVelocity.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderDiskVelocityEvent();
			}
		});
		sliderDiskVelocity.setValue(5);
		sliderDiskVelocity.setMinorTickSpacing(1);
		
		JLabel lblCoeficienteDeRozamiento = new JLabel("Coef de Rozamiento");
		lblCoeficienteDeRozamiento.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblFrictionValue = new JLabel("0.25");
		lblFrictionValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		sliderFriction = new JSlider();
		sliderFriction.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sliderFrictionEvent();
			}
		});
		sliderFriction.setValue(25);
		sliderFriction.setMinorTickSpacing(1);
		sliderFriction.setMinimum(1);
		sliderFriction.setMaximum(90);
	
		
		
		
		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
			gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addComponent(panelTitle, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(LabelBugMass, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelFallRadio, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblInitMassValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
							.addComponent(sliderBugInitMass, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblFallRadiusValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sliderFallRadius, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(labelBugVelocity, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblBugVelocityValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(sliderBugVelocity, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addContainerGap()
					.addComponent(labelDiskVelocity, GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createSequentialGroup()
						.addComponent(lblVelocityValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(sliderDiskVelocity, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
				.addGroup(Alignment.LEADING, gl_panelInputs.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCoeficienteDeRozamiento, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
					.addGap(6)
					.addComponent(lblFrictionValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(sliderFriction, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panelInputs.setVerticalGroup(
			gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panelInputs.createSequentialGroup()
					.addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGap(29)
							.addComponent(lblFallRadiusValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(sliderBugInitMass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sliderFallRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblInitMassValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(LabelBugMass))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(labelFallRadio)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(sliderBugVelocity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblBugVelocityValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addComponent(labelBugVelocity)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGap(1)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblVelocityValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(labelDiskVelocity)))
						.addComponent(sliderDiskVelocity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGap(1)
							.addComponent(lblCoeficienteDeRozamiento, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGap(1)
							.addComponent(lblFrictionValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(sliderFriction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		
		JLabel lblDatosDeEntrada = new JLabel("Datos de Entrada");
		lblDatosDeEntrada.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTitle.add(lblDatosDeEntrada);
		panelInputs.setLayout(gl_panelInputs);
		panel_control.setLayout(gl_panel_control);
		
		JPanel panel_visualizar = new JPanel();
		
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_control, GroupLayout.PREFERRED_SIZE, 432, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_visualizar, GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_visualizar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
						.addComponent(panel_control, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 598, Short.MAX_VALUE))
					.addContainerGap())
		);
		GridBagLayout gbl_panel_visualizar = new GridBagLayout();
		gbl_panel_visualizar.columnWidths = new int[]{0, 0};
		gbl_panel_visualizar.rowHeights = new int[]{0, 0, 0};
		gbl_panel_visualizar.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_visualizar.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_visualizar.setLayout(gbl_panel_visualizar);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridheight = 2;
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		panel_visualizar.add(tabbedPane, gbc_tabbedPane);
		
		panelSimulation = new JPanelGrafica();
		tabbedPane.addTab("C�mara Fija", null, panelSimulation, null);
		panelSimulation.setBackground(Color.WHITE);
		
		getContentPane().setLayout(groupLayout);
	}
}
