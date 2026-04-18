package tower;
 
import shapes.Rectangle;
 
/**
 * Tapa Anchor (tipo propuesto): una vez colocada sobre su copa, queda anclada
 * permanentemente y no puede ser eliminada mientras la copa siga en la torre.
 * Solo desaparece automáticamente cuando su copa es eliminada (como FearfulLid),
 * pero a diferencia de FearfulLid, puede entrar incluso si la copa no está
 * presente (se ancla a la primera copa con su número que aparezca).
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
*/
public class AnchorLid extends Lid {
 
    private Rectangle lockMark;
    private int lockX;
    private int lockY;
 
    /**
     * Crea una tapa Anchor con el número identificador dado.
     *
     * @param number número identificador de la tapa
     */
    public AnchorLid(int number) {
        super(number);
        lockMark = new Rectangle();
        // Marca diferente 
        int markW = Math.max(4, getWidth() / 5);
        lockMark.changeSize(getHeight() - 4, markW);
        lockMark.changeColor("black");
        lockX = 0;
        lockY = 0;
    }
 
    /**
     * Retorna un color dorado para distinguir visualmente esta tapa anclada.
     * El dorado comunica "fijo" y "valioso", reforzando la semántica de anclaje.
     *
     * @param n número de la tapa (no utilizado)
     * @return "gold"
     */
    @Override
    protected String assignColor(int n) {
        return "gold";
    }
 
    /**
     * La tapa Anchor nunca puede ser eliminada manualmente.
     * Solo desaparece automáticamente cuando su copa es quitada de la torre
     * (controlado por shouldExist).
     *
     * @param tower la torre que contiene esta tapa
     * @return siempre false
     */
    @Override
    public boolean canBeRemoved(Tower tower) {
        return false;
    }
 
    /**
     * La tapa Anchor debe existir solo mientras su copa esté en la torre.
     * Si la copa es eliminada, la tapa desaparece automáticamente.
     *
     * @param tower la torre que contiene esta tapa
     * @return true si la copa compañera sigue en la torre, false en caso contrario
     */
    @Override
    public boolean shouldExist(Tower tower) {
        return tower.hasCup(this.getNumber());
    }
 
    /**
     * Posiciona la tapa en el tope de su copa (comportamiento estándar).
     *
     * @param item la copa a la que está asignada
     * @return 0 (sin desplazamiento desde el tope)
     */
    @Override
    public int getDrawYOffset(StackItem item) {
        return 0;
    }
 
    /**
     * La tapa se hunde ligeramente (como NormalLid) para dar sensación
     * de estar ajustada sobre la copa.
     *
     * @return desplazamiento de hundimiento en píxeles
     */
    @Override
    public int lidTheCup() {
        return getHeight() / 4;
    }
 
    /**
     * Actualiza la posición del rectángulo de candado cuando la tapa se mueve.
     *
     * @param x nueva posición X de la tapa
     * @param y nueva posición Y de la tapa
     */
    @Override
    public void draw(int x, int y) {
        super.draw(x, y);
        int newLockX = x + (getWidth() / 2) - (Math.max(4, getWidth() / 5) / 2);
        int newLockY = y + 2;
        lockMark.moveHorizontal(newLockX - lockX);
        lockMark.moveVertical(newLockY - lockY);
        lockX = newLockX;
        lockY = newLockY;
    }
 
    /** Hace visible la tapa y su marca de candado. 
     */
    @Override
    public void makeVisible() {
        super.makeVisible();
        lockMark.makeVisible();
    }
 
    /** Hace invisible la tapa y su marca de candado. 
     */
    @Override
    public void makeInvisible() {
        super.makeInvisible();
        lockMark.makeInvisible();
    }
}