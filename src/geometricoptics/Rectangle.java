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
 * @author arthurmanoha Simple rectangle that can be drawn on the screen.
 */
public class Rectangle{

    private float xCenter, yCenter;
    private float width, height;
    private Color color;

    public Rectangle(){
        xCenter = 0;
        yCenter = 0;
        width = 1;
        height = 1;
        color = Color.red;
    }

    public Rectangle(float x, float y, float w, float h){
        xCenter = x;
        yCenter = y;
        width = w;
        height = h;
    }

    public void paint(Graphics g, float x0, float y0, float zoom){
        g.setColor(color);
        g.fillRect((int) ((xCenter - width / 2) * zoom + x0),
                (int) ((yCenter + height / 2) * zoom + y0),
                (int) (width * zoom),
                (int) (height * zoom));
    }
}
