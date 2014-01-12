/* -----------------------------------------------------------------	
    Copyright (C) 2013  Modesto Modesto T Lopez-Lopez
    					Miguel Jimenez Lopez
    					
    					
						
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

package com.migueljl.ondas.reflextrans;

// Recursos necesarios.

import java.awt.Color;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raccoon.easyjchart.Grafica;
import com.raccoon.easyjchart.JPanelGrafica;
/**
 * Clase que representa a la aplicación (Applet) que ejecutará el experimento virtual
 * 
 * @author Miguel Jiménez López
 */

public class applet1 extends javax.swing.JApplet implements Runnable {
	public applet1() {
	}

    /**
     * Hebra que ejecuta la simulación.
     */
    Thread flujo = null;
    
    /**
     * Variable que identifica la instantánea de las funciones simuladas que se mostrará en la aplicación.
     */
    int i = 0;
    
    JPanelGrafica panel_grafica;

    /**
     * Método que inicializa el applet.
     */
    public void init() {
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
        
        interfase = new Point2D[2];
        
        interfase[0] = new Point2D.Double(0.0,-20.0);
        interfase[1] = new Point2D.Double(0.0,20.0);
    }

    /**
     * Función que permite transformar una cadena de caracteres de entrada en un número real.
     * Considera que el separador para los decimales puede ser '.', ',' o '''.
     * @param real Número real que es representado por la cadena de entrada
     * @return Devuelve el número real indicado por la cadena de caracteres de entrada.
     */
    private double transformarDouble(String real) {
        double realt = 0.0;

         real = real.replace(',','.');
         real = real.replace('\'','.');
         
        realt = Double.parseDouble(real);

        return realt;
    }

    /**
      * Este método es llamado por init() y ha sido generado automáticamente por Netbeans.
      */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

    	
    	 
        panel_controles = new javax.swing.JPanel();
        panel_tiempo = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        tiempo_actual = new javax.swing.JLabel();
        tiempo_label = new javax.swing.JLabel();
        label_td_tiposimulacion = new javax.swing.JLabel();
        ld_tiposimulacion = new javax.swing.JComboBox();
        cb_solo_resultante = new javax.swing.JCheckBox();
        panel_input = new javax.swing.JPanel();
        ro2_input = new javax.swing.JTextField();
        ro1_input = new javax.swing.JTextField();
        frecuencia_input = new javax.swing.JTextField();
        ro2_etiq = new javax.swing.JLabel();
        ro1_etiq = new javax.swing.JLabel();
        frecuencia_etiq = new javax.swing.JLabel();
        panel_botones = new javax.swing.JPanel();
        bpausa_continua = new javax.swing.JButton();
        bsimular = new javax.swing.JButton();
        panel_visualizar = new javax.swing.JPanel();
        panel_grafica = new JPanelGrafica();

        panel_controles.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createEtchedBorder()));

        panel_tiempo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0))));

        tiempo_actual.setText("0");

        tiempo_label.setText("Tiempo actual: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tiempo_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tiempo_actual, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tiempo_label)
                    .addComponent(tiempo_actual, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        label_td_tiposimulacion.setText("Velocidad de simulación:");

        ld_tiposimulacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rápida", "Normal", "Lenta" }));

        cb_solo_resultante.setText("Solo resultante");
        cb_solo_resultante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_solo_resultanteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_tiempoLayout = new javax.swing.GroupLayout(panel_tiempo);
        panel_tiempo.setLayout(panel_tiempoLayout);
        panel_tiempoLayout.setHorizontalGroup(
            panel_tiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_tiempoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_tiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_tiempoLayout.createSequentialGroup()
                        .addComponent(label_td_tiposimulacion)
                        .addGap(35, 35, 35)
                        .addComponent(ld_tiposimulacion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panel_tiempoLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cb_solo_resultante)
                        .addContainerGap())))
        );
        panel_tiempoLayout.setVerticalGroup(
            panel_tiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_tiempoLayout.createSequentialGroup()
                .addGroup(panel_tiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_tiempoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel_tiempoLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(cb_solo_resultante)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(panel_tiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_td_tiposimulacion)
                    .addComponent(ld_tiposimulacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panel_input.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        ro2_input.setText("2");

        ro1_input.setText("1");

        frecuencia_input.setText("0.2");

        ro2_etiq.setText("Densidad en la cuerda 2 ");

        ro1_etiq.setText("Densidad en la cuerda 1");

        frecuencia_etiq.setText("Frecuencia");

        javax.swing.GroupLayout panel_inputLayout = new javax.swing.GroupLayout(panel_input);
        panel_input.setLayout(panel_inputLayout);
        panel_inputLayout.setHorizontalGroup(
            panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_inputLayout.createSequentialGroup()
                        .addComponent(ro2_etiq)
                        .addGap(18, 18, 18)
                        .addComponent(ro2_input))
                    .addGroup(panel_inputLayout.createSequentialGroup()
                        .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ro1_etiq)
                            .addComponent(frecuencia_etiq))
                        .addGap(18, 18, 18)
                        .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ro1_input)
                            .addComponent(frecuencia_input))))
                .addGap(31, 31, 31))
        );
        panel_inputLayout.setVerticalGroup(
            panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frecuencia_etiq)
                    .addComponent(frecuencia_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ro1_etiq)
                    .addComponent(ro1_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ro2_etiq)
                    .addComponent(ro2_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        panel_botones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        bpausa_continua.setText("Parar/Continuar");
        bpausa_continua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bpausa_continuaActionPerformed(evt);
            }
        });

        bsimular.setText("Inicio Simular");
        bsimular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_botonesLayout = new javax.swing.GroupLayout(panel_botones);
        panel_botones.setLayout(panel_botonesLayout);
        panel_botonesLayout.setHorizontalGroup(
            panel_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_botonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bsimular, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(bpausa_continua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_botonesLayout.setVerticalGroup(
            panel_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_botonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bsimular, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bpausa_continua, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_controlesLayout = new javax.swing.GroupLayout(panel_controles);
        panel_controles.setLayout(panel_controlesLayout);
        panel_controlesLayout.setHorizontalGroup(
            panel_controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_controlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(panel_botones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_tiempo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_input, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_controlesLayout.setVerticalGroup(
            panel_controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_controlesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(panel_tiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(panel_botones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panel_visualizar.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        panel_grafica.setBackground(new java.awt.Color(255, 255, 255));
        panel_grafica.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout panel_graficaLayout = new javax.swing.GroupLayout(panel_grafica);
        panel_grafica.setLayout(panel_graficaLayout);
        panel_graficaLayout.setHorizontalGroup(
            panel_graficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 617, Short.MAX_VALUE)
        );
        panel_graficaLayout.setVerticalGroup(
            panel_graficaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_visualizarLayout = new javax.swing.GroupLayout(panel_visualizar);
        panel_visualizar.setLayout(panel_visualizarLayout);
        panel_visualizarLayout.setHorizontalGroup(
            panel_visualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_grafica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_visualizarLayout.setVerticalGroup(
            panel_visualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_grafica, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel_controles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_visualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_controles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_visualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Permite comenzar de nuevo la simulación
     * @param evt Evento con la información necesaria
     */
    private void bsimularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimularActionPerformed
        // TODO add your handling code here:
        int vsimulacion;

        if(flujo != null && flujo.isAlive()) {
            fin = true;
            while(flujo.isAlive()) {}
        }

       tactual = 0;
       frecuencia = transformarDouble(frecuencia_input.getText());
       densidad_cuerda1 = transformarDouble(ro1_input.getText());
       densidad_cuerda2 = transformarDouble(ro2_input.getText());
       tension = 1.0;
       vsimulacion = ld_tiposimulacion.getSelectedIndex();
       
       if(vsimulacion == 0)
           TSIMULACION = 10000;
       else
           if(vsimulacion == 1)
               TSIMULACION = 40000;
           else
               TSIMULACION = 60000;

       Modelo modelo = new Modelo(frecuencia,densidad_cuerda1,densidad_cuerda2,tension);
       modelo.simular(TINICIO,TFIN,DT,DX);
       oi = modelo.getOndaIncidente();
       or = modelo.getOndaReflejada();
       ot = modelo.getOndaTransmitida();
       os = modelo.getOndaSuperposicion();

        i = 0;
        fin = false;
        flujo = new Thread(this);
        flujo.start();
        

    }//GEN-LAST:event_bsimularActionPerformed

    /**
     * Permite pausar y continuar la simulación en curso.
     * @param evt Evento con la información necesaria
     */
    private void bpausa_continuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bpausa_continuaActionPerformed
        // TODO add your handling code here:
        if(flujo != null && flujo.isAlive()) {
            fin = true;
            while(flujo.isAlive()) {}
        }
        else {

            fin = false;

            flujo = new Thread(this);
            flujo.start();
        }
    }//GEN-LAST:event_bpausa_continuaActionPerformed

    /**
     * Permite indicar si se desea visualizar solo la resultante o todas las ondas.
     * @param evt Evento con la información necesaria
     */
    private void cb_solo_resultanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_solo_resultanteActionPerformed
        // TODO add your handling code here:
        if(cb_solo_resultante.isSelected())
            solo_resultante = true;
        else
            solo_resultante = false;
    }//GEN-LAST:event_cb_solo_resultanteActionPerformed
    
    /**
     * Obtiene la instantánea actual de la onda incidente.
     * @return Conjunto de puntos que representa el estado actual de la onda incidente.
     */
    private Point2D[] getInstantaneaOndaIncidente() {
        Point2D[] ins_oi = oi.get(i);

        return ins_oi;
    }
    
    
    /**
     * Obtiene la instantánea actual de la onda reflejada.
     * @return Conjunto de puntos que representa el estado actual de la onda reflejada.
     */
    private Point2D[] getInstantaneaOndaReflejada() {
        Point2D[] ins_or = or.get(i);

        return ins_or;
    }
    
    
    /**
     * Obtiene la instantánea actual de la onda transmitida.
     * @return Conjunto de puntos que representa el estado actual de la onda transmitida.
     */
    private Point2D[] getInstantaneaOndaTransmitida() {
        Point2D[] ins_ot = ot.get(i);

        return ins_ot;
    }
    
    
    /**
     * Obtiene la instantánea actual de la onda resultante.
     * @return Conjunto de puntos que representa el estado actual de la onda resultante.
     */
    private Point2D[] getInstantaneaOndaSuperposicion() {
        Point2D[] ins_os = os.get(i);

        return ins_os;
    }
    
    /**
     * Avanza la simulación. Es decir, incrementa el número de instantánea (de las funciones simuladas) que será representado posteriormente.
     */
    private void actualizarInstantaneas() {
        i = (i+1)%oi.size();

        if(i == 0)
            tactual = 0;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bpausa_continua;
    private javax.swing.JButton bsimular;
    private javax.swing.JCheckBox cb_solo_resultante;
    private javax.swing.JLabel frecuencia_etiq;
    private javax.swing.JTextField frecuencia_input;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel label_td_tiposimulacion;
    private javax.swing.JComboBox ld_tiposimulacion;
    private javax.swing.JPanel panel_botones;
    private javax.swing.JPanel panel_controles;
    private javax.swing.JPanel panel_input;
    private javax.swing.JPanel panel_tiempo;
    private javax.swing.JPanel panel_visualizar;
    private javax.swing.JLabel ro1_etiq;
    private javax.swing.JTextField ro1_input;
    private javax.swing.JLabel ro2_etiq;
    private javax.swing.JTextField ro2_input;
    private javax.swing.JLabel tiempo_actual;
    private javax.swing.JLabel tiempo_label;
    // End of variables declaration//GEN-END:variables

   
    /**
     * Contiene las gráficas de las ondas implicadas en el fenómeno físico (reflejada, transmitida, incidente y resultante).
     */
    private Grafica grafica;
    /**
     * Conjunto de puntos con la evolución temporal de la onda incidente.
     */
    private ArrayList<Point2D[]> oi;
    /**
     * Conjunto de puntos con la evolución temporal de la onda reflejada.
     */
    private ArrayList<Point2D[]> or;
    /**
     * Conjunto de puntos con la evolución temporal de la onda transmitida.
     */
    private ArrayList<Point2D[]> ot;
    /**
     * Conjunto de puntos con la evolución temporal de la onda resultante.
     */
    private ArrayList<Point2D[]> os;
    /**
     * Tiempo actual de la simulación.
     */
    private float tactual = (float) 0.0;
    /**
     * Tiempo de inicio de la simulación.
     */
    private final double TINICIO = 0.0;
    /**
     * Tiempo total de la simulación.
     */
    private int TSIMULACION = 10000;
    /**
     * Tiempo de fin de la simulación.
     */
    private final double TFIN = 15.0;
    /**
     * Incremento de tiempo.
     */
    private final double DT = 0.1;
    /**
     * Incremento de longitud (espacio).
     */
    private final double DX = 0.01;
    /**
     * Indica si la simulación ha terminado.
     */
    private boolean fin = false;
    /**
     * Frecuencia de la onda incidente.
     */
    private double frecuencia = 0.2;
    /**
     * Densidad de la primera cuerda.
     */
    private double densidad_cuerda1 = 1;
    /**
     * Densidad de la segunda cuerda.
     */
    private double densidad_cuerda2 = 2;
    /**
     * Tensión de las cuerdas.
     */
    private double tension = 1.0;
    /**
     * Indica si solo se quiere visualizar la onda resultante.
     */
    private boolean solo_resultante = false;
    
    /**
     * Conjunto de puntos que determina la interfase entre los dos medios.
     */
    private Point2D[] interfase = null;


    /**
     * Función que contiene la ejecución de la simulación del experimento virtual.
     */
    public void run() {
        int tespera = (TSIMULACION)/oi.size();
        DecimalFormat formateador = new DecimalFormat("##.#");

        while(!fin) {
            tiempo_actual.setText(formateador.format(tactual));
            tactual += DT;
            
            if(!solo_resultante) {
                grafica = new Grafica(getInstantaneaOndaIncidente(),"Reflexión y transmisión","Onda Incidente","Posición","Amplitud",true);
                grafica.agregarGrafica(getInstantaneaOndaReflejada(),"Onda Reflejada");
                grafica.agregarGrafica(getInstantaneaOndaTransmitida(),"Onda Transmitida");
                grafica.agregarGrafica(getInstantaneaOndaSuperposicion(),"Onda Superposicion");
                grafica.agregarGrafica(interfase, "Interfase");
                
                grafica.fijaColor(0, Color.BLACK);
                grafica.fijaGrosor(0,1);
                grafica.fijaColor(1, Color.RED);
                grafica.fijaGrosor(1,1);
                grafica.fijaColor(2, new Color(10,150,10));
                grafica.fijaGrosor(2,2);
                grafica.fijaColor(3, Color.BLUE);
                grafica.fijaGrosor(3,2);
                grafica.fijaColor(4, Color.BLACK);
                grafica.fijaGrosor(4,2);
            }
            else {
                grafica = new Grafica(getInstantaneaOndaSuperposicion(),"Reflexión y transmisión","Onda Superposicion","Posición","Amplitud",true);
                grafica.agregarGrafica(getInstantaneaOndaTransmitida(),"Onda Transmitida");
                grafica.agregarGrafica(interfase, "Interfase");
                
                grafica.fijaColor(0, Color.BLUE);
                grafica.fijaGrosor(0,2);
                grafica.fijaColor(1, new Color(10,150,10));
                grafica.fijaGrosor(1,2);
                grafica.fijaColor(2, Color.BLACK);
                grafica.fijaGrosor(2,2);
            }
            
            grafica.fijaRango(-20,20,0,0);
            grafica.fijaRango(-20,20,0,1);
            
            panel_grafica.actualizaGrafica(grafica);
            
            
            actualizarInstantaneas();
            
            try {
                Thread.sleep(tespera);
            } catch (InterruptedException ex) {
                Logger.getLogger(applet1.class.getName()).log(Level.SEVERE, null, ex);
            }
            repaint();
         
        }
    }
}
