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

package com.migueljl.solidorigido.planoinclinado;

// Referencias utilizadas.

import java.awt.Color;
import java.awt.Transparency;
import java.awt.geom.Point2D;
import  com.raccoon.easyjchart.*;

/**
 * Clase que representa a la aplicación (Applet) que ejecutará el experimento virtual
 * 
 * @author Miguel Jiménez López
 */

public class applet1 extends javax.swing.JApplet {

    /**
     * Conjunto de puntos que contiene la evolución temporal de la posición del cuerpo.
     */
    private Point2D[] p;
    /**
     * Conjunto de puntos que contiene la evolución temporal de la velocidad del cuerpo.
     */
    private Point2D[] v;
    /**
     * Conjunto de puntos que contiene la evolución temporal de la energía cinética de rotación del cuerpo.
     */
    private Point2D[] ecr;
    /**
     * Conjunto de puntos que contiene la evolución temporal de la energía cinética de traslación del cuerpo.
     */
    private Point2D[] ect;
    /**
     * Conjunto de puntos que contiene la evolución temporal de la energía potencial del cuerpo.
     */
    private Point2D[] ep;
    /**
     * Conjunto de puntos que contiene la evolución temporal del ángulo de rotación del cuerpo.
     */
    private Point2D[] angulo;
    /**
     * Constate de gravedad.
     */
    private double G = 9.8;
    /**
     * Masa del cuerpo.
     */
    private double M = 1;
    /**
     * Longitud del plano.
     */
    private double L = 100;
    /**
     * �?ngulo de inclinación del plano.
     */
    private double theta = 45;
    /**
     * La constante de proporcionalidad entre el momento de inercia y el producto de la masa por el radio al cuadrado
     * (I = alpha*M*R^2).
     */
    private double alpha = 0.5;
    /**
     * Radio del cuerpo.
     */
    private double R = 1;
    /**
     * Incremento de tiempo (simulación).
     */
    private double dt = 0.1;
    /**
     * Vector con las constantes para alpha según el tipo de cuerpo.
     */
    private final double[] alpha_array = {0.5,0.4,1.0};

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
    private void initComponents() {

        Modelo modelo = new Modelo(G,M,(theta*Math.PI)/180.0,alpha,R,L);

        modelo.simular(0,10,dt);

        p = modelo.getPosicion();
        v = modelo.getVelocidad();
        ect = modelo.getEnergiaCineticaTraslacion();
        ecr = modelo.getEnergiaCineticaRotacion();
        ep = modelo.getEnergiaPotencial();
        angulo = modelo.getAnguloSinNormalizar();

        gposicion = new Grafica(p,"Evolución temporal de la posición","Posicion","Tiempo (s)","Posición (m)",false,Color.RED,1f,true);
        gangulo = new Grafica(angulo,"Evolución temporal del ángulo de giro del cuerpo","Velocidad de giro","Tiempo (s)","�?ngulo (grados)",false,Color.RED,1f,true);
        gvelocidad = new Grafica(v,"Evolución temporal de la velocidad","Velocidad lineal","Tiempo (s)","Velocidad (m/s)",false,Color.RED,1f,true);
        gep = new Grafica(ep,"Evolución temporal de la energía potencial","Energía potencial","Tiempo (s)","Energía potencial (J)",false,Color.RED,1f,true);
        gect = new Grafica(ect,"Evolución temporal de la energía cinética de traslación","Energía cinética de traslación","Tiempo (s)","Energía cinética de traslación (J)",false,Color.RED,1f,true);
        gecr = new Grafica(ecr,"Evolución temporal de la energía cinética de rotación","Energía cinética de rotación","Tiempo (s)","Energía cinética de rotación (J)",false,Color.RED,1f,true);

        panel_graficas = new javax.swing.JTabbedPane();
        panel_posicion = new JPanelGrafica(gposicion);
        panel_angulo = new JPanelGrafica(gangulo);
        panel_velocidad = new JPanelGrafica(gvelocidad);
        panel_ep = new JPanelGrafica(gep);
        panel_ect = new JPanelGrafica(gect);
        panel_ecr = new JPanelGrafica(gecr);
        panel_animacion = new javax.swing.JPanel();
        psimulacion = new JPanelAnimacion();
        panel_control = new javax.swing.JPanel();
        bsimular = new javax.swing.JButton();
        masa_input = new javax.swing.JTextField();
        angulo_input = new javax.swing.JTextField();
        longitud_input = new javax.swing.JTextField();
        tipo_input = new javax.swing.JComboBox();
        masa_label = new javax.swing.JLabel();
        angulo_label = new javax.swing.JLabel();
        longitud_label = new javax.swing.JLabel();
        tipo_label = new javax.swing.JLabel();
        tinicio_label = new javax.swing.JLabel();
        tinicio_input = new javax.swing.JTextField();
        tfin_label = new javax.swing.JLabel();
        tfin_input = new javax.swing.JTextField();

        panel_graficas.setBackground(new java.awt.Color(255, 255, 255));
        panel_graficas.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        panel_posicion.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panel_posicionLayout = new javax.swing.GroupLayout(panel_posicion);
        panel_posicion.setLayout(panel_posicionLayout);
        panel_posicionLayout.setHorizontalGroup(
            panel_posicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
        );
        panel_posicionLayout.setVerticalGroup(
            panel_posicionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );

        panel_graficas.addTab("Posicion", panel_posicion);

        panel_angulo.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panel_anguloLayout = new javax.swing.GroupLayout(panel_angulo);
        panel_angulo.setLayout(panel_anguloLayout);
        panel_anguloLayout.setHorizontalGroup(
            panel_anguloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
        );

        panel_anguloLayout.setVerticalGroup(
            panel_anguloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );

        panel_graficas.addTab("Angulo", panel_angulo);

        panel_velocidad.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panel_velocidadLayout = new javax.swing.GroupLayout(panel_velocidad);
        panel_velocidad.setLayout(panel_velocidadLayout);
        panel_velocidadLayout.setHorizontalGroup(
            panel_velocidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
        );
        panel_velocidadLayout.setVerticalGroup(
            panel_velocidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );

        panel_graficas.addTab("Velocidad", panel_velocidad);

        panel_ep.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panel_epLayout = new javax.swing.GroupLayout(panel_ep);
        panel_ep.setLayout(panel_epLayout);
        panel_epLayout.setHorizontalGroup(
            panel_epLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
        );
        panel_epLayout.setVerticalGroup(
            panel_epLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );

        panel_graficas.addTab("Energia Potencial", panel_ep);

        panel_ect.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panel_ectLayout = new javax.swing.GroupLayout(panel_ect);
        panel_ect.setLayout(panel_ectLayout);
        panel_ectLayout.setHorizontalGroup(
            panel_ectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
        );
        panel_ectLayout.setVerticalGroup(
            panel_ectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );

        panel_graficas.addTab("Energia cinetica traslacion", panel_ect);

        panel_ecr.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panel_ecrLayout = new javax.swing.GroupLayout(panel_ecr);
        panel_ecr.setLayout(panel_ecrLayout);
        panel_ecrLayout.setHorizontalGroup(
            panel_ecrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 903, Short.MAX_VALUE)
        );
        panel_ecrLayout.setVerticalGroup(
            panel_ecrLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 447, Short.MAX_VALUE)
        );

        panel_graficas.addTab("Energia cinetica rotacion", panel_ecr);

        panel_animacion.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout psimulacionLayout = new javax.swing.GroupLayout(psimulacion);
        psimulacion.setLayout(psimulacionLayout);
        psimulacionLayout.setHorizontalGroup(
            psimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 883, Short.MAX_VALUE)
        );
        psimulacionLayout.setVerticalGroup(
            psimulacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 425, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_animacionLayout = new javax.swing.GroupLayout(panel_animacion);
        panel_animacion.setLayout(panel_animacionLayout);
        panel_animacionLayout.setHorizontalGroup(
            panel_animacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_animacionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(psimulacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_animacionLayout.setVerticalGroup(
            panel_animacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_animacionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(psimulacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panel_graficas.addTab("Animacion", panel_animacion);

        panel_control.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bsimular.setText("Simular");
        bsimular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimularActionPerformed(evt);
            }
        });

        masa_input.setText("1");

        angulo_input.setText("45");

        longitud_input.setText("100");

        tipo_input.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cilindro", "Esfera", "Aro" }));

        masa_label.setText("Masa del cuerpo:");

        angulo_label.setText("�?ngulo del plano:");

        longitud_label.setText("Longitud del plano:");

        tipo_label.setText("Tipo de cuerpo:");

        tinicio_label.setText("Tiempo inicio:");

        tinicio_input.setText("0");

        tfin_label.setText("Tiempo fin:");

        tfin_input.setText("10");

        javax.swing.GroupLayout panel_controlLayout = new javax.swing.GroupLayout(panel_control);
        panel_control.setLayout(panel_controlLayout);
        panel_controlLayout.setHorizontalGroup(
            panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_controlLayout.createSequentialGroup()
                .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_controlLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panel_controlLayout.createSequentialGroup()
                                .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(masa_label)
                                    .addComponent(angulo_label))
                                .addGap(26, 26, 26))
                            .addGroup(panel_controlLayout.createSequentialGroup()
                                .addComponent(longitud_label)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_controlLayout.createSequentialGroup()
                                .addComponent(tipo_label)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_controlLayout.createSequentialGroup()
                                .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(longitud_input)
                                    .addComponent(angulo_input)
                                    .addComponent(masa_input, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                                .addGap(87, 87, 87)
                                .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tinicio_label, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfin_label))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tfin_input)
                                    .addComponent(tinicio_input, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)))
                            .addComponent(tipo_input, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panel_controlLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(bsimular, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(432, Short.MAX_VALUE))
        );
        panel_controlLayout.setVerticalGroup(
            panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_controlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_controlLayout.createSequentialGroup()
                        .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tinicio_label)
                            .addComponent(tinicio_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfin_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfin_label)))
                    .addGroup(panel_controlLayout.createSequentialGroup()
                        .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(masa_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(masa_label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(angulo_label)
                            .addComponent(angulo_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(longitud_label)
                            .addComponent(longitud_input, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel_controlLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(tipo_label))
                            .addGroup(panel_controlLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(tipo_input, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(37, 37, 37)
                        .addComponent(bsimular, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panel_control, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(panel_graficas, javax.swing.GroupLayout.DEFAULT_SIZE, 912, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panel_graficas, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panel_control, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    /**
     * Permite comenzar de nuevo la simulación
     * @param evt Evento con la información necesaria
     */
    private void bsimularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimularActionPerformed
       // TODO add your handling code here:
        double tinicial;
        double tfinal;


        tinicial = transformarDouble(tinicio_input.getText());
        tfinal = transformarDouble(tfin_input.getText());


        int tipo_objeto = this.tipo_input.getSelectedIndex();

        if(tipo_objeto != -1)
            this.alpha = this.alpha_array[tipo_objeto];
        else
            this.alpha = this.alpha_array[0];

        this.theta = transformarDouble(angulo_input.getText());
        this.L = transformarDouble(longitud_input.getText());
        this.M = transformarDouble(masa_input.getText());

        Modelo modelo = new Modelo(G,M,(theta*Math.PI)/180.0,alpha,R,L);

        modelo.simular(tinicial,tfinal,dt);

        p = modelo.getPosicion();
        v = modelo.getVelocidad();
        ect = modelo.getEnergiaCineticaTraslacion();
        ecr = modelo.getEnergiaCineticaRotacion();
        ep = modelo.getEnergiaPotencial();
        angulo = modelo.getAnguloSinNormalizar();

        gposicion = new Grafica(p,"Evolución temporal de la posición","Posicion","Tiempo (s)","Posición (m)",false,Color.RED,1f,true);
        gangulo = new Grafica(angulo,"Evolución temporal del ángulo de giro del cuerpo","Velocidad de giro","Tiempo (s)","�?ngulo (grados)",false,Color.RED,1f,true);
        gvelocidad = new Grafica(v,"Evolución temporal de la velocidad","Velocidad lineal","Tiempo (s)","Velocidad (m/s)",false,Color.RED,1f,true);
        gep = new Grafica(ep,"Evolución temporal de la energía potencial","Energía potencial","Tiempo (s)","Energía potencial (J)",false,Color.RED,1f,true);
        gect = new Grafica(ect,"Evolución temporal de la energía cinética de traslación","Energía cinética de traslación","Tiempo (s)","Energía cinética de traslación (J)",false,Color.RED,1f,true);
        gecr = new Grafica(ecr,"Evolución temporal de la energía cinética de rotación","Energía cinética de rotación","Tiempo (s)","Energía cinética de rotación (J)",false,Color.RED,1f,true);

        panel_posicion.actualizaGrafica(gposicion);
        panel_angulo.actualizaGrafica(gangulo);
        panel_velocidad.actualizaGrafica(gvelocidad);
        panel_ep.actualizaGrafica(gep);
        panel_ect.actualizaGrafica(gect);
        panel_ecr.actualizaGrafica(gecr);

       psimulacion.comenzarSimulacion(p,angulo,L);

        this.update(this.getGraphics());

    }

    private javax.swing.JTextField angulo_input;
    private javax.swing.JLabel angulo_label;
    private javax.swing.JButton bsimular;
    private javax.swing.JTextField longitud_input;
    private javax.swing.JLabel longitud_label;
    private javax.swing.JTextField masa_input;
    private javax.swing.JLabel masa_label;
    private javax.swing.JPanel panel_animacion;
    private javax.swing.JPanel panel_control;
    private javax.swing.JTabbedPane panel_graficas;
    private JPanelAnimacion psimulacion;
    private javax.swing.JTextField tfin_input;
    private javax.swing.JLabel tfin_label;
    private javax.swing.JTextField tinicio_input;
    private javax.swing.JLabel tinicio_label;
    private javax.swing.JComboBox tipo_input;
    private javax.swing.JLabel tipo_label;

    /**
     * Panel gráfico donde se visualiza la evolución de la posición.
     */
    private JPanelGrafica panel_posicion;
    /**
     * Panel gráfico donde se visualiza la evolución del ángulo.
     */
    private JPanelGrafica panel_angulo;
    /**
     * Panel gráfico donde se visualiza la evolución de la velocidad.
     */
    private JPanelGrafica panel_velocidad;
    /**
     * Panel gráfico donde se visualiza la evolución de la energía cinética de rotación.
     */
    private JPanelGrafica panel_ecr;
    /**
     * Panel gráfico donde se visualiza la evolución de la energía cinética de traslación.
     */
    private JPanelGrafica panel_ect;
    /**
     * Panel gráfico donde se visualiza la evolución de la energía potencial.
     */
    private JPanelGrafica panel_ep;
	
    
    /**
     * Gráfica que contiene la evolución temporal de la posición.
     */
    private Grafica gposicion = null;
    
    /**
     * Gráfica que contiene la evolución temporal del ángulo.
     */
    private Grafica gangulo = null;
    
    /**
     * Gráfica que contiene la evolución temporal de la velocidad.
     */
    private Grafica gvelocidad = null;
    
    /**
     * Gráfica que contiene la evolución temporal de la energía potencial.
     */
    private Grafica gep = null;
    
    /**
     * Gráfica que contiene la evolución temporal de la energía cinética de traslación.
     */
    private Grafica gect = null;
    
    
    /**
     * Gráfica que contiene la evolución temporal de la energía cinética de rotación.
     */
    private Grafica gecr = null;
    
}
