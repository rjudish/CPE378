import greenfoot.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
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
        addAdjacencyLists(territoryMap, terrainsMap);
        initConflictedBorders(territoryMap);
        
        drawRivers(); //run this after river init
        setBackground("images/background.png");
        setPaintOrder(DisplayBar.class, Toggle.class, Troop.class, Actor.class);
        setupDisplayBar();
        
        ai = new AI(factions);
        ai.defaultToggle();
        ai.initToggle();
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
                territoryID++;
            }
        }
        return territoryMap;
    
    }
    
    /**
     * Psuedo-randomly generates the locations of map features in the world.
     * Then creates and initializes the objects.
     */
    public void placeMapFeatures(HexTile[][] tiles) {
        double waterEdgeChance = 0.0, waterInteriorChance = 0.1; //ALPHA no edges to prevent islands
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
        displayBar.setPlayer(factions.get(1));
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
                            adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                            adjacencyNullCheck(territoryMap, j, i, 1, j+1, i-1);
                            adjacencyNullCheck(territoryMap, j, i, 2, j+1, i);
                            adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                            adjacencyNullCheck(territoryMap, j, i, 4, j-1, i);
                            adjacencyNullCheck(territoryMap, j, i, 5, j-1, i-1);
                        } else { //edge
                            if(j == 0) { //left edge
                                if(i == 0) { //top
                                   adjacencyNullCheck(territoryMap, j, i, 2, j+1, i);
                                   adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                                } else if (i == ROWS - 1) { //bottom
                                    adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                                    adjacencyNullCheck(territoryMap, j, i, 1, j+1, i-1);
                                    adjacencyNullCheck(territoryMap, j, i, 2, j+1, i);
                                } else { // middle
                                    adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                                    adjacencyNullCheck(territoryMap, j, i, 1, j+1, i-1);
                                    adjacencyNullCheck(territoryMap, j, i, 2, j+1, i);
                                    adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                                }
                            } else if (j == COLUMNS - 1) { //right edge
                                if(i == 0) { //top
                                    adjacencyNullCheck(territoryMap, j, i, 2, j+1, i);
                                    adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                                } else if (i == ROWS - 1) { //bottom
                                    adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                                    adjacencyNullCheck(territoryMap, j, i, 1, j+1, i-1);
                                    adjacencyNullCheck(territoryMap, j, i, 2, j+1, i);
                                } else { // middle
                                    adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                                    adjacencyNullCheck(territoryMap, j, i, 1, j+1, i-1);
                                    adjacencyNullCheck(territoryMap, j, i, 2, j+1, i);
                                    adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                                }
                            } else if(i == 0) { //top
                                adjacencyNullCheck(territoryMap, j, i, 2, j+1, i);
                                adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                                adjacencyNullCheck(territoryMap, j, i, 4, j-1, i);
                            } else if (i == ROWS - 1) { //bottom
                                adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                                adjacencyNullCheck(territoryMap, j, i, 1, j+1, i-1);
                                adjacencyNullCheck(territoryMap, j, i, 2, j+1, i);
                                adjacencyNullCheck(territoryMap, j, i, 4, j-1, i);
                                adjacencyNullCheck(territoryMap, j, i, 5, j-1, i-1);
                            }
                            else {
                                System.out.println("Error in even edge cases");
                            }
                            
                        }
                    } else { //odd column
                        if(j > 1 && i > 0 && i < ROWS - 1 && j < COLUMNS - 1) { //not edge
                            adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                            adjacencyNullCheck(territoryMap, j, i, 1, j+1, i);
                            adjacencyNullCheck(territoryMap, j, i, 2, j+1, i+1);
                            adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                            adjacencyNullCheck(territoryMap, j, i, 4, j-1, i+1);
                            adjacencyNullCheck(territoryMap, j, i, 5, j-1, i);
                        } else { //edge
                            if (j == COLUMNS - 1) { //right edge
                                if(i == 0) { //top
                                    adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                                    adjacencyNullCheck(territoryMap, j, i, 4, j-1, i+1);
                                    adjacencyNullCheck(territoryMap, j, i, 5, j-1, i);
                                } else if (i == ROWS - 1) { //bottom
                                   adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                                   adjacencyNullCheck(territoryMap, j, i, 5, j-1, i);
                                } else { // middle
                                   adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                                   adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                                   adjacencyNullCheck(territoryMap, j, i, 4, j-1, i+1);
                                   adjacencyNullCheck(territoryMap, j, i, 5, j-1, i);
                                }
                            } else if(i == 0) { //top
                                System.out.println("Top! j: "+j+" i: "+i);
                                adjacencyNullCheck(territoryMap, j, i, 1, j+1, i);
                                adjacencyNullCheck(territoryMap, j, i, 2, j+1, i+1);
                                adjacencyNullCheck(territoryMap, j, i, 3, j, i+1);
                                adjacencyNullCheck(territoryMap, j, i, 4, j-1, i+1);
                                adjacencyNullCheck(territoryMap, j, i, 5, j-1, i);
                            } else if (i == ROWS - 1) { //bottom
                               adjacencyNullCheck(territoryMap, j, i, 0, j, i-1);
                               adjacencyNullCheck(territoryMap, j, i, 1, j+1, i);
                               adjacencyNullCheck(territoryMap, j, i, 5, j-1, i);
                            }else {
                                System.out.println("Error in odd edge cases");
                            }
                        }
                    }
                    
                }
            }
        }
    }
    
    private void adjacencyNullCheck(Territory[][] territoryMap, int j,int i,int direction, int x,int y) {
        if(territoryMap[x][y] != null) {
            
            territoryMap[j][i].adjacentTerritoryList[direction] = territoryMap[x][y];
            
            int oppositeDir = direction + 3;
            if (oppositeDir > 5)
                oppositeDir -=6;
            
            territoryMap[j][i].borders[direction].otherBorder = territoryMap[x][y].borders[oppositeDir];

        }
    }
    
    private void initConflictedBorders(Territory[][] territoryMap) {
        for (int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                if(territoryMap[j][i] != null) {
                    for(int k=0; k < 6; k++) {
                        if(territoryMap[j][i].adjacentTerritoryList[k] != null && territoryMap[j][i].getOwner() != territoryMap[j][i].adjacentTerritoryList[k].getOwner()) {
                            territoryMap[j][i].conflictedBorderList[k] = true;
                            territoryMap[j][i].isExterior = true;
                            territoryMap[j][i].borders[k].toggle.setToggleVal(3);
                        } else if (territoryMap[j][i].adjacentTerritoryList[k] == null) {
                            territoryMap[j][i].borders[k].toggle.setToggleVal(0);
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
        int offset = 3;
        for (Territory territory : territories) {
            Border[] borders = territory.getBorders();
            for (int i = 0; i < borders.length; i++) {
                if (borders[i].hasRiver) {
                    int[] xs = null;
                    int[] ys = null;
                    //Center to flat is 53px; 106 tall
                    //Center to point is 60px; 120 wide
                    switch (i) {
                    case 0: //N
                        xs = new int[] {30, 30-offset, 90+offset, 90};
                        ys = new int[] {0, offset, offset, 0};
                        break;
                    case 1: //NE
                        xs = new int[] {90, 90-offset, 120-offset, 120};
                        ys = new int[] {0, 0, 53 + offset, 53};
                        break;
                    case 2: //SE
                        xs = new int[] {120, 120-offset-2, 90-offset-2, 90};
                        ys = new int[] {53, 53 - offset, 106, 106};
                        break;
                    case 3: //S
                        xs = new int[] {90, 90+offset, 30-offset, 30};
                        ys = new int[] {106, 106-offset-1, 106-offset-1, 106};
                        break;
                    case 4: //SW
                        xs = new int[] {30, 30+offset, offset, 0};
                        ys = new int[] {106, 106, 53 - offset, 53};
                        break;
                    case 5: //NW
                        xs = new int[] {0, offset, 30 + offset, 30};
                        ys = new int[] {53, 53 + offset, 0, 0};
                        break;
                    }
                    GreenfootImage parentImage = territory.getImage();
                    parentImage.setColor(new Color(112, 141, 241));
                    parentImage.fillPolygon(xs, ys, 4);
                }
            }
        }
    }
}
