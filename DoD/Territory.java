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
    int resource = 0; //0 nothing, 1 iron, 2 horses, 3 leather
    public Faction owner;
    Terrain terrain;
    boolean isExterior = false;
    boolean isToggleSet = false;
    public boolean[] conflictedBorderList = new boolean[6];
    public Territory[] adjacentTerritoryList = new Territory[6]; //Starts from N goes clockwise
    
    int lastTime = 0;
    int territoryID = 1;
    Border[] borders = new Border[6]; //Starts from N goes clockwise
    Border pastOutwardToggleBorder = null;
    Border outwardToggleBorder = null;
    
    
    public Territory(Faction owner, int territoryID, boolean isExterior) {
        super();
        this.owner = owner;
        this.territoryID = territoryID;
        this.isExterior = isExterior;
        for( int i = 0; i < 6; i++ ) {
            this.borders[i] = new Border(this, i);
        }
    }
    
    public Territory(Faction owner, int territoryID, boolean isExterior, Terrain terrain, int resource) {
        this(owner, territoryID, isExterior);
        this.terrain = terrain;
        this.resource = resource;
        setDisplay();
    }
    
    private void setDisplay() {
        GreenfootImage image = new GreenfootImage(terrain.getImage());
        //image.drawImage(new GreenfootImage("MP: " + recruitNumber, 20, Color.BLACK, Color.WHITE), 40, 30);
            //image.drawImage(new GreenfootImage("MP: " + recruitNumber, 15, Color.BLACK, Color.WHITE), 45, 30);    // I like this more
        //GreenfootImage factionImage = owner.getFlag();
        //image.drawImage(factionImage, 50, 50);
        setImage(image);
        image.drawImage(new GreenfootImage("MP: " + recruitNumber, 20, owner.fgColor, owner.bgColor), 40, 40);    // I like this more
        
    }
    
    protected void addedToWorld(World world) {
        //owner = new Faction(); // for testing
        
        //conflictedBorderList.add(borders[0]); // for testing
        //conflictedBorderList.add(borders[1]); // for testing
    
        // Clockwise from N
        world.addObject(borders[0], getX(), getY() - 53);
        world.addObject(borders[1], getX() + 45, getY() - 27);
        world.addObject(borders[2], getX() + 45, getY() + 26);
        world.addObject(borders[3], getX(), getY() + 52);
        world.addObject(borders[4], getX() - 45, getY() + 26);
        world.addObject(borders[5], getX() - 45, getY() - 27);
    }
    
    /**
     * Act - do whatever the Territory wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    int birthControl = 1;
    public void act() 
    {
        //System.out.println("Territory " + territoryID + " acting.");
        
        if ( ((DoDWorld)getWorld()).getGameTime() > lastTime) {
            lastTime = ((DoDWorld)getWorld()).getGameTime();
            //if (territoryID == 123)    // To limit troops for testing
                
                //if( birthControl-- == 0) {
                    //if(birthControl < 0)
                        //birthControl = 5;
                    addUnits(recruitNumber);
                //}
        }
    }  
    
    //whether or not its shares a border with the enemy (sets exterior)
    public boolean enemyBorder() {
        for (int i = 0; i < adjacentTerritoryList.length; i++) {
            if (this.owner != null && adjacentTerritoryList[i] != null) {
                if (this.owner.id != adjacentTerritoryList[i].owner.id) {
                    this.isExterior = true;
                    return true;
                }
            }
        }
        
        this.isExterior = false;
        return false;
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
        setDisplay();
        
        //Update a crapton of other stuff
        isExterior = false;
        for(int k=0; k < 6; k++) {
            conflictedBorderList[k] = false;
            borders[k].inConflict = false;
            borders[k].setBorderManCount(0);
            //borders[k].bd.getImage().clear();
            
            if(adjacentTerritoryList[k] != null) {
                adjacentTerritoryList[k].isExterior = false;
                //owner.world.conflictedTerritoryList.remove(adjacentTerritoryList[k]); // can't remove why we're iterating on this list, so we're going to add a second and remove later
                owner.conflictedTerritoryList.remove(adjacentTerritoryList[k]);
                owner.nonConflictedTerritoryList.remove(adjacentTerritoryList[k]);
                
                //Deal with this territory's changes
                if (owner != adjacentTerritoryList[k].getOwner()) {
                    conflictedBorderList[k] = true;
                    borders[k].inConflict = true;
                    isExterior = true;
                    borders[k].toggle.setToggleVal(3);
                }
                
                
                //And deal with its adjacent territorys' changes
                for (int i = 0; i < 6; i++ ) {
                    adjacentTerritoryList[k].conflictedBorderList[i] = false;
                    if (adjacentTerritoryList[k].borders[i].getOwner() != adjacentTerritoryList[k].borders[i].otherBorder.getOwner()) {
                        adjacentTerritoryList[k].conflictedBorderList[i] = true;
                        adjacentTerritoryList[k].isExterior = true;
                       //owner.world.conflictedTerritoryList.add(adjacentTerritoryList[k]);
                        owner.conflictedTerritoryList.add(adjacentTerritoryList[k]);
                        adjacentTerritoryList[k].borders[i].inConflict = true;
                        adjacentTerritoryList[k].borders[i].toggle.setToggleVal(3);

                    }
                }
                
                if (!adjacentTerritoryList[k].isExterior)
                    owner.nonConflictedTerritoryList.add(adjacentTerritoryList[k]);
                
            }// else {
            //    borders[k].toggle.setToggleVal(0);
            //}
        }
        
        
        //Update adjacent territories' borders in conflict
        
        // Update faction's manpower

 

    }
    
    private void addUnits(int incoming) {
        //System.out.println("Territory " + territoryID + " spawns " + incoming + " units.");
        //System.out.println("Territory " + territoryID + " isExterior? " + isExterior);
        /*
        int menLeft = incoming;
        int size = 0;
        for (int i = 0; i < 6; i++) {
            if (conflictedBorderList[i]) {
                size++;
            }
        }
        if (territoryID == 2)
            System.out.println("Territory " + territoryID + " has " + size + " conflicted Borders.");

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
        */
                //System.out.println("Territory " + territoryID + " gets " + incoming + " new units (1 troop).");
                owner.addTroops(incoming, getX(), getY() );
        //    }
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
