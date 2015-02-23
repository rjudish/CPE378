import greenfoot.*;
import java.util.ArrayList;

/**
 * Write a description of class Faction here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Faction extends Actor
{
    int INIT_TERRITORIES = 25;

    int id = 0;
    int manpower = 0;
    int troopCount = 0;
    private ArrayList<Troop> troopList = new ArrayList<Troop>();
    private ArrayList<Territory> territoryList = new ArrayList<Territory>(INIT_TERRITORIES);
    private ArrayList<Territory> conflictedTerritoryList = new ArrayList<Territory>();
    private ArrayList<Territory> nonConflictedTerritoryList = new ArrayList<Territory>();
    

      
    int lastTime = 0;
    
    
   
    
    /**
     * Act - do whatever the Faction wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if ( ((DoDWorld)getWorld()).getGameTime() > lastTime) {
            lastTime = ((DoDWorld)getWorld()).getGameTime();
            System.out.println("Faction Loop.");
            System.out.println("Spawn Code.");
            
        }
    }
    
    public void addTroops(int incoming, int xPos, int yPos) {
        Troop newTroop = new Troop(this, incoming);
        troopList.add(newTroop);
        troopCount += incoming;
        //getWorld().addObject(newTroop, xPos, yPos); // HALP
        System.out.println("Gave " + incoming + " units to Faction " + this.id + " for a total of " + troopCount);

    }
}
