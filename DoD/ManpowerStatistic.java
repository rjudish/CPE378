import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class ManpowerStatistic here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ManpowerStatistic extends Actor
{
    private static final String text = "Manpower: ";
    public ManpowerStatistic() {
        setImage(new GreenfootImage(text, 20, Color.BLACK, Color.WHITE));
    }
    /**
     * Act - do whatever the ManpowerStatistic wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
         setImage(new GreenfootImage(text + "400", 20, Color.BLACK, Color.WHITE));
    }    
}
