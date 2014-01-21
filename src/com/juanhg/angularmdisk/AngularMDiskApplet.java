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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

import com.raccoon.easyjchart.Grafica;
import com.raccoon.easyjchart.JPanelGrafica;


public class AngularMDiskApplet extends JApplet implements Runnable {
	
	private static final long serialVersionUID = -3261548917574875054L;

	
	int supXLimit = 1;
	int infXLimit = -1;
	int supYLimit = 1;
	int infYLimit = -1;
	
	double sleepTime = 50;
	int zoom = 1;
	boolean end = false;
	
	double starInitSize = 50;
	
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
    AngularMDiskModel model;
    
    /**
     * Contiene la gráfica que representa a la onda en la cuerda.
     */
    private Grafica grafica;
    private JLabel lblPeriodValue;
    private JLabel textFinalTime;
    private JLabel textFinalRadius;
    private JLabel labelActualTime; 
    JLabel lblIteracinActual;
    JLabel lblFinalMassValue, lblDistanceValue, lblVelocityValue, lblInitMassValue;
    JLabel lblActualSimulationValue;
    
    private JSlider sliderInitMass; 
    private JSlider sliderFinalMass;
    private JSlider sliderDistance;
    private JSlider sliderVelocity;
	private JSlider sliderPlotBackGround;
	private JSlider sliderSimulations;
	private JLabel lblSimulaciones;
    
   
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
		setSize(1030,565);

		
        
		
		
		JPanel panel_control = new JPanel();
		panel_control.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		
		JPanel panelInputs = new JPanel();
		panelInputs.setToolTipText("");
		panelInputs.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JPanel panelTiempo = new JPanel();
		panelTiempo.setToolTipText("");
		panelTiempo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		sliderPlotBackGround = new JSlider();
		sliderPlotBackGround.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderPlotBackgroundEvent(event);
			}
		});
		sliderPlotBackGround.setValue(10);
		sliderPlotBackGround.setMinorTickSpacing(1);
		sliderPlotBackGround.setMaximum(10);
		
		sliderSimulations = new JSlider();
		sliderSimulations.setMinimum(1);
		sliderSimulations.setMaximum(2000);
		sliderSimulations.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderSimulationsEvent();
			}
		});
		
		sliderSimulations.setValue(600);
		
		JLabel lblVisibilidadDelFondo = new JLabel("Visibilidad del Fondo");
		lblVisibilidadDelFondo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		lblSimulaciones = new JLabel("Simulaciones:  600");
		lblSimulaciones.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GroupLayout gl_panelTiempo = new GroupLayout(panelTiempo);
		gl_panelTiempo.setHorizontalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTiempo.createSequentialGroup()
					.addGroup(gl_panelTiempo.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelTiempo.createSequentialGroup()
							.addGap(10)
							.addComponent(sliderPlotBackGround, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelTiempo.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblVisibilidadDelFondo)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelTiempo.createParallelGroup(Alignment.TRAILING)
						.addComponent(sliderSimulations, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
						.addComponent(lblSimulaciones, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panelTiempo.setVerticalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelTiempo.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panelTiempo.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblVisibilidadDelFondo)
						.addComponent(lblSimulaciones, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelTiempo.createParallelGroup(Alignment.LEADING)
						.addComponent(sliderPlotBackGround, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(sliderSimulations, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
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
		
		JLabel labelTiempoFinal = new JLabel("Tiempo Final:");
		labelTiempoFinal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelOutput3 = new JLabel("T:");
		labelOutput3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelOutput2 = new JLabel("Radio Final:");
		labelOutput2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblPeriodValue = new JLabel();
		lblPeriodValue.setText("0");
		lblPeriodValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textFinalTime = new JLabel();
		textFinalTime.setText("0");
		textFinalTime.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		textFinalRadius = new JLabel();
		textFinalRadius.setText("0");
		textFinalRadius.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelTime = new JLabel("Tiempo Actual:");
		labelTime.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		labelActualTime = new JLabel("0");
		labelActualTime.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblIteracinActual = new JLabel("Simulaci\u00F3n Actual");
		lblIteracinActual.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblActualSimulationValue = new JLabel("0");
		lblActualSimulationValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GroupLayout gl_panelOutputs = new GroupLayout(panelOutputs);
		gl_panelOutputs.setHorizontalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, 404, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelOutputs.createSequentialGroup()
									.addComponent(labelTiempoFinal, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED))
								.addGroup(gl_panelOutputs.createSequentialGroup()
									.addComponent(labelTime)
									.addGap(27)))
							.addGroup(gl_panelOutputs.createSequentialGroup()
								.addComponent(labelOutput3, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
								.addGap(70)))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(labelOutput2, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panelOutputs.createSequentialGroup()
							.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING, false)
								.addComponent(textFinalRadius, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(textFinalTime, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelActualTime, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
							.addGap(18)
							.addComponent(lblActualSimulationValue, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addGap(4))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblPeriodValue, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblIteracinActual)
							.addContainerGap())))
		);
		gl_panelOutputs.setVerticalGroup(
			gl_panelOutputs.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelOutputs.createSequentialGroup()
					.addComponent(panelTitleOutputs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelTiempoFinal)
						.addComponent(textFinalTime))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelTime, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelActualTime))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelOutput3)
						.addComponent(lblPeriodValue)
						.addComponent(lblIteracinActual))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFinalRadius, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelOutput2)
						.addComponent(lblActualSimulationValue))
					.addGap(20))
		);
		panelOutputs.setLayout(gl_panelOutputs);
		GroupLayout gl_panel_control = new GroupLayout(panel_control);
		gl_panel_control.setHorizontalGroup(
			gl_panel_control.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_control.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_control.createParallelGroup(Alignment.TRAILING)
						.addComponent(panelButtons, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(panelTiempo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(panelOutputs, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 404, Short.MAX_VALUE)
						.addComponent(panelInputs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
					.addContainerGap())
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
					.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(20, Short.MAX_VALUE))
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
		
		JLabel labelFinalMass = new JLabel("% de Masa Final");
		labelFinalMass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelDistance = new JLabel("Distancia al Centro");
		labelDistance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelVelocity = new JLabel("Velocidad de P\u00E9rdida");
		labelVelocity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		lblFinalMassValue = new JLabel("0.6");
		lblFinalMassValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblDistanceValue = new JLabel("10");
		lblDistanceValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblVelocityValue = new JLabel("0.2");
		lblVelocityValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		lblInitMassValue = new JLabel("2.0");
		lblInitMassValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		sliderInitMass = new JSlider();
		sliderInitMass.setValue(20);
		sliderInitMass.setMinimum(5);
		sliderInitMass.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderInitMassEvent();
			}
		});
		sliderInitMass.setMaximum(500);
		
		sliderFinalMass = new JSlider();
		sliderFinalMass.setMinimum(60);
		sliderFinalMass.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderFinalMassEvent();
			}
		});
		sliderFinalMass.setValue(30);
		sliderFinalMass.setMinorTickSpacing(1);
		sliderFinalMass.setMaximum(95);
		
		sliderDistance = new JSlider();
		sliderDistance.setValue(100);
		sliderDistance.setMaximum(500);
		sliderDistance.setMinimum(3);
		sliderDistance.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderDistanceEvent();
			}
		});
		sliderDistance.setMinorTickSpacing(1);
		
		sliderVelocity = new JSlider();
		sliderVelocity.setMaximum(1000);
		sliderVelocity.setMinimum(1);
		sliderVelocity.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderVelocityEvent();
			}
		});
		sliderVelocity.setValue(200);
		sliderVelocity.setMinorTickSpacing(1);
	
		
		
		
		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
			gl_panelInputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitle, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(LabelInitMass, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelDistance, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelVelocity, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
						.addComponent(labelFinalMass, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblInitMassValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
							.addComponent(sliderInitMass, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panelInputs.createSequentialGroup()
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
								.addComponent(lblFinalMassValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDistanceValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING)
								.addComponent(sliderFinalMass, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
								.addComponent(sliderDistance, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblVelocityValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sliderVelocity, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panelInputs.setVerticalGroup(
			gl_panelInputs.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addComponent(panelTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGap(29)
							.addComponent(lblFinalMassValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(sliderInitMass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sliderFinalMass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblInitMassValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(LabelInitMass))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(labelFinalMass)))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(sliderDistance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblDistanceValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addComponent(labelDistance)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGap(1)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblVelocityValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(labelVelocity)))
						.addComponent(sliderVelocity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28))
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
					.addComponent(panel_visualizar, GroupLayout.PREFERRED_SIZE, 569, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(13, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_control, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 543, Short.MAX_VALUE)
						.addComponent(panel_visualizar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE))
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
		
		
		//Obtain values from interface
		double initMass = this.sliderInitMass.getValue()/10.0;
		double finalMass = this.sliderFinalMass.getValue()/100.0;
		double velocity = this.sliderVelocity.getValue()/1000.0;
		double distance = this.sliderDistance.getValue()/10.0;

		int simulations = this.sliderSimulations.getValue();

		//Crear modelo
		model = new AngularMDiskModel(initMass, finalMass, velocity, distance, simulations);

		grafica = new Grafica(model.getPlanetAsArray(),"Conservaci�n del Momento Angular", "Planeta", "Coordenada X", "Coordenada Y", false, Color.BLUE,1f,false);
		grafica.setRangeAxis(-100, 100, -100, 100);
		
		String workingDir = System.getProperty("user.dir");
		System.out.println(workingDir);
		
		BufferedImage image = loadImage("vinilo.png");
		grafica.setImage(image, 0, 0, Math.PI);
		BufferedImage image2 = loadImage("Bug.png");
		grafica.setImage(image2, 0, 0, 270); 
		
//		Thread musicThread  = new AePlayWave("./com/juanhg/diskangularmomentum/win.wav");
//		musicThread.start();
	
			    
		panelSimulacion.actualizaGrafica(grafica);
		
	}
	
	
	void btnLaunchSimulationEvent(ActionEvent event){
		
		 if(flujo != null && flujo.isAlive()) {
	            end = true;
	            while(flujo.isAlive()) {}
	        }
		
		//Obtain values from interface
		double initMass = this.sliderInitMass.getValue()/10.0;
		double finalMass = this.sliderFinalMass.getValue()/100.0;
		double velocity = this.sliderVelocity.getValue()/1000.0;
		double distance = this.sliderDistance.getValue()/10.0;
		
		int simulations = this.sliderSimulations.getValue();
		
       //Crear modelo
       model = new AngularMDiskModel(initMass, finalMass, velocity, distance, simulations);
       
       zoom = this.getZoom(model.getPlanet(), supXLimit, infXLimit, supYLimit, infYLimit);
       grafica.setRangeAxis(infXLimit*zoom, supXLimit*zoom, infYLimit*zoom, supYLimit*zoom);
       
       //Initializes and runs the thread (Run())
       flujo = new Thread();
       flujo = new Thread(this);
       flujo.start();
	}
	
	void btnPauseContinueEvent(ActionEvent event){
		   if(flujo != null && flujo.isAlive()) {
	            end = true;
	            while(flujo.isAlive()) {}
	        }
	        else {

	            end = false;

	            flujo = new Thread(this);
	            flujo.start();
	        }
	}
	
	void sliderPlotBackgroundEvent(ChangeEvent event){
		
		if(sliderPlotBackGround.getValueIsAdjusting()){
			float alpha = ((float)sliderPlotBackGround.getValue())/10f;
			grafica.getPlot().setBackgroundImageAlpha(alpha);
			panelSimulacion.actualizaGrafica(grafica);
			repaint();
		}
	}
	
	void sliderSimulationsEvent(){
		
		if(sliderSimulations.getValueIsAdjusting()){
			lblSimulaciones.setText("Simulaciones: " + sliderSimulations.getValue());
			repaint();
		}
	}
	void sliderInitMassEvent(){

		if(sliderInitMass.getValueIsAdjusting()){
			lblInitMassValue.setText(Double.toString((double)sliderInitMass.getValue()/10.0));
			sliderVelocity.setMinimum((int)(0.01*((double)sliderInitMass.getValue()/10.0)*1000));
			sliderVelocity.setMaximum((int)(0.3*((double)sliderInitMass.getValue()/10.0)*1000));
			lblVelocityValue.setText(Double.toString((double)sliderVelocity.getValue()/1000.0));
			repaint();
		}
	}
	void sliderVelocityEvent(){

		if(sliderVelocity.getValueIsAdjusting()){
			lblVelocityValue.setText(Double.toString(((double)sliderVelocity.getValue()/1000.0)));
			repaint();
		}
	}
	void sliderDistanceEvent(){

		if(sliderDistance.getValueIsAdjusting()){
			lblDistanceValue.setText(Double.toString((double)sliderDistance.getValue()/10.0));
			repaint();
		}
	}
	void sliderFinalMassEvent(){

		if(sliderFinalMass.getValueIsAdjusting()){
			lblFinalMassValue.setText(Double.toString((double)sliderFinalMass.getValue()/100.0));
			repaint();
		}
}
	
	void sliderChangeEvent(JSlider slider, JLabel label){
		if(slider.getValueIsAdjusting()){
			label.setText(Double.toString(((double)slider.getValue())/10.0));
			repaint();
		}
	}
	
	/**
	 * Calculate the multiplicative zoom that must be applied to the minimum range of the
	 * plot, to achieve that the point will be drawn inside the plot.
	 * @param point Point that must be drawn inside the chart
	 * @param supXLimit Superior X Limit of the plot
	 * @param infXLimit Inferior X Limit of the plot
	 * @param supYLimit Superior Y Limit of the plot
	 * @param infYLimit Inferior Y Limit of the plot
	 * @return Multiplicative factor that must be applied to the limits of the plot
	 */
	int getZoom(Point2D point, int supXLimit, int infXLimit, int supYLimit, int infYLimit){
		int tempZoom = 1;
		
		while(point.getX() >= supXLimit*tempZoom
		      || point.getY() >= supYLimit*tempZoom
		      || point.getX() <= infXLimit*tempZoom
		      || point.getY() <= infYLimit*tempZoom){
			tempZoom = tempZoom * 2;
		}
		
		return tempZoom;
	}
	
	int obtainExponent(double number){
		int exponent = 0;
		
		while(0 == (int)number){
			exponent--;
			number *= 10;
		}
		return exponent;
		
	}
	
    
	@Override
	public void run() {

		double dColor = ((double)model.getFinalTime()/256.0);
		int colorGreen = 255;
		end = false;
		double dSimulation = 70.0/model.getTotalSimulations();
		int exp = 0;
		
		while(!end){
			
  		if(!model.finalTimeReached()){
			colorGreen = (255 - ((int) (model.getActualTime()/dColor)));
			}
			else{
				colorGreen = 0;
			}
			
			//System.out.println(colorGreen);
			
	
			if(!model.finalTimeReached()){
				this.sleepTime += dSimulation;
			}
		
			
			grafica.replacePlot(0,model.getPlanetAsArray(),"Planeta", Color.BLUE,1f,false);
			grafica.replacePlot(1,model.getTrajectoryAsArray(), "Trayectoria", Color.RED, 1f,false);
			grafica.replacePlot(2,model.getStarAsArray(), "Star3", new Color(255,colorGreen,0),1f,false);
			
			grafica.visualizaMuestras(0,true,5);
			grafica.visualizaMuestras(1,true,1);
			grafica.visualizaMuestras(2,true,((starInitSize+(50*model.getActualDistance()))/zoom));
			
			int zoomLastPoint = this.getZoom(model.getPlanet(), supXLimit, infXLimit, supYLimit, infYLimit);
			if(zoomLastPoint > zoom){
				zoom = zoomLastPoint;
			}
			
			grafica.setRangeAxis(infXLimit*zoom, supXLimit*zoom, infYLimit*zoom, supYLimit*zoom);


			panelSimulacion.actualizaGrafica(grafica);

			model.simulate();
			
			String auxTime;
			
			auxTime = String.valueOf((model.getFinalTime()*Math.pow(10.0, model.getVelocityModifier())));
			exp = obtainExponent((model.getFinalTime()*Math.pow(10.0, model.getVelocityModifier())));
			if(auxTime.length() > 5){
				textFinalTime.setText(auxTime.substring(0,5) + " E" + exp);
			}
			else{
				textFinalTime.setText(auxTime + " E" + exp);
			}
			
			textFinalRadius.setText(String.valueOf(model.getFinalDistance()));
			auxTime = String.valueOf((model.getActualTime()*Math.pow(10.0, model.getVelocityModifier())));
		    exp = obtainExponent((model.getActualTime()*Math.pow(10.0, model.getVelocityModifier())));
			if(auxTime.length() > 5){
				labelActualTime.setText(auxTime.substring(0,5)+ " E" + exp);
			}
			else{
				labelActualTime.setText(auxTime+ " E" + exp);
			}
			
			
			auxTime = String.valueOf((model.getActualPeriod()));
			exp = this.obtainExponent(model.getActualPeriod());
			if(auxTime.length() > 5){
				lblPeriodValue.setText(auxTime.substring(0,5)+ " E" + exp);
			}
			else{
				lblPeriodValue.setText(auxTime+ " E" + exp);
			}
			
			
			//lblPeriodValue.setText(String.valueOf(model.getActualPeriod()));
			
			lblActualSimulationValue.setText(String.valueOf(model.getActualSimulation()));
			
			
			repaint();

			try {
				Thread.sleep((long)sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(AngularMDiskApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
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
}
