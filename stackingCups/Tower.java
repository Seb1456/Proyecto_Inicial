import java.util.*;
import javax.swing.JOptionPane;
/**
 * Class Tower
 * 
 * @author Paula Díaz 
 * @author Sebastian Granados 
 * 
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
    private boolean ok;
    
    private int baseX = 120;
    private int baseY = 260;

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
    
        // Pared
        leftSide.changeSize(towerHeightPixels, 5);
        leftSide.moveHorizontal(baseX);
        leftSide.moveVertical(baseY - towerHeightPixels);
        leftSide.changeColor("black");

        // Base
        baseLine.changeSize(5, towerWidthPixels);
        baseLine.moveHorizontal(baseX);
        baseLine.moveVertical(baseY);
        baseLine.changeColor("black");
    
        // Marca para cada cm
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
    
        
    public Tower(int cups){

        if(cups < 1){
            cups = 1;
        }
    
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
    
        for(int i = 1; i <= cups; i++){
            pushCup(i);
        }
    }
    
    public void pushCup(int n) {
        ok = false;
        for(Cup c: cups){
            if(c.getNumber() == n){
                if(visible){
                    JOptionPane.showMessageDialog(null, "Ya existe una copa con el número " + n + " en la torre.", "No se puede añadir",
                    JOptionPane.ERROR_MESSAGE);
                    ok = false;
                    return;
                }
                ok = false;
                return;
            }
        }
        Cup c = new Cup(n);
        int cAltura = c.getHeight();
        int alturaProyeccion = alturaTotalPixelsCups + cAltura;
        int alturaMaxTorrePixels = maxHeight * 40;
        if (alturaProyeccion > alturaMaxTorrePixels){
            if(visible){
                JOptionPane.showMessageDialog(null, "La altura de las copas superan la altura de la torre", 
                "No se puede añadir una copa más.",
                JOptionPane.ERROR_MESSAGE);
                ok = false;
                return;
            }
            ok = false;
            return;
        }
        
        if (maxCups != -1 && cups.size() >= maxCups) {
            if (visible) {
                JOptionPane.showMessageDialog(null,
                    "Esta torre solo admite " + maxCups + " copa(s).",
                    "Límite alcanzado",
                    JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        cups.push(c);
        reorganize1();
        if (visible) c.makeVisible();    
        ok = true;
        alturaTotalPixelsCups = alturaProyeccion;
    }
    
    public void popCup() {
        ok = false;

        if (cups.isEmpty()){
            if(visible){
                JOptionPane.showMessageDialog(null, "No es posible hacer pop cuando no hay copas.", 
                "No hay copas en la torre.",
                JOptionPane.ERROR_MESSAGE);
                ok = false;
                return;
            }
            ok = false;
            return;
        }
        Cup c = cups.pop();
        alturaTotalPixelsCups -= c.getHeight();
        c.makeInvisible();
        reorganize1();
        
        ok = true;
    }

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
        
        while (!temp.isEmpty()) {
            cups.push(temp.pop());
        }

        reorganize1();
        ok = found;
    }
    
    public void pushLid(int n){
    
        ok = false;
    
        for(Lid l : lids){
            if(l.getNumber() == n){
                return;
            }
        }
    
        Lid l = new Lid(n);
    
        lids.push(l);
    
        if(visible){
            l.makeVisible();
        }
    
        reorganize1();
    
        ok = true;
    }
    
    public void popLid() {
        ok = false;

        if (lids.isEmpty()){
            if(visible){
                JOptionPane.showMessageDialog(null, "No es posible hacer pop cuando no hay tapas.", 
                "No hay tapas en la torre.",
                JOptionPane.ERROR_MESSAGE);
                ok = false;
                return;
            }
            ok = false;
            return;
        }
        Lid l = lids.pop();
        l.makeInvisible();
        reorganize1();
        
        ok = true;
    }
    
    public void removeLid(int n) {

        ok = false;
        Stack<Lid> temp = new Stack<>();
        boolean found = false;

        while (!lids.isEmpty()) {
            Lid l = lids.pop();
            if (l.getNumber() == n) {
                l.makeInvisible();
                found = true;
                break;
            }
            temp.push(l);
        }

        while (!temp.isEmpty()) {
            lids.push(temp.pop());
        }

        reorganize1();
        ok = found;
    }

    public void reverseTower() {

        ok = false;

        List<Cup> list = new ArrayList<>(cups);
        Collections.sort(list, Comparator.comparingInt(Cup::getNumber));
        cups.clear();
        cups.addAll(list);
        
        boolean algunaTieneTapa = false;
        for (Cup c : cups) {
            if (tieneTapa(c.getNumber())) {
                algunaTieneTapa = true;
                break;
            }
        }
        
        if(algunaTieneTapa){
            reorganize();
        }else{
            reorganize1();
        }        
        
        
        ok = true;
    }

    public void orderTower() {

        ok = false;

        Collections.reverse(cups);
        
        boolean algunaTieneTapa = false;
        for (Cup c : cups) {
            if (tieneTapa(c.getNumber())) {
                algunaTieneTapa = true;
                break;
            }
        }
        
        if(algunaTieneTapa){
            reorganize();
        }else{
            reorganize1();
        }        
        ok = true;
    }

    private void reorganize() {

        int currentY = baseY;
        int torreWidthPixels = width * 40;
        int centroTorreX = baseX + (torreWidthPixels / 2);
    
        for (Cup c : cups) {
    
            int mitadAncho = c.getWidth() / 2;
            int xCentrada = centroTorreX - mitadAncho;
            int y = currentY - c.getHeight();
    
            c.draw(xCentrada, y);
    
            currentY -= c.getHeight();
        }

        for (Lid l : lids) {
    
            int mitadAncho = l.getWidth() / 2;
            int lidX = centroTorreX - mitadAncho;
    
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
    
            if (!found) {
    
                int lidY = baseY - (maxHeight * 40) - 50;
                l.draw(lidX, lidY);
            }
        }
    }
    
    private void reorganize1() {
        List<Cup> list   = new ArrayList<>(cups);
        int[] drawnX     = new int[list.size()];
        int[] drawnY     = new int[list.size()];
    
        posicionarCopas(list, drawnX, drawnY);
        posicionarTapas(list, drawnX, drawnY);
    }
    
    private void posicionarCopas(List<Cup> list, int[] drawnX, int[] drawnY) {
        int centroTorreX  = baseX + (width * 40) / 2;
        int stackingY     = baseY;
        int[] espacioUsado = new int[list.size()];
    
        for (int i = 0; i < list.size(); i++) {
            Cup c = list.get(i);
            boolean nested = false;
    
            // Look-back greedy
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
    
    private void posicionarTapas(List<Cup> list, int[] drawnX, int[] drawnY) {
        int centroTorreX = baseX + (width * 40) / 2;
    
        for (Lid l : lids) {
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
                l.draw(centroTorreX - l.getWidth() / 2, baseY - (maxHeight * 40) - 50);
            }
        }
    }
    
    private boolean tieneTapa(int cupNumber) {
        for (Lid l : lids) {
            if (l.getNumber() == cupNumber) return true;
        }
        return false;
    }
    
    public int lidedCups() {
        int count = 0;
    
        for (Cup c : cups) {
    
            for (Lid l : lids) {
    
                if (c.getNumber() == l.getNumber()) {
                    count++;
                    break;
                }
            }
        }
    
        return count;
    }
    
    public String[][] stackingItems() {

        List<String[]> items = new ArrayList<>();
    
        Stack<Cup> temp = new Stack<>();
    
        while (!cups.isEmpty()) {
            temp.push(cups.pop());
        }
    
        while (!temp.isEmpty()) {
    
            Cup cup = temp.pop();
    
            items.add(new String[]{"cup", String.valueOf(cup.getNumber())});
    
            // buscar si existe una tapa con el nuero de la copa
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
    
    public void cover(){
        ok = false;
        for (Cup c : cups) {
    
            boolean hasLid = false;
    
            for (Lid l : lids) {
                if (l.getNumber() == c.getNumber()) {
                    hasLid = true;
                    break;
                }
            }
    
            if (!hasLid) {
                pushLid(c.getNumber());
            }
        }
        reorganize1();
    
        ok = true;
    }
    
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
        
        if (tieneTapa(num1) || tieneTapa(num2)) {
            reorganize();
        } else {
            reorganize1();
        }
        
        ok = true;
    }
    
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
        
        if (tieneTapa(num1) || tieneTapa(num2)) {
            reorganize();
        } else {
            reorganize1();
        }
        ok = true;
    }
    
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
            if (visible) {
                JOptionPane.showMessageDialog(null,
                    "La copa no cabe en la posición de la tapa.",
                    "Swap inválido", JOptionPane.ERROR_MESSAGE);
            }
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
        
        
        if (tieneTapa(cupNum)) {
            reorganize();
        } else {
            reorganize1();
        }
        ok = true;
    }
        
    public int height(){
        int alturaTotal = 0;
        for(Cup c: cups){ 
            alturaTotal += c.getHeightCm();
        }
        return alturaTotal;
    }
    
    public void makeVisible() {
        visible = true;
        for (Cup c : cups) c.makeVisible();
        for (Lid l : lids) l.makeVisible();
    }

    public void makeInvisible() {
        visible = false;
        for (Cup c : cups) c.makeInvisible();
        for (Lid l : lids) l.makeInvisible();
    }

    public boolean ok() {
        return ok;
    }
}