/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author arthurmanoha
 */
public class Photon {

    private float x, y;
    private float vx, vy;
    float radius;

    public Photon(float xParam, float yParam, float vxParam, float vyParam) {
        x = xParam;
        y = yParam;
        vx = vxParam;
        vy = vyParam;
        radius = 0.1f;
    }

    public Photon(float xParam, float yParam) {
        this(xParam, yParam, 0, 0);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setSpeed(float newVx, float newVy) {
        vx = newVx;
        vy = newVy;
    }

    public float getVx() {
        return vx;
    }

    public float getVy() {
        return vy;
    }

    public float getDistance() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Travel at the photon's speed for the given amount of time.
     *
     * @param dt
     */
    public void travel(float dt) {
        x += dt * vx;
        y += dt * vy;
    }

    public void paint(Graphics g, float x0, float y0, float zoom) {
//        System.out.println("Photon.paint " + x + ", " + y);
        g.setColor(Color.green);

        int xApp = (int) (x * zoom + x0);
        int yApp = g.getClipBounds().height - (int) (y * zoom + y0);
        int appRadius = (int) (radius * zoom);
        g.fillOval(xApp - appRadius, yApp - appRadius, 2 * appRadius, 2 * appRadius);
    }

}
