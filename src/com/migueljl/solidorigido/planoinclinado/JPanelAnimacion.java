package com.migueljl.solidorigido.planoinclinado;

// Referencias utilizadas.

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Clase que implementa el panel donde se situará la simulación gráfica del experimento virtual.
 * @author Miguel Jiménez López
 */

public class JPanelAnimacion extends JPanel implements Runnable {
    /**
     * Tiempo de espera entre cada instantánea de la simulación.
     */
    private final int TICK = 200; 
    /**
     * Desplazamiento con los margenes.
     */
    private int offset = 30;
    /**
     * Coordenadas X de la rampa.
     */
    private int xrampa[] = new int[3];
    /**
     * Coordenadas Y de la rampa.
     */
    private int yrampa[] = new int[3];
    /**
     * Radio del cuerpo que se desliza por la rampa.
     */
    private int radio = 15;
    /**
     * Coordenada X del cuerpo.
     */
    private int xcuerpo = offset-radio;
    /**
     * Coordenada Y del cuerpo.
     */
    private int ycuerpo = offset-2*radio;
    /**
     * Indica la orientación del cuerpo. (Como se pinta la línea)
     */
    private int segmento_linea = 0;
    /**
     * Indica si la simulación está detenida o no.
     */
    private boolean detenida = false;
    /**
     * Indica si la simulación ha terminado o no.
     */
    private boolean fin_simulacion = false;
    /**
     * Hebra que ejecuta la simulación.
     */
    private Thread hilo = null;
    /**
     * Número de posiciones del angulo de rotación en cada segmento (hay cuatro segmentos que se corresponden
     * con los lados del cuadrado sobre el que se inscribe el cuerpo (círculo)).
     */
    private int numero_partes_segmento = 50;
    /**
     * Vector con las posiciones del cuerpo a lo largo del tiempo.
     */
    private Point2D[] posicion_t;
    /**
     * Longitud de la rampa.
     */
    private double L_rampa = 1.0;
    /**
     * Vector con los angulos de rotación del cuerpo a lo largo del tiempo.
     */
    private Point2D[] angulos;
    
    /**
     * Constructor por defecto de la clase.
     */

    public JPanelAnimacion() {
        super();
    }

    /**
     * Función que inicia la simulación.
     * @param p Vector con las posiciones del cuerpo en la rampa.
     * @param ang Vector con los ángulos de rotación del cuerpo.
     * @param L Longitud del plano.
     */
    public void comenzarSimulacion(Point2D[] p,Point2D[] ang,double L) {

        if(hilo != null) {
            while(hilo.isAlive())
                fin_simulacion = true;
        }

        L_rampa = L;
        posicion_t = p;
        angulos = ang;
        hilo = new Thread(this);
        hilo.start();
        fin_simulacion = false;

    }
    
    /**
     * Funcion que actualiza la orientación del cuerpo.
     * @param i Indice que determina el ángulo de rotación del cuerpo considerado.
     * @param nsec Número de segmentos para cada tramo (90 grados).
     */

    private void actualizarOrientacion(int i,int nsec) {
        double angulo = angulos[i].getY();
        double inc_angulo = 90.0/nsec;
        double angulo_aux = 0.0;
        
        segmento_linea = 0;
        
        while(angulo_aux < angulo) {
            segmento_linea = (segmento_linea+1)%(nsec*4);
            angulo_aux += inc_angulo;
        }
    }
    
    /**
     * Genera los puntos que determinan la posición de la línea que representa la orientación del cuerpo.
     * @param nseg Numero de segmentos considerados.
     * @return Devuelve un Vector de puntos que determinan las orientaciones en cada instante.
     */
    Point2D[] generaOrientaciones(int nseg) {
        ArrayList<Point2D> destino = new ArrayList();
        double seg = 0;
        double long_seg = (radio*2);
        double inc_seg = long_seg/nseg;
        int i = 0;
        Point2D pactual = null;
        
        while(i < 4) {
            switch (i) {
                case 0: pactual = new Point2D.Double(xcuerpo+seg,ycuerpo);
                        seg += inc_seg;
                        destino.add(pactual);
                        if(seg > long_seg) {
                            i++;
                            seg = 0.0;
                        }
                        break;
                case 1: pactual = new Point2D.Double(xcuerpo+long_seg,ycuerpo+seg);
                        seg += inc_seg;
                        destino.add(pactual);
                        if(seg > long_seg) {
                            i++;
                            seg = 0.0;
                        }
                        break;
                case 2: 
                        pactual = new Point2D.Double(xcuerpo+long_seg-seg,ycuerpo+long_seg);
                        seg += inc_seg;
                        destino.add(pactual);
                        if(seg > long_seg) {
                            i++;
                            seg = 0.0;
                        }  
                        break;
                case 3: 
                        pactual = new Point2D.Double(xcuerpo,ycuerpo+long_seg-seg);
                        seg += inc_seg;
                        destino.add(pactual);
                        if(seg > long_seg) {
                            i++;
                            seg = 0.0;
                        }
                        break;
            }
        }
        
        Point2D[] destino_array = new Point2D[destino.size()];
        
        for(int j = 0 ; j < destino.size() ; j++)
            destino_array[j] = destino.get(j);
        
        return destino_array;
        
    }

    /**
     * Método que pinta el applet en pantalla
     * @param g Lienzo sobre el que se pintará el applet.
     */
    public void paint(Graphics g) {
        super.paint(g);


        Point2D origen = new Point2D.Double(xcuerpo+radio,ycuerpo+radio);
        Point2D [] destino = generaOrientaciones(numero_partes_segmento);
        
        xrampa[0] = 0+offset;
        xrampa[1] = 0+offset;
        xrampa[2] = this.getWidth() - offset;

        yrampa[0] = 0+offset;
        yrampa[1] = this.getHeight()-offset;
        yrampa[2] = this.getHeight() - offset;

        Polygon rampa = new Polygon(xrampa,yrampa,3);
        Ellipse2D cuerpo = new Ellipse2D.Double(xcuerpo,ycuerpo,radio*2,radio*2);
        Line2D linea = new Line2D.Double(origen,destino[segmento_linea]);
        
        Graphics2D g2 = (Graphics2D) g;

        g2.setStroke(new BasicStroke(3.0f));
        g2.drawPolygon(rampa);
        
        g2.setColor(Color.lightGray);
        g2.fillPolygon(rampa);

        g2.setStroke(new BasicStroke(2.0f));
        g2.setColor(Color.red);
        g2.fill(cuerpo);

        g2.setColor(Color.black);
        g2.draw(cuerpo);

        g2.setStroke(new BasicStroke(1.5f));
        g2.setClip(cuerpo);
        g2.setColor(Color.black);
        g2.draw(linea);
    }

    /**
     * Función que contiene la ejecución de la simulación del experimento virtual.
     */
    public void run() {
        double x = 0.0;

        for(int i = 0 ; i < posicion_t.length && x <= L_rampa && !fin_simulacion ; i++) {
            if(!detenida) {
                x = posicion_t[i].getY();

                xcuerpo = (int) (((x)/L_rampa)*(xrampa[2]) + ((L_rampa-x)/L_rampa)*(xrampa[0]) - radio);
                ycuerpo = (int) (((x)/L_rampa)*(yrampa[2]) + ((L_rampa-x)/L_rampa)*(yrampa[0]) - 2*radio);
                actualizarOrientacion(i,numero_partes_segmento);
                
                repaint();
                try {
                    Thread.sleep(TICK);
                } catch (InterruptedException ex) { }
            }
        }
    }

}
