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
    private static final String text = "Territories captured: ";
    public TerritoryStatistic() {
        setImage(new GreenfootImage(text, 20, Color.BLACK, Color.WHITE));
    }
    /**
     * Act - do whatever the TerritoryStatistic wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        setImage(new GreenfootImage(text + "400", 20, Color.BLACK, Color.WHITE));
    }    
}
