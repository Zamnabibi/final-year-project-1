package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RecordManager extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable donorTable, patientTable, historyTable;
    private JTextField searchField;
    private JButton saveButton, deleteButton, displayButton, searchButton;

    private Connection con;

    public RecordManager() {
        setTitle("Record Manager");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        saveButton = new JButton("Donor Record");
        deleteButton = new JButton("Patient Record");
        displayButton = new JButton("Display");
        searchButton = new JButton("Search");

        JPanel inputPanel = new JPanel();
        inputPanel.setBounds(10, 11, 938, 40);
        JLabel lblSearchDonorId = new JLabel("Search Donor/Patient ID: ");
        inputPanel.add(lblSearchDonorId);
        searchField = new JTextField(20);
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        inputPanel.add(saveButton);
        inputPanel.add(deleteButton);
        inputPanel.add(displayButton);
        contentPane.add(inputPanel);

        donorTable = new JTable();
        JScrollPane donorScrollPane = new JScrollPane(donorTable);
        donorScrollPane.setBounds(20, 107, 614, 322);
        contentPane.add(donorScrollPane);

        patientTable = new JTable();
        JScrollPane patientScrollPane = new JScrollPane(patientTable);
        patientScrollPane.setBounds(652, 107, 708, 322);
        contentPane.add(patientScrollPane);

        historyTable = new JTable();
        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyScrollPane.setBounds(10, 489, 1350, 262);
        contentPane.add(historyScrollPane);
        
        JLabel lblNewLabel = new JLabel("History");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblNewLabel.setBounds(10, 440, 105, 38);
        contentPane.add(lblNewLabel);
        
        JLabel lblDonor = new JLabel("Donor");
        lblDonor.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblDonor.setBounds(10, 62, 105, 38);
        contentPane.add(lblDonor);
        
        JLabel lblPatient = new JLabel("Patient");
        lblPatient.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblPatient.setBounds(650, 62, 105, 38);
        contentPane.add(lblPatient);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for saving records
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic for deleting records
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayDonorRecords();
                displayPatientRecords();
                displayHistoryRecords();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchRecord();
            }
        });
    }

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
    }

    private void displayPatientRecords() {
        displayRecords("Patient", patientTable);
    }

    private void displayDonorRecords() {
        displayRecords("donor", donorTable);
    }

    private void displayHistoryRecords() {
        displayRecords("history", historyTable);
    }

    private void displayRecords(String tableName, JTable table) {
        try {
            con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData rsmd = rs.getMetaData();
            DefaultTableModel model = new DefaultTableModel();
            int cols = rsmd.getColumnCount();
            String[] colNames = new String[cols];
            for (int i = 0; i < cols; i++) {
                colNames[i] = rsmd.getColumnName(i + 1);
            }
            model.setColumnIdentifiers(colNames);

            while (rs.next()) {
                String[] row = new String[cols];
                for (int i = 0; i < cols; i++) {
                    row[i] = rs.getString(i + 1);
                }
                model.addRow(row);
            }
            table.setModel(model);
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching " + tableName + " records", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void searchRecord() {
        String id = searchField.getText();
        try {
            con = getConnection();
            String query = "SELECT * FROM donor WHERE DonorId = ? UNION SELECT * FROM Patient WHERE PatientId = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, id);
            pst.setString(2, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Record found: \n"
                        + "UserType: " + rs.getString("UserType") + "\n"
                        + "Name: " + rs.getString("Name") + "\n"
                        + "FatherName: " + rs.getString("FatherName") + "\n"
                        + "MotherName: " + rs.getString("MotherName") + "\n"
                        + "DOB: " + rs.getString("DOB") + "\n"
                        + "MobileNo: " + rs.getString("MobileNo") + "\n"
                        + "Gender: " + rs.getString("Gender") + "\n"
                        + "Email: " + rs.getString("Email") + "\n"
                        + "BloodGroup: " + rs.getString("BloodGroup") + "\n"
                        + "BloodUnit: " + rs.getString("BloodUnit") + "\n"
                        + "City: " + rs.getString("City") + "\n"
                        + "Address: " + rs.getString("Address") + "\n");
            } else {
                JOptionPane.showMessageDialog(this, "ID does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            }
            pst.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching record", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RecordManager().setVisible(true);
            }
        });
    }
}
