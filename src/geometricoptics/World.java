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

    private ArrayList<JComponent> listeners;

    private boolean isAllSelected;
    public boolean elementBeingCreated;

    public World() {
        elements = new ArrayList<>();
        listeners = new ArrayList<>();
        isAllSelected = false;
        elementBeingCreated = false;

        clones = new ArrayList<>();

        elements.add(new Mirror(0, 4));
        elements.add(new Mirror(0, 0));
        elements.add(new Mirror(-6, 0));
        elements.add(new Mirror(6, 0));
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
     * Create a new element.
     *
     * @param elem the newly created element. The coordinates of the new element
     * must have been already chosen.
     */
    public void create(OpticElement elem) {
        if (elem != null) {
            clones.add(elem);
            elem.setSelected(true);
            elementBeingCreated = true;
        }
        triggerListeners();
    }

    /**
     * Translate each selected element.
     *
     * @param dx
     * @param dy
     */
    public void translateSelected(float dx, float dy) {
        for (OpticElement element : elements) {
            if (element.isSelected()) {
                element.translate(dx, dy);
            }
        }
    }

    /**
     * Translate each selected clone.
     *
     * @param dx
     * @param dy
     */
    public void translateClones(float dx, float dy) {
        for (OpticElement element : clones) {
            if (element.isSelected()) {
                element.translate(dx, dy);
            }
        }
    }

    public void paint(Graphics g, float x0, float y0, float zoom) {

        int graphicsHeight = g.getClipBounds().height;

        // Paint the axes
        float axisLength = 10;
        g.setColor(Color.black);
        g.drawLine((int) x0, graphicsHeight - (int) y0, (int) (x0 + zoom * axisLength), graphicsHeight - (int) y0);
        g.drawLine((int) x0, graphicsHeight - (int) y0, (int) x0, graphicsHeight - (int) (y0 + zoom * axisLength));

        for (OpticElement element : elements) {
            element.paint(g, x0, y0, zoom);
        }
        for (OpticElement element : clones) {
            element.paint(g, x0, y0, zoom);
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

    public void makeClonesReal() {

        if (elementBeingCreated) {
            elementBeingCreated = false;
            for (OpticElement clone : clones) {
                elements.add(clone);
                clone.isSelected = false;
            }
            clones.clear();
        }
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

    /**
     * Get one object that contains the given point.
     *
     * @param x
     * @param y
     * @return one of the objects that contain the given point, if any exists,
     * null otherwise.
     */
    public OpticElement getObjectAt(float x, float y) {

        for (OpticElement e : elements) {
            if (e.containsPoint(x, y)) {
                // We found the first object that contains that point.
                return e;
            }
        }

        // No element contains that point.
        return null;
    }

    /**
     * Rotate each selected element around their common barycenter or around
     * itself.
     *
     * @param angleIncrement
     * @param aroundBarycenter
     */
    public void rotateAllSelectedObjects(float angleIncrement, boolean aroundBarycenter) {
        if (aroundBarycenter) {
            float xCenter = findBarycenterOfSelectionX();
            float yCenter = findBarycenterOfSelectionY();

            for (OpticElement e : elements) {
                if (e.isSelected) {
                    e.rotate(angleIncrement, xCenter, yCenter);
                }
            }
        } else {
            for (OpticElement e : elements) {
                if (e.isSelected) {
                    e.rotate(angleIncrement);
                }
            }
        }
        triggerListeners();
    }

    /**
     * Rotate each selected element around itself.
     *
     * @param angleIncrement the change in the object's orientation.
     */
    public void rotateAllSelectedObjects(float angleIncrement) {
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
    public float findBarycenterOfSelectionX() {
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
    public float findBarycenterOfSelectionY() {
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
}
