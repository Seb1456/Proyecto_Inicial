package tower;

import shapes.Rectangle;

/**
 * Copa Cover: al ser apilada en la torre genera automáticamente su propia tapa.
 * Si ya existe una tapa con su número, no hace nada adicional.
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 1
 */
public class CoverCup extends Cup {

    private static final String[] PURPLE_COLORS = {
        "magenta", "purple", "violet", "pink", "indigo"
    };

    private Rectangle dot;
    private int dotX;
    private int dotY;

    /**
     * Crea una copa Cover con el número identificador dado.
     *
     * @param number número identificador de la copa
     */
    public CoverCup(int number) {
        super(number);
        dot = new Rectangle();
        dot.changeSize(6, 6);
        dot.changeColor("white");
        dotX = 0;
        dotY = 0;
    }

    @Override
    protected String assignColor(int n) {
        return randomColor(PURPLE_COLORS);
    }

    @Override
    protected void drawMarker(int x, int y) {
        int newDotX = x + getWidth() / 2 - 3;
        int newDotY = y + getHeight() / 2 - 3;
        int dx = newDotX - dotX;
        int dy = newDotY - dotY;
        dotX = newDotX;
        dotY = newDotY;
        dot.moveHorizontal(dx);
        dot.moveVertical(dy);
    }

    @Override
    public void makeVisible() {
        super.makeVisible();
        dot.makeVisible();
    }

    @Override
    public void makeInvisible() {
        super.makeInvisible();
        dot.makeInvisible();
    }

    /**
     * Al ser apilada, genera automáticamente su propia tapa si aún no tiene una.
     *
     * @param tower la torre sobre la que fue apilada esta copa
     */
    @Override
    public void onPushed(Tower tower) {
        for (Lid l : tower.lids) {
            if (l.getNumber() == getNumber()) return;
        }
        tower.pushLid(getNumber());
    }
}
