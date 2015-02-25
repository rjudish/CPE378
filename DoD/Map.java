import greenfoot.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
/**
 * Write a description of class Map here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Map extends DoDWorld
{
    public static final int ROWS = 8;
    public static final int COLUMNS = 16;
    public static final int NUM_FACTIONS = 4; //Don't change without modifying placeFactions method!
    public static final int NUM_MAP_FEATURES = 3;

    private static final int START_X = 140;
    private static final int START_Y = 192;
    private static final int HEX_WIDTH = 120;
    private static final int HEX_HEIGHT = 106;

    int cameraX = 0;
    int cameraY = 0;
    public static final int SCROLL_SPEED = 10;

    DisplayBar displayBar;

    /**
     * Constructor for objects of class Map.
     * 
     */
    public Map()
    {    
        
        HexTile[][] tileMap = new HexTile[COLUMNS][ROWS];
        placeMapFeatures(tileMap);
        initializeFactionMap();
        int[][] factionsMap = placeFactions(tileMap, NUM_FACTIONS);
        int[][] terrainsMap = placeTerrains(tileMap);
        for (int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                if(tileMap[j][i] == null) {
                    tileMap[j][i] = new HexTile(terrainsMap[j][i], factionsMap[j][i]);
                }
            }
        }
        Territory[][] territoryMap = buildMap(tileMap);
        setBackground("images/background.png");
        setPaintOrder(DisplayBar.class, Troop.class, Actor.class);
        setupDisplayBar();
    }
    
    public void act() {
        super.act();
        scroll();
    }
    
    private Territory[][] buildMap(HexTile[][] tileMap) {
        boolean zigzag = false;
        //random numbers to make things line up. No idea why.
        int offsetY = HEX_WIDTH/2-8;
        Territory[][] territoryMap = new Territory[COLUMNS][ROWS];
        int territoryID = 0;
        for (int y = START_Y, row = 0; row < ROWS; y += HEX_HEIGHT-2, row++) {
            for (int x = START_X, column = 0; column < COLUMNS; x += HEX_WIDTH*3/4-2, column++) {
                HexTile currTile = tileMap[column][row];
                Actor currObject;
                if (currTile.getTerrain() > 2) {
                    //NOT Map feature
                    Terrain terrainType = Terrain.getTerrain(currTile.getTerrain());
                    Faction currFaction = factions.get(currTile.getFaction());
                    Territory currTerritory = new Territory(currFaction, territoryID, false, terrainType);
                    currObject = currTerritory;
                    territories.add(currTerritory);
                    territoryMap[column][row] = currTerritory;
                }
                else {
                    //MapFeature
                    currObject = new MapFeature(Terrain.getTerrain(currTile.getTerrain()));
                }
                if (!zigzag) {
                    addObject(currObject, x, y);
                }
                else {
                    addObject(currObject, x, y+offsetY);
                }
                zigzag = !zigzag;
            }
        }
        return territoryMap;
    
    }
    
    /**
     * Psuedo-randomly generates the locations of map features in the world.
     * Then creates and initializes the objects.
     */
    public void placeMapFeatures(HexTile[][] tiles) {
        double waterEdgeChance = 0.2, waterInteriorChance = 0.1;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (i == 0 || i == ROWS - 1 || j == 0 || j == COLUMNS - 1) {
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
        for (int i = 1; i < ROWS - 1; i++) {
            for (int j = 1; j < COLUMNS - 1; j++) {
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
        int[][] factionsMap = new int[COLUMNS][ROWS];
        for (int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                if(tiles[j][i] == null) {
                    if(i < ROWS / 2) {
                        if(j < COLUMNS / 2) {
                            factionsMap[j][i] = 1;
                        } else {
                            factionsMap[j][i] = 2;
                        }
                    } else {
                        if(j < COLUMNS / 2) {
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
        int[][] terrainsMap = new int[COLUMNS][ROWS];
        for (int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                if(tiles[j][i] == null) {
                    terrainsMap[j][i] = rand.nextInt(6)+NUM_MAP_FEATURES;
                }
            }
        }
        return terrainsMap;
    }
    
    public void setupDisplayBar() {
        displayBar = new DisplayBar();
        addObject(displayBar, SCREEN_WIDTH/2, 40);
    }
    
    private void initializeFactionMap() {
        for (int i = 1; i <= NUM_FACTIONS; i++) {
            Faction newFaction = new Faction(this);
            newFaction.setFlag(new GreenfootImage("faction" + i + ".png"));
            factions.put(i, newFaction);
        }
    }
    
        public void scroll() {
        MouseInfo info = Greenfoot.getMouseInfo();
        if (info != null) {
            int shiftX = 0;
            int shiftY = 0;
            if (info.getX() < 30 && cameraX > 0) {
                shiftX = -SCROLL_SPEED;
            }
            else if (info.getX() > 870 && cameraX < (1600-900)) {
                shiftX = SCROLL_SPEED;
            }
            if (info.getY() < 110 && cameraY > 0) {
                shiftY = -SCROLL_SPEED;
            }
            else if (info.getY() > 570 && cameraY < (1080-600)) {
                shiftY = SCROLL_SPEED;
            }
            if (shiftX != 0 || shiftY != 0) {
                cameraX += shiftX;
                cameraY += shiftY;
                moveAllObjects(shiftX, shiftY);
            }
        }
    }
    
    private void addAdjacencyLists(Territory[][] territoryMap, int[][] terrainsMap) {
        for (int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                if(terrainsMap[j][i] >= NUM_MAP_FEATURES) { //if not map feature
                    if(j % 2 == 0) { //even column
                        if(j > 1 && i > 0 && i < ROWS - 1 && j < COLUMNS - 1) { //not edge
                            territoryMap[j][i].adjacentTerritoryList.add(territoryMap[j-1][i-1]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j-1][i]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j][i+1]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j+1][i]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j+1][i-1]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j][i-1]);
                        } else { //edge
                            
                            
                            
                        }
                    } else { //odd column
                        if(j > 1 && i > 0 && i < ROWS - 1 && j < COLUMNS - 1) { //not edge
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j-1][i-1]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j-1][i]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j][i+1]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j+1][i+1]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j+1][i]);
                            (territoryMap[j][i]).adjacentTerritoryList.add(territoryMap[j][i-1]);
                        } else { //edge
                            
                        }
                    }
                    
                }
            }
        }
    }
    
    public void moveAllObjects(int shiftX, int shiftY) {
        List<Actor> actors = getObjects(Actor.class);
        for (Actor actor : actors) {
            if (actor.getClass() != DisplayBar.class) {
                actor.setLocation(actor.getX() - shiftX, actor.getY() - shiftY);
            }
        }
    }

    public void drawRivers() {
        for (Territory territory : territories) {
            Border[] borders = territory.getBorders();
            for (int i = 0; i < borders.length; i++) {
                if (borders[i].hasRiver) {
                    int centerX = territory.getX();
                    int centerY = territory.getY();
                    int x1 = 0;
                    int x2 = 0;
                    int y1 = 0;
                    int y2 = 0;
                    //Center to flat is 53px
                    switch (i) {
                    case 0: //N
                        break;
                    case 1: //NE
                        break;
                    case 2: //SE
                        break;
                    case 3: //S
                        break;
                    case 4: //SW
                        break;
                    case 5: //NW
                        break;
                    }
                    //GreenfootImage parentImage = parentTerritory.getImage();
                    //parentImage.setColor(new Color(112, 141, 241));
                    //parentImage.drawLine(this.getX(), this.getY(), this.getX() + 100, this.getY() + 100);
                    //parentImage.drawPolygon();
                    //parentTerritory.setImage(parentImage);
                }
            }
        }
    }
}
