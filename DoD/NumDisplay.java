import greenfoot.*;
import java.awt.Color;

/**
 * Write a description of class NumDisplay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class NumDisplay extends Actor
{
    Border parentBorder;
    private int displayNum = 0;
    private int lastDisplayNum = 0;

    public NumDisplay(Border parentBorder) {
        this.parentBorder = parentBorder;
        this.getImage().clear();
    }

    public void act() 
    {
        if (displayNum != lastDisplayNum) {
            if (displayNum !=0) {
                setImage(new GreenfootImage(Integer.toString(displayNum), 15, parentBorder.getOwner().fgColor, parentBorder.getOwner().bgColor));    
            } else {
                this.getImage().clear();
            }
            lastDisplayNum = displayNum;
        }
        
    }
    
    public void setDisplayNum(int newNum) {
        this.displayNum = newNum;
    }
    
}
