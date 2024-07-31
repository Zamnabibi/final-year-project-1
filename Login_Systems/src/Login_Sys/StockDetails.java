package Login_Sys;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.awt.Font;
import javax.swing.JScrollPane;

public class StockDetails extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JLabel timeLabel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                StockDetails frame = new StockDetails();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public StockDetails() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Stock Details");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 34));
        lblNewLabel.setBounds(151, 22, 280, 41);
        contentPane.add(lblNewLabel);
        
        // Add time label
        timeLabel = new JLabel(); // Changed from JTextField to JLabel
        timeLabel.setBounds(640, 10, 184, 20);
        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(timeLabel);

        // Set the timer to update the JLabel every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();

        // Initial time update
        updateTime();
        
        table = new JTable();
        table.setFont(new Font("Tahoma", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 100, 814, 225);
        contentPane.add(scrollPane);

        JButton btnPrint = new JButton("Print");
        Image imgPrint = new ImageIcon(this.getClass().getResource("/print.png")).getImage();
        btnPrint.setIcon(new ImageIcon(imgPrint));
        btnPrint.addActionListener(e -> {
            try {
                table.print(JTable.PrintMode.NORMAL);
            } catch (Exception t) {
                JOptionPane.showMessageDialog(null, "Error printing table: " + t.getMessage());
            }
        });
        btnPrint.setBounds(45, 371, 99, 36);
        contentPane.add(btnPrint);

        JButton btnClose = new JButton("Close");
        Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnClose.setIcon(new ImageIcon(imgClose));
        btnClose.addActionListener(e -> setVisible(false));
        btnClose.setBounds(660, 371, 99, 36);
        contentPane.add(btnClose);

        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 475, 850, 50); // Adjust size and position as needed
        contentPane.add(footerPanel);

        JLabel lblBackground = new JLabel("");
        Image imgBackground = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblBackground.setIcon(new ImageIcon(imgBackground));
        lblBackground.setBounds(0, 0, 834, 461);
        contentPane.add(lblBackground);

        // Automatically display stock details
        displayStockDetails();
    }

    private void displayStockDetails() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM stock")) {

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connection created");

            // Get metadata to determine the column names
            ResultSetMetaData rsmd = rs.getMetaData();
            int cols = rsmd.getColumnCount();
            String[] colNames = new String[cols];
            for (int i = 0; i < cols; i++) {
                colNames[i] = rsmd.getColumnName(i + 1);
            }

            // Set up the table model
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(colNames);
            table.setModel(model);

            // Populate the table with data
            while (rs.next()) {
                String[] row = new String[cols];
                for (int i = 0; i < cols; i++) {
                    row[i] = rs.getString(i + 1);
                }
                model.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error displaying stock details: " + ex.getMessage());
        }
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
    }
}
