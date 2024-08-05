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
public class PatientFrame extends JFrame {
    private Connection con;
    private JTable patientTable;
    private JTextField patientIdField;
    private JTextField bloodGroupField;
    private JTextField hospitalIdField;

    public PatientFrame() {
        setTitle("Patient Management");
        setSize(907, 600);
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

        // Table for data
        patientTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(patientTable);
        getContentPane().add(tableScrollPane, BorderLayout.CENTER);

        // Panel for form and buttons
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Patient ID:"));
        patientIdField = new JTextField();
        formPanel.add(patientIdField);
        formPanel.add(new JLabel("Blood Group:"));
        bloodGroupField = new JTextField();
        formPanel.add(bloodGroupField);
        formPanel.add(new JLabel("Hospital ID:"));
        hospitalIdField = new JTextField();
        formPanel.add(hospitalIdField);

        JButton loadPatientButton = new JButton("Load Patients");
        loadPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPatients();
            }
        });

        JButton loadBloodGroupButton = new JButton("Load Blood Groups");
        loadBloodGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBloodGroups();
            }
        });

        JButton loadHospitalButton = new JButton("Load Hospitals");
        loadHospitalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadHospitals();
            }
        });

        JButton loadUserPatientsButton = new JButton("Load User Patients");
        loadUserPatientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUserPatients();
            }
        });

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        JButton updateButton = new JButton("Update Patient");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePatient();
            }
        });

        JButton deleteButton = new JButton("Delete Patient");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePatient();
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
        buttonPanel.add(loadPatientButton);
        buttonPanel.add(loadBloodGroupButton);
        buttonPanel.add(loadHospitalButton);
        buttonPanel.add(loadUserPatientsButton); // Add new button for user patients
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton); // Add close button

        getContentPane().add(formPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Load data initially
        loadPatients(); // Load patient data for the table

        // Add ListSelectionListener to table
        patientTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = patientTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Get data from the selected row
                        String patientId = patientTable.getValueAt(selectedRow, 0).toString();
                        String bloodGroup = patientTable.getValueAt(selectedRow, 1).toString();
                        String hospitalId = patientTable.getValueAt(selectedRow, 2).toString();

                        // Set data to text fields
                        patientIdField.setText(patientId);
                        bloodGroupField.setText(bloodGroup);
                        hospitalIdField.setText(hospitalId);
                    }
                }
            }
        });
    }

    private void loadPatients() {
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

    private void loadBloodGroups() {
        try {
            String query = "SELECT * FROM BloodGroup";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            patientTable.setModel(buildTableModel(rs));
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
            patientTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading hospitals.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUserPatients() {
        try {
            String query = "SELECT * FROM User WHERE UserType = 'Patient'";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            patientTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user patients.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPatient() {
        try {
            String patientId = patientIdField.getText();
            String bloodGroup = bloodGroupField.getText();
            String hospitalId = hospitalIdField.getText();

            // Debugging: Check the values
            System.out.println("PatientId: " + patientId);
            System.out.println("BloodGroup: " + bloodGroup);
            System.out.println("HospitalId: " + hospitalId);

            // Check if the hospitalId exists
            String checkHospitalQuery = "SELECT COUNT(*) FROM Hospital WHERE HospitalId = ?";
            PreparedStatement checkHospitalStmt = con.prepareStatement(checkHospitalQuery);
            checkHospitalStmt.setInt(1, Integer.parseInt(hospitalId));
            ResultSet rs = checkHospitalStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Hospital ID does not exist.", "Data Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "INSERT INTO Patient (PatientId, BloodGroup, HospitalId) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.setString(2, bloodGroup);
            pstmt.setInt(3, Integer.parseInt(hospitalId));
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Patient added successfully.");
            loadPatients(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding patient.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid input for Patient ID or Hospital ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatient() {
        try {
            String patientId = patientIdField.getText();
            String bloodGroup = bloodGroupField.getText();
            String hospitalId = hospitalIdField.getText();

            String query = "UPDATE Patient SET BloodGroup = ?, HospitalId = ? WHERE PatientId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, bloodGroup);
            pstmt.setInt(2, Integer.parseInt(hospitalId));
            pstmt.setInt(3, Integer.parseInt(patientId));
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Patient updated successfully.");
            loadPatients(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating patient.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid input for Patient ID or Hospital ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        try {
            String patientId = patientIdField.getText();

            String query = "DELETE FROM Patient WHERE PatientId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Patient deleted successfully.");
            loadPatients(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting patient.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Invalid input for Patient ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PatientFrame().setVisible(true);
            }
        });
    }
}
