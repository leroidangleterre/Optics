/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 *
 * @author arthurmanoha
 */
public class Laser extends OpticElement {

    public Laser() {
        super();
        size = 10;
        polygon = new FloatPolygon();
        polygon.addPoint(2, 0);
        polygon.addPoint(1, 1);
        polygon.addPoint(-1, 1);
        polygon.addPoint(-1, -1);
        polygon.addPoint(1, -1);
        color = Color.orange;
    }

    public Laser(float x, float y) {
        this();
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor.
     *
     * @param toCopy the existing laser that we want to duplicate.
     */
    public Laser(Laser toCopy) {

        this.polygon = toCopy.polygon.clone();
        this.color = toCopy.color;
        this.x = toCopy.x;
        this.y = toCopy.y;
        this.rotation = toCopy.rotation;
    }

    public OpticElement clone() {
        Laser copy = new Laser(this);
        return copy;
    }

    /**
     * This method tells if a given set of coordinates is contained in the
     * element.
     *
     * @param xPixel
     * @param yPixel
     * @return
     */
    @Override
    public boolean containsPixel(int xPixel, int yPixel) {

        if (super.containsPixel(xPixel, yPixel)) {
            System.out.println("contains pixel.");
            return true;
        } else {
            System.out.println("does NOT contain pixel.");
            return false;
        }
    }

    @Override
    public boolean containsPoint(float xPoint, float yPoint) {

        float xMin = x - size / 2;
        float xMax = x + size / 2;
        float yMin = y - size / 2;
        float yMax = y + size / 2;

        boolean result = (xMin <= xPoint) && (xPoint <= xMax) && (yMin <= yPoint) && (yPoint <= yMax);

        return result;
    }
}
