/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
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

    public OpticsPanel(World w) {
        super();

        world = w;

        Dimension preferredSize = new Dimension(1000, 700);
        this.setPreferredSize(preferredSize);
        this.zoomScroll = new ZoomScroll();
        this.zoomScroll.addListener(this);
        zoomScroll.setX(500);
        zoomScroll.setY(350);
        zoomScroll.setZoom(7.9f);

        world.addListener(this);

        MyMouseListener mouseListener = new MyMouseListener(zoomScroll, w, this);

        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.addMouseWheelListener(new MyMouseWheelListener(zoomScroll, w));
        this.addKeyListener(new MyKeyListener(w));

        isCurrentlyScrolling = false;
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
}
