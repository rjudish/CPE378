import greenfoot.*;
import java.util.ArrayList;
import java.awt.Color;

/**
 * Write a description of class Faction here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Faction //extends Actor
{
    public static final int INIT_TERRITORIES = 25;
    
    int id = 0;
    
    //int manpower = 0;
        //renamed to:
            //int factionTroopCount = 0;
                // which I then realized is just troopList.size
    
    private int factionManCount = 0; //includes men in troops as well as men at borders
    
    public ArrayList<Troop> troopList = new ArrayList<Troop>();    // does not include men at borders
    
    public ArrayList<Territory> territoryList = new ArrayList<Territory>();
    public ArrayList<Territory> conflictedTerritoryList = new ArrayList<Territory>();
    public ArrayList<Territory> nonConflictedTerritoryList = new ArrayList<Territory>();
    
    int lastTime = 0;
    GreenfootImage flag;
    Color bgColor;
    Color fgColor;
    
    
    DoDWorld world;
    
    public Faction(World world) {
        this.world = (DoDWorld) world;
    }
    
    public Faction(World world, int id) {
        this(world);
        this.id = id;
        switch (id) {
            case 0:  bgColor = Color.RED;
                    fgColor = Color.WHITE;
                     break;
            case 1:  bgColor = Color.YELLOW;
                    fgColor = Color.BLACK;
                    break;
            case 2:  bgColor = Color.GREEN;
                    fgColor = Color.BLACK;
                    break;
            case 3:  bgColor = Color.BLUE;
                    fgColor = Color.WHITE;
                    break;
        }   
    }
    
    /*
    public void act() 
    {
        if ( ((DoDWorld)getWorld()).getGameTime() > lastTime) {
            lastTime = ((DoDWorld)getWorld()).getGameTime();
            //System.out.println("Faction Loop.");
            //System.out.println("Spawn Code.");
            
        }
        
        System.out.println("Faction " + id + " has " + conflictedTerritoryList.size() + " conflicted territories");
    }
    */
    
    public void addTroops(int incoming, int xPos, int yPos) {
        Troop newTroop = new Troop(this, incoming);
        troopList.add(newTroop);
        
        factionManCount += incoming;
        
        world.addObject(newTroop, xPos, yPos); // HALP
        //System.out.println("Gave " + incoming + " units to Faction " + this.id + " for a total of " + troopList.size());

    }
    
    
    
    public GreenfootImage getFlag() {
        return flag;
    }
    
    public void setFlag(GreenfootImage flag) {
        this.flag = flag;
    }
    
    public int getFactionManCount() {
        return factionManCount;
    }
    
    public void redFactionManCount( int less) {
        factionManCount -= less;
    }
    
}
