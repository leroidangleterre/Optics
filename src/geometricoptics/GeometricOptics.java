/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 *
 * @author arthurmanoha
 */
public class GeometricOptics {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        World world = new World();

        JFrame window = new JFrame();

        window.setTitle("Optics");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        JMenuBar menuBar = new JMenuBar();

        window.setJMenuBar(menuBar);

        OpticsPanel panel = new OpticsPanel(world);
        JMenu menu = new OpticsMenu(world, panel);
        menuBar.add(menu);

        MyMouseListener listener = new MyMouseListener(panel);
        panel.setMouseListener(listener);

        window.addKeyListener(new MyKeyListener(world));

        window.setContentPane(panel);
        window.pack();
        window.repaint();
    }

}
