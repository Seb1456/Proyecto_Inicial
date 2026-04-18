package shapes;

import java.awt.*;

/**
 * Un triángulo que puede ser manipulado y que se dibuja a sí mismo en el canvas.
 *
 * @author Michael Kolling and David J. Barnes
 * @version 2.0
 */
public class Triangle extends Shape {

    public static int VERTICES = 3;

    private int height;
    private int width;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;

    /**
     * Crea un nuevo triángulo en la posición y color por defecto.
     */
    public Triangle() {
        height = 30;
        width = 40;
        xPosition = 140;
        yPosition = 15;
        color = "green";
        isVisible = false;
    }

    /**
     * Hace visible este triángulo. Si ya era visible, no hace nada.
     */
    @Override
    public void makeVisible() {
        isVisible = true;
        draw();
    }

    /**
     * Hace invisible este triángulo. Si ya era invisible, no hace nada.
     */
    @Override
    public void makeInvisible() {
        erase();
        isVisible = false;
    }

    /** Mueve el triángulo unos píxeles hacia la derecha. */
    public void moveRight() {
        moveHorizontal(20);
    }

    /** Mueve el triángulo unos píxeles hacia la izquierda. */
    public void moveLeft() {
        moveHorizontal(-20);
    }

    /** Mueve el triángulo unos píxeles hacia arriba. */
    public void moveUp() {
        moveVertical(-20);
    }

    /** Mueve el triángulo unos píxeles hacia abajo. */
    public void moveDown() {
        moveVertical(20);
    }

    /**
     * Mueve el triángulo horizontalmente.
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
     * Mueve el triángulo verticalmente.
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
     * Mueve el triángulo horizontalmente de forma lenta.
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
     * Mueve el triángulo verticalmente de forma lenta.
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
     * Cambia el tamaño del triángulo.
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
     * Cambia el color del triángulo.
     *
     * @param newColor nuevo color. Valores válidos: "red", "yellow", "blue",
     *                 "green", "magenta", "black"
     */
    @Override
    public void changeColor(String newColor) {
        color = newColor;
        draw();
    }

    /** Dibuja el triángulo con las especificaciones actuales en pantalla. */
    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            int[] xpoints = {xPosition, xPosition + (width / 2), xPosition - (width / 2)};
            int[] ypoints = {yPosition, yPosition + height, yPosition + height};
            canvas.draw(this, color, new Polygon(xpoints, ypoints, 3));
            canvas.wait(10);
        }
    }

    /** Borra el triángulo de la pantalla. */
    private void erase() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.erase(this);
        }
    }
}
