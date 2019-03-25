/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Color;

/**
 *
 * @author arthurmanoha
 */
public class Mirror extends OpticElement {

    float width, height;

    public Mirror() {
        super();
        width = 1f;
        height = 3f;
        polygon.addPoint(width / 2, height / 2);
        polygon.addPoint(-width / 2, height / 2);
        polygon.addPoint(-width / 2, -height / 2);
        polygon.addPoint(width / 2, -height / 2);
        color = Color.red;
    }

    public Mirror(float x, float y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Mirror(Mirror toCopy) {

        this.polygon = toCopy.polygon.clone();
        this.color = toCopy.color;
        this.x = toCopy.x;
        this.y = toCopy.y;
        this.rotation = toCopy.rotation;
    }

    public OpticElement clone() {
        Mirror copy = new Mirror(this);
        return copy;
    }

    /**
     * This method tells if a given set of coordinates is contained in the
     * element.
     *
     * @param xPoint
     * @param yPoint
     * @return
     */
    @Override
    public boolean containsPoint(float xPoint, float yPoint) {

        // Compute the coordinates of the point in the reference of the mirror.
        float xConv = (float) ((xPoint - this.x) * Math.cos(-rotation) - (yPoint - this.y) * Math.sin(-rotation));
        float yConv = (float) ((xPoint - this.x) * Math.sin(-rotation) + (yPoint - this.y) * Math.cos(-rotation));

        return xConv >= -width / 2 && xConv <= width / 2 && yConv >= -height / 2 && yConv <= height / 2;
    }
}
