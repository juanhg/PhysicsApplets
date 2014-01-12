package com.migueljl.ondas.reflextrans;

// Recursos necesarios.

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.migueljl.ondas.reflextrans.iModelo;

/**
 * Clase que representa al modelo del fenómeno físico del experimento virtual.
 * Fenómeno: Reflexión y transmisión de ondas.
 * @author Miguel Jiménez López
 */

public class Modelo implements iModelo {
    /**
     * Frecuencia de la onda incidente.
     */
    private Double frec = null;
    /**
     * Velocidad de la onda en el medio 1.
     */
    private Double c1 = null;
    /**
     * Velocidad de la onda en el medio 2.
     */
    private Double c2 = null;
    /**
     * Densidad del medio 1.
     */
    private Double ro1 = null;
    /**
     * Densidad del medio 2.
     */
    private Double ro2 = null;
    /**
     * Tensión de las cuerdas.
     */
    private Double T = null;
    /**
     * Amplitud máxima de la onda incidente.
     */
    private Double A1 = new Double(10);
    /**
     * Número de ondas en el medio 1.
     */
    private Double k1 = null;
    /**
     * Número de ondas en el medio 2.
     */
    private Double k2 = null;
    /**
     * Impedancia mecánica del medio 1.
     */
    private Double z1 = null;
    /**
     * Impedancia mecánica del medio 2.
     */
    private Double z2 = null;
    /**
     * Frecuencia angular de la onda incidente.
     */
    private Double w = null;

    /**
     * Conjunto de puntos con la evolución temporal de la onda incidente.
     */
    private ArrayList<ArrayList<Point2D>> onda_incidente = null;
    /**
     * Conjunto de puntos con la evolución temporal de la onda reflejada.
     */
    private ArrayList<ArrayList<Point2D>> onda_reflejada = null;
    /**
     * Conjunto de puntos con la evolución temporal de la onda resultante.
     */
    private ArrayList<ArrayList<Point2D>> onda_superposicion = null;
    /**
     * Conjunto de puntos con la evolución temporal de la onda transmitida.
     */
    private ArrayList<ArrayList<Point2D>> onda_transmitida = null;

    /**
     * Tiempo actual de simulación.
     */
    private double t_actual = 0.0;
    
    /**
     * Tiempo de inicio de simulación.
     */
    private double t_inicio = 0.0;
    
    /**
     * Tiempo de fin de simulación.
     */
    private double t_final = 0.0;
    
    /**
     * Incremento de tiempo.
     */
    private double dt = 0.0;
    
    /**
     * Incremento de longitud (espacio).
     */
    private double dx = 0.0;

    /**
     * Constructor de la clase.
     * @param frecuencia Frecuencia de la onda incidente.
     * @param densidad_cuerda1 Densidad del medio 1.
     * @param densidad_cuerda2 Densidad del medio 2.
     * @param tension Tensión de las cuerdas.
     */
    public Modelo(double frecuencia, double densidad_cuerda1, double densidad_cuerda2, double tension) {
        double velocidad_cuerda1 = 0.0;
        double velocidad_cuerda2 = 0.0;
        
        velocidad_cuerda1 = Math.sqrt(tension/densidad_cuerda1);
        velocidad_cuerda2 = Math.sqrt(tension/densidad_cuerda2);
        
        frec = new Double(frecuencia);
        ro1 = new Double(densidad_cuerda1);
        ro2 = new Double(densidad_cuerda2);
        c1 = new Double(velocidad_cuerda1);
        c2 = new Double(velocidad_cuerda2);
        T = new Double(tension);
        
        w = new Double(2*Math.PI*frecuencia);
        k1 = new Double(w.doubleValue()/velocidad_cuerda1);
        k2 = new Double(w.doubleValue()/velocidad_cuerda2);
            
        z2 = new Double(tension/velocidad_cuerda2);    
        z1 = new Double(tension/velocidad_cuerda1);

        onda_incidente = new ArrayList();
        onda_reflejada = new ArrayList();
        onda_superposicion = new ArrayList();
        onda_transmitida = new ArrayList();
    }

    /**
     * Consulta la frecuencia angular de la onda incidente.
     * @return La frecuencia angular de la onda incidente.
     */
    public double getFrecuenciaAngular() {
        return w.doubleValue();
    }
    
    /**
     * Consulta la frecuencia lineal de la onda incidente.
     * @return La frecuencia lineal de la onda incidente.
     */
    public double getFrecuenciaLineal() {
        return frec.doubleValue();
    }

    /**
     * Consulta la velocidad de la onda en la cuerda 1.
     * @return La velocidad de la onda en la cuerda 1.
     */
    public double getVelocidadCuerda1() {
        return c1.doubleValue();
    }
    
    /**
     * Consulta la velocidad de la onda en la cuerda 2.
     * @return La velocidad de la onda en la cuerda 2.
     */
    public double getVelocidadCuerda2() {
        return c2.doubleValue();
    }
    
    
    /**
     * Consulta la densidad de la onda en la cuerda 1.
     * @return La densidad de la onda en la cuerda 1.
     */
    public double getDensidadCuerda1() {
        return ro1.doubleValue();
    }
    
    
    /**
     * Consulta la densidad de la onda en la cuerda 2.
     * @return La densidad de la onda en la cuerda 2.
     */
    public double getDensidadCuerda2() {
        return ro2.doubleValue();
    }

    /**
     * Consulta la tensión de las cuerdas.
     * @return La tensión de las cuerdas.
     */
    public double getTension() {
        return T.doubleValue();
    }

    /**
     * Consulta la evolución temporal de la onda incidente.
     * @return Una lista de puntos con la onda incidente.
     */
      public ArrayList<Point2D[]> getOndaIncidente() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> oi = null;

          oi = new ArrayList();

          for(int i = 0 ; i < onda_incidente.size() ; i++) {
              aux = new Point2D[onda_incidente.get(i).size()];
              for(int j = 0 ; j < onda_incidente.get(i).size() ; j++)
                  aux[j] = onda_incidente.get(i).get(j);
              oi.add(aux);

          }

          return oi;

        }
      
      
    /**
     * Consulta la evolución temporal de la onda reflejada.
     * @return Una lista de puntos con la onda reflejada.
     */
      public ArrayList<Point2D[]> getOndaReflejada() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> or = null;

          or = new ArrayList();

          for(int i = 0 ; i < onda_reflejada.size() ; i++) {
              aux = new Point2D[onda_reflejada.get(i).size()];
              for(int j = 0 ; j < onda_reflejada.get(i).size() ; j++)
                  aux[j] = onda_reflejada.get(i).get(j);
              or.add(aux);

          }

          return or;

        }
      
      
    /**
     * Consulta la evolución temporal de la onda transmitida.
     * @return Una lista de puntos con la onda transmitida.
     */
      public ArrayList<Point2D[]> getOndaTransmitida() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> ot = null;

          ot = new ArrayList();

          for(int i = 0 ; i < onda_transmitida.size() ; i++) {
              aux = new Point2D[onda_transmitida.get(i).size()];
              for(int j = 0 ; j < onda_transmitida.get(i).size() ; j++)
                  aux[j] = onda_transmitida.get(i).get(j);
              ot.add(aux);

          }

          return ot;

        }
      
      
    /**
     * Consulta la evolución temporal de la onda resultante.
     * @return Una lista de puntos con la onda resultante.
     */
      public ArrayList<Point2D[]> getOndaSuperposicion() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> os = null;

          os = new ArrayList();

          for(int i = 0 ; i < onda_superposicion.size() ; i++) {
              aux = new Point2D[onda_superposicion.get(i).size()];
              for(int j = 0 ; j < onda_superposicion.get(i).size() ; j++)
                  aux[j] = onda_superposicion.get(i).get(j);
              os.add(aux);

          }

          return os;

        }


      /**
       * Actualiza la evolución temporal de la onda incidente.
       */
       private void actualizarOndaIncidente() {
           ArrayList<Point2D> oi_actual = null;
           double waux = w.doubleValue();
           double k1aux = k1.doubleValue();
           double A1aux = A1.doubleValue();
           double c1aux = c1.doubleValue();
           double oi_amp;
           int cont = 0;
           double inicio_x = -20;
           double fin_x = 0;
           double actual_x = inicio_x;

           oi_actual = new ArrayList();

           while(actual_x <= fin_x) {
               if(c1aux != 0)
                    oi_amp = A1aux*Math.cos(waux*t_actual-k1aux*actual_x);
               else
                   oi_amp = 0;
               
               oi_actual.add(new Point2D.Double(actual_x,oi_amp));
               actual_x += dx;
               cont++;
           }

           onda_incidente.add(oi_actual);
       }
       
       
      /**
       * Actualiza la evolución temporal de la onda reflejada.
       */
       private void actualizarOndaReflejada() {
           ArrayList<Point2D> or_actual = null;
           double waux = w.doubleValue();
           double k1aux = k1.doubleValue();
           double z1aux = z1.doubleValue();
           double z2aux = z2.doubleValue();
           double A1aux = A1.doubleValue();
           double c2aux = c2.doubleValue();
           double c1aux = c1.doubleValue();
           double or_amp;
           int cont = 0;
           double inicio_x = -20;
           double fin_x = 0;
           double actual_x = inicio_x;

           or_actual = new ArrayList();

           while(actual_x <= fin_x) {
               if(c2aux != 0)
                    or_amp = ((z1aux-z2aux)/(z1aux+z2aux))*A1aux*Math.cos(waux*t_actual+k1aux*actual_x);
               else
                   if(c1aux != 0)
                        or_amp = -A1aux*Math.cos(waux*t_actual+k1aux*actual_x);
                   else
                       or_amp = 0;
               
               or_actual.add(new Point2D.Double(actual_x,or_amp));
               actual_x += dx;
               cont++;
           }

           onda_reflejada.add(or_actual);
       }
       
       
      /**
       * Actualiza la evolución temporal de la onda transmitida.
       */
       private void actualizarOndaTransmitida() {
           ArrayList<Point2D> ot_actual = null;
           double waux = w.doubleValue();
           double k2aux = k2.doubleValue();
           double z1aux = z1.doubleValue();
           double z2aux = z2.doubleValue();
           double A1aux = A1.doubleValue();
           double c2aux = c2.doubleValue();
           double c1aux = c1.doubleValue();
           double ot_amp;
           int cont = 0;
           double inicio_x = 0;
           double fin_x = 20;
           double actual_x = inicio_x;

           ot_actual = new ArrayList();

           while(actual_x <= fin_x) {
               if(c2aux != 0 && c1aux != 0)
                    ot_amp = ((2*z1aux)/(z1aux+z2aux))*A1aux*Math.cos(waux*t_actual-k2aux*actual_x);
               else
                    ot_amp = 0;
               
               ot_actual.add(new Point2D.Double(actual_x,ot_amp));
               actual_x += dx;
               cont++;
           }

           onda_transmitida.add(ot_actual);
       }
       
       
      /**
       * Actualiza la evolución temporal de la onda resultante.
       */
       private void actualizarOndaSuperposicion() {
           ArrayList<Point2D> os_actual = null;
           double actual_x;
           double os_amp;
           double oi_amp;
           double or_amp;
           
           actualizarOndaIncidente();
           actualizarOndaReflejada();
           
           
           int ultimo = onda_incidente.size()-1;
           int tam_array_onda = onda_incidente.get(ultimo).size();
           
           os_actual = new ArrayList();
           
           for(int j = 0 ; j < tam_array_onda ; j++) {
               actual_x = onda_incidente.get(ultimo).get(j).getX();
               oi_amp = onda_incidente.get(ultimo).get(j).getY();
               or_amp = onda_reflejada.get(ultimo).get(j).getY();
               
               os_amp = oi_amp+or_amp;
               
               os_actual.add(new Point2D.Double(actual_x,os_amp));
           }
           
           onda_superposicion.add(os_actual);
       }
       
       /**
       * Actualiza la evolución temporal de las ondas implicadas en el fenómeno físico. 
       * Agrega un punto nuevo cada vez que se realiza una llamada.
       */
       private void actualizar() {
           actualizarOndaSuperposicion();
           actualizarOndaTransmitida();
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
           this.onda_incidente.clear();
           this.onda_reflejada.clear();
           this.onda_superposicion.clear();
           this.onda_transmitida.clear();
           
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
