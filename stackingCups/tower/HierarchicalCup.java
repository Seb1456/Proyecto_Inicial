package tower;

import java.util.*;

/**
 * Copa Hierarchical: al entrar a la torre desplaza todos los objetos de menor tamaño
 * hasta encontrar su posición correcta (por encima de todas las copas más grandes
 * y por debajo de todas las más pequeñas). Si logra llegar al fondo de la torre,
 * no puede ser eliminada.
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public class HierarchicalCup extends Cup {

    /**
     * Crea una copa Hierarchical con el número identificador dado.
     *
     * @param number número identificador de la copa
     */
    public HierarchicalCup(int number) {
        super(number);
    }

    /**
     * Retorna un color frío aleatorio para distinguir visualmente
     * esta copa de los demás tipos.
     *
     * @param n número de la copa (no utilizado)
     * @return nombre de un color frío elegido al azar
     */
    @Override
    protected String assignColor(int n) {
        return randomColor(COOL_COLORS);
    }

    /**
     * Al ser apilada, desplaza todos los objetos de menor tamaño reordenando
     * la pila de modo que las copas más grandes queden en la base y las más
     * pequeñas en el tope.
     *
     * @param tower la torre sobre la que fue apilada esta copa
     */
    @Override
    public void onPushed(Tower tower) {
        List<Cup> list = new ArrayList<>(tower.cups);
        list.sort(Comparator.comparingInt(Cup::getNumber).reversed());
        tower.cups.clear();
        tower.cups.addAll(list);
        tower.reorganize1();
    }

    /**
     * No puede ser eliminada si se encuentra en el fondo de la torre (índice 0).
     *
     * @param tower la torre que contiene esta copa
     * @return false si esta copa está en el fondo de la torre, true en caso contrario
     */
    @Override
    public boolean canBeRemoved(Tower tower) {
        if (tower.cups.isEmpty()) return true;
        return tower.cups.get(0).getNumber() != this.getNumber();
    }
}
