
/**
 * Write a description of class Lid here.
 * 
 * @author Paula Díaz 
 * @author Sebastian Granados 
 * 
 * @version 1
 */
public class Lid{

    private int number;
    private String color;
    private Rectangle top;
    private int xPos;
    private int yPos;
    
    private static final int PIXELES_POR_CM = 20;
    
    private int lidHeight;
    private int lidWidth;

    public Lid(int number){
        this.number = number;
        this.color = assignColor(number);
        xPos = 0;
        yPos = 0;
        
        int sizeCm = 2 * number - 1;
        this.lidHeight = 1 * PIXELES_POR_CM;
        this.lidWidth = sizeCm * PIXELES_POR_CM;
        
        top = new Rectangle();
        top.changeSize(lidHeight, lidWidth); 
        top.changeColor(color);
    }

    public int getNumber() {
        return number;
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
    
        top.moveHorizontal(dx);
        top.moveVertical(dy);
    }

    public void moveVertical(int distance) {
        yPos += distance;
        top.moveVertical(distance);
    }

    public void moveHorizontal(int distance) {
        xPos += distance;
        top.moveHorizontal(distance);
    }

    public void makeVisible() {
        top.makeVisible();
    }

    public void makeInvisible() {
        top.makeInvisible();
    }
    
    public int getHeight(){
        return lidHeight;
    }
    
    public int getWidth(){
        return lidWidth;
    }
}