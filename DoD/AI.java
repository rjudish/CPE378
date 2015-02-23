import greenfoot.*;
import java.lang.*;

/**
 * Write a description of class AI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class AI extends Actor
{
    /**
     * Act - do whatever the AI wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        for (int i = 0; j < factionNumber; i++) {
            for (int j = 0; j < numOfTerr; j++) {
                int allowed = 8 - notAllowed;
                
                
                int choice = (int) (Math.random() * allowed);
                Factions[i].terr[j].moveTroops(j);
            }
        }
    }    
}
