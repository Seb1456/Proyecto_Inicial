package tower;

/**
 * Tapa Fearful: no entra a la torre si su taza compañera no está presente,
 * y no puede salir si está tapando activamente a su taza.
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public class FearfulLid extends Lid {

    /**
     * Crea una tapa Fearful con el número identificador dado.
     *
     * @param number número identificador de la tapa
     */
    public FearfulLid(int number) {
        super(number);
    }

    /**
     * Retorna un color calmado aleatorio para distinguir visualmente
     * esta tapa de los demás tipos.
     *
     * @param n número de la tapa (no utilizado)
     * @return nombre de un color calmado elegido al azar
     */
    @Override
    protected String assignColor(int n) {
        return randomColor(CALM_COLORS);
    }

    /**
     * La tapa Fearful solo entra a la torre si su taza compañera está presente.
     *
     * @param tower la torre a la que se intenta agregar esta tapa
     * @return true si la taza compañera está en la torre, false en caso contrario
     */
    @Override
    public boolean shouldExist(Tower tower) {
        return tower.hasCup(this.getNumber());
    }

    /**
     * La tapa Fearful no puede salir si está tapando activamente a su taza.
     * Si es una tapa independiente (no asignada), sí puede ser eliminada.
     *
     * @param tower la torre que contiene esta tapa
     * @return false si está asignada (tapando) a su taza, true en caso contrario
     */
    @Override
    public boolean canBeRemoved(Tower tower) {
        return !tower.lidsAsignadas.contains(this.getNumber());
    }
}
