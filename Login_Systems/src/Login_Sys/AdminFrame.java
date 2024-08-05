package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class AdminFrame extends JFrame {
    private Connection con;
    private JTable adminTable;
    private JTextField adminIdField;

    public AdminFrame() {
        setTitle("Admin Management");
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

        // Table for Admin data
        adminTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(adminTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Panel for form and buttons
        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        formPanel.add(new JLabel("Admin ID:"));
        adminIdField = new JTextField();
        formPanel.add(adminIdField);

        JButton loadButton = new JButton("Load Admins");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAdmins();
            }
        });

        JButton addButton = new JButton("Add Admin");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAdmin();
            }
        });

        JButton updateButton = new JButton("Update Admin");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAdmin();
            }
        });

        JButton deleteButton = new JButton("Delete Admin");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAdmin();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data initially
        loadAdmins(); // Load admin data for the table
    }

    private void loadAdmins() {
        try {
            String query = "SELECT * FROM Admin";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            adminTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading admins.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addAdmin() {
        try {
            String adminId = adminIdField.getText();

            String query = "INSERT INTO Admin (AdminId) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(adminId));
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Admin added successfully.");
            loadAdmins(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding admin.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAdmin() {
        // For this table, update is generally not needed as there is no additional data to update other than AdminId.
        // But if you want to extend functionality, implement it similarly to addAdmin() method.
    }

    private void deleteAdmin() {
        try {
            String adminId = adminIdField.getText();
            String query = "DELETE FROM Admin WHERE AdminId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(adminId));
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Admin deleted successfully.");
            loadAdmins(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting admin.", "Database Error", JOptionPane.ERROR_MESSAGE);
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
            AdminFrame frame = new AdminFrame();
            frame.setVisible(true);
        });
    }
}
