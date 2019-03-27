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
    private boolean nbCtrlPressed;

    public MyKeyListener(World w) {
        world = w;
        nbCtrlPressed = false;
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
            case KeyEvent.VK_L:
                world.toggleLasers();
                break;
            case KeyEvent.VK_P:
                world.togglePlayPause();
                break;
            case KeyEvent.VK_DELETE:
                world.deleteSelected();
                break;
            case KeyEvent.VK_RIGHT:
                if (nbCtrlPressed) {
                    world.rotateAllSelectedObjects(-angleIncrement);
                } else {
                    world.rotateAllSelectedObjects(-angleIncrement, true);
                }
                System.out.println("rotate right");
                break;
            case KeyEvent.VK_LEFT:
                if (nbCtrlPressed) {
                    world.rotateAllSelectedObjects(angleIncrement);
                } else {
                    world.rotateAllSelectedObjects(angleIncrement, true);
                }
                System.out.println("rotate left");
                break;

            case KeyEvent.VK_CONTROL:
                nbCtrlPressed = true;
                break;
            case KeyEvent.VK_ENTER:
                world.evolve();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL:
                nbCtrlPressed = false;
                break;
            default:
                break;
        }
    }
}
