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

package com.migueljl.oscilaciones.amortiguada;

// Referencias utilizadas.

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raccoon.easyjchart.*;
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
        panel_input = new javax.swing.JPanel();
        r_input = new javax.swing.JTextField();
        m_input = new javax.swing.JTextField();
        r_etiq = new javax.swing.JLabel();
        m_etiq = new javax.swing.JLabel();
        k_etiq = new javax.swing.JLabel();
        k_input = new javax.swing.JTextField();
        x0_etiq = new javax.swing.JLabel();
        x0_input = new javax.swing.JTextField();
        v0_etiq = new javax.swing.JLabel();
        v0_input = new javax.swing.JTextField();
        rango_etiq = new javax.swing.JLabel();
        rango_escala = new javax.swing.JComboBox();
        panel_botones = new javax.swing.JPanel();
        bpausa_continua = new javax.swing.JButton();
        bsimular = new javax.swing.JButton();
        graficas_simulacion = new javax.swing.JTabbedPane();
        panel_graficas = new javax.swing.JPanel();
        panelgposicion = new javax.swing.JPanel();
        gposicion = new JPanelGrafica();
        panel_simulacion = new javax.swing.JPanel();
        gsimulacion = new JPanelGrafica();

        panel_controles.setBackground(new java.awt.Color(204, 204, 204));
        panel_controles.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createEtchedBorder()));

        panel_input.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        r_input.setText("1");

        m_input.setText("5");

        r_etiq.setText("Cte Amortiguamiento (r)");

        m_etiq.setText("Masa (M)");

        k_etiq.setText("Cte Elástica (k)");

        k_input.setText("5");

        x0_etiq.setText("Posición inicial (Pi)");

        x0_input.setText("100");

        v0_etiq.setText("Velocidad inicial (Vi)");

        v0_input.setText("-50");

        rango_etiq.setText("Rango:");

        rango_escala.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "5", "10", "50", "100", "500" }));
        rango_escala.setSelectedIndex(4);

        javax.swing.GroupLayout panel_inputLayout = new javax.swing.GroupLayout(panel_input);
        panel_input.setLayout(panel_inputLayout);
        panel_inputLayout.setHorizontalGroup(
            panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_inputLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_etiq, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panel_inputLayout.createSequentialGroup()
                        .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_inputLayout.createSequentialGroup()
                                .addComponent(rango_etiq)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rango_escala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(v0_input, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(m_input, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(r_input, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(r_etiq)
                                .addComponent(k_input, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(k_etiq)
                                .addComponent(x0_etiq)
                                .addComponent(x0_input, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(v0_etiq)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panel_inputLayout.setVerticalGroup(
            panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(m_etiq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(r_etiq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(r_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(k_etiq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(k_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(x0_etiq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(x0_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(v0_etiq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(v0_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rango_etiq)
                    .addComponent(rango_escala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_botones.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        bpausa_continua.setText("PARAR/CONTINUAR");
        bpausa_continua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bpausa_continuaActionPerformed(evt);
            }
        });

        bsimular.setText("SIMULAR");
        bsimular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_botonesLayout = new javax.swing.GroupLayout(panel_botones);
        panel_botones.setLayout(panel_botonesLayout);
        panel_botonesLayout.setHorizontalGroup(
            panel_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_botonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bsimular, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bpausa_continua)
                .addContainerGap())
        );
        panel_botonesLayout.setVerticalGroup(
            panel_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_botonesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bsimular, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bpausa_continua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panel_controlesLayout = new javax.swing.GroupLayout(panel_controles);
        panel_controles.setLayout(panel_controlesLayout);
        panel_controlesLayout.setHorizontalGroup(
            panel_controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_controlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panel_input, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_botones, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_controlesLayout.setVerticalGroup(
            panel_controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_controlesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_input, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(panel_botones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        graficas_simulacion.setBackground(new java.awt.Color(255, 255, 255));

        panel_graficas.setBackground(new java.awt.Color(204, 204, 204));
        panel_graficas.setPreferredSize(new java.awt.Dimension(929, 400));

        gposicion.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout gposicionLayout = new javax.swing.GroupLayout(gposicion);
        gposicion.setLayout(gposicionLayout);
        gposicionLayout.setHorizontalGroup(
            gposicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 676, Short.MAX_VALUE)
        );
        gposicionLayout.setVerticalGroup(
            gposicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelgposicionLayout = new javax.swing.GroupLayout(panelgposicion);
        panelgposicion.setLayout(panelgposicionLayout);
        panelgposicionLayout.setHorizontalGroup(
            panelgposicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gposicion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelgposicionLayout.setVerticalGroup(
            panelgposicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gposicion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_graficasLayout = new javax.swing.GroupLayout(panel_graficas);
        panel_graficas.setLayout(panel_graficasLayout);
        panel_graficasLayout.setHorizontalGroup(
            panel_graficasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_graficasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelgposicion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_graficasLayout.setVerticalGroup(
            panel_graficasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_graficasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelgposicion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        graficas_simulacion.addTab("Gráficas", panel_graficas);

        gsimulacion.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout gsimulacionLayout = new javax.swing.GroupLayout(gsimulacion);
        gsimulacion.setLayout(gsimulacionLayout);
        gsimulacionLayout.setHorizontalGroup(
            gsimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        gsimulacionLayout.setVerticalGroup(
            gsimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_simulacionLayout = new javax.swing.GroupLayout(panel_simulacion);
        panel_simulacion.setLayout(panel_simulacionLayout);
        panel_simulacionLayout.setHorizontalGroup(
            panel_simulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gsimulacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panel_simulacionLayout.setVerticalGroup(
            panel_simulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gsimulacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        graficas_simulacion.addTab("Simulación", panel_simulacion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(graficas_simulacion, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_controles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_controles, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(graficas_simulacion)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Permite comenzar de nuevo la simulación
     * @param evt Evento con la información necesaria
     */
    private void bsimularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimularActionPerformed
        // TODO add your handling code here:

        if(flujo != null && flujo.isAlive()) {
            fin = true;
            while(flujo.isAlive()) {}
        }

       m = transformarDouble(m_input.getText());
       r = transformarDouble(r_input.getText());
       k = transformarDouble(k_input.getText());
       x0 = transformarDouble(x0_input.getText());
       v0 = transformarDouble(v0_input.getText());

       Modelo modelo = new Modelo(m,r,k,x0,v0);
       modelo.simular(TINICIO,TFIN,DT);

       posicion = modelo.getPosicion();
       muelle_simulacion = modelo.getSimulacionMuelle();
       L = modelo.getLongitudMuelle();
       Adiente = modelo.getAmplitudDientes();

       repaint();

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
     * Función que obtiene el conjunto de puntos que delimita la superficie sobre la que se apoya el muelle.
     * @return Un conjunto de puntos que representa la superficie de apoyo
     */
    private Point2D[] getSuperficie() {
        Point2D[] superficie = new Point2D[2];

        superficie[0] = new Point2D.Double(-L,-Adiente);
        superficie[1] = new Point2D.Double(L,-Adiente);

        return superficie;
    }

    /**
     * Función que obtiene el conjunto de puntos que delimita la pared donde está enganchado el resorte.
     * @param limite_superior Identifica el límite superior hasta donde se representará la pared (punto más alto)
     * @return Un conjunto de puntos que representa la pared.
     */
      private Point2D[] getPared(double limite_superior) {
        Point2D[] pared = new Point2D[2];

        pared[0] = new Point2D.Double(-L,-Adiente);
        pared[1] = new Point2D.Double(-L,limite_superior);

        return pared;
    }

          /**
     * Función que obtiene el conjunto de puntos que define el bloque que se engancha en un extremo del resorte.
     * @param pos_actual Indica la posición actual del punto de enganche entre el muelle y el bloque.
     * @return Un conjunto de puntos que representa el bloque enganchado al resorte.
     */
    private Point2D[][] getBloque(double pos_actual) {
        Point2D[][] caja = new Point2D[6][2];

        caja[0][0] = new Point2D.Double(pos_actual,-Adiente);
        caja[0][1] = new Point2D.Double(pos_actual,Adiente);

        caja[1][0] = new Point2D.Double(pos_actual,Adiente);
        caja[1][1] = new Point2D.Double(pos_actual+36*Adiente,Adiente);

        caja[2][0] = new Point2D.Double(pos_actual+36*Adiente,Adiente);
        caja[2][1] = new Point2D.Double(pos_actual+36*Adiente,-Adiente);

        caja[3][0] = new Point2D.Double(pos_actual+36*Adiente,-Adiente);
        caja[3][1] = new Point2D.Double(pos_actual,-Adiente);

        caja[4][0] = new Point2D.Double(pos_actual,Adiente);
        caja[4][1] = new Point2D.Double(pos_actual+36*Adiente,-Adiente);

        caja[5][0] = new Point2D.Double(pos_actual+36*Adiente,Adiente);
        caja[5][1] = new Point2D.Double(pos_actual,-Adiente);

        return caja;
    }

    /**
     * Avanza la simulación. Es decir, incrementa el número de instantánea (de las funciones simuladas) que será representado posteriormente.
     */
    private void actualizaSimulacion() {
        i = (i+1)%muelle_simulacion.size();
    }

    /**
     * Obtiene la instantánea actual de la simulación del muelle.
     * @return Conjunto de puntos que representa el estado actual de la simulación del muelle.
     */
    private Point2D[] getFuncionSimulacion() {
        Point2D[] func_simulacion = muelle_simulacion.get(i);

        return func_simulacion;
    }

    /**
     * Obtiene la instantánea actual de la gráfica Posición
     * @return Conjunto de puntos que representa el estado actual de la posición
     */
    private Point2D[] getFuncionSimulacion2() {
        Point2D[] func_simulacion2 = null;

        func_simulacion2 = new Point2D[i+1];

        for(int j = 0 ; j < i+1 ; j++)
            func_simulacion2[j] = new Point2D.Double(posicion[j].getX(),posicion[j].getY());

        return func_simulacion2;
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bpausa_continua;
    private javax.swing.JButton bsimular;
    private JPanelGrafica gposicion;
    private javax.swing.JTabbedPane graficas_simulacion;
    private JPanelGrafica gsimulacion;
    private javax.swing.JLabel k_etiq;
    private javax.swing.JTextField k_input;
    private javax.swing.JLabel m_etiq;
    private javax.swing.JTextField m_input;
    private javax.swing.JPanel panel_botones;
    private javax.swing.JPanel panel_controles;
    private javax.swing.JPanel panel_graficas;
    private javax.swing.JPanel panel_input;
    private javax.swing.JPanel panel_simulacion;
    private javax.swing.JPanel panelgposicion;
    private javax.swing.JLabel r_etiq;
    private javax.swing.JTextField r_input;
    private javax.swing.JComboBox rango_escala;
    private javax.swing.JLabel rango_etiq;
    private javax.swing.JLabel v0_etiq;
    private javax.swing.JTextField v0_input;
    private javax.swing.JLabel x0_etiq;
    private javax.swing.JTextField x0_input;
    // End of variables declaration//GEN-END:variables

    /**
     * Contiene la gráfica de la posición.
     */
    private Grafica grafposicion;

    /**
     * Contiene la simulación del muelle.
     */
    private Grafica simulacion;

    /**
     * Conjunto de puntos con la evolución temporal de la simulación del muelle.
     */
    private ArrayList<Point2D[]> muelle_simulacion;

    /**
     * Conjunto de puntos con la evolución temporal de la gráfica de la posición.
     */
    private Point2D[] posicion;

    /**
     * Tiempo de inicio de la simulación.
     */
    private final double TINICIO = 0.0;
    /**
     * Tiempo total de la simulación (en el applet).
     */
    private int TSIMULACION = 60000;
    /**
     * Tiempo de fin de la simulación.
     */
    private final double TFIN = 60.0;
    /**
     * Incremento de tiempo en la simulación.
     */
    private final double DT = 0.1;
    /**
     * Indica si la simulación ha terminado.
     */
    private boolean fin = false;
    /**
     * Masa del cuerpo enganchado al resorte.
     */
    private double m = 5.0;
    /**
     * Coeficiente de amortiguamiento.
     */
    private double r = 1.0;
    /**
     * Constante elástica del muelle.
     */
    private double k = 5.0;
    /**
     * Posición inicial del cuerpo enganchado al resorte.
     */
    private double x0 = 100.0;
    /**
     * Velocidad inicial del cuerpo enganchado al resorte.
     */
    private double v0 = -50.0;
    /**
     * Longitud del muelle.
     */
    private double L;
    /**
     * Altura de cada uno de los picos del muelle.
     */
    private double Adiente;

    private Point2D[] lineacero_puntos = {new Point2D.Double(0.0,0.0),new Point2D.Double(TFIN,0.0)};

    /**
     * Método que pinta el applet en pantalla
     * @param g Lienzo sobre el que se pintará el applet.
     */
    @Override
    public void paint(Graphics g) {
        graficas_simulacion.repaint();
        panel_botones.repaint();
        panel_controles.repaint();
        panel_graficas.repaint();
    }

    /**
     * Función que contiene la ejecución de la simulación del experimento virtual.
     */
    public void run() {
        int tespera = (TSIMULACION)/muelle_simulacion.size();
        Point2D[] superficie = getSuperficie();
        Point2D[] pared = getPared(4);
        Point2D[] instantanea_muelle;
        Point2D[] instancia_posicion;
        Point2D[][] bloque;
        double rango = 10;
        int indice_rango = 0;

        while(!fin) {

            instantanea_muelle = getFuncionSimulacion();
            instancia_posicion = getFuncionSimulacion2();
            actualizaSimulacion();

            grafposicion = new Grafica(instancia_posicion,"Evolución temporal de la posición","Posición","Tiempo","Posición",false,Color.RED,1,true);
            grafposicion.agregarGrafica(lineacero_puntos, "referencia", Color.gray,1,true);
            
            indice_rango = rango_escala.getSelectedIndex();
            
            rango = Integer.parseInt((String) rango_escala.getItemAt(indice_rango));
            
            grafposicion.fijaRango(0.0, TFIN, 0,0);
            grafposicion.fijaRango(-rango,rango,0,1);

            gposicion.actualizaGrafica(grafposicion);

            simulacion = new Grafica(instantanea_muelle,"Simulación de la oscilación del muelle","Muelle","X","Y",false,Color.black,1,true);
            bloque = getBloque(instantanea_muelle[instantanea_muelle.length-1].getX());
            simulacion.agregarGrafica(superficie,"Plano", Color.black,1,true);
            simulacion.agregarGrafica(pared,"Pared",Color.black,1,true);
            simulacion.agregarGrafica(bloque[0],"Lado1", Color.blue,1,true);
            simulacion.agregarGrafica(bloque[1],"Lado2", Color.blue,1,true);
            simulacion.agregarGrafica(bloque[2],"Lado3", Color.blue,1,true);
            simulacion.agregarGrafica(bloque[3],"Lado4", Color.blue,1,true);
            simulacion.agregarGrafica(bloque[4],"X1",Color.blue,1,true);
            simulacion.agregarGrafica(bloque[5],"X2",Color.blue,1,true);
            
            simulacion.fijaRango(-120,120,0,0);
            simulacion.fijaRango(-4, 4,0,1);
            
            simulacion.fijaFondo(Color.white);
            
            gsimulacion.actualizaGrafica(simulacion);
            try {
                Thread.sleep(tespera);
            } catch (InterruptedException ex) {
                Logger.getLogger(applet1.class.getName()).log(Level.SEVERE, null, ex);
            }
            repaint();
        }
    }
    
    
}
