package com.migueljl.solidorigido.planoinclinado;

// Referencias necesarias.

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.migueljl.solidorigido.planoinclinado.iModelo;

/**
 * Clase que representa al modelo del fenómeno físico del experimento virtual.
 * Fenómeno: Caída de un cuerpo por un plano inclinado.
 * @author Miguel Jiménez López
 */

public class Modelo implements iModelo {
    /**
     * Constante de gravedad.
     */
    private Double G = null;
    /**
     * Masa del cuerpo.
     */
    private Double M = null;
    /**
     * �?ngulo de inclinación del plano.
     */
    private Double theta = null;
    /**
     * Constante de proporcionalidad entre el momento de inercia y el producto de la masa por el radio al cuadrado
     * (I = alpha*M*R^2).
     */
    private Double alpha = null;
    /**
     * Longitud del plano.
     */
    private Double L = null;
    /**
     * Radio del cuerpo.
     */
    private Double R = null;

    /**
     * Conjunto de puntos con la evolución temporal de la velocidad.
     */
    private ArrayList<Point2D> velocidad = null;
    /**
     * Conjunto de puntos con la evolución temporal de la posición.
     */
    private ArrayList<Point2D> posicion = null;
    /**
     * Conjunto de puntos con la evolución temporal de la energía potencial.
     */
    private ArrayList<Point2D> energia_potencial = null;
    /**
     * Conjunto de puntos con la evolución temporal de la energía cinética de rotación.
     */
    private ArrayList<Point2D> energia_cinetica_rotacion = null;
    /**
     * Conjunto de puntos con la evolución temporal de la energía cinética de traslación.
     */
    private ArrayList<Point2D> energia_cinetica_traslacion = null;
    /**
     * Conjunto de puntos con la evolución temporal del ángulo (normalizado).
     */
    private ArrayList<Point2D> angulo = null;
    /**
     * Conjunto de puntos con la evolución temporal del ángulo (sin normalizar).
     */
    private ArrayList<Point2D> angulo_sin_normalizar = null;

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
     * Constructor de la clase.
     * @param G1 Constante de gravedad.
     * @param M1 Masa del cuerpo.
     * @param theta1 �?ngulo de inclinación del plano.
     * @param alpha1 Constante de proporcionalidad (véase en los atributos).
     * @param R1 Radio del cuerpo.
     * @param L1 Longitud del plano.
     */
    public Modelo(double G1, double M1, double theta1, double alpha1,double R1, double L1) {
        G = new Double(G1);
        M = new Double(M1);
        theta = new Double(theta1);
        alpha = new Double(alpha1);
        L = new Double(L1);
        R = new Double(R1);

        velocidad = new ArrayList();
        posicion = new ArrayList();
        energia_potencial = new ArrayList();
        energia_cinetica_rotacion = new ArrayList();
        energia_cinetica_traslacion = new ArrayList();
        angulo = new ArrayList();
        angulo_sin_normalizar = new ArrayList();
    }

    /**
     * Consulta la constante de gravedad.
     * @return La constante de gravedad.
     */
    public double getG() {
        return G.doubleValue();
    }

    /**
     * Consulta la masa del cuerpo.
     * @return La masa del cuerpo.
     */
    public double getM() {
        return M.doubleValue();
    }

    /**
     * Consulta el ángulo de inclinación del plano.
     * @return El ángulo de inclinación del plano.
     */
    public double getTheta() {
        return theta.doubleValue();
    }

    /**
     * Consulta la constante de proporcionalidad entre el momento de inercia y el producto de la masa por el radio al cuadrado
     * (I = alpha*M*R^2).
     * @return La constante de proporcionalidad entre el momento de inercia y el producto de la masa por el radio al cuadrado
     * (I = alpha*M*R^2).
     */
    public double getAlpha() {
        return alpha.doubleValue();
    }

    /**
     * Consulta la longitud del plano.
     * @return La longitud del plano.
     */
    public double getL() {
        return L.doubleValue();
    }

    /**
     * Consulta el radio del cuerpo.
     * @return El radio del cuerpo.
     */
    public double getR() {
        return R.doubleValue();
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
     * Consulta la evolución temporal del ángulo de rotación (normalizado).
     * @return Una lista de puntos con el ángulo de rotación (normalizado).
     */
       public Point2D[] getAngulo() {
            Point2D[] angulo_array;
            int n_array = angulo.size();

            if(n_array > 0) {
                angulo_array = new Point2D[n_array];

                for(int i = 0 ; i < n_array ; i++)
                    angulo_array[i] = new Point2D.Double(angulo.get(i).getX(),angulo.get(i).getY());

                return angulo_array;
            }
            else {
                return null;
            }

        }
       
      /**
     * Consulta la evolución temporal del ángulo de rotación (sin normalizar).
     * @return Una lista de puntos con el ángulo de rotación (sin normalizar).
     */
       public Point2D[] getAnguloSinNormalizar() {
            Point2D[] angulo_array;
            int n_array = angulo_sin_normalizar.size();

            if(n_array > 0) {
                angulo_array = new Point2D[n_array];

                for(int i = 0 ; i < n_array ; i++)
                    angulo_array[i] = new Point2D.Double(angulo_sin_normalizar.get(i).getX(),angulo_sin_normalizar.get(i).getY());

                return angulo_array;
            }
            else {
                return null;
            }

        }

      /**
     * Consulta la evolución temporal de la energía potencial.
     * @return Una lista de puntos con la energía potencial.
     */
       public Point2D[] getEnergiaPotencial() {
            Point2D[] ep_array;
            int n_array = energia_potencial.size();

            if(n_array > 0) {
                ep_array = new Point2D[n_array];

                for(int i = 0 ; i < n_array ; i++)
                    ep_array[i] = new Point2D.Double(energia_potencial.get(i).getX(),energia_potencial.get(i).getY());

                return ep_array;
            }
            else {
                return null;
            }

        }

       
      /**
     * Consulta la evolución temporal de la energía cinética de rotación.
     * @return Una lista de puntos con la energía cinética de rotación.
     */
       public Point2D[] getEnergiaCineticaRotacion() {
            Point2D[] ecr_array;
            int n_array = energia_cinetica_rotacion.size();

            if(n_array > 0) {
                ecr_array = new Point2D[n_array];

                for(int i = 0 ; i < n_array ; i++)
                    ecr_array[i] = new Point2D.Double(energia_cinetica_rotacion.get(i).getX(),energia_cinetica_rotacion.get(i).getY());

                return ecr_array;
            }
            else {
                return null;
            }

        }

       
      /**
     * Consulta la evolución temporal de la energía cinética de traslación.
     * @return Una lista de puntos con la energía cinética de traslación.
     */
       public Point2D[] getEnergiaCineticaTraslacion() {
            Point2D[] ect_array;
            int n_array = energia_cinetica_traslacion.size();

            if(n_array > 0) {
                ect_array = new Point2D[n_array];

                for(int i = 0 ; i < n_array ; i++)
                    ect_array[i] = new Point2D.Double(energia_cinetica_traslacion.get(i).getX(),energia_cinetica_traslacion.get(i).getY());

                return ect_array;
            }
            else {
                return null;
            }

        }

       /**
       * Actualiza la evolución temporal de la velocidad.
       */
       private void actualizaVelocidad() {
           Point2D.Double vactual;
           double g = G.doubleValue();
           double theta_a = theta.doubleValue();
           double alpha_a = alpha.doubleValue();

           vactual = new Point2D.Double(t_actual,g*((Math.sin(theta_a)/(1+alpha_a))*t_actual));

           velocidad.add(vactual);
           
       }

       
       /**
       * Actualiza la evolución temporal de la posición.
       */
       private void actualizaPosicion() {
           Point2D.Double pactual;
           double g = G.doubleValue();
           double theta_a = theta.doubleValue();
           double alpha_a = alpha.doubleValue();

           pactual = new Point2D.Double(t_actual,0.5*g*((Math.sin(theta_a))/(1+alpha_a))*(t_actual*t_actual));

           posicion.add(pactual);
       }
       
       /**
        * Calcula el ángulo de rotación del cuerpo.
        * @return El ángulo de rotación del cuerpo.
        */
       private double calculaAngulo() {
            double ang;
            double g = G.doubleValue();
            double theta_a = theta.doubleValue();
            double alpha_a = alpha.doubleValue();
            double r = R.doubleValue();

            ang = ((g/(2*r))*(Math.sin(theta_a)/(1+alpha_a))*(t_actual*t_actual));
            
            return ang;

       }

       
       /**
       * Actualiza la evolución temporal del ángulo de rotación.
       */
       private void actualizaAngulo() {
            double ang;
            Point2D ang_act = null;
           
            ang = calculaAngulo();
            // Se normaliza el angulo
            
            angulo_sin_normalizar.add(new Point2D.Double(t_actual,Math.toDegrees(ang)));

            while(ang > 2*Math.PI)
                ang -= 2*Math.PI;

            // Se convierte en grados
            
            ang = Math.toDegrees(ang);

            ang_act = new Point2D.Double(t_actual,ang);

            angulo.add(ang_act);

       }

       
       /**
       * Actualiza la evolución temporal de la energía potencial.
       */
       private void actualizaEnergiaPotencial() {
            Point2D.Double epactual;
            double g = G.doubleValue();
            double m = M.doubleValue();
            double theta_a = theta.doubleValue();
            Point2D xultimo = posicion.get(posicion.size()-1);
            double l = L.doubleValue();

            epactual = new Point2D.Double(t_actual,m*g*(l-xultimo.getY())*Math.sin(theta_a));

            energia_potencial.add(epactual);
       }

       
       /**
       * Actualiza la evolución temporal de la energía cinética de rotación.
       */
       private void actualizaEnergiaCineticaRotacion() {
            Point2D.Double ecractual;
            double alpha_a = alpha.doubleValue();
            Point2D ectactual = energia_cinetica_traslacion.get(energia_cinetica_traslacion.size()-1);

            ecractual = new Point2D.Double(t_actual,alpha_a*ectactual.getY());

            energia_cinetica_rotacion.add(ecractual);
       }

       
       /**
       * Actualiza la evolución temporal de la energía cinética de traslación.
       */
       private void actualizaEnergiaCineticaTraslacion() {
           Point2D.Double ectactual;
           double m = M.doubleValue();
           Point2D vultima = velocidad.get(velocidad.size()-1);

           ectactual = new Point2D.Double(t_actual,0.5*m*vultima.getY()*vultima.getY());

           energia_cinetica_traslacion.add(ectactual);
       }

       /**
       * Actualiza la evolución temporal de los parámetros de la simulación. Agrega un punto nuevo cada vez que se realiza una llamada.
       */
       private void actualizar() {
           actualizaPosicion();
           actualizaAngulo();
           actualizaVelocidad();
           actualizaEnergiaPotencial();
           actualizaEnergiaCineticaTraslacion();
           actualizaEnergiaCineticaRotacion();
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
           this.posicion.clear();
           this.velocidad.clear();
           this.energia_cinetica_rotacion.clear();
           this.energia_potencial.clear();
           this.energia_cinetica_traslacion.clear();
           this.angulo.clear();
           this.angulo_sin_normalizar.clear();
           
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
