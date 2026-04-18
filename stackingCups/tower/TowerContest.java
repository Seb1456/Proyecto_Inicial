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
        int[] order = findVisibleCups(n, h);
        if (order == null) {
            return "impossible";
        }
    
        StringBuilder sb = new StringBuilder();
        for (int cup : order) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(2 * cup - 1);
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
     * Calcula el orden de apilamiento de n copas que produce exactamente la altura h.
     * Parte de la copa más grande como base y procesa las demás de mayor a menor:
     * anida cada copa (aporta 1 cm) si la paridad lo permite, o la deja visible
     * (aporta 2i-1 cm) en caso contrario. Retorna null si h está fuera del rango
     * alcanzable [2n-1, n²] o cae en el hueco de paridad (h == n²-2).
     *
     * @param n número de copas disponibles (copa i tiene altura 2i-1 cm)
     * @param h altura objetivo en cm
     * @return índices de copa (1-based) en orden de apilamiento de abajo hacia arriba,
     *         o {@code null} si h es imposible de alcanzar
     */
    private int[] findVisibleCups(int n, int h) {
        long minH = 2L * n - 1;
        long maxH = (long) n * n;
        if (h < minH || h > maxH || h == maxH - 2) {
            return null;
        }
    
        java.util.List<Integer> order = new java.util.ArrayList<>();
        long current = 2L * n - 1;
        order.add(n);
    
        for (int i = n - 1; i >= 1; i--) {
            long full = 2L * i - 1;
            long inside = current + 1;
    
            if (h >= inside && (h - inside) % 2 == 0) {
                order.add(0, i);
                current = inside;
            } else {
                order.add(i);
                current += full;
            }
        }
    
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = order.get(i);
        }
        return result;
    }
}
