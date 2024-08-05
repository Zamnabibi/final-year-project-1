package Login_Sys;

import javax.swing.*;
import java.sql.*;

@SuppressWarnings("serial")
public class PatientHomePage extends AdminHomePage {

    public PatientHomePage() {
        super("Patient Home Page");
    }

    @Override
    protected void loadRequests() {
        tableModel.setRowCount(0);

        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String password = "zamna0";

        // Adjusted SQL query to match the Admin table and its fields
        String query = "SELECT * FROM Admin WHERE type = 'Patient'";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int AdminId = rs.getInt("adminId");  // Changed from UserId to adminId
                String UserName = rs.getString("username");
                String Password = rs.getString("password");
                String Email = rs.getString("email");
                String FullName = rs.getString("fullname");
                String Status = rs.getString("status");
                String Type = rs.getString("type");

                tableModel.addRow(new Object[]{AdminId, UserName, Password, Email, FullName, Status, Type});
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
                int AdminId = (int) tableModel.getValueAt(selectedRow, 0);
                handleRequest(AdminId, "Accepted");
                loadRequests();
            } else {
                JOptionPane.showMessageDialog(PatientHomePage.this, "Please select a request to accept.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        rejectButton.addActionListener(e -> {
            int selectedRow = requestsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int AdminId = (int) tableModel.getValueAt(selectedRow, 0);
                handleRequest(AdminId, "Rejected");
                loadRequests();
            } else {
                JOptionPane.showMessageDialog(PatientHomePage.this, "Please select a request to reject.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void handleRequest(int AdminId, String status) {
        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String password = "zamna0";

        // Adjusted SQL query to use adminId
        String query = "UPDATE Admin SET Status = ? WHERE adminId = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, AdminId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Request " + status + " successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
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
