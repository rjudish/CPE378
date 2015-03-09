import greenfoot.*;

/**
 * Write a description of class Screen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Screen extends World
{

    /**
     * Constructor for objects of class Screen.
     * 
     */
    public Screen()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(900, 600, 1);
    }
    
    public void act() {
        if (Greenfoot.mousePressed(this)) {
           checkClick(Greenfoot.getMouseInfo());
        }
    }
    
    abstract void checkClick(MouseInfo mouse);
}
