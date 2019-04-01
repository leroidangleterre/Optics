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
public class Laser extends OpticElement {

    float width, height;
    boolean status;

    public Laser() {
        super();
        width = 4f;
        height = 2f;
        polygon = new FloatPolygon();
        polygon.addPoint(-width / 2, height / 2);
        polygon.addPoint(-width / 2, -height / 2);
        polygon.addPoint(width / 2 - height / 2, -height / 2);
        polygon.addPoint(width / 2, 0);
        polygon.addPoint(width / 2 - height / 2, height / 2);
        color = Color.orange;
        status = false;
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
        this.width = toCopy.width;
        this.height = toCopy.height;
        this.status = toCopy.status;
    }

    public OpticElement clone() {
        Laser copy = new Laser(this);
        return copy;
    }

    @Override
    public boolean containsPoint(float xPoint, float yPoint) {

        // Compute the coordinates of the point in the reference of the mirror.
        float xConv = (float) ((xPoint - this.x) * Math.cos(-rotation) - (yPoint - this.y) * Math.sin(-rotation));
        float yConv = (float) ((xPoint - this.x) * Math.sin(-rotation) + (yPoint - this.y) * Math.cos(-rotation));

        // Point must be inside the triangle defined by the two diagonals of the laser emitting point.
        boolean inCorner = yConv < -xConv + width / 2 && yConv >= xConv - width / 2;

        // Point must be inside the rectangle.
        boolean inRectangle = xConv >= -width / 2 && xConv <= width / 2 && yConv >= -height / 2 && yConv <= height / 2;

        return inCorner && inRectangle;
    }

    public Photon emit() {
        if (status) {
//        System.out.println("Laser.emit()");
            float xPhoton = (float) (width / 2 * Math.cos(rotation)) + this.x;
            float yPhoton = (float) (width / 2 * Math.sin(rotation)) + this.y;
            float vX = (float) (width / 2 * Math.cos(rotation));
            float vY = (float) (width / 2 * Math.sin(rotation));
            return new Photon(xPhoton, yPhoton, vX, vY);
        } else {
            return null;
        }
    }

    /**
     * Absorb any colliding photon.
     *
     * @param p
     * @return
     */
    @Override
    public Photon interactWithPhoton(Photon p) {
        if (this.containsPoint(p.getX(), p.getY())) {
            return null;
        } else {
            return p;
        }
    }

    public void activate(boolean active) {
        this.status = active;
    }

    public void toggle() {
        this.status = !this.status;
    }

    @Override
    public String toString() {
        return "Laser " + x + " " + y + " " + rotation + " " + width + " " + height + " " + status;
    }

}
