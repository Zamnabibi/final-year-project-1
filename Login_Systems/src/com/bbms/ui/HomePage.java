package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class HomePage extends JFrame {

    public HomePage() {
        // Set up the frame
        setTitle("Home Page");
        setSize(854, 517);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel with a background color
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(null);
        backgroundPanel.setBackground(Color.PINK);

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create menus and add them to the menu bar
        createUserMenu(menuBar);
        createDonorMenu(menuBar);
        createPatientMenu(menuBar);
        createBloodGroupMenu(menuBar);
        createHistoryMenu(menuBar); // Add the History menu
        createLogoutMenu(menuBar); // Add the Logout menu

        setJMenuBar(menuBar);

        // Add the background image
        JLabel lblBackground = new JLabel();
        Image img1 = new ImageIcon(getClass().getResource("/back.jpg")).getImage();
        lblBackground.setIcon(new ImageIcon(img1.getScaledInstance(854, 517, Image.SCALE_SMOOTH)));
        lblBackground.setBounds(0, 0, 854, 517);
        backgroundPanel.add(lblBackground);

        // Add the background panel to the frame
        setContentPane(backgroundPanel);
    }

    private void createUserMenu(JMenuBar menuBar) {
        JMenu userMenu = new JMenu("User");
        // Set the icon for the User menu
        try {
            Image img = new ImageIcon(this.getClass().getResource("/add donor.png")).getImage(); // Update with your actual icon path
            userMenu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("User menu icon not found.");
        }

        // Define actions and corresponding icons
        String[] actions = {"Add User", "Update User", "Delete User"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png"}; // Update with your actual icon paths

        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i]; // Make the variable final for use in lambda expression
            JMenuItem menuItem = new JMenuItem(action);
            // Set icon for the menu item
            try {
                Image img = new ImageIcon(this.getClass().getResource(icons[i])).getImage();
                menuItem.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Icon not found for action: " + action);
            }
            menuItem.addActionListener(e -> openFrame(action));
            addMenuShortcut(menuItem, action);
            userMenu.add(menuItem);
        }

        menuBar.add(userMenu);
    }


    private void createDonorMenu(JMenuBar menuBar) {
        JMenu donorMenu = new JMenu("Donor Unit");
        // Set the icon for the Donor menu
        try {
            Image img = new ImageIcon(this.getClass().getResource("/add new.png")).getImage(); // Update with your actual icon path
            donorMenu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Donor icon not found.");
        }

        // Define actions and corresponding icons
        String[] actions = {"Add Donor Unit", "Update Donor Unit", "Delete Donor Unit"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png"}; // Update with your actual icon paths

        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i]; // Make the variable final for use in lambda expression
            JMenuItem menuItem = new JMenuItem(action);
            // Set icon for the donor menu item
            try {
                Image img = new ImageIcon(this.getClass().getResource(icons[i])).getImage();
                menuItem.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Icon not found for action: " + action);
            }
            menuItem.addActionListener(e -> openFrame(action));
            addMenuShortcut(menuItem, action);
            donorMenu.add(menuItem);
        }

        menuBar.add(donorMenu);
    }

    private void createPatientMenu(JMenuBar menuBar) {
        JMenu patientMenu = new JMenu("Patient Unit");
        // Set the icon for the Patient menu
        try {
            Image img = new ImageIcon(this.getClass().getResource("/add donor.png")).getImage(); // Update with your actual icon path
            patientMenu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Patient icon not found.");
        }

        // Define actions and corresponding icons
        String[] actions = {"Add Patient Unit", "Update Patient Unit", "Delete Patient Unit"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png"}; // Update with your actual icon paths

        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i]; // Make the variable final for use in lambda expression
            JMenuItem menuItem = new JMenuItem(action);
            // Set icon for the patient menu item
            try {
                Image img = new ImageIcon(this.getClass().getResource(icons[i])).getImage();
                menuItem.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Icon not found for action: " + action);
            }
            menuItem.addActionListener(e -> openFrame(action));
            addMenuShortcut(menuItem, action);
            patientMenu.add(menuItem);
        }

        menuBar.add(patientMenu);
    }

    private void createBloodGroupMenu(JMenuBar menuBar) {
        JMenu bloodGroupMenu = new JMenu("Blood Group");
        // Set the icon for the Blood Group menu
        try {
            Image img = new ImageIcon(this.getClass().getResource("/blood group.png")).getImage(); // Update with your actual icon path
            bloodGroupMenu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Blood Group icon not found.");
        }

        // Define actions and corresponding icons
        String[] actions = {"Add Blood Group", "Update Blood Group", "Delete Blood Group"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png"}; // Update with your actual icon paths

        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i]; // Make the variable final for use in lambda expression
            JMenuItem menuItem = new JMenuItem(action);
            // Set icon for the blood group menu item
            try {
                Image img = new ImageIcon(this.getClass().getResource(icons[i])).getImage();
                menuItem.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Icon not found for action: " + action);
            }
            menuItem.addActionListener(e -> openFrame(action));
            addMenuShortcut(menuItem, action);
            bloodGroupMenu.add(menuItem);
        }

        menuBar.add(bloodGroupMenu);
    }

    private void createHistoryMenu(JMenuBar menuBar) {
        JMenu historyMenu = new JMenu("History");
        // Set the icon for the History menu
        try {
            Image img = new ImageIcon(this.getClass().getResource("/details.png")).getImage(); // Update with your actual icon path
            historyMenu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("History icon not found.");
        }

        // Define actions and corresponding icons
        String[] actions = {"View History"};
        String[] icons = {"/details.png"}; // Update with your actual icon path

        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i]; // Make the variable final for use in lambda expression
            JMenuItem menuItem = new JMenuItem(action);
            // Set icon for the history menu item
            try {
                Image img = new ImageIcon(this.getClass().getResource(icons[i])).getImage();
                menuItem.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Icon not found for action: " + action);
            }
            menuItem.addActionListener(e -> openFrame(action));
            addMenuShortcut(menuItem, action);
            historyMenu.add(menuItem);
        }

        menuBar.add(historyMenu);
    }


    private void createLogoutMenu(JMenuBar menuBar) {
        JMenu logoutMenu = new JMenu("Logout");
        // Set the icon for the Logout menu
        try {
            Image img = new ImageIcon(this.getClass().getResource("/logout.jpg")).getImage();
            logoutMenu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Logout icon not found.");
        }
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        // Set icon for the logout menu item
        try {
            Image img = new ImageIcon(this.getClass().getResource("/logout.jpg")).getImage();
            logoutMenuItem.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Logout item icon not found.");
        }
        logoutMenuItem.addActionListener(e -> logout());
        logoutMenu.add(logoutMenuItem);
        menuBar.add(logoutMenu);
    }

    private void openFrame(String action) {
        switch (action) {
            case "Add User": new AddUserFrame().setVisible(true); break;
            case "Update User": new UpdateUserFrame().setVisible(true); break;
            case "Delete User": new DeleteUserFrame().setVisible(true); break;
            case "Add Donor Unit": new AddDonorFrame().setVisible(true); break;
            case "Update Donor Unit": new UpdateDonorFrame().setVisible(true); break;
            case "Delete Donor Unit": new DeleteDonorFrame().setVisible(true); break;
            case "Add Patient Unit": new AddPatientFrame().setVisible(true); break;
            case "Update Patient Unit": new UpdatePatientFrame().setVisible(true); break;
            case "Delete Patient Unit": new DeletePatientFrame().setVisible(true); break;
            case "Add Blood Group": new AddBloodGroupFrame().setVisible(true); break;
            case "Update Blood Group": new UpdateBloodGroupFrame().setVisible(true); break;
            case "Delete Blood Group": new DeleteBloodGroupFrame().setVisible(true); break;
            case "View History": new HistoryFrame().setVisible(true); break;
        }
    }

    @SuppressWarnings("deprecation")
	private void addMenuShortcut(JMenuItem menuItem, String action) {
        Action openFrameAction = new AbstractAction(action) {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFrame(action);
            }
        };
        getRootPane().getActionMap().put(action, openFrameAction);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(action.charAt(0), Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    private void logout() {
    	 int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
         if (result == JOptionPane.YES_OPTION) {
             this.dispose(); // Close the current frame
             // Uncomment the line below to open the login page after logout
             new Login_S().setVisible(true); 
         } 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage().setVisible(true));
    }
}
