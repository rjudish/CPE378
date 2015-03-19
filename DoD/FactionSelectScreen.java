import greenfoot.*;

/**
 * Write a description of class FactionSelectScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FactionSelectScreen extends Screen
{

    /**
     * Constructor for objects of class FactionSelectScreen.
     * 
     */
    public FactionSelectScreen()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super();
        setBackground("images/opponentsScreen.png");
        music = new GreenfootSound("Broken-Mbit-World.wav");
    }
    
    void checkClick(MouseInfo mouse){ //Only odd number of opponents
        if (mouse.getX() >= 390 && mouse.getX() <= 510) {
            if (mouse.getY() >= 170 && mouse.getY() <= 265) {
                music.playLoop();
                Greenfoot.setWorld(new Map(1));
            }
            else if (mouse.getY() >= 265 && mouse.getY() <= 350) {
                music.playLoop();
                Greenfoot.setWorld(new Map(3));
            }
            else if (mouse.getY() >= 350 && mouse.getY() <= 435) {
                music.playLoop();
                Greenfoot.setWorld(new Map(5));
            }
            else if (mouse.getY() >= 435 && mouse.getY() <= 520) {
                music.playLoop();
                Greenfoot.setWorld(new Map(7));
            }
        }
    }
}
