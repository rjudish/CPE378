import greenfoot.*;

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
    int speed = 1;
    
    public Troop(Faction faction, int incoming) {
        super();
        this.owner = faction;
        this.troopManCount = incoming;
    }
    
    /**
     * Act - do whatever the Troop wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
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
    
    public Territory getCurrTerritory() {
        Territory terr = (Territory) getOneIntersectingObject(Territory.class);
        return terr;
    }
}
