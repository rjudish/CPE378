import greenfoot.*;
import java.util.HashMap;
/**
 * Write a description of class Terrain here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public enum Terrain
{

    OCEAN(0, "images/oceanTile.png"),
    INLAND_SEA(1, "images/innerSeaTile.png"),
    MOUNTAIN(2, "images/mountainTile.png"),
    FOREST(3, "images/forestTile.png"),
    HILLS(4, "images/hillsTile.png"),
    GRASSLAND(5, "images/grasslandTile.png"),
    PLAINS(6, "images/plainsTile.png"),
    DESERT(7, "images/desertTile.png"),
    ARCTIC(8, "images/arcticTile.png");
   
    int id;
    String image;
    private static java.util.Map<Integer, Terrain> terrains = new HashMap<Integer, Terrain>();
    Terrain(int id, String image)
    {
        this.id = id;
        this.image = image;
    }
    static {
        for (Terrain terr : Terrain.values()) {
            terrains.put(terr.id, terr);
        }
    }
    
    public static Terrain getTerrain(int id)
    {
        return terrains.get(id);
    }
    
    public String getImage() {
        return image;
    }
}
