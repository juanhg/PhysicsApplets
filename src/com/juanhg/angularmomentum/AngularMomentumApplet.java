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

package com.juanhg.angularmomentum;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.raccoon.easyjchart.JPanelGrafica;
import com.raccoon.easyjchart.*;

public class AngularMomentumApplet extends JApplet implements Runnable {
	
	private static final long serialVersionUID = -3261548917574875054L;
	private JTextField textInitMass;
	private JTextField textFinalMass;
	private JTextField textDistance;
	private JTextField textVelocity;
	private JSlider sliderPlotBackGround;
	
	long sleepTime = 200;
	
	JPanelGrafica panelSimulacion;
	
	/**
     * Hebra que ejecuta la simulación.
     */
    Thread flujo = null;
    /**
     * Variable que identifica la instantánea de las funciones simuladas que se mostrará en la aplicación.
     */
    int i = 0;
    int tActual = 0;
    
    ArrayList<Point2D[]> funcion;
    boolean fin = true;
    
    //Model
    AngularMomentumModel model;
    
    /**
     * Contiene la gráfica que representa a la onda en la cuerda.
     */
    private Grafica grafica;
    private JLabel textOutput3;
    private JLabel textFinalTime;
    private JLabel textFinalRadius;
    JLabel labelActualTime; 
	
	public AngularMomentumApplet() {}

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
		setSize(1200,565);
		
		model = new AngularMomentumModel();
		
		grafica = new Grafica(model.getPlanetAsArray(),"Conservaci�n del Momento Angular", "Planeta", "Coordenada X", "Coordenada Y", false, Color.BLUE,1f,false);
		grafica.agregarGrafica(model.getTrajectoryAsArray(), "Trayectoria", Color.RED, 1f,false);
		//grafica.agregarGrafica(model.getStarAsArray(), "Star", Color.RED,1f,false);
		//grafica.agregarGrafica(model.getStarAsArray(), "Star2", Color.ORANGE,1f,false);
		grafica.agregarGrafica(model.getStarAsArray(), "Star3", new Color(255,255,0),1f,false);
		grafica.fijaFondo(Color.WHITE);
        grafica.visualizaMuestras(0,true,5);
        grafica.visualizaMuestras(1,true,1);
        grafica.visualizaMuestras(2, true, 40);
        //grafica.visualizaMuestras(3, true, 25);
        //grafica.visualizaMuestras(4, true, 40);
        
        grafica.fijaRango(-5,5,0,0);
        grafica.fijaRango(-5,5,0,1);
        try {
			grafica.setBackGroundImage("D:\\EclipseWorkSpace\\Applets\\AppletsFisica\\res\\plotImg2.jpg",0.9f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
		
		JPanel panel_control = new JPanel();
		panel_control.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		
		JPanel panelInputs = new JPanel();
		panelInputs.setToolTipText("");
		panelInputs.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JPanel panelTiempo = new JPanel();
		panelTiempo.setToolTipText("");
		panelTiempo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JPanel panelTitleSimulacion = new JPanel();
		panelTitleSimulacion.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		JLabel labelInputData = new JLabel("Visibilidad del Fondo");
		labelInputData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelTitleSimulacion.add(labelInputData);
		
		sliderPlotBackGround = new JSlider();
		sliderPlotBackGround.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderPlotBackgroundEvent(event);
			}
		});
		sliderPlotBackGround.setValue(10);
		sliderPlotBackGround.setMinorTickSpacing(1);
		sliderPlotBackGround.setMaximum(10);
		GroupLayout gl_panelTiempo = new GroupLayout(panelTiempo);
		gl_panelTiempo.setHorizontalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitleSimulacion, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
				.addGroup(gl_panelTiempo.createSequentialGroup()
					.addGap(96)
					.addComponent(sliderPlotBackGround, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(104, Short.MAX_VALUE))
		);
		gl_panelTiempo.setVerticalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelTiempo.createSequentialGroup()
					.addComponent(panelTitleSimulacion, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sliderPlotBackGround, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(10))
		);
		panelTiempo.setLayout(gl_panelTiempo);
		
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
		
		JLabel labelTiempoFinal = new JLabel("Tiempo Final");
		labelTiempoFinal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelOutput3 = new JLabel("T");
		labelOutput3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelOutput2 = new JLabel("Radio Final");
		labelOutput2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textOutput3 = new JLabel();
		textOutput3.setText("0");
		textOutput3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textFinalTime = new JLabel();
		textFinalTime.setText("0");
		textFinalTime.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textFinalRadius = new JLabel();
		textFinalRadius.setText("...");
		textFinalRadius.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelTime = new JLabel("Tiempo Actual:");
		labelTime.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		labelActualTime = new JLabel("0");
		labelActualTime.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitleOutputs, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING, false)
							.addComponent(labelTiempoFinal, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
							.addComponent(labelOutput3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(labelOutput2, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
						.addComponent(labelTime))
					.addGap(18)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addComponent(labelActualTime, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING, false)
							.addComponent(textOutput3, 0, 0, Short.MAX_VALUE)
							.addComponent(textFinalTime, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE))
						.addComponent(textFinalRadius, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panelOutputs.setVerticalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelTiempoFinal)
						.addComponent(textFinalTime))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelOutput2)
						.addComponent(textFinalRadius, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelOutput3)
						.addComponent(textOutput3))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelTime, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelActualTime))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		panelOutputs.setLayout(gl_panelOutputs);
		GroupLayout gl_panel_control = new GroupLayout(panel_control);
		gl_panel_control.setHorizontalGroup(
			gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_control.createParallelGroup(Alignment.TRAILING)
						.addComponent(panelOutputs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
						.addComponent(panelButtons, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panelTiempo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
						.addComponent(panelInputs, 0, 0, Short.MAX_VALUE))
					.addGap(427))
		);
		gl_panel_control.setVerticalGroup(
			gl_panel_control.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelInputs, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panelOutputs, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panelTiempo, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panelButtons, GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
					.addContainerGap())
		);
		panelButtons.setLayout(null);
		
		JButton btnLaunchSimulation = new JButton("Lanzar Simulaci\u00F3n");
		btnLaunchSimulation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLaunchSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnLaunchSimulationEvent(event);
			}
		});
		btnLaunchSimulation.setBounds(10, 11, 191, 62);
		panelButtons.add(btnLaunchSimulation);
		
		JButton btnPauseContinue = new JButton("Pausar/Continuar");
		btnPauseContinue.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnPauseContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnPauseContinueEvent(event);
			}
		});
		btnPauseContinue.setBounds(211, 11, 190, 62);
		panelButtons.add(btnPauseContinue);
		
		JLabel LabelInitMass = new JLabel("Masa Inicial");
		LabelInitMass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textInitMass = new JTextField();
		textInitMass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textInitMass.setText("3");
		textInitMass.setColumns(10);
		
		JLabel labelFinalMass = new JLabel("Masa Final");
		labelFinalMass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textFinalMass = new JTextField();
		textFinalMass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textFinalMass.setText("0.3");
		textFinalMass.setColumns(10);
		
		JLabel labelDistance = new JLabel("Distancia al Centro");
		labelDistance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelVelocity = new JLabel("Velocidad de P\u00E9rdida");
		labelVelocity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textDistance = new JTextField();
		textDistance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textDistance.setText("10");
		textDistance.setColumns(3);
		
		textVelocity = new JTextField();
		textVelocity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textVelocity.setText("0.2");
		textVelocity.setColumns(10);
		
		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
			gl_panelInputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitle, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING, false)
						.addComponent(LabelInitMass, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
						.addComponent(labelDistance, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(labelVelocity, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(labelFinalMass, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING, false)
						.addComponent(textDistance, 0, 0, Short.MAX_VALUE)
						.addComponent(textVelocity, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
						.addComponent(textFinalMass, GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
						.addComponent(textInitMass, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(29, Short.MAX_VALUE))
		);
		gl_panelInputs.setVerticalGroup(
			gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(LabelInitMass)
						.addComponent(textInitMass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelFinalMass)
						.addComponent(textFinalMass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(9)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelDistance)
						.addComponent(textDistance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(6)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelVelocity)
						.addComponent(textVelocity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(43, Short.MAX_VALUE))
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
					.addComponent(panel_visualizar, GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_visualizar, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
						.addComponent(panel_control, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		GridBagLayout gbl_panel_visualizar = new GridBagLayout();
		gbl_panel_visualizar.columnWidths = new int[]{0, 0};
		gbl_panel_visualizar.rowHeights = new int[]{0, 0};
		gbl_panel_visualizar.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_visualizar.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_visualizar.setLayout(gbl_panel_visualizar);
		
		panelSimulacion = new JPanelGrafica();
		panelSimulacion.setBackground(Color.WHITE);
		GridBagConstraints gbc_panelSimulacion = new GridBagConstraints();
		gbc_panelSimulacion.fill = GridBagConstraints.BOTH;
		gbc_panelSimulacion.gridx = 0;
		gbc_panelSimulacion.gridy = 0;
		panel_visualizar.add(panelSimulacion, gbc_panelSimulacion);
		getContentPane().setLayout(groupLayout);
		
		panelSimulacion.actualizaGrafica(grafica);
		
	}
	
	
	void btnLaunchSimulationEvent(ActionEvent event){
		
		//Obtain values from interface
		double initMass = Double.parseDouble(this.textInitMass.getText());
		double finalMass = Double.parseDouble(this.textFinalMass.getText());
		double velocity = Double.parseDouble(this.textVelocity.getText());
		double distance = Double.parseDouble(this.textDistance.getText());

       //Crear modelo
       model = new AngularMomentumModel(initMass, finalMass, velocity, distance);
       
       //Initializes and runs the thread (Run())
       flujo = new Thread();
       flujo = new Thread(this);
       flujo.start();
	}
	
	void btnPauseContinueEvent(ActionEvent event){}
	
	void sliderPlotBackgroundEvent(ChangeEvent event){
		
		if(sliderPlotBackGround.getValueIsAdjusting()){
			float alpha = ((float)sliderPlotBackGround.getValue())/10f;
			grafica.getPlot().setBackgroundImageAlpha(alpha);
			panelSimulacion.actualizaGrafica(grafica);
			repaint();
		}
	}
	
    
	@Override
	public void run() {

		double dColor = model.getFinalTime()/255;
		int colorGreen = 0;
		
		while(true){
			
			if(!model.finalTimeReached()){
			colorGreen = 255 - ((int) (model.getActualTime()/dColor));
			}
			else{
				colorGreen = 0;
			}
			
			System.out.println(colorGreen);
			
			grafica.replacePlot(0,model.getPlanetAsArray(),"Planeta", Color.BLUE,1f,false);
			grafica.replacePlot(1,model.getTrajectoryAsArray(), "Trayectoria", Color.RED, 1f,false);

			grafica.replacePlot(2,model.getStarAsArray(), "Star3", new Color(255,colorGreen,0),1f,false);
			
			grafica.visualizaMuestras(0,true,5);
			grafica.visualizaMuestras(1,true,1);
			grafica.visualizaMuestras(2,true,40);


			panelSimulacion.actualizaGrafica(grafica);

			model.simulate();
			textFinalTime.setText(String.valueOf(model.getFinalTime()));
			textFinalRadius.setText(String.valueOf(model.getFinalDistance()));
			labelActualTime.setText(String.valueOf(model.getActualTime()));
			
			
			
			repaint();

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(AngularMomentumApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
