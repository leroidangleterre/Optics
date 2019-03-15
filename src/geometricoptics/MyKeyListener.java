/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author arthurmanoha
 */
public class MyKeyListener implements KeyListener {

    private World world;

    public MyKeyListener(World w) {
        world = w;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        float angleIncrement = (float) (Math.PI / 16);
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                System.out.println("KeyListener A");
                world.toggleSelection();
                break;
            case KeyEvent.VK_D:
                world.duplicateSelection();
                break;
            case KeyEvent.VK_DELETE:
                world.deleteSelected();
                break;
            case KeyEvent.VK_RIGHT:
                world.rotateAllSelectedObjects(0, 0, angleIncrement);
                System.out.println("rotate right");
                break;
            case KeyEvent.VK_LEFT:
                world.rotateAllSelectedObjects(0, 0, -angleIncrement);
                System.out.println("rotate left");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
