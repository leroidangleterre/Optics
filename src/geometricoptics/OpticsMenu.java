/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geometricoptics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author arthurmanoha
 */
public class OpticsMenu extends JMenu implements ActionListener, ItemListener {

    private World world;
    private OpticsPanel panel;

    public OpticsMenu(World w, OpticsPanel p) {
        super("Menu");

        world = w;
        panel = p;

        // Add objects to the simulation
        JMenu addMenu = new JMenu("Add");

        // Lens
        JMenuItem item = new JMenuItem("Create lens");
        item.addActionListener(this);
        addMenu.add(item);
        // Laser
        item = new JMenuItem("Create laser");
        item.addActionListener(this);
        addMenu.add(item);
        // Mirror
        item = new JMenuItem("Create mirror");
        item.addActionListener(this);
        addMenu.add(item);

        this.add(addMenu);

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        throw new UnsupportedOperationException("Menu: not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        panel.applyCommand(command);
    }

}
