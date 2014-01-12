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

package com.migueljl.oscilaciones.simple;


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
        v0_input = new javax.swing.JTextField();
        x0_input = new javax.swing.JTextField();
        m_input = new javax.swing.JTextField();
        k_input = new javax.swing.JTextField();
        v0_etiq = new javax.swing.JLabel();
        x0_etiq = new javax.swing.JLabel();
        m_etiq = new javax.swing.JLabel();
        k_etiq = new javax.swing.JLabel();
        panel_botones = new javax.swing.JPanel();
        bpausa_continua = new javax.swing.JButton();
        bsimular = new javax.swing.JButton();
        graficas_simulacion = new javax.swing.JTabbedPane();
        panel_graficas = new javax.swing.JPanel();
        panelgposicion = new javax.swing.JPanel();
        gposicion = new JPanelGrafica();
        panelgvelocidad = new javax.swing.JPanel();
        gvelocidad = new JPanelGrafica();
        panelgaceleracion = new javax.swing.JPanel();
        gaceleracion = new JPanelGrafica();
        panel_simulacion = new javax.swing.JPanel();
        gsimulacion = new JPanelGrafica();

        panel_controles.setBackground(new java.awt.Color(204, 204, 204));
        panel_controles.setBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createEtchedBorder()));

        panel_input.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        v0_input.setText("1");

        x0_input.setText("5");

        m_input.setText("0.5");

        k_input.setText("1");

        v0_etiq.setText("Velocidad inicial (Vi)");

        x0_etiq.setText("Posición inicial (Pi)");

        m_etiq.setText("Masa (m)");

        k_etiq.setText("Constante elástica (k)");

        javax.swing.GroupLayout panel_inputLayout = new javax.swing.GroupLayout(panel_input);
        panel_input.setLayout(panel_inputLayout);
        panel_inputLayout.setHorizontalGroup(
            panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(x0_etiq)
                    .addComponent(v0_etiq)
                    .addComponent(m_etiq)
                    .addGroup(panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(v0_input, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(x0_input, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(m_input, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(k_input, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(k_etiq, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_inputLayout.setVerticalGroup(
            panel_inputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_inputLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(k_etiq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(k_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(m_etiq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(x0_etiq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(x0_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(v0_etiq)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(v0_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
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
            .addGroup(panel_botonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bsimular, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(bpausa_continua, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_botonesLayout.setVerticalGroup(
            panel_botonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_botonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bpausa_continua, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bsimular, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_controlesLayout = new javax.swing.GroupLayout(panel_controles);
        panel_controles.setLayout(panel_controlesLayout);
        panel_controlesLayout.setHorizontalGroup(
            panel_controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_controlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panel_botones, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel_input, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_controlesLayout.setVerticalGroup(
            panel_controlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_controlesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_botones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        panel_graficas.setBackground(new java.awt.Color(204, 204, 204));

        gposicion.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout gposicionLayout = new javax.swing.GroupLayout(gposicion);
        gposicion.setLayout(gposicionLayout);
        gposicionLayout.setHorizontalGroup(
            gposicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 889, Short.MAX_VALUE)
        );
        gposicionLayout.setVerticalGroup(
            gposicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelgposicionLayout = new javax.swing.GroupLayout(panelgposicion);
        panelgposicion.setLayout(panelgposicionLayout);
        panelgposicionLayout.setHorizontalGroup(
            panelgposicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelgposicionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gposicion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelgposicionLayout.setVerticalGroup(
            panelgposicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gposicion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        gvelocidad.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout gvelocidadLayout = new javax.swing.GroupLayout(gvelocidad);
        gvelocidad.setLayout(gvelocidadLayout);
        gvelocidadLayout.setHorizontalGroup(
            gvelocidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 889, Short.MAX_VALUE)
        );
        gvelocidadLayout.setVerticalGroup(
            gvelocidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 194, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelgvelocidadLayout = new javax.swing.GroupLayout(panelgvelocidad);
        panelgvelocidad.setLayout(panelgvelocidadLayout);
        panelgvelocidadLayout.setHorizontalGroup(
            panelgvelocidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelgvelocidadLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gvelocidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelgvelocidadLayout.setVerticalGroup(
            panelgvelocidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gvelocidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        gaceleracion.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout gaceleracionLayout = new javax.swing.GroupLayout(gaceleracion);
        gaceleracion.setLayout(gaceleracionLayout);
        gaceleracionLayout.setHorizontalGroup(
            gaceleracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 878, Short.MAX_VALUE)
        );
        gaceleracionLayout.setVerticalGroup(
            gaceleracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelgaceleracionLayout = new javax.swing.GroupLayout(panelgaceleracion);
        panelgaceleracion.setLayout(panelgaceleracionLayout);
        panelgaceleracionLayout.setHorizontalGroup(
            panelgaceleracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelgaceleracionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gaceleracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelgaceleracionLayout.setVerticalGroup(
            panelgaceleracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gaceleracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout panel_graficasLayout = new javax.swing.GroupLayout(panel_graficas);
        panel_graficas.setLayout(panel_graficasLayout);
        panel_graficasLayout.setHorizontalGroup(
            panel_graficasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_graficasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_graficasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelgaceleracion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelgvelocidad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelgposicion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panel_graficasLayout.setVerticalGroup(
            panel_graficasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_graficasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelgposicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelgvelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelgaceleracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        graficas_simulacion.addTab("Gráficas", panel_graficas);

        gsimulacion.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout gsimulacionLayout = new javax.swing.GroupLayout(gsimulacion);
        gsimulacion.setLayout(gsimulacionLayout);
        gsimulacionLayout.setHorizontalGroup(
            gsimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 926, Short.MAX_VALUE)
        );
        gsimulacionLayout.setVerticalGroup(
            gsimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 591, Short.MAX_VALUE)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panel_controles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(graficas_simulacion, javax.swing.GroupLayout.PREFERRED_SIZE, 934, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(graficas_simulacion, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(panel_controles, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

       k = transformarDouble(k_input.getText());
       m = transformarDouble(m_input.getText());
       Pi = transformarDouble(x0_input.getText());
       Vi = transformarDouble(v0_input.getText());

       Modelo modelo = new Modelo(k,m,Pi,Vi);
       modelo.simular(TINICIO,TFIN,DT);

       posicion = modelo.getPosicion();
       velocidad = modelo.getVelocidad();
       aceleracion = modelo.getAceleracion();
       muelle_simulacion = modelo.getSimulacionMuelle();
       L = modelo.getLongitudMuelle();
       Adiente = modelo.getAmplitudDientes();


       grafposicion = new Grafica(posicion,"Gráfica posición","Posición","Tiempo","Posición",false,Color.red,1,true);
       grafposicion.agregarGrafica(lineacero_puntos, "referencia", Color.gray,1,true);

       grafvelocidad = new Grafica(velocidad,"Gráfica velocidad","Velocidad","Tiempo","Velocidad",false,Color.blue,1,true);
       grafvelocidad.agregarGrafica(lineacero_puntos, "referencia", Color.gray,1,true);

       grafaceleracion = new Grafica(aceleracion,"Gráfica aceleración","Aceleración","Tiempo","Aceleración",false,Color.green,1,true);
       grafaceleracion.agregarGrafica(lineacero_puntos, "referencia", Color.gray,1,true);
       
       gposicion.actualizaGrafica(grafposicion);
       gvelocidad.actualizaGrafica(grafvelocidad);
       gaceleracion.actualizaGrafica(grafaceleracion);

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
        caja[1][1] = new Point2D.Double(pos_actual+6*Adiente,Adiente);

        caja[2][0] = new Point2D.Double(pos_actual+6*Adiente,Adiente);
        caja[2][1] = new Point2D.Double(pos_actual+6*Adiente,-Adiente);

        caja[3][0] = new Point2D.Double(pos_actual+6*Adiente,-Adiente);
        caja[3][1] = new Point2D.Double(pos_actual,-Adiente);

        caja[4][0] = new Point2D.Double(pos_actual,Adiente);
        caja[4][1] = new Point2D.Double(pos_actual+6*Adiente,-Adiente);

        caja[5][0] = new Point2D.Double(pos_actual+6*Adiente,Adiente);
        caja[5][1] = new Point2D.Double(pos_actual,-Adiente);

        return caja;
    }

    /**
     * Obtiene la instantánea actual de la simulación del muelle.
     * @return Conjunto de puntos que representa el estado actual de la simulación del muelle.
     */
    private Point2D[] getFuncionSimulacion() {
        Point2D[] func_simulacion = muelle_simulacion.get(i);

        i = (i+1)%muelle_simulacion.size();

        return func_simulacion;
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bpausa_continua;
    private javax.swing.JButton bsimular;
    private JPanelGrafica gaceleracion;
    private JPanelGrafica gposicion;
    private javax.swing.JTabbedPane graficas_simulacion;
    private JPanelGrafica gsimulacion;
    private JPanelGrafica gvelocidad;
    private javax.swing.JLabel k_etiq;
    private javax.swing.JTextField k_input;
    private javax.swing.JLabel m_etiq;
    private javax.swing.JTextField m_input;
    private javax.swing.JPanel panel_botones;
    private javax.swing.JPanel panel_controles;
    private javax.swing.JPanel panel_graficas;
    private javax.swing.JPanel panel_input;
    private javax.swing.JPanel panel_simulacion;
    private javax.swing.JPanel panelgaceleracion;
    private javax.swing.JPanel panelgposicion;
    private javax.swing.JPanel panelgvelocidad;
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
     * Contiene la gráfica de la velocidad.
     */
    private Grafica grafvelocidad;
    /**
     * Contiene la gráfica de la aceleracion.
     */
    private Grafica grafaceleracion;

    private Point2D[] lineacero_puntos = {new Point2D.Double(0.0,0.0),new Point2D.Double(31,0.0)};

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
     * Conjunto de puntos con la evolución temporal de la gráfica de la velocidad.
     */
    private Point2D[] velocidad;
    
     /**
     * Conjunto de puntos con la evolución temporal de la gráfica de la aceleracion.
     */
    private Point2D[] aceleracion;

    /**
     * Tiempo de inicio de la simulación.
     */
    private final double TINICIO = 0.0;
    
    /**
     * Tiempo total de la simulación (en el applet).
     */
    private int TSIMULACION = 30000;
    
    /**
     * Tiempo de fin de la simulación.
     */
    private final double TFIN = 30.0;
    
    /**
     * Incremento de tiempo en la simulación.
     */
    private final double DT = 0.1;
    
    /**
     * Indica si la simulación ha terminado.
     */
    private boolean fin = false;
    
    /**
     * Constante elástica del muelle.
     */
    private double k = 1.0;
    
    /**
     * Masa del cuerpo enganchado al resorte.
     */
    private double m = 0.5;
    
    /**
     * Posición inicial del cuerpo enganchado al resorte.
     */
    private double Pi = 5.0;
    
    /**
     * Velocidad inicial del cuerpo enganchado al resorte.
     */
    private double Vi = 1.0;
    
     /**
     * Longitud del muelle.
     */
    private double L;
    
    /**
     * Altura de cada uno de los picos del muelle.
     */
    private double Adiente;

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
        Point2D[][] bloque;

        while(!fin) {
            instantanea_muelle = getFuncionSimulacion();
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
            
            simulacion.fijaRango(-21, 21, 0,0);
            simulacion.fijaRango(-4, 4,0, 1);
            
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
