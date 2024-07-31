package Login_Sys;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RecordManager extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable donorTable, patientTable, historyTable, stockTable;
    private JTextField donorSearchField, patientSearchField;
    private JButton donorRecordButton, patientRecordButton, historyRecordButton, stockRecordButton, deleteButton, donorSearchButton, patientSearchButton;
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
        
        JLabel lblHistory = new JLabel("History");
        lblHistory.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblHistory.setBounds(10, 440, 105, 38);
        contentPane.add(lblHistory);
        
        JLabel lblDonor = new JLabel("Donor");
        lblDonor.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblDonor.setBounds(10, 62, 105, 38);
        contentPane.add(lblDonor);
        
        JLabel lblPatient = new JLabel("Patient");
        lblPatient.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblPatient.setBounds(928, 62, 105, 38);
        contentPane.add(lblPatient);
        Image img2 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        
        JLabel lblStock = new JLabel("Stock");
        lblStock.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblStock.setBounds(928, 440, 105, 38);
        contentPane.add(lblStock);
        patientRecordButton = new JButton("Display Patient Records");
        Image img8=new ImageIcon(this.getClass().getResource("/add donor.png")).getImage();
        patientRecordButton.setIcon(new ImageIcon(img8));
        patientRecordButton.setBounds(490, 43, 199, 27);
        contentPane.add(patientRecordButton);
        stockRecordButton = new JButton("Display Stock Records");
        Image img14=new ImageIcon(this.getClass().getResource("/stock.png")).getImage();
        stockRecordButton.setIcon(new ImageIcon(img14));
        stockRecordButton.setBounds(699, 43, 210, 27);
        contentPane.add(stockRecordButton);
        
        JLabel lblPatientSearch = new JLabel("Search Patient ID: ");
        lblPatientSearch.setBounds(85, 47, 125, 14);
        contentPane.add(lblPatientSearch);
        patientSearchField = new JTextField(10);
        patientSearchField.setBounds(201, 44, 105, 20);
        contentPane.add(patientSearchField);
        patientSearchButton = new JButton("Search Patient ID");
        
        patientSearchButton.setBounds(316, 43, 164, 27);
        contentPane.add(patientSearchButton);
        Image img5=new ImageIcon(this.getClass().getResource("/search user.jpg")).getImage();
		patientSearchButton.setIcon(new ImageIcon(img5));
        JButton btnNewButton_3 = new JButton("Close");
        btnNewButton_3.setBounds(928, 11, 156, 27);
        contentPane.add(btnNewButton_3);
        btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btnNewButton_3.setIcon(new ImageIcon(img2));
        
        donorSearchField = new JTextField(10);
		donorSearchField.setBounds(201, 16, 105, 20);
		contentPane.add(donorSearchField);
		donorSearchButton = new JButton("Search Donor ID");
		Image img4=new ImageIcon(this.getClass().getResource("/search user.jpg")).getImage();
		donorSearchButton.setIcon(new ImageIcon(img4));
		donorSearchButton.setBounds(316, 11, 164, 27);
		contentPane.add(donorSearchButton);
		
		        donorRecordButton = new JButton("Display Donor Records");
		        Image img6=new ImageIcon(this.getClass().getResource("/add donor.png")).getImage();
		        donorRecordButton.setIcon(new ImageIcon(img6));
		        donorRecordButton.setBounds(490, 11, 199, 27);
		        contentPane.add(donorRecordButton);
		        historyRecordButton = new JButton("Display History Records");
		        Image img12=new ImageIcon(this.getClass().getResource("/details.png")).getImage();
		        historyRecordButton.setIcon(new ImageIcon(img12));
		        historyRecordButton.setBounds(699, 11, 210, 27);
		        contentPane.add(historyRecordButton);
		        deleteButton = new JButton("Delete Record");
		        Image img19=new ImageIcon(this.getClass().getResource("/stock.png")).getImage();
		        deleteButton.setIcon(new ImageIcon(img19));
		        deleteButton.setBounds(928, 45, 156, 25);
		        contentPane.add(deleteButton);
		        
		        JLabel lblDonorSearch = new JLabel("Search Donor ID: ");
				lblDonorSearch.setBounds(85, 19, 95, 14);
				contentPane.add(lblDonorSearch);
        
        JLabel lblNewLabel = new JLabel("");
		Image img145=new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img145));
		lblNewLabel.setBounds(806, 234, 834, 527);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1= new JLabel("");
		Image img146=new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
		lblNewLabel_1.setIcon(new ImageIcon(img146));
		lblNewLabel_1.setBounds(647, -13, 834, 527);
		contentPane.add(lblNewLabel_1);
        
		JLabel lblNewLabel_2= new JLabel("");
		Image img1=new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
		lblNewLabel_2.setIcon(new ImageIcon(img1));
		lblNewLabel_2.setBounds(-13, 249, 834, 527);
		contentPane.add(lblNewLabel_2);
        
		JLabel lblNewLabel_3= new JLabel("");
		Image img3=new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
		lblNewLabel_3.setIcon(new ImageIcon(img3));
		lblNewLabel_3.setBounds(0, -13, 834, 527);
		contentPane.add(lblNewLabel_3);
		
		
		        
		                deleteButton.addActionListener(new ActionListener() {
		                    @Override
		                    public void actionPerformed(ActionEvent e) {
		                        deleteRecord();
		                    }
		                });
		        
		                historyRecordButton.addActionListener(new ActionListener() {
		                    @Override
		                    public void actionPerformed(ActionEvent e) {
		                        displayHistoryRecords();
		                    }
		                });
		        
		                donorRecordButton.addActionListener(new ActionListener() {
		                    @Override
		                    public void actionPerformed(ActionEvent e) {
		                        displayDonorRecords();
		                    }
		                });
		
		        donorSearchButton.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		                searchDonorRecord();
		            }
		        });
        
                patientSearchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        searchPatientRecord();
                    }
                });
        
                stockRecordButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        displayStockRecords();
                    }
                });
        
                patientRecordButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        displayPatientRecords();
                    }
                });

        donorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = donorTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedDonorId = donorTable.getValueAt(selectedRow, 0).toString();
                    selectedPatientId = null; // Clear patient selection
                }
            }
        });

        patientTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedPatientId = patientTable.getValueAt(selectedRow, 0).toString();
                    selectedDonorId = null; // Clear donor selection
                }
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

    private void searchDonorRecord() {
        String id = donorSearchField.getText();
        searchRecord("donor", id);
    }

    private void searchPatientRecord() {
        String id = patientSearchField.getText();
        searchRecord("Patient", id);
    }

    private void searchRecord(String tableName, String id) {
        try {
            con = getConnection();
            String query = "SELECT * FROM " + tableName + " WHERE " + tableName + "Id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, id);
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

    private void deleteRecord() {
        if (selectedDonorId != null) {
            moveRecordToHistory("donor", selectedDonorId);
            deleteRecordFromTable("donor", selectedDonorId);
            selectedDonorId = null;
        } else if (selectedPatientId != null) {
            moveRecordToHistory("Patient", selectedPatientId);
            deleteRecordFromTable("Patient", selectedPatientId);
            selectedPatientId = null;
        } else {
            JOptionPane.showMessageDialog(this, "No record selected for deletion", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moveRecordToHistory(String tableName, String id) {
        try {
            con = getConnection();
            // Insert record into History
            PreparedStatement psHistory = con.prepareStatement("INSERT INTO History (UserType, Name, FatherName, MotherName, DOB, MobileNo, Gender, BloodGroup, BloodUnit, City, Email, Address) SELECT UserType, Name, FatherName, MotherName, DOB, MobileNo, Gender, BloodGroup, BloodUnit, City, Email, Address FROM " + tableName + " WHERE " + tableName + "Id = ?");
            psHistory.setString(1, id);
            psHistory.executeUpdate();
            psHistory.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error moving record to history", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteRecordFromTable(String tableName, String id) {
        try {
            con = getConnection();
            String query = "DELETE FROM " + tableName + " WHERE " + tableName + "Id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, id);
            pst.executeUpdate();
            pst.close();
            con.close();
            // Refresh tables after deletion
            displayDonorRecords();
            displayPatientRecords();
            displayHistoryRecords();
            displayStockRecords();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting record", "Error", JOptionPane.ERROR_MESSAGE);
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
