/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Polygon;

/**
 *
 * @author arthurmanoha
 */
public class FloatPolygon {

    public float xTab[]; // TODO: make private
    public float yTab[];
    private int xTabApp[];
    private int yTabApp[];
    private int nbPoints;
    private int nbPointsMax;
    private Polygon apparentPolygon;

    public FloatPolygon() {
        nbPointsMax = 20; // TODO: auto-adapt the amount of points for complex polygons.
        nbPoints = 0;
        xTab = new float[nbPointsMax];
        xTabApp = new int[nbPointsMax];
        yTab = new float[nbPointsMax];
        yTabApp = new int[nbPointsMax];
        apparentPolygon = new Polygon();
    }

    public FloatPolygon clone() {
        FloatPolygon copy = new FloatPolygon();
        copy.nbPoints = this.nbPoints;
        copy.nbPointsMax = this.nbPointsMax;

        for (int i = 0; i < this.xTab.length; i++) {
            copy.xTab[i] = this.xTab[i];
            copy.yTab[i] = this.yTab[i];
        }
        return copy;
    }

    public void addPoint(float x, float y) {
        xTab[nbPoints] = x;
        yTab[nbPoints] = y;
        nbPoints++;
    }

    public int getNbPoints() {
        return nbPoints;
    }

    /**
     * Get the polygon transformed by the given translation and rotation.
     *
     * @param x0
     * @param y0
     * @param zoom
     * @param angle
     * @return
     */
    public void computeApparentPolygon(float x0, float y0, float zoom, float angle) {
        // Apply the rotation on all of these points.
        for (int i = 0; i < nbPoints; i++) {
            float newX = (float) (xTab[i] * Math.cos(angle) - yTab[i] * Math.sin(angle));
            float newY = (float) (xTab[i] * Math.sin(angle) + yTab[i] * Math.cos(angle));
            this.xTabApp[i] = (int) newX;
            this.yTabApp[i] = (int) newY;
        }
        // Apply the translation and zoom
        apparentPolygon.reset();
        for (int i = 0; i < nbPoints; i++) {
            xTabApp[i] = (int) (xTabApp[i] * zoom + x0);
            yTabApp[i] = (int) (yTabApp[i] * zoom + y0);
            apparentPolygon.addPoint((int) (xTabApp[i]), (int) (yTabApp[i]));
        }
    }

    public int[] getXTabApp() {
        return xTabApp;
    }

    public int[] getYTabApp() {
        return yTabApp;
    }

    public Polygon getPolygon() {
        return apparentPolygon;
    }

    public String toString() {
        String res = "Polygon{";
        res += " <" + apparentPolygon.npoints + "> ";
        for (int i = 0; i < apparentPolygon.npoints; i++) {
            res += "(" + apparentPolygon.xpoints[i] + ", " + apparentPolygon.ypoints[i] + "); ";
        }
        res += " }";
        return res;
    }

    /**
     * Decide whether the apparent polygon contains a given pixel on screen.
     *
     * @param xPoint
     * @param yPoint
     * @return
     */
    public boolean containsPixel(int xPoint, int yPoint) {
        boolean result = apparentPolygon.contains(xPoint, yPoint);
        return result;
    }
}
