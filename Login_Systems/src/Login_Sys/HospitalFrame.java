package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

@SuppressWarnings("serial")
public class HospitalFrame extends JFrame {
    private Connection con;
    private JTable hospitalTable;
    private JTextField idField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField cityField;
    private JTextField stateField;
    private JTextField countryField;
    private JTextField zipCodeField;
    private JTextField contactNumberField;
    private JTextField emailField;
    private JTextField locationField;

    public HospitalFrame() {
        setTitle("Hospital Management");
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

        // Table for Hospital data
        hospitalTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(hospitalTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Panel for form and buttons
        JPanel formPanel = new JPanel(new GridLayout(12, 2));
        formPanel.add(new JLabel("Hospital ID:"));
        idField = new JTextField();
        idField.setEditable(false);
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);
        formPanel.add(new JLabel("City:"));
        cityField = new JTextField();
        formPanel.add(cityField);
        formPanel.add(new JLabel("State:"));
        stateField = new JTextField();
        formPanel.add(stateField);
        formPanel.add(new JLabel("Country:"));
        countryField = new JTextField();
        formPanel.add(countryField);
        formPanel.add(new JLabel("Zip Code:"));
        zipCodeField = new JTextField();
        formPanel.add(zipCodeField);
        formPanel.add(new JLabel("Contact Number:"));
        contactNumberField = new JTextField();
        formPanel.add(contactNumberField);
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        // Location JTextField
        formPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        formPanel.add(locationField);

        JButton loadButton = new JButton("Load Hospitals");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadHospitals();
            }
        });

        JButton addButton = new JButton("Add Hospital");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addHospital();
            }
        });

        JButton updateButton = new JButton("Update Hospital");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHospital();
            }
        });

        JButton deleteButton = new JButton("Delete Hospital");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHospital();
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

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data initially
        loadHospitals();
    }

    private void loadHospitals() {
        try {
            String query = "SELECT * FROM Hospital";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            hospitalTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading hospitals.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addHospital() {
        try {
            String name = nameField.getText();
            String address = addressField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String country = countryField.getText();
            String zipCode = zipCodeField.getText();
            String contactNumber = contactNumberField.getText();
            String email = emailField.getText();
            String location = locationField.getText();
            String query = "INSERT INTO Hospital (Name, Address, City, State, Country, ZipCode, ContactNumber, Email, Location) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, city);
            pstmt.setString(4, state);
            pstmt.setString(5, country);
            pstmt.setString(6, zipCode);
            pstmt.setString(7, contactNumber);
            pstmt.setString(8, email);
            pstmt.setString(9, location);
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Hospital added successfully.");
            loadHospitals(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding hospital.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateHospital() {
        try {
            String id = idField.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a hospital to update.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String name = nameField.getText();
            String address = addressField.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String country = countryField.getText();
            String zipCode = zipCodeField.getText();
            String contactNumber = contactNumberField.getText();
            String email = emailField.getText();
            String location = locationField.getText();
            String query = "UPDATE Hospital SET Name = ?, Address = ?, City = ?, State = ?, Country = ?, ZipCode = ?, ContactNumber = ?, Email = ?, Location = ? WHERE HospitalId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, city);
            pstmt.setString(4, state);
            pstmt.setString(5, country);
            pstmt.setString(6, zipCode);
            pstmt.setString(7, contactNumber);
            pstmt.setString(8, email);
            pstmt.setString(9, location);
            pstmt.setInt(10, Integer.parseInt(id));
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Hospital updated successfully.");
            loadHospitals(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating hospital.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteHospital() {
        try {
            String id = idField.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a hospital to delete.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String query = "DELETE FROM Hospital WHERE HospitalId = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(id));
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "Hospital deleted successfully.");
            loadHospitals(); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting hospital.", "Database Error", JOptionPane.ERROR_MESSAGE);
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
            HospitalFrame frame = new HospitalFrame();
            frame.setVisible(true);
        });
    }
}
