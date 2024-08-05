package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class BloodGroupFrame extends JFrame {
    private Connection con;
    private JTable bloodGroupTable;
    private JTextField bloodGroupField;
    private JTextField descriptionField;

    public BloodGroupFrame() {
        setTitle("Blood Group Management");
        setSize(678, 534);
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

        // Table for BloodGroup data
        bloodGroupTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(bloodGroupTable);
        getContentPane().add(tableScrollPane, BorderLayout.CENTER);

        // Panel for form and buttons
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("Blood Group:"));
        bloodGroupField = new JTextField();
        formPanel.add(bloodGroupField);
        formPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        formPanel.add(descriptionField);

        JButton loadButton = new JButton("Load Blood Groups");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBloodGroups();
            }
        });
        
        JButton addButton = new JButton("Add Blood Group");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBloodGroup();
            }
        });
        
        JButton updateButton = new JButton("Update Blood Group");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBloodGroup();
            }
        });
        
        JButton deleteButton = new JButton("Delete Blood Group");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBloodGroup();
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
        buttonPanel.add(closeButton); // Add close button

        getContentPane().add(formPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        // Load data initially
        loadBloodGroups();
    }

    private void loadBloodGroups() {
        try {
            String query = "SELECT * FROM BloodGroup";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            bloodGroupTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood groups.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBloodGroup() {
        try {
            String bloodGroup = bloodGroupField.getText();
            String description = descriptionField.getText();
            String query = "INSERT INTO BloodGroup (BloodGroup, Description) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, bloodGroup);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Blood group added successfully.");
            loadBloodGroups(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding blood group.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBloodGroup() {
        try {
            String bloodGroup = bloodGroupField.getText();
            String description = descriptionField.getText();
            String query = "UPDATE BloodGroup SET Description = ? WHERE BloodGroup = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, description);
            pstmt.setString(2, bloodGroup);
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Blood group updated successfully.");
            loadBloodGroups(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating blood group.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBloodGroup() {
        try {
            String bloodGroup = bloodGroupField.getText();
            String query = "DELETE FROM BloodGroup WHERE BloodGroup = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, bloodGroup);
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Blood group deleted successfully.");
            loadBloodGroups(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting blood group.", "Database Error", JOptionPane.ERROR_MESSAGE);
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
            BloodGroupFrame frame = new BloodGroupFrame();
            frame.setVisible(true);
        });
    }
}
