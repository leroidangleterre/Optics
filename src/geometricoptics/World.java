/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;

/**
 *
 * @author arthurmanoha
 *
 * This class encapsulates all the physical elements that are simulated: lasers,
 * lenses, mirrors... It is drawn in an OpticsPanel.
 */
public class World {

    private boolean testPixels = false;

    private ArrayList<OpticElement> elements;
    private ArrayList<OpticElement> clones; // Clones or new elements

    private ArrayList<Photon> photonList;
    private boolean allLasersActive;

    private ArrayList<JComponent> listeners;

    private boolean isAllSelected;
    public boolean elementBeingCreated;

    private float dt;

    private float xMax = 30;

    private Timer timer;

    private boolean isPlaying;

    public World() {
        elements = new ArrayList<>();
        listeners = new ArrayList<>();
        isAllSelected = false;
        elementBeingCreated = false;

        clones = new ArrayList<>();

        for (int i = 0; i < 1; i++) {

            Laser newLaser = new Laser(0, 0);
            elements.add(newLaser);
            float angle = (float) (i * 0.03);
            newLaser.rotate(angle);

            Mirror m = new Mirror(25, i);
            elements.add(m);
        }

        dt = 0.1f;

        photonList = new ArrayList<>();
        allLasersActive = true;

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (isPlaying) {
                    evolve();
                }
            }
        }, 0, 25);
        isPlaying = true;
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
     * Display the size of the elements list and the clones list.
     */
    public void test() {
        System.out.println("World: " + elements.size() + " elements, " + clones.size() + " clones.");
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
        if (elem instanceof Laser) {
            ((Laser) elem).activate(this.allLasersActive);
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

    public synchronized void paint(Graphics g, float x0, float y0, float zoom) {

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
        for (Photon p : photonList) {
            p.paint(g, x0, y0, zoom);
        }

        if (testPixels) {
            // Display pixels that are contained in any elements.
            for (int xPixel = 0; xPixel <= 1000; xPixel += 50) {
                for (int yPixel = 0; yPixel <= 1000; yPixel += 50) {
                    // Compute in-world coordinates of the pixel
                    float xReal = (xPixel - x0) / zoom;
                    float yReal = (graphicsHeight - yPixel - y0) / zoom;
                    if (this.pointIsInObject(xReal, yReal)) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.black);
                    }
                    g.fillRect(xPixel - 1, yPixel - 1, 3, 3);
                }
            }
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
        System.out.println("World.makeClonesReal() " + elements.size() + ", " + clones.size());

        if (elementBeingCreated) {
            elementBeingCreated = false;
            for (OpticElement clone : clones) {
                elements.add(clone);
//                clone.isSelected = false;
            }
            clones.clear();
        }
        System.out.println("      makeClonesReal() " + elements.size() + ", " + clones.size());
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

        System.out.println("Point (" + x + ", " + y + "):");
        for (OpticElement e : elements) {
            System.out.println("    element at " + e.x + ", " + e.y + " ");
            if (e.containsPoint(x, y)) {
                // We found the first object that contains that point.
                System.out.println("contains point.");
                return e;
            }
            System.out.println("does not contain point.");
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
//            System.out.println("World.selectObjectsInRegion, same spot");
            // Selection by simple click on the object.
            for (OpticElement elem : elements) {
                if (elem.containsPoint(x1, y1)) {
                    elem.setSelected(true);
//                    System.out.println("Elem " + elem + " is now selected.");
                } else {
                    elem.setSelected(false);
//                    System.out.println("Elem " + elem + " is now NOT selected.");
                }
            }
        } else {
//            System.out.println("World.selectObjectsInRegion, selection by rectangle");
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

    /**
     * Delete any photon located too far away from the origin.
     */
    private void removeExternalPhotons() {
        Iterator iter = photonList.iterator();
        while (iter.hasNext()) {
            Photon p = (Photon) iter.next();
            if (p.getDistance() > xMax) {
                iter.remove();
            }
        }
    }

    /**
     * Light is emitted from the lasers and interacts with the objects.
     */
    public synchronized void evolve() {
        for (Photon p : photonList) {
            p.travel(dt);
        }
        for (OpticElement e : elements) {
            if (e instanceof Laser) {
                Photon newPhoton = ((Laser) e).emit();
                if (newPhoton != null) {
                    photonList.add(newPhoton);
                }
            }
        }
        for (OpticElement e : elements) {
            for (Photon p : photonList) {
                e.interactWithPhoton(p);
            }
        }
        removeExternalPhotons();
        triggerListeners();
    }

    /**
     * Activate all lasers if they are off, or shut them off.
     *
     */
    public void toggleLasers() {
        for (OpticElement elem : elements) {
            if (elem instanceof Laser) {
                ((Laser) elem).toggle();
            }
        }
    }

    public void togglePlayPause() {
        isPlaying = !isPlaying;
    }

    public void saveToFile(File f) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));

            for (OpticElement e : elements) {
                writer.write(e.toString() + "\n");
            }

            writer.close();
        } catch (IOException e) {
            System.out.println("Writing error.");
        }
    }

    public void loadFromFile(File f) {
        try {
            elements.clear();
            BufferedReader reader = new BufferedReader(new FileReader(f));

            String line = reader.readLine();
            do {

                String[] values = line.split(" ");

                String objectType = values[0];
                OpticElement newElement;
                float x = new Float(values[1]);
                float y = new Float(values[2]);
                float rotation = new Float(values[3]);
                float width = new Float(values[4]);
                float height = new Float(values[5]);
                switch (objectType) {
                    case "Laser":
                        newElement = new Laser();
                        ((Laser) newElement).width = width;
                        ((Laser) newElement).height = height;
                        break;
                    case "Mirror":
                        newElement = new Mirror();
                        ((Mirror) newElement).width = width;
                        ((Mirror) newElement).height = height;
                        break;
                    default:
                        newElement = null;
                        break;
                }
                if (newElement != null) {
                    newElement.x = x;
                    newElement.y = y;
                    newElement.rotation = rotation;
                }
                elements.add(newElement);
                line = reader.readLine();
            } while (line != null);

        } catch (IOException e) {
            System.out.println("error reading file");
        }
    }
}
