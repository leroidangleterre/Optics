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
        height = 1f;
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

    @Override
    public Photon interactWithPhoton(Photon p) {
        float xP = p.getX();
        float yP = p.getY();
        float vxP = p.getVx();
        float vyP = p.getVy();
        if (this.containsPoint(p.getX(), p.getY())) {

            float c = (float) Math.cos(rotation);
            float s = (float) Math.sin(rotation);

            // Compute the coordinates of the point in the reference of the mirror.
            float xConv = (float) ((xP - this.x) * c + (yP - this.y) * s);
            float yConv = (float) (-(xP - this.x) * s + (yP - this.y) * c);

            float vxConv = (float) (vxP * c + vyP * s);
            float vyConv = (float) (-vxP * s + vyP * c);

            // Reflection
            if (xConv > 0) {
                if (yConv > xConv + height / 2 - width / 2) {
                    // N
                    if (vyConv < 0) {
                        vyConv = -vyConv;
                    }
                } else if (yConv < -xConv - height / 2 + width / 2) {
                    // S
                    if (vyConv > 0) {
                        vyConv = -vyConv;
                    }
                } else {
                    // E
                    if (vxConv < 0) {
                        vxConv = -vxConv;
                    }
                }
            } else {
                if (yConv > -xConv + height / 2 - width / 2) {
                    // N
                    if (vyConv < 0) {
                        vyConv = -vyConv;
                    }
                } else if (yConv < xConv - height / 2 + width / 2) {
                    // S
                    if (vyConv > 0) {
                        vyConv = -vyConv;
                    }
                } else {
                    // W
                    if (vxConv > 0) {
                        vxConv = -vxConv;
                    }
                }
            }

            float vxReconv = vxConv * c - vyConv * s;
            float vyReconv = vxConv * s + vyConv * c;
            p.setSpeed(vxReconv, vyReconv);
        }
        return p;
    }
}
