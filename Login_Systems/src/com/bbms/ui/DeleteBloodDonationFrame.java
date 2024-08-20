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
public class DeleteBloodDonationFrame extends JFrame {
    private JComboBox<Integer> bloodDonationIdComboBox;
    private JButton deleteButton;
    private JButton closeButton;
    private JTable bloodDonationTable;
    private DefaultTableModel tableModel;

    public DeleteBloodDonationFrame() {
        setTitle("Delete Blood Donation");
        setSize(692, 591);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Set the background color of the content pane
        getContentPane().setBackground(Color.PINK);

        // Panel for input and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2));
        inputPanel.setOpaque(false); // Make the panel's background transparent
        
        inputPanel.add(new JLabel("BloodDonationId:"));
        bloodDonationIdComboBox = new JComboBox<>();
        inputPanel.add(bloodDonationIdComboBox);

        deleteButton = new JButton("Delete");
        inputPanel.add(deleteButton);

        closeButton = new JButton("Close");
        inputPanel.add(closeButton);

        getContentPane().add(inputPanel, BorderLayout.NORTH);

        // Table for displaying BloodDonation data
        tableModel = new DefaultTableModel(new Object[]{"BloodDonationId", "DonorId", "BloodGroupId", "GroupName", "DonationDate", "DonationStatus"}, 0);
        bloodDonationTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(bloodDonationTable);
        tableScrollPane.setOpaque(false); // Make the scroll pane's background transparent
        tableScrollPane.getViewport().setBackground(Color.PINK); // Set the viewport color

        getContentPane().add(tableScrollPane, BorderLayout.CENTER);

        deleteButton.addActionListener(e -> deleteBloodDonation());
        closeButton.addActionListener(e -> dispose());

        loadComboBoxData();
        loadBloodDonationTable();
    }

    private void loadComboBoxData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodDonationId FROM BloodDonation";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bloodDonationIdComboBox.addItem(resultSet.getInt("BloodDonationId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading combo box data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBloodDonation() {
        Integer bloodDonationId = (Integer) bloodDonationIdComboBox.getSelectedItem();

        if (bloodDonationId == null) {
            JOptionPane.showMessageDialog(this, "Please select a BloodDonationId.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM BloodDonation WHERE BloodDonationId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bloodDonationId);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blood donation deleted successfully.");
            loadBloodDonationTable(); // Refresh the table data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBloodDonationTable() {
        tableModel.setRowCount(0); // Clear existing data

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT b.BloodDonationId, b.DonorId, b.BloodGroupId, bg.GroupName, b.DonationDate, b.DonationStatus " +
                    "FROM BloodDonation b JOIN BloodGroup bg ON b.BloodGroupId = bg.BloodGroupId";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("BloodDonationId"),
                        resultSet.getInt("DonorId"),
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getString("GroupName"),
                        resultSet.getDate("DonationDate"),
                        resultSet.getString("DonationStatus")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood donation data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteBloodDonationFrame().setVisible(true));
    }
}
