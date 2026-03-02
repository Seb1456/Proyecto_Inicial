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
    private boolean visible;
    private Rectangle leftSide;
    private Rectangle baseLine;
    private ArrayList<Rectangle> heightMarks;
    private static final int PIXELES_POR_CM = 20;
    protected Stack<Cup> cups;
    private int alturaTotalPixelsCups = 0;
    private int alturaProyeccion;
    private Stack<Lid> lids;
    private boolean ok;

    private int baseX = 120;
    private int baseY = 260;

    public Tower(int width, int maxHeight) {

        this.width = width;
        this.maxHeight = maxHeight;
        this.visible = true;
        this.cups = new Stack<>();
        this.lids = new Stack<>();
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
    
    public Tower(int cups) {
        if(cups < 1){
            cups = 1;
        }
        
        int cupsNumber = cups;
        
        int sizeCm = 2 * cupsNumber - 1;             
        int cupWidthPixels  = sizeCm * PIXELES_POR_CM;    
        int cupHeightPixels = sizeCm * PIXELES_POR_CM;
        this.width     = cupsNumber + 2;         
        this.maxHeight = sizeCm * cups;        
        this.visible = true;
        this.cups = new Stack<>();
        this.lids = new Stack<>();
        this.ok = true;
        baseX = -35;
        baseY = 450;
    
        leftSide = new Rectangle();
        baseLine = new Rectangle();
        heightMarks = new ArrayList<>();

        int towerHeightPixels = maxHeight * 40;
        int towerWidthPixels  = width * 40;
    
        // Pared izquierda
        leftSide.changeSize(towerHeightPixels, 5);
        leftSide.moveHorizontal(baseX);
        leftSide.moveVertical(baseY - towerHeightPixels);
        leftSide.changeColor("black");
    
        // Base
        baseLine.changeSize(5, towerWidthPixels);
        baseLine.moveHorizontal(baseX);
        baseLine.moveVertical(baseY);
        baseLine.changeColor("black");
    
        // Marcas de altura 
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
        
        for (int i = 1; i <= cups; i++) {
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
        cups.push(c);
        reorganize();
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
        reorganize();
        
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

        reorganize();
        ok = found;
    }
    
    public void pushLid(int n) {
        ok = false;
        for(Lid l: lids){
            if(l.getNumber() == n){
                if(visible){
                    JOptionPane.showMessageDialog(null, "Ya existe una tapa con el número " + n + " en la torre.", "No se puede añadir",
                    JOptionPane.ERROR_MESSAGE);
                    ok = false;
                    return;
                }
                ok = false;
                return;
            }
        }
        
        Cup objLid = null;
            for (Cup c : cups) {
                if (c.getNumber() == n) {
                    objLid = c;
                    break;
                }
            }
        
            if (objLid == null) {
                ok = false;
                return;
            }
            Lid l = new Lid(n);
            objLid.setLid(l); 
            lids.push(l);
            reorganize();
            if (visible) l.makeVisible();
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
        
        for (Cup c : cups) {
            if (c.getLid() != null && c.getLid().getNumber() == l.getNumber()) {
                c.setLid(null);     
            break;
            }
        }
        l.makeInvisible();
        reorganize();
        
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

        reorganize();
        ok = found;
    }

    public void reverseTower() {

        ok = false;

        List<Cup> list = new ArrayList<>(cups);
        Collections.sort(list, Comparator.comparingInt(Cup::getNumber));
        cups.clear();
        cups.addAll(list);
        reorganize();
        
        ok = true;
    }

    public void orderTower() {

        ok = false;

        Collections.reverse(cups);
        reorganize();
        
        ok = true;
    }

    private void reorganize() {
        int currentY = baseY;
        int torreWidthPixels = width * 40;                  
        int centroTorreX = baseX + (torreWidthPixels / 2);
    
        for (Cup c : cups) {
            int mitadAnchoTaza = c.getWidth() / 2;
            int xCentrada      = centroTorreX - mitadAnchoTaza;
            c.draw(xCentrada, currentY - c.getHeight());
            
            if (c.getLid() != null) {
                Lid l = c.getLid();
                int mitadAncholid = l.getWidth() / 2;
                int lidX = centroTorreX - mitadAncholid;
                int lidY = currentY - c.getHeight();
                l.draw(lidX, lidY);
            }
    
            currentY -= c.getHeight();
        }
        
        for (Lid l : lids) {
            int mitadAncho = l.getWidth() / 2;
            int lidX = centroTorreX - mitadAncho;
        
            boolean found = false;
            currentY = baseY;
        
            for (Cup c : cups) {
                if (c.getNumber() == l.getNumber()) {
                    int lidY = currentY - c.getHeight();
                    l.draw(lidX, lidY);
                    found = true;
                    break;
                }
                currentY -= c.getHeight();
            }
        
            if (!found) {
                int lidY = baseY - (maxHeight * 40) - 50;
                l.draw(lidX, lidY);
            }
        }
    }
    
    public int lidedCups() {
        int count = 0;
        for (Cup cup : cups) {
            if (cup.getLid() != null) {
                count++;
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
    
            if (cup.getLid() != null) {
                items.add(new String[]{"lid", String.valueOf(cup.getLid().getNumber())});
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
        for(Cup c : cups){
            if(c.getLid() == null){
                pushLid(c.getNumber());
            }
        }
        reorganize();
        ok = true;
    }
    
    public void swap(String[] o1, String[] o2){
        
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