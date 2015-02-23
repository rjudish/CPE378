import greenfoot.*;
import java.util.ArrayList;

/**
 * Write a description of class Territory here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Territory extends Actor
{
    int recruitNumber = 10;
    private Faction owner;
    Terrain terrain;
    boolean isExterior = false;
    public ArrayList<Border> conflictedBorderList = new ArrayList<Border>();
    public ArrayList<Territory> adjacentTerritoryList = new ArrayList<Territory>();

    
    
    int lastTime = 0;
    int territoryID = 1;
    Border[] border ={
        new Border(this), new Border(this),
        new Border(this), new Border(this),
        new Border(this), new Border(this),
    };
    
    public Territory(int territoryID, boolean isExterior) {
        super();
        this.territoryID = territoryID;
        this.isExterior = isExterior;
        
    }
    
    public Territory(int owner, int territoryID, boolean isExterior) {
        super();
        this.owner = owner;
        this.territoryID = territoryID;
        this.isExterior = isExterior;
        
    }
    
    protected void addedToWorld(World world) {
        owner = new Faction(); // for testing
        
        conflictedBorderList.add(border[0]); // for testing
        conflictedBorderList.add(border[1]); // for testing
    
        world.addObject(border[0], getX(), getY() + 30);
        world.addObject(border[1], getX() + 25, getY() + 15);
        world.addObject(border[2], getX() + 25, getY() - 15);
        world.addObject(border[3], getX(), getY() - 30);
        world.addObject(border[4], getX() - 25, getY() + 15);
        world.addObject(border[5], getX() - 25, getY() - 15);
    }
    
    /**
     * Act - do whatever the Territory wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if ( ((DoDWorld)getWorld()).getGameTime() > lastTime) {
            lastTime = ((DoDWorld)getWorld()).getGameTime();
            System.out.println("Territory " + territoryID + " spawns " + recruitNumber + " units.");
            addUnits(recruitNumber);
        }
    }  
    
    
    public Faction getOwner() {
        return owner;
    }
        
    
    public void newOwner(Faction newFaction) {
        //Update territory owner
        owner = newFaction;
        //Update borders in conflict
        //Update adjacent territories' borders in conflict
        //Update toggles (use AI?)
        // Update faction's manpower

 

    }
    
    private void addUnits(int incoming) {
        // Daniel points out that Andrew forgot that troops have to travel from the center to the border. This section will be updated appropriately.
        
        // if territory has borders in conflict, set split troops between borders
        if (isExterior) {
            int menLeft = incoming;
            System.out.println("Territory " + territoryID + " has " + conflictedBorderList.size() + " conflicted Borders.");
            int size = conflictedBorderList.size();
            for( int i = 0; i < size; i++) {
                if (menLeft > (incoming / size)) {
                    border[i].addTroops(incoming / size);
                    menLeft -= (incoming / size);
                } else {
                    border[i].addTroops(menLeft);
                    menLeft = 0;
                }
            }
        } else { // else give territories to faction
            owner.addTroops(incoming, getX(), getY() );
        }
        
    }

    
}
