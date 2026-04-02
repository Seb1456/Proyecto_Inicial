package tower;

/**
 * Tapa Crazy: en lugar de ubicarse en el tope de su taza, se ubica en la base de ella.
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public class CrazyLid extends Lid {

    /**
     * Crea una tapa Crazy con el número identificador dado.
     *
     * @param number número identificador de la tapa
     */
    public CrazyLid(int number) {
        super(number);
    }

    /**
     * Retorna un color vivo aleatorio para distinguir visualmente
     * esta tapa de los demás tipos.
     *
     * @param n número de la tapa (no utilizado)
     * @return nombre de un color vivo elegido al azar
     */
    @Override
    protected String assignColor(int n) {
        return randomColor(VIVID_COLORS);
    }

    /**
     * Retorna el desplazamiento vertical para dibujarse en la base de su taza
     * en lugar del tope. El valor desplaza la tapa hacia abajo hasta la base.
     *
     * @param cup la copa a la que está asignada esta tapa
     * @return desplazamiento en píxeles para ubicarse en la base de la copa
     */
    @Override
    public int getDrawYOffset(StackItem item) {
        return item.getHeight() - this.getHeight();
    }
}
