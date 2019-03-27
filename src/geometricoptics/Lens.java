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
public class Lens extends OpticElement {

    public Lens() {
        super();
        size = 10;
        polygon.addPoint(0, 3);
        polygon.addPoint(1, 1);
        polygon.addPoint(1, -1);
        polygon.addPoint(0, -3);
        polygon.addPoint(-1, -1);
        polygon.addPoint(-1, 1);
        color = Color.blue;
    }

    public Lens(float x, float y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Lens(Lens toCopy) {

        this.polygon = toCopy.polygon.clone();
        this.color = toCopy.color;
        this.x = toCopy.x;
        this.y = toCopy.y;
        this.rotation = toCopy.rotation;
    }

    public OpticElement clone() {
        Lens copy = new Lens(this);
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

        float xMin = x - size / 2;
        float xMax = x + size / 2;
        float yMin = y - size / 2;
        float yMax = y + size / 2;

        boolean result = (xMin <= xPoint) && (xPoint <= xMax) && (yMin <= yPoint) && (yPoint <= yMax);

        return result;
    }

    @Override
    public Photon interactWithPhoton(Photon p) {
        return p;
    }
}
