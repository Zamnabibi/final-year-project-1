package Login_Sys;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.sql.*;
import java.text.SimpleDateFormat;

public class StockIncrease extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldCity;
    private JTextField textFieldUnits;
    private JComboBox<String> comboBoxBloodGroup;
    private JTable tableStock;
    private JLabel timeLabel;
    private Connection con;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                StockIncrease frame = new StockIncrease();
                frame.setVisible(true);
                // Set the timer to update the JLabel every second
                Timer timer = new Timer(1000, e -> frame.updateTime());
                timer.start();
                // Initial time update
                frame.updateTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public StockIncrease() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 750);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        connectToDatabase();
        
        JLabel lblTitle = new JLabel("Add Blood Units to Stock");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 34));
        lblTitle.setBounds(138, 11, 500, 47);
        contentPane.add(lblTitle);

        JLabel lblCity = new JLabel("City");
        lblCity.setBounds(34, 82, 76, 31);
        contentPane.add(lblCity);

        textFieldCity = new JTextField();
        textFieldCity.setBounds(152, 86, 150, 20);
        contentPane.add(textFieldCity);
        textFieldCity.setColumns(10);

        JLabel lblBloodGroup = new JLabel("Blood Group");
        lblBloodGroup.setBounds(34, 122, 76, 31);
        contentPane.add(lblBloodGroup);

        comboBoxBloodGroup = new JComboBox<>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        comboBoxBloodGroup.setBounds(152, 126, 150, 22);
        contentPane.add(comboBoxBloodGroup);

        JLabel lblUnits = new JLabel("Units");
        lblUnits.setBounds(34, 164, 63, 23);
        contentPane.add(lblUnits);

        textFieldUnits = new JTextField();
        textFieldUnits.setBounds(152, 165, 150, 20);
        contentPane.add(textFieldUnits);
        textFieldUnits.setColumns(10);

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 14));
        Image img1 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnClose.setIcon(new ImageIcon(img1));
        btnClose.addActionListener(e -> setVisible(false));
        btnClose.setBounds(617, 157, 163, 33);
        contentPane.add(btnClose);

        JButton btnAddStock = new JButton("Add to Stock");
        btnAddStock.setIcon(new ImageIcon(getClass().getResource("/add donor.png"))); // Ensure this path is correct
        btnAddStock.addActionListener(e -> addStock());
        btnAddStock.setBounds(329, 156, 163, 35);
        contentPane.add(btnAddStock);

        JScrollPane scrollPaneStock = new JScrollPane();
        scrollPaneStock.setBounds(10, 200, 814, 450);
        contentPane.add(scrollPaneStock);

        // Initialize tableStock
        tableStock = new JTable();
        tableStock.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"City", "BloodGroup", "Units"} // Add the AddDate column if needed
        ));
        scrollPaneStock.setViewportView(tableStock);

        timeLabel = new JLabel();
        timeLabel.setBounds(617, 51, 184, 20);
        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(timeLabel);
        
        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 661, 850, 50); // Adjust size and position as needed
        contentPane.add(footerPanel);

        JLabel lblBackground = new JLabel("");
        lblBackground.setIcon(new ImageIcon(getClass().getResource("/back.jpg"))); // Ensure this path is correct
        lblBackground.setBounds(0, 200, 834, 461);
        contentPane.add(lblBackground);

        JLabel lblBackground1 = new JLabel("");
        lblBackground1.setIcon(new ImageIcon(getClass().getResource("/back.jpg"))); // Ensure this path is correct
        lblBackground1.setBounds(0, 0, 834, 461);
        contentPane.add(lblBackground1);

        // Load data automatically
        loadData();
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0"); // Change "password" to your actual password
            System.out.println("Connection created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addStock() {
        String city = textFieldCity.getText();
        String bloodGroup = (String) comboBoxBloodGroup.getSelectedItem();
        String unitsStr = textFieldUnits.getText();

        try {
            int units = Integer.parseInt(unitsStr);

            // Check if the blood group and city already exist
            String checkQuery = "SELECT Units FROM stock WHERE BloodGroup = ? AND City = ?";
            try (PreparedStatement checkPst = con.prepareStatement(checkQuery)) {
                checkPst.setString(1, bloodGroup);
                checkPst.setString(2, city);
                ResultSet rs = checkPst.executeQuery();

                if (rs.next()) {
                    // Update existing record by adding the units
                    int existingUnits = rs.getInt("Units");
                    int newUnits = existingUnits + units;

                    String updateQuery = "UPDATE stock SET Units = ?, AddDate = NOW() WHERE BloodGroup = ? AND City = ?";
                    try (PreparedStatement updatePst = con.prepareStatement(updateQuery)) {
                        updatePst.setInt(1, newUnits);
                        updatePst.setString(2, bloodGroup);
                        updatePst.setString(3, city);
                        updatePst.executeUpdate();
                    }
                } else {
                    // Insert new record if no existing record is found
                    String insertQuery = "INSERT INTO stock (City, BloodGroup, Units, AddDate) VALUES (?, ?, ?, NOW())";
                    try (PreparedStatement insertPst = con.prepareStatement(insertQuery)) {
                        insertPst.setString(1, city);
                        insertPst.setString(2, bloodGroup);
                        insertPst.setInt(3, units);
                        insertPst.executeUpdate();
                    }
                }
                JOptionPane.showMessageDialog(null, "Stock successfully updated");
                refreshStockTable();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Units must be a valid number");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating stock: " + e.getMessage());
        }
    }

    private void refreshStockTable() {
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Database connection is not established.");
            return;
        }

        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT City, BloodGroup, Units FROM stock")) {
            DefaultTableModel model = (DefaultTableModel) tableStock.getModel();
            model.setRowCount(0); // Clear existing data

            while (rs.next()) {
                String[] row = {
                    rs.getString("City"),
                    rs.getString("BloodGroup"),
                    rs.getString("Units"),
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error refreshing stock table: " + e.getMessage());
        }
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
    }

    private void loadData() {
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Database connection is not established.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tableStock.getModel();
        model.setRowCount(0); // Clear existing rows
        String query = "SELECT * FROM stock";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                String city = rs.getString("City");
                String bloodGroup = rs.getString("BloodGroup");
                String units = rs.getString("Units");

                String[] row = { city, bloodGroup, units };
                model.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
