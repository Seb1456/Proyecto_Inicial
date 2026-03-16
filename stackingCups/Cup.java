/**
 * Representa una copa cilíndrica con número identificador.
 * La copa número i tiene altura y ancho de (2i-1) cm expresados en píxeles.
 * Se compone visualmente de una pared izquierda, una pared derecha y una base.
 * Una copa i cabe dentro de una copa j si y solo si i menor que j.
 * 
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 1
 */
public class Cup {
    private int number;
    private int xPos;
    private int yPos;
    private String color;
    private Rectangle leftWall;
    private Rectangle rightWall;
    private Rectangle base;
    protected boolean exists;
    private int sizeCm;
    private int cupHeight;
    private int cupWidth;
    private static final int PIXELES_POR_CM = 20;

    /**
     * Crea una copa con el número identificador dado.
     * Su tamaño en cm es (2*number - 1) tanto en alto como en ancho.
     * 
     * @param number número identificador de la copa (mayor número = copa más grande)
     */
    public Cup(int number) {
        this.number = number;
        this.color = assignColor(number);
        this.sizeCm = 2 * number - 1;
        this.cupWidth = sizeCm * PIXELES_POR_CM;
        this.cupHeight = sizeCm * PIXELES_POR_CM;
        this.exists = true;
        xPos = 0;
        yPos = 0;
        leftWall = new Rectangle();
        rightWall = new Rectangle();
        base = new Rectangle();
        configureShapes();
        positionInitialParts();
    }

    /**
     * Configura el tamaño y color de las tres partes visuales de la copa.
     */
    private void configureShapes() {
        leftWall.changeSize(cupHeight, 6);
        leftWall.changeColor(color);
        rightWall.changeSize(cupHeight, 5);
        rightWall.changeColor(color);
        base.changeSize(5, cupWidth);
        base.changeColor(color);
    }

    /**
     * Posiciona la pared derecha y la base en su lugar correcto
     * relativo a la esquina superior izquierda de la copa.
     */
    private void positionInitialParts() {
        rightWall.moveHorizontal(cupWidth - 6);
        base.moveVertical(cupHeight - 6);
    }

    /**
     * Asigna un color a la copa según su número usando módulo 6.
     * 
     * @param n número de la copa
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
     * Mueve la copa a la posición absoluta (x, y) calculando
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
        leftWall.moveHorizontal(dx);
        leftWall.moveVertical(dy);
        rightWall.moveHorizontal(dx);
        rightWall.moveVertical(dy);
        base.moveHorizontal(dx);
        base.moveVertical(dy);
    }

    /**
     * Mueve la copa verticalmente una distancia relativa.
     * 
     * @param distance píxeles a desplazar (positivo = abajo, negativo = arriba)
     */
    public void moveVertical(int distance) {
        yPos += distance;
        leftWall.moveVertical(distance);
        rightWall.moveVertical(distance);
        base.moveVertical(distance);
    }

    /** Hace visible la copa en pantalla. */
    public void makeVisible() {
        leftWall.makeVisible();
        rightWall.makeVisible();
        base.makeVisible();
    }

    /** Hace invisible la copa en pantalla. */
    public void makeInvisible() {
        leftWall.makeInvisible();
        rightWall.makeInvisible();
        base.makeInvisible();
    }

    /** @return número identificador de la copa */
    public int getNumber() { 
        return number; 
    }

    /** @return altura de la copa en píxeles */
    public int getHeight() { 
        return cupHeight; 
    }

    /** @return altura de la copa en centímetros */
    public int getHeightCm() { 
        return sizeCm; 
    }

    /** @return ancho de la copa en píxeles */
    public int getWidth() { 
        return cupWidth; 
    }
}