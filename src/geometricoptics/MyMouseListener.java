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

    private OpticsPanel opticsPanel;

    public MyMouseListener(OpticsPanel panel) {
        opticsPanel = null;
        opticsPanel = panel;
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
                break;
            case 2:
                // Start scrolling
                opticsPanel.setCurrentScroll(true);
                break;
            case 3:
                opticsPanel.receiveRightClick(e.getX(), e.getY());
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        opticsPanel.setCurrentScroll(false);
        switch (e.getButton()) {
            case 1:
                opticsPanel.receiveLeftUnclick(e.getX(), e.getY());
                break;
            case 3:
                opticsPanel.receiveRightUnclick(e.getX(), e.getY());
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
        opticsPanel.receiveMouseMoved(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        opticsPanel.receiveMouseMoved(e.getX(), e.getY());
    }

    public void setOpticsPanel(OpticsPanel p) {
        this.opticsPanel = p;
    }
}
