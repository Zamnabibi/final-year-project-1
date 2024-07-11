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

public class home extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					home frame = new home();
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
	public home() {
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
				new addNewDonor().setVisible(true);
			}
		});
		Image img1=new ImageIcon(this.getClass().getResource("/add new.png")).getImage();
		mntmNewMenuItem.setIcon(new ImageIcon(img1));
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Update Donor Details");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new updateDetailsDonor().setVisible(true);
			}
		});
		Image img2=new ImageIcon(this.getClass().getResource("/update.png")).getImage();
		mntmNewMenuItem_1.setIcon(new ImageIcon(img2));
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Donor Details");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new allDonorDetails().setVisible(true);
			}
		});
		Image img3=new ImageIcon(this.getClass().getResource("/details.png")).getImage();
		mntmNewMenuItem_2.setIcon(new ImageIcon(img3));
		mnNewMenu.add(mntmNewMenuItem_2);
		
		JMenu mnNewMenu_1 = new JMenu("Search Blood Donor");
		Image img4=new ImageIcon(this.getClass().getResource("/search user.jpg")).getImage();
		mnNewMenu_1.setIcon(new ImageIcon(img4));
		menuBar.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Location");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new searchBloodDonorLocation().setVisible(true);
			}
		});
		Image img5=new ImageIcon(this.getClass().getResource("/location.jpg")).getImage();
		mntmNewMenuItem_3.setIcon(new ImageIcon(img5));
		mnNewMenu_1.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("Blood Group");
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new searchBloodDonorBloodGroup().setVisible(true);
			}
		});
		Image img6=new ImageIcon(this.getClass().getResource("/blood group.png")).getImage();
		mntmNewMenuItem_4.setIcon(new ImageIcon(img6));
		mnNewMenu_1.add(mntmNewMenuItem_4);
		
		JMenu mnNewMenu_2 = new JMenu("Stock");
		Image img7=new ImageIcon(this.getClass().getResource("/stock.png")).getImage();
		mnNewMenu_2.setIcon(new ImageIcon(img7));
		menuBar.add(mnNewMenu_2);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("Add units");
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new stockIncrease().setVisible(true);
			}
		});
		Image img8=new ImageIcon(this.getClass().getResource("/inc.png")).getImage();
		mntmNewMenuItem_5.setIcon(new ImageIcon(img8));
		mnNewMenu_2.add(mntmNewMenuItem_5);
		
		JMenuItem mntmNewMenuItem_6 = new JMenuItem("Delete units");
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new stockDecrese().setVisible(true);
			}
		});
		Image img9=new ImageIcon(this.getClass().getResource("/dec.png")).getImage();
		mntmNewMenuItem_6.setIcon(new ImageIcon(img9));
		mnNewMenu_2.add(mntmNewMenuItem_6);
		
		JMenuItem mntmNewMenuItem_10 = new JMenuItem("Details");
		mntmNewMenuItem_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new stockDetails().setVisible(true);
			}
		});
		Image img10=new ImageIcon(this.getClass().getResource("/details.png")).getImage();
		mntmNewMenuItem_10.setIcon(new ImageIcon(img10));
		mnNewMenu_2.add(mntmNewMenuItem_10);
		
		JMenu mnNewMenu_3 = new JMenu("Delete Donor");
		Image img11=new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
		mnNewMenu_3.setIcon(new ImageIcon(img11));
		menuBar.add(mnNewMenu_3);
		
		JMenuItem mntmNewMenuItem_9 = new JMenuItem("Delete Donor");
		mntmNewMenuItem_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new deleteDonor().setVisible(true);
				
			}
		});
		Image img12=new ImageIcon(this.getClass().getResource("/delete donor.jpg")).getImage();
		mntmNewMenuItem_9.setIcon(new ImageIcon(img12));
		mnNewMenu_3.add(mntmNewMenuItem_9);
		
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
						new Login_S().setVisible(true);
					}
				}
			}
		});
		Image img14=new ImageIcon(this.getClass().getResource("/logout.jpg")).getImage();
		mntmNewMenuItem_7.setIcon(new ImageIcon(img14));
		mnNewMenu_4.add(mntmNewMenuItem_7);
		
		JMenuItem mntmNewMenuItem_8 = new JMenuItem("History");
		mntmNewMenuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new RecordManager().setVisible(true);
				}
			}
		);
		Image img15=new ImageIcon(this.getClass().getResource("/close application.png")).getImage();
		mntmNewMenuItem_8.setIcon(new ImageIcon(img15));
		mnNewMenu_4.add(mntmNewMenuItem_8);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		Image img145=new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img145));
		lblNewLabel.setBounds(0, -13, 834, 527);
		contentPane.add(lblNewLabel);
		
	}
}