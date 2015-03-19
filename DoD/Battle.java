import greenfoot.*;
import java.util.List;

/**
 * Write a description of class Battle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
final public class Battle
{

    private Battle()
    {
    }
    
    private static int territoryDefenseBonus(Territory territory) {
        int rtn = 0;
        int type = territory.terrain.id;
        
        if (type == 3) { //forrest
            rtn += 1;
        }
        else if (type == 4) { //hill
            rtn += 1;
        }
        else if (type == 7) { //desert
            rtn -= 1;
        }
        else if (type == 8) { //arctic
            rtn -= 1;
        }
        
        return rtn;
    }
    
    static void step(List<Territory> territoryList, AI ai, java.util.Map<Integer, Faction> factions) {
        //System.out.println("Executing Battle Code.");
        //Territory terr = null;
        
        int[] newManCount = new int[9];
        
        for(Territory terr : territoryList) {
            if (terr.isExterior && !terr.hasChangedOwner) {
                //for(int iterator = conflictedTerritoryList.size() ; iterator > 0; iterator-- ) {
                //    terr = conflictedTerritoryList.get(iterator);
                
                Faction owner = terr.getOwner();
    
                for(int i = 0; i < 6; i++) {
                    Border bord = terr.borders[i];
                    Border other = bord.otherBorder;
                    
                    if (terr.borders[i].inConflict && !terr.hasChangedOwner && !other.parentTerritory.hasChangedOwner) {
                        //if (terr.territoryID == 48 || terr.territoryID == 64) { // limiting for testing
                            //System.out.println("Dealing with Territory " + terr.territoryID
                            //    + "'s border " + bord.borderID + " | conflicted: " + bord.inConflict);
                            
                            int aCount = bord.getBorderManCount();
                            int bCount = other.getBorderManCount();
                            
                            aCount += aCount * terr.owner.ironCount / 10; //resource offense
                            bCount += bCount * other.parentTerritory.owner.leatherCount / 10; //resource defense
                            bCount += bCount * territoryDefenseBonus(other.parentTerritory) / 10; //terrain defense
                            
                            //System.out.println(terr.owner.id + ": " + terr.owner.ironCount + " iron " + other.parentTerritory.owner.id + ": " + other.parentTerritory.owner.leatherCount + " leather");
                            //System.out.println(aCount + " vs. " + bCount);
                            
                            if (aCount / 10 > bCount) {
                                // A wins territory
                                //System.out.println("Territory " + terr.territoryID + " has changed ownership!");
                                
                                boolean gameOver = ai.victoryCheck(other.parentTerritory.owner, bord.parentTerritory.owner);
                                if (gameOver) {
                                    return;
                                }
                                //Disperse troops remaining at victorious border
                                owner.addTroops(bord.getBorderManCount(), bord.getX(), bord.getY());
                                //owner.redFactionManCount(bord.getBorderManCount());    // they were already counted, don't want to double count
                                bord.setBorderManCount(0);
                                
                                //loser
                                for (int j = 0; j < 6; j++) {
                                    //other.parentTerritory.owner.redFactionManCount(other.parentTerritory.borders[j].getBorderManCount());
                                    other.parentTerritory.borders[j].setBorderManCount(0);
                                }
                                
                                boolean noEnemies = ai.battleOutcome(other.parentTerritory, bord.parentTerritory.owner); //lostTerritory, winningFaction
                                other.parentTerritory.newOwner( terr.getOwner() );
                                if (noEnemies)
                                    ai.reToggle(other.parentTerritory);
                                
                                //Disperse troops remaining in adjacent borders that are no longer in conflict
                                for (Territory aTerr : bord.parentTerritory.adjacentTerritoryList) {
                                    if (aTerr != null) {
                                        for( int j = 0; j < 6; j++) {
                                            Border aBord = aTerr.borders[j];
                                            if (!aBord.inConflict && aBord.getBorderManCount() != 0) {
                                                owner.addTroops(aBord.getBorderManCount(), aBord.getX(), aBord.getY());
                                                //owner.redFactionManCount(aBord.getBorderManCount());    // they were already counted, don't want to double count
                                                aBord.setBorderManCount(0);
                                            }
                                        }
                                    }
                                }
    
                                
                            } else if (aCount > bCount) {
                                // A wins battle round
                                //System.out.println("won battle");
                                bord.battleWon();
                            }
                            // B vs A gets checked when we loop through opponent's conflicted territory
                        //}
                    }
                    
                    if (terr.borders[i].inConflict) {
                        newManCount[terr.owner.id] += terr.borders[i].getBorderManCount();
                    }
                }
            }
        }
        
        for (int i = 1; i < newManCount.length; i++) {
            if (factions.get(i) != null)
                factions.get(i).setFactionManCount(newManCount[i]);
        }
        
        for (int i = 1; i < newManCount.length; i++) {
            if (newManCount[i] > 0) {
                Faction fac = factions.get(i);
                
                for (int j = 0; j < fac.troopList.size(); j++) {
                    newManCount[i] += fac.troopList.get(j).troopManCount;
                }
            }
        }
    }
    
    
    static void cleanup(List<Territory> territoryList) {
        //System.out.println("Executing Battle Code.");
        //Territory terr = null;
        for(Territory terr : territoryList) {
            terr.hasChangedOwner = false;
        }
    }
    
}
