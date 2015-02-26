import java.util.ArrayList;

/**
 * Write a description of class AI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AI {
    // instance variables - replace the example below with your own
    private java.util.Map<Integer, Faction> factions;

    /**
     * Constructor for objects of class AI
     */
    public AI(java.util.Map<Integer, Faction> factions) {
        this.factions = factions;
    }
    
    /*public AI(Faction player, int numAIFactions) {
        this.player = player;
        this.numAIFactions = numAIFactions;
        aiFactions = new ArrayList<Faction>(numAIFactions);
    }*/
    
    public void initToggle() {
        System.out.println("AI factions size: " + factions.size());
        for (int i = 1; i <= factions.size(); i++) {
            System.out.println("\tAI: " + i);
            Faction currFaction = factions.get(1);
            System.out.println("\tnonConflictedTerrList: " + currFaction.nonConflictedTerritoryList.size());
            ArrayList<Territory> interiorTerritoryQueue = (ArrayList<Territory>) currFaction.nonConflictedTerritoryList.clone();
            //go through each interior territory owned by currFaction
            while (interiorTerritoryQueue.size() > 0) {
                System.out.println("\t\tqueue size: " + interiorTerritoryQueue.size());
                Territory interiorTerritory = interiorTerritoryQueue.get(0);
                Territory[] adjacentTerritoryList = interiorTerritory.adjacentTerritoryList;
                int k = 0, otherK;
                Territory recv = null;
                
                //find an exterior territory or one that has a toggled border
                for (k = 0; k < adjacentTerritoryList.length; k++) {
                    Territory temp = adjacentTerritoryList[k];
                    if (temp.isExterior || temp.isToggleSet)
                        break;
                }
                
                if (k < adjacentTerritoryList.length) {
                    recv = adjacentTerritoryList[k];
                    interiorTerritory.borders[k].toggle.toggleVal = 2;
                    interiorTerritory.isToggleSet = true;
                    otherK = recv.sharedBorderIndex(k);
                    recv.borders[otherK].toggle.toggleVal = 1; //sharedBorderIndex in Territory
                    System.out.println("Works");
                }
                else
                    interiorTerritoryQueue.add(interiorTerritoryQueue.remove(0));
            }
        }
    }
    
    public void defaultToggle() {
        
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