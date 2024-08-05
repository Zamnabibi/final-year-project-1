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
public class BloodStockFrame extends JFrame {
    private Connection con;
    private JTable stockTable;
    private JTextField stockIdField;
    private JTextField bloodGroupField;
    private JTextField totalUnitsField;

    public BloodStockFrame() {
        setTitle("Blood Stock Management");
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

        // Table for Blood Stock data
        stockTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(stockTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Panel for form and buttons
        JPanel formPanel = new JPanel(new GridLayout(4, 2)); // Increased row count
        formPanel.add(new JLabel("Stock ID:"));
        stockIdField = new JTextField();
        formPanel.add(stockIdField);

        formPanel.add(new JLabel("Blood Group:"));
        bloodGroupField = new JTextField();
        formPanel.add(bloodGroupField);

        formPanel.add(new JLabel("Total Units:"));
        totalUnitsField = new JTextField();
        formPanel.add(totalUnitsField);

        JButton loadButton = new JButton("Load Stocks");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadStocks();
            }
        });

        JButton loadBloodGroupButton = new JButton("Load Blood Groups");
        loadBloodGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBloodGroups();
            }
        });

        JButton addButton = new JButton("Add Stock");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStock();
            }
        });

        JButton updateButton = new JButton("Update Stock");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStock();
            }
        });

        JButton deleteButton = new JButton("Delete Stock");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStock();
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
        buttonPanel.add(loadButton);
        buttonPanel.add(loadBloodGroupButton); // Added button
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton); // Add close button

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data initially
        loadStocks(); // Load stock data for the table

        // Add selection listener to the table
        stockTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = stockTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int stockId = (Integer) stockTable.getValueAt(selectedRow, 0);
                    String bloodGroup = (String) stockTable.getValueAt(selectedRow, 1);
                    int totalUnits = (Integer) stockTable.getValueAt(selectedRow, 2);
                    
                    stockIdField.setText(String.valueOf(stockId));
                    bloodGroupField.setText(bloodGroup);
                    totalUnitsField.setText(String.valueOf(totalUnits));
                }
            }
        });
    }

    private void loadStocks() {
        try {
            String query = "SELECT * FROM BloodStock";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            stockTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading stocks.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBloodGroups() {
        try {
            String query = "SELECT * FROM BloodGroup";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            stockTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood groups.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addStock() {
        try {
            String bloodGroup = bloodGroupField.getText();
            int totalUnits = Integer.parseInt(totalUnitsField.getText());

            // Check if the blood group exists
            String checkQuery = "SELECT COUNT(*) FROM BloodGroup WHERE BloodGroup = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setString(1, bloodGroup);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            int count = checkRs.getInt(1);
            checkRs.close();
            checkStmt.close();

            if (count == 0) {
                JOptionPane.showMessageDialog(this, "Blood group does not exist.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add stock if the blood group exists
            String query = "INSERT INTO BloodStock (BloodGroup, TotalUnits) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, bloodGroup);
            pstmt.setInt(2, totalUnits);
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Stock added successfully.");
            loadStocks(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding stock.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid total units.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStock() {
        try {
            int stockId = Integer.parseInt(stockIdField.getText());
            String bloodGroup = bloodGroupField.getText();
            int totalUnits = Integer.parseInt(totalUnitsField.getText());

            // Debugging output
            System.out.println("Updating Stock: ID=" + stockId + ", BloodGroup=" + bloodGroup + ", TotalUnits=" + totalUnits);

            String query = "UPDATE BloodStock SET BloodGroup = ?, TotalUnits = ? WHERE StockId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, bloodGroup);
            pstmt.setInt(2, totalUnits);
            pstmt.setInt(3, stockId);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();

            // Debugging output
            System.out.println("Rows Affected: " + rowsAffected);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Stock updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No stock found with the specified ID.", "Update Error", JOptionPane.ERROR_MESSAGE);
            }

            loadStocks(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating stock.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check the fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStock() {
        try {
            int stockId = Integer.parseInt(stockIdField.getText());
            String query = "DELETE FROM BloodStock WHERE StockId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, stockId);
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Stock deleted successfully.");
            loadStocks(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting stock.", "Database Error", JOptionPane.ERROR_MESSAGE);
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
            BloodStockFrame frame = new BloodStockFrame();
            frame.setVisible(true);
        });
    }
}
