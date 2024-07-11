package Login_Sys;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;


public class searchBloodDonorBloodGroup extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTable table;
    private Connection con;
    private PreparedStatement pst;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    searchBloodDonorBloodGroup frame = new searchBloodDonorBloodGroup();
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
    public searchBloodDonorBloodGroup() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        try {
            // Establishing database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
            System.out.println("Connection created");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage());
            e.printStackTrace();
        }

        JLabel lblNewLabel = new JLabel("Search Blood Donor (Blood Group)");
        lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 34));
        lblNewLabel.setBounds(111, 11, 951, 51);
        contentPane.add(lblNewLabel);

        JSeparator separator = new JSeparator();
        separator.setBounds(10, 73, 1323, 2);
        contentPane.add(separator);

        JLabel lblNewLabel_1 = new JLabel("Blood Group");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_1.setBounds(134, 86, 110, 25);
        contentPane.add(lblNewLabel_1);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField.setBounds(379, 85, 153, 28);
        contentPane.add(textField);
        textField.setColumns(10);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 122, 1323, 2);
        contentPane.add(separator_1);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(26, 140, 798, 217);
        contentPane.add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(10, 368, 1323, 2);
        contentPane.add(separator_2);

        JButton btnNewButton = new JButton("Print");
        Image img3 = new ImageIcon(this.getClass().getResource("/print.png")).getImage();
        btnNewButton.setIcon(new ImageIcon(img3));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    table.print(JTable.PrintMode.NORMAL);
                } catch (Exception t) {
                    JOptionPane.showMessageDialog(null, "Error printing: " + t.getMessage());
                }
            }
        });
        btnNewButton.setBounds(41, 392, 116, 23);
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Display");
        Image img2 = new ImageIcon(this.getClass().getResource("/display.png")).getImage();
        btnNewButton_1.setIcon(new ImageIcon(img2));
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bloodGroup = textField.getText();
                try {
                    String query = "SELECT * FROM donor WHERE BloodGroup LIKE ?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, "%" + bloodGroup + "%");

                    ResultSet rs = pst.executeQuery();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for (int i = 0; i < cols; i++)
                        colName[i] = rsmd.getColumnName(i + 1);
                    model.setColumnIdentifiers(colName);

                    while (rs.next()) {
                        String[] row = new String[cols];
                        for (int i = 0; i < cols; i++) {
                            row[i] = rs.getString(i + 1);
                        }
                        model.addRow(row);
                    }

                    pst.close();
                    rs.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error executing query: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        btnNewButton_1.setBounds(337, 392, 110, 23);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Close");
        Image img1 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnNewButton_2.setIcon(new ImageIcon(img1));
        btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    con.close(); // Closing the connection when closing the frame
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                setVisible(false);
            }
        });
        btnNewButton_2.setBounds(628, 392, 116, 23);
        contentPane.add(btnNewButton_2);

        JLabel lblNewLabel_13 = new JLabel("");
        Image img4 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_13.setIcon(new ImageIcon(img4));
        lblNewLabel_13.setBounds(0, -119, 1370, 749);
        contentPane.add(lblNewLabel_13);
    }
}
