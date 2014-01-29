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


package com.migueljl.ondas.cuerda;

// Referencias necesarias

import java.awt.Color;
import com.raccoon.easyjchart.*;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        panel_input = new javax.swing.JPanel();
        tension_input = new javax.swing.JTextField();
        mcuenta_input = new javax.swing.JTextField();
        c_input = new javax.swing.JTextField();
        frecuencia_input = new javax.swing.JTextField();
        tension_etiq = new javax.swing.JLabel();
        mcuenta_etiq = new javax.swing.JLabel();
        c_etiq = new javax.swing.JLabel();
        frecuencia_etiq = new javax.swing.JLabel();
        panel_botones = new javax.swing.JPanel();
        bpausa_continua = new javax.swing.JButton();
        bsimular = new javax.swing.JButton();
        panel_visualizar = new javax.swing.JPanel();
        panel_grafica = new JPanelGrafica();

        setSize(1000,500);
        
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
                        .addComponent(ld_tiposimulacion, 0, 83, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        panel_tiempoLayout.setVerticalGroup(
            panel_tiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_tiempoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(panel_tiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_td_tiposimulacion)
                    .addComponent(ld_tiposimulacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        panel_input.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tension_input.setText("1");

        mcuenta_input.setText("5");

        c_input.setText("0.5");

        frecuencia_input.setText("1");

        tension_etiq.setText("Tensión (T)");

        mcuenta_etiq.setText("Masa de la cuenta (M)");

        c_etiq.setText("Velocidad en la cuerda (c)");

        frecuencia_etiq.setText("Frecuencia (w)");

        javax.swing.GroupLayout panel_inputLayout = new javax.swing.GroupLayout(panel_input);
        panel_input.setLayout(panel_inputLayout);
        panel_inputLayout.setHorizontalGroup(
            panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(c_etiq)
                    .addComponent(tension_etiq)
                    .addComponent(mcuenta_etiq)
                    .addComponent(frecuencia_etiq))
                .addGap(18, 18, 18)
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tension_input, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(mcuenta_input, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(frecuencia_input, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(c_input, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addGap(31, 31, 31))
        );
        panel_inputLayout.setVerticalGroup(
            panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frecuencia_etiq)
                    .addComponent(frecuencia_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(c_etiq)
                    .addComponent(c_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(mcuenta_etiq)
                    .addComponent(mcuenta_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tension_etiq)
                    .addComponent(tension_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(bpausa_continua, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGap(0, 636, Short.MAX_VALUE)
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
       velocidad = transformarDouble(c_input.getText());
       masa = transformarDouble(mcuenta_input.getText());
       tension = transformarDouble(tension_input.getText());
       vsimulacion = ld_tiposimulacion.getSelectedIndex();
       
       if(vsimulacion == 0)
           TSIMULACION = 20000;
       else
           if(vsimulacion == 1)
               TSIMULACION = 40000;
           else
               TSIMULACION = 60000;

       Modelo modelo = new Modelo(frecuencia,velocidad,masa,tension);
       modelo.simular(TINICIO,TFIN,DT,DX);
       funcion = modelo.getAmplitud();

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
     * Función que obtiene la posición de la cuenta en el instante actual de simulación
     * @param f Conjunto de puntos que representa la oscilación en la cuerda.
     * @return Un conjunto de puntos (con solo un punto) que identifica la posición de la cuenta en el instante actual.
     */
    private Point2D[] getCuenta(Point2D[] f) {
        
        Point2D[] c = new Point2D[1];

        for(int i = 0 ; i < f.length ; i++) {
            if(f[i].getX() > -DX && f[i].getX() < DX ) {
                c[0] = new Point2D.Double(f[i].getX(),f[i].getY());
                break;
            }
        }
        
        return c;
    
    }
    /**
     * Obtiene la instantánea actual del estado de la onda en la cuerda.
     * @return Conjunto de puntos que representa a la onda en la cuerda.
     */
    private Point2D[] getFuncionSimulacion() {
        Point2D[] func_simulacion = funcion.get(i);

        return func_simulacion;
    }
    
     /**
     * Avanza la simulación. Es decir, incrementa el número de instantánea (de las funciones simuladas) que será representado posteriormente.
     */
    private void avanzaSimulacion() {
        i = (i+1)%funcion.size();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bpausa_continua;
    private javax.swing.JButton bsimular;
    private javax.swing.JLabel c_etiq;
    private javax.swing.JTextField c_input;
    private javax.swing.JLabel frecuencia_etiq;
    private javax.swing.JTextField frecuencia_input;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel label_td_tiposimulacion;
    private javax.swing.JComboBox ld_tiposimulacion;
    private javax.swing.JLabel mcuenta_etiq;
    private javax.swing.JTextField mcuenta_input;
    private javax.swing.JPanel panel_botones;
    private javax.swing.JPanel panel_controles;
    private JPanelGrafica panel_grafica;
    private javax.swing.JPanel panel_input;
    private javax.swing.JPanel panel_tiempo;
    private javax.swing.JPanel panel_visualizar;
    private javax.swing.JLabel tension_etiq;
    private javax.swing.JTextField tension_input;
    private javax.swing.JLabel tiempo_actual;
    private javax.swing.JLabel tiempo_label;
    // End of variables declaration//GEN-END:variables

    /**
     * Contiene la gráfica que representa a la onda en la cuerda.
     */
    private Grafica grafica;
    /**
     * Conjunto de puntos con la evolución temporal de la onda en la cuerda.
     */
    private ArrayList<Point2D[]> funcion;
    /**
     * Tiempo actual de la simulación.
     */
    private float tactual = (float) 0.0;
    /**
     * Tiempo de inicio de la simulación.
     */
    private final double TINICIO = 0.0;
    /**
     * Tiempo total de la simulación (en el applet).
     */
    private int TSIMULACION = 20000;
    /**
     * Tiempo de fin de la simulación.
     */
    private final double TFIN = 15.0;
    /**
     * Incremento de tiempo en la simulación.
     */
    private final double DT = 0.1;
    /**
     * Incremento de longitud en la cuerda.
     */
    private final double DX = 0.01;
    /**
     * Indica si la simulación ha finalizado.
     */
    private boolean fin = false;
    /**
     * Frecuencia de la onda.
     */
    private double frecuencia = 1.0;
    /**
     * Velocidad de la onda en la cuerda.
     */
    private double velocidad = 0.5;
    /**
     * Masa de la cuenta.
     */
    private double masa = 5.0;
    /**
     * Tensión de la cuerda.
     */
    private double tension = 1.0;
    
    /**
     * Instantánea de la onda en la cuerda.
     */
    private Point2D[] instantanea_funcion;

    /**
     * Función que contiene la ejecución de la simulación del experimento virtual.
     */
    public void run() {
        int tespera = (TSIMULACION)/funcion.size();
        DecimalFormat formateador = new DecimalFormat("##.#");

        while(!fin) {
            tiempo_actual.setText(formateador.format(tactual));
            tactual += DT;
            instantanea_funcion = getFuncionSimulacion();
            grafica = new Grafica(instantanea_funcion,"Evolución temporal del desplazamiento","Cuerda","Posición","Amplitud",true,Color.RED,1f,true);
            grafica.agregarGrafica(getCuenta(instantanea_funcion),"Cuenta", Color.BLUE, 4f,true);
            grafica.visualizaMuestras(0,true,0);
            grafica.fijaRango(-2,2,0,1);
           
            avanzaSimulacion();
            try {
                Thread.sleep(tespera);
            } catch (InterruptedException ex) {
                Logger.getLogger(applet1.class.getName()).log(Level.SEVERE, null, ex);
            }
            repaint();
        }
    }
}
