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
import javax.swing.JTable;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class stockDetails extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	Connection con;
	PreparedStatement pst;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					stockDetails frame = new stockDetails();
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
	public stockDetails() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Stock Details");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 34));
		lblNewLabel.setBounds(151, 22, 280, 41);
		contentPane.add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 85, 814, 4);
		contentPane.add(separator);
		
		table = new JTable();
		table.setBounds(10, 100, 814, 225);
		contentPane.add(table);
		
		JButton btnNewButton = new JButton("Print");
		Image img2=new ImageIcon(this.getClass().getResource("/print.png")).getImage();
		btnNewButton.setIcon(new ImageIcon(img2));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					table.print(JTable.PrintMode.NORMAL);	
				}catch(Exception t) {
					JOptionPane.showMessageDialog(null,t);
				}
			}
		});
		btnNewButton.setBounds(45, 371, 99, 36);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Display");
		Image img3=new ImageIcon(this.getClass().getResource("/display.png")).getImage();
		btnNewButton_1 .setIcon(new ImageIcon(img3));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms","root","zamna0");
					System.out.println("connection created");
					Statement st=con.createStatement();
					ResultSet rs=st.executeQuery("select *from stock");
					ResultSetMetaData rsmd=rs.getMetaData();
					DefaultTableModel model=(DefaultTableModel)table.getModel();
					int cols=rsmd.getColumnCount();
					String[] colName=new String[cols];
					for(int i=0;i<cols;i++)
						colName[i]=rsmd.getColumnName(i+1);
					model.setColumnIdentifiers(colName);
					String BloodGroup,Units;
					while(rs.next()) {
						BloodGroup=rs.getString(1);
						Units=rs.getString(2);
						
						String[]row= {BloodGroup,Units};
						model.addRow(row);
					}
					st.close();
					con.close();
				}catch(Exception j) {
					JOptionPane.showMessageDialog(null,"error");
				}
			}
		});
		btnNewButton_1.setBounds(335, 371, 112, 36);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Close");
		Image img1=new ImageIcon(this.getClass().getResource("/close.png")).getImage();
		btnNewButton_2.setIcon(new ImageIcon(img1));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton_2.setBounds(660, 371, 99, 36);
		contentPane.add(btnNewButton_2);
        
        
        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 336, 824, 4);
        contentPane.add(separator_1);
        
        JLabel lblNewLabel_3 = new JLabel("");
        Image img5 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_3.setIcon(new ImageIcon(img5));
        lblNewLabel_3.setBounds(0, 0, 834, 461);
        contentPane.add(lblNewLabel_3);
	}
}
