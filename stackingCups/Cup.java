
/**
 * Write a description of class Cup here.
 * 
 * @author Paula Díaz 
 * @author Sebastian Granados 
 * 
 * @version 1
 */
public class Cup {

    private int number;
    private int xPos;
    private int yPos;
    private String color;
    private Rectangle leftWall;
    private Rectangle rightWall;
    private Rectangle base;
    protected boolean exists;

    private int sizeCm;
    private int cupHeight;
    private int cupWidth;
    private static final int PIXELES_POR_CM = 20;

    public Cup(int number) {
        this.number = number;
        this.color = assignColor(number);
        this.sizeCm = 2 * number - 1;
        this.cupWidth = sizeCm * PIXELES_POR_CM;
        this.cupHeight = sizeCm * PIXELES_POR_CM;
        this.exists = true;
        
        xPos = 0;
        yPos = 0;
        
        leftWall = new Rectangle();
        rightWall = new Rectangle();
        base = new Rectangle();

        configureShapes();
        positionInitialParts();
    }

    private void configureShapes() {

        leftWall.changeSize(cupHeight, 6);
        leftWall.changeColor(color);

        rightWall.changeSize(cupHeight, 5);
        rightWall.changeColor(color);

        base.changeSize(5, cupWidth);
        base.changeColor(color);
    }
    
    private void positionInitialParts(){
        rightWall.moveHorizontal(cupWidth - 6);
        base.moveVertical(cupHeight - 6);
    }
    
    private String assignColor(int n) {
        switch (n % 6) {
            case 0: return "magenta";
            case 1: return "yellow";
            case 2: return "red";
            case 3: return "blue";
            case 4: return "black";
            default: return "cyan";
        }
    }
    
    
    public void draw(int x, int y) {
    
        int dx = x - xPos;
        int dy = y - yPos;
    
        xPos = x;
        yPos = y;
    
        // Pared izquierda
        leftWall.moveHorizontal(dx);
        leftWall.moveVertical(dy);
    
        // Pared derecha
        rightWall.moveHorizontal(dx);
        rightWall.moveVertical(dy);
    
        // Base
        base.moveHorizontal(dx);
        base.moveVertical(dy);
    }   
       
    public void moveVertical(int distance) {
        yPos += distance;

        leftWall.moveVertical(distance);
        rightWall.moveVertical(distance);
        base.moveVertical(distance);
     }

    public void makeVisible() {
        leftWall.makeVisible();
        rightWall.makeVisible();
        base.makeVisible();
    }
    
    public void makeInvisible() {
        leftWall.makeInvisible();
        rightWall.makeInvisible();
        base.makeInvisible();
    }

    public int getNumber() {
        return number;
    }

    public int getHeight() {
        return cupHeight;
    }
    
    public int getHeightCm(){
        return sizeCm;
    }
    
    public int getWidth(){
        return cupWidth;
    }

}