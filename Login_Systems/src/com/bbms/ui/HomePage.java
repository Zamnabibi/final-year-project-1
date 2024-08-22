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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel with a background color
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BorderLayout()); // Use BorderLayout for better layout management
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
        backgroundPanel.add(lblBackground, BorderLayout.CENTER); // Use BorderLayout.CENTER to cover the whole panel

        // Create and add the footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.pink);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.");
        footerPanel.add(footerLabel);
        backgroundPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add the background panel to the frame
        setContentPane(backgroundPanel);
    }

    private void createUserMenu(JMenuBar menuBar) {
        JMenu userMenu = new JMenu("User");
        setMenuIcon(userMenu, "/add donor.png");

        String[] actions = {"Add User", "Update User", "Delete User", "View User"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png", "/view.png"};

        addMenuItems(userMenu, actions, icons);
        menuBar.add(userMenu);
    }

    private void createDonorMenu(JMenuBar menuBar) {
        JMenu donorMenu = new JMenu("Donor Unit");
        setMenuIcon(donorMenu, "/add new.png");

        String[] actions = {"Add Donor Unit", "Update Donor Unit", "Delete Donor Unit", "View Donor Units"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png", "/view.png"};

        addMenuItems(donorMenu, actions, icons);
        menuBar.add(donorMenu);
    }

    private void createPatientMenu(JMenuBar menuBar) {
        JMenu patientMenu = new JMenu("Patient Unit");
        setMenuIcon(patientMenu, "/add donor.png");

        String[] actions = {"Add Patient Unit", "Update Patient Unit", "Delete Patient Unit", "View Patient Units"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png", "/view.png"};

        addMenuItems(patientMenu, actions, icons);
        menuBar.add(patientMenu);
    }

    private void createBloodGroupMenu(JMenuBar menuBar) {
        JMenu bloodGroupMenu = new JMenu("Blood Group");
        setMenuIcon(bloodGroupMenu, "/blood group.png");

        String[] actions = {"Add Blood Group", "Update Blood Group", "Delete Blood Group", "View Blood Group"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png", "/view.png"};

        addMenuItems(bloodGroupMenu, actions, icons);
        menuBar.add(bloodGroupMenu);
    }

    private void createHistoryMenu(JMenuBar menuBar) {
        JMenu historyMenu = new JMenu("History");
        setMenuIcon(historyMenu, "/details.png");

        String[] actions = {"View History"};
        String[] icons = {"/details.png"};

        addMenuItems(historyMenu, actions, icons);
        menuBar.add(historyMenu);
    }

    private void createLogoutMenu(JMenuBar menuBar) {
        JMenu logoutMenu = new JMenu("Logout");
        setMenuIcon(logoutMenu, "/logout.jpg");

        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        setMenuIcon(logoutMenuItem, "/logout.jpg");
        logoutMenuItem.addActionListener(e -> logout());
        logoutMenu.add(logoutMenuItem);
        menuBar.add(logoutMenu);
    }

    private void setMenuIcon(JMenu menu, String iconPath) {
        try {
            Image img = new ImageIcon(getClass().getResource(iconPath)).getImage();
            menu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }
    }

    private void setMenuIcon(JMenuItem menuItem, String iconPath) {
        try {
            Image img = new ImageIcon(getClass().getResource(iconPath)).getImage();
            menuItem.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }
    }

    private void addMenuItems(JMenu menu, String[] actions, String[] icons) {
        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i];
            JMenuItem menuItem = new JMenuItem(action);
            setMenuIcon(menuItem, icons[i]);
            menuItem.addActionListener(e -> openFrame(action));
            addMenuShortcut(menuItem, action);
            menu.add(menuItem);
        }
    }

    private void openFrame(String action) {
        switch (action) {
            case "Add User": new AddUserFrame().setVisible(true); break;
            case "Update User": new UpdateUserFrame().setVisible(true); break;
            case "Delete User": new DeleteUserFrame().setVisible(true); break;
            case "View User": new ViewUserFrame().setVisible(true); break;
            case "Add Donor Unit": new AddDonorFrame().setVisible(true); break;
            case "Update Donor Unit": new UpdateDonorFrame().setVisible(true); break;
            case "Delete Donor Unit": new DeleteDonorFrame().setVisible(true); break;
            case "View Donor Units": new ViewDonorFrame().setVisible(true); break;
            case "Add Patient Unit": new AddPatientFrame().setVisible(true); break;
            case "Update Patient Unit": new UpdatePatientFrame().setVisible(true); break;
            case "Delete Patient Unit": new DeletePatientFrame().setVisible(true); break;
            case "View Patient Units": new ViewPatientFrame().setVisible(true); break;
            case "Add Blood Group": new AddBloodGroupFrame().setVisible(true); break;
            case "Update Blood Group": new UpdateBloodGroupFrame().setVisible(true); break;
            case "Delete Blood Group": new DeleteBloodGroupFrame().setVisible(true); break;
            case "View Blood Group": new ViewBloodGroupFrame().setVisible(true); break;
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
            new Login_S().setVisible(true); // Open the login frame
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePage().setVisible(true));
    }
}
