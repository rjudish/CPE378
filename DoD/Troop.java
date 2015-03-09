import greenfoot.*;
import java.util.List;
import java.awt.Color;

/**
 * Write a description of class Troop here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Troop extends Actor
{
    Faction owner;
    int troopManCount;
    int speed = 3;
    int lastTime = 0;
    
    Territory currTerr;
    Territory lastTerr;
    
    Border targetBord = null;
    TroopIcon icon;
    
    public Troop(Faction faction, int incoming) {
        super();
        this.owner = faction;
        this.troopManCount = incoming;
        this.currTerr = null;
        this.lastTerr = null;
        getImage().setTransparency(0);
        icon = new TroopIcon(this, troopManCount, this.owner.fgColor, this.owner.bgColor);
            //setImage(new GreenfootImage(Integer.toString(incoming), 15, faction.fgColor, faction.bgColor));
            // should eventually change this to drawImage [text] on top of the actual troop sprite with a transparent (null) background color
            // Might want to use NumDisplay class that I used for borders to separate the text image object from troop sprite, to set rotation of number back to normal
        
    }
    
    public Troop(Faction faction, int incoming, Border targetBord) {
        this(faction, incoming);
        this.targetBord = targetBord;
    }
    
    
    /**
     * Act - do whatever the Troop wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    /*
    public void act() 
    {
        Territory currTerr = getCurrTerritory();
        //System.out.println("territory");
        if(currTerr != null) {
            for (Border bord : currTerr.borders) {
                //System.out.println("border");
                // This is supposed to be 2, not 1, but currently toggles are all init to 1 and mouseclicks broke so this works for testing
                if (bord.toggle.getToggleVal() == 2) {
                    //System.out.println("turn");
                    turnTowards( bord.getX(), bord.getY());
                }
                if (//bord.inConflict && //doesn't work til inConflict is init'd
                    intersects(bord)) {
                    bord.addTroops(troopManCount);
                    owner.troopList.remove(this);
                    owner.world.removeObject(this);
                }
            }
            move(speed);
        }
    }
    
    */
  
  
    public void act() 
    {
        currTerr = getCurrTerritory();
        //System.out.println("Current rotation: " +getRotation());
        
        boolean cont = true;
        
        /*
        if (currTerr != null && currTerr.isExterior) {
            cont = checkAbsorb();
        }
        */
        if (targetBord != null) {
            cont = checkAbsorb();
        }
        
        if (cont) {
            //if ( ((DoDWorld)getWorld()).getGameTime() > lastTime) {       //limits checking for new territory and changing directions to not happen every frame.
                //lastTime = ((DoDWorld)getWorld()).getGameTime();
                
                if (currTerr != null && currTerr.owner != this.owner) {
                    //System.out.println("Problem: Troop in enemy territory! " + currTerr.territoryID);     //expected now that battle code changes territory ownership
                    owner.world.removeObject(this.icon);
                    owner.world.removeObject(this);
                    cont = false;
                }
        
                //If we've gone into a new territory
                if (cont && lastTerr != currTerr) {
                    if(currTerr != null) {
                        /*
                        if(lastTerr!=null)
                            System.out.println("CurrTerr: ("+ currTerr.getX() +", "+ currTerr.getY() +"). LastTerr: ("+ lastTerr.getX() +", "+ lastTerr.getY() +").");
                        else
                            System.out.println("LastTerr is null!");
                        */
                        if(currTerr.isExterior) {
                            //System.out.println("Troop entering new exterior territory");
                            if (targetBord == null)
                                targetBorder();
                            cont = false;
                        } else {
                            //System.out.println("Troop entering new interior territory");
                            turnToBorder(2);
                        }
                        lastTerr = currTerr;
    
                    } else {
                        System.out.println("Problem: Troop went off the map!");
                            //Only happens when toggle AI is broken
                        owner.world.removeObject(this.icon);
                        owner.world.removeObject(this);
                        cont = false;
                    }
                } else {
                    //System.out.println("Same territory");
                }
            //}
            if (cont) {
                move(speed);
                icon.update();
            }
        }
    }
      
    private void turnToBorder(int turnToToggleVal) {
        boolean hasTarget = false;
        for (Border bord : currTerr.borders) {
            if (!hasTarget && bord.toggle.getToggleVal() == turnToToggleVal) {
                //System.out.println("Turning from ("+ getX() +", "+ getY() +") toward ("+ bord.getX() +", "+ bord.getY() +") [toggleVal: "+ bord.toggle.getToggleVal() +"]");
                //System.out.println("Old rotation: " +getRotation());
                turnTowards( bord.getX(), bord.getY());
                //System.out.println("New rotation: " +getRotation());
                hasTarget = true;
            }
        }
        if (!hasTarget) {
            System.out.println("Problem: No borders in territory are viable target " + currTerr.territoryID);
                //Should be fixed by toggle AI
            owner.world.removeObject(this.icon);
            owner.world.removeObject(this);
        }
    }
    
    private boolean targetBorder() {
        int menLeft = troopManCount;
        int size = 0;
        for (int i = 0; i < 6; i++) {
            if (currTerr.conflictedBorderList[i]) {
                size++;
            }
        }
        //System.out.println("Splitting troop (size " + troopManCount + ") into " + size + " troops");
        for (int i = 0; i < 6; i++) {
            if (currTerr.conflictedBorderList[i]) {
                Troop troop = null;
                if (menLeft - (troopManCount / size) >= (troopManCount / size)) {
                    //System.out.println("Men left: " + menLeft);
                    troop = new Troop(owner, troopManCount / size, currTerr.borders[i]);
                    //currTerr.borders[i].addTroops(troopManCount / size);
                    menLeft -= (troopManCount / size);
                } else {
                    //System.out.println("Last men left: " + menLeft);
                    //currTerr.borders[i].addTroops(menLeft);
                    troop = new Troop(owner, menLeft, currTerr.borders[i]);
                    menLeft = 0;
                }
                owner.world.addObject(troop, getX(), getY());
                owner.world.addObject(troop.getIcon(), getX(), getY());
                troop.turnTowards( currTerr.borders[i].getX(), currTerr.borders[i].getY());
                //System.out.println("Troop of size " + troop.troopManCount + " turning from ("+ getX() +", "+ getY() +") toward ("+ currTerr.borders[i].getX() +", "+ currTerr.borders[i].getY() +") [toggleVal: "+ currTerr.borders[i].toggle.getToggleVal() +"]");

            }
        }
        owner.world.removeObject(this);
        owner.world.removeObject(this.icon);
        return false;
    }
    
    private boolean checkAbsorb() {
        if (intersects(targetBord)) {
            //System.out.println("Absorb");
            

            targetBord.addTroops(troopManCount);
            owner.troopList.remove(this);
            owner.world.removeObject(this.icon);
            owner.world.removeObject(this);
            return false;
        }
        
        /*
        currTerr = getCurrTerritory();
        
        if (currTerr == lastTerr) {
            for (Border bord : currTerr.borders) {
                if (bord.toggle.getToggleVal() == 3) {
                    if (intersects(bord) && bord == targetBord) {
                        //System.out.println("Absorb");
    
                        bord.addTroops(troopManCount);
                        owner.troopList.remove(this);
                        owner.world.removeObject(this);
                        return false;
                    }
                }
            }
        }
        */
        return true;
    }
    
  
  
   
    public Territory getCurrTerritory() {
        Territory rightTerr = null;
        int rightTerrDistSq = 0;
        List<Territory> territories = getIntersectingObjects(Territory.class);
        for (Territory terr : territories) {
            if (rightTerr == null) {
                rightTerr = terr;
                rightTerrDistSq = (getX() - terr.getX()) * (getX() - terr.getX()) + (getY() - terr.getY()) * (getY() - terr.getY());
            } else {
                int distanceSq = (getX() - terr.getX()) * (getX() - terr.getX()) + (getY() - terr.getY()) * (getY() - terr.getY());
                if (distanceSq < rightTerrDistSq) {
                    rightTerr = terr;
                    rightTerrDistSq = distanceSq;
                }
            }
        }
        return rightTerr;
    }
    
    public TroopIcon getIcon() {
        return icon;
    }
}
