import greenfoot.*;
import java.util.ArrayList;


/**
 * Write a description of class DoDWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DoDWorld extends World
{
    private Player player;
    private AI ai;
    private int gameTime = 0;
    private int deltaTime = 0;
    public DisplayBar displayBar;
    private ArrayList<Territory> conflictedTerritoryList = new ArrayList<Territory>();

        
    /**
     * Constructor for objects of class DoDWorld.
     * 
     */
    public DoDWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
        player = new Player();
        ai = new AI(); //or add an integer parameter for more than one AI
        displayBar = new DisplayBar();
        Territory TEST_TERRITORY = new Territory(1, true);
        this.addObject(TEST_TERRITORY, 400, 300);
        conflictedTerritoryList.add(TEST_TERRITORY);
        Territory TEST_TERRITORY2 = new Territory(2, false);
        this.addObject(TEST_TERRITORY2, 300, 300);
        
    }
    
    public int getGameTime() {
        return gameTime;
    }
    
    public void act() {
        deltaTime = (deltaTime + 1) % 60;
        if (deltaTime == 0) {
            gameTime++;
            System.out.println("Game Time: " + gameTime);
            Battle.step(conflictedTerritoryList);
            System.out.println("Executing Spawn Code...");
            
        }
    }
}
