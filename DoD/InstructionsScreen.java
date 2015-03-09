import greenfoot.*;

/**
 * Write a description of class InstructionsScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class InstructionsScreen extends Screen
{

    int screen = 1;
    private static final int NUM_SCREENS = 3;
    /**
     * Constructor for objects of class InstructionsScreen.
     * 
     */
    public InstructionsScreen()
    {    
        super();
        setBackground("images/instructions1.png");
    }
    
    void checkClick(MouseInfo mouse) {
        if (mouse.getY() >= 545 && mouse.getY() <= 580) {
            if (mouse.getX() >= 50 && mouse.getX() <= 275) {
                Greenfoot.setWorld(new TitleScreen());
            }
            else if (mouse.getX() >= 775 && mouse.getX() <= 850) {
                if (screen < NUM_SCREENS) {
                    setBackground("images/instructions" + (++screen) + ".png");
                }
            }
        }
    }
}
