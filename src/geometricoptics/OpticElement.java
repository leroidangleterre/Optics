/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 *
 * @author arthurmanoha
 *
 * Generic class that models all the elements manipulated by the user: mirrors,
 * lasers, lenses...
 */
public abstract class OpticElement implements Cloneable {

    protected float x, y, rotation;
    protected float size;

    protected boolean isSelected;

    private static int nbElements = 0;
    protected int elemID;

    protected FloatPolygon polygon;

    protected Color color;

    public OpticElement() {
        x = 0;
        y = 0;
        size = 1;
        rotation = 0;
        isSelected = false;
        elemID = nbElements;
        nbElements++;
        color = Color.black;
        polygon = new FloatPolygon();
    }

    public OpticElement(OpticElement toCopy) {
        this();
        this.x = toCopy.x;
        this.y = toCopy.y;
        this.size = toCopy.size;
        this.rotation = toCopy.rotation;
        for (int i = 0; i < polygon.getNbPoints(); i++) {
            this.polygon.addPoint(toCopy.polygon.getXTabApp()[i],
                    toCopy.polygon.getYTabApp()[i]);
        }
    }

    public abstract OpticElement clone();

    public void setSelected(boolean param) {
        isSelected = param;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setPosition(float xParam, float yParam) {
        x = xParam;
        y = yParam;
    }

    public void translate(float dx, float dy) {
        x += dx;
        y += dy;
    }

    public void paint(Graphics g, float x0, float y0, float zoom) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(this.color);

        Polygon appPoly = computeAppPolygon(x0, y0, zoom);
        g.fillPolygon(appPoly);

        if (isSelected) {
            g.setColor(Color.BLACK);
            g.drawPolygon(appPoly);
        }
    }

    private Polygon computeAppPolygon(float x0, float y0, float zoom) {
        Polygon appPoly = new Polygon();
        for (int i = 0; i < polygon.getNbPoints(); i++) {
            float x = polygon.xTab[i];
            float y = polygon.yTab[i];

            float c = (float) Math.cos(rotation);
            float s = (float) Math.sin(rotation);

            // rotate
            float xRotation = x * c - y * s;
            float yRotation = y * c + x * s;

            // translate
            float xReal = xRotation + this.x;
            float yReal = yRotation + this.y;

            int xApp = (int) (xReal * zoom + x0);
            int yApp = (int) (yReal * zoom + y0);
            appPoly.addPoint(xApp, yApp);
        }
        return appPoly;
    }

    /**
     * Tells if the object is enclosed in the rectangular region between the
     * points (x1, y1) and (x2, y2). Point 1 must be to the left and lower that
     * point 2.
     */
    public boolean isContainedInRegion(float x1, float y1, float x2, float y2) {
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
    public boolean containsPoint(float x, float y) {
        boolean result = false;

        return result;
    }

    public boolean containsPixel(int x, int y) {
        return this.polygon.containsPixel(x, y);
    }

    @Override
    public String toString() {
        return "OpticElement at (" + x + ", " + y + ");";
    }

    /**
     * Set the orientation of the object.
     *
     * @param angle the new rotation.
     */
    public void setRotation(float angle) {
        this.rotation = angle;
    }

    /**
     * Change the rotation of the object
     *
     * @param dAngle the angle increment
     */
    public void rotate(float dAngle) {
        this.rotation += dAngle;
    }
}
