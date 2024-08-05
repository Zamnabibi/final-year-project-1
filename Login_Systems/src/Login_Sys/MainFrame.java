package Login_Sys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private Connection con;

    public MainFrame() {
        setTitle("Blood Bank Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            // Initialize database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");
            System.out.println("Connection created");

            // Create a tabbed pane
            tabbedPane = new JTabbedPane();

            // Add tabs
            tabbedPane.addTab("Donor Management", createDonorPanel());
            tabbedPane.addTab("Patient Management", createPatientPanel());
            tabbedPane.addTab("Admin Management", createAdminPanel());
            tabbedPane.addTab("Blood Donation Records", createBloodDonationPanel());
            tabbedPane.addTab("Blood Request Records", createBloodRequestPanel());
            tabbedPane.addTab("Blood Stock Management", createBloodStockPanel());
            tabbedPane.addTab("History Records", createHistoryPanel());

            add(tabbedPane, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createDonorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable donorTable = new JTable();
        panel.add(new JScrollPane(donorTable), BorderLayout.CENTER);
        JButton loadDonorsButton = new JButton("Load Donors");
        loadDonorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load Donors button clicked");
                loadDonors(donorTable);
            }
        });
        panel.add(loadDonorsButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable patientTable = new JTable();
        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);
        JButton loadPatientsButton = new JButton("Load Patients");
        loadPatientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load Patients button clicked");
                loadPatients(patientTable);
            }
        });
        panel.add(loadPatientsButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable adminTable = new JTable();
        panel.add(new JScrollPane(adminTable), BorderLayout.CENTER);
        JButton loadAdminsButton = new JButton("Load Admins");
        loadAdminsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load Admins button clicked");
                loadAdmins(adminTable);
            }
        });
        panel.add(loadAdminsButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createBloodDonationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable donationTable = new JTable();
        panel.add(new JScrollPane(donationTable), BorderLayout.CENTER);
        JButton loadDonationsButton = new JButton("Load Donations");
        loadDonationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load Donations button clicked");
                loadDonations(donationTable);
            }
        });
        panel.add(loadDonationsButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createBloodRequestPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable requestTable = new JTable();
        panel.add(new JScrollPane(requestTable), BorderLayout.CENTER);
        JButton loadRequestsButton = new JButton("Load Requests");
        loadRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load Requests button clicked");
                loadRequests(requestTable);
            }
        });
        panel.add(loadRequestsButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createBloodStockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable stockTable = new JTable();
        panel.add(new JScrollPane(stockTable), BorderLayout.CENTER);
        JButton loadStockButton = new JButton("Load Stock");
        loadStockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load Stock button clicked");
                loadStock(stockTable);
            }
        });
        panel.add(loadStockButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable historyTable = new JTable();
        panel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        JButton loadHistoryButton = new JButton("Load History");
        loadHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load History button clicked");
                loadHistory(historyTable);
            }
        });
        panel.add(loadHistoryButton, BorderLayout.SOUTH);
        return panel;
    }

    private void loadDonors(JTable donorTable) {
        try {
            String query = "SELECT * FROM Donor";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            donorTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading donors.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPatients(JTable patientTable) {
        try {
            String query = "SELECT * FROM Patient";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            patientTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAdmins(JTable adminTable) {
        try {
            String query = "SELECT * FROM Admin";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            adminTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading admins.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDonations(JTable donationTable) {
        try {
            String query = "SELECT * FROM BloodDonation";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            donationTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading donations.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRequests(JTable requestTable) {
        try {
            String query = "SELECT * FROM BloodRequest";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            requestTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading requests.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStock(JTable stockTable) {
        try {
            String query = "SELECT * FROM BloodStock";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            stockTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading stock.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHistory(JTable historyTable) {
        try {
            String query = "SELECT * FROM History";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            historyTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading history.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Utility method to convert ResultSet to TableModel
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Column names
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        // Data rows
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
