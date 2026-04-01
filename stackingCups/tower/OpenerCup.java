package tower;

/**
 * Copa Opener: al entrar a la torre elimina todas las tapas que le impiden el paso.
 * Cuando es apilada, borra todas las tapas presentes en la torre.
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public class OpenerCup extends Cup {

    /**
     * Crea una copa Opener con el número identificador dado.
     *
     * @param number número identificador de la copa
     */
    public OpenerCup(int number) {
        super(number);
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
        tower.clearAllLids();
    }
}
