package com.migueljl.oscilaciones.simple;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.migueljl.oscilaciones.simple.iModelo;

/**
 * Clase que representa al modelo del fenómeno físico del experimento virtual.
 * Fenómeno: Movimiento armónico simple.
 * @author Miguel Jiménez López
 */

public class Modelo implements iModelo {
    /**
     * Constante elástica del muelle.
     */
    private Double k = null;
    /**
     * Masa del cuerpo enganchado al muelle.
     */
    private Double m = null;
    /**
     * Amplitud de la oscilación máxima del muelle.
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
     * Desfase inicial de la oscilación del muelle.
     */
    private Double phi = null;
    /**
     * Posición inicial del cuerpo enganchado al muelle.
     */
    private Double x0 = null;
    /**
     * Velocidad inicial del cuerpo enganchado al muelle.
     */
    private Double v0 = null;
    /**
     * Frecuencia de la oscilación del muelle.
     */
    private Double w = null;
    /**
     * Conjunto de puntos con la evolución temporal de la posición.
     */
    private ArrayList<Point2D> posicion = null;
    /**
     * Conjunto de puntos con la evolución temporal de la velocidad.
     */
    private ArrayList<Point2D> velocidad = null;
    /**
     * Conjunto de puntos con la evolución temporal de la aceleración.
     */
    private ArrayList<Point2D> aceleracion = null;
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
     * @param k Constante elástica del muelle.
     * @param m Masa del cuerpo enganchado al resorte.
     * @param posicion_inicial Posición inicial del cuerpo enganchado al resorte.
     * @param velocidad_inicial Velocidad inicial del cuerpo enganchado al resorte. 
     */
    public Modelo(double k, double m, double posicion_inicial, double velocidad_inicial) {
        this.k = new Double(k);
        this.m = new Double(m);
        x0 = new Double(posicion_inicial);
        v0 = new Double(velocidad_inicial);

        w = new Double(Math.sqrt(k/m));
        phi = new Double(Math.atan((x0.doubleValue()/v0.doubleValue())*w.doubleValue()));

        if(x0.doubleValue()!= 0.0)
            A = new Double(x0.doubleValue()/Math.sin(phi.doubleValue()));
        else
            A = new Double(v0.doubleValue()/(w.doubleValue()));

        posicion = new ArrayList();
        velocidad = new ArrayList();
        aceleracion = new ArrayList();
        muelle_simulacion = new ArrayList();
    }

    /**
     * Consulta la constante elástica del muelle.
     * @return La constante elástica del muelle.
     */
    public double getConstanteElastica() {
        return k.doubleValue();
    }

    /**
     * Consulta la masa del cuerpo enganchado al resorte.
     * @return La masa del cuerpo enganchado al resorte.
     */
    public double getMasa() {
        return m.doubleValue();
    }

    /**
     * Consulta la amplitud del movimiento oscilatorio del muelle.
     * @return La amplitud del movimiento oscilatorio del muelle.
     */
    public double getAmplitudOscilacion() {
        return A.doubleValue();
    }

    /**
     * Consulta el desfase del movimiento oscilatorio del muelle.
     * @return El desfase del movimiento oscilatorio del muelle.
     */
    public double getFase() {
        return phi.doubleValue();
    }

    /**
     * Consulta la posición inicial del cuerpo enganchado al muelle.
     * @return La posición inicial del cuerpo enganchado al muelle.
     */
    public double getPosicionInicial() {
        return x0.doubleValue();
    }

    /**
     * Consulta la velocidad inicial del cuerpo enganchado al muelle.
     * @return La velocidad inicial del cuerpo enganchado al muelle.
     */
    public double getVelocidadInicial() {
        return v0.doubleValue();
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
     * Consulta la evolución temporal de la aceleración.
     * @return Una lista de puntos con la aceleración.
     */
    public Point2D[] getAceleracion() {
        Point2D[] aceleracion_array;
        int n_array = aceleracion.size();

        if(n_array > 0) {
            aceleracion_array = new Point2D[n_array];

            for(int i = 0 ; i < n_array ; i++)
                aceleracion_array[i] = new Point2D.Double(aceleracion.get(i).getX(),aceleracion.get(i).getY());

            return aceleracion_array;
        }
        else {
            return null;
        }

    }

     /**
     * Consulta la evolución temporal de la velocidad.
     * @return Una lista de puntos con la velocidad.
     */
    public Point2D[] getVelocidad() {
        Point2D[] velocidad_array;
        int n_array = velocidad.size();

        if(n_array > 0) {
            velocidad_array = new Point2D[n_array];

            for(int i = 0 ; i < n_array ; i++)
                velocidad_array[i] = new Point2D.Double(velocidad.get(i).getX(),velocidad.get(i).getY());

            return velocidad_array;
        }
        else {
            return null;
        }

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
          actualizarVelocidad();
          actualizarAceleracion();
          actualizarSimulacionMuelle();
      }

      /**
       * Actualiza la evolución temporal de la posición.
       */
      private void actualizarPosicion() {
        double A_aux = A.doubleValue();
        double phi_aux = phi.doubleValue();
        double w_aux = w.doubleValue();

        Point2D.Double pactual;

        pactual = new Point2D.Double(t_actual,A_aux*Math.sin(w_aux*t_actual+phi_aux));

        posicion.add(pactual);

      }

      /**
       * Actualiza la evolución temporal de la velocidad.
       */
      private void actualizarVelocidad() {
        double A_aux = A.doubleValue();
        double phi_aux = phi.doubleValue();
        double w_aux = w.doubleValue();

        Point2D.Double vactual;

        vactual = new Point2D.Double(t_actual,A_aux*w_aux*Math.cos(w_aux*t_actual+phi_aux));

        velocidad.add(vactual);
      }

      /**
       * Actualiza la evolución temporal de la aceleración.
       */
      private void actualizarAceleracion() {
          double w_aux = w.doubleValue();

          Point2D.Double aactual;

          Point2D posactual = posicion.get(posicion.size()-1);
          aactual = new Point2D.Double(t_actual,-(w_aux*w_aux)*posactual.getY());
          
          aceleracion.add(aactual);
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
           this.velocidad.clear();
           this.aceleracion.clear();
           
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
