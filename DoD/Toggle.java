import greenfoot.*;

/**
 * Write a description of class Toggle here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Toggle extends Actor
{
    int toggleVal = 1;
    Border parentBorder;
    
    public Toggle(Border parentBorder) {
        this.parentBorder = parentBorder;
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
            this.setImage("ArrowU.png");
        else if (toggleVal == 2)
            this.setImage("ArrowD.png");
    }    
    
    public void toggleToggle() {
        Toggle otherToggle = parentBorder.otherBorder.toggle;
        // TODO: Post alpha: add conditionals to verify that it's an okay move
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
