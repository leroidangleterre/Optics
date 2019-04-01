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
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author arthurmanoha
 */
public class OpticsMenuBar extends JMenuBar implements ActionListener, ItemListener {

    private World world;
    private OpticsPanel panel;

    public OpticsMenuBar(World w, OpticsPanel p) {
        super();

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

        JMenu saveMenu = new JMenu("Save/Load");
        this.add(saveMenu);
        // Save the current simulation
        item = new JMenuItem("Save");
        item.addActionListener(this);
        saveMenu.add(item);
        // Load simulations
        item = new JMenuItem("Load");
        item.addActionListener(this);
        saveMenu.add(item);

        JMenu toolsMenu = new JMenu("Tools");
        this.add(toolsMenu);
        item = new JMenuItem("Symmetry");
        item.addActionListener(this);
        toolsMenu.add(item);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        throw new UnsupportedOperationException("Menu: not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Save")) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                world.saveToFile(file);
            }
        } else if (command.equals("Load")) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                world.loadFromFile(file);
            }
        } else if (command.equals("Symmetry")) {
            world.flipSelectionHorizontally();
        } else {
            panel.applyCommand(command);
        }
    }

}
