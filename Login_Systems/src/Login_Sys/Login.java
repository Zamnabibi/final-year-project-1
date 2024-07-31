package Login_Sys;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 850, 550);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		
		
		JMenu mnNewMenu = new JMenu("Donor");
		Image img=new ImageIcon(this.getClass().getResource("/add donor.png")).getImage();
		mnNewMenu.setIcon(new ImageIcon(img));
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Add New Donor");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddNewDonor().setVisible(true);
			}
		});
		Image img1=new ImageIcon(this.getClass().getResource("/add new.png")).getImage();
		mntmNewMenuItem.setIcon(new ImageIcon(img1));
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Update Donor Details");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new UpdateDetailsDonor().setVisible(true);
			}
		});
		Image img2=new ImageIcon(this.getClass().getResource("/update.png")).getImage();
		mntmNewMenuItem_1.setIcon(new ImageIcon(img2));
		mnNewMenu.add(mntmNewMenuItem_1);
		
		
		JMenu mnNewMenu_3 = new JMenu("Delete Donor");
		Image img11=new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
		mnNewMenu_3.setIcon(new ImageIcon(img11));
		menuBar.add(mnNewMenu_3);
		
		JMenuItem mntmNewMenuItem_9 = new JMenuItem("Delete Donor");
		mntmNewMenuItem_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new DeleteDonor().setVisible(true);
				
			}
		});
		Image img12=new ImageIcon(this.getClass().getResource("/delete donor.jpg")).getImage();
		mntmNewMenuItem_9.setIcon(new ImageIcon(img12));
		mnNewMenu_3.add(mntmNewMenuItem_9);
		
		JMenu mnHospital = new JMenu("Hospital");
		Image img123=new ImageIcon(this.getClass().getResource("/hospital.png")).getImage();
        mnHospital.setIcon(new ImageIcon(img123));
        menuBar.add(mnHospital);
        
        JMenuItem mntmHospital = new JMenuItem("Hospital");
        mntmHospital.addActionListener(e -> new Hospital().setVisible(true));
        Image img126=new ImageIcon(this.getClass().getResource("/hospital.png")).getImage();
        mntmHospital.setIcon(new ImageIcon(img126));
        mnHospital.add(mntmHospital);
		
		JMenu mnNewMenu_4 = new JMenu("Exit");
		Image img13=new ImageIcon(this.getClass().getResource("/exit.png")).getImage();
		mnNewMenu_4.setIcon(new ImageIcon(img13));
		menuBar.add(mnNewMenu_4);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Logout");
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a=(JOptionPane.showConfirmDialog(null,"Confirm if you want to close","Login System", JOptionPane.YES_NO_OPTION)); {
					if (a==0)
					{
						setVisible(false);
						new DonorSignupSystem().setVisible(true);
					}
				}
			}
		});
		Image img14=new ImageIcon(this.getClass().getResource("/logout.jpg")).getImage();
		mntmNewMenuItem_7.setIcon(new ImageIcon(img14));
		mnNewMenu_4.add(mntmNewMenuItem_7);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 422, 850, 55); // Adjust size and position as needed
        contentPane.add(footerPanel);
		
		JLabel lblNewLabel = new JLabel("");
		Image img145=new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img145));
		lblNewLabel.setBounds(0, -13, 834, 527);
		contentPane.add(lblNewLabel);
		
	}
}