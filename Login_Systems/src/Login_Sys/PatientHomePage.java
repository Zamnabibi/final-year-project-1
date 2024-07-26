package Login_Sys;

import javax.swing.*;
import java.awt.*;

import java.sql.*;

@SuppressWarnings("serial")
public class PatientHomePage extends AdminHomePage {

    public PatientHomePage() {
        super("Patient Home Page");
    }

    @Override
    protected void initializeUI() {
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        contentPane.add(footerPanel, BorderLayout.SOUTH);

        // Set up layout
        setLayout(new BorderLayout());

        // Set up the table
        JScrollPane scrollPane = new JScrollPane(requestsTable);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Add time label
        JPanel timePanel = new JPanel();
        timePanel.add(timeLabel);
        contentPane.add(timePanel, BorderLayout.NORTH);

        // Set up the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Set the timer to update the JLabel every second
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();

        // Initial time update
        updateTime();

        // Load initial requests
        loadRequests();

        // Add action listeners
        addListeners();
    }

    @Override
    protected void loadRequests() {
        tableModel.setRowCount(0);

        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String password = "zamna0";

        String query = "SELECT * FROM Users WHERE type = 'Patient'";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int UserId = rs.getInt("userid");
                String UserName = rs.getString("username");
                String Password = rs.getString("password");
                String Email = rs.getString("email");
                String FullName = rs.getString("fullname");
                String Status = rs.getString("status");
                String Type = rs.getString("type");

                tableModel.addRow(new Object[]{UserId, UserName, Password, Email, FullName, Status, Type});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void addListeners() {
        acceptButton.addActionListener(e -> {
            int selectedRow = requestsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int UserId = (int) tableModel.getValueAt(selectedRow, 0);
                handleRequest(UserId, "Accepted");
                loadRequests();
            } else {
                JOptionPane.showMessageDialog(PatientHomePage.this, "Please select a request to accept.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        rejectButton.addActionListener(e -> {
            int selectedRow = requestsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int UserId = (int) tableModel.getValueAt(selectedRow, 0);
                handleRequest(UserId, "Rejected");
                loadRequests();
            } else {
                JOptionPane.showMessageDialog(PatientHomePage.this, "Please select a request to reject.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void handleRequest(int UserId, String Status) {
        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String password = "zamna0";

        String query = "UPDATE Users SET Status = ? WHERE UserId = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, Status);
            stmt.setInt(2, UserId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Request " + Status + " successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update request status", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update request status: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PatientHomePage().setVisible(true));
    }
}
