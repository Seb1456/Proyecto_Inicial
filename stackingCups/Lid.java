
/**
 * Representa una tapa para una copa cilíndrica.
 * La tapa número i tiene ancho (2i-1) cm y altura fija de 1 cm.
 * Su ancho coincide con el de la copa del mismo número,
 * por lo que encaja exactamente sobre ella.
 * 
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 1
 */
public class Lid {
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
    public Lid(int number) {
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

    /**
     * Asigna un color a la tapa según su número usando módulo 6.
     * Mismo esquema de colores que Cup para mantener coherencia visual.
     * 
     * @param n número de la tapa
     * @return nombre del color asignado
     */
    private String assignColor(int n) {
        switch (n % 6) {
            case 0: return "magenta";
            case 1: return "yellow";
            case 2: return "red";
            case 3: return "blue";
            case 4: return "black";
            default: return "cyan";
        }
    }

    /**
     * Mueve la tapa a la posición absoluta (x, y) calculando
     * el desplazamiento delta desde la posición actual.
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
}