import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.JOptionPane;

/**
 * Clase que resuelve y simula el problema de apilamiento de copas (Problem J - Stacking Cups).
 * Dado n copas y una altura favorita h, encuentra el orden de apilamiento
 * que produce exactamente esa altura considerando el anidamiento entre copas.
 * 
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 1
 */
public class TowerContest {

    /**
     * Resuelve el problema de la maratón para n copas y altura objetivo h.
     * Encuentra el orden de apilamiento cuya altura visual sea exactamente h.
     * 
     * @param n número de copas disponibles (copa i tiene altura 2i-1 cm)
     * @param h altura objetivo favorita en cm
     * @return String con las alturas de las copas en el orden correcto,
     *         separadas por espacio. Retorna "impossible" si no existe solución.
     */
    public String solve(int n, int h) {
        int[] perm      = new int[n];
        boolean[] usado = new boolean[n + 1];
        int[] resultado = buscar(perm, usado, 0, n, h);
        if (resultado == null) return "impossible";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < resultado.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(2 * resultado[i] - 1);
        }
        return sb.toString();
    }

    /**
     * Simula visualmente la solución del problema para n copas y altura h.
     * Si existe solución, crea una Tower y apila las copas en el orden correcto,
     * pintándolas en pantalla. Si no existe solución, muestra un mensaje de error.
     * 
     * @param n número de copas a simular
     * @param h altura objetivo en cm
     */
    public void simulate(int n, int h) {
        int[] perm      = new int[n];
        boolean[] usado = new boolean[n + 1];
        int[] orden     = buscar(perm, usado, 0, n, h);
        if (orden == null) {
            JOptionPane.showMessageDialog(null,
                "impossible", "Sin solución",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        Tower tower = new Tower(n + 2, h);
        for (int num : orden) tower.pushCup(num);
    }

    /**
     * Busca mediante backtracking una permutación de [1..n] tal que
     * al apilar las copas en ese orden la altura visual sea exactamente h.
     * 
     * @param perm    arreglo donde se construye la permutación actual
     * @param usado   arreglo booleano que indica qué números ya están en perm
     * @param pos     posición actual en la permutación que se está construyendo
     * @param n       número total de copas
     * @param h       altura objetivo en cm
     * @return arreglo con la permutación válida encontrada, o null si no existe
     */
    private int[] buscar(int[] perm, boolean[] usado, int pos, int n, int h) {
        if (pos == n) {
            Tower t = new Tower(n + 2, h);
            t.makeInvisible();
            for (int num : perm) t.pushCup(num);
            if (t.heightVisual() == h) return perm.clone();
            return null;
        }
        for (int i = 1; i <= n; i++) {
            if (!usado[i]) {
                usado[i]  = true;
                perm[pos] = i;
                int[] res = buscar(perm, usado, pos + 1, n, h);
                if (res != null) return res;
                usado[i]  = false;
            }
        }
        return null;
    }
}