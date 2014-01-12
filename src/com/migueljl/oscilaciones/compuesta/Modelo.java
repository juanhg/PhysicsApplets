package com.migueljl.oscilaciones.compuesta;

// Referencias necesarias.

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.migueljl.oscilaciones.compuesta.iModelo;

/**
 * Clase que representa al modelo del fenómeno físico del experimento virtual.
 * Fenómeno: Composición de movimientos armónicos simples.
 * @author Miguel Jiménez López
 */

public class Modelo implements iModelo {
    /**
     * Amplitud de los movimientos armónicos.
     */
    private Double A = null;
    /**
     * Longitud natural del muelle.
     */
    private final Double L = new Double(20.0);
    /**
     * Número de picos (dientes) que tiene el muelle.
     */
    private final Integer Npicos = new Integer(8);
    /**
     * Amplitud de los picos (dientes). Por defecto es +/- 1.
     */
    private final Double APico = new Double(-1.0);
    /**
     * Frecuencia angular del primer movimiento oscilatorio.
     */
    private Double w1 = null;
    /**
     * Frecuencia angular del segundo movimiento oscilatorio.
     */
    private Double w2 = null;

    /**
     * Conjunto de puntos con la evolución temporal de la posición.
     */
    private ArrayList<Point2D> posicion = null;
    /**
     * Conjunto de puntos con la evolución temporal de la envolvente superior.
     */
    private ArrayList<Point2D> envolvente_superior = null;
    /**
     * Conjunto de puntos con la evolución temporal de la envolvente inferior.
     */
    private ArrayList<Point2D> envolvente_inferior = null;
    
    /**
     * Conjunto de puntos con la evolución temporal de la simulación del muelle.
     */
    private ArrayList<ArrayList<Point2D>> muelle_simulacion = null;

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
    private double dt = 0.1;

    /**
     * Constructor de la clase.
     * @param w1 Frecuencia angular de la primera oscilación.
     * @param w2 Frecuencia angular de la segunda oscilación.
     * @param A Amplitud de las oscilaciones.
     */
    public Modelo(double w1, double w2, double A) {
        this.w1 = new Double(w1);
        this.w2 = new Double(w2);
        this.A = new Double(A);

        posicion = new ArrayList();
        envolvente_superior = new ArrayList();
        envolvente_inferior = new ArrayList();
        muelle_simulacion = new ArrayList();
    }

    /**
     * Consulta la amplitud de las oscilaciones.
     * @return La amplitud de las oscilaciones.
     */
    public double getAmplitudOscilacion() {
        return A.doubleValue();
    }


    /**
     * Consulta la longitud del muelle.
     * @return La longitud del muelle.
     */
    public double getLongitudMuelle() {
        return L.doubleValue();
    }

    /**
     * Consulta la altura de los dientes del muelle.
     * @return La altura de los dientes del muelle.
     */
    public double getAmplitudDientes() {
        return Math.abs(APico.doubleValue());
    }

    /**
     * Consulta la evolución temporal de la posición.
     * @return Una lista de puntos con la posición.
     */
    public Point2D[] getPosicion() {
            Point2D[] posicion_array;
            int n_array = posicion.size();

            if(n_array > 0) {
                posicion_array = new Point2D[n_array];

                for(int i = 0 ; i < n_array ; i++)
                    posicion_array[i] = new Point2D.Double(posicion.get(i).getX(),posicion.get(i).getY());

                return posicion_array;
            }
            else {
                return null;
            }

      }

    /**
     * Consulta la evolución temporal de la envolvente superior.
     * @return Una lista de puntos con la envolvente superior.
     */
    public Point2D[] getEnvolventeSuperior() {
            Point2D[] es_array;
            int n_array = envolvente_superior.size();

            if(n_array > 0) {
                es_array = new Point2D[n_array];

                for(int i = 0 ; i < n_array ; i++)
                    es_array[i] = new Point2D.Double(envolvente_superior.get(i).getX(),envolvente_superior.get(i).getY());

                return es_array;
            }
            else {
                return null;
            }

      }

    /**
     * Consulta la evolución temporal de la envolvente inferior.
     * @return Una lista de puntos con la envolvente inferior.
     */
     public Point2D[] getEnvolventeInferior() {
            Point2D[] ei_array;
            int n_array = envolvente_inferior.size();

            if(n_array > 0) {
                ei_array = new Point2D[n_array];

                for(int i = 0 ; i < n_array ; i++)
                    ei_array[i] = new Point2D.Double(envolvente_inferior.get(i).getX(),envolvente_inferior.get(i).getY());

                return ei_array;
            }
            else {
                return null;
            }

      }
    
     /**
     * Consulta la evolución temporal de la simulación del movimiento del resorte.
     * @return Una lista de puntos con la simulación del movimiento del resorte.
     */
      public ArrayList<Point2D[]> getSimulacionMuelle() {
          Point2D[] aux = null;
          ArrayList<Point2D[]> simulacion_muelle1 = null;

          simulacion_muelle1 = new ArrayList();

          for(int i = 0 ; i < muelle_simulacion.size() ; i++) {
              aux = new Point2D[muelle_simulacion.get(i).size()];
              for(int j = 0 ; j < muelle_simulacion.get(i).size() ; j++)
                  aux[j] = muelle_simulacion.get(i).get(j);
              simulacion_muelle1.add(aux);

          }

          return simulacion_muelle1;

        }

      /**
       * Actualiza la evolución temporal de la oscilación. Agrega un punto nuevo cada vez que se realiza una llamada.
       */
      private void actualizar() {
          actualizarPosicion();
          actualizarEnvolventeInferior();
          actualizarEnvolventeSuperior();
          actualizarSimulacionMuelle();
      }

      /**
       * Actualiza la evolución temporal de la posición.
       */
      private void actualizarPosicion() {
        double A_aux = A.doubleValue();
        double w1_aux = w1.doubleValue();
        double w2_aux = w2.doubleValue();

        Point2D.Double pactual;

        pactual = new Point2D.Double(t_actual,A_aux*(Math.sin(w1_aux*t_actual)+Math.sin(w2_aux*t_actual)));

        posicion.add(pactual);

      }

      /**
       * Actualiza la evolución temporal de la envolvente inferior.
       */
      private void actualizarEnvolventeInferior() {
        double A_aux = A.doubleValue();
        double w1_aux = w1.doubleValue();
        double w2_aux = w2.doubleValue();

        Point2D.Double pei;

        pei = new Point2D.Double(t_actual,-2*A_aux*(Math.cos(((w1_aux-w2_aux)/2)*t_actual)));

        envolvente_inferior.add(pei);
      }

      /**
       * Actualiza la evolución temporal de la envolvente superior.
       */
      private void actualizarEnvolventeSuperior() {
        double pei = envolvente_inferior.get(envolvente_inferior.size()-1).getY();
        Point2D.Double pes = new Point2D.Double(t_actual,-pei);

        envolvente_superior.add(pes);

      }

      /**
       * Actualiza la evolución temporal de la simulación del resorte.
       */
       private void actualizarSimulacionMuelle() {
           ArrayList<Point2D> muelle_actual = null;
           double pos_actual = posicion.get(posicion.size()-1).getY();
           double amplitud_pico_actual = APico.doubleValue();
           double L_aux = L.doubleValue();
           double A_aux = A.doubleValue();
           double L_actual = L_aux+pos_actual;
           double L_total = L_aux+A_aux;
           //double fraccion_picos = (L_actual)/(L_total);
           int Nx = Npicos;

           muelle_actual = new ArrayList();

           muelle_actual.add(new Point2D.Double(-(L_aux),0));

           for(int i = 1 ; i < Nx ; i++) {
               muelle_actual.add(new Point2D.Double(((L_actual)/Nx)*i-L_aux,amplitud_pico_actual));
               amplitud_pico_actual = -amplitud_pico_actual;
           }

           muelle_actual.add(new Point2D.Double(pos_actual,0));

           muelle_simulacion.add(muelle_actual);


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
           this.muelle_simulacion.clear();
           this.posicion.clear();
           this.envolvente_inferior.clear();
           this.envolvente_superior.clear();
       }

         /**
        * Función que realiza la simulación temporal del fenómeno físico.
        * @param tinicial Tiempo de inicio de la simulación.
        * @param tfinal Tiempo final de la simulación.
        * @param dt Incremento de tiempo.
        */
    public void simular(double tinicial, double tfinal, double dt) {

        inicializarSimulacion();

        t_inicio = tinicial;
        t_final = tfinal;
        this.dt = dt;

        t_actual = t_inicio;
        actualizarModelo();
    }
}
