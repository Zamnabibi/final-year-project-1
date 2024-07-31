package Login_Sys;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class StockDecrease extends JFrame {
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
                StockDecrease frame = new StockDecrease();
                frame.setVisible(true);
                frame.connectToDatabase();

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

    public StockDecrease() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 750);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        connectToDatabase();

        JLabel lblTitle = new JLabel("Delete Blood Units from Stock");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 34));
        lblTitle.setBounds(138, 11, 533, 47);
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
        btnClose.setBounds(657, 157, 140, 33);
        contentPane.add(btnClose);

        JButton btnDeleteStock = new JButton("Delete from Stock");
        btnDeleteStock.setIcon(new ImageIcon(getClass().getResource("/delete.png"))); // Ensure this path is correct
        btnDeleteStock.addActionListener(e -> deleteStock());
        btnDeleteStock.setBounds(349, 160, 161, 31);
        contentPane.add(btnDeleteStock);

        JScrollPane scrollPaneStock = new JScrollPane();
        scrollPaneStock.setBounds(10, 200, 814, 450);
        contentPane.add(scrollPaneStock);

        // Initialize tableStock
        tableStock = new JTable();
        tableStock.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"City", "BloodGroup", "Units"} // Adjust columns if necessary
        ));
        scrollPaneStock.setViewportView(tableStock);

        timeLabel = new JLabel();
        timeLabel.setBounds(640, 10, 184, 20);
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

        // Automatically load data into the table
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

    private void deleteStock() {
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
                    // Update existing record by reducing the units
                    int existingUnits = rs.getInt("Units");
                    int newUnits = existingUnits - units;

                    if (newUnits < 0) {
                        JOptionPane.showMessageDialog(null, "Insufficient units in stock");
                        return;
                    }

                    String updateQuery = "UPDATE stock SET Units = ?, RemoveDate = NOW() WHERE BloodGroup = ? AND City = ?";
                    try (PreparedStatement updatePst = con.prepareStatement(updateQuery)) {
                        updatePst.setInt(1, newUnits);
                        updatePst.setString(2, bloodGroup);
                        updatePst.setString(3, city);
                        updatePst.executeUpdate();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No existing stock found for the specified city and blood group");
                    return;
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
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT City, BloodGroup, Units FROM stock")) {
            DefaultTableModel model = (DefaultTableModel) tableStock.getModel();
            model.setRowCount(0); // Clear existing data

            while (rs.next()) {
                String[] row = {
                    rs.getString("City"),
                    rs.getString("BloodGroup"),
                    rs.getString("Units")
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
