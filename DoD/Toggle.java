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
    
    public Toggle(Border parentBorder) {
        this.parentBorder = parentBorder;
        boolean done = false;
        
        setAngle();
        
    }
    
    public void act() 
    {
        if (Greenfoot.mouseClicked(this)) {
            //this.toggleToggle();
            AI.toggleAI(parentBorder);
        }
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
    }
    
    public int getToggleVal() {
        return toggleVal;
    }
    
    public void setToggleVal(int newVal) {
        toggleVal = newVal;
    }
    
}
