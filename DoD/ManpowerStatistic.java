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
    
    public ManpowerStatistic(int initialCount) {
        update(initialCount);
    }

    public void update(int newCount) {
        setImage(new GreenfootImage(text + newCount, 20, Color.BLACK, Color.WHITE));
    }
}
