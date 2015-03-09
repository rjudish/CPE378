import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class TroopIcon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TroopIcon extends Actor
{
    Troop parent;
    public TroopIcon(Troop parent, int num, Color fg, Color bg) {
        this.parent = parent;
        setImage(new GreenfootImage(Integer.toString(num), 15, fg, bg));
    }
    
    public void update() {
        try {
            setLocation(parent.getX(), parent.getY());
        }
        catch (IllegalStateException e) {
            //getWorld().removeObject(this);
        }
    }
}
