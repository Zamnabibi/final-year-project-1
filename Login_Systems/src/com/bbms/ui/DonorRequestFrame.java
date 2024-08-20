package com.bbms.ui;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class DonorRequestFrame extends JFrame {
    private JTable requestTable;
    private JTextField responseField;
    private JButton sendResponseButton;
    private DefaultTableModel model;

    public DonorRequestFrame() {
        // Frame setup
        setTitle("Donor Request Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        
        // Request Table
        requestTable = new JTable();
        model = new DefaultTableModel(new String[]{"Request ID", "Patient ID", "Blood Group", "Request Message"}, 0);
        requestTable.setModel(model);
        JScrollPane scrollPane = new JScrollPane(requestTable);
        scrollPane.setBounds(20, 20, 550, 200);
        add(scrollPane);

        // Response Field
        JLabel responseLabel = new JLabel("Response:");
        responseLabel.setBounds(20, 240, 100, 25);
        add(responseLabel);

        responseField = new JTextField();
        responseField.setBounds(120, 240, 450, 25);
        add(responseField);

        // Send Response Button
        sendResponseButton = new JButton("Send Response");
        sendResponseButton.setBounds(220, 300, 150, 30);
        add(sendResponseButton);

        // Load data from patientrequest table
        loadRequestData();

        // Send Response Button Action
        sendResponseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendResponse();
            }
        });

        setVisible(true);
    }

    public static class DatabaseConnection {
        public static Connection getConnection() {
            Connection connection = null;
            try {
                // Replace with your database details
                String url = "jdbc:mysql://localhost:3306/blood";
                String user = "root";
                String password = "zamna0";

                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }
    }

    private void loadRequestData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT RequestId, PatientId, BloodGroupId, RequestMessage FROM patientrequests";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String requestId = resultSet.getString("RequestId");
                String patientId = resultSet.getString("PatientId");
                String bloodGroup = resultSet.getString("BloodGroupId");
                String requestMessage = resultSet.getString("RequestMessage");
                model.addRow(new Object[]{requestId, patientId, bloodGroup, requestMessage});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load data from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendResponse() {
        // Get the selected row
        int selectedRow = requestTable.getSelectedRow();
        if (selectedRow >= 0) {
            String requestId = requestTable.getValueAt(selectedRow, 0).toString();
            String patientId = requestTable.getValueAt(selectedRow, 1).toString();
            String response = responseField.getText();

            if (response.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Response cannot be empty.");
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String updateQuery = "UPDATE patientrequests SET ResponseMessage = ? WHERE RequestId = ?";
                PreparedStatement statement = connection.prepareStatement(updateQuery);
                statement.setString(1, response);
                statement.setString(2, requestId);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Response sent to Patient ID: " + patientId);
                    responseField.setText(""); // Clear the response field after sending
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to send response.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a request to respond.");
        }
    }

    public static void main(String[] args) {
        new DonorRequestFrame();
    }
}
