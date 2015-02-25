import greenfoot.*;
import java.awt.Color;
/**
 * Write a description of class Border here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Border extends Actor
{
    boolean inConflict = false;
    boolean hasRiver = false;
    int borderManCount = 0;
    
    int lastTime = 0;
    int borderID = 1;
    Border otherBorder = this;
    Territory parentTerritory;
    Toggle toggle = new Toggle(this);
    
    public Border(Territory parentTerritory) {
        this.parentTerritory = parentTerritory;
    }
    
    protected void addedToWorld(World world) {
        world.addObject(toggle, getX(), getY());
    }
    
    
    /**
     * Act - do whatever the Border wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
    }
    
    public Faction getOwner() {
        return parentTerritory.getOwner();
    }
    
    public Border getOther() {
        return otherBorder;
    }
    
    public void addTroops(int incoming) {
        borderManCount += incoming;
        //System.out.println("Border " + borderID + " gets " + incoming + " new units, for a total of " + borderManCount);
    }
}
