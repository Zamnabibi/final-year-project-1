package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;
import java.util.Vector;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class UserFrame extends JFrame {
    private Connection con;
    private JTable userTable;
    private JTextField nameField;
    private JTextField fatherNameField;
    private JTextField motherNameField;
    private JTextField mobileNoField;
    private JTextField emailField;
    private JTextField addressField;
    private JComboBox<String> userTypeComboBox;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> comboBoxCity;
    private JLabel userIdLabel;
    private JDateChooser dateChooser;
    private JButton btnUpdateUser;
    private JButton btnClose;

    public UserFrame() {
        setTitle("User Management");
        setSize(800, 600);
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

        // Table for User data
        userTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        getContentPane().add(tableScrollPane, BorderLayout.CENTER);

        // Panel for form and buttons
        JPanel formPanel = new JPanel(new GridLayout(13, 2)); // Changed to 13 to fit all fields
        formPanel.add(new JLabel("User ID:"));
        userIdLabel = new JLabel("1"); // Default value
        formPanel.add(userIdLabel);
        setUserIdLabel(); // Set the next user ID

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Father Name:"));
        fatherNameField = new JTextField();
        formPanel.add(fatherNameField);

        formPanel.add(new JLabel("Mother Name:"));
        motherNameField = new JTextField();
        formPanel.add(motherNameField);

        formPanel.add(new JLabel("Date of Birth:"));
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        formPanel.add(dateChooser);

        formPanel.add(new JLabel("Mobile No:"));
        mobileNoField = new JTextField();
        formPanel.add(mobileNoField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("City:"));
        comboBoxCity = new JComboBox<>(new String[]{
            "Johar Town", "Gulberg", "DHA", "Model Town", "Bahria Town", "Garden Town", "Shadman", "Cantt",
            "Faisal Town", "Allama Iqbal Town", "Anarkali", "New Campus", "New Muslim Town", "Ferozepur Road",
            "Jail Road", "Shalimar Link Road", "Wapda Town", "Sabzazar", "Mughalpura", "Multan Road", "Raiwind Road",
            "Sanda Road", "Westwood Colony", "Main Boulevard", "Main Gulberg", "L-Block, DHA Phase 1", "GT Road",
            "University Avenue"
        });
        formPanel.add(comboBoxCity);

        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        formPanel.add(new JLabel("User Type:"));
        userTypeComboBox = new JComboBox<>(new String[]{"Donor", "Patient", "Admin"});
        formPanel.add(userTypeComboBox);

        formPanel.add(new JLabel("Gender:"));
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        formPanel.add(genderComboBox);

        JButton loadButton = new JButton("Load Users");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });

        JButton addButton = new JButton("Add User");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 new DeleteUser().setVisible(true);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        getContentPane().add(formPanel, BorderLayout.NORTH);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        btnUpdateUser = new JButton("Update User");
        btnUpdateUser.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		 new UpdateUserFrame().setVisible(true);
        	}
        });
        buttonPanel.add(btnUpdateUser);
        
        btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        	}
        });
        buttonPanel.add(btnClose);

       
    }

      

    private void setUserIdLabel() {
        try {
            String query = "SELECT MAX(UserId) FROM User";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int id = rs.getInt(1);
                userIdLabel.setText(String.valueOf(id + 1));
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching user ID.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUsers() {
        try {
            String query = "SELECT * FROM User";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            userTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addUser() {
        String name = nameField.getText().trim();
        String fatherName = fatherNameField.getText().trim();
        String motherName = motherNameField.getText().trim();
        Date dob = new Date(dateChooser.getDate().getTime());
        String mobileNo = mobileNoField.getText().trim();
        String email = emailField.getText().trim();
        String city = (String) comboBoxCity.getSelectedItem();
        String address = addressField.getText().trim();
        String userType = (String) userTypeComboBox.getSelectedItem();
        String gender = (String) genderComboBox.getSelectedItem();
        Integer donorId = null;
        Integer patientId = null;

        if (userType.equals("Donor")) {
            donorId = Integer.parseInt(userIdLabel.getText());
        } else if (userType.equals("Patient")) {
            patientId = Integer.parseInt(userIdLabel.getText());
        }

        if (name.isEmpty() || fatherName.isEmpty() || motherName.isEmpty() || mobileNo.isEmpty() || email.isEmpty() || address.isEmpty() || city == null || userType == null || gender == null) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "INSERT INTO User (Name, FatherName, MotherName, DOB, MobileNo, Email, City, Address, UserType, Gender, DonorId, PatientId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, fatherName);
            pstmt.setString(3, motherName);
            pstmt.setDate(4, dob);
            pstmt.setString(5, mobileNo);
            pstmt.setString(6, email);
            pstmt.setString(7, city);
            pstmt.setString(8, address);
            pstmt.setString(9, userType);
            pstmt.setString(10, gender);
            pstmt.setObject(11, donorId, Types.INTEGER);  // Set DonorId if applicable
            pstmt.setObject(12, patientId, Types.INTEGER);  // Set PatientId if applicable
            pstmt.executeUpdate();
            pstmt.close();
            JOptionPane.showMessageDialog(this, "User added successfully.");
            loadUsers(); // Refresh the table
            setUserIdLabel();// Update User ID for the next user
            new UserFrame().setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding user.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

  
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserFrame userFrame = new UserFrame();
            userFrame.setVisible(true);
        });
    }
}
