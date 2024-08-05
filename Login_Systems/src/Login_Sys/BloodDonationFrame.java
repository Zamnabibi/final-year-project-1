package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Date;
import java.util.Vector;

@SuppressWarnings("serial")
public class BloodDonationFrame extends JFrame {
    private Connection con;
    private JTable donationTable;
    private JTextField donationIdField;
    private JTextField donorIdField;
    private JTextField bloodGroupField;
    private JTextField bloodUnitField;
    private JTextField hospitalIdField;
    private JSpinner donationDateSpinner;

    public BloodDonationFrame() {
        setTitle("Blood Donation Management");
        setSize(1200, 600); // Increased width to accommodate new tables
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");
            System.out.println("Connection established");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Table for Blood Donation data
        donationTable = new JTable();
        JScrollPane donationTableScrollPane = new JScrollPane(donationTable);
        getContentPane().add(donationTableScrollPane, BorderLayout.CENTER);

        // Panel for form and buttons
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("Donation ID:"));
        donationIdField = new JTextField();
        formPanel.add(donationIdField);

        formPanel.add(new JLabel("Donor ID:"));
        donorIdField = new JTextField();
        formPanel.add(donorIdField);

        formPanel.add(new JLabel("Donation Date:"));
        donationDateSpinner = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(donationDateSpinner, "yyyy-MM-dd");
        donationDateSpinner.setEditor(dateEditor);
        formPanel.add(donationDateSpinner);

        formPanel.add(new JLabel("Blood Group:"));
        bloodGroupField = new JTextField();
        formPanel.add(bloodGroupField);

        formPanel.add(new JLabel("Blood Unit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        formPanel.add(new JLabel("Hospital ID:"));
        hospitalIdField = new JTextField();
        formPanel.add(hospitalIdField);

        JButton loadDonationsButton = new JButton("Load Donations");
        loadDonationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDonations();
            }
        });

        JButton addButton = new JButton("Add Donation");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDonation();
            }
        });

        JButton updateButton = new JButton("Update Donation");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDonation();
            }
        });

        JButton deleteButton = new JButton("Delete Donation");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteDonation();
            }
        });

        JButton loadDonorsButton = new JButton("Load Donors");
        loadDonorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDonors();
            }
        });

        JButton loadBloodGroupsButton = new JButton("Load Blood Groups");
        loadBloodGroupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBloodGroups();
            }
        });

        JButton loadHospitalsButton = new JButton("Load Hospitals");
        loadHospitalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadHospitals();
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadDonorsButton);
        buttonPanel.add(loadBloodGroupsButton);
        buttonPanel.add(loadHospitalsButton);
        buttonPanel.add(loadDonationsButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        getContentPane().add(formPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Load data initially
        loadDonations();
    }

    private void loadDonations() {
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

    private void loadDonors() {
        try {
            String query = "SELECT * FROM Donor";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            displayTable("Donors", rs);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading donors.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBloodGroups() {
        try {
            String query = "SELECT * FROM BloodGroup";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            displayTable("Blood Groups", rs);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood groups.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHospitals() {
        try {
            String query = "SELECT * FROM Hospital";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            displayTable("Hospitals", rs);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading hospitals.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayTable(String title, ResultSet rs) throws SQLException {
        JTable table = new JTable(buildTableModel(rs));
        JScrollPane scrollPane = new JScrollPane(table);
        JDialog dialog = new JDialog(this, title, true);
        dialog.getContentPane().add(scrollPane);
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addDonation() {
        // Retrieve form fields
        String donorId = donorIdField.getText().trim();
        Date donationDate = (Date) donationDateSpinner.getValue();
        String bloodGroup = bloodGroupField.getText().trim();
        int bloodUnit = Integer.parseInt(bloodUnitField.getText().trim());
        String hospitalId = hospitalIdField.getText().trim();

        try {
            String query = "INSERT INTO BloodDonation (DonorId, DonationDate, BloodGroup, BloodUnit, HospitalId) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(donorId));
            pstmt.setDate(2, new java.sql.Date(donationDate.getTime()));
            pstmt.setString(3, bloodGroup);
            pstmt.setInt(4, bloodUnit);
            pstmt.setInt(5, Integer.parseInt(hospitalId));
            pstmt.executeUpdate();
            pstmt.close();
            loadDonations();
            JOptionPane.showMessageDialog(this, "Donation added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding donation.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDonation() {
        // Retrieve form fields
        int donationId = Integer.parseInt(donationIdField.getText().trim());
        String donorId = donorIdField.getText().trim();
        Date donationDate = (Date) donationDateSpinner.getValue();
        String bloodGroup = bloodGroupField.getText().trim();
        int bloodUnit = Integer.parseInt(bloodUnitField.getText().trim());
        String hospitalId = hospitalIdField.getText().trim();

        try {
            String query = "UPDATE BloodDonation SET DonorId = ?, DonationDate = ?, BloodGroup = ?, BloodUnit = ?, HospitalId = ? WHERE DonationId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(donorId));
            pstmt.setDate(2, new java.sql.Date(donationDate.getTime()));
            pstmt.setString(3, bloodGroup);
            pstmt.setInt(4, bloodUnit);
            pstmt.setInt(5, Integer.parseInt(hospitalId));
            pstmt.setInt(6, donationId);
            pstmt.executeUpdate();
            pstmt.close();
            loadDonations();
            JOptionPane.showMessageDialog(this, "Donation updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating donation.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDonation() {
        int donationId = Integer.parseInt(donationIdField.getText().trim());

        try {
            String query = "DELETE FROM BloodDonation WHERE DonationId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, donationId);
            pstmt.executeUpdate();
            pstmt.close();
            loadDonations();
            JOptionPane.showMessageDialog(this, "Donation deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting donation.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BloodDonationFrame().setVisible(true);
            }
        });
    }
}
