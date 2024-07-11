package Login_Sys;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Login_S extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	protected Component frmLoginSystem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_S frame = new Login_S();
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
	public Login_S() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 60));
		lblNewLabel.setBounds(193, 0, 445, 74);
		contentPane.add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblUsername.setBounds(321, 110, 96, 17);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPassword.setBounds(321, 196, 89, 23);
		contentPane.add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtUsername.setBounds(682, 108, 131, 20);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
		txtPassword.setBounds(682, 197, 131, 20);
		contentPane.add(txtPassword);
		
		JButton btnLogin = new JButton("Login");
		Image img=new ImageIcon(this.getClass().getResource("/OK-icon.png")).getImage();
		btnLogin.setIcon(new ImageIcon(img));
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("deprecation")
				String password = txtPassword.getText();
				String username = txtUsername.getText();
				if(password.contains("zamna")&&username.contains("aqsa")) {
					txtPassword.setText(null);
					txtUsername.setText(null);
					setVisible(false);
					new home().setVisible(true);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Invalid Login Details","Login Error", JOptionPane.ERROR_MESSAGE);
					txtPassword.setText(null);
					txtUsername.setText(null);
				}
			}
		});
		btnLogin.setBounds(49, 286, 107, 34);
		contentPane.add(btnLogin);
		
		JButton btnClose = new JButton("Close");
		Image img2=new ImageIcon(this.getClass().getResource("/close.png")).getImage();
		btnClose.setIcon(new ImageIcon(img2));
		btnClose.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmLoginSystem =new JFrame("Close");
				if(JOptionPane.showConfirmDialog(frmLoginSystem,"Confirm if you want to close","Login System", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_NO_OPTION) {
					System.exit(0);
				}
			}
		});
		btnClose.setBounds(355, 286, 119, 34);
		contentPane.add(btnClose);
		
		JButton btnReset = new JButton("Reset");
		Image img3=new ImageIcon(this.getClass().getResource("/reset-icon.png")).getImage();
		btnReset.setIcon(new ImageIcon(img3));
		btnReset.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtUsername.setText(null);
				txtPassword.setText(null);
			}
		});
		btnReset.setBounds(689, 286, 124, 34);
		contentPane.add(btnReset);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(23, 85, 1300, 2);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(23, 248, 1300, 2);
		contentPane.add(separator_1);
		
		JLabel lblNewLabel_1 = new JLabel("");
		Image img1=new ImageIcon(this.getClass().getResource("/image.jpeg")).getImage();
		lblNewLabel_1.setIcon(new ImageIcon(img1));
		lblNewLabel_1.setBounds(-77, -190, 1400, 1000);
		contentPane.add(lblNewLabel_1);
	}
}

