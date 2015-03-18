import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class DisplayBar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DisplayBar extends Actor
{
    private static final String territoryText = "Territories: ";
    private static final String manpowerText = "Manpower: ";
    private static final String factionText = "Faction: ";
    private int currentTerritories = Faction.INIT_TERRITORIES;
    private int currentManpower = 0;
    private Faction player;
    private static GreenfootImage BASE_IMAGE = new GreenfootImage("images/DisplayBar.png");
    public DisplayBar(Faction player) {
        BASE_IMAGE.scale(DoDWorld.SCREEN_WIDTH, 80);
        this.player = player;
        redrawDisplayBar();
    }
    
    public void act() {
        if (currentManpower != player.getFactionManCount() ||
                currentTerritories != player.territoryList.size()) {
            currentManpower = player.getFactionManCount();
            currentTerritories = player.territoryList.size();
            redrawDisplayBar();
        }
    }
    private void redrawDisplayBar() {
        GreenfootImage image = new GreenfootImage(BASE_IMAGE);
        image.drawImage(new GreenfootImage(factionText, 20, Color.BLACK, Color.WHITE), 5, 20);
        image.setColor(player.bgColor);
        image.fillRect(65, 20, 30, 20);
        image.drawImage(new GreenfootImage(territoryText + currentTerritories, 20,
            Color.BLACK, Color.WHITE), 5, 40);
        image.drawImage(new GreenfootImage(manpowerText + currentManpower/1000 + "K", 20,
            Color.BLACK, Color.WHITE), 5, 60); 
        setImage(image);
    }
    
    public void updateManpower(int manpower) {
        currentManpower = manpower;
        //redrawDisplayBar();
    }
    
    public void updateTerritories(int territories) {
        currentTerritories = territories;
        //redrawDisplayBar();
    }
    
    public void setPlayer(Faction player) {
        this.player = player;
    }
}
