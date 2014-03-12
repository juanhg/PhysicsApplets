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

package com.juanhg.icewalker;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
import javax.swing.Icon;
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

import com.juanhg.util.ImageProcessing;
import com.raccoon.easyjchart.Grafica;
import com.raccoon.easyjchart.JPanelGrafica;
import javax.swing.JTextField;

public class IceWalkerApplet extends JApplet implements Runnable {


	private static final long serialVersionUID = -3017107307819023599L;
	private float energySize = 33;

	
	//Control variables
	double sleepTime = 50;	
	boolean end = false;
	int currentlPerson = 0;
	double countCurrentPerson = 0.0;

	//Inputs
	double F; //Strength
	double mu; //Static Friction Coefficient
	double mud; //Dynamic Friction Coefficient

	//Thread that executed the simulation
	private Thread flujo = null;

	//Model
	private IceWalkerModel model;

	//Charts
	private Grafica chart; 

	//ChartPanels
	private JPanelGrafica panelSimulation;

	int supXLimit = 400;
	int infXLimit = 0;
	int supYLimit = 350;
	int infYLimit = 0;


	//Images String Declaration
	final String person0 = "person0.png";
	final String person1 = "person1.png";
	final String person2 = "person2.png";
	final String person3 = "person3.png";
	final String person4 = "person4.png";
	final String person5 = "person5.png";

	final String box = "box.png";
	final String water = "water.png";
	final String surface = "surface.png";
	final String ground = "ground.png";
	final String pulley = "pulley.png";
	final String banana = "banana.png";
	final String burger = "burger.png";
	final String cake = "cake.png";
	final String cookie = "cookie.png";
	final String carrot = "carrot.png";
	final String base = "base.png";
	private final String background = "background.jpg";

	//Images
	BufferedImage person0Image, person1Image, person2Image, person3Image, person4Image, person5Image;
	BufferedImage rotatedPersonImage;
	BufferedImage boxImage, pulleyImage, carrotImage, baseImage;
	BufferedImage waterImage, surfaceImage, groundImage, backgroundImage, bananaImage, cakeImage, burgerImage, cookieImage;

	//Annotations
	XYAnnotation personAnnotation;
	XYAnnotation boxAnnotation;

	//Labels
	private JLabel lblVValue;  
	private JLabel lblStaticFrictionValue, lblDynamicFrictionValue, lblStregthValue;
	private JLabel lblPhase;

	//Sliders
	private JSlider sliderStrength, sliderStaticFriction, sliderDynamicFriction; 

	//Buttons
	JButton btnLaunchSimulation, btnBanana, btnBurger, btnCookie;
	JButton btnCarrot;
	private JPanel panel;
	private JLabel label;
	private JPanel panel_1;
	private JLabel lblCalg;
	private JLabel lblCalg_1;


	public IceWalkerApplet() {}

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

		if(sliderStrength.getValueIsAdjusting()){
			lblStregthValue.setText(Integer.toString(sliderStrength.getValue()));
		}
	}
	void sliderDynamicFrictionEvent(){
		double dynamicF;
		if(sliderDynamicFriction.getValueIsAdjusting()){
			dynamicF = (double) sliderDynamicFriction.getValue();
			lblDynamicFrictionValue.setText(Double.toString(dynamicF/100.0));
		}
	}

	void sliderStaticFrictionEvent(){

		if(sliderStaticFriction.getValueIsAdjusting()){
			double staticF;
			staticF = (double) sliderStaticFriction.getValue();
			lblStaticFrictionValue.setText(Double.toString(staticF/100.0));
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

			sliderStrength.setEnabled(buttonsOn);
			sliderDynamicFriction.setEnabled(buttonsOn);
			sliderStaticFriction.setEnabled(buttonsOn);

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

			sliderStrength.setEnabled(buttonsOn);
			sliderDynamicFriction.setEnabled(buttonsOn);
			sliderStaticFriction.setEnabled(buttonsOn);
			
			
			
			model.getT().start();
			flujo.start();
		}
	}

	void btnBananaEvent(){
		end = true;
		while(flujo.isAlive()) {}
		readInputs();
		model.reset();
	    flujo = new Thread();
	    flujo = new Thread(this);
	    
		btnBanana.setEnabled(false);
		btnBurger.setEnabled(false);
		btnCarrot.setEnabled(false);
		btnCookie.setEnabled(false);
		
	    model.eatBanana();
		paintEnergy();

	    flujo.start();
	}

	void btnBurgerEvent(){
		end = true;
		while(flujo.isAlive()) {}
		readInputs();
		model.reset();
	    flujo = new Thread();
	    flujo = new Thread(this);
	    
		btnBanana.setEnabled(false);
		btnBurger.setEnabled(false);
		btnCarrot.setEnabled(false);
		btnCookie.setEnabled(false);
		
	    model.eatBurger();
		paintEnergy();

	    flujo.start();
	}
	
	void btnCookieEvent(){
		end = true;
		while(flujo.isAlive()) {}
		readInputs();
		model.reset();
	    flujo = new Thread();
	    flujo = new Thread(this);
	    
		btnBanana.setEnabled(false);
		btnBurger.setEnabled(false);
		btnCarrot.setEnabled(false);
		btnCookie.setEnabled(false);
		
	    model.eatCookie();
		paintEnergy();

	    flujo.start();
	}
	
	void btnCarrotEvent(){
		end = true;
		while(flujo.isAlive()) {}
		readInputs();
		model.reset();
	    flujo = new Thread();
	    flujo = new Thread(this);
	    
		btnBanana.setEnabled(false);
		btnBurger.setEnabled(false);
		btnCarrot.setEnabled(false);
		btnCookie.setEnabled(false);
		
	    model.eatCarrot();
		paintEnergy();

	    flujo.start();
	}
	
	@Override
	public void run() {

		end = false;


		while(!end){

			model.getT().pause();
			chart.deleteImage(boxAnnotation);
			boxAnnotation = chart.setImageAtPoint(boxImage, model.getBoxPoint());
			chart.deleteImage(personAnnotation);
			rotatedPersonImage = ImageProcessing.rotateRadians(getPersonImage(), model.getPhiPerson());
			personAnnotation = chart.setImageAtPoint(rotatedPersonImage, model.getPersonPoint());

			chart.replacePlot(1, model.getRopeToPerson(), "", Color.darkGray, 3f,true);
			chart.replacePlot(2, model.getRopeToBox(), "", Color.darkGray, 3f,true);
			paintEnergy();
			panelSimulation.actualizaGrafica(chart);

			repaint();

			//Begin step of simulation

			model.simulate();

			//End Step of simulation

			String v = String.valueOf((model.getV()));
			if(v.length() > 8){
				lblVValue.setText(v.substring(0,8));
			}
			else{
				lblVValue.setText(v);
			}
	
			if(model.readyToEat()){
				btnBanana.setEnabled(true);
				btnBurger.setEnabled(true);
				btnCarrot.setEnabled(true);
				btnCookie.setEnabled(true);
			}

			try {
				Thread.sleep((long)sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(IceWalkerApplet.class.getName()).log(Level.SEVERE, null, ex);
			}

			model.getT().start();

			try {
				Thread.sleep((long)20);
			} catch (InterruptedException ex) {
				Logger.getLogger(IceWalkerApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}

	/**
	 * Read the input values from the interface and loads its 
	 * in the variable of the class 
	 */
	private void readInputs(){
		this.F = this.sliderStrength.getValue();
		this.mu = ((double)this.sliderStaticFriction.getValue())/100.0;
		this.mud = ((double)this.sliderDynamicFriction.getValue())/100.0;
	}

	//Init the elements of the simulation
	private void initSimulation(){

		Point2D [] nullArray = new Point2D[0];

		//Crear modelo
		model = new IceWalkerModel(F, mu, mud);

		chart = new Grafica(nullArray,"", "", "Coordenada X", "", false, Color.BLUE,1f,false);
		chart.setRangeAxis(this.infXLimit, this.supXLimit, this.infYLimit, this.supYLimit);

		chart.agregarGrafica(model.getRopeToPerson(), "", Color.darkGray, 3f,true);
		chart.agregarGrafica(model.getRopeToBox(), "", Color.darkGray, 3f,true);
		paintEnergy();

		//Load Images
		backgroundImage = loadImage("background.png");
		boxImage = loadImage(box);
		pulleyImage = loadImage(pulley);
		baseImage = loadImage(base);
		person0Image = loadImage(person0);
		person1Image = loadImage(person1);
		person2Image = loadImage(person2);
		person3Image = loadImage(person3);
		person4Image = loadImage(person4);

		try {
			chart.setBackGroundImage(this.backgroundImage, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Set Images  
		chart.deleteImage(boxAnnotation);
		boxAnnotation = chart.setImageAtPoint(boxImage, model.getBoxPoint());
		chart.deleteImage(personAnnotation);
		personAnnotation = chart.setImageAtPoint(getPersonImage(), model.getPersonPoint());
		chart.setImageAtPoint(baseImage, model.getBase());
		chart.setImageAtPoint(pulleyImage, model.getPulleyPoint());
		
		
		btnBanana.setEnabled(false);
		btnBurger.setEnabled(false);
		btnCarrot.setEnabled(false);
		btnCookie.setEnabled(false);

		//Actualize panels
		panelSimulation.actualizaGrafica(chart);
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

	/**
	 * Obtain the image of the person required to achieve movement sensation
	 * @return The BufferedImage corresponding 
	 */
	BufferedImage getPersonImage(){

		BufferedImage person = person1Image;

		if(model.manInMovement()){
			switch(currentlPerson){
			case 0:
				person = person0Image;
				break;
			case 1:
				person = person1Image;
				break;
			case 2:
				person = person2Image;
				break;
			case 3:
				person = person3Image;
				break;
			case 4:
				person = person4Image;
				break;
			}

			countCurrentPerson += 0.4;
			currentlPerson = (int) (countCurrentPerson%5);
		}
		else{
			person = person2Image;
		}
		return person;
	}

	public void paintEnergy(){
		if(model.remainEnergy()){
			energySize = 33;
		}
		else{
			energySize = 0;
		}
		
		Color color;
		double energy = model.getEnergyValue();
		if(energy > 0 && energy <= 200){
			color = Color.red;
		}
		else if(energy > 200 && energy <= 400){
			color = Color.orange;
		}
		else if(energy > 400 && energy <= 600){
			color = Color.yellow;
		}
		else{
			color = Color.green;
		}
		
		chart.replacePlot(3, model.getEnergy(), "", color, energySize,true);
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
		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
				gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitleOutputs, GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
				.addGroup(gl_panelOutputs.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblPhase, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(26)
						.addComponent(lblVValue, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(112, Short.MAX_VALUE))
				);
		gl_panelOutputs.setVerticalGroup(
				gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
						.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPhase)
								.addComponent(lblVValue))
								.addGap(42))
				);
		panelOutputs.setLayout(gl_panelOutputs);

		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_panel_control = new GroupLayout(panel_control);
		gl_panel_control.setHorizontalGroup(
			gl_panel_control.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_control.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_control.createSequentialGroup()
							.addGroup(gl_panel_control.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(panelOutputs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panelInputs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panel_control.createSequentialGroup()
							.addGroup(gl_panel_control.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
								.addComponent(panelTiempo, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
							.addContainerGap())))
		);
		gl_panel_control.setVerticalGroup(
			gl_panel_control.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelInputs, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelOutputs, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelTiempo, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		JLabel lblNewLabel = new JLabel("GNU GENERAL PUBLIC LICENSE");
		panel_1.add(lblNewLabel);

		btnLaunchSimulation = new JButton("Iniciar");
		btnLaunchSimulation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLaunchSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnLaunchSimulationEvent(event);
			}
		});

		panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		label = new JLabel("Datos de la Simulaci\u00F3n");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(label);
		
		btnBanana = new JButton("");
		btnBanana.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnBananaEvent();
			}
		});
		bananaImage = loadImage(banana);
		btnBanana.setIcon(new ImageIcon(bananaImage));
		
		btnBurger = new JButton("");
		btnBurger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnBurgerEvent();
			}
		});
		burgerImage = loadImage(burger);
		btnBurger.setIcon(new ImageIcon(burgerImage));
		
		btnCookie = new JButton("");
		btnCookie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCookieEvent();
			}
		});
		cookieImage = loadImage(cookie);
		btnCookie.setIcon(new ImageIcon(cookieImage));
		
		btnCarrot = new JButton("");
		carrotImage = loadImage(carrot);
		btnCarrot.setIcon(new ImageIcon(carrotImage));
		btnCarrot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnCarrotEvent();
			}
		});
		
		JLabel lblNewLabel_1 = new JLabel("30 cal/100g");
		
		JLabel lblCalg_2 = new JLabel("734 cal/100g");
		
		lblCalg = new JLabel("90 cal/100g");
		
		lblCalg_1 = new JLabel("433 cal/100g");
		
		GroupLayout gl_panelTiempo = new GroupLayout(panelTiempo);
		gl_panelTiempo.setHorizontalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
				.addGroup(gl_panelTiempo.createSequentialGroup()
					.addGap(17)
					.addGroup(gl_panelTiempo.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnLaunchSimulation, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panelTiempo.createSequentialGroup()
							.addGroup(gl_panelTiempo.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnCarrot, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panelTiempo.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnBanana)
								.addComponent(lblCalg, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelTiempo.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnCookie, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCalg_1, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panelTiempo.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelTiempo.createSequentialGroup()
									.addGap(6)
									.addComponent(btnBurger, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panelTiempo.createSequentialGroup()
									.addGap(18)
									.addComponent(lblCalg_2, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap(24, Short.MAX_VALUE))
		);
		gl_panelTiempo.setVerticalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTiempo.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelTiempo.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelTiempo.createSequentialGroup()
							.addGroup(gl_panelTiempo.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCarrot, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnCookie, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnBurger, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panelTiempo.createParallelGroup(Alignment.LEADING, false)
								.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(gl_panelTiempo.createParallelGroup(Alignment.BASELINE)
									.addComponent(lblCalg_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblCalg_1, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
								.addComponent(lblCalg, GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)))
						.addComponent(btnBanana, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addComponent(btnLaunchSimulation, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(33, Short.MAX_VALUE))
		);
		panelTiempo.setLayout(gl_panelTiempo);

		JLabel LabelStrength = new JLabel("Fuerza");
		LabelStrength.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelStaticFriction = new JLabel("Roz. Est\u00E1tico");
		labelStaticFriction.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel labelDynamicFriction = new JLabel("Roz. Din\u00E1mico");
		labelDynamicFriction.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		lblStaticFrictionValue = new JLabel("0.2");
		lblStaticFrictionValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblDynamicFrictionValue = new JLabel("0.1");
		lblDynamicFrictionValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		lblStregthValue = new JLabel("100");
		lblStregthValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		sliderStrength = new JSlider();
		sliderStrength.setMinorTickSpacing(1);
		sliderStrength.setMinimum(1);
		sliderStrength.setMaximum(300);
		sliderStrength.setValue(100);
		sliderStrength.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderStrenghtEvent();
			}
		});


		sliderStaticFriction = new JSlider();
		sliderStaticFriction.setMinimum(15);
		sliderStaticFriction.setMaximum(80);
		sliderStaticFriction.setMinorTickSpacing(1);
		sliderStaticFriction.setValue(20);
		sliderStaticFriction.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderStaticFrictionEvent();
			}
		});




		sliderDynamicFriction = new JSlider();
		sliderDynamicFriction.setValue(10);
		sliderDynamicFriction.setMaximum(15);
		sliderDynamicFriction.setMinimum(5);
		sliderDynamicFriction.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderDynamicFrictionEvent();
			}
		});
		sliderDynamicFriction.setMinorTickSpacing(1);




		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
				gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelInputs.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(labelDynamicFriction, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(LabelStrength, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelStaticFriction, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 120, Short.MAX_VALUE))
								.addGap(18)
								.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
										.addComponent(lblStregthValue, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblStaticFrictionValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDynamicFrictionValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
												.addComponent(sliderStaticFriction, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
												.addComponent(sliderStrength, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
												.addComponent(sliderDynamicFriction, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
												.addGap(26))
												.addComponent(panelTitle, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
				);
		gl_panelInputs.setVerticalGroup(
				gl_panelInputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelInputs.createSequentialGroup()
						.addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(8)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
										.addComponent(LabelStrength)
										.addComponent(lblStregthValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
										.addComponent(sliderStrength, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
														.addComponent(labelStaticFriction)
														.addComponent(lblStaticFrictionValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
														.addComponent(sliderStaticFriction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
														.addGap(11)
														.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																.addComponent(labelDynamicFriction)
																.addComponent(lblDynamicFrictionValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																.addComponent(sliderDynamicFriction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																.addGap(75))
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
		tabbedPane.addTab("Simulación", null, panelSimulation, null);
		panelSimulation.setBackground(Color.WHITE);

		getContentPane().setLayout(groupLayout);
	}
}