/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;

/**
 *
 * @author arthurmanoha
 *
 * This class encapsulates all the physical elements that are simulated: lasers,
 * lenses, mirrors... It is drawn in an OpticsPanel.
 */
public class World {

    private ArrayList<OpticElement> elements;
    private ArrayList<OpticElement> clones; // Clones or new elements

    private boolean selectionBeingMoved = false;
    private boolean selectionBeingRotated = false;
    private boolean singleObjectBeingRotated = false;
    private OpticElement rotatingObject = null;
    private float xRotationCenter;
    private float yRotationCenter;
    private float initialAngle, currentAngle;

    private ArrayList<JComponent> listeners;

    private boolean isCurrentlySelecting;

    private boolean isAllSelected;
    private boolean elementBeingCreated;

    private boolean leftClickActive, rightClickActive;
    private float xLeftClick, yLeftClick, xRightClick, yRightClick;
    private float lastXMouse, lastYMouse;

    public World() {
        elements = new ArrayList<>();
        listeners = new ArrayList<>();
        isAllSelected = false;
        isCurrentlySelecting = false;
        elementBeingCreated = false;

        leftClickActive = false;
        rightClickActive = false;

        clones = new ArrayList<>();
    }

    public void addListener(JComponent component) {
        listeners.add(component);
    }

    public void triggerListeners() {
        for (JComponent component : listeners) {
            component.repaint();
        }
    }

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
            OpticElement newOpticElement = null;
            if (objectName.equals("lens")) {
                newOpticElement = new Lens();
            } else if (objectName.equals("laser")) {
                newOpticElement = new Laser();
            }
            if (newOpticElement != null) {
                clones.add(newOpticElement);
                newOpticElement.setSelected(true);
                newOpticElement.x = lastXMouse;
                newOpticElement.y = lastYMouse;
                elementBeingCreated = true;
            }
        }
        triggerListeners();
    }

    public void moveSelected(float dx, float dy) {
        for (OpticElement element : elements) {
            if (element.isSelected()) {
                element.translate(dx, dy);
            }
        }
    }

    public void paint(Graphics g, float x0, float y0, float zoom) {

        // Paint the axes
        float axisLength = 10;
        g.setColor(Color.black);
        g.drawLine((int) x0, (int) y0, (int) (x0 + zoom * axisLength), (int) y0);
        g.drawLine((int) x0, (int) y0, (int) x0, (int) (y0 + zoom * axisLength));

        for (OpticElement element : elements) {
            element.paint(g, x0, y0, zoom);
        }
        for (OpticElement element : clones) {
            element.paint(g, x0, y0, zoom);
        }

        // Paint the selection rectangle if a selection is being made.
        if (isCurrentlySelecting) {
            float xLeft = Math.min(lastXMouse, xLeftClick);
            float xRight = Math.max(lastXMouse, xLeftClick);
            float yBottom = Math.min(lastYMouse, yLeftClick);
            float yTop = Math.max(lastYMouse, yLeftClick);

            float xLeftApp = xLeft * zoom + x0;
            float yBottomApp = yBottom * zoom + y0;
            float xRightApp = xRight * zoom + x0;
            float yTopApp = yTop * zoom + y0;

            g.setColor(Color.orange);
            g.drawLine((int) xLeftApp, (int) yBottomApp, (int) xRightApp, (int) yBottomApp);
            g.drawLine((int) xLeftApp, (int) yTopApp, (int) xRightApp, (int) yTopApp);
            g.drawLine((int) xLeftApp, (int) yBottomApp, (int) xLeftApp, (int) yTopApp);
            g.drawLine((int) xRightApp, (int) yBottomApp, (int) xRightApp, (int) yTopApp);
        }

    }

    /* Select everything if not everything is selected yet;
     *  if everything is already selected, then un-select all objects.
     */
    public void toggleSelection() {
        if (isAllSelected) {
            for (OpticElement elem : elements) {
                elem.setSelected(false);
            }
            isAllSelected = false;
        } else {
            for (OpticElement elem : elements) {
                elem.setSelected(true);
            }
            isAllSelected = true;
        }
        triggerListeners();
    }

    /*
     * Remove each selected object; leave all other objects unchanged.
     */
    public void deleteSelected() {

        Iterator<OpticElement> iter = elements.iterator();
        while (iter.hasNext()) {
            OpticElement elem = iter.next();
            // Remove the element if it is selected.
            if (elem.isSelected()) {
                iter.remove();
            }
        }

        triggerListeners();
    }

    private void displayInfo() {

        String info = "World has " + elements.size() + " elements.";
        for (OpticElement e : elements) {
            info += "{" + e.getClass() + ": " + e.x + ", " + e.y + "}\n";
        }
        System.out.println(info);
    }

    public void receiveLeftClick(float xClick, float yClick) {
        xLeftClick = xClick;
        yLeftClick = yClick;

        // if objects are being created: stop moving the objects
        if (elementBeingCreated) {
            elementBeingCreated = false;
            // Place the new elements / clones in the main list.
            for (OpticElement clone : clones) {
                elements.add(clone);
                clone.isSelected = false;
            }
            clones.clear();
            elementBeingCreated = false;
            selectionBeingMoved = false;
            displayInfo();
        }

        if (pointIsInSelectedObject(xClick, yClick)) {
            // click on a selected object: start moving all selected objects
            selectionBeingMoved = true;
        } else {
            // Start the selection rectangle.
            isCurrentlySelecting = true;
        }

        triggerListeners();
    }

    /**
     * Un-select each and every element. After calling this method, no element
     * is selected.
     */
    public void unselectEverything() {
        for (OpticElement e : elements) {
            e.setSelected(false);
        }
        for (OpticElement e : clones) {
            e.setSelected(false);
        }
    }

    public void receiveLeftUnclick(float x, float y) {
        if (x == xLeftClick && y == yLeftClick) {
            // release at the same point, if inside an object: select that object
            unselectEverything();
            if (pointIsInObject(x, y)) {
                selectObjectsInRegion(x, y, x, y);
            }
        } else if (isCurrentlySelecting) {
            // Select all objects included in the rectangle.
            unselectEverything();
            selectObjectsInRegion(xLeftClick, yLeftClick, x, y);
            isCurrentlySelecting = false;
        }

        selectionBeingMoved = false;
        isCurrentlySelecting = false;
        triggerListeners();
    }

    /**
     * Get one object that contains the given point.
     *
     * @param x
     * @param y
     * @return one of the objects that contain the given point, if any exists,
     * null otherwise.
     */
    private OpticElement getObjectAt(float x, float y) {

        for (OpticElement e : elements) {
            if (e.containsPoint(x, y)) {
                // We found the first object that contains that point.
                return e;
            }
        }

        // No element contains that point.
        return null;
    }

    public void rotateAllSelectedObjects(float xCenter, float yCenter, float angleIncrement) {
        for (OpticElement e : elements) {
            if (e.isSelected) {
                e.rotate(angleIncrement);
            }
        }
        triggerListeners();
    }

    /**
     * Compute the x-coordinate of the barycenter of all objects that are
     * currently selected.
     *
     * @return
     */
    private float findBarycenterOfSelectionX() {
        float sum = 0;
        int count = 0;
        for (OpticElement e : elements) {
            if (e.isSelected) {
                sum += e.x;
                count++;
            }
        }
        if (count > 0) {
            return sum / count;
        } else {
            return 0;
        }
    }

    /**
     * Compute the y-coordinate of the barycenter of all objects that are
     * currently selected.
     *
     * @return
     */
    private float findBarycenterOfSelectionY() {
        float sum = 0;
        int count = 0;
        for (OpticElement e : elements) {
            if (e.isSelected) {
                sum += e.y;
                count++;
            }
        }
        if (count > 0) {
            return sum / count;
        } else {
            return 0;
        }
    }

    public void receiveRightClick(float x, float y) {
        /* If the click happens inside an object:
         *  if the object is selected,
         *      then all selected objects rotate around their mean center (even if only this object is selected);
         *  else the single object rotates around its own center.
         * If the click happens outside all objects, the view is translated.
         */

        if (pointIsInObject(x, y)) {
            if (pointIsInSelectedObject(x, y)) {
                // Select this object, un-select all other objects.
                for (OpticElement e : elements) {
                    e.isSelected = false;
                }
                getObjectAt(x, y).setSelected(true);
            }
            // Else keep the current selection as is.

            selectionBeingRotated = true;
            singleObjectBeingRotated = false;
            // Find the coordinates of the center of the rotation
            xRotationCenter = findBarycenterOfSelectionX();
            yRotationCenter = findBarycenterOfSelectionY();

            xRightClick = x;
            yRightClick = y;

            // Find the initial slope of the line going from the center of rotation to the mouse.
            float dx = xRightClick - xRotationCenter;
            float dy = yRightClick - yRotationCenter;
            initialAngle = (float) Math.atan(dy / dx);
        }
    }

    public void receiveRightUnclick(float x, float y) {
        selectionBeingRotated = false;
        singleObjectBeingRotated = false;
        triggerListeners();
    }

    public void receiveMouseMove(float x, float y) {

        float dx = x - lastXMouse;
        float dy = y - lastYMouse;

        // Translate any selected objects
        if (selectionBeingMoved) {
            for (OpticElement e : elements) {
                if (e.isSelected) {
                    e.translate(dx, dy);
                }
            }
        }

        // Translate any new objects in the clones list
        for (OpticElement e : clones) {
            e.translate(dx, dy);
        }

        // Rotate one or more objects only if the right click happens inside an object.
        if (singleObjectBeingRotated) {
            System.out.println("Rotating single object.");
            rotatingObject = getObjectAt(x, y);

            float dxRotation = xRightClick - xRotationCenter;
            float dyRotation = yRightClick - yRotationCenter;
            currentAngle = (float) Math.atan(dy / dx);
            rotatingObject.setRotation(currentAngle - initialAngle);
        }
        if (selectionBeingRotated) {
            System.out.println("Rotating all selection.");

        }
        lastXMouse = x;
        lastYMouse = y;

        triggerListeners();
    }

    /**
     * Select all objects that are located in the rectangle constructed between
     * the two points (x1, y1) and (x2, y2).
     */
    public void selectObjectsInRegion(float x1, float y1, float x2, float y2) {

        if (x1 == x2 && y1 == y2) {
            // Selection by simple click on the object.
            for (OpticElement elem : elements) {
                if (elem.containsPoint(x1, y1)) {
                    elem.setSelected(true);
                } else {
                    elem.setSelected(false);
                }
            }
        } else {
            // Selection by rectangle.
            float xLeft = Math.min(x1, x2);
            float xRight = Math.max(x1, x2);
            float yBottom = Math.min(y1, y2);
            float yTop = Math.max(y1, y2);

            for (OpticElement elem : elements) {
                elem.setSelected(elem.isContainedInRegion(xLeft, yBottom, xRight, yTop));
            }
        }
    }

    /**
     * Duplicate any currently selected object, and let the user choose the
     * position of the copies by clicking at the chosen position. After this
     * method ends, all previously existing objects are unselected, and all the
     * copies are selected.
     */
    public void duplicateSelection() {

        for (OpticElement e : elements) {

            if (e.isSelected) {
                elementBeingCreated = true;
                // Clone the element.
                OpticElement clone = e.clone();
                clone.isSelected = false;
                clones.add(clone);

                e.isSelected = false;
                clone.isSelected = true;
            }
        }
    }

    /**
     * Returns true when the given point is inside at least one selected object,
     * false otherwise.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean pointIsInSelectedObject(float x, float y) {
        for (OpticElement e : elements) {
            if (e.isSelected) {
                if (e.containsPoint(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true when the given point is inside at least one object, selected
     * or not, false otherwise.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean pointIsInObject(float x, float y) {
        for (OpticElement e : elements) {
            if (e.containsPoint(x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean pixelIsInObject(int x, int y) {
        for (OpticElement e : elements) {
            if (e.containsPixel(x, y)) {
                return true;
            }
        }
        return false;
    }
}
