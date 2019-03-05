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
public class Laser extends OpticElement{

    public Laser(){
        super();
        size = 10;
    }

    public Laser(float x, float y){
        this();
        this.x = x;
        this.y = y;
    }

    public Laser(Laser toCopy){
        super();
        this.size = toCopy.size;
        this.x = toCopy.x;
        this.y = toCopy.y;
    }

    @Override
    public Laser clone(){
        return new Laser(this);
    }

    @Override
    public void paint(Graphics g, float x0, float y0, float zoom){
        g.setColor(Color.red);

        float xApp = this.x * zoom + x0;
        float yApp = this.y * zoom + y0;
        float halfSize = size / 2;

        g.fillRect((int) (xApp - halfSize * zoom),
                (int) (yApp - halfSize * zoom),
                (int) (2 * halfSize * zoom),
                (int) (2 * halfSize * zoom));

        if(this.isSelected()){
            // Add a black lining around the drawing.
            g.setColor(Color.black);
            g.drawRect((int) (xApp - halfSize * zoom),
                    (int) (yApp - halfSize * zoom),
                    (int) (2 * halfSize * zoom),
                    (int) (2 * halfSize * zoom));
        }

    }

    /**
     * This method tells if a given set of coordinates is contained in the
     * element.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean containsPoint(float xPoint, float yPoint){

        float xMin = x - size / 2;
        float xMax = x + size / 2;
        float yMin = y - size / 2;
        float yMax = y + size / 2;

        boolean result = (xMin <= xPoint) && (xPoint <= xMax) && (yMin <= yPoint) && (yPoint <= yMax);

        return result;
    }
}
