package shapes;

import java.awt.*;

/**
 * Un rectángulo que puede ser manipulado y que se dibuja a sí mismo en el canvas.
 *
 * @author Michael Kolling and David J. Barnes (Modificado)
 * @version 2.0
 */
public class Rectangle extends Shape {

    public static int EDGES = 4;

    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    /**
     * Crea un nuevo rectángulo en la posición y color por defecto.
     */
    public Rectangle() {
        height = 30;
        width = 40;
        xPosition = 70;
        yPosition = 15;
        color = "magenta";
        isVisible = false;
    }

    /**
     * Hace visible este rectángulo. Si ya era visible, no hace nada.
     */
    @Override
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Hace invisible este rectángulo. Si ya era invisible, no hace nada.
     */
    @Override
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /** Mueve el rectángulo unos píxeles hacia la derecha. */
    public void moveRight() { moveHorizontal(20); }

    /** Mueve el rectángulo unos píxeles hacia la izquierda. */
    public void moveLeft() { moveHorizontal(-20); }

    /** Mueve el rectángulo unos píxeles hacia arriba. */
    public void moveUp() { moveVertical(-20); }

    /** Mueve el rectángulo unos píxeles hacia abajo. */
    public void moveDown() { moveVertical(20); }

    /**
     * Mueve el rectángulo horizontalmente.
     *
     * @param distance distancia deseada en píxeles
     */
    @Override
    public void moveHorizontal(int distance) {
        erase();
        xPosition += distance;
        draw();
    }

    /**
     * Mueve el rectángulo verticalmente.
     *
     * @param distance distancia deseada en píxeles
     */
    @Override
    public void moveVertical(int distance) {
        erase();
        yPosition += distance;
        draw();
    }

    /**
     * Mueve el rectángulo horizontalmente de forma lenta.
     *
     * @param distance distancia deseada en píxeles
     */
    public void slowMoveHorizontal(int distance) {
        int delta = (distance < 0) ? -1 : 1;
        distance = Math.abs(distance);
        for (int i = 0; i < distance; i++) {
            xPosition += delta;
            draw();
        }
    }

    /**
     * Mueve el rectángulo verticalmente de forma lenta.
     *
     * @param distance distancia deseada en píxeles
     */
    public void slowMoveVertical(int distance) {
        int delta = (distance < 0) ? -1 : 1;
        distance = Math.abs(distance);
        for (int i = 0; i < distance; i++) {
            yPosition += delta;
            draw();
        }
    }

    /**
     * Cambia el tamaño del rectángulo.
     *
     * @param newHeight nuevo alto en píxeles (debe ser >= 0)
     * @param newWidth  nuevo ancho en píxeles (debe ser >= 0)
     */
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }

    /**
     * Cambia el color del rectángulo.
     *
     * @param newColor nuevo color. Valores válidos: "red", "yellow", "blue",
     *                 "green", "magenta", "black"
     */
    @Override
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    /** Dibuja el rectángulo con las especificaciones actuales en pantalla. */
    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, new java.awt.Rectangle(xPosition, yPosition, width, height));
            canvas.wait(10);
        }
    }

    /** Borra el rectángulo de la pantalla. */
    private void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
