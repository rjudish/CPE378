import java.util.ArrayList;

/**
 * Write a description of class AI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AI {
    // instance variables - replace the example below with your own
    private int numAIFactions = 1;
    private ArrayList<Faction> aiFactions;
    private Faction player;

    /**
     * Constructor for objects of class AI
     */
    public AI(Faction player) {
        this.player = player;
        this.numAIFactions = 1;
        aiFactions = new ArrayList<Faction>(1);
    }
    
    public AI(Faction player, int numAIFactions) {
        this.player = player;
        this.numAIFactions = numAIFactions;
        aiFactions = new ArrayList<Faction>(numAIFactions);
    }
    
    //Faction nonConflictedTerritoryList needs to be public, not private; GitHub should sync change
    //same for toggleToggle in Toggle
    //Added isToggleSet variable to Territory; used for initializing
    public void initToggle() {
        for (int i = 0; i < numAIFactions; i++) {
            Faction currFaction = player;//aiFactions.get(i);
            ArrayList<Territory> interiorTerritoryQueue = (ArrayList<Territory>) currFaction.nonConflictedTerritoryList.clone();
            //go through each interior territory owned by currFaction
            for (int j = 0; j < interiorTerritoryQueue.size(); ) {
                Territory interiorTerritory = interiorTerritoryQueue.get(j);
                ArrayList<Territory> adjacentTerritoryList = interiorTerritory.adjacentTerritoryList;
                Border shared = null;
                int k = 0;
                //find an exterior territory or one that has a toggled border
                for (k = 0; k < adjacentTerritoryList.size(); k++) {
                    if (adjacentTerritoryList.get(k).isExterior)
                        break;
                    if (adjacentTerritoryList.get(k).isToggleSet)
                        break;
                }
                
                if (k < adjacentTerritoryList.size() && adjacentTerritoryList.get(k).isExterior) {
                    shared = interiorTerritory.sharedBorder(adjacentTerritoryList.get(k));
                }
                else if (k < adjacentTerritoryList.size() && adjacentTerritoryList.get(k).isToggleSet) {
                    shared = interiorTerritory.sharedBorder(adjacentTerritoryList.get(k));
                }
                
                if (shared != null) {
                    shared.toggle.toggleToggle();
                    adjacentTerritoryList.get(k).isToggleSet = true;
                }
                else
                    interiorTerritoryQueue.add(interiorTerritoryQueue.remove(0));
            }
        }
    }
    
    /*public void act() {
        //iterate through each faction
        for (int i = 0; i < numFactions; i++) {
            Faction currFaction = player;//aiFactions.get(i);
            //go through each territory owned by currFaction
            for (int j = 0; j < currFaction.nonConflictedTerritoryList.size(); j++) {
                int allowed = 6;
                int choice = (int) (Math.random() * allowed);
                //currFaction.territoryList.get(j).moveTroops(j);
            }
        }
    }*/
}