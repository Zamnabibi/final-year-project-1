package Login_Sys;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.awt.Font;
public class allDonorDetails extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	Connection con;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					allDonorDetails frame = new allDonorDetails();
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
	public allDonorDetails() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Donor Details");
		lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD | Font.ITALIC, 60));
		lblNewLabel.setBounds(99, 11, 556, 76);
		contentPane.add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 85, 1323, 2);
		contentPane.add(separator);
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "FatherName", "MotherName", "DOB", "MobileNo", "Gender", "Email", "BloodGroup", "City", "Address"
			}
		));
		table.setBounds(17, 98, 804, 284);
		contentPane.add(table);
		
		JButton btnNewButton = new JButton("Print");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
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
		btnNewButton.setBounds(39, 406, 116, 31);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		Image img1=new ImageIcon(this.getClass().getResource("/close.png")).getImage();
		btnNewButton_1.setIcon(new ImageIcon(img1));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton_1.setBounds(690, 405, 106, 33);
		contentPane.add(btnNewButton_1);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 393, 1323, 2);
		contentPane.add(separator_1);
		
		JButton btnNewButton_2 = new JButton("Display");
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		Image img3=new ImageIcon(this.getClass().getResource("/display.png")).getImage();
		btnNewButton_2 .setIcon(new ImageIcon(img3));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms","root","zamna0");
					System.out.println("connection created");
					Statement st=con.createStatement();
					ResultSet rs=st.executeQuery("select *from donor");
					ResultSetMetaData rsmd=rs.getMetaData();
					DefaultTableModel model=(DefaultTableModel)table.getModel();
					int cols=rsmd.getColumnCount();
					String[] colName=new String[cols];
					for(int i=0;i<cols;i++)
						colName[i]=rsmd.getColumnName(i+1);
					model.setColumnIdentifiers(colName);
					String DonorId,Name,FatherName,MotherName,DOB,MobileNo,Gender,Email,BloodGroup,City,Address;
					while(rs.next()) {
						DonorId=rs.getString(1);
						Name=rs.getString(2);
						FatherName=rs.getString(3);
						MotherName=rs.getString(4);
						DOB=rs.getString(5);
						MobileNo=rs.getString(6);
						Gender=rs.getString(7);
						Email=rs.getString(8);
						BloodGroup=rs.getString(9);
						City=rs.getString(10);
						Address=rs.getString(11);
						String[]row= {DonorId,Name,FatherName,MotherName,DOB,MobileNo,Gender,Email,BloodGroup,City,Address};
						model.addRow(row);
					}
					st.close();
					con.close();
				}catch(Exception j) {
					JOptionPane.showMessageDialog(null,"error");
				}
				
			}
		});
		btnNewButton_2.setBounds(340, 405, 135, 33);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel_13 = new JLabel("");
		Image img4=new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
		lblNewLabel_13.setIcon(new ImageIcon(img4));
		lblNewLabel_13.setBounds(0, -113, 1370, 749);
		contentPane.add(lblNewLabel_13);
		
	}
}
