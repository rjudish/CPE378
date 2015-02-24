import greenfoot.*;
import java.awt.Color;
/**
 * Write a description of class TerritoryStatistic here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TerritoryStatistic extends Actor
{
    private static final String text = "Territories: ";
    public TerritoryStatistic() {
        setImage(new GreenfootImage(text, 20, Color.BLACK, Color.WHITE));
    }
    
    public TerritoryStatistic(int initialCount) {
       update(initialCount);
    }
    
    public void update(int newCount) {
        setImage(new GreenfootImage(text + newCount, 20, Color.BLACK, Color.WHITE));
    }    
}
