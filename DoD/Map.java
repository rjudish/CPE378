import greenfoot.*;
import java.util.Random;

/**
 * Write a description of class Map here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Map extends DoDWorld
{
    public static final int rows = 8;
    public static final int columns = 16;
    
    HexTile[][] tileMap;
    java.util.Map<Integer, GreenfootImage> terrains;
    
    private static final int START_X = 140;
    private static final int START_Y = 192;
    private static final int HEX_WIDTH = 120;
    private static final int HEX_HEIGHT = 106;
    /**
     * Constructor for objects of class Map.
     * 
     */
    public Map()
    {    
        int numFactions = 4; //Don't change without modifying placeFactions method!
        tileMap = new HexTile[columns][rows];
        placeMapFeatures(tileMap);
        int[][] factionsMap = placeFactions(tileMap, numFactions);
        int[][] terrainsMap = placeTerrains(tileMap);
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(tileMap[j][i] == null) {
                    tileMap[j][i] = new HexTile(terrainsMap[j][i], factionsMap[j][i]);
                }
            }
        }
        buildMap();
        setupDisplayBar();
        setBackground("images/background.png");
    }
    
    private void buildMap() {
        boolean zigzag = false;
        //random numbers to make things line up. No idea why.
        int offsetY = HEX_WIDTH/2-8;
        
        int territoryID = 0;
        for (int y = START_Y, row = 0; row < rows; y += HEX_HEIGHT-2, row++) {
            for (int x = START_X, column = 0; column < columns; x += HEX_WIDTH*3/4-2, column++) {
                HexTile currTile = tileMap[column][row];
                Actor currObject;
                if (currTile.getTerrain() > 2) {
                    //NOT Map feature
                    Terrain terrainType = Terrain.getTerrain(currTile.getTerrain());
                    Territory currTerritory = new Territory(new Faction(), territoryID, false, terrainType);
                    currObject = currTerritory;
                }
                else {
                    //MapFeature
                    currObject = new MapFeature(Terrain.getTerrain(currTile.getTerrain()));
                }
                if (!zigzag) {
                    addObject(currObject, x, y);//tileMap[column][row], x, y);
                }
                else {
                    addObject(currObject, x, y+offsetY); //tileMap[column][row],
                }
                zigzag = !zigzag;
            }
        }
    
    
    }
    
    /**
     * Psuedo-randomly generates the locations of map features in the world.
     * Then creates and initializes the objects.
     */
    public void placeMapFeatures(HexTile[][] tiles) {
        double waterEdgeChance = 0.2, waterInteriorChance = 0.1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i == 0 || i == rows - 1 || j == 0 || j == columns - 1) {
                    if (Math.random() < waterEdgeChance) {
                        tiles[j][i] = new HexTile(0, 0);
                    }
                } else {
                    if (Math.random() <  waterInteriorChance) {
                        tiles[j][i] = new HexTile(1, 0);
                    }
                }
            }
        }
        //Mountains Pass
        double mountainChance = 0.1;
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                if (tiles[j][i] == null && Math.random() < mountainChance) {
                    tiles[j][i] = new HexTile(2, 0);
                }
            }
        }
    }
    
    /**
     * Psuedo-randomly generates the locations of factions in the world.
     * ALPHA: 4 factions everytime
     */
    public int[][] placeFactions(HexTile[][] tiles, int numFactions) {
        int[][] factionsMap = new int[columns][rows];
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(tiles[j][i] == null) {
                    if(i < rows / 2) {
                        if(j < columns / 2) {
                            factionsMap[j][i] = 1;
                        } else {
                            factionsMap[j][i] = 2;
                        }
                    } else {
                        if(j < columns / 2) {
                            factionsMap[j][i] = 3;
                        } else {
                            factionsMap[j][i] = 4;
                        }
                    }
                } 
            }
        }
        return factionsMap;
    }
    
    /**
     * Sets the terrains of territory objects.
     */
    public int[][] placeTerrains(HexTile[][] tiles) {
        Random rand = new Random();
        int[][] terrainsMap = new int[columns][rows];
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(tiles[j][i] == null) {
                    terrainsMap[j][i] = rand.nextInt(6)+3;
                }
            }
        }
        return terrainsMap;
    }
    
    public void setupDisplayBar() {
        addObject(new TerritoryStatistic(), 100, 40);
        addObject(new ManpowerStatistic(), 1500, 40);
    }
}
