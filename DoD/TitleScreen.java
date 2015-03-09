import greenfoot.*;

/**
 * Write a description of class TitleScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TitleScreen extends Screen
{

    /**
     * Constructor for objects of class TitleScreen.
     * 
     */
    public TitleScreen()
    {    
        super();
        setBackground("images/titleScreen.png");
    }
    
    void checkClick(MouseInfo mouse) {
        if (mouse.getX() >= 310 && mouse.getY() <= 600) {
            if (mouse.getY() >= 305 && mouse.getY() <= 380) {
                Greenfoot.setWorld(new FactionSelectScreen());
            }
            else if (mouse.getY() >= 380 && mouse.getY() <= 470) {
                Greenfoot.setWorld(new InstructionsScreen());
            }
            else if (mouse.getY() >= 470 && mouse.getY() <= 540) {
                Greenfoot.setWorld(new CreditsScreen());
            }
        }
    }
}
