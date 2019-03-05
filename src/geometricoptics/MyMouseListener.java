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
public class MyMouseListener implements MouseListener, MouseMotionListener{

    private World world;
    private ZoomScroll zs;
    private int lastX, lastY;
    private OpticsPanel opticsPanel;

    private float xMouse, yMouse;
    private float xLeftClick, yLeftClick, xRightClick, yRightClick;
    private boolean leftClickActive, rightClickActive;

    public MyMouseListener(ZoomScroll zsParam, World w){
        zs = zsParam;
        world = w;
        lastX = -1;
        lastY = -1;
        opticsPanel = null;

        xLeftClick = -1;
        yLeftClick = -1;
        xRightClick = -1;
        yRightClick = -1;
        leftClickActive = false;
        rightClickActive = false;
    }

    public MyMouseListener(ZoomScroll zsParam){
        this(zsParam, null);
    }

    @Override
    public void mouseClicked(MouseEvent e){
    }

    @Override
    public void mousePressed(MouseEvent e){

        float xClickInWorld = (e.getX() - zs.getX()) / zs.getZoom();
        float yClickInWorld = (e.getY() - zs.getY()) / zs.getZoom();

        switch(e.getButton()){
        case 1:
            // Place an object, or start selecting things
            world.receiveLeftClick(xClickInWorld, yClickInWorld);
            leftClickActive = true;
            break;
        case 2:
            // Start scrolling
            opticsPanel.setCurrentScroll(true);
            break;
        case 3:
            if(world.pointIsInSelection(xClickInWorld, yClickInWorld)){
                // Start rotating selection
                world.receiveRightClick(xClickInWorld, yClickInWorld);
            } else{
                // Start scrolling
                opticsPanel.setCurrentScroll(true);
            }
            rightClickActive = true;
            break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        opticsPanel.setCurrentScroll(false);
        float xClickInWorld = (e.getX() - zs.getX()) / zs.getZoom();
        float yClickInWorld = (e.getY() - zs.getY()) / zs.getZoom();
        switch(e.getButton()){
        case 1:
            world.receiveLeftUnclick(xClickInWorld, yClickInWorld);
            leftClickActive = false;
            break;
        case 3:
            world.receiveRightUnclick(xClickInWorld, yClickInWorld);
            rightClickActive = false;
            break;
        default:
            break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e){
    }

    @Override
    public void mouseExited(MouseEvent e){
    }

    @Override
    public void mouseDragged(MouseEvent e){
        float xWorld = (e.getX() - zs.getX()) / zs.getZoom();
        float yWorld = (e.getY() - zs.getY()) / zs.getZoom();
        world.receiveMouseMove(xWorld, yWorld);
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e){
        float xWorld = (e.getX() - zs.getX()) / zs.getZoom();
        float yWorld = (e.getY() - zs.getY()) / zs.getZoom();
        world.receiveMouseMove(xWorld, yWorld);
        lastX = e.getX();
        lastY = e.getY();
    }

    public void setOpticsPanel(OpticsPanel p){
        this.opticsPanel = p;
    }
}
