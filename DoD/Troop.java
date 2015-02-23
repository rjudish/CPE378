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
        move(speed);
    }    
}
