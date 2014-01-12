package com.migueljl.oscilaciones.forzada.particular;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.migueljl.oscilaciones.forzada.particular.iModelo;

/**
 * Clase que representa al modelo del fenómeno físico del experimento virtual.
 * Fenómeno: Movimiento oscilatorio forzado (sol. particular)
 * @author Miguel Jiménez López
 */

public class Modelo implements iModelo {
    /**
     * Masa del cuerpo enganchado al resorte.
     */
    private Double m = null;
     /**
     * Longitud del muelle.
     */
    private final Double L = new Double(100.0);
    /**
     * Número de picos (dientes) que tiene el muelle.
     */
    private final Integer Npicos = new Integer(8);
    /**
     * Amplitud de los picos (dientes). Por defecto es +/- 1.
     */
    private final Double APico = new Double(-1.0);
    /**
     * Constante elástica del muelle.
     */
    private Double k = null;
    /**
     * Coeficiente de amortiguamiento.
     */
    private Double r = null;
    /**
     * Frecuencia de la fuerza impulsora.
     */
    private Double w = null;
    /**
     * Impedancia mecánica del oscilador.
     */
    private Double z = null;
    /**
     * Desfase de la oscilación del muelle.
     */
    private Double phi = null;
    /**
     * Amplitud máxima de la fuerza impulsora.
     */
    private Double F0 = new Double(100);
    /**
     * Amplitud de la oscilación del muelle.
     */
    private Double Aoscilacion = null;

    /**
     * Conjunto de puntos con la evolución temporal de la posición.
     */
    private ArrayList<Point2D> posicion = null;
    /**
     * Conjunto de puntos con la evolución temporal de la fuerza.
     */
    private ArrayList<Point2D> fuerza = null;
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
     * @param w Frecuencia de la fuerza impulsora.
     * @param m Masa del cuerpo enganchado al muelle.
     * @param r Coeficiente de amortiguamiento.
     * @param k Constante elástica del muelle.
     */
    public Modelo(double w,double m, double r, double k) {
        this.w = new Double(w);
        this.m = new Double(m);
        this.r = new Double(r);
        this.k = new Double(k);
        
        this.z = new Double(Math.sqrt(r*r + (w*m - (k/w))*(w*m - (k/w))));
        this.phi = new Double(Math.atan((w*m-(k/w))/r));
        
        this.Aoscilacion = new Double(F0.doubleValue()/(w/z.doubleValue()));
        
        posicion = new ArrayList();
        fuerza = new ArrayList();
        muelle_simulacion = new ArrayList();
    }

     /**
     * Consulta la masa enganchada al resorte.
     * @return La masa del cuerpo enganchado al muelle.
     */
    public double getMasa() {
        return m.doubleValue();
    }


    /**
     * Consulta la longitud del resorte.
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
     * Consulta la constante elástica del resorte.
     * @return La constante elástica del muelle.
     */
    public double getConstanteElastica() {
        return k.doubleValue();
    }
    
    /**
     * Consulta la frecuencia de la fuerza impulsora.
     * @return La frecuencia de la fuerza impulsora.
     */
    public double getFrecuenciaFuerzaImpulsora() {
        return w.doubleValue();
    }

    /**
     * Consulta el coeficiente de amorgituamiento.
     * @return El coeficiente de amortiguamiento.
     */
    public double getConstanteAmortiguamiento() {
        return r.doubleValue();
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
     * Consulta la evolución temporal de la fuerza impulsora.
     * @return Una lista de puntos con la fuerza impulsora.
     */
    public Point2D[] getFuerza() {
            Point2D[] fuerza_array;
            int n_array = fuerza.size();

            if(n_array > 0) {
                fuerza_array = new Point2D[n_array];

                for(int i = 0 ; i < n_array ; i++)
                    fuerza_array[i] = new Point2D.Double(fuerza.get(i).getX(),fuerza.get(i).getY());

                return fuerza_array;
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
          actualizarFuerza();
          actualizarSimulacionMuelle();
      }

      /**
       * Actualiza la evolución temporal de la posición.
       */
      private void actualizarPosicion() {
          Point2D pactual = null;
          
          pactual = new Point2D.Double(t_actual,(F0.doubleValue()/(w.doubleValue()*z.doubleValue()))
                                        *Math.sin(w.doubleValue()*t_actual-phi.doubleValue()));
          
          posicion.add(pactual);
      }
      
      /**
       * Actualiza la evolución temporal de la fuerza impulsora.
       */
       private void actualizarFuerza() {
          Point2D factual = null;
          
          factual = new Point2D.Double(t_actual,F0.doubleValue()*Math.cos(w.doubleValue()*t_actual));
          
          fuerza.add(factual);
      }


       /**
       * Actualiza la evolución temporal de la simulación del resorte.
       */
       private void actualizarSimulacionMuelle() {
           ArrayList<Point2D> muelle_actual = null;
           double pos_actual = posicion.get(posicion.size()-1).getY();
           double amplitud_pico_actual = APico.doubleValue();
           double L_aux = L.doubleValue();
           double L_actual = L_aux+pos_actual;
           
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
           this.fuerza.clear();
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
