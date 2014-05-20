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

package com.juanhg.pot;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
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
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;

import com.raccoon.easyjchart.Grafica;
import com.raccoon.easyjchart.JPanelGrafica;

public class PotApplet extends JApplet implements Runnable {


	private static final long serialVersionUID = -3017107307819023599L;
	private final String example = "example.png";
	private final String fire1 = "fire1.png";
	private final String fire2 = "fire2.png";
	private final String display = "display.png";
	private final String rScrew = "rScrew.png";
	private final String lScrew = "lScrew.png";
	int currentSimulation = 0;

	final int pistonY0 = 3;
	final int pistonY1 = 7;

	//Control variables
	long sleepTime = 200;	
	boolean end = false;

	//Inputs
	double T, V, mo, m, mc;
	int type;

	//Thread that executed the simulation
	private Thread flujo = null;

	//Model
	private PotModel model;

	//Charts
	private Grafica chartPot, chartChart;

	//Panels
	private JPanelGrafica panelPot, panelGraficas;

	int supXLimit = 10;
	int infXLimit = 0;
	int supYLimit = 70;
	int infYLimit = -20;

	int potInfYLimit = 0;
	int potSupYLimit = 50;
	int potInfXLimit = infXLimit + 2;
	int potSupXLimit = supXLimit -2;

	int xFire = (supXLimit - infXLimit)/2;
	int yFire = -8;
	double yDisplay = 9.5;

	int chartsSupXLimit = 12;
	int chartsInfXLimit = 0;
	int chartsSupYLimit = 11;
	int chartsInfYLimit = 0;

	int lScrewX = potInfXLimit;
	int rScrewX = potSupXLimit;
	//Images
	BufferedImage exampleImage, fire1Image, fire2Image, displayImage, rScrewImage, lScrewImage;

	//Annotations
	XYAnnotation exampleAnnotation = null;
	XYAnnotation fireAnnotation = null;
	XYAnnotation WAnnotation = null;
	XYAnnotation QAnnotation = null;
	XYAnnotation TAnnotation = null;
	XYAnnotation UAnnotation = null;
	XYAnnotation PAnnotation = null;
	XYAnnotation VAnnotation = null;
	XYAnnotation topAnnotation = null;
	XYAnnotation contentAnnotation = null;
	XYAnnotation rScrewAnnotation = null;
	XYAnnotation lScrewAnnotation = null;
	private JLabel lblVValue, lblMoValue, lblTValue, lblWValue, lblMValue, lblTypeValue, lblMcValue;
	private JLabel lblQValue, lblQ, lblT, lblV, lblM, lblMo, lblMc, lblType;

	//Sliders
	private JSlider sliderT, sliderV, sliderMo, sliderM, sliderType, sliderMc; 

	//Buttons
	JButton btnLaunchSimulation, btn1, btn2, btn3;
	private JLabel lblT2;
	private JLabel lblTOValue;
	private JLabel lblU;
	private JLabel lblP;
	private JLabel lblVO;
	private JLabel lblVOValue;
	private JLabel lblPValue;
	private JLabel lblUValue;


	public PotApplet() {}

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

		setSize(1140,590);

		this.autogeneratedCode();
		btn1function();

		//Obtain values from interface
		this.readInputs();
		this.initSimulation();

	}



	private void sliderI1Event(){
		if(sliderT.getValueIsAdjusting()){
			lblTValue.setText(Integer.toString(sliderT.getValue()));
		}
	}

	private void sliderI2Event(){
		if(sliderV.getValueIsAdjusting()){
			int staticF;
			staticF = (int) sliderV.getValue();
			lblVValue.setText("" + staticF); 
		}
	}

	private void sliderI3Event(){
		int dynamicF;
		if(sliderMo.getValueIsAdjusting()){
			dynamicF = (int) sliderMo.getValue();
			lblMoValue.setText("" + dynamicF);
		}
	}

	private void sliderI4Event(){
		int dynamicF;
		if(sliderM.getValueIsAdjusting()){
			dynamicF = (int) sliderM.getValue();
			lblMValue.setText("" + dynamicF);
		}
	}

	private void sliderI5Event(){
		int dynamicF;
		if(sliderType.getValueIsAdjusting()){
			dynamicF = (int) sliderType.getValue();
			switch(dynamicF){
			case 1:
				lblTypeValue.setText("Madera");
				break;
			case 2:
				lblTypeValue.setText("Carbón");
				break;
			case 3:
				lblTypeValue.setText("Gasolina");
				break;
			case 4:
				lblTypeValue.setText("Gas");
				break;
			}
		}
	}

	private void sliderI6Event(){
		int dynamicF;
		if(sliderMc.getValueIsAdjusting()){
			dynamicF = (int) sliderMc.getValue();
			lblMcValue.setText("" + dynamicF);
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

			sliderT.setEnabled(buttonsOn);
			sliderMo.setEnabled(buttonsOn);
			sliderM.setEnabled(buttonsOn);
			sliderType.setEnabled(buttonsOn);
			sliderV.setEnabled(buttonsOn);
			sliderMc.setEnabled(buttonsOn);

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

			sliderT.setEnabled(buttonsOn);
			sliderMo.setEnabled(buttonsOn);
			sliderM.setEnabled(buttonsOn);
			sliderType.setEnabled(buttonsOn);
			sliderV.setEnabled(buttonsOn);
			sliderMc.setEnabled(buttonsOn);

			flujo.start();
		}
	}

	@Override
	public void run() {
		int precision = 7;
		end = false;


		while(!end){

			//Begin step of simulation
			model.simulate();
			++currentSimulation;
			//End Step of simulation

			if(model.getCurrentPhase() == 1 && model.finalTimeReached()){
				lblPValue.setText(dToString(model.getPf(), precision));
				lblTOValue.setText(dToString(model.getTf(), precision));
			}
			else if(model.getCurrentPhase() == 1 && model.isPaused()){
				lblPValue.setText(dToString(model.getPo(), precision));
				lblTOValue.setText(dToString(model.getTo(), precision));
			}
			else if(model.getCurrentPhase() == 1 && !model.isPaused()){
				lblPValue.setText("...");
				lblTOValue.setText("...");
			}
			else{
				lblPValue.setText(dToString(model.getP(), precision));
				lblTOValue.setText(dToString(model.getT(), precision));
			}

			lblWValue.setText(dToString(model.getW(), precision));
			lblVOValue.setText(dToString(model.getV(), precision));

			lblQValue.setText(dToString(model.getQ(), precision));
			lblUValue.setText(dToString(model.getU(), precision));

			updatePot();
			updateOutputs();
			updateFire();

			this.updatePanels();
			repaint();

			if(model.getEnd()){
				end = true;
			}

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(PotApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		btnLaunchSimulation.setText("Iniciar");
	}

	/**
	 * Read the input values from the interface and loads its 
	 * in the variable of the class 
	 */
	private void readInputs(){
		T = sliderT.getValue();
		V = sliderV.getValue();
		type = sliderType.getValue();
		mo = sliderMo.getValue();
		m = sliderM.getValue();
		mc = sliderMc.getValue();
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

	//Init the elements of the simulation
	private void initSimulation(){
		int phase = 1;
		Point2D [] nullArray = new Point2D[0];

		if(!btn1.isEnabled()){
			phase = 1;
		}
		if(!btn2.isEnabled()){
			phase = 2;
		}
		if(!btn3.isEnabled()){
			phase = 3;
		}

		//Crear modelo
		model = new PotModel(T, V, mo, m, type, mc,phase);

		// Inicializar charts
		chartPot = new Grafica(nullArray,"", "", "", "", false, Color.BLUE,1f,false);
		chartPot.setRangeAxis(infXLimit, supXLimit, infYLimit, supYLimit);

		ValueAxis yAxis = new SymbolAxis("", new String[]{"","W","","Q","","T","","U","","P","","V", ""});
		Font font = new Font("", Font.PLAIN, 30);
		chartChart = new Grafica(nullArray,"", "", "", "", false, Color.BLUE,1f,false);
		chartChart.setRangeAxis(chartsInfXLimit, chartsSupXLimit, chartsInfYLimit, chartsSupYLimit);
		chartChart.setDomainAxis(yAxis, font);


		//Load Images
		exampleImage = loadImage(example);
		fire1Image = loadImage(fire1);
		fire2Image = loadImage(fire2);
		displayImage = loadImage(display);
		rScrewImage = loadImage(rScrew);
		lScrewImage = loadImage(lScrew);

		//Set Images  
		updateFire();
		initPlot();
		updateScrews();
		updatePot();

		//		initDisplay();
		updateOutputs();

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
		panelPot.actualizaGrafica(chartPot);
		panelGraficas.actualizaGrafica(chartChart);
	}

	private void updateScrews(){
		chartPot.deleteAnnotation(rScrewAnnotation);
		chartPot.deleteAnnotation(lScrewAnnotation);

		if(model.getCurrentPhase() == 2){
			rScrewAnnotation = chartPot.setImageAtPoint(rScrewImage, rScrewX, model.getH());
			lScrewAnnotation = chartPot.setImageAtPoint(lScrewImage, lScrewX, model.getH());
		}
	}
	
	void initPlot(){
		Stroke stroke = new BasicStroke(2f);

		chartPot.drawBox(potInfXLimit, potInfYLimit, potSupXLimit, potInfYLimit+1, stroke, Color.BLACK, Color.GRAY);
		chartPot.drawBox(potInfXLimit, potInfYLimit, potInfXLimit+0.18, potSupYLimit, stroke, Color.BLACK, Color.GRAY);
		chartPot.drawBox(potSupXLimit-0.18, potInfYLimit, potSupXLimit, potSupYLimit, stroke, Color.BLACK, Color.GRAY);
		chartPot.drawBox(potInfXLimit-1, potInfYLimit-1, potSupXLimit+1, potInfYLimit, stroke, Color.BLACK, Color.BLACK);
		chartPot.drawBox(potInfXLimit-1, infYLimit+2, potInfXLimit-1+0.22, potInfYLimit, stroke, Color.BLACK, Color.BLACK);
		chartPot.drawBox(potSupXLimit+0.78, infYLimit+2, potSupXLimit+1, potInfYLimit, stroke, Color.BLACK, Color.BLACK);

	}

	void updateFire(){
		chartPot.deleteAnnotation(fireAnnotation);
		if(model.getCurrentPhase() != 1){
			fireAnnotation = chartPot.setImageAtPoint(getFireImage(), xFire, yFire);
		}
	}


	void updatePot(){
		Stroke stroke = new BasicStroke(2f);

		chartPot.deleteAnnotation(topAnnotation);
		topAnnotation = chartPot.drawBox(potInfXLimit+0.18, model.getH(), potSupXLimit-0.18, model.getH()+2, stroke, Color.BLACK, Color.WHITE);
		chartPot.deleteAnnotation(contentAnnotation);
		contentAnnotation = chartPot.drawBox(potInfXLimit+0.18, potInfYLimit, potSupXLimit-0.18, model.getH(), stroke, Color.BLACK, Color.BLUE);		
	}

	void initDisplay(){
		chartChart.setImageAtPoint(displayImage, 1.5, yDisplay);
	}

	void updateOutputs(){

		double Wo = model.getWo();
		double Wf = model.getWf();
		double Qo = model.getQo();
		double Qf = model.getQf();
		double To = model.getTo();
		double Tf = model.getTf();
		double Uo = model.getUo();
		double Uf = model.getUf();
		double Po = model.getPo();
		double Pf = model.getPf();
		double Vo = model.getVo();
		double Vf = model.getVf();
		double aux;

		if(Wo > Wf){
			aux = Wo;
			Wo = Wf;
			Wf = aux;
		}
		if(Qo > Qf){
			aux = Qo;
			Qo = Qf;
			Qf = aux;
		}
		if(To > Tf){
			aux = To;
			To = Tf;
			Tf = aux;
		}
		if(Uo > Uf){
			aux = Uo;
			Uo = Uf;
			Uf = aux;
		}
		if(Po > Pf){
			aux = Po;
			Po = Pf;
			Pf = aux;
		}
		if(Vo > Vf){
			aux = Vo;
			Vo = Vf;
			Vf = aux;
		}

		double Wn = normalize(model.getW(),Wo,Wf,0,10);
		double Qn = normalize(model.getQ(),Qo,Qf,0,10);
		double Tn = normalize(model.getT(),To,Tf,0,10);
		double Un = normalize(model.getU(),Uo,Uf,0,10);
		double Pn = normalize(model.getP(),Po,Pf,0,10);
		double Vn = normalize(model.getV(),Vo,Vf,0,10);

		Stroke stroke = new BasicStroke(1f);
		chartChart.deleteAnnotation(WAnnotation);
		WAnnotation = chartChart.drawBox(0.5, 0, 1.5, Wn ,stroke, Color.BLACK, Color.RED);
		chartChart.deleteAnnotation(QAnnotation);
		QAnnotation = chartChart.drawBox(2.5, 0, 3.5, Qn ,stroke, Color.BLACK, Color.ORANGE);
		chartChart.deleteAnnotation(TAnnotation);
		TAnnotation = chartChart.drawBox(4.5, 0, 5.5, Tn,stroke, Color.BLACK, Color.YELLOW);
		chartChart.deleteAnnotation(UAnnotation);
		UAnnotation = chartChart.drawBox(6.5, 0, 7.5, Un,stroke, Color.BLACK, Color.GREEN);
		chartChart.deleteAnnotation(PAnnotation);
		PAnnotation = chartChart.drawBox(8.5, 0, 9.5, Pn,stroke, Color.BLACK, Color.BLUE);
		chartChart.deleteAnnotation(VAnnotation);
		VAnnotation = chartChart.drawBox(10.5, 0, 11.5, Vn ,stroke, Color.BLACK, Color.MAGENTA);
	}

	BufferedImage getFireImage(){
		if((currentSimulation)%2 == 0){
			return fire1Image;
		}
		else{
			return fire2Image;
		}
	}

	public void btn1function(){
		if(model != null){
			model.setCurrentPhase(1);
			updateFire();
			updateScrews();
			updatePot();
		}

		btn1.setEnabled(false);
		btn2.setEnabled(true);
		btn3.setEnabled(true);

		lblT.setEnabled(true);
		lblTValue.setEnabled(true);
		sliderT.setEnabled(true);

		lblV.setEnabled(false);
		lblVValue.setEnabled(false);
		sliderV.setEnabled(false);

		lblMo.setEnabled(true);
		lblMoValue.setEnabled(true);
		sliderMo.setEnabled(true);

		lblM.setEnabled(true);
		lblMValue.setEnabled(true);
		sliderM.setEnabled(true);

		lblType.setEnabled(false);
		lblTypeValue.setEnabled(false);
		sliderType.setEnabled(false);

		lblMc.setEnabled(false);
		lblMcValue.setEnabled(false);
		sliderMc.setEnabled(false);
		
		updatePanels();
		repaint();
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

		JLabel lblW = new JLabel("W:");
		lblW.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblWValue = new JLabel();
		lblWValue.setText("-");
		lblWValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblQ = new JLabel("Q:");
		lblQ.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblQValue = new JLabel();
		lblQValue.setText("-");
		lblQValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblT2 = new JLabel("T:");
		lblT2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblTOValue = new JLabel();
		lblTOValue.setText("-");
		lblTOValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblU = new JLabel("U:");
		lblU.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblP = new JLabel("P:");
		lblP.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblVO = new JLabel("V:");
		lblVO.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblVOValue = new JLabel();
		lblVOValue.setText("-");
		lblVOValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblPValue = new JLabel();
		lblPValue.setText("-");
		lblPValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblUValue = new JLabel();
		lblUValue.setText("-");
		lblUValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
				gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitleOutputs, GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
				.addGroup(gl_panelOutputs.createSequentialGroup()
						.addGap(31)
						.addGroup(gl_panelOutputs.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panelOutputs.createSequentialGroup()
										.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING, false)
												.addComponent(lblQ, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(lblW, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
														.addComponent(lblWValue, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
														.addComponent(lblQValue, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)))
														.addGroup(Alignment.LEADING, gl_panelOutputs.createSequentialGroup()
																.addComponent(lblT2, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.RELATED)
																.addComponent(lblTOValue, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
																.addGap(18)
																.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
																		.addGroup(gl_panelOutputs.createSequentialGroup()
																				.addComponent(lblU, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
																				.addGap(6)
																				.addComponent(lblUValue, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
																				.addGroup(gl_panelOutputs.createSequentialGroup()
																						.addComponent(lblP, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
																						.addGap(6)
																						.addComponent(lblPValue, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
																						.addGroup(gl_panelOutputs.createSequentialGroup()
																								.addComponent(lblVO, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
																								.addGap(6)
																								.addComponent(lblVOValue, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
																								.addGap(25))
				);
		gl_panelOutputs.setVerticalGroup(
				gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
						.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelOutputs.createSequentialGroup()
										.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
												.addComponent(lblU, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblUValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
												.addGap(6)
												.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
														.addComponent(lblP, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblPValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
														.addGap(6)
														.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
																.addComponent(lblVO, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																.addComponent(lblVOValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)))
																.addGroup(gl_panelOutputs.createSequentialGroup()
																		.addGroup(gl_panelOutputs.createParallelGroup(Alignment.TRAILING)
																				.addGroup(gl_panelOutputs.createSequentialGroup()
																						.addComponent(lblW, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																						.addPreferredGap(ComponentPlacement.RELATED)
																						.addComponent(lblQ, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
																						.addGroup(gl_panelOutputs.createSequentialGroup()
																								.addComponent(lblWValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																								.addPreferredGap(ComponentPlacement.RELATED)
																								.addComponent(lblQValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)))
																								.addPreferredGap(ComponentPlacement.RELATED)
																								.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
																										.addComponent(lblT2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																										.addComponent(lblTOValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))))
																										.addContainerGap())
				);
		panelOutputs.setLayout(gl_panelOutputs);

		JPanel panelLicense = new JPanel();
		panelLicense.setBorder(new LineBorder(new Color(0, 0, 0)));

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_panel_control = new GroupLayout(panel_control);
		gl_panel_control.setHorizontalGroup(
				gl_panel_control.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_control.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel_control.createParallelGroup(Alignment.TRAILING)
								.addComponent(panelInputs, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
								.addComponent(panelOutputs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 346, Short.MAX_VALUE)
								.addComponent(panelLicense, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
								.addContainerGap())
				);
		gl_panel_control.setVerticalGroup(
				gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_control.createSequentialGroup()
						.addContainerGap()
						.addComponent(panelInputs, GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelOutputs, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
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
				.addGroup(Alignment.TRAILING, gl_panel_6.createSequentialGroup()
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

		lblT = new JLabel("Temperatura");
		lblT.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblV = new JLabel("Volumen");
		lblV.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblMo = new JLabel("Masa Inicial");
		lblMo.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		lblVValue = new JLabel("15");
		lblVValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblMoValue = new JLabel("5");
		lblMoValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		lblTValue = new JLabel("300");
		lblTValue.setFont(new Font("Tahoma", Font.PLAIN, 14));


		sliderT = new JSlider();
		sliderT.setMinimum(280);
		sliderT.setMaximum(330);
		sliderT.setMinorTickSpacing(1);
		sliderT.setValue(300);
		sliderT.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderI1Event();
			}
		});


		sliderV = new JSlider();
		sliderV.setMinimum(3);
		sliderV.setMaximum(30);
		sliderV.setMinorTickSpacing(1);
		sliderV.setValue(15);
		sliderV.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI2Event();
			}
		});




		sliderMo = new JSlider();
		sliderMo.setMinimum(1);
		sliderMo.setMaximum(10);
		sliderMo.setValue(5);
		sliderMo.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI3Event();
			}
		});

		lblM = new JLabel("Masa Final");
		lblM.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblMValue = new JLabel("4");
		lblMValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		sliderM = new JSlider();
		sliderM.setMinimum(1);
		sliderM.setMaximum(20);
		sliderM.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sliderI4Event();
			}
		});
		sliderM.setValue(4);
		sliderM.setMinorTickSpacing(1);

		lblType = new JLabel("Combustible");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblTypeValue = new JLabel("Madera");
		lblTypeValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		sliderType = new JSlider();
		sliderType.setMinimum(1);
		sliderType.setMaximum(4);
		sliderType.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderI5Event();
			}
		});
		sliderType.setValue(1);
		sliderType.setMinorTickSpacing(1);

		lblMc = new JLabel("Masa Combustible");
		lblMc.setFont(new Font("Tahoma", Font.PLAIN, 14));

		lblMcValue = new JLabel("10");
		lblMcValue.setFont(new Font("Tahoma", Font.PLAIN, 14));

		sliderMc = new JSlider();
		sliderMc.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sliderI6Event();
			}
		});
		sliderMc.setMinimum(1);
		sliderMc.setValue(10);
		sliderMc.setMinorTickSpacing(1);
		sliderMc.setMaximum(50);

		btn1 = new JButton("1");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btn1function();
			}
		});

		btn2 = new JButton("2");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				model.setCurrentPhase(2);
				updateFire();
				updateScrews();
				updatePot();

				btn1.setEnabled(true);
				btn2.setEnabled(false);
				btn3.setEnabled(true);

				lblT.setEnabled(true);
				lblTValue.setEnabled(true);
				sliderT.setEnabled(true);

				lblV.setEnabled(true);
				lblVValue.setEnabled(true);
				sliderV.setEnabled(true);

				lblMo.setEnabled(false);
				lblMoValue.setEnabled(false);
				sliderMo.setEnabled(false);

				lblM.setEnabled(false);
				lblMValue.setEnabled(false);
				sliderM.setEnabled(false);

				lblType.setEnabled(true);
				lblTypeValue.setEnabled(true);
				sliderType.setEnabled(true);

				lblMc.setEnabled(true);
				lblMcValue.setEnabled(true);
				sliderMc.setEnabled(true);
				
				updatePanels();
				repaint();
			}
		});

		btn3 = new JButton("3");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				model.setCurrentPhase(3);
				updateFire();
				updateScrews();
				updatePot();

				btn1.setEnabled(true);
				btn2.setEnabled(true);
				btn3.setEnabled(false);

				lblT.setEnabled(true);
				lblTValue.setEnabled(true);
				sliderT.setEnabled(true);

				lblV.setEnabled(false);
				lblVValue.setEnabled(false);
				sliderV.setEnabled(false);

				lblMo.setEnabled(false);
				lblMoValue.setEnabled(false);
				sliderMo.setEnabled(false);

				lblM.setEnabled(true);
				lblMValue.setEnabled(true);
				sliderM.setEnabled(true);

				lblType.setEnabled(true);
				lblTypeValue.setEnabled(true);
				sliderType.setEnabled(true);

				lblMc.setEnabled(true);
				lblMcValue.setEnabled(true);
				sliderMc.setEnabled(true);
				
				updatePanels();
				repaint();
			}
		});




		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
				gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addComponent(panelTitle, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
				.addGroup(gl_panelInputs.createSequentialGroup()
						.addGap(25)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_panelInputs.createSequentialGroup()
										.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_panelInputs.createSequentialGroup()
														.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																.addComponent(lblV, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
																.addComponent(lblMo, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
																.addComponent(lblM, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
																.addComponent(lblType, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
																.addComponent(lblMc, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE))
																.addGap(18))
																.addComponent(lblT, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
																.addPreferredGap(ComponentPlacement.RELATED)
																.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																		.addComponent(lblTValue, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lblVValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lblMoValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lblMValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lblTypeValue, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lblMcValue, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
																		.addGap(4)
																		.addPreferredGap(ComponentPlacement.RELATED)
																		.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																				.addComponent(sliderMc, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
																				.addComponent(sliderType, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
																				.addComponent(sliderM, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
																				.addComponent(sliderMo, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
																				.addComponent(sliderV, 0, 0, Short.MAX_VALUE)
																				.addComponent(sliderT, 0, 0, Short.MAX_VALUE)))
																				.addGroup(gl_panelInputs.createSequentialGroup()
																						.addComponent(btn1, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
																						.addPreferredGap(ComponentPlacement.UNRELATED)
																						.addComponent(btn2, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
																						.addPreferredGap(ComponentPlacement.UNRELATED)
																						.addComponent(btn3, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)))
																						.addContainerGap())
				);
		gl_panelInputs.setVerticalGroup(
				gl_panelInputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelInputs.createSequentialGroup()
						.addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblT)
										.addComponent(lblTValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
										.addComponent(sliderT, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblV)
														.addComponent(lblVValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
														.addComponent(sliderV, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
														.addGap(11)
														.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																.addComponent(lblMo)
																.addComponent(lblMoValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																.addComponent(sliderMo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																		.addComponent(lblM, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																		.addComponent(lblMValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																		.addComponent(sliderM, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																		.addPreferredGap(ComponentPlacement.UNRELATED)
																		.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																				.addComponent(lblType, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																				.addComponent(sliderType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																				.addComponent(lblTypeValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
																				.addPreferredGap(ComponentPlacement.UNRELATED)
																				.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																						.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
																								.addComponent(lblMcValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
																								.addComponent(lblMc, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
																								.addComponent(sliderMc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																								.addPreferredGap(ComponentPlacement.UNRELATED)
																								.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
																										.addGroup(gl_panelInputs.createSequentialGroup()
																												.addComponent(btn1, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
																												.addGap(12))
																												.addGroup(gl_panelInputs.createSequentialGroup()
																														.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
																																.addComponent(btn2, GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
																																.addComponent(btn3, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
																																.addContainerGap())))
				);

		JLabel lblDatosDeEntrada = new JLabel("Datos de Entrada");
		lblDatosDeEntrada.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTitle.add(lblDatosDeEntrada);
		panelInputs.setLayout(gl_panelInputs);
		panel_control.setLayout(gl_panel_control);

		JPanel panel_visualizar = new JPanel();
		panel_visualizar.setBackground(UIManager.getColor("Button.background"));


		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(panel_control, GroupLayout.PREFERRED_SIZE, 382, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_visualizar, GroupLayout.PREFERRED_SIZE, 741, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(31, Short.MAX_VALUE))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
										.addContainerGap()
										.addComponent(panel_visualizar, GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
										.addGroup(groupLayout.createSequentialGroup()
												.addGap(12)
												.addComponent(panel_control, GroupLayout.PREFERRED_SIZE, 568, Short.MAX_VALUE)))
												.addContainerGap())
				);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 345, 568);
		panel.setBackground(Color.WHITE);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(351, 0, 390, 568);
		panel_1.setBackground(Color.WHITE);
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));

		JPanel panel33 = new JPanel();
		panel33.setBounds(1, 38, 388, 529);
		panel33.setBackground(Color.WHITE);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(1, 1, 388, 31);
		panel_5.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		JLabel lblGrficas = new JLabel("Gr\u00E1ficas");
		lblGrficas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_5.add(lblGrficas);
		panel_visualizar.setLayout(null);

		JPanel panel32 = new JPanel();
		panel32.setBounds(1, 38, 343, 529);
		panel32.setBackground(Color.WHITE);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(1, 1, 343, 31);
		panel_4.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

		JLabel lblSimulacion = new JLabel("Simulaci\u00F3n");
		lblSimulacion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel_4.add(lblSimulacion);
		panel_visualizar.add(panel);
		panel.setLayout(null);
		panel.add(panel_4);
		panel.add(panel32);

		panelPot = new JPanelGrafica();
		panelPot.setBackground(Color.WHITE);
		GroupLayout gl_panel32 = new GroupLayout(panel32);
		gl_panel32.setHorizontalGroup(
				gl_panel32.createParallelGroup(Alignment.LEADING)
				.addComponent(panelPot, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
				);
		gl_panel32.setVerticalGroup(
				gl_panel32.createParallelGroup(Alignment.LEADING)
				.addComponent(panelPot, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
				);
		panel32.setLayout(gl_panel32);
		panel_visualizar.add(panel_1);
		panel_1.setLayout(null);
		panel_1.add(panel33);

		panelGraficas = new JPanelGrafica();
		panelGraficas.setBackground(Color.WHITE);
		GroupLayout gl_panel33 = new GroupLayout(panel33);
		gl_panel33.setHorizontalGroup(
				gl_panel33.createParallelGroup(Alignment.LEADING)
				.addComponent(panelGraficas, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
				);
		gl_panel33.setVerticalGroup(
				gl_panel33.createParallelGroup(Alignment.LEADING)
				.addComponent(panelGraficas, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
				);
		panel33.setLayout(gl_panel33);
		panel_1.add(panel_5);

		getContentPane().setLayout(groupLayout);
	}
}