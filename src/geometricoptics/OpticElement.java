/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Graphics;

/**
 *
 * @author arthurmanoha
 *
 * Generic class that models all the elements manipulated by the user: mirrors,
 * lasers, lenses...
 */
public abstract class OpticElement{

    protected float x, y, rotation;
    protected float size;

    protected boolean isSelected;

    private static int nbElements = 0;
    protected int elemID;

    public OpticElement(){
        x = 0;
        y = 0;
        size = 1;
        rotation = 0;
        isSelected = false;
        elemID = nbElements;
        nbElements++;
    }

    public void setSelected(boolean param){
        isSelected = param;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void setPosition(float xParam, float yParam){
        x = xParam;
        y = yParam;
    }

    public void translate(float dx, float dy){
        x += dx;
        y += dy;
    }

    public abstract void paint(Graphics g, float x0, float y0, float zoom);

    /**
     * Tells if the object is enclosed in the rectangular region between the
     * points (x1, y1) and (x2, y2). Point 1 must be to the left and lower that
     * point 2.
     */
    public boolean isContainedInRegion(float x1, float y1, float x2, float y2){
        boolean result = (x1 <= x) && (x <= x2) && (y1 < y) && (y < y2);
        return result;
    }

    /**
     * This method tells if a given set of coordinates is contained in the
     * element. It will have to be implemented by the elements.
     *
     * @param x
     * @param y
     * @return
     */
    public abstract boolean containsPoint(float x, float y);

    @Override
    public abstract OpticElement clone();

    public String toString(){
        return "OpticElement at (" + x + ", " + y + ");";
    }
}
