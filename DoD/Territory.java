import greenfoot.*;
import java.util.ArrayList;
import java.awt.Color;
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
    boolean isToggleSet = false;
    public ArrayList<Border> conflictedBorderList = new ArrayList<Border>();
    public ArrayList<Territory> adjacentTerritoryList = new ArrayList<Territory>();

    
    
    int lastTime = 0;
    int territoryID = 1;
    Border[] border ={
        new Border(this), new Border(this),
        new Border(this), new Border(this),
        new Border(this), new Border(this),
   
    };
    
    
    
    public Territory(Faction owner, int territoryID, boolean isExterior) {
        super();
        this.owner = owner;
        this.territoryID = territoryID;
        this.isExterior = isExterior;
    }
    
    public Territory(Faction owner, int territoryID, boolean isExterior, Terrain terrain) {
        this(owner, territoryID, isExterior);
        this.terrain = terrain;
        //setImage(terrain.getImage());
        GreenfootImage image = new GreenfootImage(terrain.getImage());
        image.drawImage(new GreenfootImage("MP: " + recruitNumber, 20, Color.BLACK, Color.WHITE), 40, 30);
        GreenfootImage factionImage = owner.getFlag();
        image.drawImage(factionImage, 50, 50);
        setImage(image);
    }
    
    protected void addedToWorld(World world) {
        owner = new Faction(); // for testing
        
        conflictedBorderList.add(border[0]); // for testing
        conflictedBorderList.add(border[1]); // for testing
    
        world.addObject(border[0], getX(), getY() + 40);
        world.addObject(border[1], getX() + 35, getY() + 25);
        world.addObject(border[2], getX() + 35, getY() - 25);
        world.addObject(border[3], getX(), getY() - 40);
        world.addObject(border[4], getX() - 35, getY() + 25);
        world.addObject(border[5], getX() - 35, getY() - 25);
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
    
    //gives the border shared between two neighboring territories
    public Border sharedBorder(Territory neighbor) {
        //FIX
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (this.border[i].borderID == neighbor.border[j].borderID)
                    return this.border[i];
            }
        }
        
        return null;
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

    public Terrain getTerrain() {
        return terrain;
    }
    
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
}
