/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author arthurmanoha
 *
 * This panel is used to draw the optical elements such as lenses, as well as
 * the light rays. The user can scroll and zoom in the image.
 */
public class OpticsPanel extends JPanel{

    // Scroll and zoom
    private ZoomScroll zoomScroll;
    private Rectangle rect;

    public OpticsPanel(){
        super();
        Dimension preferredSize = new Dimension(1000, 700);
        this.setPreferredSize(preferredSize);
        this.zoomScroll = new ZoomScroll();
        this.zoomScroll.addListener(this);
        this.rect = new Rectangle(150, 100, 300, 200);
        this.addMouseListener(new MyMouseListener(zoomScroll));
        this.addMouseMotionListener(new MyMouseMotionListener(zoomScroll));
        this.addMouseWheelListener(new MyMouseWheelListener(zoomScroll));
    }

    public void paintComponent(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        g.setColor(Color.red);
        rect.paint(g, zoomScroll.getX(), zoomScroll.getY(), zoomScroll.getZoom());
    }
}
