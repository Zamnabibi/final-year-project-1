package Login_Sys;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

@SuppressWarnings("serial")
public class PatientHomePage extends AdminHomePage {

    public PatientHomePage() {
        super("Patient Home Page");
    }

    @Override
    protected void loadRequests() {
        // Clear the existing rows
        tableModel.setRowCount(0);

        // Fetch requests from the database and populate the table
        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String password = "zamna0";

        String query = "SELECT * FROM users WHERE type = 'Patient'";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int Userid = rs.getInt("userid");
                String Username = rs.getString("username");
                String Password = rs.getString("password"); // Renamed for clarity
                String Email = rs.getString("email");
                String FullName = rs.getString("fullname");
                String Status = rs.getString("status");
                String Type = rs.getString("type");

                tableModel.addRow(new Object[]{Userid, Username, Password, Email, FullName, Status, Type});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void addListeners() {
        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = requestsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int UserId = (int) tableModel.getValueAt(selectedRow, 0);
                    handleRequest(UserId, "Accepted");
                    loadRequests(); // Reload requests after handling
                } else {
                    JOptionPane.showMessageDialog(PatientHomePage.this, "Please select a request to accept.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = requestsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int UserId = (int) tableModel.getValueAt(selectedRow, 0);
                    handleRequest(UserId, "Rejected");
                    loadRequests(); // Reload requests after handling
                } else {
                    JOptionPane.showMessageDialog(PatientHomePage.this, "Please select a request to reject.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void handleRequest(int UserId, String Status) {
        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String password = "zamna0";

        String query = "UPDATE users SET Status = ? WHERE UserId = ?";

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
