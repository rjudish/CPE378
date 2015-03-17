import greenfoot.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
<<<<<<< HEAD
import java.lang.Math;

=======
import java.util.Collections;
>>>>>>> origin/master
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
    public static final int NUM_MAP_FEATURES = 3;

    private static final int START_X = 140;
    private static final int START_Y = 192;
    private static final int HEX_WIDTH = 120;
    private static final int HEX_HEIGHT = 106;

    int cameraX = 0;
    int cameraY = 0;
    public static final int SCROLL_SPEED = 10;
    
    int numFactions;

    DisplayBar displayBar;
    Faction player;
    java.util.Map<Integer, String> resourceTypes;
    
    /**
     * Constructor for objects of class Map.
     * 
     */
    public Map(int numFactions)
    {    
        this.numFactions = ++numFactions;
        HexTile[][] tileMap = new HexTile[COLUMNS][ROWS];
        placeMapFeatures(tileMap);
        initializeFactionMapping(numFactions);
        initializeResourceMapping();
        int[][] factionsMap = placeFactions(numFactions);
        int[][] terrainsMap = placeTerrains(tileMap);
        int[][] resourcesMap = placeResources(terrainsMap, numFactions);
        for (int i = 0; i < ROWS; i++) {
            for(int j = 0; j < COLUMNS; j++) {
                if(tileMap[j][i] == null) {
                    tileMap[j][i] = new HexTile(terrainsMap[j][i], factionsMap[j][i]);
                }
            }
        }
        Territory[][] territoryMap = buildMap(tileMap, resourcesMap);
        addAdjacencyLists(territoryMap, terrainsMap);
        initConflictedBorders(territoryMap);
        for(int i=0; i<numFactions; i++) {
            setRecruits(factions, numFactions);
        }
        
        drawRivers(); //run this after river init
        setBackground("images/background.png");
        setPaintOrder(DisplayBar.class, Toggle.class, NumDisplay.class, TroopIcon.class, Actor.class);
        setupDisplayBar(player);
        
        ai = new AI(this, factions, territories);
        ai.initToggle();
    }
    
    public void act() {
        super.act();
        scroll();
    }
    
    private Territory[][] buildMap(HexTile[][] tileMap, int[][] resourcesMap) {
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
                    Territory currTerritory = new Territory(currFaction, territoryID, false, terrainType, resourcesMap[column][row]);
                    currFaction.territoryList.add(currTerritory);
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
    
       
    public int[][] placeFactions(int numFactions) {
        int[][] factionsMap = new int[COLUMNS][ROWS];
        int spacing = COLUMNS/(numFactions/2);
        for (int i = 0; i < ROWS; i++) { 
            for(int j = 0; j < COLUMNS; j++) {
                if(i < ROWS/2) {
                   factionsMap[j][i] = Math.min(j/spacing+1,numFactions/2);
                }else {
                    factionsMap[j][i] = Math.min(j/spacing+numFactions/2+1,numFactions);
                }
            }
        }            
        
        return factionsMap;
    }
    
    public void setRecruits(java.util.Map<Integer, Faction> factions, int numFactions) {
        Random rand = new Random();
        for (int i = 1; i <= factions.size(); i++) {
            Faction currFaction = factions.get(i);
            for(int j=0; j < currFaction.territoryList.size(); j++) {
                if(rand.nextInt(10) < 1) {
                    currFaction.territoryList.get(j).recruitNumber = 800 + rand.nextInt(150);
                } else {
                    currFaction.territoryList.get(j).recruitNumber = 50 + rand.nextInt(100);
                }
                currFaction.territoryList.get(j).setDisplay();
            }
        }
    }

    
    public int[][] placeResources(int[][] terrainsMap, int numFactions) {
        Random rand = new Random();
        int[][] resourcesMap = new int[COLUMNS][ROWS];
        int horsesLeft = 8+numFactions%2, leatherLeft = 8+numFactions%2, ironLeft = 4+numFactions%2;
        while(ironLeft > 0 || horsesLeft > 0 || leatherLeft > 0) {
            int row = rand.nextInt(ROWS);
            int column = rand.nextInt(COLUMNS);
            if(ironLeft != 0 && (terrainsMap[column][row] == 3 || terrainsMap[column][row] == 4)) {
                resourcesMap[column][row] = 1;//Iron
                ironLeft--;
            } else if (horsesLeft != 0 && (terrainsMap[column][row] == 5 || terrainsMap[column][row] == 6)) {
                resourcesMap[column][row] = 2;//Horses
                horsesLeft--;
            } else if (leatherLeft != 0 && (terrainsMap[column][row] == 5 || terrainsMap[column][row] == 6)) {
                resourcesMap[column][row] = 3;//Leather
                leatherLeft--;
            }
            else {
                resourcesMap[column][row] = 0;
            }
        }
        return resourcesMap;
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
    
    public void setupDisplayBar(Faction player) {
        displayBar = new DisplayBar(player);
        addObject(displayBar, SCREEN_WIDTH/2, 40);
    }
    
    private void initializeFactionMapping(int numFactions) {
        Random rand = new Random();
        /*List<Integer> randomFactionList = new ArrayList<Integer>();
        for (int i = 0; i < numFactions; i++) {
            randomFactionList.add(i+1);
        }*/
        List<Integer> randomColorList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        Collections.shuffle(randomColorList);
        for (int i = 1; i <= numFactions; i++) {
            Faction newFaction = new Faction(this, i);
            newFaction.setColors(randomColorList.get(i-1));
            //addObject(newFaction, 10, 10);    // This was so I could use the faction's act method for test output
            factions.put(i, newFaction);
        }
        player = factions.get(rand.nextInt(numFactions) + 1);
    }
    
    private void initializeResourceMapping() {
        resourceTypes = new java.util.HashMap<Integer, String>();
        resourceTypes.put(1, "I");
        resourceTypes.put(2, "H");
        resourceTypes.put(3, "L");
    }
    
    public String getResourceType(int res) {
        return resourceTypes.get(res);
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
                                System.out.println("Error in even edge cases j: "+j+" i: "+i);
                            }
                            
                        }
                    } else { //odd column
                        if(j >= 1 && i > 0 && i < ROWS - 1 && j < COLUMNS - 1) { //not edge
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
                                System.out.println("Error in odd edge cases j: "+j+" i: "+i);
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
                            territoryMap[j][i].borders[k].inConflict = true;
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
