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
    
    public static void toggleAI(Border border) {
        Toggle curToggle = border.toggle;
        Territory curTerritory = border.parentTerritory;
        Territory otherTerritory = border.otherBorder.parentTerritory;
        
        if (curToggle.getToggleVal() == 3 && !curTerritory.isExterior) {
            Territory temp = otherTerritory;
            
            /*while (temp != null && !temp.isExterior) {
                if (temp.territoryID == curTerritory.territoryID)
                    return;
                temp = temp.outwardToggleBorder.parentTerritory;
            }*/
            
            curTerritory.pastOutwardToggleBorder = curTerritory.outwardToggleBorder;
            curTerritory.pastOutwardToggleBorder.toggle.setToggleVal(3);
            curTerritory.pastOutwardToggleBorder.otherBorder.toggle.setToggleVal(3);
            
            curTerritory.outwardToggleBorder = border;
            curToggle.setToggleVal(2);
            border.otherBorder.toggle.setToggleVal(1);
        }
        else if (curToggle.getToggleVal() == 3 && !otherTerritory.isExterior) {
            Territory temp = curTerritory;
            
            /*while (temp != null && !temp.isExterior) {
                if (temp.territoryID == otherTerritory.territoryID)
                    return;
                temp = temp.outwardToggleBorder.parentTerritory;
            }*/
            
            otherTerritory.pastOutwardToggleBorder = otherTerritory.outwardToggleBorder;
            otherTerritory.pastOutwardToggleBorder.toggle.setToggleVal(3);
            otherTerritory.pastOutwardToggleBorder.otherBorder.toggle.setToggleVal(3);
            
            otherTerritory.outwardToggleBorder = border.otherBorder;
            curToggle.setToggleVal(1);
            border.otherBorder.toggle.setToggleVal(2);
        }
        else if (curToggle.getToggleVal() == 2 && curTerritory.pastOutwardToggleBorder != null && !otherTerritory.isExterior) {
            Territory temp = curTerritory.pastOutwardToggleBorder.parentTerritory;
            
            /*while (temp != null && !temp.isExterior) {
                if (temp.territoryID == otherTerritory.territoryID)
                    return;
                temp = temp.outwardToggleBorder.parentTerritory;
            }*/
            
            curToggle.setToggleVal(1);
            border.otherBorder.toggle.setToggleVal(2);
            
            otherTerritory.pastOutwardToggleBorder = otherTerritory.outwardToggleBorder;
            otherTerritory.outwardToggleBorder.toggle.setToggleVal(3);
            otherTerritory.outwardToggleBorder.otherBorder.toggle.setToggleVal(3);
            otherTerritory.outwardToggleBorder = border.otherBorder;
            
            curTerritory.pastOutwardToggleBorder.toggle.setToggleVal(2);
            curTerritory.pastOutwardToggleBorder.otherBorder.toggle.setToggleVal(1);
            curTerritory.outwardToggleBorder = curTerritory.pastOutwardToggleBorder;
            curTerritory.pastOutwardToggleBorder = border;
        }
        else if (curToggle.getToggleVal() == 1 && otherTerritory.pastOutwardToggleBorder != null && !curTerritory.isExterior) {
            Territory temp = otherTerritory.pastOutwardToggleBorder.parentTerritory;
            
            /*while (temp != null && !temp.isExterior) {
                if (temp.territoryID == curTerritory.territoryID)
                    return;
                temp = temp.outwardToggleBorder.parentTerritory;
            }*/
            
            curToggle.setToggleVal(2);
            border.otherBorder.toggle.setToggleVal(1);
            
            curTerritory.pastOutwardToggleBorder = curTerritory.outwardToggleBorder;
            curTerritory.outwardToggleBorder.toggle.setToggleVal(3);
            curTerritory.outwardToggleBorder.otherBorder.toggle.setToggleVal(3);
            curTerritory.outwardToggleBorder = border;
            
            otherTerritory.pastOutwardToggleBorder.toggle.setToggleVal(2);
            otherTerritory.pastOutwardToggleBorder.otherBorder.toggle.setToggleVal(1);
            otherTerritory.outwardToggleBorder = otherTerritory.pastOutwardToggleBorder;
            otherTerritory.pastOutwardToggleBorder = border.otherBorder;
        }
    }
    
    public int getInverseIndex(int index) {
        return index > 2 ? index % 3 : index + 3;
    }
    
    public void battleOutcome(Territory lost, Faction winner) {
         Faction loser = lost.owner;
         Territory[] adjLostTerrList = lost.adjacentTerritoryList;
         //System.out.println("Battle it out!");
         
         lost.owner = winner;
         winner.territoryList.add(lost);
         loser.territoryList.remove(lost);
         loser.conflictedTerritoryList.remove(lost);
         lost.isExterior = true; //FIX
         winner.conflictedTerritoryList.add(lost); //FIX
         for (int i = 0; i < adjLostTerrList.length; i++) { //for alladjacent territories to the lost territory
             Territory adjTerr = adjLostTerrList[i];
             
             if (adjTerr == null) {
                 // do nothing... 
             }
             else if (adjTerr.owner.id == winner.id) { //winner's adjacent Territory
                 Border[] borders = adjTerr.borders;
                 int index;
                 
                 lost.conflictedBorderList[i] = false;
                 adjTerr.conflictedBorderList[this.getInverseIndex(i)] = false;
                 for (index = 0; index < adjTerr.conflictedBorderList.length; index++) {
                     if (adjTerr.conflictedBorderList[index])
                        break;
                 }
                 if (index == adjTerr.conflictedBorderList.length && adjTerr.isExterior) { //interior territory now
                    adjTerr.isExterior = false;
                    winner.conflictedTerritoryList.remove(adjTerr);
                    winner.nonConflictedTerritoryList.add(adjTerr);
                    //winner.world.conflictedTerritoryList.remove(adjTerr);
                    lost.borders[i].toggle.setToggleVal(1);
                    adjTerr.borders[this.getInverseIndex(i)].toggle.setToggleVal(2);
                    adjTerr.borders[this.getInverseIndex(i)].parentTerritory.outwardToggleBorder = adjTerr.borders[this.getInverseIndex(i)];
                    adjTerr.borders[this.getInverseIndex(i)].parentTerritory.pastOutwardToggleBorder = null;
                 }
             }
             else { //other factions adjacent Territory
                 lost.conflictedBorderList[i] = true;
                 adjTerr.conflictedBorderList[this.getInverseIndex(i)] = true;
                 
                 if (adjTerr.owner.id == loser.id) { //loser's interior territories
                     Border[] borders = adjTerr.borders;
                     //if (!loser.world.conflictedTerritoryList.contains(adjTerr))
                     //    loser.world.conflictedTerritoryList.add(adjTerr);
                     
                     if (!adjTerr.isExterior) {
                         for (int j = 0; j < borders.length; j++) {
                             if (borders[j].toggle.getToggleVal() == 2) { //pointing outside
                                 borders[j].toggle.setToggleVal(3); //inactive
                                 borders[j].otherBorder.toggle.setToggleVal(3); //inactive
                                 break;
                             }
                         }
                     }
                 }
             }
         }
    }
    
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
                    interiorTerritory.borders[k].toggle.toggleVal = 2; //points outside
                    interiorTerritory.isToggleSet = true;
                    interiorTerritory.outwardToggleBorder = interiorTerritory.borders[k];
                    otherK = recv.sharedBorderIndex(k);
                    recv.borders[otherK].toggle.toggleVal = 1; //sharedBorderIndex in Territory, points inside
                    interiorTerritoryQueue.remove(0);
                }
                else if (backUp != null) {
                    recv = backUp;
                    interiorTerritory.borders[backUpIndex].toggle.toggleVal = 2;
                    interiorTerritory.isToggleSet = true;
                    interiorTerritory.outwardToggleBorder = interiorTerritory.borders[backUpIndex];
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
                temp.owner.world.conflictedTerritoryList.add(temp); //not using this list anymore?
            }
            
            for (int j = 0; j < temp.borders.length; j++) {
                if (temp.adjacentTerritoryList[j] == null)
                    temp.borders[j].toggle.toggleVal = 0; //none
                else
                    temp.borders[j].toggle.toggleVal = 3; //inactive
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