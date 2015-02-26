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
    public boolean[] conflictedBorderList = new boolean[6];
    public Territory[] adjacentTerritoryList = new Territory[6]; //Starts from N goes clockwise
    
    
    int lastTime = 0;
    int territoryID = 1;
    Border[] borders = new Border[6]; //Starts from N goes clockwise
    
    
    
    public Territory(Faction owner, int territoryID, boolean isExterior) {
        super();
        this.owner = owner;
        this.territoryID = territoryID;
        this.isExterior = isExterior;
        for( int i = 0; i < 6; i++ ) {
            this.borders[i] = new Border(this);
        }
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
        //owner = new Faction(); // for testing
        
        //conflictedBorderList.add(borders[0]); // for testing
        //conflictedBorderList.add(borders[1]); // for testing
    
        // Clockwise from N
        world.addObject(borders[0], getX(), getY() - 40);
        world.addObject(borders[1], getX() + 35, getY() - 25);
        world.addObject(borders[2], getX() + 35, getY() + 25);
        world.addObject(borders[3], getX(), getY() + 40);
        world.addObject(borders[4], getX() - 35, getY() + 25);
        world.addObject(borders[5], getX() - 35, getY() - 25);
    }
    
    /**
     * Act - do whatever the Territory wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if ( ((DoDWorld)getWorld()).getGameTime() > lastTime) {
            lastTime = ((DoDWorld)getWorld()).getGameTime();
            addUnits(recruitNumber);
        }
    }  
    
    //gives this territory's shared border index based on another territory's border index (0-5)
    public int sharedBorderIndex(int otherBorderIndex) {
        return otherBorderIndex % 3 == otherBorderIndex ? otherBorderIndex + 3 : otherBorderIndex % 3;
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
        //System.out.println("Territory " + territoryID + " spawns " + incoming + " units.");

        // if territory has borders in conflict, set split troops between borders
        // Daniel points out that Andrew forgot that troops have to travel from the center to the border. This section will be updated appropriately.
        //if (isExterior) {
            int menLeft = incoming;
            int size = 0;
            for (int i = 0; i < 6; i++) {
                if (conflictedBorderList[i]) {
                    size++;
                }
            }
            //System.out.println("Territory " + territoryID + " has " + size + " conflicted Borders.");

            for (int i = 0; i < 6; i++) {
                if (conflictedBorderList[i]) {
                    if (menLeft > (incoming / size)) {
                        borders[i].addTroops(incoming / size);
                        menLeft -= (incoming / size);
                    } else {
                        borders[i].addTroops(menLeft);
                        menLeft = 0;
                    }
                } else { // else give territories to faction
                    owner.addTroops(incoming, getX(), getY() );
                }
            }
        //}
    }

    public Terrain getTerrain() {
        return terrain;
    }
    
    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }
    
    public Border[] getBorders() {
        return borders;
    }
}
