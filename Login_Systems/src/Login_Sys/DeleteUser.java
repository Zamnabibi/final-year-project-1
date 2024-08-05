package Login_Sys;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.sql.*;
import java.text.SimpleDateFormat;
public class DeleteUser extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField userIdField;
    private JTextField nameField;
    private JTextField fatherNameField;
    private JTextField motherNameField;
    private JTextField dobField;
    private JTextField mobileNoField;
    private JTextField emailField;
    private JTextField cityField;
    private JTextField addressField;
    private JTextField userTypeField;
    private JTextField genderField;
    private Connection con;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DeleteUser frame = new DeleteUser();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DeleteUser() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 528, 537);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        initializeDatabaseConnection(); // Ensure database connection is initialized

        JLabel lblUpdateUser = new JLabel("Update User");
        lblUpdateUser.setBounds(156, 11, 110, 25);
        contentPane.add(lblUpdateUser);

        JLabel lblUserId = new JLabel("User Id");
        lblUserId.setBounds(20, 44, 46, 14);
        contentPane.add(lblUserId);

        userIdField = new JTextField();
        userIdField.setBounds(94, 41, 86, 20);
        contentPane.add(userIdField);
        userIdField.setColumns(10);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchUser());
        searchButton.setBounds(190, 40, 89, 23);
        contentPane.add(searchButton);

        JLabel lblName = new JLabel("Name");
        lblName.setBounds(20, 85, 46, 14);
        contentPane.add(lblName);

        nameField = new JTextField();
        nameField.setBounds(94, 82, 212, 20);
        contentPane.add(nameField);
        nameField.setColumns(10);

        JLabel lblFatherName = new JLabel("Father Name");
        lblFatherName.setBounds(20, 110, 80, 14);
        contentPane.add(lblFatherName);

        fatherNameField = new JTextField();
        fatherNameField.setBounds(94, 107, 212, 20);
        contentPane.add(fatherNameField);
        fatherNameField.setColumns(10);

        JLabel lblMotherName = new JLabel("Mother Name");
        lblMotherName.setBounds(20, 135, 80, 14);
        contentPane.add(lblMotherName);

        motherNameField = new JTextField();
        motherNameField.setBounds(94, 132, 212, 20);
        contentPane.add(motherNameField);
        motherNameField.setColumns(10);

        JLabel lblDob = new JLabel("Date of Birth");
        lblDob.setBounds(20, 160, 80, 14);
        contentPane.add(lblDob);

        dobField = new JTextField();
        dobField.setBounds(94, 157, 212, 20);
        contentPane.add(dobField);
        dobField.setColumns(10);

        JLabel lblMobileNo = new JLabel("Mobile No");
        lblMobileNo.setBounds(20, 185, 80, 14);
        contentPane.add(lblMobileNo);

        mobileNoField = new JTextField();
        mobileNoField.setBounds(94, 182, 212, 20);
        contentPane.add(mobileNoField);
        mobileNoField.setColumns(10);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(20, 210, 80, 14);
        contentPane.add(lblEmail);

        emailField = new JTextField();
        emailField.setBounds(94, 207, 212, 20);
        contentPane.add(emailField);
        emailField.setColumns(10);

        JLabel lblCity = new JLabel("City");
        lblCity.setBounds(20, 235, 80, 14);
        contentPane.add(lblCity);

        cityField = new JTextField();
        cityField.setBounds(94, 232, 212, 20);
        contentPane.add(cityField);
        cityField.setColumns(10);

        JLabel lblAddress = new JLabel("Address");
        lblAddress.setBounds(20, 261, 80, 14);
        contentPane.add(lblAddress);

        addressField = new JTextField();
        addressField.setBounds(94, 258, 212, 20);
        contentPane.add(addressField);
        addressField.setColumns(10);

        JLabel lblUserType = new JLabel("User Type");
        lblUserType.setBounds(20, 286, 80, 14);
        contentPane.add(lblUserType);

        userTypeField = new JTextField();
        userTypeField.setBounds(94, 283, 212, 20);
        contentPane.add(userTypeField);
        userTypeField.setColumns(10);

        JLabel lblGender = new JLabel("Gender");
        lblGender.setBounds(20, 311, 80, 14);
        contentPane.add(lblGender);

        genderField = new JTextField();
        genderField.setBounds(94, 308, 212, 20);
        contentPane.add(genderField);
        genderField.setColumns(10);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateUser());
        updateButton.setBounds(190, 339, 89, 23);
        contentPane.add(updateButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        closeButton.setBounds(289, 339, 89, 23);
        contentPane.add(closeButton);
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteUser());
        deleteButton.setBounds(304, 40, 89, 23);
        contentPane.add(deleteButton);
    }

    private void initializeDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchUser() {
        String userId = userIdField.getText().trim();
        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a User ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM User WHERE UserId = ?")) {
            pst.setInt(1, Integer.parseInt(userId));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("Name"));
                fatherNameField.setText(rs.getString("FatherName"));
                motherNameField.setText(rs.getString("MotherName"));
                dobField.setText(new SimpleDateFormat("yyyy-MM-dd").format(rs.getDate("DOB")));
                mobileNoField.setText(rs.getString("MobileNo"));
                emailField.setText(rs.getString("Email"));
                cityField.setText(rs.getString("City"));
                addressField.setText(rs.getString("Address"));
                userTypeField.setText(rs.getString("UserType"));
                genderField.setText(rs.getString("Gender"));
            } else {
                JOptionPane.showMessageDialog(this, "User not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching for user.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        String userId = userIdField.getText().trim();
        String name = nameField.getText().trim();
        String fatherName = fatherNameField.getText().trim();
        String motherName = motherNameField.getText().trim();
        String dob = dobField.getText().trim();
        String mobileNo = mobileNoField.getText().trim();
        String email = emailField.getText().trim();
        String city = cityField.getText().trim();
        String address = addressField.getText().trim();
        String userType = userTypeField.getText().trim();
        String gender = genderField.getText().trim();

        if (userId.isEmpty() || name.isEmpty() || fatherName.isEmpty() || motherName.isEmpty() || dob.isEmpty() ||
            mobileNo.isEmpty() || email.isEmpty() || city.isEmpty() || address.isEmpty() || userType.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PreparedStatement pst = con.prepareStatement(
                "UPDATE User SET Name = ?, FatherName = ?, MotherName = ?, DOB = ?, MobileNo = ?, Email = ?, City = ?, Address = ?, UserType = ?, Gender = ? WHERE UserId = ?")) {
            pst.setString(1, name);
            pst.setString(2, fatherName);
            pst.setString(3, motherName);
            pst.setDate(4, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(dob).getTime()));
            pst.setString(5, mobileNo);
            pst.setString(6, email);
            pst.setString(7, city);
            pst.setString(8, address);
            pst.setString(9, userType);
            pst.setString(10, gender);
            pst.setInt(11, Integer.parseInt(userId));
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "User not found.", "Update Result", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        String userId = userIdField.getText().trim();
        if (userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a User ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (PreparedStatement pst = con.prepareStatement("DELETE FROM User WHERE UserId = ?")) {
                pst.setInt(1, Integer.parseInt(userId));
                int rowsDeleted = pst.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");
                    // Clear fields after deletion
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "User not found.", "Delete Result", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting user.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearFields() {
        userIdField.setText("");
        nameField.setText("");
        fatherNameField.setText("");
        motherNameField.setText("");
        dobField.setText("");
        mobileNoField.setText("");
        emailField.setText("");
        cityField.setText("");
        addressField.setText("");
        userTypeField.setText("");
        genderField.setText("");
    }
}
