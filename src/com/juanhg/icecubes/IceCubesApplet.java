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
import java.awt.Color;
import java.awt.Font;
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

import java.awt.FlowLayout;

import javax.swing.JSpinner;

import java.awt.Choice;
import java.awt.List;

public class IceCubesApplet extends JApplet implements Runnable {


	private static final long serialVersionUID = -3017107307819023599L;
	private final String water = "water.png";

	final int pistonY0 = 3;
	final int pistonY1 = 7;
	
	//Control variables
	long sleepTime = 100;	
	boolean end = false;
	
	//Inputs
	double vol, T, t;
	int N, type;
	
	//Thread that executed the simulation
	private Thread flujo = null;

	//Model
	private IceCubesModel model;
	
	//Charts
	private Grafica chartTQ;
	
	//Panels
	private JPanelGrafica panel7, panelChart;

	int supXLimit = 10;
	int infXLimit = 0;
	int supYLimit = 10;
	int infYLimit = 0;

	//Images
	BufferedImage waterImage;
	
	//Annotations
	XYAnnotation exampleAnnotation = null;

	//Labels
	private JLabel lblCaseValue;  
	private JLabel lblTValue, lbltValue, lblVolValue, lblPhaseValue, lblNValue;
	private JLabel lblO1;

	//Sliders
	private JSlider sliderVol, sliderT, slidert, sliderN; 

	//Buttons
	JButton btnLaunchSimulation, btnWater, btnMilk, btn3, btn4;



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
		//350 - 450
		if(sliderVol.getValueIsAdjusting()){
			lblVolValue.setText(Integer.toString(sliderVol.getValue()));
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

			//Initializes and runs the thread (Run())
			flujo = new Thread();
			flujo = new Thread(this);
			
			model = new IceCubesModel(vol, T, t, 0, N);

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


		while(!end){

			model.getTime().pause();
			//Begin step of simulation
			model.simulate();
			//End Step of simulation
			model.getTime().start();
			
			chartTQ.replacePlot(0, model.getChartTQ(), "", Color.BLUE, 1f, true);
			this.updatePanels();
			
			lblCaseValue.setText("" + model.getCurrentCase());
			lblPhaseValue.setText("" + model.getCurrentPhase());

			
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

		//Crear modelo
		//model = new IceCubesModel(I1, I2, I3, (int)I4, (int)I5);
		
		// Inicializar charts
		chartTQ = new Grafica(nullArray,"", "", "", "", false, Color.BLUE,1f,false);
		chartTQ.setRangeAxis(0, 2500, -35, 35);
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
	
	private void updatePanels(){
		panelChart.actualizaGrafica(chartTQ);
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

		lblO1 = new JLabel("Caso:");
		lblO1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblCaseValue = new JLabel();
		lblCaseValue.setText("0");
		lblCaseValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblO2 = new JLabel("Fase:");
		lblO2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblPhaseValue = new JLabel();
		lblPhaseValue.setText("0");
		lblPhaseValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitleOutputs, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addGap(22)
							.addComponent(lblO1, GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(Alignment.TRAILING, gl_panelOutputs.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblO2, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
							.addGap(37)))
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblCaseValue, GroupLayout.DEFAULT_SIZE, 13, Short.MAX_VALUE)
							.addGap(3))
						.addGroup(Alignment.LEADING, gl_panelOutputs.createSequentialGroup()
							.addComponent(lblPhaseValue, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(173))
		);
		gl_panelOutputs.setVerticalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblO1)
						.addComponent(lblCaseValue))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblO2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPhaseValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
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


		lblVolValue = new JLabel("33");
		lblVolValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		sliderVol = new JSlider();
		sliderVol.setMinimum(1);
		sliderVol.setMaximum(33);
		sliderVol.setMinorTickSpacing(1);
		sliderVol.setValue(33);
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
		
		lblNValue = new JLabel("5");
		lblNValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		sliderN = new JSlider();
		sliderN.setMaximum(10);
		sliderN.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sliderI4Event();
			}
		});
		sliderN.setValue(5);
		sliderN.setMinorTickSpacing(1);
		
		waterImage = loadImage(water);
		btnWater = new JButton(new ImageIcon(waterImage));
		btnWater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		btnMilk = new JButton("Milk");
		
		btn3 = new JButton("Coke");
		
		btn4 = new JButton("New button");




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
							.addComponent(btn3, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btn4, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
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
						.addComponent(btn3, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
						.addComponent(btn4, GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
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
				panel_1.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
				
				JLabel lblSimulacin = new JLabel("Simulaci\u00F3n");
				lblSimulacin.setFont(new Font("Tahoma", Font.PLAIN, 14));
				panel_1.add(lblSimulacin);
				GroupLayout gl_panel = new GroupLayout(panel);
				gl_panel.setHorizontalGroup(
					gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
				);
				gl_panel.setVerticalGroup(
					gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(536, Short.MAX_VALUE))
				);
				panel.setLayout(gl_panel);
				
				JPanel panel_3 = new JPanel();
				panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
				panel_3.setBackground(Color.WHITE);
				panel_3.setBounds(368, 0, 326, 245);
				panel_visualizar.setLayout(null);
				panel_visualizar.add(panel);
				panel_visualizar.add(panel_3);
				panel_3.setLayout(null);
				
				JPanel panel_4 = new JPanel();
				panel_4.setBounds(1, 1, 324, 31);
				panel_4.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
				
				JLabel lblGrficaDeEvolucin = new JLabel("Gr\u00E1fica de Evoluci\u00F3n (T frente a Q)");
				lblGrficaDeEvolucin.setFont(new Font("Tahoma", Font.PLAIN, 14));
				panel_4.add(lblGrficaDeEvolucin);
				panel_3.add(panel_4);
				
				panel7 = new JPanelGrafica();
				panel7.setBorder(new LineBorder(new Color(0, 0, 0)));
				panel7.setBackground(Color.WHITE);
				panel7.setBounds(1, 31, 324, 214);
				panel_3.add(panel7);
				panel7.setLayout(null);
				
				panelChart = new JPanelGrafica();
				panelChart.setBorder(new LineBorder(new Color(0, 0, 0)));
				panelChart.setBackground(Color.WHITE);
				panelChart.setBounds(0, 0, 324, 214);
				panel7.add(panelChart);
				
				JPanel panel_2 = new JPanel();
				panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
				panel_2.setBackground(Color.WHITE);
				panel_2.setBounds(368, 243, 326, 251);
				panel_visualizar.add(panel_2);
				
				JPanel panel_5 = new JPanel();
				panel_5.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
				
				JLabel lblTemperaturaFludo = new JLabel("Temperatura del Flu\u00EDdo");
				lblTemperaturaFludo.setFont(new Font("Tahoma", Font.PLAIN, 14));
				panel_5.add(lblTemperaturaFludo);
				
				JLabel lblNewLabel_1 = new JLabel("0");
				lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 40));
				GroupLayout gl_panel_2 = new GroupLayout(panel_2);
				gl_panel_2.setHorizontalGroup(
					gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_panel_2.createSequentialGroup()
							.addContainerGap(148, Short.MAX_VALUE)
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
							.addGap(45))
				);
				gl_panel_2.setVerticalGroup(
					gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(62)
							.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(74, Short.MAX_VALUE))
				);
				panel_2.setLayout(gl_panel_2);

		getContentPane().setLayout(groupLayout);
	}
}