package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class RecordManager extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JTextField searchField;
    private JButton saveButton, deleteButton, displayButton, searchButton;
    private JTextArea recordsDisplay, historyDisplay;
    private ArrayList<String> records;
    private ArrayList<String> history;
    private Connection con;

    public RecordManager() {
        records = new ArrayList<>();
        history = new ArrayList<>();

        setTitle("Record Manager");
        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);
        saveButton = new JButton("Save Record");
        deleteButton = new JButton("Delete Record");
        displayButton = new JButton("Display");

        JPanel inputPanel = new JPanel();
        inputPanel.setBounds(10, 10, 760, 40);
        JLabel lblSearchDonorId = new JLabel("Search Donor id: ");
        inputPanel.add(lblSearchDonorId);
        searchField = new JTextField(20);
        inputPanel.add(searchField);
        searchButton = new JButton("Search");
        inputPanel.add(searchButton);
        
                searchButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        searchRecord();
                    }
                });
        inputPanel.add(saveButton);
        inputPanel.add(deleteButton);
        inputPanel.add(displayButton);

        JScrollPane recordsScrollPane = new JScrollPane();
        recordsScrollPane.setBounds(10, 60, 370, 300);

        JScrollPane historyScrollPane = new JScrollPane();
        historyScrollPane.setBounds(390, 60, 370, 300);

        contentPane.add(inputPanel);
        contentPane.add(recordsScrollPane);
        
                recordsDisplay = new JTextArea();
                recordsScrollPane.setViewportView(recordsDisplay);
                recordsDisplay.setEditable(false);
        contentPane.add(historyScrollPane);
        historyDisplay = new JTextArea();
        historyScrollPane.setViewportView(historyDisplay);
        historyDisplay.setEditable(false);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRecord();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRecords();
            }
        });

        // Initialize table
        table = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBounds(10, 371, 760, 199);
        contentPane.add(tableScrollPane);
    }

    private void saveRecord() {
        String record = searchField.getText();
        if (!record.isEmpty()) {
            records.add(record);
            history.add("Saved: " + record);
            updateDisplay();
            searchField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Record cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        String record = searchField.getText();
        if (records.contains(record)) {
            int response = JOptionPane.showConfirmDialog(this, "Do you want to save the record before deleting?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                saveRecord();
            }
            records.remove(record);
            history.add("Deleted: " + record);
            updateDisplay();
            searchField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Record not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecord() {
        String donorId = searchField.getText();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM donor WHERE DonorId=" + donorId);
            if (rs.next()) {
                recordsDisplay.setText(
                    "Donor ID: " + rs.getString(1) + "\n" +
                    "Name: " + rs.getString(2) + "\n" +
                    "Age: " + rs.getString(3) + "\n" +
                    "Blood Type: " + rs.getString(4) + "\n" +
                    "Phone: " + rs.getString(5) + "\n" +
                    "Address: " + rs.getString(6) + "\n" +
                    "Email: " + rs.getString(7) + "\n" +
                    "Gender: " + rs.getString(8) + "\n" +
                    "Donation Date: " + rs.getString(9) + "\n" +
                    "Comments: " + rs.getString(10)
                );
            } else {
                JOptionPane.showMessageDialog(this, "Donor ID does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            }
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching record", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateDisplay() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StringBuilder recordsText = new StringBuilder();
                for (String record : records) {
                    recordsText.append(record).append("\n");
                }
                recordsDisplay.setText(recordsText.toString());

                StringBuilder historyText = new StringBuilder();
                for (String log : history) {
                    historyText.append(log).append("\n");
                }
                historyDisplay.setText(historyText.toString());
            }
        });
    }

    private void displayRecords() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM donor");
            ResultSetMetaData rsmd = rs.getMetaData();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
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
            st.close();
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching records", "Error", JOptionPane.ERROR_MESSAGE);
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
