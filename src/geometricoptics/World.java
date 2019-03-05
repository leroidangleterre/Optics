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
public class World{

    private ArrayList<OpticElement> elements;
    private ArrayList<OpticElement> clones; // Clones or new elements

    private boolean selectionBeingMoved = false;

    private ArrayList<JComponent> listeners;

    private boolean isCurrentlySelecting;

    private boolean isAllSelected;
    private boolean elementBeingCreated;

    private boolean leftClickActive, rightClickActive;
    private float xLeftClick, yLeftClick, xRightClick, yRightClick;
    private float lastXMouse, lastYMouse;

    public World(){
        elements = new ArrayList<>();
        listeners = new ArrayList<>();
        isAllSelected = false;
        isCurrentlySelecting = false;
        elementBeingCreated = false;

        leftClickActive = false;
        rightClickActive = false;

        clones = new ArrayList<>();
    }

    public void addListener(JComponent component){
        listeners.add(component);
    }

    public void triggerListeners(){
        for(JComponent component : listeners){
            component.repaint();
        }
    }

    /**
     * Apply the command given as a parameter. Example: "Lens" or "Laser" to
     * create a lens or a laser.
     *
     * @param command
     */
    public void applyCommand(String command){
        if(command.startsWith("Create")){
            // Create a new object
            String objectName = command.substring(7);
            System.out.println("object name: <" + objectName + ">");
            OpticElement newOpticElement = null;
            if(objectName.equals("lens")){
                newOpticElement = new Lens();
            } else if(objectName.equals("laser")){
                newOpticElement = new Laser();
            }
            if(newOpticElement != null){
                clones.add(newOpticElement);
                newOpticElement.setSelected(true);
                newOpticElement.x = lastXMouse;
                newOpticElement.y = lastYMouse;
                elementBeingCreated = true;
            }
        }
        triggerListeners();
    }

    public void moveSelected(float dx, float dy){
        for(OpticElement element : elements){
            if(element.isSelected()){
                element.translate(dx, dy);
            }
        }
    }

    public void paint(Graphics g, float x0, float y0, float zoom){

        // Paint the axes
        float axisLength = 10;
        g.setColor(Color.black);
        g.drawLine((int) x0, (int) y0, (int) (x0 + zoom * axisLength), (int) y0);
        g.drawLine((int) x0, (int) y0, (int) x0, (int) (y0 + zoom * axisLength));

        for(OpticElement element : elements){
            element.paint(g, x0, y0, zoom);
        }
        for(OpticElement element : clones){
            element.paint(g, x0, y0, zoom);
        }

        // Paint the selection rectangle if a selection is being made.
        if(isCurrentlySelecting){
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
    public void toggleSelection(){
        if(isAllSelected){
            for(OpticElement elem : elements){
                elem.setSelected(false);
            }
            isAllSelected = false;
        } else{
            for(OpticElement elem : elements){
                elem.setSelected(true);
            }
            isAllSelected = true;
        }
        triggerListeners();
    }

    /*
    * Remove each selected object; leave all other objects unchanged.
     */
    public void deleteSelected(){

        Iterator<OpticElement> iter = elements.iterator();
        while(iter.hasNext()){
            OpticElement elem = iter.next();
            // Remove the element if it is selected.
            if(elem.isSelected()){
                iter.remove();
            }
        }

        triggerListeners();
    }

    /**
     * A point is in the selection if it is in at least one selected element.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean pointIsInSelection(float x, float y){
        for(OpticElement elem : elements){
            if(elem.isSelected){
                if(elem.containsPoint(x, y)){
                    return true;
                }
            }
        }
        return false;
    }

    public void receiveLeftClick(float xClick, float yClick){

        xLeftClick = xClick;
        yLeftClick = yClick;

        // if objects are being created: stop moving the objects
        if(elementBeingCreated){
            elementBeingCreated = false;
            // Place the new elements / clones in the main list.
            for(OpticElement clone : clones){
                elements.add(clone);
                clone.isSelected = false;
            }
            clones.clear();
            elementBeingCreated = false;
            selectionBeingMoved = false;
        }

        if(pointIsInSelected(xClick, yClick)){
            // click on a selected object: start moving all selected objects
            selectionBeingMoved = true;
        } else{
            // Start the selection rectangle.
            isCurrentlySelecting = true;
        }

        triggerListeners();
    }

    public void receiveLeftUnclick(float x, float y){
        if(x == xLeftClick && y == yLeftClick){
            // release at the same point, if inside an object: select that object

        } else if(isCurrentlySelecting){
            // Select all objects included in the rectangle.
            selectObjectsInRegion(xLeftClick, yLeftClick, x, y);
            isCurrentlySelecting = false;
        }

        selectionBeingMoved = false;
        isCurrentlySelecting = false;
        triggerListeners();
    }

    public void receiveRightClick(float x, float y){

    }

    public void receiveRightUnclick(float x, float y){
        triggerListeners();
    }

    public void receiveMouseMove(float x, float y){

        float dx = x - lastXMouse;
        float dy = y - lastYMouse;

        // Translate any selected objects
        if(selectionBeingMoved){
            for(OpticElement e : elements){
                if(e.isSelected){
                    e.translate(dx, dy);
                }
            }
        }

        // Translate any new objects in the clones list
        for(OpticElement e : clones){
            if(e.isSelected){
                e.translate(dx, dy);
            }
        }

        lastXMouse = x;
        lastYMouse = y;

        triggerListeners();
    }

    /**
     * Select all objects that are located in the rectangle constructed between
     * the two points (x1, y1) and (x2, y2).
     */
    public void selectObjectsInRegion(float x1, float y1, float x2, float y2){

        float xLeft = Math.min(x1, x2);
        float xRight = Math.max(x1, x2);
        float yBottom = Math.min(y1, y2);
        float yTop = Math.max(y1, y2);

        for(OpticElement elem : elements){
            elem.setSelected(elem.isContainedInRegion(xLeft, yBottom, xRight, yTop));
        }
    }

    /**
     * Duplicate any currently selected object, and let the user choose the
     * position of the copies by clicking at the chosen position. After this
     * method ends, all previously existing objects are unselected, and all the
     * copies are selected.
     */
    public void duplicateSelection(){

        for(OpticElement e : elements){

            if(e.isSelected){
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
    public boolean pointIsInSelected(float x, float y){
        for(OpticElement e : elements){
            if(e.isSelected){
                if(e.containsPoint(x, y)){
                    return true;
                }
            }
        }
        return false;
    }
}
