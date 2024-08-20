package com.bbms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class DeleteHospitalFrame extends JFrame {
    private JComboBox<String> hospitalIdComboBox;
    private JButton deleteButton;
    private JButton closeButton;
    private JTable hospitalTable;
    private DefaultTableModel tableModel;

    public DeleteHospitalFrame() {
        setTitle("Delete Hospital");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color of the frame
        getContentPane().setBackground(Color.PINK);

        // Top Panel for ID selection and buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));
        topPanel.setBackground(Color.PINK); // Set background color of the top panel

        topPanel.add(new JLabel("HospitalId:"));
        hospitalIdComboBox = new JComboBox<>();
        topPanel.add(hospitalIdComboBox);

        deleteButton = new JButton("Delete");
        topPanel.add(deleteButton);

        closeButton = new JButton("Close");
        topPanel.add(closeButton);

        add(topPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel();
        hospitalTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(hospitalTable);

        // Set background color of the table's viewport
        tableScrollPane.getViewport().setBackground(Color.PINK);

        add(tableScrollPane, BorderLayout.CENTER);

        tableModel.addColumn("HospitalId");
        tableModel.addColumn("HospitalName");
        tableModel.addColumn("Address");
        tableModel.addColumn("City");
        tableModel.addColumn("UserType");
        tableModel.addColumn("Name");
        tableModel.addColumn("ContactNo");

        // Load data
        loadHospitalIds();
        loadHospitalData();

        // Action Listeners
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHospital();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void loadHospitalIds() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT HospitalId FROM Hospital";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            hospitalIdComboBox.removeAllItems();
            while (resultSet.next()) {
                hospitalIdComboBox.addItem(resultSet.getString("HospitalId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadHospitalData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT HospitalId, HospitalName, Address, City, UserType, Name, ContactNo FROM Hospital";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0);
            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("HospitalId"),
                        resultSet.getString("HospitalName"),
                        resultSet.getString("Address"),
                        resultSet.getString("City"),
                        resultSet.getString("UserType"),
                        resultSet.getString("Name"),
                        resultSet.getString("ContactNo")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteHospital() {
        String selectedHospitalId = (String) hospitalIdComboBox.getSelectedItem();
        if (selectedHospitalId == null) {
            JOptionPane.showMessageDialog(this, "Please select a hospital ID.");
            return;
        }
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM Hospital WHERE HospitalId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedHospitalId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Hospital deleted successfully.");
                loadHospitalIds();
                loadHospitalData(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, "Hospital not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteHospitalFrame().setVisible(true));
    }
}
