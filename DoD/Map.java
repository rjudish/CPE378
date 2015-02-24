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
    
    /**
     * Constructor for objects of class Map.
     * 
     */
    public Map()
    {    
        int numFactions = 4; //Don't change without modifying placeFactions method!
        HexTile[][] tiles = new HexTile[columns][rows];
        placeMapFeatures(tiles);
        int[][] factionsMap = placeFactions(tiles, numFactions);
        int[][] terrainsMap = placeTerrains(tiles);
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(tiles[j][i] == null) {
                    tiles[j][i] = new HexTile(terrainsMap[j][i], factionsMap[j][i]);
                }
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
                        tiles[j][i] = new HexTile(2, 0);
                    }
                }
            }
        }
        //Mountains Pass
        double mountainChance = 0.1;
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                if (tiles[j][i] == null && Math.random() < mountainChance) {
                    tiles[j][i] = new HexTile(3, 0);
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
}
