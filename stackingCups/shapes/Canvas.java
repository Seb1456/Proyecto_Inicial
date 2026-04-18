package shapes;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Canvas es una clase que permite dibujar figuras gráficas simples en un lienzo.
 * Es una modificación del Canvas de propósito general, especialmente diseñada
 * para el ejemplo de "shapes" de BlueJ.
 *
 * @author Bruce Quig
 * @author Michael Kolling (mik)
 * @version 1.6 (shapes)
 */
public class Canvas {

    private static Canvas canvasSingleton;
    private static boolean enabled = true;

    /**
     * Habilita o deshabilita la visualización del canvas.
     * Cuando está deshabilitado, la ventana no se mostrará
     * (útil para ejecutar pruebas sin interfaz gráfica).
     *
     * @param value true para mostrar el canvas, false para suprimirlo
     */
    public static void setEnabled(boolean value) {
        enabled = value;
    }

    /**
     * Método de fábrica que retorna la instancia singleton del canvas.
     *
     * @return instancia única del Canvas
     */
    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            canvasSingleton = new Canvas("BlueJ Shapes Demo", screen.width, screen.height, Color.white);
        }
        canvasSingleton.setVisible(enabled);
        return canvasSingleton;
    }

    // ----- parte de instancia -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;

    /**
     * Crea un Canvas con el título, dimensiones y color de fondo dados.
     *
     * @param title    título que aparece en el marco del Canvas
     * @param width    ancho deseado del canvas en píxeles
     * @param height   alto deseado del canvas en píxeles
     * @param bgColour color de fondo deseado
     */
    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object, ShapeDescription>();
    }

    /**
     * Establece la visibilidad del canvas y lo trae al frente cuando se hace visible.
     *
     * @param visible true para mostrar el canvas, false para ocultarlo
     */
    public void setVisible(boolean visible) {
        if (graphic == null) {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Dibuja una figura dada en el canvas.
     *
     * @param referenceObject objeto de referencia para identificar la figura
     * @param color           color de la figura
     * @param shape           la figura AWT a dibujar en el canvas
     */
    public void draw(Object referenceObject, String color, java.awt.Shape shape) {
        objects.remove(referenceObject);
        objects.add(referenceObject);
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    /**
     * Borra una figura del canvas.
     *
     * @param referenceObject el objeto de referencia de la figura a borrar
     */
    public void erase(Object referenceObject) {
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * Establece el color de primer plano del Canvas.
     *
     * @param colorString nombre del nuevo color (ej. "red", "blue", "green")
     */
    public void setForegroundColor(String colorString) {
        if      (colorString.equals("red"))          graphic.setColor(Color.red);
        else if (colorString.equals("black"))        graphic.setColor(Color.black);
        else if (colorString.equals("blue"))         graphic.setColor(Color.blue);
        else if (colorString.equals("yellow"))       graphic.setColor(Color.yellow);
        else if (colorString.equals("green"))        graphic.setColor(Color.green);
        else if (colorString.equals("magenta"))      graphic.setColor(Color.magenta);
        else if (colorString.equals("white"))        graphic.setColor(Color.white);
        else if (colorString.equals("cyan"))         graphic.setColor(Color.cyan);
        else if (colorString.equals("orange"))       graphic.setColor(new Color(255, 165,   0));
        else if (colorString.equals("pink"))         graphic.setColor(new Color(255, 105, 180));
        else if (colorString.equals("purple"))       graphic.setColor(new Color(128,   0, 128));
        else if (colorString.equals("gray"))         graphic.setColor(Color.gray);
        else if (colorString.equals("brown"))        graphic.setColor(new Color(139,  69,  19));
        else if (colorString.equals("darkGreen"))    graphic.setColor(new Color(  0, 100,   0));
        else if (colorString.equals("lightBlue"))    graphic.setColor(new Color(135, 206, 235));
        else if (colorString.equals("lime"))         graphic.setColor(new Color( 50, 205,  50));
        else if (colorString.equals("gold"))         graphic.setColor(new Color(255, 215,   0));
        else if (colorString.equals("teal"))         graphic.setColor(new Color(  0, 128, 128));
        else if (colorString.equals("navy"))         graphic.setColor(new Color(  0,   0, 128));
        else if (colorString.equals("coral"))        graphic.setColor(new Color(255, 127,  80));
        else if (colorString.equals("salmon"))       graphic.setColor(new Color(250, 128, 114));
        else if (colorString.equals("violet"))       graphic.setColor(new Color(238, 130, 238));
        else if (colorString.equals("indigo"))       graphic.setColor(new Color( 75,   0, 130));
        else if (colorString.equals("turquoise"))    graphic.setColor(new Color( 64, 224, 208));
        else if (colorString.equals("crimson"))      graphic.setColor(new Color(220,  20,  60));
        else if (colorString.equals("olive"))        graphic.setColor(new Color(128, 128,   0));
        else if (colorString.equals("maroon"))       graphic.setColor(new Color(128,   0,   0));
        else                                         graphic.setColor(Color.black);
    }

    /**
     * Espera un número determinado de milisegundos.
     * Útil para producir animaciones simples.
     *
     * @param milliseconds número de milisegundos a esperar
     */
    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {
            // ignorando excepción
        }
    }

    /**
     * Redibuja todas las figuras actualmente en el Canvas.
     */
    private void redraw() {
        erase();
        for (Iterator i = objects.iterator(); i.hasNext();) {
            shapes.get(i.next()).draw(graphic);
        }
        canvas.repaint();
    }

    /**
     * Borra el contenido completo del canvas sin redibujar.
     */
    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    /************************************************************************
     * Clase interna CanvasPane: el componente real del canvas dentro del marco.
     * Es esencialmente un JPanel con capacidad de refrescar la imagen dibujada.
     */
    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    /************************************************************************
     * Clase interna ShapeDescription: almacena la figura AWT y su color.
     */
    private class ShapeDescription {
        private java.awt.Shape shape;
        private String colorString;

        public ShapeDescription(java.awt.Shape shape, String color) {
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic) {
            setForegroundColor(colorString);
            graphic.draw(shape);
            graphic.fill(shape);
        }
    }
}
