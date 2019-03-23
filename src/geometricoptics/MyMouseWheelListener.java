/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author arthurmanoha
 */
public class MyMouseWheelListener implements MouseWheelListener {

    private World world;
    private final ZoomScroll zs;
    private OpticsPanel opticsPanel;

    public MyMouseWheelListener(ZoomScroll zsParam, World w) {
        this(zsParam, w, null);
    }

    public MyMouseWheelListener(ZoomScroll zsParam, World w, OpticsPanel panel) {
        zs = zsParam;
        world = w;
        opticsPanel = panel;
    }

    public MyMouseWheelListener(ZoomScroll zsParam) {
        this(zsParam, null);
    }

    // The zoom and scroll are modified when the mouse wheel is used.
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation(); // +1: zoom out; -1: zoom in.
        opticsPanel.receiveMouseWheelMoved(e.getX(), e.getY(), rotation);
    }

}
