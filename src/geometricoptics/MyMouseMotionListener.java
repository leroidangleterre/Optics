/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author arthurmanoha
 */
public class MyMouseMotionListener implements MouseMotionListener{

    private ZoomScroll zs;
    private int lastX, lastY;

    public MyMouseMotionListener(ZoomScroll zsParam){
        zs = zsParam;
        lastX = -1;
        lastY = -1;
    }

    @Override
    public void mouseDragged(MouseEvent e){
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;

        zs.increaseX(dx);
        zs.increaseY(dy);

        lastX = e.getX();
        lastY = e.getY();

    }

    @Override
    public void mouseMoved(MouseEvent e){
        lastX = e.getX();
        lastY = e.getY();

    }

}
