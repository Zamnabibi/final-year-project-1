package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class RecordManager extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable donorTable, patientTable, historyTable, stockTable;
    private JTextField donorSearchField, patientSearchField;
    private JButton searchDonorButton, searchPatientButton, displayDonorButton, displayPatientButton, displayHistoryButton, displayStockButton, deleteButton, closeButton;
    private Connection con;
    private String selectedDonorId;
    private String selectedPatientId;

    public RecordManager() {
        setTitle("Record Manager");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPane = new JPanel();
        contentPane.setBackground(Color.PINK); // Set background color to pink
        setContentPane(contentPane);
        contentPane.setLayout(null);

        donorTable = new JTable();
        JScrollPane donorScrollPane = new JScrollPane(donorTable);
        donorScrollPane.setBounds(20, 107, 635, 322);
        contentPane.add(donorScrollPane);

        patientTable = new JTable();
        JScrollPane patientScrollPane = new JScrollPane(patientTable);
        patientScrollPane.setBounds(674, 107, 653, 322);
        contentPane.add(patientScrollPane);

        historyTable = new JTable();
        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        historyScrollPane.setBounds(10, 489, 645, 249);
        contentPane.add(historyScrollPane);

        stockTable = new JTable();
        JScrollPane stockScrollPane = new JScrollPane(stockTable);
        stockScrollPane.setBounds(674, 489, 653, 249);
        contentPane.add(stockScrollPane);

        JLabel lblDonor = new JLabel("Donor");
        lblDonor.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblDonor.setBounds(10, 62, 105, 38);
        contentPane.add(lblDonor);

        JLabel lblPatient = new JLabel("Patient");
        lblPatient.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblPatient.setBounds(674, 62, 105, 38);
        contentPane.add(lblPatient);

        JLabel lblHistory = new JLabel("History");
        lblHistory.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblHistory.setBounds(10, 440, 105, 38);
        contentPane.add(lblHistory);

        JLabel lblStock = new JLabel("Stock");
        lblStock.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblStock.setBounds(674, 440, 105, 38);
        contentPane.add(lblStock);

        JLabel lblDonorSearch = new JLabel("Search Donor ID:");
        lblDonorSearch.setBounds(38, 11, 125, 14);
        contentPane.add(lblDonorSearch);

        donorSearchField = new JTextField(10);
        donorSearchField.setBounds(135, 7, 105, 20);
        contentPane.add(donorSearchField);

        searchDonorButton = new JButton("Search Donor ID");
        searchDonorButton.setBounds(271, 7, 164, 27);
        contentPane.add(searchDonorButton);

        JLabel lblPatientSearch = new JLabel("Search Patient ID:");
        lblPatientSearch.setBounds(29, 38, 125, 14);
        contentPane.add(lblPatientSearch);

        patientSearchField = new JTextField(10);
        patientSearchField.setBounds(135, 34, 105, 20);
        contentPane.add(patientSearchField);

        searchPatientButton = new JButton("Search Patient ID");
        searchPatientButton.setBounds(271, 37, 164, 27);
        contentPane.add(searchPatientButton);

        displayDonorButton = new JButton("Display Donor Records");
        displayDonorButton.setBounds(486, 4, 199, 27);
        contentPane.add(displayDonorButton);

        displayPatientButton = new JButton("Display Patient Records");
        displayPatientButton.setBounds(490, 35, 199, 27);
        contentPane.add(displayPatientButton);

        displayHistoryButton = new JButton("Display History Records");
        displayHistoryButton.setBounds(698, 3, 210, 27);
        contentPane.add(displayHistoryButton);

        displayStockButton = new JButton("Display Stock Records");
        displayStockButton.setBounds(698, 35, 210, 27);
        contentPane.add(displayStockButton);

        deleteButton = new JButton("Delete Record");
        deleteButton.setBounds(926, 33, 156, 29);
        contentPane.add(deleteButton);

        closeButton = new JButton("Close");
        closeButton.setBounds(926, 3, 156, 28);
        contentPane.add(closeButton);

        closeButton.addActionListener(e -> setVisible(false));

        deleteButton.addActionListener(e -> deleteRecord());

        displayDonorButton.addActionListener(e -> displayDonorRecords());

        displayPatientButton.addActionListener(e -> displayPatientRecords());

        displayHistoryButton.addActionListener(e -> displayHistoryRecords());

        displayStockButton.addActionListener(e -> displayStockRecords());

        searchDonorButton.addActionListener(e -> searchDonorRecord());

        searchPatientButton.addActionListener(e -> searchPatientRecord());

        donorTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = donorTable.getSelectedRow();
            if (selectedRow != -1) {
                selectedDonorId = donorTable.getValueAt(selectedRow, 0).toString();
                selectedPatientId = null; // Clear patient selection
            }
        });

        patientTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow != -1) {
                selectedPatientId = patientTable.getValueAt(selectedRow, 0).toString();
                selectedDonorId = null; // Clear donor selection
            }
        });
    }

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
    }

    private void displayDonorRecords() {
        displayRecords("Donor", donorTable);
    }

    private void displayPatientRecords() {
        displayRecords("Patient", patientTable);
    }

    private void displayHistoryRecords() {
        displayRecords("History", historyTable);
    }

    private void displayStockRecords() {
        displayRecords("Stock", stockTable);
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
            for (int i = 1; i <= cols; i++) {
                colNames[i - 1] = rsmd.getColumnName(i);
            }
            model.setColumnIdentifiers(colNames);
            while (rs.next()) {
                Object[] rowData = new Object[cols];
                for (int i = 1; i <= cols; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }
            table.setModel(model);
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void searchDonorRecord() {
        searchRecord("Donor", donorSearchField.getText(), donorTable, "DonorId");
    }

    private void searchPatientRecord() {
        searchRecord("Patient", patientSearchField.getText(), patientTable, "PatientId");
    }

    private void searchRecord(String tableName, String searchText, JTable table, String idColumn) {
        try {
            con = getConnection();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?");
            pst.setString(1, searchText);
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            DefaultTableModel model = new DefaultTableModel();
            int cols = rsmd.getColumnCount();
            String[] colNames = new String[cols];
            for (int i = 1; i <= cols; i++) {
                colNames[i - 1] = rsmd.getColumnName(i);
            }
            model.setColumnIdentifiers(colNames);
            while (rs.next()) {
                Object[] rowData = new Object[cols];
                for (int i = 1; i <= cols; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }
            table.setModel(model);
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteRecord() {
        if (selectedDonorId != null) {
            deleteRecord("Donor", "DonorId", selectedDonorId);
        } else if (selectedPatientId != null) {
            deleteRecord("Patient", "PatientId", selectedPatientId);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.");
        }
    }

    private void deleteRecord(String tableName, String idColumn, String id) {
        try {
            con = getConnection();
            String insertHistoryQuery = "INSERT INTO History (UserId, UserType, Name, FatherName, MotherName, DOB, MobileNo, Gender, Email, BloodGroup, BloodUnit, City, Address, DeletedAt) " +
                    "SELECT " + idColumn + ", ?, Name, FatherName, MotherName, DOB, MobileNo, Gender, Email, BloodGroup, BloodUnit, City, Address, NOW() " +
                    "FROM " + tableName + " WHERE " + idColumn + " = ?";
            PreparedStatement insertHistoryPst = con.prepareStatement(insertHistoryQuery);
            insertHistoryPst.setString(1, tableName); // Setting UserType as Donor or Patient
            insertHistoryPst.setString(2, id);
            insertHistoryPst.executeUpdate();

            String deleteQuery = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";
            PreparedStatement deletePst = con.prepareStatement(deleteQuery);
            deletePst.setString(1, id);
            deletePst.executeUpdate();

            con.close();
            if (tableName.equals("Donor")) {
                displayDonorRecords();
            } else if (tableName.equals("Patient")) {
                displayPatientRecords();
            }
            JOptionPane.showMessageDialog(this, "Record deleted successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                RecordManager frame = new RecordManager();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
