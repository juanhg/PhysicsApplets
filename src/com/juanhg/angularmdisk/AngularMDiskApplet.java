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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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

import com.juanhg.util.ImageProcessing;
import com.juanhg.util.PolarPoint2D;
import com.juanhg.util.Time;
import com.raccoon.easyjchart.Grafica;
import com.raccoon.easyjchart.JPanelGrafica;
import javax.swing.JTabbedPane;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.SwingConstants;


public class AngularMDiskApplet extends JApplet implements Runnable {
	
	private static final long serialVersionUID = -3261548917574875054L;

	//Time variables
	Time time = new Time();
	double sleepTime = 40;
	
	boolean end = false;
	
	double bugInitMass, fallRadius, diskVelocity, bugVelocity;
	
	//Thread that executed the simlation
    private Thread flujo = null;
      
    //Model
    private AngularMDiskModel model;
    
    //Charts
    private Grafica chart; 
    
    //ChartPanels
	private JPanelGrafica panelSimulation;
	
	int supXLimit = 23;
	int infXLimit = -23;
	int supYLimit = 23;
	int infYLimit = -23;
	
	//Annotations
	XYAnnotation diskAnnotation = null;
	XYAnnotation bugAnnotation = null; 
	XYAnnotation targetAnnotation = null;
	
	BufferedImage bugImage, diskImage,  roseImage, targetImage;
	BufferedImage rotatedBugImage, rotatedDiskImage; 
	BufferedImage backgroundImage; 

	//Labels
    private JLabel lblPeriodValue, textFinalTime, textFinalRadius, labelActualTime;  
    private JLabel lblFinalMassValue, lblDistanceValue, lblVelocityValue, lblInitMassValue;
    private JLabel lblActualSimulationValue;
    
    //Sliders
    private JSlider sliderBugInitMass, sliderFallRadius, sliderBugVelocity, sliderDiskVelocity;

	//Buttons
	JButton btnFase1, btnLaunchSimulation, btnPauseContinue;
    
   
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
		
		setSize(1040,610);
		
		JPanel panel_control = new JPanel();
		panel_control.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED, null, null), new BevelBorder(BevelBorder.RAISED, null, null, null, null)));
		
		JPanel panelInputs = new JPanel();
		panelInputs.setToolTipText("");
		panelInputs.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		JPanel panelTiempo = new JPanel();
		panelTiempo.setToolTipText("");
		panelTiempo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		btnFase1 = new JButton("Lanzar Bicho");
		btnFase1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnLadyBugEvent();
			}
		});
		
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
					.addGroup(gl_panelOutputs.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addGroup(gl_panelOutputs.createParallelGroup(Alignment.LEADING, false)
								.addComponent(textFinalRadius, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(textFinalTime, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(labelActualTime, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
							.addGap(18)
							.addComponent(lblActualSimulationValue, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addGap(4))
						.addGroup(gl_panelOutputs.createSequentialGroup()
							.addComponent(lblPeriodValue, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addGap(143))))
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
						.addComponent(lblPeriodValue))
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
						.addComponent(panelOutputs, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 404, Short.MAX_VALUE)
						.addComponent(panelInputs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(panelButtons, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
						.addComponent(panelTiempo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
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
					.addComponent(panelTiempo, GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(panelButtons, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		GroupLayout gl_panelTiempo = new GroupLayout(panelTiempo);
		gl_panelTiempo.setHorizontalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panelTiempo.createSequentialGroup()
					.addGap(26)
					.addComponent(btnFase1, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(188, Short.MAX_VALUE))
		);
		gl_panelTiempo.setVerticalGroup(
			gl_panelTiempo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTiempo.createSequentialGroup()
					.addGap(11)
					.addComponent(btnFase1, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(47, Short.MAX_VALUE))
		);
		panelTiempo.setLayout(gl_panelTiempo);
		panelButtons.setLayout(null);
		
		btnLaunchSimulation = new JButton("Lanzar Simulaci\u00F3n");
		btnLaunchSimulation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnLaunchSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnLaunchSimulationEvent(event);
			}
		});
		btnLaunchSimulation.setBounds(10, 11, 191, 62);
		panelButtons.add(btnLaunchSimulation);
		
		btnPauseContinue = new JButton("Pausar/Continuar");
		btnPauseContinue.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnPauseContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				btnPauseContinueEvent(event);
			}
		});
		btnPauseContinue.setBounds(211, 11, 190, 62);
		panelButtons.add(btnPauseContinue);
		
		JLabel LabelBugMass = new JLabel("Masa del Bicho");
		LabelBugMass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelFallRadio = new JLabel("Radio de Ca\u00EDda");
		labelFallRadio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelBugVelocity = new JLabel("Velocidad del Bicho");
		labelBugVelocity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel labelDiskVelocity = new JLabel("Velocidad del Disco");
		labelDiskVelocity.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		
		lblFinalMassValue = new JLabel("10");
		lblFinalMassValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblDistanceValue = new JLabel("1");
		lblDistanceValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		lblVelocityValue = new JLabel("2.5");
		lblVelocityValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		lblInitMassValue = new JLabel("30");
		lblInitMassValue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		sliderBugInitMass = new JSlider();
		sliderBugInitMass.setValue(30);
		sliderBugInitMass.setMinimum(20);
		sliderBugInitMass.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				sliderBugInitMassEvent();
			}
		});
		sliderBugInitMass.setMaximum(70);
		
		sliderFallRadius = new JSlider();
		sliderFallRadius.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderFallRadiusEvent();
			}
		});
		sliderFallRadius.setValue(10);
		sliderFallRadius.setMinorTickSpacing(1);
		sliderFallRadius.setMaximum(20);
		
		sliderBugVelocity = new JSlider();
		sliderBugVelocity.setValue(25);
		sliderBugVelocity.setMaximum(35);
		sliderBugVelocity.setMinimum(5);
		sliderBugVelocity.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderBugVelocityEvent();
			}
		});
		sliderBugVelocity.setMinorTickSpacing(1);
		
		sliderDiskVelocity = new JSlider();
		sliderDiskVelocity.setMaximum(50);
		sliderDiskVelocity.setMinimum(5);
		sliderDiskVelocity.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sliderDiskVelocityEvent();
			}
		});
		sliderDiskVelocity.setValue(25);
		sliderDiskVelocity.setMinorTickSpacing(1);
	
		
		
		
		GroupLayout gl_panelInputs = new GroupLayout(panelInputs);
		gl_panelInputs.setHorizontalGroup(
			gl_panelInputs.createParallelGroup(Alignment.LEADING)
				.addComponent(panelTitle, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
				.addGroup(gl_panelInputs.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(LabelBugMass, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelBugVelocity, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelDiskVelocity, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
						.addComponent(labelFallRadio, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblInitMassValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
							.addComponent(sliderBugInitMass, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panelInputs.createSequentialGroup()
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
								.addComponent(lblFinalMassValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDistanceValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.TRAILING)
								.addComponent(sliderFallRadius, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
								.addComponent(sliderBugVelocity, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addComponent(lblVelocityValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(sliderDiskVelocity, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))
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
							.addComponent(sliderBugInitMass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(sliderFallRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblInitMassValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(LabelBugMass))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(labelFallRadio)))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addComponent(sliderBugVelocity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblDistanceValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addComponent(labelBugVelocity)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelInputs.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelInputs.createSequentialGroup()
							.addGap(1)
							.addGroup(gl_panelInputs.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblVelocityValue, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(labelDiskVelocity)))
						.addComponent(sliderDiskVelocity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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
		tabbedPane.addTab("Cámara Fija", null, panelSimulation, null);
		panelSimulation.setBackground(Color.WHITE);
		
//		Thread musicThread  = new AePlayWave("./com/juanhg/diskangularmomentum/win.wav");
//		musicThread.start();
	
		getContentPane().setLayout(groupLayout);	
		
		//Obtain values from interface
		this.readInputs();
		
		//Crear modelo
		model = new AngularMDiskModel(bugInitMass, fallRadius, bugVelocity, diskVelocity);

		this.initCharts();	
	}
	
	

	void sliderBugInitMassEvent(){

		if(sliderBugInitMass.getValueIsAdjusting()){
			lblInitMassValue.setText(Double.toString((double)sliderBugInitMass.getValue()));
			repaint();
		}
	}
	void sliderDiskVelocityEvent(){

		if(sliderDiskVelocity.getValueIsAdjusting()){
			lblVelocityValue.setText(Double.toString(((double)sliderDiskVelocity.getValue()/10.0)));
			repaint();
		}
	}
	void sliderBugVelocityEvent(){

		if(sliderBugVelocity.getValueIsAdjusting()){
			if(sliderBugVelocity.getValue() <= 20){
				lblDistanceValue.setText(Double.toString(-(double)sliderBugVelocity.getValue()/10.0));
				model.setBugVelocity(-(this.sliderBugVelocity.getValue()/1000.0));
			}
			else{
				lblDistanceValue.setText(Double.toString(((double)sliderBugVelocity.getValue()-15)/10.0));
				model.setBugVelocity((this.sliderBugVelocity.getValue()-15)/1000.0);
			}
			repaint();
		}
	}
	void sliderFallRadiusEvent(){

		if(sliderFallRadius.getValueIsAdjusting() && model.getPhase() == AngularMDiskModel.INIT_PHASE){
			
			
			lblFinalMassValue.setText(Double.toString((double)sliderFallRadius.getValue()));
			
			if(targetAnnotation != null){
				chart.deleteImage(targetAnnotation);
			}
			targetAnnotation = chart.setImageAtPoint(targetImage, sliderFallRadius.getValue(), 0);
			
			model.setBugInitRadius(((double)sliderFallRadius.getValue())/100.0);
			model.getBugCoordinates().setRadius(((double)sliderFallRadius.getValue())/100.0);
			model.calculateWf();
			model.calculateWff();
			
			end = false;
			
			repaint();
		}
	}
	
	void btnLaunchSimulationEvent(ActionEvent event){
		
		btnFase1.setEnabled(true);
		
		if(flujo != null && flujo.isAlive()) {
			end = true;
	
			while(flujo.isAlive()) {}
			time.clear();
			
			if(bugAnnotation != null){
				chart.deleteImage(bugAnnotation);
			}
			if(diskAnnotation != null){
				chart.deleteImage(diskAnnotation);
			}
		}

		//time = new Time();
		
		//Obtain values from interface
		this.readInputs();

		//Crear modelo
		model = new AngularMDiskModel(bugInitMass, fallRadius, bugVelocity, diskVelocity);

		this.initCharts();
       
       //Initializes and runs the thread (Run())
       flujo = new Thread();
       flujo = new Thread(this);
       
       time.start();
       flujo.start();
	}
	
	void btnPauseContinueEvent(ActionEvent event){
		   if(flujo != null && flujo.isAlive()) {
	            time.stop();
			    end = true;
	            while(flujo.isAlive()) {}
	        }
	        else {

	            end = false;

	            flujo = new Thread(this);
	            time.start();
	            flujo.start();
	        }
	}
	
	void btnLadyBugEvent() {

		if (flujo != null && flujo.isAlive()) {

			end = true;
			while(flujo.isAlive()) {}
			
			model.setPhase(AngularMDiskModel.BUG_FALLS_PHASE);
			PolarPoint2D bug = new PolarPoint2D(model.getBugCoordinates().getRadius() * 100, model.getBugCoordinates().getPhi());

			bugAnnotation = chart.setImageAtPoint(this.bugImage,bug.toCartesianPoint());

			chart.deleteImage(targetAnnotation);

			btnFase1.setEnabled(false);

			end = false;

			time.clear();
			time.start();

			flujo = new Thread(this);
			flujo.start();
		}
	}
	

	    
	@Override
	public void run() {

		end = false;

		while(!end){


			this.rotatedDiskImage = ImageProcessing.rotateRadians(this.diskImage, model.getDiskPhi());
			if(diskAnnotation != null){
				chart.deleteImage(diskAnnotation);
			}
			diskAnnotation = chart.setImageAtPoint(this.rotatedDiskImage, 0, 0); 
					
			switch(model.getPhase()){
				case AngularMDiskModel.INIT_PHASE:
					if(targetAnnotation != null){
						chart.deleteImage(targetAnnotation);
					}
					targetAnnotation = chart.setImageAtPoint(targetImage, sliderFallRadius.getValue(), 0);
					break;
				case AngularMDiskModel.BUG_FALLS_PHASE:
					PolarPoint2D bug = new PolarPoint2D(model.getBugCoordinates().getRadius()*100, model.getBugCoordinates().getPhi());
					
					this.rotatedBugImage = ImageProcessing.rotateRadians(this.bugImage, -(model.getBugCoordinates().getPhi()-(1.5*Math.PI)));
					if(bugAnnotation != null){
						chart.deleteImage(bugAnnotation);
					}
					bugAnnotation = chart.setImageAtPoint(this.rotatedBugImage, bug.toCartesianPoint()); 
					break;
				case AngularMDiskModel.FINAL_PHASE:
					end = true;
					chart.deleteImage(bugAnnotation);
					break;
			}
			
		
			time.pause();
			model.setActualTime(((double)time.getTime())/1000.0);
			model.simulate();
			time.start();
			
			panelSimulation.actualizaGrafica(chart);
			//panelSimulation2.actualizaGrafica(chart2);
			
			repaint();

			try {
				Thread.sleep((long)sleepTime);
			} catch (InterruptedException ex) {
				Logger.getLogger(AngularMDiskApplet.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	/**
	 * Read the input values from the interface and loads its 
	 * in the variable of the class 
	 */
	private void readInputs(){
		this.bugInitMass = this.sliderBugInitMass.getValue();
		this.fallRadius = this.sliderFallRadius.getValue()/100.0;
		this.diskVelocity = this.sliderDiskVelocity.getValue()/10.0;
		this.bugVelocity = 0;
		
		if(this.sliderBugVelocity.getValue() <= 20){
			this.bugVelocity = -(this.sliderBugVelocity.getValue()/1000.0);
		}
		else{
			this.bugVelocity = (this.sliderBugVelocity.getValue()-15)/1000.0;
		}
	}
	
	//Init the chart with the model data
	private void initCharts(){
		chart = new Grafica(model.getBugsCoordinatesAsArray(),"Conservación del Momento Angular", "Bug", "Coordenada X", "Coordenada Y", false, Color.BLUE,1f,false);
		chart.setRangeAxis(this.infXLimit, this.supXLimit, this.infYLimit, this.supYLimit);
		
		
		backgroundImage = this.loadImage("wood.jpg");
		try {
			chart.setBackGroundImage(backgroundImage, 0.9f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Load Images
		
		this.diskImage = loadImage("vinilo4.png");
		this.bugImage = loadImage("ladyBug1.png");
		this.targetImage = loadImage("target642.png");
		
		//Set Images 
		diskAnnotation = chart.setImageAtPoint(this.diskImage, 0, 0); 
		targetAnnotation = chart.setImageAtPoint(targetImage, sliderFallRadius.getValue(),0);
		
		
		//Actualize panels
		panelSimulation.actualizaGrafica(chart);
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
	
	
	int obtainExponent(double number){
		int exponent = 0;
		
		while(0 == (int)number){
			exponent--;
			number *= 10;
		}
		return exponent;
		
	}
}
