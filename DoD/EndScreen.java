import greenfoot.*;

/**
 * Write a description of class EndScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EndScreen extends World
{

    /**
     * Constructor for objects of class EndScreen.
     * 
     */
    public EndScreen(boolean win)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(900, 600, 1);
        if (win) {
            setBackground("images/winScreen.png");
        }
        else {
            setBackground("images/loseScreen.png");
        }
        Greenfoot.stop();
    }
}
