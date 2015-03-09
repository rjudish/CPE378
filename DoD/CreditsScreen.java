import greenfoot.*;

/**
 * Write a description of class CreditsScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CreditsScreen extends Screen
{

    /**
     * Constructor for objects of class CreditsScreen.
     * 
     */
    public CreditsScreen()
    {
        super();
        setBackground("images/creditsScreen.png");
    }
    
    void checkClick(MouseInfo mouse) {
        if (mouse.getX() >= 340 && mouse.getX() <= 560) {
            if (mouse.getY() >= 550 && mouse.getY() <= 585) {
                Greenfoot.setWorld(new TitleScreen());
            }
        }
    }
}
