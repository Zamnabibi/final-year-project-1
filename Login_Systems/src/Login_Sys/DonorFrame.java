package Login_Sys;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class DonorFrame extends JFrame {
    private Connection con;
    private JTable donorTable;
    private JTable userDonorTable;
    private JTextField donorIdField;
    private JTextField bloodGroupField;
    private JTextField bloodUnitField;

    public DonorFrame() {
        setTitle("Donor Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");
            System.out.println("Connection established");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Panel for main tables
        JPanel tablePanel = new JPanel(new GridLayout(2, 1));

        // Table for Donor data
        donorTable = new JTable();
        JScrollPane donorTableScrollPane = new JScrollPane(donorTable);
        tablePanel.add(donorTableScrollPane);

        // Table for User data with UserType as Donor
        userDonorTable = new JTable();
        JScrollPane userDonorTableScrollPane = new JScrollPane(userDonorTable);
        tablePanel.add(userDonorTableScrollPane);

        add(tablePanel, BorderLayout.CENTER);

        // Panel for form and buttons
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Donor ID:"));
        donorIdField = new JTextField();
        formPanel.add(donorIdField);
        formPanel.add(new JLabel("Blood Group:"));
        bloodGroupField = new JTextField();
        formPanel.add(bloodGroupField);
        formPanel.add(new JLabel("Blood Unit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        JButton loadButton = new JButton("Load Donors");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDonors();
                loadUserDonors();
            }
        });

        JButton addButton = new JButton("Add Donor");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDonor();
                loadUserDonors(); // Refresh user donor table
            }
        });

        JButton updateButton = new JButton("Update Donor");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDonor();
                loadUserDonors(); // Refresh user donor table
            }
        });

        JButton deleteButton = new JButton("Delete Donor");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteDonor();
                loadUserDonors(); // Refresh user donor table
            }
        });

        JButton bloodGroupButton = new JButton("Blood Groups");
        bloodGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBloodGroups();
            }
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(bloodGroupButton); // Add Blood Groups button
        buttonPanel.add(closeButton); // Add Close button

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data initially
        loadDonors();
        loadUserDonors();

        // Add row selection listener
        donorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && donorTable.getSelectedRow() != -1) {
                    int selectedRow = donorTable.getSelectedRow();
                    donorIdField.setText(donorTable.getValueAt(selectedRow, 0).toString());
                    bloodGroupField.setText(donorTable.getValueAt(selectedRow, 1).toString());
                    bloodUnitField.setText(donorTable.getValueAt(selectedRow, 2).toString());
                }
            }
        });
    }

    private void loadDonors() {
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

    private void loadUserDonors() {
        try {
            String query = "SELECT * FROM User WHERE UserType = 'Donor'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            userDonorTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user donors.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDonor() {
        try {
            String donorId = donorIdField.getText().trim();
            String bloodGroup = bloodGroupField.getText().trim();
            int bloodUnit = Integer.parseInt(bloodUnitField.getText().trim());

            // Check if BloodGroup exists in bloodgroup table
            String checkQuery = "SELECT COUNT(*) FROM bloodgroup WHERE BloodGroup = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setString(1, bloodGroup);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            checkStmt.close();

            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Blood Group does not exist in bloodgroup table.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if DonorId exists in User table
            String userCheckQuery = "SELECT COUNT(*) FROM User WHERE UserId = ?";
            PreparedStatement userCheckStmt = con.prepareStatement(userCheckQuery);
            userCheckStmt.setInt(1, Integer.parseInt(donorId));
            ResultSet userRs = userCheckStmt.executeQuery();
            userRs.next();
            int userCount = userRs.getInt(1);
            userRs.close();
            userCheckStmt.close();

            if (userCount == 0) {
                JOptionPane.showMessageDialog(this, "Donor ID does not exist in User table.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert into Donor table
            String insertQuery = "INSERT INTO Donor (DonorId, BloodGroup, BloodUnit) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertQuery);
            pstmt.setInt(1, Integer.parseInt(donorId));
            pstmt.setString(2, bloodGroup);
            pstmt.setInt(3, bloodUnit);
            pstmt.executeUpdate();
            pstmt.close();

            JOptionPane.showMessageDialog(this, "Donor added successfully.");
            loadDonors(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding donor.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Blood Unit must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDonor() {
        try {
            String donorId = donorIdField.getText();
            String bloodGroup = bloodGroupField.getText();
            int bloodUnit = Integer.parseInt(bloodUnitField.getText());
            String query = "UPDATE Donor SET BloodGroup = ?, BloodUnit = ? WHERE DonorId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, bloodGroup);
            pstmt.setInt(2, bloodUnit);
            pstmt.setInt(3, Integer.parseInt(donorId));
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Donor updated successfully.");
            loadDonors(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating donor.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDonor() {
        try {
            int selectedRow = donorTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "No donor selected.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String donorId = donorTable.getValueAt(selectedRow, 0).toString();
            String query = "DELETE FROM Donor WHERE DonorId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(donorId));
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Donor deleted successfully.");
            loadDonors(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting donor.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBloodGroups() {
        JFrame bloodGroupFrame = new JFrame("Blood Groups");
        bloodGroupFrame.setSize(600, 400);
        bloodGroupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bloodGroupFrame.setLayout(new BorderLayout());

        JTable bloodGroupTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bloodGroupTable);
        bloodGroupFrame.add(scrollPane, BorderLayout.CENTER);

        try {
            String query = "SELECT * FROM bloodgroup";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            bloodGroupTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(bloodGroupFrame, "Error loading blood groups.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        bloodGroupFrame.setVisible(true);
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

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
            DonorFrame frame = new DonorFrame();
            frame.setVisible(true);
        });
    }
}
