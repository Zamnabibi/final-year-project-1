package Login_Sys;

import javax.swing.*;
import java.awt.*;

import java.sql.*;
import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class DonorHomePage extends AdminHomePage {
    
    public DonorHomePage() {
        super("Donor Home Page");
    }

    @Override
    protected void initializeUI() {
        // Ensure content pane is set properly
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        contentPane.add(footerPanel, BorderLayout.SOUTH);

        // Set up time label
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        JPanel timePanel = new JPanel();
        timePanel.add(timeLabel);
        contentPane.add(timePanel, BorderLayout.NORTH);

        // Set up layout
        setLayout(new BorderLayout());

        // Set up the table
        JScrollPane scrollPane = new JScrollPane(requestsTable);
        contentPane.add(scrollPane, BorderLayout.CENTER);

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
        // Clear the existing rows
        tableModel.setRowCount(0);

        // Fetch requests from the database and populate the table
        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String password = "zamna0";

        String query = "SELECT * FROM donors WHERE type = 'Donor'";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int DonorId = rs.getInt("donorid");
                String UserName = rs.getString("username");
                String Password = rs.getString("password");
                String Email = rs.getString("email");
                String FullName = rs.getString("fullname");
                String Status = rs.getString("status");
                String Type = rs.getString("type");

                tableModel.addRow(new Object[]{DonorId, UserName, Password, Email, FullName, Status, Type});
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
                int DonorId = (int) tableModel.getValueAt(selectedRow, 0);
                handleRequest(DonorId, "Accepted");
                loadRequests();
            } else {
                JOptionPane.showMessageDialog(DonorHomePage.this, "Please select a request to accept.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        rejectButton.addActionListener(e -> {
            int selectedRow = requestsTable.getSelectedRow();
            if (selectedRow >= 0) {
                int DonorId = (int) tableModel.getValueAt(selectedRow, 0);
                handleRequest(DonorId, "Rejected");
                loadRequests();
            } else {
                JOptionPane.showMessageDialog(DonorHomePage.this, "Please select a request to reject.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void handleRequest(int DonorId, String status) {
        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String password = "zamna0";

        String query = "UPDATE donors SET Status = ? WHERE DonorId = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, DonorId);

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

    protected void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DonorHomePage().setVisible(true));
    }
}
