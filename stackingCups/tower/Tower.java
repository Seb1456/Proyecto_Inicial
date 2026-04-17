package tower;

import java.util.*;
import shapes.Rectangle;
import javax.swing.JOptionPane;

/**
 * Representa una torre que puede contener copas (Cup) y tapas (Lid) apiladas.
 * Las copas se organizan con un algoritmo greedy look-back que anida copas más
 * pequeñas dentro de copas más grandes cuando es posible.
 *
 * @author Paula Díaz
 * @author Sebastian Granados
 * @version 2
 */
public class Tower {

    private int width;
    private int maxHeight;
    private int alturaMaxTorrePixels;
    private int maxCups;
    private int alturaProyeccion;
    private int baseX = 120;
    private int baseY = 260;
    private int alturaTotalPixelsCups = 0;
    private static final int PIXELES_POR_CM = 20;
        
    private boolean visible;
    private boolean ok;
    
    private Rectangle leftSide;
    private Rectangle baseLine;
    private ArrayList<Rectangle> heightMarks;
    protected Stack<Cup> cups;
    protected Stack<Lid> lids;
    
    Set<Integer> lidsAsignadas = new HashSet<>();
    private List<StackItem> pushOrder;



    /**
     * Crea una torre con ancho y altura máxima dados.
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
        this.pushOrder = new ArrayList<>();
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
            for (Rectangle r : heightMarks) {
                r.makeVisible();
            }
        }
    }

    /**
     * Crea una torre con exactamente n copas preapiladas (copa 1 hasta copa n).
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
        this.pushOrder = new ArrayList<>();
        this.ok = true;
        baseX = -35;
        baseY = 450;
        for (int i = 1; i <= cups; i++) pushCup(i);
    }

    /**
     * Agrega una copa normal con el número dado al tope de la torre.
     * Verifica duplicados, altura máxima y límite de copas.
     *
     * @param n número identificador de la copa a agregar
     */
    public void pushCup(int n) {
        ok = false;
        for (Cup existing : cups) {
            if (existing.getNumber() == n) {
                if (visible) JOptionPane.showMessageDialog(null,
                    "Ya existe una copa con el número " + n + " en la torre.",
                    "No se puede añadir", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Cup c = new NormalCup(n);
        int cAltura = c.getHeight();
        int alturaProyeccion = alturaTotalPixelsCups + cAltura;
        int alturaMaxTorrePixels = maxHeight * 40;
        if (alturaProyeccion > alturaMaxTorrePixels) {
            if (visible) JOptionPane.showMessageDialog(null,
                "La altura de las copas superan la altura de la torre",
                "No se puede añadir una copa más.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (maxCups != -1 && cups.size() >= maxCups) {
            if (visible) JOptionPane.showMessageDialog(null,
                "Esta torre solo admite " + maxCups + " copa(s).",
                "Límite alcanzado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cups.push(c);
        pushOrder.add(c);
        c.onPushed(this);
        reorganize1();
        if (visible) c.makeVisible();
        ok = true;
        alturaTotalPixelsCups = alturaProyeccion;
    }

    /**
     * Agrega una copa del tipo especificado al tope de la torre.
     * Los tipos válidos son: "normal", "opener", "hierarchical".
     * Cualquier otro valor crea una copa normal.
     *
     * @param type tipo de copa a crear
     * @param i    número identificador de la copa
     */
    public void pushCup(String type, int i) {
        ok = false;
        for (Cup existing : cups) {
            if (existing.getNumber() == i) {
                if (visible) JOptionPane.showMessageDialog(null,
                    "Ya existe una copa con el número " + i + " en la torre.",
                    "No se puede añadir", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Cup c;
        switch (type.toLowerCase()) {
            case "opener":       c = new OpenerCup(i);       break;
            case "hierarchical": c = new HierarchicalCup(i); break;
            default:             c = new NormalCup(i);       break;
        }
        int cAltura = c.getHeight();
        int alturaProyeccion = alturaTotalPixelsCups + cAltura;
        int alturaMaxTorrePixels = maxHeight * 40;
        if (alturaProyeccion > alturaMaxTorrePixels) {
            if (visible){
            JOptionPane.showMessageDialog(null,
                "La altura de las copas superan la altura de la torre",
                "No se puede añadir una copa más.", JOptionPane.ERROR_MESSAGE);
            }return;
        }
        if (maxCups != -1 && cups.size() >= maxCups) {
            if (visible) JOptionPane.showMessageDialog(null,
                "Esta torre solo admite " + maxCups + " copa(s).",
                "Límite alcanzado", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cups.push(c);
        pushOrder.add(c);
        c.onPushed(this);
        reorganize1();
        if (visible) c.makeVisible();
        ok = true;
        alturaTotalPixelsCups = alturaProyeccion;
    }

    /**
     * Elimina la copa del tope de la torre.
     * Verifica si la copa puede ser eliminada antes de proceder.
     */
    public void popCup() {
        ok = false;
        if (cups.isEmpty()) {
            if (visible) JOptionPane.showMessageDialog(null,
                "No es posible hacer pop cuando no hay copas.",
                "No hay copas en la torre.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Cup c = cups.peek();
        if (!c.canBeRemoved(this)) {
            if (visible) JOptionPane.showMessageDialog(null,
                "Esta copa no puede ser eliminada.",
                "No se puede quitar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        cups.pop();
        pushOrder.remove(c);
        alturaTotalPixelsCups -= c.getHeight();
        c.makeInvisible();
        checkLidExistence();
        reorganize1();
        ok = true;
    }

    /**
     * Elimina la copa con el número dado de cualquier posición de la torre.
     * Verifica si la copa puede ser eliminada antes de proceder.
     * La verificación se hace mientras la copa aún está en la pila,
     * para que canBeRemoved pueda consultar correctamente su posición.
     *
     * @param n número identificador de la copa a eliminar
     */
    public void removeCup(int n) {
        ok = false;

        Cup targetCup = null;
        for (Cup c : cups) {
            if (c.getNumber() == n) { targetCup = c; break; }
        }
        if (targetCup == null) {
            reorganize1();
            return;
        }
        if (!targetCup.canBeRemoved(this)) {
            if (visible) JOptionPane.showMessageDialog(null,
                "Esta copa no puede ser eliminada.",
                "No se puede quitar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Stack<Cup> temp = new Stack<>();
        while (!cups.isEmpty()) {
            Cup c = cups.pop();
            if (c.getNumber() == n) {
                c.makeInvisible();
                alturaTotalPixelsCups -= c.getHeight();
                break;
            }
            temp.push(c);
        }
        while (!temp.isEmpty()) cups.push(temp.pop());
        pushOrder.remove(targetCup);
        checkLidExistence();
        reorganize1();
        ok = true;
    }

    /**
     * Agrega una tapa normal con el número dado a la torre.
     * Verifica duplicados y altura máxima.
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
        Lid nueva = new NormalLid(n);
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
        pushOrder.add(nueva);
        if (visible) nueva.makeVisible();
        reorganize1();
        ok = true;
    }

    /**
     * Agrega una tapa del tipo especificado a la torre.
     * Los tipos válidos son: "normal", "fearful", "crazy".
     * Cualquier otro valor crea una tapa normal.
     *
     * @param type tipo de tapa a crear
     * @param i    número identificador de la tapa
     */
    public void pushLid(String type, int i) {
        ok = false;
        for (Lid l : lids) {
            if (l.getNumber() == i) {
                if (visible) JOptionPane.showMessageDialog(null,
                    "Ya existe una tapa con el número " + i,
                    "No se puede añadir", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        Lid nueva;
        switch (type.toLowerCase()) {
            case "fearful": nueva = new FearfulLid(i); break;
            case "crazy":   nueva = new CrazyLid(i);   break;
            case "anchor": nueva = new AnchorLid(i); break;
            default:        nueva = new NormalLid(i);   break;
        }
        if (!nueva.shouldExist(this)) {
            if (visible) JOptionPane.showMessageDialog(null,
                "La tapa " + i + " no puede entrar: su taza compañera no está en la torre.",
                "No se puede añadir", JOptionPane.ERROR_MESSAGE);
            return;
        }
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
        pushOrder.add(nueva);
        if (visible) nueva.makeVisible();
        reorganize1();
        ok = true;
    }

    /**
     * Elimina la tapa del tope de la pila de tapas.
     * Verifica si la tapa puede ser eliminada antes de proceder.
     */
    public void popLid() {
        ok = false;
        if (lids.isEmpty()) {
            if (visible) JOptionPane.showMessageDialog(null,
                "No es posible hacer pop cuando no hay tapas.",
                "No hay tapas en la torre.", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Lid l = lids.peek();
        if (!l.canBeRemoved(this)) {
            if (visible) JOptionPane.showMessageDialog(null,
                "Esta tapa no puede ser eliminada.",
                "No se puede quitar", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lids.pop();
        pushOrder.remove(l);
        alturaTotalPixelsCups -= l.getHeight();
        lidsAsignadas.remove(l.getNumber());
        l.makeInvisible();
        reorganize1();
        ok = true;
    }

    /**
     * Elimina la tapa con el número dado de cualquier posición de la pila.
     * Verifica si la tapa puede ser eliminada antes de proceder.
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
                if (!l.canBeRemoved(this)) {
                    temp.push(l);
                    while (!temp.isEmpty()) lids.push(temp.pop());
                    if (visible) JOptionPane.showMessageDialog(null,
                        "Esta tapa no puede ser eliminada.",
                        "No se puede quitar", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                alturaLid = l.getHeight();
                l.makeInvisible();
                pushOrder.remove(l);
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
     * Ordena las copas de menor a mayor número.
     */
    public void reverseTower() {
        ok = false;
        List<Cup> list = new ArrayList<>(cups);
        Collections.sort(list, Comparator.comparingInt(Cup::getNumber));
        cups.clear();
        cups.addAll(list);
        rebuildCupOrderInPushOrder(list);
        reorganize1();
        ok = true;
    }

    /**
     * Invierte el orden actual de las copas en la torre.
     */
    public void orderTower() {
        ok = false;
        Collections.reverse(cups);
        rebuildCupOrderInPushOrder(new ArrayList<>(cups));
        reorganize1();
        ok = true;
    }

    /**
     * Reorganiza visualmente las copas y tapas sin anidamiento.
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
     * Reorganiza visualmente usando greedy look-back unificado para copas y tapas.
     * Los elementos se posicionan en el orden en que fueron apilados (pushOrder).
     * Solo las copas actúan como contenedores; las tapas asignadas se dibujan
     * encima de su copa mediante posicionarTapas.
     */
    void reorganize1() {
        int centroTorreX = baseX + (width * 40) / 2;
        int n = pushOrder.size();
        int[] drawnX       = new int[n];
        int[] drawnY       = new int[n];
        int[] espacioUsado = new int[n];
        int stackingY = baseY;
        for (int i = 0; i < n; i++) {
            StackItem item = pushOrder.get(i);
            if (!item.isContainer() && lidsAsignadas.contains(item.getNumber())) continue;
            int iW   = item.getWidth();
            int iH   = item.getHeight();
            int sink = item.lidTheCup();
            boolean nested = false;
            for (int j = i - 1; j >= 0; j--) {
                StackItem candidate = pushOrder.get(j);
                if (!candidate.isContainer()) continue;
                int libre = candidate.getHeight() - espacioUsado[j];
                if (iW < candidate.getWidth() && iH <= libre) {
                    drawnX[i] = drawnX[j] + (candidate.getWidth() - iW) / 2;
                    drawnY[i] = drawnY[j] + candidate.getHeight() - espacioUsado[j] - iH + sink;
                    espacioUsado[j] += iH;
                    nested = true;
                    break;
                }
            }
            if (!nested) {
                drawnX[i] = centroTorreX - iW / 2;
                drawnY[i] = stackingY - iH + sink;
                stackingY = drawnY[i];
            }
            item.draw(drawnX[i], drawnY[i]);
        }
        posicionarTapas(drawnX, drawnY);
    }

    /**
     * Posiciona las tapas asignadas encima de su copa usando las posiciones
     * calculadas por reorganize1.
     */
    private void posicionarTapas(int[] drawnX, int[] drawnY) {
        int centroTorreX = baseX + (width * 40) / 2;
        int stackingY    = baseY;
        for (Lid l : lids) {
            if (!lidsAsignadas.contains(l.getNumber())) continue;
            boolean found = false;
            for (int i = 0; i < pushOrder.size(); i++) {
                StackItem item = pushOrder.get(i);
                if (item.isContainer() && item.getNumber() == l.getNumber()) {
                    int yPos = drawnY[i] + l.getDrawYOffset(item) + l.lidTheCup();
                    l.draw(drawnX[i] + (item.getWidth() - l.getWidth()) / 2, yPos);
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

    /** Intercambia dos elementos en pushOrder por referencia. */
    private void swapInPushOrder(StackItem a, StackItem b) {
        int ia = pushOrder.indexOf(a), ib = pushOrder.indexOf(b);
        if (ia == -1 || ib == -1) return;
        pushOrder.set(ia, b);
        pushOrder.set(ib, a);
    }

    /** Reemplaza las entradas de copas en pushOrder con el nuevo orden dado. */
    private void rebuildCupOrderInPushOrder(List<Cup> newOrder) {
        List<Integer> pos = new ArrayList<>();
        for (int i = 0; i < pushOrder.size(); i++) {
            if (pushOrder.get(i).isContainer()) pos.add(i);
        }
        for (int i = 0; i < pos.size() && i < newOrder.size(); i++) {
            pushOrder.set(pos.get(i), newOrder.get(i));
        }
    }

    /**
     * Verifica si existe una tapa con el número dado en la pila de tapas.
     */
    private boolean tieneTapa(int cupNumber) {
        for (Lid l : lids) {
            if (l.getNumber() == cupNumber) return true;
        }
        return false;
    }

    /**
     * Elimina automáticamente las tapas cuyo método shouldExist retorna false.
     * Se invoca tras cada eliminación de copa.
     */
    private void checkLidExistence() {
        List<Lid> allLids = new ArrayList<>(lids);
        lids.clear();
        for (Lid l : allLids) {
            if (l.shouldExist(this)) {
                lids.push(l);
            } else {
                lidsAsignadas.remove(l.getNumber());
                alturaTotalPixelsCups -= l.getHeight();
                l.makeInvisible();
                pushOrder.remove(l);
            }
        }
    }

     /**
     * Elimina únicamente las tapas que bloquean el paso de la copa dada.
     * Una tapa bloquea a la copa opener si el número de la tapa es mayor o
     * igual al número de la copa (es decir, la tapa es tan ancha o más ancha).
     * Las tapas más angostas se conservan.
     *
     * Invocado por OpenerCup al ser apilada.
     *
     * @param opener la copa que acaba de entrar y limpia su camino
     */
    void clearLidsBlocking(Cup opener) {
        List<Lid> toKeep   = new ArrayList<>();
        List<Lid> toRemove = new ArrayList<>();
 
        for (Lid l : lids) {
            // La tapa bloquea si su número >= número de la copa opener
            // (igual o mayor ancho que la copa)
            if (l.getNumber() >= opener.getNumber()) {
                toRemove.add(l);
            } else {
                toKeep.add(l);
            }
        }
 
        for (Lid l : toRemove) {
            lidsAsignadas.remove(l.getNumber());
            pushOrder.remove(l);
            l.makeInvisible();
        }
        // Recalcular alturaTotalPixelsCups solo para las que sí se eliminan
        // y no estaban anidadas (el cálculo exacto depende de si estaban
        // asignadas o libres; por simplicidad recalculamos desde cero).
        lids.clear();
        lids.addAll(toKeep);
 
        // Recalcular altura total
        alturaTotalPixelsCups = 0;
        for (Cup c : cups) {
            alturaTotalPixelsCups += c.getHeight();
        }
        for (Lid l : lids) {
            boolean cabeEnCup = false;
            for (Cup c : cups) {
                if (l.getWidth() < c.getWidth() && l.getHeight() <= c.getHeight()) {
                    cabeEnCup = true;
                    break;
                }
            }
            if (!cabeEnCup) {
                alturaTotalPixelsCups += l.getHeight();
            }
        }
 
        reorganize1();
    }

    /**
     * Indica si existe una copa con el número dado en la torre.
     *
     * @param n número de la copa a buscar
     * @return true si la copa está en la torre
     */
    public boolean hasCup(int n) {
        for (Cup c : cups) {
            if (c.getNumber() == n) return true;
        }
        return false;
    }

    /**
     * Cuenta cuántas copas tienen una tapa asignada.
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
     * Retorna un arreglo con todos los elementos de la torre en orden de fondo a tope.
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
        for (int i = 0; i < items.size(); i++) {
            result[i] = items.get(i);
        }
        return result;
    }

    /**
     * Asigna una tapa a cada copa de la torre.
     * Si una copa no tiene tapa, crea una nueva con pushLid().
     */
    public void cover() {
        ok = false;
        for (Cup c : cups) {
            boolean hasLid = false;
            for (Lid l : lids) {
                if (l.getNumber() == c.getNumber()) {
                    hasLid = true; break;
                }
            }
            if (!hasLid) pushLid(c.getNumber());
            lidsAsignadas.add(c.getNumber());
        }
        reorganize();
        ok = true;
    }

    /**
     * Intercambia la posición de dos elementos en la torre.
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
     * Encuentra el intercambio entre dos copas que más reduce la altura visual.
     *
     * @return String[][] con los dos elementos a intercambiar, o null si ningún swap reduce la altura
     */
    public String[][] swapToReduce() {
        String[][] items   = stackingItems();
        int alturaActual   = heightVisual();
        String[] mejorO1   = null;
        String[] mejorO2   = null;
        int mejorReduccion = 0;
        boolean wasVisible = this.visible;
        this.visible = false;

        List<Cup> cupList = new ArrayList<>(cups);

        for (int i = 0; i < items.length; i++) {
            if (!items[i][0].equals("cup")) continue;
            for (int j = i + 1; j < items.length; j++) {
                if (!items[j][0].equals("cup")) continue;
                int numI = Integer.parseInt(items[i][1]);
                int numJ = Integer.parseInt(items[j][1]);
                int idxI = -1, idxJ = -1;
                for (int k = 0; k < cupList.size(); k++) {
                    if (cupList.get(k).getNumber() == numI) idxI = k;
                    if (cupList.get(k).getNumber() == numJ) idxJ = k;
                }
                if (idxI == -1 || idxJ == -1) continue;
                Cup temp = cupList.get(idxI);
                cupList.set(idxI, cupList.get(idxJ));
                cupList.set(idxJ, temp);
                cups.clear(); cups.addAll(cupList);
                int reduccion = alturaActual - heightVisual();
                temp = cupList.get(idxI);
                cupList.set(idxI, cupList.get(idxJ));
                cupList.set(idxJ, temp);
                cups.clear(); cups.addAll(cupList);
                if (reduccion > mejorReduccion) {
                    mejorReduccion = reduccion;
                    mejorO1 = items[i];
                    mejorO2 = items[j];
                }
            }
        }
        this.visible = wasVisible;
        return mejorO1 != null ? new String[][]{ mejorO1, mejorO2 } : null;
    }

    /** Intercambia dos copas por sus números. */
    private void swapCupCup(int num1, int num2) {
        List<Cup> list = new ArrayList<>(cups);
        int idx1 = -1, idx2 = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNumber() == num1) idx1 = i;
            if (list.get(i).getNumber() == num2) idx2 = i;
        }
        if (idx1 == -1 || idx2 == -1) return;
        Cup c1 = list.get(idx1), c2 = list.get(idx2);
        list.set(idx1, c2);
        list.set(idx2, c1);
        cups.clear();
        cups.addAll(list);
        swapInPushOrder(c1, c2);
        if (lidsAsignadas.contains(num1) || lidsAsignadas.contains(num2)) reorganize();
        else reorganize1();
        ok = true;
    }

    /** Intercambia dos tapas por sus números. */
    private void swapLidLid(int num1, int num2) {
        List<Lid> list = new ArrayList<>(lids);
        int idx1 = -1, idx2 = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNumber() == num1) idx1 = i;
            if (list.get(i).getNumber() == num2) idx2 = i;
        }
        if (idx1 == -1 || idx2 == -1) return;
        Lid l1 = list.get(idx1), l2 = list.get(idx2);
        list.set(idx1, l2);
        list.set(idx2, l1);
        lids.clear();
        lids.addAll(list);
        swapInPushOrder(l1, l2);
        if (lidsAsignadas.contains(num1) || lidsAsignadas.contains(num2)) reorganize();
        else reorganize1();
        ok = true;
    }

    /** Intercambia una copa con una tapa. */
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
        cups.clear(); cups.addAll(cupList);
        lids.clear(); lids.addAll(lidList);
        alturaTotalPixelsCups += alturaExtra;
        swapInPushOrder(targetCup, targetLid);
        if (lidsAsignadas.contains(cupNum)) reorganize();
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
     *
     * @return altura visual real en cm
     */
    public int heightVisual() {
        List<Cup> list     = new ArrayList<>(cups);
        int[] bottomPos    = new int[list.size()];
        int[] espacioUsado = new int[list.size()];
        int maxTop         = 0;
        int stackingBottom = 0;

        for (int i = 0; i < list.size(); i++) {
            Cup c = list.get(i);
            boolean nested = false;
            for (int j = i - 1; j >= 0; j--) {
                Cup candidate = list.get(j);
                if (c.getNumber() < candidate.getNumber()) {
                    bottomPos[i] = bottomPos[j] + 1 + espacioUsado[j];
                    espacioUsado[j] += c.getHeightCm();
                    nested = true;
                    break;
                }
            }
            if (!nested) {
                bottomPos[i] = stackingBottom;
                stackingBottom += c.getHeightCm();
            }
            int top = bottomPos[i] + c.getHeightCm();
            if (top > maxTop) maxTop = top;
        }
        return maxTop;
    }

    /** Hace visible la torre con todas sus copas y tapas. */
    public void makeVisible() {
        visible = true;
        for (Cup c : cups) {
            c.makeVisible();
        }
        for (Lid l : lids) {
            l.makeVisible();
        }
    }

    /** Hace invisible la torre con todas sus copas y tapas. */
    public void makeInvisible() {
        visible = false;
        for (Cup c : cups) {
            c.makeInvisible();
        }
        for (Lid l : lids) {
            l.makeInvisible();
        }
    }

    /**
     * Indica si la última operación realizada fue exitosa.
     *
     * @return true si la última operación fue exitosa, false en caso contrario
     */
    public boolean ok() {
        return ok;
    }
}
