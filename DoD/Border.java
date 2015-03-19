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
    private int borderManCount = 0;
    NumDisplay bd;
    private Faction player;
    
    int lastTime = 0;
    int borderID = 0;
    Border otherBorder = this;
    Territory parentTerritory;
    Toggle toggle;
    
    public Border(Faction player, Territory parentTerritory, int borderID) {
        this.player = player;
        this.parentTerritory = parentTerritory;
        this.borderID = borderID;
        this.toggle = new Toggle(player, this);
        
        this.bd = new NumDisplay(this);
        
        //Fixing border collisions
        this.getImage().scale(80, 10);
        int idCounter = borderID;
        while (idCounter-- > 0) {
            //System.out.println("Rotating 60");
            this.turn(60);
        }

        this.getImage().setTransparency(0);
    }
    
    protected void addedToWorld(World world) {
        world.addObject(toggle, getX(), getY());
        int dx = getX() - parentTerritory.getX();
        int dy = getY() - parentTerritory.getY();
        world.addObject(bd, getX() - dx/4, getY() - dy/4);
    }
    
    
    /**
     * Act - do whatever the Border wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        //System.out.println("Border " + borderID
        //    + " of Terr " + parentTerritory.territoryID
        //    + " conflicted: " + inConflict);
    }
    
    public Faction getOwner() {
        return parentTerritory.getOwner();
    }
    
    public Border getOther() {
        return otherBorder;
    }
    
    public int getBorderManCount() {
        return borderManCount;
    }
    
    public void setBorderManCount(int newManCount) {
        //this.getOwner().redFactionManCount(borderManCount - newManCount);
        this.borderManCount = newManCount;
        this.bd.setDisplayNum(newManCount);
    }
    
    public void addTroops(int incoming) {
        borderManCount += incoming;
        //this.getOwner().redFactionManCount(-incoming);
        bd.setDisplayNum(borderManCount);
        //System.out.println("Border " + borderID + " gets " + incoming + " new units, for a total of " + borderManCount);
    }
    
    public void battleWon() {
        int old = otherBorder.borderManCount;
        
        otherBorder.borderManCount *= 0.5;
        borderManCount -= otherBorder.borderManCount;
        
        otherBorder.parentTerritory.owner.redFactionManCount(old - otherBorder.borderManCount);
        parentTerritory.owner.redFactionManCount(otherBorder.borderManCount);
    }
}
