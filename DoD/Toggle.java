import greenfoot.*;

/**
 * Write a description of class Toggle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Toggle extends Actor
{
    int toggleVal = 2;
    Border parentBorder;
    
    public Toggle(Border parentBorder) {
        this.parentBorder = parentBorder;
        boolean done = false;

        //System.out.println("Toggle:");
        
        for (Border bord : parentBorder.parentTerritory.getBorders()) {
            if (bord != null) {
                //System.out.println("Rotating 60");
                this.turn(60);
            }
        }
        
    }
    
    /**
     * Act - do whatever the Toggle wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (Greenfoot.mouseClicked(this)) {
            this.toggleToggle();
        }
        if (toggleVal == 0)
            this.getImage().clear();
        else if (toggleVal == 1)
            this.setImage("ArrowD.png");
        else if (toggleVal == 2)
            this.setImage("ArrowU.png");
        else if (toggleVal == 3)
            this.getImage().clear();
            
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
