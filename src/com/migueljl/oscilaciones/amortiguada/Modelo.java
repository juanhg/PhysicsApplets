package com.migueljl.oscilaciones.amortiguada;

// Referencias utilizadas.

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.migueljl.oscilaciones.amortiguada.TipoOscilacion;
import com.migueljl.oscilaciones.amortiguada.iModelo;

/**
 * Clase que representa al modelo del fenómeno físico del experimento virtual.
 * Fenómeno: Movimiento armónico amortiguado.
 * @author Miguel Jiménez López
 */

public class Modelo implements iModelo {
    /**
     * Masa del cuerpo enganchado al muelle.
     */
    private Double m = null;
    /**
     * Longitud natural del muelle.
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
     * Posición inicial del cuerpo enganchado al muelle.
     */
    private Double x0 = null;
    /**
     * Velocidad inicial del cuerpo enganchado al muelle.
     */
    private Double v0 = null;
    /**
     * Paramentos p, q.
     */
    private Double p = null;
    private Double q = null;
    /**
     * Amplitud inicial en el amortiguamiento critico.
     */
    private Double A = null;
    /**
     * Parametro que aparece en la solucion: x = (A+B*t)e^(-p*t).
     */
    private Double B = null;
    /**
     * Amplitud inicial en el movimiento subamortiguado.
     */
    private Double A2 = null;
    /**
     * Movimiento sobreamortiguado: x = e^(-p*t)*(e^(q*t)*C1 + e^(-q*t)*C2).
     */
    private Double C1 = null;
    private Double C2 = null;
    /**
     * Frecuencia angular de la oscilación.
     */
    private Double w = null;
    /**
     * Desfase de la oscilación (movimiento subamortiguado).
     */
    private Double phi = null;
    /**
     * Tipo de oscilación.
     */
    private TipoOscilacion toscilacion;

    /**
     * Conjunto de puntos con la evolución temporal de la posición.
     */
    private ArrayList<Point2D> posicion = null;
    
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
     * @param m Masa del cuerpo enganchado al resorte.
     * @param r Coeficiente de amortiguamiento.
     * @param k Constante elástica del muelle.
     * @param x0 Posición inicial del cuerpo enganchado al resorte.
     * @param v0 Velocidad inicial del cuerpo enganchado al resorte.
     */
    public Modelo(double m, double r, double k, double x0, double v0) {
        this.m = new Double(m);
        this.r = new Double(r);
        this.k = new Double(k);
        this.x0 = new Double(x0);
        this.v0 = new Double(v0);

        double compr1 = (r*r)/(4*m*m);
        double compr2 = (k/m);
 

        if(compr1 > compr2) {
            toscilacion = TipoOscilacion.Sobreamortiguada;
            p = new Double(r/(2*m));
            q = new Double(Math.sqrt(((r*r)/(4*m*m))-(k/m)));
            C1 = new Double(((q.doubleValue()+p.doubleValue())*x0+v0)/(2*q.doubleValue()));
            C2 = new Double(x0-C1.doubleValue());
        }
        else {
            if(compr1 < compr2) {
                toscilacion = TipoOscilacion.Subamortiguada;
                p = new Double(r/(2*m));
                w = new Double(Math.sqrt(-((r*r)/(4*m*m))+(k/m)));
                phi = new Double(Math.atan((w.doubleValue()*x0)/(v0+p.doubleValue()*x0)));
                if(x0 != 0.0)
                    A2 = new Double(x0/Math.sin(phi.doubleValue()));
                else
                    A2 = new Double(v0/w.doubleValue());
            }
            else {
                toscilacion = TipoOscilacion.Critica;
                p = new Double(r/(2*m));
                this.A = new Double(x0);
                B = new Double(v0+p.doubleValue()*x0);
                q = new Double(0);
            }
        }

        posicion = new ArrayList();
        muelle_simulacion = new ArrayList();
    }

    /**
     * Consulta la masa del cuerpo enganchado al resorte.
     * @return La masa del cuerpo enganchado al resorte.
     */
    public double getMasa() {
        return m.doubleValue();
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
     * Consulta la constante elástica del muelle.
     * @return La constante elástica del muelle.
     */
    public double getConstanteElastica() {
        return k.doubleValue();
    }

    /**
     * Consulta el coeficiente de amortiguamiento.
     * @return El coeficiente de amortiguamiento.
     */
    public double getConstanteAmortiguamiento() {
        return r.doubleValue();
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
     * Comprueba si el movimiento oscilatorio es sobreamortiguado.
     * @return Devuelve True si es sobreamortiguado y False en otro caso.
     */
    public boolean esSobreAmortiguada() {
        return (toscilacion == TipoOscilacion.Sobreamortiguada);
    }

    /**
     * Comprueba si el movimiento oscilatorio es subamortiguado.
     * @return Devuelve True si es subamortiguado y False en otro caso.
     */
    public boolean esSubAmortiguada() {
        return (toscilacion == TipoOscilacion.Subamortiguada);
    }

    /**
     * Comprueba si el movimiento oscilatorio es criticamente amortiguado.
     * @return Devuelve True si es criticamente amortiguado y False en otro caso.
     */
    public boolean esCritica() {
        return (toscilacion == TipoOscilacion.Critica);
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
          actualizarSimulacionMuelle();
      }

      /**
       * Actualiza la evolución temporal de la posición. Para ello comprueba el tipo de oscilación.
       */
      private void actualizarPosicion() {
          switch(toscilacion) {
              case Critica: actualizarPosicionCritica();
                  break;
              case Subamortiguada: actualizarPosicionSubAmortiguada();
                  break;
              case Sobreamortiguada: actualizarPosicionSobreAmortiguada();
                  break;
          }
      }

      /**
       * Actualiza la evolución temporal de la posición para una oscilación sobreamortiguada.
       */
      private void actualizarPosicionSobreAmortiguada() {

        Point2D.Double pactual;

        pactual = new Point2D.Double(t_actual,Math.exp(-p.doubleValue()*t_actual)*(C1.doubleValue()*Math.exp(q.doubleValue()*t_actual)
                  +C2.doubleValue()*Math.exp(-q.doubleValue()*t_actual)));

        posicion.add(pactual);

      }

      
      /**
       * Actualiza la evolución temporal de la posición para una oscilación subamortiguada.
       */
      private void actualizarPosicionSubAmortiguada() {
          Point2D.Double pactual;

          pactual = new Point2D.Double(t_actual,A2.doubleValue()*Math.exp(-p.doubleValue()*t_actual)*Math.sin(w.doubleValue()*t_actual+phi.doubleValue()));

          posicion.add(pactual);
      }

      
      /**
       * Actualiza la evolución temporal de la posición para una oscilación criticamente amortiguada.
       */
      private void actualizarPosicionCritica() {
        Point2D.Double pactual;

        pactual = new Point2D.Double(t_actual,(A.doubleValue()+B.doubleValue()*t_actual)*Math.exp(-p.doubleValue()*t_actual));

        posicion.add(pactual);

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
