package com.migueljl.ondas.grupoondas;

import java.awt.geom.Point2D;


/**
 * Clase que representa al modelo del fenómeno físico del experimento virtual.
 * Fenómeno: Grupo de ondas.
 * @author Miguel Jiménez López
 */
import java.util.ArrayList;

import com.migueljl.ondas.grupoondas.iModelo;

public class Modelo implements iModelo {

    /**
     * Velocidad de la onda 1.
     */
    private Double c1 = null;
    /**
     * Velocidad de la onda 2.
     */
    private Double c2 = null;
    
    /**
     * Amplitud de las ondas.
     */
    private Double A = new Double(10);
    /**
     * Numero de ondas de la onda 1.
     */
    private Double k1 = null;
    /**
     * Numero de ondas de la onda 2.
     */
    private Double k2 = null;
    /**
     * Frecuencia de la onda 1.
     */
    private Double w1 = null;
    /**
     * Frecuencia de la onda 2.
     */
    private Double w2 = null;

    /**
     * Onda resultante (parte 1).
     */
    private ArrayList<ArrayList<Point2D>> onda_resultante1 = null;
    /**
     * Onda resultante (parte 2).
     */
    private ArrayList<ArrayList<Point2D>> onda_resultante2 = null;
    /**
     * Onda resultante (parte 3).
     */
    private ArrayList<ArrayList<Point2D>> onda_resultante3 = null;
    /**
     * Onda resultante (parte 4).
     */
    private ArrayList<ArrayList<Point2D>> onda_resultante4 = null;
    /**
     * Onda 1.
     */
    private ArrayList<ArrayList<Point2D>> onda1 = null;
    /**
     * Onda 2.
     */
    private ArrayList<ArrayList<Point2D>> onda2 = null;

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
     * Incremento de tiempo en simulación.
     */
    private double dt = 0.0;
    /**
     * Incremento de espacio en simulación.
     */
    private double dx = 0.0;
    /**
     * Límite (en valor absoluto) considerado para el espacio en simulación.
     */
    private final double RX = 5;

    /**
     * Constructor de clase.
     * @param frecuencia1 Frecuencia de la onda 1.
     * @param frecuencia2 Frecuencia de la onda 2.
     * @param velocidad1 Velocidad de la onda 1.
     * @param velocidad2 Velocidad de la onda 2.
     */
    public Modelo(double frecuencia1, double frecuencia2, double velocidad1, double velocidad2) {
      
        w1 = new Double(frecuencia1);
        w2 = new Double(frecuencia2);
        c1 = new Double(velocidad1);
        c2 = new Double(velocidad2);
        
        k1 = new Double(frecuencia1/velocidad1);
        k2 = new Double(frecuencia2/velocidad2);
     
        onda_resultante1 = new ArrayList();
        onda_resultante2 = new ArrayList();
        onda_resultante3 = new ArrayList();
        onda_resultante4 = new ArrayList();
        onda1 = new ArrayList();
        onda2 = new ArrayList();        
    }

    /**
     * Consulta la frecuencia angular de la onda 1.
     * @return La frecuencia angular de la primera onda.
     */
    public double getFrecuenciaAngular1() {
        return w1.doubleValue();
    }
    
    /**
     * Consulta la frecuencia angular de la onda 2.
     * @return La frecuencia angular de la segunda onda.
     */
    public double getFrecuenciaAngular2() {
        return w2.doubleValue();
    }
    
    /**
     * Consulta la velocidad de la onda 1.
     * @return La velocidad de la primera onda.
     */
    public double getVelocidad1() {
        return c1.doubleValue();
    }
    
    /**
     * Consulta la velocidad de la onda 2.
     * @return La velocidad de la segunda onda.
     */
    public double getVelocidad2() {
        return c2.doubleValue();
    }

    /**
     * Consulta la evolución temporal de la onda resultante (primera parte).
     * @return Una lista de puntos con la onda resultante (primera parte)
     */
      public ArrayList<Point2D[]> getOndaResultante1() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> or1 = null;

          or1 = new ArrayList();

          for(int i = 0 ; i < onda_resultante1.size() ; i++) {
              aux = new Point2D[onda_resultante1.get(i).size()];
              for(int j = 0 ; j < onda_resultante1.get(i).size() ; j++)
                  aux[j] = onda_resultante1.get(i).get(j);
              or1.add(aux);

          }

          return or1;

        }
      
       /**
     * Consulta la evolución temporal de la onda resultante (segunda parte).
     * @return Una lista de puntos con la onda resultante (segunda parte)
     */
      public ArrayList<Point2D[]> getOndaResultante2() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> or2 = null;

          or2 = new ArrayList();

          for(int i = 0 ; i < onda_resultante2.size() ; i++) {
              aux = new Point2D[onda_resultante2.get(i).size()];
              for(int j = 0 ; j < onda_resultante2.get(i).size() ; j++)
                  aux[j] = onda_resultante2.get(i).get(j);
              or2.add(aux);

          }

          return or2;

        }
      
      /**
     * Consulta la evolución temporal de la onda resultante (tercera parte).
     * @return Una lista de puntos con la onda resultante (tercera parte)
     */
      public ArrayList<Point2D[]> getOndaResultante3() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> or3 = null;

          or3 = new ArrayList();

          for(int i = 0 ; i < onda_resultante3.size() ; i++) {
              aux = new Point2D[onda_resultante3.get(i).size()];
              for(int j = 0 ; j < onda_resultante3.get(i).size() ; j++)
                  aux[j] = onda_resultante3.get(i).get(j);
              or3.add(aux);

          }

          return or3;

        }
      
       /**
     * Consulta la evolución temporal de la onda resultante (cuarta parte).
     * @return Una lista de puntos con la onda resultante (cuarta parte)
     */
        public ArrayList<Point2D[]> getOndaResultante4() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> or4 = null;

          or4 = new ArrayList();

          for(int i = 0 ; i < onda_resultante4.size() ; i++) {
              aux = new Point2D[onda_resultante4.get(i).size()];
              for(int j = 0 ; j < onda_resultante4.get(i).size() ; j++)
                  aux[j] = onda_resultante4.get(i).get(j);
              or4.add(aux);

          }

          return or4;

        }
      
         /**
     * Consulta la evolución temporal de la onda 1.
     * @return Una lista de puntos con la primera onda
     */
        
      public ArrayList<Point2D[]> getOnda1() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> o1 = null;

          o1 = new ArrayList();

          for(int i = 0 ; i < onda1.size() ; i++) {
              aux = new Point2D[onda1.get(i).size()];
              for(int j = 0 ; j < onda1.get(i).size() ; j++)
                  aux[j] = onda1.get(i).get(j);
              o1.add(aux);

          }

          return o1;

        }
      
      /**
     * Consulta la evolución temporal de la onda 2.
     * @return Una lista de puntos con la segunda onda
     */
      
      public ArrayList<Point2D[]> getOnda2() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> o2 = null;

          o2 = new ArrayList();

          for(int i = 0 ; i < onda2.size() ; i++) {
              aux = new Point2D[onda2.get(i).size()];
              for(int j = 0 ; j < onda2.get(i).size() ; j++)
                  aux[j] = onda2.get(i).get(j);
              o2.add(aux);

          }

          return o2;

        }
      
       /**
       * Actualiza la evolución temporal de la onda 1. Agrega un punto nuevo cada vez que se realiza una llamada.
       */
      private void actualizarOnda1() {
          ArrayList<Point2D> o1_actual = null;
           double waux = w1.doubleValue();
           double k1aux = k1.doubleValue();
           double A1aux = A.doubleValue();
           double c1aux = c1.doubleValue();
           double o1_amp;
           int cont = 0;
           double inicio_x = -RX*(Math.PI/k1aux)+c1aux*t_actual;
           double fin_x = RX*(Math.PI/k1aux)+c1aux*t_actual;
           double actual_x = inicio_x;

           o1_actual = new ArrayList();

           while(actual_x <= fin_x) {
               if(c1aux != 0)
                    o1_amp = A1aux*Math.cos(waux*t_actual-k1aux*actual_x);
               else
                   o1_amp = 0;
               
               o1_actual.add(new Point2D.Double(actual_x,o1_amp));
               actual_x += dx;
           }

           onda1.add(o1_actual);
      }
      
      /**
       * Actualiza la evolución temporal de la onda 2. Agrega un punto nuevo cada vez que se realiza una llamada.
       */
      private void actualizarOnda2() {
          ArrayList<Point2D> o2_actual = null;
           double waux = w2.doubleValue();
           double k2aux = k2.doubleValue();
           double A2aux = A.doubleValue();
           double c2aux = c2.doubleValue();
           double o2_amp;
           int cont = 0;
           double inicio_x = -RX*(Math.PI/k2aux)+c2aux*t_actual;
           double fin_x = RX*(Math.PI/k2aux)+c2aux*t_actual;
           double actual_x = inicio_x;

           o2_actual = new ArrayList();

           while(actual_x <= fin_x) {
               if(c2aux != 0)
                    o2_amp = A2aux*Math.cos(waux*t_actual-k2aux*actual_x);
               else
                   o2_amp = 0;
                              
               o2_actual.add(new Point2D.Double(actual_x,o2_amp));
                              
               actual_x += dx;
               cont++;
           }

           onda2.add(o2_actual);
      }
      
      /**
       * Actualiza la evolución temporal de la onda resultante. Agrega un punto nuevo cada vez que se realiza una llamada.
       */
      private void actualizarOndaResultante() {
           ArrayList<Point2D> or_actual1 = new ArrayList();
           ArrayList<Point2D> or_actual2 = new ArrayList();
           ArrayList<Point2D> or_actual3 = new ArrayList();
           ArrayList<Point2D> or_actual4 = new ArrayList();
           
           double w1aux = w1.doubleValue();
           double k1aux = k1.doubleValue();
           double A1aux = A.doubleValue();
           double c1aux = c1.doubleValue();
           double w2aux = w2.doubleValue();
           double k2aux = k2.doubleValue();
           double A2aux = A.doubleValue();
           double c2aux = c2.doubleValue();
           
           double inicio_x1 = -RX*(Math.PI/k1aux)+c1aux*t_actual;
           double fin_x1 = RX*(Math.PI/k1aux)+c1aux*t_actual;
           
           double inicio_x2 = -RX*(Math.PI/k2aux)+c2aux*t_actual;
           double fin_x2 = RX*(Math.PI/k2aux)+c2aux*t_actual;
           
           double inicio_x;
           double otro_limite_inicio_x;
           double fin_x;
           double otro_limite_fin_x;
           double amp;
           
           boolean onda1_presente = false;
           boolean onda2_presente = false;
           boolean onda1_mas_rapida = c1.doubleValue() > c2.doubleValue();
           
           if(inicio_x1 < inicio_x2) {
               inicio_x = inicio_x1;
               otro_limite_inicio_x = inicio_x2;
           }
           else {
               inicio_x = inicio_x2;
               otro_limite_inicio_x = inicio_x1;
           }
           
           if(fin_x1 > fin_x2) {
               fin_x = fin_x1;
               otro_limite_fin_x = fin_x2;
           }
           else {
               fin_x = fin_x2;
               otro_limite_fin_x = fin_x1;
           }
           
           double actual_x = inicio_x;
           
           while(actual_x <= fin_x) {
               amp = 0;
               onda1_presente = false;
               onda2_presente = false;
               if(actual_x > inicio_x1 && actual_x < fin_x1 && c1aux != 0) {
                   amp += A1aux*Math.cos(w1aux*t_actual-k1aux*actual_x);
                   onda1_presente = true;
               }
               
               if(actual_x > inicio_x2 && actual_x < fin_x2 && c2aux != 0) {
                   amp += A2aux*Math.cos(w2aux*t_actual-k2aux*actual_x);
                   onda2_presente = true;
               
               }
               
               if(onda1_presente && onda2_presente)
                    or_actual3.add(new Point2D.Double(actual_x,amp));
               else {
                   if(onda1_mas_rapida) {
                        if(onda1_presente && actual_x < otro_limite_inicio_x)
                            or_actual1.add(new Point2D.Double(actual_x,amp));
                        else
                            if(onda1_presente && actual_x > otro_limite_fin_x)
                                or_actual4.add(new Point2D.Double(actual_x,amp));
                            else
                                if(onda2_presente)
                                    or_actual2.add(new Point2D.Double(actual_x,amp));
                   }
                   else {
                       if(onda2_presente && actual_x < otro_limite_inicio_x)
                            or_actual2.add(new Point2D.Double(actual_x,amp));
                        else
                            if(onda2_presente && actual_x > otro_limite_fin_x)
                                or_actual4.add(new Point2D.Double(actual_x,amp));
                            else
                                if(onda1_presente)
                                    or_actual1.add(new Point2D.Double(actual_x,amp));
                   }
               }
               
               actual_x += dx;
           }
           
           onda_resultante1.add(or_actual1);
           onda_resultante2.add(or_actual2);
           onda_resultante3.add(or_actual3);
           onda_resultante4.add(or_actual4);
      }
      
      /**
       * Funcion que determina si la onda 1 es más rápida que la onda 2 (tiene mayor velocidad).
       * @return Devuelve True en caso de que la onda 1 sea más rápida que la 2 o False en otro caso.
       */
      public boolean esOnda1MasRapida() {
          return  c1.doubleValue() > c2.doubleValue();
      }
           
       /**
        * Función que actualiza cada una de las ondas del modelo.
        */
       private void actualizar() {
           // No es necesario actualizar las ondas 1 y 2 (solo la resultante) para que la simulación también visualice
           // estas dos ondas es suficiente con descomentar las dos lineas siguientes.
           //actualizarOnda1();
           //actualizarOnda2();
           actualizarOndaResultante();
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
            onda_resultante1.clear();
            onda_resultante2.clear();
            onda_resultante3.clear();
            onda_resultante4.clear();
            onda1.clear();
            onda2.clear();
           
       }

           /**
        * Función que realiza la simulación temporal del fenómeno físico.
        * @param tinicial Tiempo de inicio de la simulación.
        * @param tfinal Tiempo final de la simulación.
        * @param dt Incremento de tiempo.
        * @param dx Incremento de longitud (espacio).
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
