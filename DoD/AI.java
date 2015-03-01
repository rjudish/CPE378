import java.util.List;
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
    private List<Territory> territories;

    /**
     * Constructor for objects of class AI
     */
    public AI(java.util.Map<Integer, Faction> factions, List<Territory> territories) {
        this.factions = factions;
        this.territories = territories;
    }
    
    /*public AI(Faction player, int numAIFactions) {
        this.player = player;
        this.numAIFactions = numAIFactions;
        aiFactions = new ArrayList<Faction>(numAIFactions);
    }*/
    
    /*public void setExterior() {
        for (int i = 0; i < territories.size(); i++) {
            territories.get(i).enemyBorder();
        }
    }*/
    
    public void initToggle() {
        setNonConflictedTerritoryList();
        
        for (int i = 1; i <= factions.size(); i++) {
            Faction currFaction = factions.get(i);
            ArrayList<Territory> interiorTerritoryQueue = (ArrayList<Territory>) currFaction.nonConflictedTerritoryList.clone();
            
            //go through each interior territory owned by currFaction
            while (interiorTerritoryQueue.size() > 0) {
                Territory interiorTerritory = interiorTerritoryQueue.get(0);
                Territory[] adjacentTerritoryList = interiorTerritory.adjacentTerritoryList;
                int k = 0, otherK, backUpIndex = 0;
                Territory recv = null;
                Territory backUp = null;
                
                //find an exterior territory or one that has a toggled border
                for (k = 0; k < adjacentTerritoryList.length; k++) {
                    Territory temp = adjacentTerritoryList[k];
                    if (temp != null) {
                        if (temp.isExterior)
                            break;
                        if (temp.isToggleSet) {
                            backUp = temp;
                            backUpIndex = k;
                        }
                    }
                }
                
                if (k < adjacentTerritoryList.length) {
                    recv = adjacentTerritoryList[k];
                    interiorTerritory.borders[k].toggle.toggleVal = 2;
                    interiorTerritory.isToggleSet = true;
                    otherK = recv.sharedBorderIndex(k);
                    recv.borders[otherK].toggle.toggleVal = 1; //sharedBorderIndex in Territory
                    interiorTerritoryQueue.remove(0);
                }
                else if (backUp != null) {
                    recv = backUp;
                    interiorTerritory.borders[backUpIndex].toggle.toggleVal = 2;
                    interiorTerritory.isToggleSet = true;
                    otherK = recv.sharedBorderIndex(backUpIndex);
                    recv.borders[otherK].toggle.toggleVal = 1; //sharedBorderIndex in Territory
                    interiorTerritoryQueue.remove(0);
                }
                else
                    interiorTerritoryQueue.add(interiorTerritoryQueue.remove(0));
            }
        }
    }
    
    //defaults toggle too
    public void setNonConflictedTerritoryList() {
        for (int i = 0; i < territories.size(); i++) {
            Territory temp = territories.get(i);
            if (!temp.isExterior) {
                temp.owner.nonConflictedTerritoryList.add(temp);
            }
            else {
                temp.owner.conflictedTerritoryList.add(temp);
            }
            
            for (int j = 0; j < temp.borders.length; j++) {
                if (temp.adjacentTerritoryList[j] == null)
                    temp.borders[j].toggle.toggleVal = 0;
                else
                    temp.borders[j].toggle.toggleVal = 3;
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