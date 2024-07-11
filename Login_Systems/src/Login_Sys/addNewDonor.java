package Login_Sys;
import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Image;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
public class addNewDonor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	JLabel lblNewLabel_8;
Connection con;
PreparedStatement pst;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					addNewDonor frame = new addNewDonor();
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
	public addNewDonor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100,850, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
	    lblNewLabel_8 = new JLabel("1");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms","root","zamna0");
			System.out.println("connection created");
		
		pst=con.prepareStatement("select max(DonorId) from donor");
		ResultSet rs=pst.executeQuery();
			
			while(rs.next())
             {
				int id=rs.getInt(1);
				id=id+1;
				String str=String.valueOf(id);
				lblNewLabel_8.setText(str);
             }
		}
		catch(Exception m) 
		{
			JOptionPane.showMessageDialog(null,m);
		}
		
	    JLabel lblNewLabel = new JLabel("Add New Donor");
		lblNewLabel.setFont(new Font("Microsoft Himalaya", Font.BOLD | Font.ITALIC, 60));
		lblNewLabel.setBounds(220, 11, 340, 54);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("New Donor ID");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(26, 81, 119, 21);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Full Name");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_2.setBounds(26, 123, 112, 21);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Father Name");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_3.setBounds(26, 155, 112, 21);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Mother Name");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_4.setBounds(26, 187, 112, 21);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Date Of Birth");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_5.setBounds(26, 219, 118, 21);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Mobile no");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_6.setBounds(26, 262, 111, 21);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("Gender");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_7.setBounds(26, 294, 88, 21);
		contentPane.add(lblNewLabel_7);
		lblNewLabel_8.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblNewLabel_8.setBounds(176, 83, 46, 14);
		contentPane.add(lblNewLabel_8);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isLetter(c)) {
					e.consume();
				}
			}
		});
		textField.setFont(new Font("Dialog", Font.BOLD, 14));
		textField.setBounds(167, 123, 161, 20);
		contentPane.add(textField);
	
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Dialog", Font.BOLD, 14));
		textField_1.setBounds(167, 155, 160, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		textField_2.setBounds(167, 262, 162, 21);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		final JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(170, 219, 158, 20);
		contentPane.add(dateChooser);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 56, 1339, 9);
		contentPane.add(separator);
		
		JComboBox<Object> comboBox = new JComboBox<Object>();
		comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] {"Male", "Female", "Others"}));
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboBox.setBounds(167, 293, 157, 22);
		contentPane.add(comboBox);
		
		JLabel lblNewLabel_9 = new JLabel("Email");
		lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_9.setBounds(26, 326, 66, 18);
		contentPane.add(lblNewLabel_9);
		
		JLabel lblNewLabel_10 = new JLabel("Blood Group");
		lblNewLabel_10.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_10.setBounds(511, 104, 119, 21);
		contentPane.add(lblNewLabel_10);
		
		JLabel lblNewLabel_11 = new JLabel("City");
		lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_11.setBounds(511, 136, 61, 21);
		contentPane.add(lblNewLabel_11);
		
		JLabel lblNewLabel_12 = new JLabel("Complete Address");
		lblNewLabel_12.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_12.setBounds(480, 163, 165, 30);
		contentPane.add(lblNewLabel_12);
		
		textField_3 = new JTextField();
		textField_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		textField_3.setBounds(167, 325, 184, 20);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
	    JComboBox<Object> comboBox_1 = new JComboBox<Object>();
		comboBox_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboBox_1.setModel(new DefaultComboBoxModel<Object>(new String[] {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB_"}));
		comboBox_1.setBounds(640, 103, 184, 22);
		contentPane.add(comboBox_1);
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		textField_4.setBounds(640, 136, 184, 20);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
		textArea.setBounds(640, 167, 182, 97);
		contentPane.add(textArea);
		
		textField_5 = new JTextField();
		textField_5.setFont(new Font("Dialog", Font.BOLD, 14));
		textField_5.setBounds(169, 187, 158, 20);
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 355, 1339, 9);
		contentPane.add(separator_1);
		
		JButton btnSave = new JButton("Save");
		Image img2=new ImageIcon(this.getClass().getResource("/save.png")).getImage();
		btnSave.setIcon(new ImageIcon(img2));
		btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			String DonorId=lblNewLabel_8.getText();
				String Name=textField.getText();
				String FatherName=textField_1.getText();
				String MotherName=textField_5.getText();
				SimpleDateFormat dFormat=new SimpleDateFormat("dd-MM-yyyy");
				String DOB=dFormat.format(dateChooser.getDate());
				String MobileNo=textField_2.getText();
				String Gender=(String)comboBox.getSelectedItem();
				String Email=textField_3.getText();
				String BloodGroup=(String)comboBox_1.getSelectedItem();
				String City=textField_4.getText();
				String Address=(String)textArea.getText();
			try {
				
				System.out.println("connection created");
					pst=con.prepareStatement("insert into donor values( "+DonorId+ ",' "+Name+"','"+FatherName+"','"+MotherName+"','"+DOB+"','"+MobileNo+"','"+Gender+"','"+Email+"','"+BloodGroup+"','"+City+"','"+Address+"');");
					pst.executeUpdate();
				JOptionPane.showMessageDialog(null,"Successfully Updated");
			    con.close();
				setVisible(false);
				new addNewDonor().setVisible(true);
				}
				catch(Exception b) {
					JOptionPane.showMessageDialog(null,b);
				}
			
			}
		});
		btnSave.setBounds(56, 393, 111, 30);
		contentPane.add(btnSave);
		
		JButton btnReset = new JButton("Reset");
		Image img3=new ImageIcon(this.getClass().getResource("/reset-icon.png")).getImage();
		btnReset.setIcon(new ImageIcon(img3));
		btnReset.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			    new addNewDonor().setVisible(true);
			}
		});
		btnReset.setBounds(352, 391, 119, 35);
		contentPane.add(btnReset);
		
		JButton btnClose = new JButton("Close");
		Image img1=new ImageIcon(this.getClass().getResource("/close.png")).getImage();
		btnClose.setIcon(new ImageIcon(img1));
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnClose.setBounds(661, 391, 119, 35);
		contentPane.add(btnClose);
		
		JLabel lblNewLabel_13 = new JLabel("");
		Image img4=new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
		lblNewLabel_13.setIcon(new ImageIcon(img4));
		lblNewLabel_13.setBounds(-21, -145, 1370, 749);
		contentPane.add(lblNewLabel_13);
	}
}