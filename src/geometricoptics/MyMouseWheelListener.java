/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author arthurmanoha
 */
public class MyMouseWheelListener implements MouseWheelListener{

    private final ZoomScroll zs;
    float zoomFact = 1.03f;

    public MyMouseWheelListener(ZoomScroll zsParam){
        zs = zsParam;
    }

    // The zoom and scroll are modified when the mouse wheel is used.
    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        int rotation = e.getWheelRotation(); // +1: zoom out; -1: zoom in.
        float fact = (rotation > 0) ? (1 / zoomFact) : zoomFact;

        zs.increaseZoom(fact);

        zs.setX(-(fact * (e.getX() - zs.getX()) - e.getX()));
        zs.setY(-(fact * (e.getY() - zs.getY()) - e.getY()));
    }

}
