package com.bbms.ui;

import javax.swing.*;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class AddBloodGroupFrame extends JFrame {
    private JTextField groupNameField;
    private JButton addButton;
    private JButton closeButton;
    
  

    public AddBloodGroupFrame() {
        setTitle("Add Blood Group");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color to pink
        getContentPane().setBackground(Color.PINK);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        formPanel.setBackground(Color.PINK); // Set panel background color
        formPanel.add(new JLabel("Group Name:"));
        groupNameField = new JTextField();
        formPanel.add(groupNameField);
        add(formPanel, BorderLayout.NORTH);

       

        // Buttons and Footer Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.PINK); // Set panel background color
        
        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.PINK); // Set panel background color
        addButton = new JButton("Add");
        closeButton = new JButton("Close");
        buttonsPanel.add(addButton);
        buttonsPanel.add(closeButton);
        // Load icons
        try {
            Image imgUpdate = new ImageIcon(this.getClass().getResource("/add new.png")).getImage();
            addButton.setIcon(new ImageIcon(imgUpdate));
        } catch (Exception e) {
            System.out.println("Update icon not found.");
        }
        
       

        try {
            Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.out.println("Close icon not found.");
        }

        
        bottomPanel.add(buttonsPanel);

        // Add the footer panel at the bottom
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setPreferredSize(new Dimension(getWidth(), 50)); // Ensure the footer has a fixed height
        bottomPanel.add(footerPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Add Action Listeners
        addButton.addActionListener(e -> addBloodGroup());
        closeButton.addActionListener(e -> dispose());
    }

   

    private void addBloodGroup() {
        String groupName = groupNameField.getText();
        if (groupName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a group name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show confirmation dialog
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to add this blood group?",
            "Confirm Addition",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return; // If user clicks No, exit the method
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO BloodGroup (GroupName) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, groupName);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blood group added successfully.");
            // Refresh the table with updated data (assuming you have a method for that)
            // refreshTable();
            groupNameField.setText(""); // Clear the input field
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding blood group.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddBloodGroupFrame().setVisible(true));
    }
}
