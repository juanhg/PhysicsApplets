package com.migueljl.ondas.cuerda;

import java.awt.geom.Point2D;

/**
 * Clase que representa al modelo del fenómeno físico del experimento virtual.
 * Fenómeno: Cuerda que tiene una cuenta en su posición central (x = 0)
 * @author Miguel Jiménez López
 */

import java.util.ArrayList;

import com.migueljl.ondas.cuerda.iModelo;

public class Modelo implements iModelo {
    /** 
     * Frecuencia de la onda que se propaga en la cuerda.
     */
    private Double w = null;
    /**
     * Velocidad de propagación de la onda.
     */
    private Double c = null;
    /**
     * Masa de la cuenta.
     */
    private Double M = null;
    /**
     * Tensión a la que está sometida la cuerda.
     */
    private Double T = null;

    
    /**
     * Función que representa la evolución temporal de la onda en la cuerda.
     */
    private ArrayList<ArrayList<Point2D>> amplitud = null;

    /**
     * Valores temporales y espaciales (necesarios para simulación).
     * Tiempo actual de simulación.
     */
    private double t_actual = 0.0;
    /**
     * Tiempo de inicio de la simulación.
     */
    private double t_inicio = 0.0;
    /**
     * Tiempo de fin de la simulación.
     */
    private double t_final = 0.0;
    /**
     * Incremento de tiempo.
     */
    private double dt = 0.0;
    /**
     * Incremento de longitud (sobre la cuerda).
     */
    private double dx = 0.0;

    /**
     * Constructor de clase.
     * @param frecuencia Frecuencia de la onda
     * @param velocidad_cuerda Velocidad de la onda en la cuerda
     * @param masa_cuenta Masa de la cuenta
     * @param tension Tensión de la cuerda
     */
    
    public Modelo(double frecuencia, double velocidad_cuerda, double masa_cuenta, double tension) {
        w = new Double(frecuencia);
        c = new Double(velocidad_cuerda);
        M = new Double(masa_cuenta);
        T = new Double(tension);

        amplitud = new ArrayList();
    }
    
    /**
     * Consulta la frecuencia de la onda que se propaga por la cuerda.
     * @return Devuelve un real que contiene la frecuencia de la onda.
     */

    public double getFrecuencia() {
        return w.doubleValue();
    }

    /**
     * Consulta la velocidad de la onda en la cuerda
     * @return Devuelve un real que contiene la velocidad de la onda en la cuerda.
     */
    
    public double getVelocidad() {
        return c.doubleValue();
    }

    /**
     * Consulta la masa de la cuenta.
     * @return Devuelve un real que contiene la masa de la cuenta.
     */
    
    public double getMasaCuenta() {
        return M.doubleValue();
    }

    /**
     * Consulta la tensión de la cuerda.
     * @return Devuelve un real con la tensión de la cuerda.
     */
    
    public double getTension() {
        return T.doubleValue();
    }


    /**
     * Consulta la evolución temporal de la oscilación en la cuerda.
     * @return Una lista de puntos con la evolución temporal de la oscilación.
     */
    
      public ArrayList<Point2D[]> getAmplitud() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> amplitudes = null;

          amplitudes = new ArrayList();

          for(int i = 0 ; i < amplitud.size() ; i++) {
              aux = new Point2D[amplitud.get(i).size()];
              for(int j = 0 ; j < amplitud.get(i).size() ; j++)
                  aux[j] = amplitud.get(i).get(j);
              amplitudes.add(aux);

          }

          return amplitudes;

        }

      /**
       * Actualiza la evolución temporal de la oscilación en la cuerda. Agrega un punto nuevo cada vez que se realiza una llamada.
       */

       private void actualizar() {
           ArrayList<Point2D> actual = null;
           double waux = w.doubleValue();
           double caux = c.doubleValue();
           double maux = M.doubleValue();
           double taux = T.doubleValue();
           double q = (maux*waux*caux)/(2*taux);
           double k = waux/caux;
           double phi1 = Math.atan(1/q);
           double phi2 = Math.atan(-q);
           double amplitud_aux;
           int cont = 0;
           double inicio_x = -10;
           double fin_x = 10;
           double actual_x = inicio_x;

           actual = new ArrayList();

           while(actual_x <= fin_x) {
               if(actual_x < 0)
                    amplitud_aux = Math.cos((waux*t_actual)-(k*actual_x)) - (q/(Math.sqrt(1+(q*q))))
                            *Math.cos((waux*t_actual)+(k*actual_x)+phi1);
               else
                    amplitud_aux = (1/(Math.sqrt(1+(q*q))))*Math.cos((waux*t_actual)-(k*actual_x)+phi2);
               
               actual.add(new Point2D.Double(actual_x,amplitud_aux));
               actual_x += dx;
               cont++;
           }

           amplitud.add(actual);
       }
       
       /**
        * Función que actualiza el modelo a lo largo del tiempo.
        */

       private void actualizarModelo() {

           while(t_actual <= t_final) {
                actualizar();
                t_actual += dt;
           }
       }

       /**
        * Inicializa la simulación.
        */
       
       private void inicializarSimulacion() {
           this.amplitud.clear();
           
       }
       
       /**
        * Función que realiza la simulación temporal del fenómeno físico.
        * @param tinicial Tiempo de inicio de la simulación.
        * @param tfinal Tiempo final de la simulación.
        * @param dt Incremento de tiempo.
        * @param dx Incremento de longitud (sobre la cuerda).
        */

    public void simular(double tinicial, double tfinal, double dt, double dx) {

        inicializarSimulacion();

        t_inicio = tinicial;
        t_final = tfinal;
        this.dt = dt;
        this.dx = dx;

        t_actual = t_inicio;
        actualizarModelo();
    }
}
