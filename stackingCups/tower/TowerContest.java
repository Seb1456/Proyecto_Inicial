package tower;

import javax.swing.JOptionPane;

/**
 * Clase que resuelve y simula el problema de apilamiento de copas (Problem J - Stacking Cups).
 * Dado n copas y una altura favorita h, encuentra el orden de apilamiento
 * que produce exactamente esa altura considerando el anidamiento entre copas.
 *
 * El creador masivo y Contest solo usan elementos normales (NormalCup y NormalLid).
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public class TowerContest {

    /**
     * Resuelve el problema de la maratón para n copas y altura objetivo h.
     * Encuentra el orden de apilamiento cuya altura visual sea exactamente h.
     * Solo utiliza copas normales (NormalCup).
     *
     * @param n número de copas disponibles (copa i tiene altura 2i-1 cm)
     * @param h altura objetivo favorita en cm
     * @return String con las alturas de las copas en el orden correcto,
     *         separadas por espacio. Retorna "impossible" si no existe solución.
     */
    public String solve(int n, int h) {
        int[] perm    = new int[n];
        boolean[] usado = new boolean[n + 1];
        int[] resultado = buscar(perm, usado, 0, n, h, 0);
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
     * Si existe solución, crea una Tower y apila copas normales en el orden correcto.
     * Si no existe solución, muestra un mensaje de error.
     *
     * @param n número de copas a simular
     * @param h altura objetivo en cm
     */
    public void simulate(int n, int h) {
        String solucion = solve(n, h);
        if (solucion.equals("impossible")) {
            JOptionPane.showMessageDialog(null,
                "impossible", "Sin solución", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int maxHeight = n * n;
        Tower tower = new Tower(n + 2, maxHeight);
        String[] parts = solucion.split(" ");
        for (String p : parts) {
            int size = Integer.parseInt(p);
            int num  = (size + 1) / 2;
            tower.pushCup(num);
        }
    }

    /**
     * Busca mediante backtracking una permutación de [1..n] tal que
     * al apilar las copas en ese orden la altura visual sea exactamente h.
     *
     * @param perm        arreglo donde se construye la permutación actual
     * @param usado       arreglo booleano que indica qué números ya están en perm
     * @param pos         posición actual en la permutación
     * @param n           número total de copas
     * @param h           altura objetivo en cm
     * @param alturaActual altura acumulada hasta la posición actual
     * @return arreglo con la permutación válida encontrada, o null si no existe
     */
    private int[] buscar(int[] perm, boolean[] usado, int pos, int n, int h, int alturaActual) {
        if (alturaActual > h) return null;
        if (pos == n) {
            if (alturaActual == h) return perm.clone();
            return null;
        }
        for (int i = n; i >= 1; i--) {
            if (!usado[i]) {
                usado[i] = true;
                perm[pos] = i;
                boolean nested = false;
                for (int j = pos - 1; j >= 0; j--) {
                    if (perm[j] > i) { nested = true; break; }
                }
                int alturaNueva = alturaActual;
                if (!nested) alturaNueva += (2 * i - 1);
                int[] res = buscar(perm, usado, pos + 1, n, h, alturaNueva);
                if (res != null) return res;
                usado[i] = false;
            }
        }
        return null;
    }

    /**
     * Determina greedy qué copas deben ser visibles (LR-máximos) para
     * alcanzar exactamente la altura h con n copas.
     *
     * La copa n siempre es visible. Se buscan m copas adicionales de {1..n-1}
     * cuyos índices sumen s = (r+m)/2, donde r = h-(2n-1).
     *
     * Condición de factibilidad para m copas extra:
     *   r ≡ m (mod 2)  y  m² ≤ r ≤ m*(2k-m)  con k = n-1
     *
     * @param n número total de copas
     * @param h altura objetivo en cm
     * @return arreglo con los números de copa que deben ser visibles,
     * en orden ascendente con la copa n al final, o null si la altura es imposible.
     */
    private int[] findVisibleCups(int n, int h) {
        int minH = 2 * n - 1;
        int maxH = n * n;
        if (h < minH || h > maxH) {
            return null;
        }

        int r = h - minH;
        int k = n - 1;

        int m = -1;
        for (int candidate = 0; candidate <= k; candidate++) {
            if ((r % 2) == (candidate % 2)
                    && candidate * candidate <= r
                    && r <= candidate * (2 * k - candidate)) {
                m = candidate;
                break;
            }
        }
        if (m == -1) {
            return null;
        }

        int s = (r + m) / 2;
        boolean[] chosen = new boolean[k + 1];
        int remSum   = s;
        int remCount = m;

        for (int i = k; i >= 1 && remCount > 0; i--) {
            int need  = remSum - i;
            int still = remCount - 1;
            if (still == 0) {
                if (i == remSum) {
                    chosen[i] = true;
                    remSum   = 0;
                    remCount = 0;
                }
            } else {
                int minFill = still * (still + 1) / 2;
                int maxFill = still * (i - 1) - still * (still - 1) / 2;
                if (need >= minFill && need <= maxFill) {
                    chosen[i] = true;
                    remSum   = need;
                    remCount = still;
                }
            }
        }

        if (remCount != 0) {
            return null;
        }

        int[] visible = new int[m + 1];
        int idx = 0;
        for (int i = 1; i <= k; i++) {
            if (chosen[i]) {
                visible[idx++] = i;
            }
        }
        visible[idx] = n;
        return visible;
    }
}
