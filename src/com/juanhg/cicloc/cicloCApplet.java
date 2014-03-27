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
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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

import com.raccoon.easyjchart.Grafica;
import com.raccoon.easyjchart.JPanelGrafica;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class cicloCApplet extends JApplet implements Runnable {


	private static final long serialVersionUID = -3017107307819023599L;
	
	//Control variables
	double sleepTime = 5;	
	boolean end = false;

	//Inputs
	double T1, T2, Vmin, Vmax, N;

	//Thread that executed the simulation
	private Thread flujo = null;

	//Model
	private cicloCModel model;
	
	//Charts
	private Grafica chartPV, chartUV, chartTV, chartUT, chartST, chartPT;
	
	//Panels
	private JPanel panel_1;
	private JPanelGrafica panelPV, panelUV, panelTV, panelST, panelUT, panelPT;

	int supXLimit = 8;
	int infXLimit = 2;
	int supYLimit = 11000000;
	int infYLimit = 0;

	Point2D VLimits, PLimits, ULimits, TLimits, SLimits;
	
	double modif = 0;


	//Images


	//Annotations

	//Labels
	private JLabel lblVValue;  
	private JLabel lblT2Value, lblVminValue, lblT1Value, lblPositionValue, lblVmaxValue, lblNValue;
	private JLabel lblPhase;

	//Sliders
	private JSlider sliderT1, sliderT2, sliderVMin, sliderVMax, sliderN; 

	//Buttons
	JButton btnLaunchSimulation;


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

		setSize(1240,610);

		this.autogeneratedCode();

		//Obtain values from interface
		this.readInputs();
		this.initSimulation();	
	}



	void sliderStrenghtEvent(){

		if(sliderT1.getValueIsAdjusting()){
			lblT1Value.setText(Integer.toString(sliderT1.getValue()));
		}
	}
	void sliderDynamicFrictionEvent(){
		double dynamicF;
		if(sliderVMin.getValueIsAdjusting()){
			dynamicF = (double) sliderVMin.getValue();
//			lblVminValue.setText(Double.toString(dynamicF/100.0));
		}
	}

	void sliderStaticFrictionEvent(){

		if(sliderT2.getValueIsAdjusting()){
			double staticF;
			staticF = (double) sliderT2.getValue();
//			lblT2Value.setText(Double.toString(staticF/100.0));
		}
	}

	void btnLaunchSimulationEvent(ActionEvent event){

		boolean buttonsOn = false;

		if(flujo != null && flujo.isAlive()) {
			end = true;
			buttonsOn = true;

			while(flujo.isAlive()) {}

			model.getT().stop();

			this.readInputs();
			this.initSimulation();

			btnLaunchSimulation.setText("Iniciar");

			sliderT1.setEnabled(buttonsOn);
			sliderVMin.setEnabled(buttonsOn);
			sliderT2.setEnabled(buttonsOn);

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
			sliderT2.setEnabled(buttonsOn);
			model.simulate();
			
			model.getT().start();
			flujo.start();
		}
	}
	
	@Override
	public void run() {

		double pSize = 3f;
		end = false;


		while(!end){

			model.getT().pause();
			
			//Begin step of simulation
			model.simulate();
			//End Step of simulation
			
			//Replace Plots
			if(!model.isFirstCompleted()){
				chartPV.replacePlot(1 ,model.getFPV(),"", Color.BLUE,1f,false);
				chartPV.visualizaMuestras(1,true,0.7);
				chartUV.replacePlot(1 ,model.getFUV(),"", Color.BLUE,1f,false);
				chartUV.visualizaMuestras(1,true,0.7);
				chartTV.replacePlot(1 ,model.getFTV(),"", Color.BLUE,1f,false);
				chartTV.visualizaMuestras(1,true,0.7);
				chartPT.replacePlot(1 ,model.getFPT(),"", Color.BLUE,1f,false);
				chartPT.visualizaMuestras(1,true,0.7);
				chartST.replacePlot(1 ,model.getFST(),"", Color.BLUE,1f,false);
				chartST.visualizaMuestras(1,true,0.7);
				chartUT.replacePlot(1 ,model.getFUT(),"", Color.BLUE,1f,false);
				chartUT.visualizaMuestras(1,true,0.7);
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
			
			model.getT().start();
			
			
			this.updatePanels();
			repaint();
			
			try {
				Thread.sleep((long)20);
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
		model = new cicloCModel(T1, T2, Vmin, Vmax, N);
		
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
		
		

		//Load Images


		//Set Images  


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


	
	private void updatePanels(){
		panelPV.actualizaGrafica(chartPV);
		panelUV.actualizaGrafica(chartUV);
		panelTV.actualizaGrafica(chartTV);
		panelPT.actualizaGrafica(chartPT);
		panelST.actualizaGrafica(chartST);
		panelUT.actualizaGrafica(chartUT);
//		panelPiston2.actualizaGrafica(chartPiston2);
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

		lblPhase = new JLabel("Velocidad:");
		lblPhase.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblVValue = new JLabel();
		lblVValue.setText("0");
		lblVValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblPosicion = new JLabel("Posici\u00F3n:");
		lblPosicion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblPositionValue = new JLabel();
		lblPositionValue.setText("0");
		lblPositionValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
				btnLaunchSimulation = new JButton("Iniciar");
				btnLaunchSimulation.setFont(new Font("Tahoma", Font.PLAIN, 16));
				btnLaunchSimulation.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						btnLaunchSimulationEvent(event);
					}
				});
		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblPhase, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(26)
							.addComponent(lblVValue, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 89, Short.MAX_VALUE))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblPosicion, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addGap(26)
							.addComponent(lblPositionValue, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 70, Short.MAX_VALUE)))
					.addGap(7))
				.addComponent(panelTitleOutputs, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnLaunchSimulation, GroupLayout.PREFERRED_SIZE, 324, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panelOutputs.setVerticalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPhase)
						.addComponent(lblVValue))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPosicion, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPositionValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGap(143)
					.addComponent(btnLaunchSimulation, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panelOutputs.setLayout(gl_panelOutputs);

		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_panel_control = new GroupLayout(panel_control);
		gl_panel_control.setHorizontalGroup(
			gl_panel_control.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_control.createParallelGroup(Alignment.TRAILING)
						.addComponent(panelOutputs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
						.addComponent(panelInputs, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 343, Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_control.setVerticalGroup(
			gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelInputs, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelOutputs, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

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

		lblVminValue = new JLabel("3");
		lblVminValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		lblT1Value = new JLabel("350");
		lblT1Value.setFont(new Font("Tahoma", Font.PLAIN, 14));


		sliderT1 = new JSlider();
		sliderT1.setMaximum(400);
		sliderT1.setMinorTickSpacing(1);
		sliderT1.setValue(350);
		sliderT1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderStrenghtEvent();
			}
		});


		sliderT2 = new JSlider();
		sliderT2.setMaximum(400);
		sliderT2.setMinorTickSpacing(1);
		sliderT2.setValue(300);
		sliderT2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderStaticFrictionEvent();
			}
		});




		sliderVMin = new JSlider();
		sliderVMin.setValue(3);
		sliderVMin.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderDynamicFrictionEvent();
			}
		});
		
		JLabel lblVmax = new JLabel("Vmax");
		lblVmax.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblVmaxValue = new JLabel("6");
		lblVmaxValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		sliderVMax = new JSlider();
		sliderVMax.setValue(6);
		sliderVMax.setMinorTickSpacing(1);
		
		JLabel labelN = new JLabel("N");
		labelN.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblNValue = new JLabel("10");
		lblNValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		sliderN = new JSlider();
		sliderN.setValue(10);
		sliderN.setMinorTickSpacing(1);




		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
			gl_panelInputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, 342, GroupLayout.PREFERRED_SIZE)
				.addGroup(Alignment.TRAILING, gl_panelInputs.createSequentialGroup()
					.addContainerGap(23, Short.MAX_VALUE)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
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
								.addComponent(sliderVMax, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(labelN, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblNValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sliderN, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
					.addGap(19))
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


		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_control, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_visualizar, GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_control, GroupLayout.PREFERRED_SIZE, 598, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(panel_visualizar, GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)))
					.addContainerGap())
		);
				
				JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
				GroupLayout gl_panel_visualizar = new GroupLayout(panel_visualizar);
				gl_panel_visualizar.setHorizontalGroup(
					gl_panel_visualizar.createParallelGroup(Alignment.LEADING)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 845, Short.MAX_VALUE)
				);
				gl_panel_visualizar.setVerticalGroup(
					gl_panel_visualizar.createParallelGroup(Alignment.LEADING)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
				);
				
				JPanel panelXV = new JPanel();
				panelXV.setBackground(Color.WHITE);
				tabbedPane.addTab("Gr�ficas V", null, panelXV, null);
				
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
				
				JPanel panel_5 = new JPanel();
				
				JPanelGrafica panelPiston1 = new JPanelGrafica();
				GroupLayout gl_panel_5 = new GroupLayout(panel_5);
				gl_panel_5.setHorizontalGroup(
					gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addGap(0, 263, Short.MAX_VALUE)
						.addComponent(panelPiston1, GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
				);
				gl_panel_5.setVerticalGroup(
					gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addGap(0, 241, Short.MAX_VALUE)
						.addComponent(panelPiston1, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
				);
				panel_5.setLayout(gl_panel_5);
				
				JPanel panel_6 = new JPanel();
				
				JPanelGrafica panelDraw1 = new JPanelGrafica();
				GroupLayout gl_panel_6 = new GroupLayout(panel_6);
				gl_panel_6.setHorizontalGroup(
					gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(panelDraw1, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
				);
				gl_panel_6.setVerticalGroup(
					gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addComponent(panelDraw1, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
				);
				panel_6.setLayout(gl_panel_6);
				GroupLayout gl_panelXV = new GroupLayout(panelXV);
				gl_panelXV.setHorizontalGroup(
					gl_panelXV.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelXV.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelXV.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(Alignment.LEADING, gl_panelXV.createSequentialGroup()
									.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelXV.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE))
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
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panelXV.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
								.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
							.addContainerGap())
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
				tabbedPane.addTab("Gr�ficas T", null, panelXT, null);
				
				JPanel panel_7 = new JPanel();
				
				JPanelGrafica panelDraw2 = new JPanelGrafica();
				GroupLayout gl_panel_7 = new GroupLayout(panel_7);
				gl_panel_7.setHorizontalGroup(
					gl_panel_7.createParallelGroup(Alignment.LEADING)
						.addGap(0, 272, Short.MAX_VALUE)
						.addComponent(panelDraw2, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
				);
				gl_panel_7.setVerticalGroup(
					gl_panel_7.createParallelGroup(Alignment.LEADING)
						.addGap(0, 295, Short.MAX_VALUE)
						.addComponent(panelDraw2, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
				);
				panel_7.setLayout(gl_panel_7);
				
				JPanel panel_8 = new JPanel();
				
				JPanelGrafica panelPiston2 = new JPanelGrafica();
				GroupLayout gl_panel_8 = new GroupLayout(panel_8);
				gl_panel_8.setHorizontalGroup(
					gl_panel_8.createParallelGroup(Alignment.LEADING)
						.addGap(0, 541, Short.MAX_VALUE)
						.addComponent(panelPiston2, GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
				);
				gl_panel_8.setVerticalGroup(
					gl_panel_8.createParallelGroup(Alignment.LEADING)
						.addGap(0, 295, Short.MAX_VALUE)
						.addComponent(panelPiston2, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
				);
				panel_8.setLayout(gl_panel_8);
				
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
						.addGap(0, 840, Short.MAX_VALUE)
						.addGroup(gl_panelXT.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelXT.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_8, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(Alignment.LEADING, gl_panelXT.createSequentialGroup()
									.addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelXT.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_11, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
								.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(11, Short.MAX_VALUE))
				);
				gl_panelXT.setVerticalGroup(
					gl_panelXT.createParallelGroup(Alignment.LEADING)
						.addGap(0, 569, Short.MAX_VALUE)
						.addGroup(gl_panelXT.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelXT.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_11, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panelXT.createParallelGroup(Alignment.TRAILING, false)
									.addComponent(panel_10, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
									.addComponent(panel_9, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panelXT.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
								.addComponent(panel_8, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
							.addContainerGap())
				);
				panelXT.setLayout(gl_panelXT);
				panel_visualizar.setLayout(gl_panel_visualizar);

		getContentPane().setLayout(groupLayout);
	}
}