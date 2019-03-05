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
public class OpticsMenu extends JMenu implements ActionListener, ItemListener{

    private World world;

    public OpticsMenu(World w){
        super("Menu");

        world = w;

        // Add objects to the simulation
        JMenu addMenu = new JMenu("Add");

        JMenuItem item = new JMenuItem("Create lens");
        item.addActionListener(this);
        addMenu.add(item);
        item = new JMenuItem("Create laser");
        item.addActionListener(this);
        addMenu.add(item);

        this.add(addMenu);

    }

    @Override
    public void itemStateChanged(ItemEvent e){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e){
        System.out.println("actionPerformed: " + e.getActionCommand());
        String command = e.getActionCommand();
        world.applyCommand(command);
    }

}
