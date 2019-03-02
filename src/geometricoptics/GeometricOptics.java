/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import javax.swing.JFrame;

/**
 *
 * @author arthurmanoha
 */
public class GeometricOptics{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){

        JFrame window = new JFrame();

        window.setTitle("Optics");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);

        window.setContentPane(new OpticsPanel());
        window.pack();
        window.repaint();
    }

}
