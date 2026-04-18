package tower;
import shapes.Rectangle;

/**
 * Copa Opener: al entrar a la torre elimina todas las tapas que le impiden el paso.
 * Cuando es apilada, borra todas las tapas presentes en la torre.
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public class OpenerCup extends Cup {
    
    private Rectangle band;
    private int bandX;
    private int bandY;
    
    /**
     * Crea una copa Opener con el número identificador dado.
     *
     * @param number número identificador de la copa
     */
    public OpenerCup(int number) {
        super(number);
        band = new Rectangle();
        int bandWidth = Math.max(1, getWidth() - 12);
        band.changeSize(4, bandWidth);
        band.changeColor("white");
        bandX = 0;
        bandY = 0;
    }
    
    @Override
    protected void drawMarker(int x, int y) {
        int newBandX = x + 6;
        int newBandY = y + (getHeight() / 2) - 2;
        int dx = newBandX - bandX;
        int dy = newBandY - bandY;
        bandX = newBandX;
        bandY = newBandY;
        band.moveHorizontal(dx);
        band.moveVertical(dy);
    }

    /**
     * Retorna un color cálido aleatorio para distinguir visualmente
     * esta copa de los demás tipos.
     *
     * @param n número de la copa (no utilizado)
     * @return nombre de un color cálido elegido al azar
     */
    @Override
    protected String assignColor(int n) {
        return randomColor(WARM_COLORS);
    }

    /**
     * Al ser apilada en la torre, elimina todas las tapas que le impiden el paso.
     *
     * @param tower la torre sobre la que fue apilada esta copa
     */
    @Override
    public void onPushed(Tower tower) {
        tower.clearLidsBlocking(this);
    }
}
