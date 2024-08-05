package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Calendar;
import java.util.Vector;

@SuppressWarnings("serial")
public class BloodRequestFrame extends JFrame {
    private Connection con;
    private JTable requestTable;
    private JTextField requestIdField;
    private JTextField patientIdField;
    private JTextField bloodGroupField;
    private JTextField bloodUnitField;
    private JTextField hospitalIdField;
    private JSpinner requestDateSpinner;

    public BloodRequestFrame() {
        setTitle("Blood Request Management");
        setSize(1022, 614);
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

        // Table for Blood Request data
        requestTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(requestTable);
        getContentPane().add(tableScrollPane, BorderLayout.CENTER);

        // Panel for form and buttons
        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        formPanel.add(new JLabel("Request ID:"));
        requestIdField = new JTextField();
        formPanel.add(requestIdField);

        formPanel.add(new JLabel("Patient ID:"));
        patientIdField = new JTextField();
        formPanel.add(patientIdField);

        formPanel.add(new JLabel("Request Date:"));
        Calendar calendar = Calendar.getInstance();
        requestDateSpinner = new JSpinner(new SpinnerDateModel(calendar.getTime(), null, null, Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(requestDateSpinner, "yyyy-MM-dd");
        requestDateSpinner.setEditor(dateEditor);
        formPanel.add(requestDateSpinner);

        formPanel.add(new JLabel("Blood Group:"));
        bloodGroupField = new JTextField();
        formPanel.add(bloodGroupField);

        formPanel.add(new JLabel("Blood Unit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        formPanel.add(new JLabel("Hospital ID:"));
        hospitalIdField = new JTextField();
        formPanel.add(hospitalIdField);

        JButton loadRequestButton = new JButton("Load Requests");
        loadRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRequests();
            }
        });

        JButton loadPatientButton = new JButton("Load Patients");
        loadPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPatients();
            }
        });

        JButton loadHospitalButton = new JButton("Load Hospitals");
        loadHospitalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadHospitals();
            }
        });

        JButton loadBloodGroupButton = new JButton("Load Blood Groups");
        loadBloodGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBloodGroups();
            }
        });

        JButton addButton = new JButton("Add Request");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRequest();
            }
        });

        JButton updateButton = new JButton("Update Request");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRequest();
            }
        });

        JButton deleteButton = new JButton("Delete Request");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRequest();
            }
        });

        // New Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadRequestButton);
        buttonPanel.add(loadPatientButton);
        buttonPanel.add(loadHospitalButton);
        buttonPanel.add(loadBloodGroupButton); // Add blood group button
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton); // Add close button

        getContentPane().add(formPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Load data initially
        loadRequests(); // Load request data for the table
    }

    private void loadRequests() {
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

    private void loadPatients() {
        try {
            String query = "SELECT * FROM Patient";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            requestTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHospitals() {
        try {
            String query = "SELECT * FROM Hospital";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            requestTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading hospitals.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBloodGroups() {
        try {
            String query = "SELECT * FROM BloodGroup";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            requestTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood groups.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRequest() {
        try {
            int patientId = Integer.parseInt(patientIdField.getText());
            Date requestDate = new Date(((java.util.Date) requestDateSpinner.getValue()).getTime());
            String bloodGroup = bloodGroupField.getText();
            int bloodUnit = Integer.parseInt(bloodUnitField.getText());
            int hospitalId = Integer.parseInt(hospitalIdField.getText());

            String query = "INSERT INTO BloodRequest (PatientId, RequestDate, BloodGroup, BloodUnit, HospitalId) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, patientId);
            pstmt.setDate(2, requestDate);
            pstmt.setString(3, bloodGroup);
            pstmt.setInt(4, bloodUnit);
            pstmt.setInt(5, hospitalId);
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Request added successfully.");
            loadRequests(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding request.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRequest() {
        try {
            int requestId = Integer.parseInt(requestIdField.getText());
            int patientId = Integer.parseInt(patientIdField.getText());
            Date requestDate = new Date(((java.util.Date) requestDateSpinner.getValue()).getTime());
            String bloodGroup = bloodGroupField.getText();
            int bloodUnit = Integer.parseInt(bloodUnitField.getText());
            int hospitalId = Integer.parseInt(hospitalIdField.getText());

            String query = "UPDATE BloodRequest SET PatientId = ?, RequestDate = ?, BloodGroup = ?, BloodUnit = ?, HospitalId = ? WHERE RequestId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, patientId);
            pstmt.setDate(2, requestDate);
            pstmt.setString(3, bloodGroup);
            pstmt.setInt(4, bloodUnit);
            pstmt.setInt(5, hospitalId);
            pstmt.setInt(6, requestId);
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Request updated successfully.");
            loadRequests(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating request.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRequest() {
        try {
            int requestId = Integer.parseInt(requestIdField.getText());
            String query = "DELETE FROM BloodRequest WHERE RequestId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, requestId);
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Request deleted successfully.");
            loadRequests(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting request.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
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
            BloodRequestFrame frame = new BloodRequestFrame();
            frame.setVisible(true);
        });
    }
}
