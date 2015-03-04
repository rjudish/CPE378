import greenfoot.*;
import java.util.ArrayList;

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
    
    static void step(ArrayList<Territory> conflictedTerritoryList, AI ai) {
        //System.out.println("Executing Battle Code.");
        //Territory terr = null;
        for(Territory terr : conflictedTerritoryList) {
            //for(int iterator = conflictedTerritoryList.size() ; iterator > 0; iterator-- ) {
            //    terr = conflictedTerritoryList.get(iterator);
            
            Faction owner = terr.getOwner();

            for(int i = 0; i < 6; i++) {
                Border bord = terr.borders[i];
                Border other = bord.otherBorder;
                
                if (terr.borders[i].inConflict) {
                    //if (terr.territoryID == 48 || terr.territoryID == 64) { // limiting for testing
                        //System.out.println("Dealing with Territory " + terr.territoryID
                        //    + "'s border " + bord.borderID + " | conflicted: " + bord.inConflict);
                        
                        int aCount = bord.getBorderManCount();
                        int bCount = other.getBorderManCount();
                        //System.out.println(aCount + " vs. " + bCount);
                        
                        if (aCount / 10 > bCount) {
                            // A wins territory
                            //System.out.println("Territory " + terr.territoryID + " has changed ownership!");
                            
                            ai.battleOutcome(other.parentTerritory, bord.parentTerritory.owner); //lostTerritory, winningFaction
                            //other.toggle.setToggleVal(1);
                            //bord.toggle.setToggleVal(2);
                            
                            //Disperse troops remaining at victorious border
                            owner.addTroops(bord.getBorderManCount(), bord.getX(), bord.getY());
                            owner.redFactionManCount(bord.getBorderManCount());    // they were already counted, don't want to double count
                            bord.setBorderManCount(0);
                            
                            other.parentTerritory.newOwner( terr.getOwner() );
                            
                            //Disperse troops remaining in adjacent borders that are no longer in conflict
                            for (Territory aTerr : bord.parentTerritory.adjacentTerritoryList) {
                                if (aTerr != null) {
                                    for( int j = 0; j < 6; j++) {
                                        Border aBord = aTerr.borders[j];
                                        if (!aBord.inConflict && aBord.getBorderManCount() != 0) {
                                            owner.addTroops(aBord.getBorderManCount(), aBord.getX(), aBord.getY());
                                            owner.redFactionManCount(aBord.getBorderManCount());    // they were already counted, don't want to double count
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
            }
        }

    }
    
}
