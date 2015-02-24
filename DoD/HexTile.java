import greenfoot.*;

/**
 * Write a description of class HexTile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HexTile
{

    /**
     * Constructor for objects of class HexTile.
     * 0 = Ocean
     * 1 = Inland Sea
     * 2 = Mountain
     * 3 = Forrest
     * 4 = Hills
     * 5 = Grassland
     * 6 = Plains
     * 7 = Desert
     * 8 = Arctic
     */
    
    int terrain;
    int factionNum;
    public HexTile(int type, int factionNum)
    {
        this.terrain = type;
        this.factionNum = factionNum;
    }
    
    public int getTerrain() {
        return terrain;
    }
    
    public int getFaction() {
        return factionNum;
    }
}
