import java.util.*;
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
    private boolean visible;
    private Rectangle leftSide;
    private Rectangle baseLine;
    private ArrayList<Rectangle> heightMarks;

    private Stack<Cup> cups;
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

    public void pushCup(int n) {
        ok = false;

        Cup c = new Cup(n);
        cups.push(c);
        reorganize();
        if (visible) c.makeVisible();
        ok = true;
    }
    
    public void popCup() {
        ok = false;

        if (cups.isEmpty()) return;
        Cup c = cups.pop();
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

        Lid l = new Lid(n);
        lids.push(l);
        reorganize();
        if (visible) l.makeVisible();
        ok = true;
    }
    
    public void popLid() {
        ok = false;

        if (lids.isEmpty()) return;
        Lid l = lids.pop();
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

    public void orderTower() {

        ok = false;

        List<Cup> list = new ArrayList<>(cups);
        Collections.sort(list, Comparator.comparingInt(Cup::getNumber));
        cups.clear();
        cups.addAll(list);
        reorganize();
        
        ok = true;
    }

    public void reverseTower() {

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

    public void makeVisible() {
        visible = true;
        for (Cup c : cups) c.makeVisible();
    }

    public void makeInvisible() {
        visible = false;
        for (Cup c : cups) c.makeInvisible();
    }

    public boolean ok() {
        return ok;
    }
}