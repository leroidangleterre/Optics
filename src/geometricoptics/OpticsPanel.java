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
<<<<<<< HEAD
=======
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
>>>>>>> master
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

    float zoomFact = 1.03f;

    private boolean leftClickActive, rightClickActive;
    private int xLeftClick, yLeftClick, xRightClick, yRightClick;
    private float xClickInWorld, yClickInWorld;
    // Latest coordinates of the mouse (in pixels)
    private int lastXMouse, lastYMouse;
    private float xMouseInWorld, yMouseInWorld;
    private boolean isCurrentlySelecting;
    private boolean selectionBeingMoved = false;
    private boolean selectionBeingRotated = false;
    private float xRotationCenter;
    private float yRotationCenter;

    private float xRotationCenterApp;
    private float yRotationCenterApp;

    private float previousAngle, currentAngle;

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

<<<<<<< HEAD
        this.addMouseWheelListener(new MyMouseWheelListener(zoomScroll, w, this));
=======
        OpticsPanelMouseAdapter mouseAdapter = new OpticsPanelMouseAdapter();

        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
        this.addMouseWheelListener(mouseAdapter);
>>>>>>> master
        this.addKeyListener(new MyKeyListener(w));

        isCurrentlyScrolling = false;
        lastXMouse = -1;
        lastYMouse = -1;

        leftClickActive = false;
        rightClickActive = false;
        isCurrentlySelecting = false;
    }

    public void setMouseListener(MyMouseListener list) {
        MyMouseListener mouseListener = new MyMouseListener(this);

        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
    }

    @Override
    public void paintComponent(Graphics g) {

        int graphicsHeight = g.getClipBounds().height;
        float zoom = zoomScroll.getZoom();
        float x0 = zoomScroll.getX();
        float y0 = zoomScroll.getY();

        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        world.paint(g, zoomScroll.getX(), zoomScroll.getY(), zoomScroll.getZoom());

        // Paint the selection rectangle if a selection is being made.
        if (isCurrentlySelecting) {
            int xLeft = Math.min(lastXMouse, xLeftClick);
            int xRight = Math.max(lastXMouse, xLeftClick);
            int yBottom = Math.min(lastYMouse, yLeftClick);
            int yTop = Math.max(lastYMouse, yLeftClick);
            g.setColor(Color.orange);
            g.drawLine((int) xLeft, (int) yBottom, (int) xRight, (int) yBottom);
            g.drawLine((int) xLeft, (int) yTop, (int) xRight, (int) yTop);
            g.drawLine((int) xLeft, (int) yBottom, (int) xLeft, (int) yTop);
            g.drawLine((int) xRight, (int) yBottom, (int) xRight, (int) yTop);
        }

        // If a rotation is ongoing:
        if (selectionBeingRotated) {
            g.setColor(Color.red);
            int radius = 3;
            int xApp = (int) (xRotationCenter * zoom + x0);
            int yApp = (int) (graphicsHeight - (yRotationCenter * zoom + y0));
            g.fillRect(xApp - radius, yApp - radius, 2 * radius, 2 * radius);
        }
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

        xLeftClick = xClick;
        yLeftClick = yClick;
        int height = getBounds().height;

        xClickInWorld = (xClick - zoomScroll.getX()) / zoomScroll.getZoom();
        yClickInWorld = (height - yClick - zoomScroll.getY()) / zoomScroll.getZoom();

        if (world.elementBeingCreated) {
            selectionBeingMoved = false;
            world.makeClonesReal();
            world.elementBeingCreated = false;
        }
        if (world.pointIsInSelectedObject(xClickInWorld, yClickInWorld)) {
            // click on a selected object: start moving all selected objects
            selectionBeingMoved = true;
        } else {
            // Start the selection rectangle.
            isCurrentlySelecting = true;
        }
    }

    public void receiveRightClick(int xClick, int yClick) {

        int height = getBounds().height;

        xClickInWorld = (xClick - zoomScroll.getX()) / zoomScroll.getZoom();
        yClickInWorld = (height - yClick - zoomScroll.getY()) / zoomScroll.getZoom();

        if (world.pointIsInObject(xClickInWorld, yClickInWorld)) {
            if (!world.pointIsInSelectedObject(xClickInWorld, yClickInWorld)) {
                // Select only the object that contains the point.
                world.unselectEverything();
                OpticElement rotatingObject = world.getObjectAt(xClickInWorld, yClickInWorld);
                rotatingObject.setSelected(true);
            }
        }
        selectionBeingRotated = true;
        // Find the coordinates of the center of the rotation
        xRotationCenter = world.findBarycenterOfSelectionX();
        yRotationCenter = world.findBarycenterOfSelectionY();
        xRotationCenterApp = (int) (xRotationCenter * zoomScroll.getZoom() + zoomScroll.getX());
        yRotationCenterApp = (int) (height - (yRotationCenter * zoomScroll.getZoom() + zoomScroll.getY()));

        xRightClick = xClick;
        yRightClick = yClick;

        // Find the initial slope of the line going from the center of rotation to the mouse.
        float dx = xRightClick - xRotationCenterApp;
        float dy = -(yRightClick - yRotationCenterApp);
        if (dx >= 0) {
            previousAngle = (float) Math.atan(dy / dx);
        } else {
            previousAngle = (float) (Math.atan(dy / dx) + Math.PI);
        }
        repaint();
    }

    public void receiveLeftUnclick(int xClick, int yClick) {

        int height = getBounds().height;
        float xUnclickInWorld = (xClick - zoomScroll.getX()) / zoomScroll.getZoom();
        float yUnclickInWorld = (height - yClick - zoomScroll.getY()) / zoomScroll.getZoom();

        if (isCurrentlySelecting) {
            world.unselectEverything();
            world.selectObjectsInRegion(xClickInWorld, yClickInWorld, xUnclickInWorld, yUnclickInWorld);
        }
        isCurrentlySelecting = false;
        selectionBeingMoved = false;

        repaint();
    }

    public void receiveRightUnclick(int xClick, int yClick) {

        selectionBeingRotated = false;
        repaint();
    }

    public void receiveMouseMoved(int x, int y) {
        int dx = (int) (x - lastXMouse);
        int dy = (int) -(y - lastYMouse);

        int height = getBounds().height;

        if (isCurrentlyScrolling) {
            zoomScroll.scroll(dx, dy);
        } else {

            if (selectionBeingMoved) {
                world.translateSelected(dx / zoomScroll.getZoom(), dy / zoomScroll.getZoom());
            }
            if (world.elementBeingCreated) {
                world.translateClones(dx / zoomScroll.getZoom(), dy / zoomScroll.getZoom());
            }

            if (selectionBeingRotated) {

                float dxRotation = x - xRotationCenterApp;
                float dyRotation = -(y - yRotationCenterApp);
                if (dxRotation >= 0) {
                    currentAngle = (float) Math.atan(dyRotation / dxRotation);
                } else {
                    currentAngle = (float) Math.atan(dyRotation / dxRotation) + (float) Math.PI;
                }
                world.rotateAllSelectedObjects(currentAngle - previousAngle, true);
                previousAngle = currentAngle;
            }
        }
        lastXMouse = x;
        lastYMouse = y;
        xMouseInWorld = (x - zoomScroll.getX()) / zoomScroll.getZoom();
        yMouseInWorld = (height - y - zoomScroll.getY()) / zoomScroll.getZoom();
        repaint();
    }

    public void receiveMouseDragged(int x, int y) {
        receiveMouseMoved(x, y);
    }

    public void receiveMouseWheelMoved(int x, int y, int nbRotationSteps) {

        int height = getBounds().height;
        float fact = (nbRotationSteps > 0) ? (1 / zoomFact) : zoomFact;

        zoomScroll.setX(x + fact * (zoomScroll.getX() - x));
        zoomScroll.setY(height - y - fact * (height - zoomScroll.getY() - y));
        zoomScroll.setZoom(fact * zoomScroll.getZoom());
    }

<<<<<<< HEAD
    /**
     * Apply the command given as a parameter. Example: "Lens" or "Laser" to
     * create a lens or a laser.
     *
     * @param command
     */
    public void applyCommand(String command) {
        if (command.startsWith("Create")) {
            // Create a new object
            String objectName = command.substring(7);
            System.out.println("OpticsPanel is creating an object at world coordinates (" + xMouseInWorld + ", " + yMouseInWorld + ");");
            if (objectName.equals("lens")) {
                world.create(new Lens(xMouseInWorld, yMouseInWorld));
            } else if (objectName.equals("laser")) {
                world.create(new Laser(xMouseInWorld, yMouseInWorld));
            } else if (objectName.equals("mirror")) {
                world.create(new Mirror(xMouseInWorld, yMouseInWorld));
=======
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
>>>>>>> master
            }
        }
    }
}
