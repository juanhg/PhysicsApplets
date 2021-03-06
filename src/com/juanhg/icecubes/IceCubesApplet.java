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

package com.juanhg.icecubes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
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
import org.jfree.chart.annotations.XYBoxAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;

import com.raccoon.easyjchart.Grafica;
import com.raccoon.easyjchart.JPanelGrafica;

public class IceCubesApplet extends JApplet implements Runnable {


	private static final long serialVersionUID = -3017107307819023599L;
	private final String water = "water.png";
	private final String milk = "milk.png";
	private final String orange = "orange.png";
	private final String lemonade = "lemonade.png";


	boolean reset = false;

	static final int WATER = 0;
	static final int MILK = 1;
	static final int ORANGE = 2;
	static final int LEMONADE = 3;

	private final double archeight = 0.5;
	private final double archwidth = 0.5;

	int precision = 6;

	public Color fluidColor = new Color(100,180,255,70);
	final Color cubeColor = new Color(240,240,255,230);
	double fluidTotalIncrement = 90;
	double fluidMidIncrement = 50;
	int increment = 0;


	//Control variables
	long sleepTime = 100;	
	boolean end = false;

	//Inputs
	double vol, T, t;
	int N, type;
	double zoom = 1;

	//Thread that executed the simulation
	private Thread flujo = null;

	//Model
	private IceCubesModel model;

	//Charts
	private Grafica chartTQ, chartGlass;

	//Panels
	private JPanelGrafica panel_7, panelChart, panelGlass;

	int supXLimit = 2000;
	int infXLimit = -1;
	int supYLimit = 35;
	int infYLimit = -35;

	double gYBase = 0;
	double gYTop = 7.5;
	double gXLeft = 2;
	double gXRight = 15;

	double cX = 2.3;
	double cXSeparation = 2.5;

	double cW = 6;
	double cH = 2.5;

	//Images
	BufferedImage waterImage, milkImage, orangeImage, lemonadeImage;

	//Annotations
	XYAnnotation exampleAnnotation = null;
	XYAnnotation gBaseAnnotation = null;
	XYAnnotation gTopAnnotation = null;
	XYAnnotation gLeftAnnotation = null;
	XYAnnotation gRightAnnotation = null;
	XYAnnotation gSurfaceAnnotation = null;
	XYAnnotation cube1Annotation = null;
	XYAnnotation cube2Annotation = null;
	XYAnnotation cube3Annotation = null;
	XYAnnotation cube4Annotation = null;

	//Labels
	private JLabel lblO1Value, lblVValue;  
	private JLabel lblTValue, lbltValue, lblVolValue, lblO2Value, lblNValue;
	private JLabel lblO1;

	//Sliders
	private JSlider sliderVol, sliderT, slidert, sliderN; 

	//Buttons
	JButton btnLaunchSimulation, btnWater, btnMilk, btnOrange, btnlemonade;



	public IceCubesApplet() {}

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

		setSize(1090,510);

		this.autogeneratedCode();

		//Obtain values from interface
		this.readInputs();
		this.initSimulation();
	}



	private void sliderI1Event(){
		if(sliderVol.getValueIsAdjusting()){
			lblVolValue.setText(Integer.toString(sliderVol.getValue()));
			updateGlass(IceCubesModel.getV(sliderVol.getValue(), sliderN.getValue()));
			repaint();
		}
	}

	private void sliderI2Event(){
		//270-330
		if(sliderT.getValueIsAdjusting()){
			double staticF;
			staticF = (double) sliderT.getValue();
			lblTValue.setText("" + staticF); 
		}
	}

	private void sliderI3Event(){
		//1-5
		double dynamicF;
		if(slidert.getValueIsAdjusting()){
			dynamicF = -(double) slidert.getValue();
			lbltValue.setText("" + dynamicF);
		}
	}

	private void sliderI4Event(){
		//6-10
		if(sliderN.getValueIsAdjusting()){
			N = sliderN.getValue();
			lblNValue.setText("" + N);

			updateGlass(IceCubesModel.getV(sliderVol.getValue(), sliderN.getValue()));
			repaint();
		}
	}


	void btnLaunchSimulationEvent(ActionEvent event){

		boolean buttonsOn = false;

		if((flujo != null && flujo.isAlive()) || (flujo != null && reset == true)) {
			end = true;
			increment = 0;
			
			while(flujo.isAlive()) {}
			reset = false;
			buttonsOn = true;
			
			model.getTime().stop();

			this.readInputs();
			this.initSimulation();

			btnLaunchSimulation.setText("Iniciar");

			sliderVol.setEnabled(buttonsOn);
			slidert.setEnabled(buttonsOn);
			sliderN.setEnabled(buttonsOn);
			sliderT.setEnabled(buttonsOn);

			repaint();

		}
		else{
						
			buttonsOn = false;
			btnLaunchSimulation.setText("Finalizar");

			//Obtain values from interface
			this.readInputs();
			this.initSimulation();
			
			//Crear modelo
			model = new IceCubesModel(vol, T, t, 0, N);

			//Initializes and runs the thread (Run())
			flujo = new Thread(this);

			sliderVol.setEnabled(buttonsOn);
			slidert.setEnabled(buttonsOn);
			sliderN.setEnabled(buttonsOn);
			sliderT.setEnabled(buttonsOn);

			model.getTime().start();
			flujo.start();
		}
	}

	@Override
	public void run() {

		end = false;
		Point2D [] arrayChartTQ = null;
		Point2D [] arrayCharttQ = null;


		while(!end){

			model.getTime().pause();
			//Begin step of simulation
			model.simulate();
			//End Step of simulation
			model.getTime().start();

			arrayChartTQ = model.getChartTQ();
			arrayCharttQ = model.getCharttQ();

			chartTQ.replacePlot(0, arrayChartTQ, "T", Color.RED, 2f, true);
			chartTQ.replacePlot(1, arrayCharttQ, "t", Color.BLUE, 2f, true);

			zoom = 1;
			double zoom1 = 1;
			double zoom2 = 2;

			if(arrayChartTQ.length >= 1){
			}
			if(arrayCharttQ.length >= 1){
				zoom2 = getZoom(arrayCharttQ[arrayCharttQ.length-1], supXLimit, infXLimit, supYLimit, infYLimit);
			}

			if(zoom1 > zoom2){ zoom = zoom1;}
			else{ zoom = zoom2;}


			chartTQ.setRangeAxis(infXLimit, supXLimit*zoom, infYLimit, supYLimit);

			lblO1Value.setText(dToString(model.getT(), precision));
			lblO2Value.setText(dToString(model.gett(), precision));
			lblVValue.setText(dToString(model.getV(), precision+4));

			if(model.finalPhaseReached()){
				reset = true;
				end = true;
			}
			else{
				fluidColor = getFluidColor();
				System.out.println(fluidColor.getRed() + " " + fluidColor.getGreen() + " " + fluidColor.getGreen());
			}
			updateGlass(model.getV());
			this.updatePanels();
			
			repaint();

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(IceCubesApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	/**
	 * Read the input values from the interface and loads its 
	 * in the variable of the class 
	 */
	private void readInputs(){
		vol = sliderVol.getValue();
		T = sliderT.getValue();
		t = -slidert.getValue();
		N = sliderN.getValue();
	}

	//Init the elements of the simulation
	private void initSimulation(){

		Point2D [] nullArray = new Point2D[0];

		// Inicializar charts
		chartTQ = new Grafica(nullArray,"", "T", "", "", true, Color.RED,1f,false);
		chartTQ.agregarGrafica(nullArray, "t", Color.BLUE,1f,false);

		chartTQ.setRangeAxis(infXLimit, supXLimit, infYLimit, supYLimit);

		chartGlass = new Grafica(nullArray,"", "", "", "", false, Color.BLUE,1f,false);
		chartGlass.setRangeAxis(0, 18, -1, 8.5);
		//		chartGlass.setAxisVisible(false);

		//Load Images

		drawGlass();

		//Actualize panels
		updatePanels();
		repaint();
	}

	/**
	 * Load a image in the specified path
	 * @param fileName Absolute or relative path to the image
	 * @return BufferedImage that contains the image
	 */
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

	private void drawGlass(){
		Stroke border = new BasicStroke(3f);

		gBaseAnnotation = new XYShapeAnnotation(new Ellipse2D.Double(gXLeft,gYBase-0.5,gXRight-gXLeft,1), border, Color.BLACK, fluidColor);
		chartGlass.setAnnotation(gBaseAnnotation);

		gTopAnnotation = new XYShapeAnnotation(new Ellipse2D.Double(gXLeft,gYTop-0.5,gXRight-gXLeft,1), border, Color.BLACK);
		chartGlass.setAnnotation(gTopAnnotation);

		gRightAnnotation = new XYLineAnnotation(gXRight, gYBase, gXRight, gYTop, border, Color.black);
		chartGlass.setAnnotation(gRightAnnotation);

		gLeftAnnotation = new XYLineAnnotation(gXLeft, gYBase, gXLeft, gYTop, border, Color.black);
		chartGlass.setAnnotation(gLeftAnnotation);

		gTopAnnotation = new XYBoxAnnotation(gXLeft, gYBase, gXRight, gYTop/2, null, null, fluidColor);
		chartGlass.setAnnotation(gTopAnnotation);

		updateGlass(IceCubesModel.getV(sliderVol.getValue(), sliderN.getValue()));


	}

	private void updateGlass(double vol){
		double surface = vol;
		Stroke border = new BasicStroke(3f);

		chartGlass.deleteAnnotation(gBaseAnnotation);
		gBaseAnnotation = new XYShapeAnnotation(new Ellipse2D.Double(gXLeft,gYBase-0.5,gXRight-gXLeft,1), border, Color.BLACK, fluidColor);
		chartGlass.setAnnotation(gBaseAnnotation);

		chartGlass.deleteAnnotation(gTopAnnotation);
		gTopAnnotation = new XYBoxAnnotation(gXLeft, gYBase, gXRight, surface, null, null, fluidColor);
		chartGlass.setAnnotation(gTopAnnotation);

		chartGlass.deleteAnnotation(gSurfaceAnnotation);
		gSurfaceAnnotation = new XYShapeAnnotation(new Ellipse2D.Double(gXLeft,(surface)-0.5,gXRight-gXLeft,1), border, Color.BLACK, fluidColor);
		chartGlass.setAnnotation(gSurfaceAnnotation);

		chartGlass.deleteAnnotation(cube1Annotation);
		chartGlass.deleteAnnotation(cube2Annotation);
		chartGlass.deleteAnnotation(cube3Annotation);
		chartGlass.deleteAnnotation(cube4Annotation);

		if(sliderVol.getValue() >= 10 || N <= 2){
			if(N >= 1){

				cube1Annotation = new XYShapeAnnotation(new RoundRectangle2D.Double(cX, surface-2, getCubeW(), getCubeH(), archeight,archwidth), border, Color.BLACK, cubeColor);
				chartGlass.setAnnotation(cube1Annotation);

				if(N>= 2){

					cube2Annotation = new XYShapeAnnotation(new RoundRectangle2D.Double(cX + getCubeW()+0.4, surface-2,  getCubeW(), getCubeH(), archeight,archwidth), border, Color.BLACK, cubeColor);
					chartGlass.setAnnotation(cube2Annotation);

					if(N>= 3){

						cube3Annotation = new XYShapeAnnotation(new RoundRectangle2D.Double(cX, surface - 2.2 - getCubeH(),  getCubeW(), getCubeH(), archeight, archwidth), border, Color.BLACK, cubeColor);
						chartGlass.setAnnotation(cube3Annotation);

						if(N>=4){

							cube4Annotation = new XYShapeAnnotation(new RoundRectangle2D.Double(cX + getCubeW()+0.4, surface - 2.2 - getCubeH(),  getCubeW(), getCubeH(), archeight,archwidth), border, Color.BLACK, cubeColor);
							chartGlass.setAnnotation(cube4Annotation);
						}
					}
				}
			}
		}
		else{
			if(N >= 1){

				cube1Annotation = new XYShapeAnnotation(new RoundRectangle2D.Double(cX, gYBase, getCubeW(), getCubeH(), archeight,archwidth), border, Color.BLACK, cubeColor);
				chartGlass.setAnnotation(cube1Annotation);

				if(N>= 2){

					cube2Annotation = new XYShapeAnnotation(new RoundRectangle2D.Double(cX + getCubeW()+0.4, gYBase,  getCubeW(), getCubeH(), archeight,archwidth), border, Color.BLACK, cubeColor);
					chartGlass.setAnnotation(cube2Annotation);

					if(N>= 3){

						cube3Annotation = new XYShapeAnnotation(new RoundRectangle2D.Double(cX, gYBase + getCubeH(),  getCubeW(), getCubeH(), archeight, archwidth), border, Color.BLACK, cubeColor);
						chartGlass.setAnnotation(cube3Annotation);

						if(N>=4){

							cube4Annotation = new XYShapeAnnotation(new RoundRectangle2D.Double(cX + getCubeW()+0.4, gYBase + getCubeH(),  getCubeW(), getCubeH(), archeight,archwidth), border, Color.BLACK, cubeColor);
							chartGlass.setAnnotation(cube4Annotation);
						}
					}
				}
			}
		}
	}


	private void updatePanels(){
		panelChart.actualizaGrafica(chartTQ);
		panelGlass.actualizaGrafica(chartGlass);
	}


	public double getCubeW(){
		if(model != null){
			return model.getL()*cW/model.getLo();
		}
		return cW;
	}

	public double getCubeH(){
		if(model != null){
			return model.getL()*cH/model.getLo();
		}
		return cH;
	}

	/**
	 * Calculate the multiplicative zoom that must be applied to the minimum range of the
	 * plot, to achieve that the point will be drawn inside the plot.
	 * @param point Point that must be drawn inside the chart
	 * @param supXLimit Superior X Limit of the plot
	 * @param infXLimit Inferior X Limit of the plot
	 * @param supYLimit Superior Y Limit of the plot
	 * @param infYLimit Inferior Y Limit of the plot
	 */
	public double getZoom(Point2D point, int supXLimit, int infXLimit, int supYLimit, int infYLimit){
		int tempZoom = 1;

		while(point.getX() >= supXLimit*tempZoom
				|| point.getY() >= supYLimit*tempZoom
				|| point.getX() <= infXLimit*tempZoom
				|| point.getY() <= infYLimit*tempZoom){
			tempZoom = tempZoom * 2;
		}

		return tempZoom;
	}

	private Color getFluidColor(){

		Color color = null;
		int currentCase = model.getCurrentCase();
		int currentPhase = model.getCurrentPhase();
		double finalIncrement = 0;

		if(currentPhase == 2){
			switch(currentCase){
			case 2:
				finalIncrement = fluidTotalIncrement;
				break;
			case 4:
				finalIncrement = fluidMidIncrement;
				break;
			}
			double time = model.getSecond();
			increment = (int) (time*finalIncrement/(model.getDuration()/1000.0));
			System.out.println(model.getDuration());
		}

		int R, G, B;
		switch(type){
		case WATER:
			R = 100 + increment/2;
			G = 180 + increment/2;
			if(R > 255){ R = 255;}
			if(G > 255){ G = 255;}
			color = new Color(R,G,255,70);
			break;
		case MILK:
			color = new Color(255,255,255,255);
			break;
		case ORANGE:
			G = 150 + increment/2;
			B = 0 + increment/2;
			if(G > 255){ G = 255;}
			if(B > 255){ B = 255;}
			color = new Color(225,G,B,220);
			break;
		case LEMONADE:
			G = 225 + increment/2;
			B = 0 + increment/2;
			if(G > 255){ G = 255;}
			if(B > 255){ B = 255;}
			color = fluidColor = new Color(225,G,B,150);
			break;
		}

		return color;
	}

	public String dToString(double value, int precision){
		String x = String.valueOf(value);
		if(x.length() > precision){
			return (x.substring(0,precision));
		}
		else{
			return x;
		}
	}

	private void autogeneratedCode(){
		JPanel panel_control = new JPanel();
		panel_control.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));

		JPanel panelInputs = new JPanel();
		panelInputs.setToolTipText("");
		panelInputs.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

		JPanel panelOutputs = new JPanel();
		panelOutputs.setToolTipText("");
		panelOutputs.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

		JPanel panelTitleOutputs = new JPanel();
		panelTitleOutputs.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		JLabel labelOutputData = new JLabel("Datos de la Simulaci\u00F3n");
		labelOutputData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTitleOutputs.add(labelOutputData);

		lblO1 = new JLabel("T:");
		lblO1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblO1Value = new JLabel();
		lblO1Value.setText("0");
		lblO1Value.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblO2 = new JLabel("t:");
		lblO2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblO2Value = new JLabel();
		lblO2Value.setText("0");
		lblO2Value.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblV = new JLabel("V:");
		lblV.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblVValue = new JLabel();
		lblVValue.setText("0");
		lblVValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitleOutputs, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addGap(22)
							.addComponent(lblO1, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblO2, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
							.addGap(37)))
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblO1Value, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
							.addGap(3))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblO2Value, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblV, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblVValue, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panelOutputs.setVerticalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblO1)
						.addComponent(lblO1Value)
						.addComponent(lblV, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblVValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblO2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblO2Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGap(40))
		);
		panelOutputs.setLayout(gl_panelOutputs);

		JPanel panelLicense = new JPanel();
		panelLicense.setBorder(new LineBorder(new Color(0, 0, 0)));

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_panel_control = new GroupLayout(panel_control);
		gl_panel_control.setHorizontalGroup(
				gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_control.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_control.createParallelGroup(Alignment.LEADING)
								.addComponent(panelOutputs, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panelInputs, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
								.addComponent(panel_6, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
								.addComponent(panelLicense, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
								.addContainerGap())
				);
		gl_panel_control.setVerticalGroup(
				gl_panel_control.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_control.createSequentialGroup()
						.addContainerGap()
						.addComponent(panelInputs, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelOutputs, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelLicense, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);

		btnLaunchSimulation = new JButton("Iniciar");
		btnLaunchSimulation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLaunchSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnLaunchSimulationEvent(event);
			}
		});
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
				gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnLaunchSimulation, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
						.addContainerGap())
				);
		gl_panel_6.setVerticalGroup(
				gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnLaunchSimulation, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(78, Short.MAX_VALUE))
				);
		panel_6.setLayout(gl_panel_6);

		JLabel lblNewLabel = new JLabel("GNU GENERAL PUBLIC LICENSE");
		panelLicense.add(lblNewLabel);

		JLabel LabelI1 = new JLabel("Volumen");
		LabelI1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelI2 = new JLabel("T");
		labelI2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelI3 = new JLabel("t");
		labelI3.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		lblTValue = new JLabel("15");
		lblTValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lbltValue = new JLabel("-15");
		lbltValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		lblVolValue = new JLabel("20");
		lblVolValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		sliderVol = new JSlider();
		sliderVol.setMinimum(1);
		sliderVol.setMaximum(30);
		sliderVol.setMinorTickSpacing(1);
		sliderVol.setValue(20);
		sliderVol.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderI1Event();
			}
		});


		sliderT = new JSlider();
		sliderT.setMinimum(5);
		sliderT.setMaximum(30);
		sliderT.setMinorTickSpacing(1);
		sliderT.setValue(15);
		sliderT.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI2Event();
			}
		});




		slidert = new JSlider();
		slidert.setMinimum(5);
		slidert.setMaximum(30);
		slidert.setValue(15);
		slidert.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI3Event();
			}
		});

		JLabel lblI4 = new JLabel("N\u00BA de Cubitos");
		lblI4.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblNValue = new JLabel("1");
		lblNValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		sliderN = new JSlider();
		sliderN.setMinimum(1);
		sliderN.setMaximum(4);
		sliderN.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sliderI4Event();
			}
		});
		sliderN.setValue(1);
		sliderN.setMinorTickSpacing(1);

		waterImage = loadImage(water);
		btnWater = new JButton(new ImageIcon(waterImage));
		btnWater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnWater.setEnabled(false);
				btnMilk.setEnabled(true);
				btnOrange.setEnabled(true);
				btnlemonade.setEnabled(true);

				type = WATER;

				fluidColor = new Color(100,180,255,70);
				updateGlass(IceCubesModel.getV(sliderVol.getValue(), sliderN.getValue()));
				repaint();
			}
		});

		milkImage = loadImage(milk);
		btnMilk = new JButton(new ImageIcon(milkImage));
		btnMilk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnWater.setEnabled(true);
				btnMilk.setEnabled(false);
				btnOrange.setEnabled(true);
				btnlemonade.setEnabled(true);

				type = MILK;

				fluidColor = new Color(255,255,255,255);
				updateGlass(IceCubesModel.getV(sliderVol.getValue(), sliderN.getValue()));
				repaint();
			}
		});

		orangeImage = loadImage(orange);
		btnOrange = new JButton(new ImageIcon(orangeImage));
		btnOrange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnWater.setEnabled(true);
				btnMilk.setEnabled(true);
				btnOrange.setEnabled(false);
				btnlemonade.setEnabled(true);

				type = ORANGE;

				fluidColor = new Color(225,150,0,220);
				updateGlass(IceCubesModel.getV(sliderVol.getValue(), sliderN.getValue()));
				repaint();
			}
		});

		lemonadeImage = loadImage(lemonade);
		btnlemonade = new JButton(new ImageIcon(lemonadeImage));
		btnlemonade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnWater.setEnabled(true);
				btnMilk.setEnabled(true);
				btnOrange.setEnabled(true);
				btnlemonade.setEnabled(false);

				type = LEMONADE;

				fluidColor = new Color(225,225,0,150);
				updateGlass(IceCubesModel.getV(sliderVol.getValue(), sliderN.getValue()));
				repaint();
			}
		});

		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
				gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addComponent(panelTitle, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
				.addGroup(gl_panelInputs.createSequentialGroup()
						.addContainerGap(23, Short.MAX_VALUE)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panelInputs.createSequentialGroup()
										.addComponent(btnWater, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnMilk, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnOrange, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnlemonade, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
										.addGap(8))
										.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panelInputs.createSequentialGroup()
														.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING, false)
																.addComponent(labelI3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(LabelI1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(labelI2, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
																.addGap(18)
																.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																		.addComponent(lblVolValue, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lblTValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lbltValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
																		.addGap(18)
																		.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING, false)
																				.addComponent(sliderVol, 0, 0, Short.MAX_VALUE)
																				.addComponent(sliderT, 0, 0, Short.MAX_VALUE)
																				.addComponent(slidert, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
																				.addGroup(gl_panelInputs.createSequentialGroup()
																						.addComponent(lblI4, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
																						.addGap(18)
																						.addComponent(lblNValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
																						.addGap(18)
																						.addComponent(sliderN, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))))
																						.addGap(15))
				);
		gl_panelInputs.setVerticalGroup(
				gl_panelInputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelInputs.createSequentialGroup()
						.addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
										.addComponent(LabelI1)
										.addComponent(lblVolValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
										.addComponent(sliderVol, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
														.addComponent(labelI2)
														.addComponent(lblTValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
														.addComponent(sliderT, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
														.addGap(11)
														.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																.addComponent(labelI3)
																.addComponent(lbltValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																.addComponent(slidert, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																		.addComponent(lblI4, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lblNValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																		.addComponent(sliderN, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																		.addGap(7)
																		.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING, false)
																				.addComponent(btnWater, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																				.addComponent(btnMilk, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
																				.addComponent(btnOrange, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
																				.addComponent(btnlemonade, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
																				.addGap(11))
				);

		JLabel lblDatosDeEntrada = new JLabel("Datos de Entrada");
		lblDatosDeEntrada.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTitle.add(lblDatosDeEntrada);
		panelInputs.setLayout(gl_panelInputs);
		panel_control.setLayout(gl_panel_control);

		JPanel panel_visualizar = new JPanel();
		panel_visualizar.setBackground(Color.WHITE);


		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_control, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_visualizar, GroupLayout.PREFERRED_SIZE, 694, GroupLayout.PREFERRED_SIZE)
						.addGap(156))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
						.addGap(12)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(panel_visualizar, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
								.addComponent(panel_control, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 494, Short.MAX_VALUE))
								.addContainerGap(84, Short.MAX_VALUE))
				);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 368, 494);
		panel.setBackground(Color.WHITE);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(1, 1, 366, 31);
		panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		JLabel lblSimulacin = new JLabel("Simulaci\u00F3n");
		lblSimulacin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_1.add(lblSimulacin);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setBackground(Color.WHITE);
		panel_3.setBounds(368, 0, 326, 494);
		panel_visualizar.setLayout(null);
		panel_visualizar.add(panel);
		panel.setLayout(null);
		panel.add(panel_1);

		panel_7 = new JPanelGrafica();
		panel_7.setBackground(Color.WHITE);
		panel_7.setBounds(1, 31, 366, 463);
		panel.add(panel_7);

		panelGlass = new JPanelGrafica();
		panelGlass.setBackground(Color.WHITE);
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
				gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addComponent(panelGlass, GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
				);
		gl_panel_7.setVerticalGroup(
				gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addComponent(panelGlass, GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
				);
		panel_7.setLayout(gl_panel_7);
		panel_visualizar.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(1, 1, 324, 31);
		panel_4.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		JLabel lblGrficaDeEvolucin = new JLabel("Gr\u00E1fica de Evoluci\u00F3n (T frente a Q)");
		lblGrficaDeEvolucin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_4.add(lblGrficaDeEvolucin);
		panel_3.add(panel_4);

		JPanel panel7 = new JPanel();
		panel7.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel7.setBackground(Color.WHITE);
		panel7.setBounds(1, 31, 324, 463);
		panel_3.add(panel7);
		panel7.setLayout(null);

		panelChart = new JPanelGrafica();
		panelChart.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelChart.setBackground(Color.WHITE);
		panelChart.setBounds(0, 0, 324, 462);
		panel7.add(panelChart);

		getContentPane().setLayout(groupLayout);
	}
}