package tower;

import shapes.Rectangle;

/**
 * Clase abstracta que representa una tapa para una copa cilíndrica.
 * La tapa número i tiene ancho (2i-1) cm y altura fija de 1 cm.
 * Su ancho coincide con el de la copa del mismo número,
 * por lo que encaja exactamente sobre ella.
 *
 * Principio Abierto/Cerrado: esta clase está cerrada para modificación pero
 * abierta para extensión. Los subtipos sobreescriben los métodos gancho
 * canBeRemoved, shouldExist y getDrawYOffset para definir comportamientos especiales.
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public abstract class Lid implements StackItem {

    private int number;
    private String color;
    private Rectangle top;
    private int xPos;
    private int yPos;
    private static final int PIXELES_POR_CM = 20;
    private int lidHeight;
    private int lidWidth;

    /**
     * Crea una tapa con el número identificador dado.
     * Su ancho es (2*number - 1) cm y su alto es siempre 1 cm.
     *
     * @param number número identificador de la tapa,
     *               debe coincidir con el número de su copa correspondiente
     */
    protected Lid(int number) {
        this.number = number;
        this.color = assignColor(number);
        xPos = 0;
        yPos = 0;
        int sizeCm = 2 * number - 1;
        this.lidHeight = 1 * PIXELES_POR_CM;
        this.lidWidth = sizeCm * PIXELES_POR_CM;
        top = new Rectangle();
        top.changeSize(lidHeight, lidWidth);
        top.changeColor(color);
    }

    /** @return número identificador de la tapa */
    public int getNumber() {
        return number;
    }

    /** Paleta completa de colores disponibles para las tapas. */
    protected static final String[] ALL_COLORS = {
        "red", "blue", "green", "yellow", "magenta", "cyan",
        "orange", "pink", "purple", "gray", "brown", "darkGreen",
        "lightBlue", "lime", "gold", "teal", "navy", "coral",
        "salmon", "violet", "indigo", "turquoise", "crimson", "olive", "maroon"
    };

    /** Colores calmados: usados por FearfulLid para diferenciarse visualmente. */
    protected static final String[] CALM_COLORS = {
        "lightBlue", "teal", "navy", "indigo", "gray", "olive", "turquoise"
    };

    /** Colores vivos/vibrantes: usados por CrazyLid para diferenciarse visualmente. */
    protected static final String[] VIVID_COLORS = {
        "magenta", "pink", "violet", "orange", "lime", "crimson", "coral", "gold"
    };

    /**
     * Selecciona un color al azar de la paleta dada.
     *
     * @param palette arreglo de nombres de colores entre los cuales elegir
     * @return nombre de un color elegido aleatoriamente
     */
    protected static String randomColor(String[] palette) {
        return palette[(int) (Math.random() * palette.length)];
    }

    /**
     * Asigna un color a la tapa según su número rotando por la paleta completa.
     * Puede ser sobreescrito por subtipos para lograr apariencia visualmente diferenciada.
     *
     * @param n número de la tapa
     * @return nombre del color asignado
     */
    protected String assignColor(int n) {
        return ALL_COLORS[n % ALL_COLORS.length];
    }

    /**
     * Mueve la tapa a la posición absoluta (x, y).
     *
     * @param x nueva posición horizontal en píxeles
     * @param y nueva posición vertical en píxeles
     */
    public void draw(int x, int y) {
        int dx = x - xPos;
        int dy = y - yPos;
        xPos = x;
        yPos = y;
        top.moveHorizontal(dx);
        top.moveVertical(dy);
    }

    /**
     * Mueve la tapa verticalmente una distancia relativa.
     *
     * @param distance píxeles a desplazar (positivo = abajo, negativo = arriba)
     */
    public void moveVertical(int distance) {
        yPos += distance;
        top.moveVertical(distance);
    }

    /**
     * Mueve la tapa horizontalmente una distancia relativa.
     *
     * @param distance píxeles a desplazar (positivo = derecha, negativo = izquierda)
     */
    public void moveHorizontal(int distance) {
        xPos += distance;
        top.moveHorizontal(distance);
    }

    /** Hace visible la tapa en pantalla. */
    public void makeVisible() { 
        top.makeVisible(); 
    }

    /** Hace invisible la tapa en pantalla. */
    public void makeInvisible() { 
        top.makeInvisible(); 
    }

    /** @return altura de la tapa en píxeles (siempre 1 cm = 20px) */
    public int getHeight() { 
        return lidHeight; 
    }

    /** @return ancho de la tapa en píxeles */
    public int getWidth() { 
        return lidWidth; 
    }

    /**
     * Método gancho invocado por Tower antes de intentar eliminar esta tapa.
     * Retorna false para impedir la eliminación.
     *
     * @param tower la torre que contiene esta tapa
     * @return true si la tapa puede ser eliminada, false en caso contrario
     */
    public boolean canBeRemoved(Tower tower) {
        return true;
    }

    /**
     * Método gancho que determina si esta tapa debe existir en la torre.
     * Invocado por Tower tras cada cambio en la pila de copas.
     *
     * @param tower la torre que contiene esta tapa
     * @return true si la tapa debe permanecer, false si debe eliminarse automáticamente
     */
    public boolean shouldExist(Tower tower) {
        return true;
    }

    /**
     * Método gancho que retorna el desplazamiento vertical de dibujo respecto
     * al borde superior de su copa cuando está asignada.
     * El valor 0 coloca la tapa en el tope de la copa (comportamiento normal).
     *
     * @param cup la copa a la que está asignada esta tapa
     * @return desplazamiento en píxeles hacia abajo desde el tope de la copa
     */
    public int getDrawYOffset(StackItem item) {
        return 0;
    }

    /**
     * Método gancho que retorna cuántos píxeles debe hundirse esta tapa
     * dentro de la copa al dibujarse (tanto asignada como independiente).
     * El valor 0 la coloca exactamente en el borde; un valor positivo la mete
     * más adentro de la copa.
     *
     * @return desplazamiento de hundimiento en píxeles
     */
    public int lidTheCup() {
        return 0;
    }
}
