package shapes;

/**
 * Clase abstracta base para todas las figuras dibujables en el canvas.
 * Define la interfaz común para visibilidad, movimiento y color.
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public abstract class Shape {
    public abstract void makeVisible();
    public abstract void makeInvisible();
    public abstract void moveHorizontal(int distance);
    public abstract void moveVertical(int distance);
    public abstract void changeColor(String newColor);
}
