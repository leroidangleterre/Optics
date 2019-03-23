/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author arthurmanoha
 */
public class MyMouseListener implements MouseListener, MouseMotionListener {

    private World world;
    private ZoomScroll zs;
//    private int lastX, lastY;
    private OpticsPanel opticsPanel;

//    private float xMouse, yMouse;
//    private float xLeftClick, yLeftClick, xRightClick, yRightClick;
//    private boolean leftClickActive, rightClickActive;
    public MyMouseListener(ZoomScroll zsParam, World w, OpticsPanel panel) {
        zs = zsParam;
        world = w;
        opticsPanel = null;

//        xLeftClick = -1;
//        yLeftClick = -1;
//        xRightClick = -1;
//        yRightClick = -1;
//        leftClickActive = false;
//        rightClickActive = false;
        opticsPanel = panel;
    }

    public MyMouseListener(ZoomScroll zsParam, World w) {
        this(zsParam, w, null);
    }

    public MyMouseListener(ZoomScroll zsParam) {
        this(zsParam, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

        switch (e.getButton()) {
            case 1:
                // Place an object, or start selecting things
                opticsPanel.receiveLeftClick(e.getX(), e.getY());
//                leftClickActive = true;
                break;
            case 2:
                // Start scrolling
                opticsPanel.setCurrentScroll(true);
                break;
            case 3:
                opticsPanel.receiveRightClick(e.getX(), e.getY());
//                rightClickActive = true;
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        opticsPanel.setCurrentScroll(false);
        switch (e.getButton()) {
            case 1:
                opticsPanel.receiveLeftUnclick(e.getX(), e.getY());
//                leftClickActive = false;
                break;
            case 3:
                opticsPanel.receiveRightUnclick(e.getX(), e.getY());
//                rightClickActive = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        opticsPanel.receiveMouseMove(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        opticsPanel.receiveMouseMove(e.getX(), e.getY());
    }

    public void setOpticsPanel(OpticsPanel p) {
        this.opticsPanel = p;
    }
}
