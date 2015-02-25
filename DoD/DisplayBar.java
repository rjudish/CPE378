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
    private int currentTerritories = Faction.INIT_TERRITORIES;
    private int currentManpower = 0;
    private static GreenfootImage BASE_IMAGE = new GreenfootImage("images/DisplayBar.png");
    public DisplayBar() {
        BASE_IMAGE.scale(DoDWorld.SCREEN_WIDTH, 80);
        redrawDisplayBar();
    }
    
    public void redrawDisplayBar() {
        GreenfootImage image = new GreenfootImage(BASE_IMAGE);
        image.drawImage(new GreenfootImage(territoryText + currentTerritories, 20,
            Color.BLACK, Color.WHITE), 60, 40);
        image.drawImage(new GreenfootImage(manpowerText + currentManpower, 20,
            Color.BLACK, Color.WHITE), 750, 40); 
        setImage(image);
    }
    
    public void updateManpower(int manpower) {
        currentManpower = manpower;
        redrawDisplayBar();
    }
    
    public void updateTerritories(int territories) {
        currentTerritories = territories;
        redrawDisplayBar();
    }
}
