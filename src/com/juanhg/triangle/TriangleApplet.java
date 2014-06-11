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

package com.juanhg.triangle;
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

public class TriangleApplet extends JApplet implements Runnable {

	private final int CILINDRO = 1;
	private final int ESFERA = 2;
	private final int CUBO = 3;
	
	private static final long serialVersionUID = -3017107307819023599L;
	private final String example = "example.png";

	final int pistonY0 = 3;
	final int pistonY1 = 7;
	int type = 1;
	
	//Control variables
	long sleepTime = 300;	
	boolean end = false;
	
	//Inputs
	double mue, mud, m, z, I5;
	
	//Thread that executed the simulation
	private Thread flujo = null;

	//Model
	private TriangleModel model;
	
	//Charts
	private Grafica chartTriangle;
	
	//Panels
	private JPanelGrafica panelGrafica;

	int supXLimit = 22;
	int infXLimit = -2;
	int supYLimit = 22;
	int infYLimit = -2;

	//Images
	BufferedImage exampleImage;
	
	//Annotations
	XYAnnotation exampleAnnotation = null;

	//Labels
	private JLabel lblO1Value;  
	private JLabel lblMudValue, lblMValue, lblMueValue, lblO2Value, lblZValue;
	private JLabel lblO1, lblO3Value, lblO3;

	//Sliders
	private JSlider sliderMue, sliderMud, sliderIM, sliderZ; 

	
	//Buttons
	JButton btnLaunchSimulation, btnCilindro, btnCubo, btnEsfera;


	public TriangleApplet() {}

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

		setSize(1100,590);

		this.autogeneratedCode();

		//Obtain values from interface
		this.readInputs();
		this.initSimulation();	
	}



	private void sliderI1Event(){
		//350 - 450
		if(sliderMue.getValueIsAdjusting()){
			double staticF;
			staticF = ((double) sliderMue.getValue())/100.0;
			lblMueValue.setText("" + staticF); 
		}
	}
	
	private void sliderI2Event(){
		//270-330
		if(sliderMud.getValueIsAdjusting()){
			double staticF;
			staticF = ((double) sliderMud.getValue())/100.0;
			lblMudValue.setText("" + staticF); 
		}
	}
	
	private void sliderI3Event(){
		//1-5
		double dynamicF;
		if(sliderIM.getValueIsAdjusting()){
			dynamicF = (double) sliderIM.getValue();
			lblMValue.setText("" + (int)dynamicF);
		}
	}
	
	private void sliderI4Event(){
		//6-10
		double dynamicF;
		if(sliderZ.getValueIsAdjusting()){
			dynamicF = (double) sliderZ.getValue();
			lblZValue.setText("" + (int)dynamicF +"�");
		}
	}
	
	
	void btnLaunchSimulationEvent(ActionEvent event){

		boolean buttonsOn = false;

		if(flujo != null && flujo.isAlive()) {
			end = true;
			buttonsOn = true;

			while(flujo.isAlive()) {}


			this.readInputs();
			this.initSimulation();

			btnLaunchSimulation.setText("Iniciar");

			sliderMue.setEnabled(buttonsOn);
			sliderIM.setEnabled(buttonsOn);
			sliderZ.setEnabled(buttonsOn);
			sliderMud.setEnabled(buttonsOn);
					
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

			sliderMue.setEnabled(buttonsOn);
			sliderIM.setEnabled(buttonsOn);
			sliderZ.setEnabled(buttonsOn);
			sliderMud.setEnabled(buttonsOn);
			model.simulate();
			
			flujo.start();
		}
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

	
	@Override
	public void run() {

		end = false;


		while(!end){

			
			//Begin step of simulation
			model.simulate();
			//End Step of simulation
			
			
			this.updatePanels();
			repaint();
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(TriangleApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	/**
	 * Read the input values from the interface and loads its 
	 * in the variable of the class 
	 */
	private void readInputs(){
		mue = sliderMue.getValue();
		mud = sliderMud.getValue();
		m = ((double)sliderIM.getValue())/1000.0;
		z = ((double)sliderZ.getValue())/1000.0;
	}

	//Init the elements of the simulation
	private void initSimulation(){

		Point2D [] nullArray = new Point2D[0];

		//Crear modelo
		model = new TriangleModel(mue, mud, m, z, type);
		
		// Inicializar charts
		chartTriangle = new Grafica(nullArray,"", "", "", "", false, Color.BLUE,1f,false);
		chartTriangle.setRangeAxis(infXLimit, supXLimit, infYLimit, supYLimit);
		
		//Load Images
		exampleImage = loadImage(example);

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
		panelGrafica.actualizaGrafica(chartTriangle);
	}
	
	public double normalize(double x, double a, double b, double c, double d){
		return (x*(d-c))/(b-a) + (c*b - a*d)/(b-a);
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

		lblO1 = new JLabel("Tensi\u00F3n:");
		lblO1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblO1Value = new JLabel();
		lblO1Value.setText("0");
		lblO1Value.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblO2 = new JLabel("V:");
		lblO2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblO2Value = new JLabel();
		lblO2Value.setText("0");
		lblO2Value.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblO3 = new JLabel("tfinal:");
		lblO3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblO3Value = new JLabel();
		lblO3Value.setText("0");
		lblO3Value.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblW = new JLabel("W:");
		lblW.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel label_1 = new JLabel();
		label_1.setText("0");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitleOutputs, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblO2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblO1, GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblO2Value, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblO1Value, GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblO3, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblO3Value, GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblW, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panelOutputs.setVerticalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblO1)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblO2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblO1Value)
								.addComponent(lblO3, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblO3Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
									.addComponent(lblW, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
									.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblO2Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))))
					.addGap(107))
		);
		panelOutputs.setLayout(gl_panelOutputs);

		JPanel panelLicense = new JPanel();
		panelLicense.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_panel_control = new GroupLayout(panel_control);
		gl_panel_control.setHorizontalGroup(
			gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_control.createParallelGroup(Alignment.LEADING)
						.addComponent(panelInputs, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
						.addComponent(panel_6, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
						.addComponent(panelLicense, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
						.addComponent(panelOutputs, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_control.setVerticalGroup(
			gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelInputs, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panelOutputs, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
					.addGap(13)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelLicense, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(24))
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
					.addContainerGap(69, Short.MAX_VALUE))
		);
		panel_6.setLayout(gl_panel_6);

		JLabel lblNewLabel = new JLabel("GNU GENERAL PUBLIC LICENSE");
		panelLicense.add(lblNewLabel);

		JLabel LabelI1 = new JLabel("Roz Est\u00E1tico");
		LabelI1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelI2 = new JLabel("Roz Din\u00E1mico");
		labelI2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelI3 = new JLabel("Masa");
		labelI3.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		lblMudValue = new JLabel("0.1");
		lblMudValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblMValue = new JLabel("10");
		lblMValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		lblMueValue = new JLabel("0.4");
		lblMueValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		sliderMue = new JSlider();
		sliderMue.setMinimum(15);
		sliderMue.setMaximum(80);
		sliderMue.setMinorTickSpacing(1);
		sliderMue.setValue(40);
		sliderMue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderI1Event();
			}
		});


		sliderMud = new JSlider();
		sliderMud.setMinimum(5);
		sliderMud.setMaximum(15);
		sliderMud.setMinorTickSpacing(1);
		sliderMud.setValue(10);
		sliderMud.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI2Event();
			}
		});




		sliderIM = new JSlider();
		sliderIM.setMinimum(1);
		sliderIM.setMaximum(20);
		sliderIM.setValue(10);
		sliderIM.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI3Event();
			}
		});
		
		JLabel lblI4 = new JLabel("\u00C1ngulo");
		lblI4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblZValue = new JLabel("45\u00BA");
		lblZValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		sliderZ = new JSlider();
		sliderZ.setMinimum(20);
		sliderZ.setMaximum(60);
		sliderZ.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sliderI4Event();
			}
		});
		sliderZ.setValue(45);
		sliderZ.setMinorTickSpacing(1);
		
		btnCilindro = new JButton("New button");
		btnCilindro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = CILINDRO;
				btnCilindro.setEnabled(false);
				btnEsfera.setEnabled(true);
				btnCubo.setEnabled(true);
			}
		});
		
		btnEsfera = new JButton("New button");
		btnEsfera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				type = ESFERA;
				btnCilindro.setEnabled(true);
				btnEsfera.setEnabled(false);
				btnCubo.setEnabled(true);
			}
		});
		
		btnCubo = new JButton("New button");
		btnCubo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				type = CUBO;
				btnCilindro.setEnabled(true);
				btnEsfera.setEnabled(true);
				btnCubo.setEnabled(false);
			}
		});




		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
			gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addComponent(panelTitle, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(labelI3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(LabelI1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelI2, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
								.addComponent(lblMueValue, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMudValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING, false)
								.addComponent(sliderMue, 0, 0, Short.MAX_VALUE)
								.addComponent(sliderMud, 0, 0, Short.MAX_VALUE)
								.addComponent(sliderIM, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblI4, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblZValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sliderZ, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
					.addGap(19))
				.addGroup(Alignment.LEADING, gl_panelInputs.createSequentialGroup()
					.addGap(35)
					.addComponent(btnCilindro, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnEsfera, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnCubo, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(42, Short.MAX_VALUE))
		);
		gl_panelInputs.setVerticalGroup(
			gl_panelInputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
							.addComponent(LabelI1)
							.addComponent(lblMueValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(sliderMue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
							.addComponent(labelI2)
							.addComponent(lblMudValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(sliderMud, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(labelI3)
						.addComponent(lblMValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(sliderIM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(lblI4, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblZValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(sliderZ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCubo, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnEsfera, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnCilindro, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
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
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_visualizar, GroupLayout.PREFERRED_SIZE, 696, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(149, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_visualizar, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
						.addComponent(panel_control, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 568, Short.MAX_VALUE))
					.addContainerGap())
		);
				
				JPanel panel = new JPanel();
				
				panelGrafica = new JPanelGrafica();
				GroupLayout gl_panel = new GroupLayout(panel);
				gl_panel.setHorizontalGroup(
					gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panelGrafica, GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
				);
				gl_panel.setVerticalGroup(
					gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panelGrafica, GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
				);
				panel.setLayout(gl_panel);
				GroupLayout gl_panel_visualizar = new GroupLayout(panel_visualizar);
				gl_panel_visualizar.setHorizontalGroup(
					gl_panel_visualizar.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_visualizar.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel, GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
							.addContainerGap())
				);
				gl_panel_visualizar.setVerticalGroup(
					gl_panel_visualizar.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel_visualizar.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel, GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
							.addContainerGap())
				);
				
				panel_visualizar.setLayout(gl_panel_visualizar);

		getContentPane().setLayout(groupLayout);
	}
}