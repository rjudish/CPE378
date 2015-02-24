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
        if (toggleVal == 1)
            toggleVal = 2;
        else if (toggleVal == 2)
            toggleVal = 1;
            
    }
    
    public int getVal() {
        return toggleVal;
    }
}
