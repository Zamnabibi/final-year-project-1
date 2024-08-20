package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import net.proteanit.sql.DbUtils;

@SuppressWarnings("serial")
public class DeleteBloodRequestFrame extends JFrame {
    private JComboBox<Integer> requestIdComboBox;
    private JButton deleteButton;
    private JButton closeButton;
    private JTable bloodRequestTable;
    private JScrollPane tableScrollPane;

    public DeleteBloodRequestFrame() {
        setTitle("Delete Blood Request");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set the background color of the frame
        getContentPane().setBackground(Color.PINK);

        // Form panel to hold ComboBox and buttons
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.setBackground(Color.PINK); // Set background color of form panel
        add(formPanel, BorderLayout.NORTH);

        formPanel.add(new JLabel("BloodRequestId:"));
        requestIdComboBox = new JComboBox<>();
        formPanel.add(requestIdComboBox);

        deleteButton = new JButton("Delete");
        formPanel.add(deleteButton);

        closeButton = new JButton("Close");
        formPanel.add(closeButton);

        // Table to display data from BloodRequest table
        bloodRequestTable = new JTable();
        tableScrollPane = new JScrollPane(bloodRequestTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Load ComboBox Data
        loadComboBoxData();

        // Load Table Data
        loadTableData();

        deleteButton.addActionListener(e -> deleteBloodRequest());
        closeButton.addActionListener(e -> dispose());
    }

    private void loadComboBoxData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodRequestId FROM BloodRequest";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                requestIdComboBox.addItem(resultSet.getInt("BloodRequestId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading combo box data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodRequestId, PatientId, BloodGroupId, GroupName, RequestDate, RequestStatus FROM BloodRequest";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            bloodRequestTable.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading table data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBloodRequest() {
        Integer requestId = (Integer) requestIdComboBox.getSelectedItem();
        if (requestId == null) {
            JOptionPane.showMessageDialog(this, "Please select a Blood Request ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM BloodRequest WHERE BloodRequestId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, requestId);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blood request deleted successfully.");
            loadTableData(); // Reload table data after deletion
            loadComboBoxData(); // Reload ComboBox data after deletion
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting blood request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteBloodRequestFrame().setVisible(true));
    }
}
