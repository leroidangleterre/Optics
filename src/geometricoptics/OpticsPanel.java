/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;

/**
 *
 * @author arthurmanoha
 *
 * This panel is used to draw the optical elements such as lenses, as well as
 * the light rays. The user can scroll and zoom in the image.
 */
public class OpticsPanel extends JPanel {

    // The elements displayed
    private World world;

    // Scroll and zoom
    private ZoomScroll zoomScroll;

    private boolean isCurrentlyScrolling;

    private int lastX, lastY;

    float zoomFact = 1.03f;

    public OpticsPanel(World w) {
        super();

        world = w;

        Dimension preferredSize = new Dimension(1000, 700);
        this.setPreferredSize(preferredSize);
        this.zoomScroll = new ZoomScroll();
        this.zoomScroll.addListener(this);
        zoomScroll.setX(500);
        zoomScroll.setY(350);
        zoomScroll.setZoom(50f);

        world.addListener(this);

        OpticsPanelMouseAdapter mouseAdapter = new OpticsPanelMouseAdapter();

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        this.addMouseWheelListener(mouseAdapter);
        this.addKeyListener(new MyKeyListener(w));

        isCurrentlyScrolling = false;
        lastX = -1;
        lastY = -1;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        world.paint(g, zoomScroll.getX(), zoomScroll.getY(), zoomScroll.getZoom());

    }

    public void scroll(float dx, float dy) {
        zoomScroll.scroll(dx, dy);
    }

    public void setCurrentScroll(boolean b) {
        isCurrentlyScrolling = b;
    }

    public boolean isCurrentlyScrolling() {
        return isCurrentlyScrolling;
    }

    public void receiveLeftClick(int xClick, int yClick) {

        int height = getBounds().height;

        float xClickInWorld = (xClick - zoomScroll.getX()) / zoomScroll.getZoom();
        float yClickInWorld = (height - yClick - zoomScroll.getY()) / zoomScroll.getZoom();
        world.receiveLeftClick(xClickInWorld, yClickInWorld);
    }

    public void receiveRightClick(int xClick, int yClick) {

        int height = getBounds().height;

        float xClickInWorld = (xClick - zoomScroll.getX()) / zoomScroll.getZoom();
        float yClickInWorld = (height - yClick - zoomScroll.getY()) / zoomScroll.getZoom();
        world.receiveRightClick(xClickInWorld, yClickInWorld);
    }

    public void receiveLeftUnclick(int xClick, int yClick) {

        int height = getBounds().height;
        float xUnclickInWorld = (xClick - zoomScroll.getX()) / zoomScroll.getZoom();
        float yUnclickInWorld = (height - yClick - zoomScroll.getY()) / zoomScroll.getZoom();
        world.receiveLeftUnclick(xUnclickInWorld, yUnclickInWorld);
    }

    public void receiveRightUnclick(int xClick, int yClick) {

        int height = getBounds().height;
        float xUnclickInWorld = (xClick - zoomScroll.getX()) / zoomScroll.getZoom();
        float yUnclickInWorld = (height - yClick - zoomScroll.getY()) / zoomScroll.getZoom();
        world.receiveRightUnclick(xUnclickInWorld, yUnclickInWorld);
    }

    public void receiveMouseMove(int x, int y) {

        if (isCurrentlyScrolling) {
            int dx = x - lastX;
            int dy = -(y - lastY);
            zoomScroll.scroll(dx, dy);
        } else {
            int height = getBounds().height;
            float xMouseInWorld = (x - zoomScroll.getX()) / zoomScroll.getZoom();
            float yMouseInWorld = (height - y - zoomScroll.getY()) / zoomScroll.getZoom();
            world.receiveMouseMove(xMouseInWorld, yMouseInWorld);
        }
        lastX = x;
        lastY = y;
    }

    public void receiveMouseDragged(int x, int y) {

        int height = getBounds().height;
        float xMouseInWorld = (x - zoomScroll.getX()) / zoomScroll.getZoom();
        float yMouseInWorld = (height - y - zoomScroll.getY()) / zoomScroll.getZoom();

        if (isCurrentlyScrolling()) {
            zoomScroll.scroll(x - lastX, y - lastY);
        } else {
            world.receiveMouseMove(xMouseInWorld, yMouseInWorld);
        }
        lastX = x;
        lastY = y;
    }

    public void receiveMouseWheelMoved(int x, int y, int nbRotationSteps) {

        int height = getBounds().height;
        float fact = (nbRotationSteps > 0) ? (1 / zoomFact) : zoomFact;

        zoomScroll.setX(x + fact * (zoomScroll.getX() - x));
        zoomScroll.setY(height - y - fact * (height - zoomScroll.getY() - y));
        zoomScroll.setZoom(fact * zoomScroll.getZoom());
    }

    private class OpticsPanelMouseAdapter extends MouseAdapter {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int rotation = e.getWheelRotation(); // +1: zoom out; -1: zoom in.
            receiveMouseWheelMoved(e.getX(), e.getY(), rotation);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            receiveMouseMove(e.getX(), e.getY());
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            receiveMouseMove(e.getX(), e.getY());
        }

        @Override
        public void mousePressed(MouseEvent e) {
            switch (e.getButton()) {
                case 1:
                    // Place an object, or start selecting things
                    receiveLeftClick(e.getX(), e.getY());
                    // leftClickActive = true;
                    break;
                case 2:
                    // Start scrolling
                    setCurrentScroll(true);
                    break;
                case 3:
                    receiveRightClick(e.getX(), e.getY());
                    //  rightClickActive = true;
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setCurrentScroll(false);
            switch (e.getButton()) {
                case 1:
                    receiveLeftUnclick(e.getX(), e.getY());
                    // leftClickActive = false;
                    break;
                case 3:
                    receiveRightUnclick(e.getX(), e.getY());
                    // rightClickActive = false;
                    break;
                default:
                    break;
            }
        }
    }
}
