package com.migueljl.oscilaciones.compuesta;

/**
 * Interfaz que implementa el modelo de cualquier fenómeno físico y que garantiza
 * que puede simularse.
 * @author Miguel Jiménez López
 */
public interface iModelo {

    /**
        * Función que realiza la simulación temporal del fenómeno físico.
        * @param tinicial Tiempo de inicio de la simulación.
        * @param tfinal Tiempo final de la simulación.
        * @param dt Incremento de tiempo.
     */
    public void simular(double tinicial, double tfinal, double dt);

}
