import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class Toggle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Toggle extends Actor
{
    int toggleVal = 0;
    int lastToggleVal = 0;
    Border parentBorder;
    Faction player;
    
    public Toggle(Faction player, Border parentBorder) {
        this.player = player;
        this.parentBorder = parentBorder;
        boolean done = false;
        
        setAngle();
        
    }
    
    public void act() {
        if (player.id == parentBorder.parentTerritory.owner.id) {
            if (Greenfoot.mouseClicked(this)) {
                AI.toggleAI(parentBorder);
                updateImage();
            }
        }
    }
    
    public void updateImage() 
    {
        if (player.id == parentBorder.parentTerritory.owner.id) {
            if (toggleVal == 0) {
                this.getImage().clear();
            } else if (toggleVal == 1) {
                this.setImage("ArrowD.png");
                setAngle();
            } else if (toggleVal == 2) {
                this.setImage("ArrowU.png");
                setAngle();
            } else if (toggleVal == 3) {
                /*if (this.parentBorder.inConflict) {
                    this.setImage(new GreenfootImage(Integer.toString(20), 20, Color.BLACK, Color.WHITE));
                    this.setRotation(0);
                    this.getImage().setTransparency(255);
                else {
                */    this.getImage().setTransparency(0);
                //}
            }
        }
        else {
            this.getImage().setTransparency(0);
        }
    }
    
    private void setAngle() {
        //this.setRotation(0);
        
        for (Border bord : parentBorder.parentTerritory.getBorders()) {
            if (bord != null) {
                //System.out.println("Rotating 60");
                this.turn(60);
            }
        }
    }
    
    public void toggleToggle() {
        //System.out.println("Toggling from val " + toggleVal);
        Toggle otherToggle = parentBorder.otherBorder.toggle;
        // TODO: Post alpha: add conditionals to verify that it's an okay move (use AI toggle method?)
        if (toggleVal == 1) {
            toggleVal = 2;
            otherToggle.setToggleVal(1);
        }
        else if (toggleVal == 2) {
            toggleVal = 1;
            otherToggle.setToggleVal(2);
        }
        updateImage();
    }
    
    public int getToggleVal() {
        return toggleVal;
    }
    
    public void setToggleVal(int newVal) {
        toggleVal = newVal;
        updateImage();
    }
    
}
