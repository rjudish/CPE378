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

    //int manpower = 0;
        //renamed to:
            //int factionTroopCount = 0;
                // which I then realized is just troopList.size
    int factionManCount = 0; //includes men in troops as well as men at borders
    
    private ArrayList<Troop> troopList = new ArrayList<Troop>();
    public ArrayList<Territory> territoryList = new ArrayList<Territory>();
    private ArrayList<Territory> conflictedTerritoryList = new ArrayList<Territory>();
    public ArrayList<Territory> nonConflictedTerritoryList = new ArrayList<Territory>();
    
    int lastTime = 0;
   
    // Constructor
    /*public Faction(ArrayList<Territory> conflictedTerritoryList, ArrayList<Territory> nonConflictedTerritoryList)
    {
        this.conflictedTerritoryList = conflictedTerritories;
        this.nonConflictedTerritoryList = nonConflictedTerritoryList;
        for(Territory terr : conflictedTerritoryList) { 
            territoryList.add(terr);
        }
        for(Territory terr : nonConflictedTerritoryList) { 
            territoryList.add(terr);
        }
        
    }*/
    
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
        
        //troopCount += incoming;
            //This is now done using troopList.size instead
        
        //getWorld().addObject(newTroop, xPos, yPos); // HALP
        System.out.println("Gave " + incoming + " units to Faction " + this.id + " for a total of " + troopList.size());

    }
}
