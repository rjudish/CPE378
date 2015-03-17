import greenfoot.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Write a description of class DoDWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DoDWorld extends World
{
    private Player player;
    public AI ai;
    private int gameTime = 0;
    private int deltaTime = 0;
    public boolean gameOver = false;
    public DisplayBar displayBar;
    public ArrayList<Territory> conflictedTerritoryList = new ArrayList<Territory>();
    private static final int START_X = 140;
    private static final int START_Y = 152;
    private static final int HEX_WIDTH = 120;
    private static final int HEX_HEIGHT = 107;
    
    public static final int SCREEN_WIDTH = 900;
    public static final int SCREEN_HEIGHT = 600;
    
    List<Territory> territories = new ArrayList<Territory>();    
    java.util.Map<Integer, Faction> factions = new HashMap<Integer, Faction>();
    /**
     * Constructor for objects of class DoDWorld.
     * 
     */
    public DoDWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(SCREEN_WIDTH, SCREEN_HEIGHT, 1, false); 
        Faction playerFaction = new Faction(this); // for testing purposes
        
        //Territory TEST_TERRITORY = new Territory(playerFaction, 1, true);
        //this.addObject(TEST_TERRITORY, 400, 300);
        //conflictedTerritoryList.add(TEST_TERRITORY);
        //Territory TEST_TERRITORY2 = new Territory(playerFaction, 2, false);
        //this.addObject(TEST_TERRITORY2, 300, 300);
        
        
        
    }
    
    public int getGameTime() {
        return gameTime;
    }
    
    public void act() {
        deltaTime = (deltaTime + 1) % 60;
        if (deltaTime == 0) {
            gameTime++;
            //System.out.println("Game Time: " + gameTime);
            if (gameTime > 3 && !gameOver) {   //Give the initial wave of troops time to get to a conflicted Border
                Battle.step(territories, ai);
                Battle.cleanup(territories);
            }
        }
        
        //System.out.println(conflictedTerritoryList.size() + " conflicted territories");

    }
    
    public void playerWin() {
        gameOver = true;
        Greenfoot.setWorld(new EndScreen(true));
    }
    
    public void playerLoss() {
        gameOver = true;
        Greenfoot.setWorld(new EndScreen(false));
    }
}
