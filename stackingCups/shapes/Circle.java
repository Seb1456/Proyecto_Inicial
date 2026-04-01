package shapes;

import java.awt.*;
import java.awt.geom.*;

/**
 * Un círculo que puede ser manipulado y que se dibuja a sí mismo en el canvas.
 *
 * @author Michael Kolling and David J. Barnes
 * @version 2.0
 */
public class Circle extends Shape {

    public static final double PI = 3.1416;

    private int diameter;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    /**
     * Crea un nuevo círculo en la posición y color por defecto.
     */
    public Circle() {
        diameter = 30;
        xPosition = 20;
        yPosition = 15;
        color = "blue";
        isVisible = false;
    }

    /**
     * Hace visible este círculo. Si ya era visible, no hace nada.
     */
    @Override
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Hace invisible este círculo. Si ya era invisible, no hace nada.
     */
    @Override
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /** Mueve el círculo unos píxeles hacia la derecha. */
    public void moveRight() { moveHorizontal(20); }

    /** Mueve el círculo unos píxeles hacia la izquierda. */
    public void moveLeft() { moveHorizontal(-20); }

    /** Mueve el círculo unos píxeles hacia arriba. */
    public void moveUp() { moveVertical(-20); }

    /** Mueve el círculo unos píxeles hacia abajo. */
    public void moveDown() { moveVertical(20); }

    /**
     * Mueve el círculo horizontalmente.
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
     * Mueve el círculo verticalmente.
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
     * Mueve el círculo horizontalmente de forma lenta.
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
     * Mueve el círculo verticalmente de forma lenta.
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
     * Cambia el tamaño del círculo.
     *
     * @param newDiameter nuevo diámetro en píxeles (debe ser >= 0)
     */
    public void changeSize(int newDiameter) {
        erase();
        diameter = newDiameter;
        draw();
    }

    /**
     * Cambia el color del círculo.
     *
     * @param newColor nuevo color. Valores válidos: "red", "yellow", "blue",
     *                 "green", "magenta", "black"
     */
    @Override
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    /** Dibuja el círculo con las especificaciones actuales en pantalla. */
    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, new Ellipse2D.Double(xPosition, yPosition, diameter, diameter));
            canvas.wait(10);
        }
    }

    /** Borra el círculo de la pantalla. */
    private void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
