package com.bbms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

@SuppressWarnings("serial")
public class PatientRequest extends JFrame {

    private JComboBox<String> bloodGroupComboBox;
    private JTextArea requestTextArea;
    private JButton sendRequestButton;
    private JButton viewRequestsButton;
    private JTable requestsTable;
    private JLabel statusLabel;
    private int patientId;

    public PatientRequest(int patientId) {
        this.patientId = patientId;

        setTitle("Patient Request Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Patient Request Management"));
        add(headerPanel, BorderLayout.NORTH);

        // Request Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 1));

        JPanel bloodGroupPanel = new JPanel();
        bloodGroupPanel.add(new JLabel("Select Blood Group:"));
        bloodGroupComboBox = new JComboBox<>(loadBloodGroups());
        bloodGroupPanel.add(bloodGroupComboBox);
        formPanel.add(bloodGroupPanel);

        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new BorderLayout());
        requestTextArea = new JTextArea(5, 40);
        requestTextArea.setLineWrap(true);
        requestTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(requestTextArea);
        requestPanel.add(new JLabel("Request Message:"), BorderLayout.NORTH);
        requestPanel.add(scrollPane, BorderLayout.CENTER);
        formPanel.add(requestPanel);

        sendRequestButton = new JButton("Send Request");
        formPanel.add(sendRequestButton);

        add(formPanel, BorderLayout.NORTH);

        // Requests Table Panel
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        requestsTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(requestsTable);
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        viewRequestsButton = new JButton("View My Requests");
        tablePanel.add(viewRequestsButton, BorderLayout.SOUTH);

        add(tablePanel, BorderLayout.CENTER);

        // Status Label
        statusLabel = new JLabel(" ");
        add(statusLabel, BorderLayout.SOUTH);

        // Action Listeners
        sendRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendRequest();
            }
        });

        viewRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRequests();
            }
        });
    }

    private String[] loadBloodGroups() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");
            String query = "SELECT GroupName FROM BloodGroup";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            Vector<String> bloodGroups = new Vector<>();
            while (rs.next()) {
                bloodGroups.add(rs.getString("GroupName"));
            }

            rs.close();
            pstmt.close();
            conn.close();

            return bloodGroups.toArray(new String[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood groups.");
            return new String[0]; // Return an empty array on error
        }
    }

    @SuppressWarnings("resource")
    private void sendRequest() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");

            // Check if PatientId exists in the Patient table
            String checkQuery = "SELECT COUNT(*) FROM Patient WHERE UserId = ?";
            pstmt = conn.prepareStatement(checkQuery);
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                JOptionPane.showMessageDialog(this, "PatientId does not exist.");
                return;
            }

            // Get selected blood group ID and request message
            String selectedBloodGroup = (String) bloodGroupComboBox.getSelectedItem();
            String requestMessage = requestTextArea.getText();
            int bloodGroupId = getBloodGroupId(selectedBloodGroup);

            // Proceed with inserting the request
            String insertQuery = "INSERT INTO PatientRequests (PatientId, BloodGroupId, RequestMessage, Status) " +
                    "VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertQuery);
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, bloodGroupId);
            pstmt.setString(3, requestMessage);
            pstmt.setString(4, "Pending");

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Request sent successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending request.");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private int getBloodGroupId(String groupName) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");
        String query = "SELECT BloodGroupId FROM BloodGroup WHERE GroupName = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, groupName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("BloodGroupId");
        }
        throw new Exception("Blood group not found: " + groupName);
    }

    private void loadRequests() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");
            String query = "SELECT r.RequestId, b.GroupName AS BloodGroup, r.RequestMessage, r.ResponseMessage, r.Status, r.CreatedAt " +
                    "FROM PatientRequests r " +
                    "JOIN BloodGroup b ON r.BloodGroupId = b.BloodGroupId " +
                    "WHERE r.PatientId = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            // Populate table with result set
            requestsTable.setModel(buildTableModel(rs));

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading requests.");
        }
    }

    private static TableModel buildTableModel(ResultSet rs) throws Exception {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        Vector<Vector<Object>> data = new Vector<>();

        // Column names
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Data rows
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.add(rs.getObject(columnIndex));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Example: Initialize frame with patient ID 1
            new PatientRequest(1).setVisible(true);
        });
    }
}
