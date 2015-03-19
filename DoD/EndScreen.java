import greenfoot.*;

/**
 * Write a description of class EndScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EndScreen extends Screen
{

    /**
     * Constructor for objects of class EndScreen.
     * 
     */
    public EndScreen(boolean win)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super();
        music.stop();
        if (win) {
            setBackground("images/winScreen.png");
            music = new GreenfootSound("Ta Da.wav");
            music.play();
        }
        else {
            setBackground("images/loseScreen.png");
            music = new GreenfootSound("GameoverOne.wav");
            music.play();
        }
    }
    
    void checkClick(MouseInfo mouse) {
        if (mouse.getX() >= 340 && mouse.getX() <= 560) {
            if (mouse.getY() >= 550 && mouse.getY() <= 585) {
                Greenfoot.setWorld(new TitleScreen());
            }
        }
    }
}
