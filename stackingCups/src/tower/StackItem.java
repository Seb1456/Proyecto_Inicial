package tower;

/**
 * Interfaz común para los elementos apilables de la torre (copas y tapas).
 * Permite que el algoritmo greedy look-back trate copas y tapas de forma
 * uniforme sin depender de su tipo concreto (Principio Abierto/Cerrado).
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public interface StackItem {

    /** @return ancho del elemento en píxeles */
    int getWidth();

    /** @return alto del elemento en píxeles */
    int getHeight();

    /** @return número identificador del elemento */
    int getNumber();

    /**
     * Mueve el elemento a la posición absoluta (x, y).
     *
     * @param x nueva posición horizontal en píxeles
     * @param y nueva posición vertical en píxeles
     */
    void draw(int x, int y);

    /**
     * Indica si este elemento puede actuar como contenedor de otros elementos.
     * Por defecto retorna false; las copas sobreescriben para retornar true.
     *
     * @return true si el elemento puede contener otros elementos
     */
    default boolean isContainer() {
        return false;
    }

    /**
     * Desplazamiento vertical de hundimiento al dibujarse dentro o encima
     * de otro elemento. Por defecto retorna 0; NormalLid sobreescribe.
     *
     * @return desplazamiento de hundimiento en píxeles
     */
    default int lidTheCup() {
        return 0;
    }
}
