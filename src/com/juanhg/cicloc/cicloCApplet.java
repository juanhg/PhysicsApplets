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

package com.juanhg.cicloc;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
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
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;

import com.juanhg.util.Time;
import com.raccoon.easyjchart.Grafica;
import com.raccoon.easyjchart.JPanelGrafica;

public class cicloCApplet extends JApplet implements Runnable {


	private static final long serialVersionUID = -3017107307819023599L;
	private final String hot = "hot.png";
	private final String cold = "cold.png";
	private final String bulbOn = "bulbOn.png";
	private final String bulbOff = "bulbOff.png";
	private final String LEDOn = "LEDOn.png";
	private final String LEDOff = "LEDOff.png";
	private final String termo = "termo.png";
	private final String waterTexture = "waterTexture.jpg";

	final int pistonY0 = 3;
	final int pistonY1 = 7;
	
	private final int LED_TIME = 18000;
	private final int BULB_TIME = 3000;

	XYTextAnnotation tAnnotation;
	
	//Control variables
	long sleepTime = 15;	
	boolean end = false;
	
	Time tBulb, tLED;

	//Inputs
	double T1, T2, Vmin, Vmax, N;
	
	TexturePaint texture; 

	//Thread that executed the simulation
	private Thread flujo = null;

	//Model
	private cicloCModel model;
	
	//Charts
	private Grafica chartPV, chartUV, chartTV, chartUT, chartST, chartPT, chartPiston, chartTermo;
	
	//Panels
	private JPanel panel_1, panelMode;
	private JPanelGrafica panelPV, panelUV, panelTV, panelST, panelUT, panelPT, panelPiston, panelPistonInterno, panelTermo;

	int supXLimit = 8;
	int infXLimit = 2;
	int supYLimit = 11000000;
	int infYLimit = 0;

	Point2D VLimits, PLimits, ULimits, TLimits, SLimits;
	
	double modif = 0;
	boolean isHot = true;


	//Images
	BufferedImage hotImage, coldImage, bulbOnImage, bulbOffImage, LEDOnImage, LEDOffImage, waterTextureImage, termoImage;

	
	//Annotations
	XYAnnotation pistonImageAnnotation = null;
	XYAnnotation lineAnnotation1 = null; 
	XYAnnotation lineAnnotation2 = null;
	XYAnnotation lineAnnotation3 = null;
	XYAnnotation lineAnnotation4 = null;
	XYAnnotation lineAnnotation5 = null;

	//Labels
	private JLabel lblTrabajoCValue;  
	private JLabel lblT2Value, lblVminValue, lblT1Value, lblTrabajoValue, lblVmaxValue, lblNValue, lblModeValue;
	private JLabel lblPhase, lblEValue, lblE, lblBulb, lblLED ,lblLEDValue, lblBulbValue;

	//Sliders
	private JSlider sliderT1, sliderT2, sliderVMin, sliderVMax, sliderN; 

	//Buttons
	JButton btnLaunchSimulation, btnHot, btnCold;


	public cicloCApplet() {}

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

		setSize(1240,590);

		this.autogeneratedCode();

		//Obtain values from interface
		this.readInputs();
		this.initSimulation();	
	}



	private void sliderT1Event(){
		//350 - 450
		if(sliderT1.getValueIsAdjusting()){
			lblT1Value.setText(Integer.toString(sliderT1.getValue()));
		}
	}
	
	private void sliderT2Event(){
		//270-330
		if(sliderT2.getValueIsAdjusting()){
			double staticF;
			staticF = (double) sliderT2.getValue();
			lblT2Value.setText("" + staticF); 
		}
	}
	
	private void sliderVminEvent(){
		//1-5
		double dynamicF;
		if(sliderVMin.getValueIsAdjusting()){
			dynamicF = (double) sliderVMin.getValue();
			lblVminValue.setText("" + dynamicF);
		}
	}
	
	private void sliderVmaxEvent(){
		//6-10
		double dynamicF;
		if(sliderVMax.getValueIsAdjusting()){
			dynamicF = (double) sliderVMax.getValue();
			lblVmaxValue.setText("" + dynamicF);
		}
	}

	private void sliderNEvent(){
		//10-100
		double dynamicF;
		if(sliderN.getValueIsAdjusting()){
			dynamicF = (double) sliderN.getValue();
			lblNValue.setText("" + dynamicF);
		}
	}
	
	
	void btnLaunchSimulationEvent(ActionEvent event){

		boolean buttonsOn = false;

		if(flujo != null && flujo.isAlive()) {
			end = true;
			buttonsOn = true;

			while(flujo.isAlive()) {}

			model.getTime().stop();

			this.readInputs();
			this.initSimulation();

			btnLaunchSimulation.setText("Iniciar");

			sliderT1.setEnabled(buttonsOn);
			sliderVMin.setEnabled(buttonsOn);
			sliderVMax.setEnabled(buttonsOn);
			sliderN.setEnabled(buttonsOn);
			sliderT2.setEnabled(buttonsOn);
			
			if(isHot){
				btnHot.setEnabled(false);
				btnCold.setEnabled(true);
			}
			else{
				btnHot.setEnabled(true);
				btnCold.setEnabled(false);
			}

			lblBulb.setIcon(new ImageIcon(bulbOffImage));
			lblLED.setIcon(new ImageIcon(LEDOffImage));
			
			repaint();

		}
		else{

			buttonsOn = false;
			btnLaunchSimulation.setText("Finalizar");

			//Obtain values from interface
			this.readInputs();
			this.initSimulation();

			//Initializes and runs the thread (Run())
			flujo = new Thread();
			flujo = new Thread(this);

			sliderT1.setEnabled(buttonsOn);
			sliderVMin.setEnabled(buttonsOn);
			sliderVMax.setEnabled(buttonsOn);
			sliderN.setEnabled(buttonsOn);
			sliderT2.setEnabled(buttonsOn);
			btnHot.setEnabled(buttonsOn);
			btnCold.setEnabled(buttonsOn);
			model.simulate();
			
			lblBulb.setIcon(new ImageIcon(bulbOnImage));
			lblLED.setIcon(new ImageIcon(LEDOnImage));
			
			int precision = 8;
			String x = String.valueOf(model.getTl());

			if(x.length() > precision){
				lblLEDValue.setText("" + x.substring(0, precision) + " segundos");
			}
			else{
				lblLEDValue.setText(x + "segundos");
			}
			
			x = String.valueOf(model.getTb());

			if(x.length() > precision){
				lblBulbValue.setText("" + x.substring(0, precision) + " segundos");
			}
			else{
				lblBulbValue.setText(x + " segundos");
			}
			
			tBulb.start();
			tLED.start();
			
			model.getTime().start();
			flujo.start();
		}
	}
	
	@Override
	public void run() {

		double pSize = 3f;
		double lSize = 0.8f;
		end = false;


		while(!end){

			model.getTime().pause();
			
			//Begin step of simulation
			model.simulate();
			//End Step of simulation
			
			//Replace Plots
			if(!model.isFirstCompleted()){
				chartPV.replacePlot(1 ,model.getFPV(),"", Color.BLUE,1f,false);
				chartPV.visualizaMuestras(1,true,lSize);
				chartUV.replacePlot(1 ,model.getFUV(),"", Color.BLUE,1f,false);
				chartUV.visualizaMuestras(1,true,lSize);
				chartTV.replacePlot(1 ,model.getFTV(),"", Color.BLUE,1f,false);
				chartTV.visualizaMuestras(1,true,lSize);
				chartPT.replacePlot(1 ,model.getFPT(),"", Color.BLUE,1f,false);
				chartPT.visualizaMuestras(1,true,lSize);
				chartST.replacePlot(1 ,model.getFST(),"", Color.BLUE,1f,false);
				chartST.visualizaMuestras(1,true,lSize);
				chartUT.replacePlot(1 ,model.getFUT(),"", Color.BLUE,1f,false);
				chartUT.visualizaMuestras(1,true,lSize);
			}
			
			chartPV.replacePlot(0, model.getPPV(), "", Color.RED, 1f, true);
			chartPV.visualizaMuestras(0,true,pSize);
			chartUV.replacePlot(0, model.getPUV(), "", Color.RED, 1f, true);
			chartUV.visualizaMuestras(0,true,pSize);
			chartTV.replacePlot(0, model.getPTV(), "", Color.RED, 1f, true);
			chartTV.visualizaMuestras(0,true,pSize);
			chartPT.replacePlot(0, model.getPPT(), "", Color.RED, 1f, true);
			chartPT.visualizaMuestras(0,true,pSize);
			chartST.replacePlot(0, model.getPST(), "", Color.RED, 1f, true);
			chartST.visualizaMuestras(0,true,pSize);
			chartUT.replacePlot(0, model.getPUT(), "", Color.RED, 1f, true);
			chartUT.visualizaMuestras(0,true,pSize);
			
			updatePiston();
			updateTermo();
			
			int precision = 5;
			
			lblTrabajoCValue.setText("" + (int)model.getw());
			lblTrabajoValue.setText("" + (int)model.getW());
			
			String x = String.valueOf(model.getE());
			
			if(x.length() > precision){
				lblEValue.setText(x.substring(0,precision));
			}
			else{
				lblEValue.setText(x);
			}
			
			model.getTime().start();
			
			tBulb.pause();
			if(tBulb.getTime() > BULB_TIME){
				lblBulb.setIcon(new ImageIcon (bulbOffImage));
			}
			tBulb.start();
			
			tLED.pause();
			if(tLED.getTime() > LED_TIME){
				lblLED.setIcon(new ImageIcon (LEDOffImage));
			}
			tLED.start();
			
			this.updatePanels();
			repaint();
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(cicloCApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	/**
	 * Read the input values from the interface and loads its 
	 * in the variable of the class 
	 */
	private void readInputs(){
		T1 = sliderT1.getValue();
		T2 = sliderT2.getValue();
		N = sliderN.getValue();
		Vmin = sliderVMin.getValue()/(N*1000);
		Vmax = sliderVMax.getValue()/(N*1000);
	}

	//Init the elements of the simulation
	private void initSimulation(){

		Point2D [] nullArray = new Point2D[0];

		//Crear modelo
		model = new cicloCModel(T1, T2, Vmin, Vmax, N, isHot);
		
		calculateLimits();
		
		// Inicializar charts
		chartPV = new Grafica(nullArray,"", "", "V", "P", false, Color.BLUE,1f,false);
		chartPV.agregarGrafica(nullArray, "", Color.RED, 1f, false);
		chartPV.setRangeAxis(VLimits, PLimits);
		
		chartUV = new Grafica(nullArray,"", "", "V", "U", false, Color.BLUE,1f,false);
		chartUV.agregarGrafica(nullArray, "", Color.RED, 1f, false);
		chartUV.setRangeAxis(VLimits, ULimits);
		
		
		chartTV = new Grafica(nullArray,"", "", "V", "T", false, Color.BLUE,1f,false);
		chartTV.agregarGrafica(nullArray, "", Color.RED, 1f, false);
		chartTV.setRangeAxis(VLimits, TLimits);
		
		
		chartPT = new Grafica(nullArray,"", "", "T", "P", false, Color.BLUE,1f,false);
		chartPT.agregarGrafica(nullArray, "", Color.RED, 1f, false);
		chartPT.setRangeAxis(TLimits, PLimits);
		
		
		chartST = new Grafica(nullArray,"", "", "T", "S", false, Color.BLUE,1f,false);
		chartST.agregarGrafica(nullArray, "", Color.RED, 1f, false);
		chartST.setRangeAxis(TLimits, SLimits);
		
		chartUT = new Grafica(nullArray,"", "", "T", "U", false, Color.BLUE,1f,false);
		chartUT.agregarGrafica(nullArray, "", Color.RED, 1f, false);
		chartUT.setRangeAxis(TLimits, ULimits);
		
		chartPiston = new Grafica(nullArray, "", "", "", "", false, Color.BLUE, 1f, false);
		chartPiston.setRangeAxis(getPistonX0() - 0.5, (getPistonX1() + 0.5), 0, 10);
		chartPiston.fijaFondo(Color.WHITE);
		chartPiston.setAxisVisible(false);
			
		chartTermo = new Grafica(nullArray, "", "", "", "", false, Color.BLUE, 1f, false);
		chartTermo.setRangeAxis(0, 10, 0, 10);
		chartTermo.fijaFondo(Color.WHITE);
		chartTermo.setAxisVisible(false);
		
		tBulb = new Time();
		tLED = new Time();

		//Load Images
		waterTextureImage = loadImage(waterTexture);
		texture = new TexturePaint(waterTextureImage, new Rectangle(0,0,300,200));
		termoImage = loadImage(termo);

		//Set Images  
		chartTermo.setImageAtPoint(termoImage, 5, 5);
		
		tAnnotation = new XYTextAnnotation("-", 5, 5);
		tAnnotation.setPaint(Color.RED);
		tAnnotation.setFont(new Font(null, 10, 40));
		chartTermo.setAnnotation(tAnnotation);
		
		this.drawPiston();

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
	
	private void calculateLimits(){
		VLimits = model.getVLimits();
		PLimits = model.getPLimits();
		ULimits = model.getULimits();
		TLimits = model.getTLimits();
		SLimits = model.getSLimits();
	}

	
	private void drawPiston(){
		BasicStroke stroke = new BasicStroke(5.0f);
		
		chartPiston.deleteAnnotation(lineAnnotation3);
		lineAnnotation3 = chartPiston.drawLine(getPistonX0(), getPistonY0(), getPistonX1(), getPistonY0(), stroke, Color.black);
		chartPiston.deleteAnnotation(lineAnnotation4);
		lineAnnotation4 = chartPiston.drawLine(getPistonX0(), getPistonY1(), getPistonX1(), getPistonY1(), stroke, Color.black);
		chartPiston.deleteAnnotation(lineAnnotation5);
		lineAnnotation5 = chartPiston.drawLine(getPistonX1(), getPistonY0(), getPistonX1(), getPistonY1(), stroke, Color.black);
	}
	
	private void updatePiston(){
		BasicStroke stroke = new BasicStroke(5.0f);
		
		chartPiston.deleteAnnotation(pistonImageAnnotation);
		if(model.getV() < getPistonY0()){
			pistonImageAnnotation = chartPiston.drawBox(model.getV(), getPistonY0(), getPistonX1(), getPistonY1(), null, null,texture);
		}
		else{
			pistonImageAnnotation = null;
		}
		
		chartPiston.deleteAnnotation(lineAnnotation1);
		lineAnnotation1 = chartPiston.drawLine(model.getV(), getPistonY0() - 0.5, model.getV(), getPistonY1() + 0.5, stroke, Color.black);
		chartPiston.deleteAnnotation(lineAnnotation2);
		lineAnnotation2 = chartPiston.drawLine(model.getV() - 3, (getPistonY0()+getPistonY1())/2, model.getV(), (getPistonY0()+getPistonY1())/2, stroke, Color.black);
	}

	
	private void updatePanels(){
		panelPV.actualizaGrafica(chartPV);
		panelUV.actualizaGrafica(chartUV);
		panelTV.actualizaGrafica(chartTV);
		panelPT.actualizaGrafica(chartPT);
		panelST.actualizaGrafica(chartST);
		panelUT.actualizaGrafica(chartUT);
		panelPistonInterno.actualizaGrafica(chartPiston);
		panelTermo.actualizaGrafica(chartTermo);
	}
	
	private void updateTermo(){
		String x = String.valueOf(model.getT());
		
		if(x.length() > 5){
			x = x.substring(0,5);
		}
	
		chartTermo.deleteAnnotation(tAnnotation);
		tAnnotation = new XYTextAnnotation(x, 5, 5);
		tAnnotation.setPaint(Color.RED);
		tAnnotation.setFont(new Font(null, 10, 40));
		chartTermo.setAnnotation(tAnnotation);
	}
	
	private double getPistonX0(){
		return -Vmax*1000*N;
	}
	
	private double getPistonX1(){
		return 0;
	}
	
	private double getPistonY0(){
		return pistonY0;
	}
	
	private double getPistonY1(){
		return pistonY1;
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

		lblPhase = new JLabel("Trabajo Ciclo:");
		lblPhase.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblTrabajoCValue = new JLabel();
		lblTrabajoCValue.setText("0");
		lblTrabajoCValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblPosicion = new JLabel("Trabajo:");
		lblPosicion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblTrabajoValue = new JLabel();
		lblTrabajoValue.setText("0");
		lblTrabajoValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		hotImage = loadImage(hot);
		
		coldImage = loadImage(cold);
		
		lblE = new JLabel("Rendimiento:");
		lblE.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblEValue = new JLabel();
		lblEValue.setText("0");
		lblEValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitleOutputs, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblE, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addGap(26)
							.addComponent(lblEValue, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addGroup(gl_panelOutputs.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panelOutputs.createSequentialGroup()
									.addComponent(lblPhase, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGap(26))
								.addGroup(gl_panelOutputs.createSequentialGroup()
									.addComponent(lblPosicion, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
									.addGap(29)))
							.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblTrabajoValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblTrabajoCValue, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))))
					.addGap(109))
		);
		gl_panelOutputs.setVerticalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTrabajoCValue)
						.addComponent(lblPhase))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPosicion, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTrabajoValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addComponent(lblE, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGap(79))
		);
		panelOutputs.setLayout(gl_panelOutputs);

		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_panel_control = new GroupLayout(panel_control);
		gl_panel_control.setHorizontalGroup(
			gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_control.createParallelGroup(Alignment.LEADING)
						.addComponent(panelInputs, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
						.addComponent(panelOutputs, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_6, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_control.setVerticalGroup(
			gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelInputs, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelOutputs, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 146, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(24))
		);
		
				btnLaunchSimulation = new JButton("Iniciar");
				btnLaunchSimulation.setFont(new Font("Tahoma", Font.PLAIN, 16));
				btnLaunchSimulation.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						btnLaunchSimulationEvent(event);
					}
				});
		
		btnCold = new JButton("");
		btnCold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnHot.setEnabled(true);
				btnCold.setEnabled(false);
				isHot = false;
				lblE.setText("Eficiencia");
				
				lblModeValue.setText("Modo Frigorífico");
				panelMode.setBackground(Color.BLUE);
				
			}
		});
		btnCold.setIcon(new ImageIcon(coldImage));
		btnCold.setEnabled(true);
		
		btnHot = new JButton("");
		btnHot.setIcon(new ImageIcon(hotImage));
		btnHot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnHot.setEnabled(false);
				btnCold.setEnabled(true);
				isHot = true;
				lblE.setText("Rendimiento");
		
				lblModeValue.setText("Modo Motor");
				panelMode.setBackground(Color.ORANGE);
			}
		});
		btnHot.setEnabled(false);
		
		panelMode = new JPanel();
		panelMode.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelMode.setBackground(Color.ORANGE);
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(btnLaunchSimulation, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
						.addComponent(panelMode, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(btnHot, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCold, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_6.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panelMode, 0, 0, Short.MAX_VALUE)
						.addComponent(btnHot, GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnCold, 0, 0, Short.MAX_VALUE)
						.addComponent(btnLaunchSimulation, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
					.addContainerGap(21, Short.MAX_VALUE))
		);
		
		lblModeValue = new JLabel("Modo Motor");
		lblModeValue.setFont(new Font("Tahoma", Font.PLAIN, 17));
		GroupLayout gl_panelMode = new GroupLayout(panelMode);
		gl_panelMode.setHorizontalGroup(
			gl_panelMode.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelMode.createSequentialGroup()
					.addGap(53)
					.addComponent(lblModeValue, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panelMode.setVerticalGroup(
			gl_panelMode.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelMode.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblModeValue, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
					.addContainerGap())
		);
		panelMode.setLayout(gl_panelMode);
		panel_6.setLayout(gl_panel_6);

		JLabel lblNewLabel = new JLabel("GNU GENERAL PUBLIC LICENSE");
		panel_1.add(lblNewLabel);

		JLabel LabelT1 = new JLabel("T1");
		LabelT1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelT2 = new JLabel("T2");
		labelT2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelVmin = new JLabel("Vm\u00EDn");
		labelVmin.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		lblT2Value = new JLabel("300");
		lblT2Value.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblVminValue = new JLabel("2");
		lblVminValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		lblT1Value = new JLabel("400");
		lblT1Value.setFont(new Font("Tahoma", Font.PLAIN, 14));


		sliderT1 = new JSlider();
		sliderT1.setMinimum(350);
		sliderT1.setMaximum(450);
		sliderT1.setMinorTickSpacing(1);
		sliderT1.setValue(400);
		sliderT1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderT1Event();
			}
		});


		sliderT2 = new JSlider();
		sliderT2.setMinimum(270);
		sliderT2.setMaximum(330);
		sliderT2.setMinorTickSpacing(1);
		sliderT2.setValue(300);
		sliderT2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderT2Event();
			}
		});




		sliderVMin = new JSlider();
		sliderVMin.setMaximum(4);
		sliderVMin.setMinimum(1);
		sliderVMin.setValue(2);
		sliderVMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderVminEvent();
			}
		});
		
		JLabel lblVmax = new JLabel("Vm\u00E1x");
		lblVmax.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblVmaxValue = new JLabel("7");
		lblVmaxValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		sliderVMax = new JSlider();
		sliderVMax.setMaximum(10);
		sliderVMax.setMinimum(6);
		sliderVMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sliderVmaxEvent();
			}
		});
		sliderVMax.setValue(7);
		sliderVMax.setMinorTickSpacing(1);
		
		JLabel labelN = new JLabel("N");
		labelN.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblNValue = new JLabel("10");
		lblNValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		sliderN = new JSlider();
		sliderN.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderNEvent();
			}
		});
		sliderN.setValue(10);
		sliderN.setMinorTickSpacing(1);




		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
			gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(labelVmin, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(LabelT1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelT2, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
								.addComponent(lblT1Value, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblT2Value, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblVminValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING, false)
								.addComponent(sliderT1, 0, 0, Short.MAX_VALUE)
								.addComponent(sliderT2, 0, 0, Short.MAX_VALUE)
								.addComponent(sliderVMin, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblVmax, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblVmaxValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sliderVMax, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(labelN, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblNValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sliderN, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
					.addGap(19))
				.addComponent(panelTitle, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
		);
		gl_panelInputs.setVerticalGroup(
			gl_panelInputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
							.addComponent(LabelT1)
							.addComponent(lblT1Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(sliderT1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
							.addComponent(labelT2)
							.addComponent(lblT2Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(sliderT2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(labelVmin)
						.addComponent(lblVminValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(sliderVMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(lblVmax, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblVmaxValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(sliderVMax, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(labelN, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(sliderN, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(429))
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
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_control, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(panel_visualizar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_control, GroupLayout.PREFERRED_SIZE, 568, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(panel_visualizar, GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)))
					.addContainerGap())
		);
				
				JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
				
				panelPiston = new JPanelGrafica();
				panelPiston.setBackground(Color.WHITE);
				
				JPanel panel_5 = new JPanel();
				panel_5.setBackground(Color.WHITE);
				
				JPanel panel = new JPanel();
				panel.setBackground(Color.WHITE);
				GroupLayout gl_panel_visualizar = new GroupLayout(panel_visualizar);
				gl_panel_visualizar.setHorizontalGroup(
					gl_panel_visualizar.createParallelGroup(Alignment.LEADING)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
						.addGroup(gl_panel_visualizar.createSequentialGroup()
							.addContainerGap()
							.addComponent(panelPiston, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
							.addContainerGap())
				);
				gl_panel_visualizar.setVerticalGroup(
					gl_panel_visualizar.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_visualizar.createSequentialGroup()
							.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 289, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_visualizar.createParallelGroup(Alignment.LEADING)
								.addComponent(panel, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
								.addComponent(panelPiston, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
								.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
							.addContainerGap())
				);
				
				panelTermo = new JPanelGrafica();
				panelTermo.setBackground(Color.WHITE);
				GroupLayout gl_panel = new GroupLayout(panel);
				gl_panel.setHorizontalGroup(
					gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panelTermo, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
				);
				gl_panel.setVerticalGroup(
					gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panelTermo, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
				);
				panel.setLayout(gl_panel);
				
				panelPistonInterno = new JPanelGrafica();
				panelPistonInterno.setBackground(Color.WHITE);
				GroupLayout gl_panelPiston = new GroupLayout(panelPiston);
				gl_panelPiston.setHorizontalGroup(
					gl_panelPiston.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelPiston.createSequentialGroup()
							.addComponent(panelPistonInterno, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
				gl_panelPiston.setVerticalGroup(
					gl_panelPiston.createParallelGroup(Alignment.LEADING)
						.addComponent(panelPistonInterno, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
				);
				panelPiston.setLayout(gl_panelPiston);
				
		
				JPanel panelBulb = new JPanel();
				panelBulb.setBackground(Color.WHITE);
				
				JPanel panelLED = new JPanel();
				panelLED.setBackground(Color.WHITE);
				
			    lblBulbValue = new JLabel("-  Horas");
				
				lblLEDValue = new JLabel("-  Horas");
				GroupLayout gl_panel_5 = new GroupLayout(panel_5);
				gl_panel_5.setHorizontalGroup(
					gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_5.createSequentialGroup()
							.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING, false)
								.addComponent(panelLED, 0, 0, Short.MAX_VALUE)
								.addComponent(panelBulb, GroupLayout.PREFERRED_SIZE, 132, Short.MAX_VALUE))
							.addGap(18)
							.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
								.addComponent(lblBulbValue)
								.addComponent(lblLEDValue))
							.addContainerGap(87, Short.MAX_VALUE))
				);
				gl_panel_5.setVerticalGroup(
					gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_5.createSequentialGroup()
							.addComponent(panelBulb, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(panelLED, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
						.addGroup(gl_panel_5.createSequentialGroup()
							.addGap(64)
							.addComponent(lblBulbValue)
							.addPreferredGap(ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
							.addComponent(lblLEDValue)
							.addGap(64))
				);
				
				LEDOnImage = loadImage(LEDOn);
				LEDOffImage = loadImage(LEDOff);
				lblLED = new JLabel(new ImageIcon(LEDOffImage));
				lblLED.setBackground(Color.WHITE);
				
				GroupLayout gl_panelLED = new GroupLayout(panelLED);
				gl_panelLED.setHorizontalGroup(
					gl_panelLED.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelLED.createSequentialGroup()
							.addComponent(lblLED, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(33, Short.MAX_VALUE))
				);
				gl_panelLED.setVerticalGroup(
					gl_panelLED.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_panelLED.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblLED, GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
				);
				panelLED.setLayout(gl_panelLED);
				
				bulbOnImage = loadImage(bulbOn);
				bulbOffImage = loadImage(bulbOff);
				lblBulb = new JLabel(new ImageIcon(bulbOffImage));
				lblBulb.setBackground(Color.WHITE);
				
				GroupLayout gl_panelBulb = new GroupLayout(panelBulb);
				gl_panelBulb.setHorizontalGroup(
					gl_panelBulb.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelBulb.createSequentialGroup()
							.addComponent(lblBulb, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(30, Short.MAX_VALUE))
				);
				gl_panelBulb.setVerticalGroup(
					gl_panelBulb.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBulb, GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
				);
				panelBulb.setLayout(gl_panelBulb);
				panel_5.setLayout(gl_panel_5);
				
				JPanel panelXV = new JPanel();
				panelXV.setBackground(Color.WHITE);
				tabbedPane.addTab("Gráficas V", null, panelXV, null);
				
				JPanel panel_2 = new JPanel();
				
				JPanel panel_3 = new JPanel();
				
				JPanel panel_4 = new JPanel();
				
				panelTV = new JPanelGrafica();
				GroupLayout gl_panel_4 = new GroupLayout(panel_4);
				gl_panel_4.setHorizontalGroup(
					gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGap(0, 272, Short.MAX_VALUE)
						.addComponent(panelTV, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
				);
				gl_panel_4.setVerticalGroup(
					gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGap(0, 241, Short.MAX_VALUE)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addComponent(panelTV, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(306, Short.MAX_VALUE))
				);
				panel_4.setLayout(gl_panel_4);
				GroupLayout gl_panelXV = new GroupLayout(panelXV);
				gl_panelXV.setHorizontalGroup(
					gl_panelXV.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelXV.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(11, Short.MAX_VALUE))
				);
				gl_panelXV.setVerticalGroup(
					gl_panelXV.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelXV.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelXV.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panelXV.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(panel_3, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
									.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
							.addContainerGap(317, Short.MAX_VALUE))
				);
				
				panelUV = new JPanelGrafica();
				GroupLayout gl_panel_3 = new GroupLayout(panel_3);
				gl_panel_3.setHorizontalGroup(
					gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(panelUV, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
				);
				gl_panel_3.setVerticalGroup(
					gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(panelUV, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(306, Short.MAX_VALUE))
				);
				panel_3.setLayout(gl_panel_3);
				
				panelPV = new JPanelGrafica();
				GroupLayout gl_panel_2 = new GroupLayout(panel_2);
				gl_panel_2.setHorizontalGroup(
					gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(panelPV, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
				);
				gl_panel_2.setVerticalGroup(
					gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(panelPV, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
				);
				panel_2.setLayout(gl_panel_2);
				panelXV.setLayout(gl_panelXV);
				
				JPanel panelXT = new JPanel();
				panelXT.setBackground(Color.WHITE);
				tabbedPane.addTab("Gráficas T", null, panelXT, null);
				
				JPanel panel_9 = new JPanel();
				
				panelPT = new JPanelGrafica();
				GroupLayout gl_panel_9 = new GroupLayout(panel_9);
				gl_panel_9.setHorizontalGroup(
					gl_panel_9.createParallelGroup(Alignment.LEADING)
						.addGap(0, 263, Short.MAX_VALUE)
						.addComponent(panelPT, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
				);
				gl_panel_9.setVerticalGroup(
					gl_panel_9.createParallelGroup(Alignment.LEADING)
						.addGap(0, 241, Short.MAX_VALUE)
						.addComponent(panelPT, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
				);
				panel_9.setLayout(gl_panel_9);
				
				JPanel panel_10 = new JPanel();
				
				panelST = new JPanelGrafica();
				GroupLayout gl_panel_10 = new GroupLayout(panel_10);
				gl_panel_10.setHorizontalGroup(
					gl_panel_10.createParallelGroup(Alignment.LEADING)
						.addGap(0, 272, Short.MAX_VALUE)
						.addComponent(panelST, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
				);
				gl_panel_10.setVerticalGroup(
					gl_panel_10.createParallelGroup(Alignment.LEADING)
						.addGap(0, 241, Short.MAX_VALUE)
						.addGroup(gl_panel_10.createSequentialGroup()
							.addComponent(panelST, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
				panel_10.setLayout(gl_panel_10);
				
				JPanel panel_11 = new JPanel();
				
				panelUT = new JPanelGrafica();
				GroupLayout gl_panel_11 = new GroupLayout(panel_11);
				gl_panel_11.setHorizontalGroup(
					gl_panel_11.createParallelGroup(Alignment.LEADING)
						.addGap(0, 272, Short.MAX_VALUE)
						.addComponent(panelUT, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
				);
				gl_panel_11.setVerticalGroup(
					gl_panel_11.createParallelGroup(Alignment.LEADING)
						.addGap(0, 241, Short.MAX_VALUE)
						.addGroup(gl_panel_11.createSequentialGroup()
							.addComponent(panelUT, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
				panel_11.setLayout(gl_panel_11);
				GroupLayout gl_panelXT = new GroupLayout(panelXT);
				gl_panelXT.setHorizontalGroup(
					gl_panelXT.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelXT.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_11, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(11, Short.MAX_VALUE))
				);
				gl_panelXT.setVerticalGroup(
					gl_panelXT.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelXT.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelXT.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_11, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panelXT.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(panel_10, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
									.addComponent(panel_9, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
							.addContainerGap(317, Short.MAX_VALUE))
				);
				panelXT.setLayout(gl_panelXT);
				panel_visualizar.setLayout(gl_panel_visualizar);

		getContentPane().setLayout(groupLayout);
	}
}