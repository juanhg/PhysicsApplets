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

package com.juanhg.pattern;
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

public class PatternApplet extends JApplet implements Runnable {


	private static final long serialVersionUID = -3017107307819023599L;
	private final String example = "example.png";

	final int pistonY0 = 3;
	final int pistonY1 = 7;
	
	//Control variables
	long sleepTime = 300;	
	boolean end = false;
	
	//Inputs
	double I1, I2, I3, I4, I5;
	
	//Thread that executed the simulation
	private Thread flujo = null;

	//Model
	private PatternModel model;
	
	//Charts
	private Grafica chartExample;
	
	//Panels
	private JPanelGrafica panelExample;

	int supXLimit = 10;
	int infXLimit = 0;
	int supYLimit = 10;
	int infYLimit = 0;

	//Images
	BufferedImage exampleImage;
	
	//Annotations
	XYAnnotation exampleAnnotation = null;

	//Labels
	private JLabel lblO1Value;  
	private JLabel lblI2Value, lblI3Value, lblI1Value, lblO2Value, lblI4Value, lblI5Value;
	private JLabel lblO1, lblO3Value, lblO3;

	//Sliders
	private JSlider sliderI1, sliderI2, sliderI3, sliderI4, sliderI5; 

	//Buttons
	JButton btnLaunchSimulation;


	public PatternApplet() {}

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



	private void sliderI1Event(){
		//350 - 450
		if(sliderI1.getValueIsAdjusting()){
			lblI1Value.setText(Integer.toString(sliderI1.getValue()));
		}
	}
	
	private void sliderI2Event(){
		//270-330
		if(sliderI2.getValueIsAdjusting()){
			double staticF;
			staticF = (double) sliderI2.getValue();
			lblI2Value.setText("" + staticF); 
		}
	}
	
	private void sliderI3Event(){
		//1-5
		double dynamicF;
		if(sliderI3.getValueIsAdjusting()){
			dynamicF = (double) sliderI3.getValue();
			lblI3Value.setText("" + dynamicF);
		}
	}
	
	private void sliderI4Event(){
		//6-10
		double dynamicF;
		if(sliderI4.getValueIsAdjusting()){
			dynamicF = (double) sliderI4.getValue();
			lblI4Value.setText("" + dynamicF);
		}
	}

	private void sliderI5Event(){
		//10-100
		double dynamicF;
		if(sliderI5.getValueIsAdjusting()){
			dynamicF = (double) sliderI5.getValue();
			lblI5Value.setText("" + dynamicF);
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

			sliderI1.setEnabled(buttonsOn);
			sliderI3.setEnabled(buttonsOn);
			sliderI4.setEnabled(buttonsOn);
			sliderI5.setEnabled(buttonsOn);
			sliderI2.setEnabled(buttonsOn);
					
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

			sliderI1.setEnabled(buttonsOn);
			sliderI3.setEnabled(buttonsOn);
			sliderI4.setEnabled(buttonsOn);
			sliderI5.setEnabled(buttonsOn);
			sliderI2.setEnabled(buttonsOn);
			model.simulate();
			
			model.getTime().start();
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

			model.getTime().pause();
			
			//Begin step of simulation
			model.simulate();
			//End Step of simulation
			
			model.getTime().start();
			
			this.updatePanels();
			repaint();
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(PatternApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	/**
	 * Read the input values from the interface and loads its 
	 * in the variable of the class 
	 */
	private void readInputs(){
		I1 = sliderI1.getValue();
		I2 = sliderI2.getValue();
		I5 = sliderI5.getValue();
		I3 = sliderI3.getValue()/(I5*1000);
		I4 = sliderI4.getValue()/(I5*1000);
	}

	//Init the elements of the simulation
	private void initSimulation(){

		Point2D [] nullArray = new Point2D[0];

		//Crear modelo
		model = new PatternModel(I1, I2, I3, I4, I5, true);
		
		// Inicializar charts
		chartExample = new Grafica(nullArray,"", "", "V", "P", false, Color.BLUE,1f,false);
		
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
		panelExample.actualizaGrafica(chartExample);
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

		lblO1 = new JLabel("O1:");
		lblO1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblO1Value = new JLabel();
		lblO1Value.setText("0");
		lblO1Value.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblO2 = new JLabel("O2:");
		lblO2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblO2Value = new JLabel();
		lblO2Value.setText("0");
		lblO2Value.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblO3 = new JLabel("O3:");
		lblO3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblO3Value = new JLabel();
		lblO3Value.setText("0");
		lblO3Value.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitleOutputs, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblO3, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addGap(26)
							.addComponent(lblO3Value, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addGroup(gl_panelOutputs.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panelOutputs.createSequentialGroup()
									.addComponent(lblO1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGap(26))
								.addGroup(gl_panelOutputs.createSequentialGroup()
									.addComponent(lblO2, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
									.addGap(29)))
							.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblO2Value, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblO1Value, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))))
					.addGap(109))
		);
		gl_panelOutputs.setVerticalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblO1Value)
						.addComponent(lblO1))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblO2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblO2Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addComponent(lblO3, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblO3Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGap(79))
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
						.addComponent(panelOutputs, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_6, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
						.addComponent(panelLicense, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
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
			gl_panel_6.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_6.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnLaunchSimulation, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_6.createSequentialGroup()
					.addGap(80)
					.addComponent(btnLaunchSimulation, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_6.setLayout(gl_panel_6);

		JLabel lblNewLabel = new JLabel("GNU GENERAL PUBLIC LICENSE");
		panelLicense.add(lblNewLabel);

		JLabel LabelI1 = new JLabel("I1");
		LabelI1.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelI2 = new JLabel("I2");
		labelI2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelI3 = new JLabel("I3");
		labelI3.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		lblI2Value = new JLabel("5");
		lblI2Value.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblI3Value = new JLabel("5");
		lblI3Value.setFont(new Font("Tahoma", Font.PLAIN, 14));


		lblI1Value = new JLabel("5");
		lblI1Value.setFont(new Font("Tahoma", Font.PLAIN, 14));


		sliderI1 = new JSlider();
		sliderI1.setMaximum(10);
		sliderI1.setMinorTickSpacing(1);
		sliderI1.setValue(5);
		sliderI1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderI1Event();
			}
		});


		sliderI2 = new JSlider();
		sliderI2.setMaximum(10);
		sliderI2.setMinorTickSpacing(1);
		sliderI2.setValue(5);
		sliderI2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI2Event();
			}
		});




		sliderI3 = new JSlider();
		sliderI3.setMaximum(10);
		sliderI3.setValue(5);
		sliderI3.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI3Event();
			}
		});
		
		JLabel lblI4 = new JLabel("I4");
		lblI4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblI4Value = new JLabel("5");
		lblI4Value.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		sliderI4 = new JSlider();
		sliderI4.setMaximum(10);
		sliderI4.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sliderI4Event();
			}
		});
		sliderI4.setValue(5);
		sliderI4.setMinorTickSpacing(1);
		
		JLabel lblI5 = new JLabel("I5");
		lblI5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblI5Value = new JLabel("5");
		lblI5Value.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		sliderI5 = new JSlider();
		sliderI5.setMaximum(10);
		sliderI5.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI5Event();
			}
		});
		sliderI5.setValue(5);
		sliderI5.setMinorTickSpacing(1);




		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
			gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(labelI3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(LabelI1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelI2, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
								.addComponent(lblI1Value, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblI2Value, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblI3Value, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING, false)
								.addComponent(sliderI1, 0, 0, Short.MAX_VALUE)
								.addComponent(sliderI2, 0, 0, Short.MAX_VALUE)
								.addComponent(sliderI3, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblI4, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblI4Value, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sliderI4, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblI5, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblI5Value, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sliderI5, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
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
							.addComponent(LabelI1)
							.addComponent(lblI1Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(sliderI1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
							.addComponent(labelI2)
							.addComponent(lblI2Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(sliderI2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(labelI3)
						.addComponent(lblI3Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(sliderI3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(lblI4, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblI4Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(sliderI4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(lblI5, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblI5Value, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(sliderI5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
				GroupLayout gl_panel_visualizar = new GroupLayout(panel_visualizar);
				gl_panel_visualizar.setHorizontalGroup(
					gl_panel_visualizar.createParallelGroup(Alignment.LEADING)
						.addGap(0, 845, Short.MAX_VALUE)
				);
				gl_panel_visualizar.setVerticalGroup(
					gl_panel_visualizar.createParallelGroup(Alignment.LEADING)
						.addGap(0, 567, Short.MAX_VALUE)
				);
				
				panel_visualizar.setLayout(gl_panel_visualizar);

		getContentPane().setLayout(groupLayout);
	}
}