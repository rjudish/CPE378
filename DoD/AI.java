import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import greenfoot.*;

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
    private Map world;
    private int maxNumTerr = 0;
    private static final double reduction = 0.8; // 0.8 reduces the recruit number by 20%
    private static final int minRecruit = 10; //minimum recruit number that a territory must have
    /**
     * Constructor for objects of class AI
     */
    public AI(Map world, java.util.Map<Integer, Faction> factions, List<Territory> territories) {
        this.factions = factions;
        this.territories = territories;
        this.world = world;
    }
    
    public static boolean loop(Territory original, Border outward) {
        Territory temp = outward.otherBorder.parentTerritory;
        
        while (temp != null && !temp.isExterior) {
            if (temp.territoryID == original.territoryID) {
                System.out.println("loop for " + original.territoryID);
                return true;
            }
            if (temp.outwardToggleBorder == null)
                break;
            temp = temp.outwardToggleBorder.otherBorder.parentTerritory;
        }
        
        if (temp == null)
            System.out.println("AI loop an outward border leads to a null territory");
        
        return false;
    }
    
    public static Border findBorderToSendTo(Territory terr, Border exception, Territory loopCheck) {
        for (int i = 0; i < terr.borders.length; i++) {
            Territory otherTerr = terr.adjacentTerritoryList[i];
            
            if (otherTerr != null && terr.owner.id == otherTerr.owner.id) {
                if (!otherTerr.isExterior && otherTerr.outwardToggleBorder == null) {
                    System.out.println("AI findBorderToSendTo territory " + otherTerr.territoryID + "is interior and doesn't have an outward Toggle!");
                }
                else if (otherTerr.isExterior || otherTerr.outwardToggleBorder.otherBorder.parentTerritory.territoryID != terr.territoryID) {
                    if (exception.borderID != terr.borders[i].borderID && !loop(loopCheck, terr.borders[i]))
                        return terr.borders[i];
                }
            }
        }
        
        return null;
    }
    
    public static void resetAllOutwardToggles(Territory terr) {
        for (int i = 0; i < terr.borders.length; i++) {
            Territory adj = terr.adjacentTerritoryList[i];
            
            if (adj != null && (terr.borders[i].toggle.getToggleVal() == 2 || adj.owner.id != terr.owner.id)) {
                terr.borders[i].toggle.setToggleVal(3);
                terr.borders[i].otherBorder.toggle.setToggleVal(3);
            }
        }
    }
    
    public static boolean toggleAI(Border border) {
        Toggle curToggle = border.toggle;
        Territory curTerritory = border.parentTerritory;
        Territory otherTerritory = border.otherBorder.parentTerritory;
        
        if (curToggle.getToggleVal() == 3 && !curTerritory.isExterior) { //send troops away from current territory
            
            if (!loop(curTerritory, border)) {
                curTerritory.outwardToggleBorder.toggle.setToggleVal(3);
                curTerritory.outwardToggleBorder.otherBorder.toggle.setToggleVal(3);
                curTerritory.outwardToggleBorder = border;
                
                resetAllOutwardToggles(curTerritory);
                curToggle.setToggleVal(2);
                border.otherBorder.toggle.setToggleVal(1);
                
                return true;
            }
        }
        
        if (curToggle.getToggleVal() == 3 && !otherTerritory.isExterior) { //send troops into current territory
            
            if (!loop(otherTerritory, border.otherBorder)) {
                otherTerritory.outwardToggleBorder.toggle.setToggleVal(3);
                otherTerritory.outwardToggleBorder.otherBorder.toggle.setToggleVal(3);
                otherTerritory.outwardToggleBorder = border.otherBorder;
                
                resetAllOutwardToggles(otherTerritory);
                curToggle.setToggleVal(1);
                border.otherBorder.toggle.setToggleVal(2);
                
                return true;
            }
        }
        else if (curToggle.getToggleVal() == 2 && !otherTerritory.isExterior) { //send troops into
            Border newOutwardToggleBorder = findBorderToSendTo(curTerritory, border, otherTerritory);
            
            if (newOutwardToggleBorder == null && curTerritory.outwardToggleBorder.borderID == border.borderID) {
                for (int i = 0; i < curTerritory.borders.length; i++) {
                    Border flip = curTerritory.borders[i];
                    
                    if (flip.otherBorder != null && flip.otherBorder.parentTerritory != null && flip.borderID != border.borderID) {
                        if (flip.otherBorder.parentTerritory.owner.id == curTerritory.owner.id && toggleAI(flip)) {
                            break;
                        }
                    }
                }
            }
            
            if (newOutwardToggleBorder != null) {
                otherTerritory.outwardToggleBorder.toggle.setToggleVal(3); //make old outward toggle inactive for other territory
                otherTerritory.outwardToggleBorder.otherBorder.toggle.setToggleVal(3);
                otherTerritory.outwardToggleBorder = border.otherBorder; //new outward toggle border
                
                resetAllOutwardToggles(curTerritory);
                resetAllOutwardToggles(otherTerritory);
                
                curTerritory.outwardToggleBorder = newOutwardToggleBorder; //new outward toggle for cur territory
                newOutwardToggleBorder.toggle.setToggleVal(2);
                newOutwardToggleBorder.otherBorder.toggle.setToggleVal(1);
                
                curToggle.setToggleVal(1); //to cur
                border.otherBorder.toggle.setToggleVal(2);
                
                return true;
            }
        }
        else if (curToggle.getToggleVal() == 1 && !curTerritory.isExterior) { //send troops away
            Border newOtherOutwardToggleBorder = findBorderToSendTo(otherTerritory, border.otherBorder, curTerritory);
            
            if (newOtherOutwardToggleBorder == null && otherTerritory.outwardToggleBorder.borderID == border.otherBorder.borderID) {
                for (int i = 0; i < otherTerritory.borders.length; i++) {
                    Border flip = otherTerritory.borders[i];
                    
                    if (flip.otherBorder != null && flip.otherBorder.parentTerritory != null && flip.borderID != border.otherBorder.borderID) {
                        if (flip.otherBorder.parentTerritory.owner.id == otherTerritory.owner.id && toggleAI(flip)) {
                            break;
                        }
                    }
                }
            }
            
            if (newOtherOutwardToggleBorder != null) {
                curTerritory.outwardToggleBorder.toggle.setToggleVal(3); //make old outward toggle inactive
                curTerritory.outwardToggleBorder.otherBorder.toggle.setToggleVal(3);
                curTerritory.outwardToggleBorder = border; //new outward toggle border
                
                resetAllOutwardToggles(curTerritory);
                resetAllOutwardToggles(otherTerritory);
                
                otherTerritory.outwardToggleBorder = newOtherOutwardToggleBorder; //new outward toggle for other territory
                newOtherOutwardToggleBorder.toggle.setToggleVal(2);
                newOtherOutwardToggleBorder.otherBorder.toggle.setToggleVal(1);
                
                curToggle.setToggleVal(2); //away from cur
                border.otherBorder.toggle.setToggleVal(1);
                
                return true;
            }
        }
        
        return false;
    }
    
    public int getInverseIndex(int index) {
        return index > 2 ? index % 3 : index + 3;
    }
    
    public boolean victoryCheck(Faction loser, Faction winner) {
        Faction player = world.player;
        boolean gameOver = false;
        if (player.id == winner.id && player.territoryList.size() >= maxNumTerr - 1) {
            System.out.println("Player Won!");
            DoDWorld.playerWin();
            gameOver = true;
        }
         
        if (player.id == loser.id && player.territoryList.size() <= 1) {
            System.out.println("Player Lost!");
            DoDWorld.playerLoss();
            gameOver = true;
        }
        return gameOver;
    }
    
    public boolean battleOutcome(Territory lost, Faction winner) {
         Faction loser = lost.owner;
         Territory[] adjLostTerrList = lost.adjacentTerritoryList;
         boolean noEnemies = true;
         //System.out.println("Battle it out!");
         
         addResourcesCount(lost, -1);
         lost.owner = winner;
         addResourcesCount(lost, 1);
         lost.hasChangedOwner = true;
         lost.recruitNumber *= reduction;
         lost.recruitNumber = lost.recruitNumber < minRecruit ? minRecruit : lost.recruitNumber;
         lost.setDisplay();
         
         lost.isExterior = false;
         winner.territoryList.add(lost);
         loser.territoryList.remove(lost);
         loser.conflictedTerritoryList.remove(lost);
         resetAllOutwardToggles(lost);
         
         for (int i = 0; i < adjLostTerrList.length; i++) { //for all adjacent territories to the lost territory
             Territory adjTerr = adjLostTerrList[i];
             int j = this.getInverseIndex(i);
             
             if (adjTerr == null) {
                 // do nothing... 
             }
             else if (adjTerr.owner.id == winner.id) { //winner's adjacent Territory
                 int index;
                 
                 lost.conflictedBorderList[i] = false;
                 adjTerr.conflictedBorderList[j] = false;
                 lost.borders[i].inConflict = false;
                 adjTerr.borders[j].inConflict = false;
                 lost.borders[i].setBorderManCount(0);
                 adjTerr.borders[j].setBorderManCount(0);
                 
                 for (index = 0; index < adjTerr.conflictedBorderList.length; index++) {
                     if (adjTerr.conflictedBorderList[index])
                        break;
                 }
                 if (index == adjTerr.conflictedBorderList.length && adjTerr.isExterior) { //interior territory now
                    adjTerr.isExterior = false;
                    winner.conflictedTerritoryList.remove(adjTerr);
                    winner.nonConflictedTerritoryList.add(adjTerr);
                    lost.borders[i].toggle.setToggleVal(1);
                    lost.borders[i].otherBorder.toggle.setToggleVal(2);
                    lost.borders[i].otherBorder.parentTerritory.outwardToggleBorder = lost.borders[i].otherBorder;
                 }
             }
             else { //other factions adjacent Territory
                 noEnemies = false;
                 lost.isExterior = true;
                 lost.conflictedBorderList[i] = true;
                 adjTerr.conflictedBorderList[j] = true;
                 lost.borders[i].inConflict = true;
                 adjTerr.borders[j].inConflict = true;
                 lost.borders[i].setBorderManCount(0);
                 adjTerr.borders[j].setBorderManCount(0);
                 
                 if (adjTerr.owner.id == loser.id) { //loser's interior territories
                     
                     if (!adjTerr.isExterior) {
                         adjTerr.isExterior = true;
                         adjTerr.outwardToggleBorder.toggle.setToggleVal(3);
                         adjTerr.outwardToggleBorder.otherBorder.toggle.setToggleVal(3);
                         //adjTerr.outwardToggleBorder = null;
                     }
                 }
             }
         }
         
         lost.outwardToggleBorder = null;
        
         return noEnemies;
    }
    
    public static int isNeighborExterior(Territory terr) {
        for (int i = 0; i < terr.borders.length; i++) {
            Territory adj = terr.adjacentTerritoryList[i];
            
            if (adj != null && adj.owner.id == terr.owner.id && adj.isExterior)
                return i;
        }
        
        return -1;
    }
    
    public static void reToggle(Territory start) {
        Faction faction = start.owner;
        Random rand = new Random();
        int neigh = isNeighborExterior(start);
        
        /*System.out.println("Big changes Incoming! " + start.territoryID + " r: " + start.territoryID / 16 + " c: " + start.territoryID % 16);
        System.out.print(start.territoryID + " isExterior: " + start.isExterior + " outwardToggleBorder: ");
        if (start.outwardToggleBorder != null)
            System.out.println(start.outwardToggleBorder.otherBorder.parentTerritory.territoryID);
        else
            System.out.println("null");
        
        for (int i = 0; i < 6; i++) {
            System.out.println(start.territoryID + " conflictedBorderList: " + i + " " + start.conflictedBorderList[i]);
        }*/
        
        if (start.outwardToggleBorder != null || start.isExterior)
            return;
        
        
        if (neigh >= 0 && neigh < 6) {
            start.outwardToggleBorder = start.borders[neigh];
            start.borders[neigh].toggle.setToggleVal(2);
            start.borders[neigh].otherBorder.toggle.setToggleVal(1);
            return;
        }
        
        //reset all toggles for territories sending troops to the wrong territory;
        ArrayList<Territory> resetList = new ArrayList<Territory>();
        resetToggle(resetList, start);
        
        while(resetList.size() > 0) {
            Territory terr = resetList.get(0);
            int index = 0;
            
            for (index = 0; index < terr.adjacentTerritoryList.length; index++) {
                Territory adj = terr.adjacentTerritoryList[index];
                if (adj != null && (adj.isExterior || adj.isToggleSet)) {
                    terr.outwardToggleBorder = terr.borders[index];
                    terr.borders[index].toggle.setToggleVal(2);
                    terr.borders[index].otherBorder.toggle.setToggleVal(1);
                    terr.isToggleSet = true;
                    break;
                }
            }
            
            if (terr.isToggleSet)
                resetList.remove(0);
            else
                resetList.add(resetList.remove(0));
        }
    }
    
    public static void resetToggle(ArrayList<Territory> resetList, Territory terr) {
        resetList.add(terr);
        terr.outwardToggleBorder = null;
        terr.isToggleSet = false;
        
        for (int i = 0; i < terr.adjacentTerritoryList.length; i++) {
            Territory adj = terr.adjacentTerritoryList[i];
            
            if (adj != null && adj.outwardToggleBorder != null && adj.outwardToggleBorder.otherBorder.parentTerritory.territoryID == terr.territoryID) {
                terr.borders[i].toggle.setToggleVal(3);
                terr.borders[i].otherBorder.toggle.setToggleVal(3);
                resetToggle(resetList, adj);
            }
        }
    }
    
    public void initToggle() {
        setNonConflictedTerritoryList();
        Random rand = new Random();
        
        for (int i = 1; i <= factions.size(); i++) {
            Faction currFaction = factions.get(i);
            ArrayList<Territory> interiorTerritoryQueue = (ArrayList<Territory>) currFaction.nonConflictedTerritoryList.clone();
            
            //go through each interior territory owned by currFaction
            while (interiorTerritoryQueue.size() > 0) {
                int j = rand.nextInt(interiorTerritoryQueue.size());
                Territory interiorTerritory = interiorTerritoryQueue.get(j);
                Territory[] adjacentTerritoryList = interiorTerritory.adjacentTerritoryList;
                int k = 0, otherK, backUpIndex = 0;
                Territory recv = null;
                Territory backUp = null;
                ArrayList<Integer> sendList = new ArrayList<Integer>();
                
                //find an exterior territory or one that has a toggled border
                for (k = 0; k < adjacentTerritoryList.length; k++) {
                    Territory temp = adjacentTerritoryList[k];
                    if (temp != null) {
                        if (temp.isExterior || temp.isToggleSet)
                            sendList.add(k);
                    }
                }
                
                if (sendList.size() > 0) {
                    k = sendList.get(rand.nextInt(sendList.size()));
                    recv = adjacentTerritoryList[k];
                    interiorTerritory.borders[k].toggle.setToggleVal(2); //points outside
                    interiorTerritory.isToggleSet = true;
                    interiorTerritory.outwardToggleBorder = interiorTerritory.borders[k];
                    otherK = recv.sharedBorderIndex(k);
                    recv.borders[otherK].toggle.setToggleVal(1); //sharedBorderIndex in Territory, points inside
                    maxNumTerr++;
                    addResourcesCount(interiorTerritory, 1);
                    interiorTerritoryQueue.remove(j);
                }
            }
            
            for (int j = 0; j < currFaction.conflictedTerritoryList.size(); j++) {
                Territory territory = currFaction.conflictedTerritoryList.get(j);
                
                maxNumTerr++;
                addResourcesCount(territory, 1);
                territory.isToggleSet = true;
            }
        }
    }
    
    public static void addResourcesCount(Territory territory, int count) {
        if (territory.resource > 0) {
            if (territory.resource == 1) { //iron
                territory.owner.ironCount += count;
            }
            else if (territory.resource == 2) { //horse
                territory.owner.horseCount += count;
            }
            else { //leather = 3
                territory.owner.leatherCount += count;
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
                    temp.borders[j].toggle.setToggleVal(0); //none
                else
                    temp.borders[j].toggle.setToggleVal(3); //inactive
            }
        }
    }
}