package com.bbms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class AddBloodGroupFrame extends JFrame {
    private JTextField groupNameField;
    private JButton addButton;
    private JButton closeButton;
    private JTable bloodGroupTable;
    private DefaultTableModel tableModel;

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

        // Table setup
        tableModel = new DefaultTableModel();
        bloodGroupTable = new JTable(tableModel);
        tableModel.addColumn("BloodGroupId");
        tableModel.addColumn("GroupName");
        loadBloodGroupData();
        add(new JScrollPane(bloodGroupTable), BorderLayout.CENTER);

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

    private void loadBloodGroupData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM BloodGroup";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getString("GroupName")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood groups.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBloodGroup() {
        String groupName = groupNameField.getText();
        if (groupName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a group name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO BloodGroup (GroupName) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, groupName);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blood group added successfully.");
            loadBloodGroupData(); // Refresh the table with updated data
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
