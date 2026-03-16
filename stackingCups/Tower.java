import java.util.*;
import javax.swing.JOptionPane;

/**
 * Representa una torre que puede contener copas (Cup) y tapas (Lid) apiladas.
 * Las copas se organizan con un algoritmo greedy look-back que anida copas más
 * pequeñas dentro de copas más grandes cuando es posible. Las tapas pueden ser
 * independientes o asignadas a sus copas mediante el método cover().
 * 
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 1
 */
public class Tower {

    private int width;
    private int maxHeight;
    private int alturaMaxTorrePixels;
    private int maxCups;
    private boolean visible;
    private Rectangle leftSide;
    private Rectangle baseLine;
    private ArrayList<Rectangle> heightMarks;
    private static final int PIXELES_POR_CM = 20;
    protected Stack<Cup> cups;
    private int alturaTotalPixelsCups = 0;
    private int alturaProyeccion;
    protected Stack<Lid> lids;
    private Set<Integer> lidsAsignadas = new HashSet<>();
    private boolean ok;

    private int baseX = 120;
    private int baseY = 260;

    /**
     * Crea una torre con ancho y altura máxima dados.
     * No tiene límite en el número de copas que puede contener,
     * solo en la altura total.
     * 
     * @param width     ancho de la torre en unidades (se multiplica por 40px)
     * @param maxHeight altura máxima de la torre en cm
     */
    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        this.visible = true;
        this.cups = new Stack<>();
        this.lids = new Stack<>();
        this.maxCups = -1;
        this.ok = true;
        baseX = -35;
        baseY = 450;

        leftSide = new Rectangle();
        baseLine = new Rectangle();
        heightMarks = new ArrayList<>();

        int towerHeightPixels = maxHeight * 40;
        int towerWidthPixels = width * 40;

        leftSide.changeSize(towerHeightPixels, 5);
        leftSide.moveHorizontal(baseX);
        leftSide.moveVertical(baseY - towerHeightPixels);
        leftSide.changeColor("black");

        baseLine.changeSize(5, towerWidthPixels);
        baseLine.moveHorizontal(baseX);
        baseLine.moveVertical(baseY);
        baseLine.changeColor("black");

        for (int i = 1; i <= maxHeight; i++) {
            Rectangle mark = new Rectangle();
            mark.changeSize(2, 15);
            mark.changeColor("black");
            int markY = baseY - (i * 40);
            mark.moveHorizontal(baseX - 15);
            mark.moveVertical(markY);
            heightMarks.add(mark);
        }

        if (visible) {
            leftSide.makeVisible();
            baseLine.makeVisible();
            for (Rectangle r : heightMarks) r.makeVisible();
        }
    }

    /**
     * Crea una torre con exactamente n copas preapiladas (copa 1 hasta copa n).
     * La altura máxima es n² cm y el ancho es n+2. No admite más copas
     * una vez creada.
     * 
     * @param cups número de copas a preapilar (mínimo 1)
     */
    public Tower(int cups) {
        if (cups < 1) cups = 1;
        int maxHeight = cups * cups;
        int width = cups + 2;
        this.maxCups = cups;
        Tower base = new Tower(width, maxHeight);
        this.width = base.width;
        this.maxHeight = base.maxHeight;
        this.visible = base.visible;
        this.leftSide = base.leftSide;
        this.baseLine = base.baseLine;
        this.heightMarks = base.heightMarks;
        this.cups = new Stack<>();
        this.lids = new Stack<>();
        this.ok = true;
        baseX = -35;
        baseY = 450;
        for (int i = 1; i <= cups; i++) pushCup(i);
    }

    /**
     * Agrega una copa con el número dado al tope de la torre.
     * Verifica que no exista una copa con ese número, que la altura
     * total no supere el máximo, y que no se haya alcanzado el límite
     * de copas del constructor Tower(n).
     * 
     * @param n número identificador de la copa a agregar
     */
    public void pushCup(int n) {
        ok = false;
        for (Cup c : cups) {
            if (c.getNumber() == n) {
                if (visible) JOptionPane.showMessageDialog(null,
                    "Ya existe una copa con el número " + n + " en la torre.",
                    "No se puede añadir", JOptionPane.ERROR_MESSAGE);
                ok = false;
                return;
            }
        }
        Cup c = new Cup(n);
        int cAltura = c.getHeight();
        int alturaProyeccion = alturaTotalPixelsCups + cAltura;
        int alturaMaxTorrePixels = maxHeight * 40;
        if (alturaProyeccion > alturaMaxTorrePixels) {
            if (visible) JOptionPane.showMessageDialog(null,
                "La altura de las copas superan la altura de la torre",
                "No se puede añadir una copa más.", JOptionPane.ERROR_MESSAGE);
            ok = false;
            return;
        }
        if (maxCups != -1 && cups.size() >= maxCups) {
            if (visible) JOptionPane.showMessageDialog(null,
                "Esta torre solo admite " + maxCups + " copa(s).",
                "Límite alcanzado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Lid l : lids) {
            if (!lidsAsignadas.contains(l.getNumber())) {
                if (l.getWidth() < c.getWidth() && l.getHeight() <= c.getHeight()) {
                    alturaTotalPixelsCups -= l.getHeight();
                }
            }
        }
        cups.push(c);
        reorganize1();
        if (visible) c.makeVisible();
        ok = true;
        alturaTotalPixelsCups = alturaProyeccion;
    }

    /**
     * Elimina la copa del tope de la torre (la última agregada).
     * No hace nada si la torre está vacía.
     */
    public void popCup() {
        ok = false;
        if (cups.isEmpty()) {
            if (visible) JOptionPane.showMessageDialog(null,
                "No es posible hacer pop cuando no hay copas.",
                "No hay copas en la torre.", JOptionPane.ERROR_MESSAGE);
            ok = false;
            return;
        }
        Cup c = cups.pop();
        alturaTotalPixelsCups -= c.getHeight();
        c.makeInvisible();
        reorganize1();
        ok = true;
    }

    /**
     * Elimina la copa con el número dado de cualquier posición de la torre.
     * 
     * @param n número identificador de la copa a eliminar
     */
    public void removeCup(int n) {
        ok = false;
        Stack<Cup> temp = new Stack<>();
        boolean found = false;
        while (!cups.isEmpty()) {
            Cup c = cups.pop();
            if (c.getNumber() == n) {
                c.makeInvisible();
                found = true;
                alturaTotalPixelsCups -= c.getHeight();
                break;
            }
            temp.push(c);
        }
        while (!temp.isEmpty()) cups.push(temp.pop());
        reorganize1();
        ok = found;
    }

    /**
     * Agrega una tapa independiente al tope de la pila de tapas.
     * Si la tapa cabe dentro de alguna copa existente no cuenta para
     * la altura total. Solo se asigna a una copa al llamar cover().
     * 
     * @param n número identificador de la tapa a agregar
     */
    public void pushLid(int n) {
        ok = false;
        for (Lid l : lids) {
            if (l.getNumber() == n) {
                if (visible) JOptionPane.showMessageDialog(null,
                    "Ya existe una tapa con el número " + n,
                    "No se puede añadir", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Lid nueva = new Lid(n);
        boolean cabeEnCup = false;
        for (Cup c : cups) {
            if (nueva.getWidth() < c.getWidth() && nueva.getHeight() <= c.getHeight()) {
                cabeEnCup = true;
                break;
            }
        }
        if (!cabeEnCup) {
            int alturaProyeccion = alturaTotalPixelsCups + nueva.getHeight();
            if (alturaProyeccion > maxHeight * 40) {
                if (visible) JOptionPane.showMessageDialog(null,
                    "La tapa supera la altura de la torre.",
                    "No se puede añadir", JOptionPane.ERROR_MESSAGE);
                return;
            }
            alturaTotalPixelsCups += nueva.getHeight();
        }
        lids.push(nueva);
        alturaTotalPixelsCups += nueva.getHeight();
        if (visible) nueva.makeVisible();
        reorganize1();
        ok = true;
    }

    /**
     * Elimina la tapa del tope de la pila de tapas (la última agregada).
     * No hace nada si no hay tapas.
     */
    public void popLid() {
        ok = false;
        if (lids.isEmpty()) {
            if (visible) JOptionPane.showMessageDialog(null,
                "No es posible hacer pop cuando no hay tapas.",
                "No hay tapas en la torre.", JOptionPane.ERROR_MESSAGE);
            ok = false;
            return;
        }
        Lid l = lids.pop();
        alturaTotalPixelsCups -= l.getHeight();
        lidsAsignadas.remove(l.getNumber());
        l.makeInvisible();
        reorganize1();
        ok = true;
    }

    /**
     * Elimina la tapa con el número dado de cualquier posición de la pila.
     * 
     * @param n número identificador de la tapa a eliminar
     */
    public void removeLid(int n) {
        ok = false;
        Stack<Lid> temp = new Stack<>();
        boolean found = false;
        int alturaLid = 0;
        while (!lids.isEmpty()) {
            Lid l = lids.pop();
            if (l.getNumber() == n) {
                alturaLid = l.getHeight();
                l.makeInvisible();
                found = true;
                break;
            }
            temp.push(l);
        }
        while (!temp.isEmpty()) lids.push(temp.pop());
        if (found) {
            lidsAsignadas.remove(n);
            alturaTotalPixelsCups -= alturaLid;
        }
        reorganize1();
        ok = found;
    }

    /**
     * Ordena las copas de menor a mayor número (orden ascendente).
     * Las tapas independientes mantienen su posición relativa.
     */
    public void reverseTower() {
        ok = false;
        List<Cup> list = new ArrayList<>(cups);
        Collections.sort(list, Comparator.comparingInt(Cup::getNumber));
        cups.clear();
        cups.addAll(list);
        reorganize1();
        ok = true;
    }

    /**
     * Invierte el orden actual de las copas en la torre.
     * La copa del tope pasa al fondo y viceversa.
     */
    public void orderTower() {
        ok = false;
        Collections.reverse(cups);
        reorganize1();
        ok = true;
    }

    /**
     * Reorganiza visualmente las copas y tapas apilándolas una encima de otra
     * sin anidamiento. Las tapas asignadas se dibujan encima de su copa.
     * Se usa principalmente después de swap cuando hay tapas asignadas.
     */
    private void reorganize() {
        int currentY = baseY;
        int torreWidthPixels = width * 40;
        int centroTorreX = baseX + (torreWidthPixels / 2);
        for (Cup c : cups) {
            int xCentrada = centroTorreX - c.getWidth() / 2;
            int y = currentY - c.getHeight();
            c.draw(xCentrada, y);
            currentY -= c.getHeight();
        }
        for (Lid l : lids) {
            int lidX = centroTorreX - l.getWidth() / 2;
            boolean found = false;
            int tempY = baseY;
            for (Cup c : cups) {
                int cupTop = tempY - c.getHeight();
                if (c.getNumber() == l.getNumber()) {
                    l.draw(lidX, cupTop);
                    found = true;
                    break;
                }
                tempY -= c.getHeight();
            }
            if (!found) l.draw(lidX, baseY - (maxHeight * 40) - 50);
        }
    }

    /**
     * Reorganiza visualmente usando greedy look-back: anida copas más pequeñas
     * dentro de copas más grandes cuando caben, luego posiciona tapas asignadas
     * encima de su copa y tapas independientes dentro de cups o apiladas solas.
     */
    private void reorganize1() {
        List<Cup> list = new ArrayList<>(cups);
        int[] drawnX   = new int[list.size()];
        int[] drawnY   = new int[list.size()];
        posicionarCopas(list, drawnX, drawnY);
        posicionarTapas(list, drawnX, drawnY);
        posicionarTapasIndependientes(list, drawnX, drawnY);
    }

    /**
     * Posiciona todas las copas usando greedy look-back.
     * Cada copa busca hacia atrás la primera copa donde quepa (menor ancho
     * y altura disponible suficiente). Si no cabe en ninguna, apila encima.
     * Las copas arrancan desde encima de las tapas independientes.
     * 
     * @param list   lista de copas en orden de apilamiento
     * @param drawnX arreglo donde se guardan las coordenadas X dibujadas
     * @param drawnY arreglo donde se guardan las coordenadas Y dibujadas
     */
    private void posicionarCopas(List<Cup> list, int[] drawnX, int[] drawnY) {
        int centroTorreX   = baseX + (width * 40) / 2;
        int[] espacioUsado = new int[list.size()];
        int alturaLidsIndependientes = 0;
        for (Lid l : lids) {
            if (!lidsAsignadas.contains(l.getNumber()))
                alturaLidsIndependientes += l.getHeight();
        }
        int stackingY = baseY - alturaLidsIndependientes;
        for (int i = 0; i < list.size(); i++) {
            Cup c = list.get(i);
            boolean nested = false;
            for (int j = i - 1; j >= 0; j--) {
                Cup candidate    = list.get(j);
                int espacioLibre = candidate.getHeight() - espacioUsado[j];
                if (c.getWidth() < candidate.getWidth() && c.getHeight() <= espacioLibre) {
                    drawnX[i] = drawnX[j] + (candidate.getWidth() - c.getWidth()) / 2;
                    drawnY[i] = drawnY[j] + candidate.getHeight() - espacioUsado[j] - c.getHeight();
                    espacioUsado[j] += c.getHeight();
                    nested = true;
                    break;
                }
            }
            if (!nested) {
                drawnX[i] = centroTorreX - c.getWidth() / 2;
                drawnY[i] = stackingY - c.getHeight();
                stackingY = drawnY[i];
            }
            c.draw(drawnX[i], drawnY[i]);
        }
    }

    /**
     * Posiciona las tapas que han sido asignadas a sus copas mediante cover().
     * Cada tapa se dibuja centrada en el borde superior de su copa.
     * 
     * @param list   lista de copas con sus coordenadas ya calculadas
     * @param drawnX coordenadas X de cada copa
     * @param drawnY coordenadas Y de cada copa
     */
    private void posicionarTapas(List<Cup> list, int[] drawnX, int[] drawnY) {
        int centroTorreX = baseX + (width * 40) / 2;
        int stackingY    = baseY;
        for (Lid l : lids) {
            if (!lidsAsignadas.contains(l.getNumber())) continue;
            boolean found = false;
            for (int i = 0; i < list.size(); i++) {
                Cup c = list.get(i);
                if (c.getNumber() == l.getNumber()) {
                    l.draw(drawnX[i] + (c.getWidth() - l.getWidth()) / 2, drawnY[i]);
                    found = true;
                    break;
                }
            }
            if (!found) {
                int lidX = centroTorreX - l.getWidth() / 2;
                int lidY = stackingY - l.getHeight();
                l.draw(lidX, lidY);
                stackingY = lidY;
            }
        }
    }

    /**
     * Posiciona las tapas independientes (no asignadas con cover()).
     * Cada tapa intenta anidarse dentro de la primera copa donde quepa.
     * Si no cabe en ninguna copa, se apila sola debajo de las copas.
     * 
     * @param cupList  lista de copas con sus coordenadas ya calculadas
     * @param cupDrawnX coordenadas X reales de cada copa
     * @param cupDrawnY coordenadas Y reales de cada copa
     */
    private void posicionarTapasIndependientes(List<Cup> cupList, int[] cupDrawnX, int[] cupDrawnY) {
        int centroTorreX = baseX + (width * 40) / 2;
        int stackingY    = baseY;
        List<Lid> lidList = new ArrayList<>(lids);
        for (int i = 0; i < lidList.size(); i++) {
            Lid l = lidList.get(i);
            if (lidsAsignadas.contains(l.getNumber())) continue;
            boolean nested = false;
            for (int j = cupList.size() - 1; j >= 0; j--) {
                Cup candidate = cupList.get(j);
                if (l.getWidth() < candidate.getWidth() && l.getHeight() <= candidate.getHeight()) {
                    int lidX = cupDrawnX[j] + (candidate.getWidth() - l.getWidth()) / 2;
                    int lidY = cupDrawnY[j] + candidate.getHeight() - l.getHeight();
                    l.draw(lidX, lidY);
                    nested = true;
                    break;
                }
            }
            if (!nested) {
                int lidX = centroTorreX - l.getWidth() / 2;
                int lidY = stackingY - l.getHeight();
                l.draw(lidX, lidY);
                stackingY = lidY;
            }
        }
    }

    /**
     * Verifica si existe una tapa con el número dado en la pila de tapas.
     * 
     * @param cupNumber número a verificar
     * @return true si existe una tapa con ese número
     */
    private boolean tieneTapa(int cupNumber) {
        for (Lid l : lids) {
            if (l.getNumber() == cupNumber) return true;
        }
        return false;
    }

    /**
     * Cuenta cuántas copas tienen una tapa asignada (mediante cover()).
     * 
     * @return número de copas con tapa asignada
     */
    public int lidedCups() {
        int count = 0;
        for (Cup c : cups) {
            for (Lid l : lids) {
                if (c.getNumber() == l.getNumber()) { count++; break; }
            }
        }
        return count;
    }

    /**
     * Retorna un arreglo con todos los elementos de la torre en orden
     * de fondo a tope. Cada elemento es un String[] de dos posiciones:
     * [0] tipo ("cup" o "lid"), [1] número del elemento.
     * Las tapas aparecen inmediatamente después de su copa si existen.
     * 
     * @return String[][] con los elementos ordenados de fondo a tope
     */
    public String[][] stackingItems() {
        List<String[]> items = new ArrayList<>();
        Stack<Cup> temp = new Stack<>();
        while (!cups.isEmpty()) temp.push(cups.pop());
        while (!temp.isEmpty()) {
            Cup cup = temp.pop();
            items.add(new String[]{"cup", String.valueOf(cup.getNumber())});
            for (Lid l : lids) {
                if (l.getNumber() == cup.getNumber()) {
                    items.add(new String[]{"lid", String.valueOf(l.getNumber())});
                    break;
                }
            }
            cups.push(cup);
        }
        String[][] result = new String[items.size()][2];
        for (int i = 0; i < items.size(); i++) result[i] = items.get(i);
        return result;
    }

    /**
     * Asigna una tapa a cada copa de la torre.
     * Si una copa no tiene tapa, crea una nueva con pushLid().
     * Si ya existe una tapa con ese número, solo la marca como asignada.
     * Después de cover() las tapas se dibujan encima de su copa respectiva.
     */
    public void cover() {
        ok = false;
        for (Cup c : cups) {
            boolean hasLid = false;
            for (Lid l : lids) {
                if (l.getNumber() == c.getNumber()) { hasLid = true; break; }
            }
            if (!hasLid) pushLid(c.getNumber());
            lidsAsignadas.add(c.getNumber());
        }
        reorganize1();
        ok = true;
    }

    /**
     * Intercambia la posición de dos elementos en la torre.
     * Los elementos pueden ser cualquier combinación de cup y lid.
     * Si alguno tiene tapa asignada usa reorganize() simple,
     * si no usa reorganize1() con greedy.
     * 
     * @param o1 primer elemento: String[] con [tipo, número]
     * @param o2 segundo elemento: String[] con [tipo, número]
     */
    public void swap(String[] o1, String[] o2) {
        ok = false;
        String tipo1 = o1[0];
        int num1 = Integer.parseInt(o1[1]);
        String tipo2 = o2[0];
        int num2 = Integer.parseInt(o2[1]);
        if (tipo1.equals("cup") && tipo2.equals("cup")) {
            swapCupCup(num1, num2);
        } else if (tipo1.equals("lid") && tipo2.equals("lid")) {
            swapLidLid(num1, num2);
        } else {
            int cupNum = tipo1.equals("cup") ? num1 : num2;
            int lidNum = tipo1.equals("lid") ? num1 : num2;
            swapCupLid(cupNum, lidNum);
        }
    }

    /**
     * Encuentra el único intercambio entre dos elementos de la torre
     * que más reduce la altura visual considerando anidamiento.
     * Prueba todos los pares posibles simulando cada swap.
     * 
     * @return String[][] con los dos elementos a intercambiar,
     *         o null si ningún swap reduce la altura
     */
    public String[][] swapToReduce() {
        String[][] items   = stackingItems();
        int alturaActual   = heightVisual();
        String[] mejorO1   = null;
        String[] mejorO2   = null;
        int mejorReduccion = 0;
        for (int i = 0; i < items.length; i++) {
            for (int j = i + 1; j < items.length; j++) {
                swap(items[i], items[j]);
                int reduccion = alturaActual - heightVisual();
                swap(items[j], items[i]);
                if (reduccion > mejorReduccion) {
                    mejorReduccion = reduccion;
                    mejorO1 = items[i];
                    mejorO2 = items[j];
                }
            }
        }
        return mejorO1 != null ? new String[][]{ mejorO1, mejorO2 } : null;
    }

    /**
     * Intercambia dos copas por sus índices en la pila.
     * 
     * @param num1 número de la primera copa
     * @param num2 número de la segunda copa
     */
    private void swapCupCup(int num1, int num2) {
        List<Cup> list = new ArrayList<>(cups);
        int idx1 = -1, idx2 = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNumber() == num1) idx1 = i;
            if (list.get(i).getNumber() == num2) idx2 = i;
        }
        if (idx1 == -1 || idx2 == -1) return;
        Cup temp = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, temp);
        cups.clear();
        cups.addAll(list);
        if (tieneTapa(num1) || tieneTapa(num2)) reorganize();
        else reorganize1();
        ok = true;
    }

    /**
     * Intercambia dos tapas por sus índices en la pila.
     * 
     * @param num1 número de la primera tapa
     * @param num2 número de la segunda tapa
     */
    private void swapLidLid(int num1, int num2) {
        List<Lid> list = new ArrayList<>(lids);
        int idx1 = -1, idx2 = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNumber() == num1) idx1 = i;
            if (list.get(i).getNumber() == num2) idx2 = i;
        }
        if (idx1 == -1 || idx2 == -1) return;
        Lid temp = list.get(idx1);
        list.set(idx1, list.get(idx2));
        list.set(idx2, temp);
        lids.clear();
        lids.addAll(list);
        if (tieneTapa(num1) || tieneTapa(num2)) reorganize();
        else reorganize1();
        ok = true;
    }

    /**
     * Intercambia una copa con una tapa verificando que la copa
     * quepa en la altura disponible en la posición de la tapa.
     * 
     * @param cupNum número de la copa a intercambiar
     * @param lidNum número de la tapa a intercambiar
     */
    private void swapCupLid(int cupNum, int lidNum) {
        List<Cup> cupList = new ArrayList<>(cups);
        List<Lid> lidList = new ArrayList<>(lids);
        int cupIdx = -1;
        for (int i = 0; i < cupList.size(); i++) {
            if (cupList.get(i).getNumber() == cupNum) { cupIdx = i; break; }
        }
        int lidIdx = -1;
        for (int i = 0; i < lidList.size(); i++) {
            if (lidList.get(i).getNumber() == lidNum) { lidIdx = i; break; }
        }
        if (cupIdx == -1 || lidIdx == -1) return;
        Cup targetCup = cupList.get(cupIdx);
        Lid targetLid = lidList.get(lidIdx);
        int alturaExtra = targetCup.getHeight() - targetLid.getHeight();
        if (alturaTotalPixelsCups + alturaExtra > maxHeight * 40) {
            if (visible) JOptionPane.showMessageDialog(null,
                "La copa no cabe en la posición de la tapa.",
                "Swap inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cupList.remove(cupIdx);
        int insertIdx = lidIdx < cupList.size() ? lidIdx : cupList.size();
        cupList.add(insertIdx, targetCup);
        lidList.remove(lidIdx);
        lidList.add(cupIdx < lidList.size() ? cupIdx : lidList.size(), targetLid);
        cups.clear();
        cups.addAll(cupList);
        lids.clear();
        lids.addAll(lidList);
        alturaTotalPixelsCups += alturaExtra;
        if (tieneTapa(cupNum)) reorganize();
        else reorganize1();
        ok = true;
    }

    /**
     * Calcula la altura total de todas las copas en cm sin considerar anidamiento.
     * 
     * @return suma de alturas en cm de todas las copas
     */
    public int height() {
        int alturaTotal = 0;
        for (Cup c : cups) {
           alturaTotal += c.getHeightCm(); 
        }
        return alturaTotal;
    }

    /**
     * Calcula la altura real de la torre en cm considerando el anidamiento.
     * Usa el mismo algoritmo greedy look-back que reorganize1().
     * Las copas anidadas dentro de otras no suman altura propia.
     * 
     * @return altura visual real en cm
     */
    public int heightVisual() {
        List<Cup> list = new ArrayList<>(cups);
        int[] topPos   = new int[list.size()];
        int maxTop     = 0;
    
        for (int i = 0; i < list.size(); i++) {
            Cup c   = list.get(i);
            int bottom;
    
            if (i == 0) {
                bottom = 0;
            } else {
                Cup prev = list.get(i - 1);
                if (c.getNumber() < prev.getNumber()) {
                    bottom = (topPos[i-1] - prev.getHeightCm()) + 1;
                } else {
                    bottom = topPos[i - 1];
                }
            }
    
            topPos[i] = bottom + c.getHeightCm();
            if (topPos[i] > maxTop) maxTop = topPos[i];
        }
    
        return maxTop;
    }

    /** Hace visible la torre con todas sus copas y tapas. */
    public void makeVisible() {
        visible = true;
        for (Cup c : cups) c.makeVisible();
        for (Lid l : lids) l.makeVisible();
    }

    /** Hace invisible la torre con todas sus copas y tapas. */
    public void makeInvisible() {
        visible = false;
        for (Cup c : cups) c.makeInvisible();
        for (Lid l : lids) l.makeInvisible();
    }

    /**
     * Indica si la última operación realizada fue exitosa.
     * 
     * @return true si la última operación fue exitosa, false en caso contrario
     */
    public boolean ok() { return ok; }
}