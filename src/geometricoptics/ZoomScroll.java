/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.util.ArrayList;
import javax.swing.JComponent;

/**
 *
 * @author arthurmanoha
 *
 * This class represents a zoom level and a position of scroll. It is used by
 * the graphic panel to show a specific part of the 2d environment.
 */
public class ZoomScroll{

    private float zoom, x0, y0;
    private ArrayList<JComponent> listeners;

    public ZoomScroll(){
        zoom = 1;
        x0 = 0;
        y0 = 0;
        listeners = new ArrayList<>();
    }

    public void setX(float x){
        x0 = x;
        triggerListeners();
    }

    public void setY(float y){
        y0 = y;
        triggerListeners();
    }

    public void setZoom(float z){
        zoom = z;
        triggerListeners();
    }

    public float getX(){
        return x0;
    }

    public float getY(){
        return y0;
    }

    public float getZoom(){
        return zoom;
    }

    public void increaseX(float dx){
        setX(x0 + dx / zoom);
    }

    public void increaseY(float dy){
        setY(y0 += dy / zoom);
    }

    public void increaseZoom(float dZoom){
        setZoom(zoom * dZoom);
    }

    public void addListener(JComponent component){
        listeners.add(component);
    }

    public void triggerListeners(){
        for(JComponent component : listeners){
            component.repaint();
        }
    }

    @Override
    public String toString(){
        return "ZoomScroll  x = " + x0 + ", y = " + y0 + ", zoom = " + zoom;
    }
}
