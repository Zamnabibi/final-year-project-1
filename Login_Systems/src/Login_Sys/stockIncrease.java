package Login_Sys;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.awt.Font;

public class stockIncrease extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTable table;
    public static Connection con;
    PreparedStatement pst;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
              
                	stockIncrease frame = new stockIncrease();
                    frame.setVisible(true);
                   
                
            }
        });
    }

    public stockIncrease() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        /////////
        try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
        System.out.println("Connection created");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ///////

        JLabel lblNewLabel = new JLabel("Stock (Add Units)");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 34));
        lblNewLabel.setBounds(138, 11, 375, 47);
        contentPane.add(lblNewLabel);

        JSeparator separator = new JSeparator();
        separator.setBounds(10, 69, 814, 2);
        contentPane.add(separator);

        JLabel lblNewLabel_1 = new JLabel("Blood Group");
        lblNewLabel_1.setBounds(34, 82, 76, 31);
        contentPane.add(lblNewLabel_1);

        JComboBox<Object> comboBox = new JComboBox<Object>();
        comboBox.setRequestFocusEnabled(false);
        comboBox.setModel(new DefaultComboBoxModel<Object>(new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"}));
        comboBox.setBounds(152, 86, 63, 22);
        contentPane.add(comboBox);

        JLabel lblNewLabel_2 = new JLabel("Units");
        lblNewLabel_2.setBounds(271, 90, 63, 23);
        contentPane.add(lblNewLabel_2);

        textField = new JTextField();
        textField.setBounds(344, 87, 86, 20);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("Update");
        Image img2 = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
        btnNewButton.setIcon(new ImageIcon(img2));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String bloodGroup = (String) comboBox.getSelectedItem();
                String unit = textField.getText();
                int unit1 = Integer.parseInt(unit);
                try {
                	pst = con.prepareStatement("update stock set units = units + ? where bloodGroup = ?");
                    pst.setInt(1, unit1);
                    pst.setString(2, bloodGroup);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Successfully Updated");
                    setVisible(false);
                    new stockDecrese().setVisible(true);
                } catch (Exception b) {
                    JOptionPane.showMessageDialog(null, "Connection error");
                }
            }
        });
        btnNewButton.setBounds(464, 86, 120, 23);
        contentPane.add(btnNewButton);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 127, 814, 2);
        contentPane.add(separator_1);

        table = new JTable();
        table.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"BloodGroup", "Units"}
        ));
        table.setBounds(10, 155, 788, 201);
        contentPane.add(table);

        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(10, 371, 814, 2);
        contentPane.add(separator_2);

        JButton btnNewButton_1 = new JButton("Print");
        Image img1 = new ImageIcon(this.getClass().getResource("/print.png")).getImage();
        btnNewButton_1.setIcon(new ImageIcon(img1));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    table.print(JTable.PrintMode.NORMAL);
                } catch (Exception t) {
                    JOptionPane.showMessageDialog(null, t);
                }
            }
        });
        btnNewButton_1.setBounds(69, 400, 100, 31);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Close");
        Image img3 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnNewButton_2.setIcon(new ImageIcon(img3));
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btnNewButton_2.setBounds(610, 400, 100, 31);
        contentPane.add(btnNewButton_2);

        JButton btnNewButton_3 = new JButton("Display");
        Image img4 = new ImageIcon(this.getClass().getResource("/display.png")).getImage();
        btnNewButton_3.setIcon(new ImageIcon(img4));
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
                    System.out.println("Connection created");
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select * from stock");
                    ResultSetMetaData rsmd = rs.getMetaData();
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    int cols = rsmd.getColumnCount();
                    String[] colName = new String[cols];
                    for (int i = 0; i < cols; i++)
                        colName[i] = rsmd.getColumnName(i + 1);
                    model.setColumnIdentifiers(colName);
                    String BloodGroup, Units;
                    while (rs.next()) {
                        BloodGroup = rs.getString(1);
                        Units = rs.getString(2);
                        String[] row = {BloodGroup, Units};
                        model.addRow(row);
                    }
                    st.close();
                    con.close();
                } catch (Exception j) {
                    JOptionPane.showMessageDialog(null, "Error");
                }
            }
        });
        btnNewButton_3.setBounds(313, 400, 111, 31);
        contentPane.add(btnNewButton_3);
        
        JLabel lblNewLabel_3 = new JLabel("");
        Image img5 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_3.setIcon(new ImageIcon(img5));
        lblNewLabel_3.setBounds(0, 0, 834, 461);
        contentPane.add(lblNewLabel_3);
    }
}
