package Login_Sys;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Home extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Home frame = new Home();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    @SuppressWarnings("unused")
	public Home() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 550);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Load all images
        Image imgDonor = loadImage("/add donor.png");
        Image imgAddDonor = loadImage("/add new.png");
        Image imgUpdateDonor = loadImage("/update.png");
        Image imgDonorDetails = loadImage("/details.png");
        Image imgSearchBlood = loadImage("/search user.jpg");
        Image imgSearchLocation = loadImage("/location.jpg");
        Image imgSearchBloodGroup = loadImage("/blood group.png");
        Image imgPatient = loadImage("/add donor.png");
        Image imgAddPatient = loadImage("/add new.png");
        Image imgUpdatePatient = loadImage("/update.png");
        Image imgPatientDetails = loadImage("/details.png");
        Image imgStock = loadImage("/stock.png");
        Image imgAddUnits = loadImage("/inc.png");
        Image imgDeleteUnits = loadImage("/dec.png");
        Image imgStockDetails = loadImage("/details.png");
        Image imgDeleteDonor = loadImage("/delete.png");
        Image imgDeleteDonorItem = loadImage("/delete donor.jpg");
        Image imgDeletePatientItem = loadImage("/delete donor.jpg");
        Image imgExit = loadImage("/exit.png");
        Image imgHistory = loadImage("/close application.png");
        Image imgLogout = loadImage("/logout.jpg");
        Image imgBackground = loadImage("/back.jpg");
        Image imghospital = loadImage("/hospital.png");
        JMenu stockMenu_1 = new JMenu("Request");
        stockMenu_1.setIcon(new ImageIcon(imgDonorDetails));
        menuBar.add(stockMenu_1);
        
        JMenuItem mntmDonorRequest = new JMenuItem("Donor request");
        mntmDonorRequest.addActionListener(e -> new DonorHomePage().setVisible(true));
        mntmDonorRequest.setIcon(new ImageIcon(imgDonorDetails));
        stockMenu_1.add(mntmDonorRequest);
        
        JMenuItem mntmPatientRequest = new JMenuItem("Patient Request");
        mntmPatientRequest.addActionListener(e -> new PatientHomePage().setVisible(true));
        mntmPatientRequest.setIcon(new ImageIcon(imgDonorDetails));
        stockMenu_1.add(mntmPatientRequest);

        // Donor Menu
        JMenu donorMenu = new JMenu("Donor");
        donorMenu.setIcon(new ImageIcon(imgDonor));
        menuBar.add(donorMenu);
        
        JMenuItem addNewDonorItem = new JMenuItem("Add New Donor");
        addNewDonorItem.addActionListener(e -> new AddNewDonor().setVisible(true));
        addNewDonorItem.setIcon(new ImageIcon(imgAddDonor));
        donorMenu.add(addNewDonorItem);
        
        JMenuItem updateDonorItem = new JMenuItem("Update Donor Details");
        updateDonorItem.addActionListener(e -> new UpdateDetailsDonor().setVisible(true));
        updateDonorItem.setIcon(new ImageIcon(imgUpdateDonor));
        donorMenu.add(updateDonorItem);
        
        JMenuItem donorDetailsItem = new JMenuItem("Donor Details");
        donorDetailsItem.addActionListener(e -> new AllDonorDetails().setVisible(true));
        donorDetailsItem.setIcon(new ImageIcon(imgDonorDetails));
        donorMenu.add(donorDetailsItem);
        
        // Giving Blood Menu
        JMenu givingBloodMenu = new JMenu("Patient");
        givingBloodMenu.setIcon(new ImageIcon(imgDonor));
        menuBar.add(givingBloodMenu);
        
        JMenuItem addNewPatientItem = new JMenuItem("Add New Patient");
        addNewPatientItem.addActionListener(e -> new AddNewPatient().setVisible(true));
        addNewPatientItem.setIcon(new ImageIcon(imgAddPatient));
        givingBloodMenu.add(addNewPatientItem);
        
        JMenuItem updatePatientItem = new JMenuItem("Update Patient Details");
        updatePatientItem.addActionListener(e -> new UpdateDetailsPatient().setVisible(true));
        updatePatientItem.setIcon(new ImageIcon(imgUpdatePatient));
        givingBloodMenu.add(updatePatientItem);
        
        JMenuItem patientDetailsItem = new JMenuItem("Patient Details");
        patientDetailsItem.addActionListener(e -> new AllPatientDetails().setVisible(true));
        patientDetailsItem.setIcon(new ImageIcon(imgPatientDetails));
        givingBloodMenu.add(patientDetailsItem);
        
        // Search Blood Menu
        JMenu searchBloodMenu = new JMenu("Search Blood");
        searchBloodMenu.setIcon(new ImageIcon(imgSearchBlood));
        menuBar.add(searchBloodMenu);
        
        JMenuItem searchByLocationItem = new JMenuItem("Location");
        searchByLocationItem.addActionListener(e -> new SearchBloodDonorLocation().setVisible(true));
        searchByLocationItem.setIcon(new ImageIcon(imgSearchLocation));
        searchBloodMenu.add(searchByLocationItem);
        
        JMenuItem searchByBloodGroupItem = new JMenuItem("Blood Group");
        searchByBloodGroupItem.addActionListener(e -> new SearchBloodDonorBloodGroup().setVisible(true));
        searchByBloodGroupItem.setIcon(new ImageIcon(imgSearchBloodGroup));
        searchBloodMenu.add(searchByBloodGroupItem);
        
        // Stock Menu
        JMenu stockMenu = new JMenu("Stock");
        stockMenu.setIcon(new ImageIcon(imgStock));
        menuBar.add(stockMenu);
        
        JMenuItem addUnitsItem = new JMenuItem("Add Units");
        addUnitsItem.addActionListener(e -> new StockIncrease().setVisible(true));
        addUnitsItem.setIcon(new ImageIcon(imgAddUnits));
        stockMenu.add(addUnitsItem);
        
        JMenuItem deleteUnitsItem = new JMenuItem("Delete Units");
        deleteUnitsItem.addActionListener(e -> new StockDecrease().setVisible(true));
        deleteUnitsItem.setIcon(new ImageIcon(imgDeleteUnits));
        stockMenu.add(deleteUnitsItem);
        
        JMenuItem stockDetailsItem = new JMenuItem("Details");
        stockDetailsItem.addActionListener(e -> new StockDetails().setVisible(true));
        stockDetailsItem.setIcon(new ImageIcon(imgStockDetails));
        stockMenu.add(stockDetailsItem);
        
        JMenu mnHospital = new JMenu("Hospital");
        mnHospital.setIcon(new ImageIcon(imghospital));
        menuBar.add(mnHospital);
        
        JMenuItem mntmHospital = new JMenuItem("Hospital");
        mntmHospital.addActionListener(e -> new Hospital().setVisible(true));
        mntmHospital.setIcon(new ImageIcon(imghospital));
        mnHospital.add(mntmHospital);
        
        // Delete Donor Menu
        JMenu deleteDonorMenu = new JMenu("Delete");
        deleteDonorMenu.setIcon(new ImageIcon(imgDeleteDonor));
        menuBar.add(deleteDonorMenu);
        
        JMenuItem deleteDonorItem = new JMenuItem("Delete Donor");
        deleteDonorItem.addActionListener(e -> new DeleteDonor().setVisible(true));
        deleteDonorItem.setIcon(new ImageIcon(imgDeleteDonorItem));
        deleteDonorMenu.add(deleteDonorItem);
        
        JMenuItem deletePatientItem = new JMenuItem("Delete Patient");
        deletePatientItem.addActionListener(e -> new DeletePatient().setVisible(true));
        deletePatientItem.setIcon(new ImageIcon(imgDeletePatientItem));
        deleteDonorMenu.add(deletePatientItem);
        
        // Exit Menu
        JMenu exitMenu = new JMenu("Exit");
        exitMenu.setIcon(new ImageIcon(imgExit));
        menuBar.add(exitMenu);
        
        JMenuItem historyItem = new JMenuItem("History");
        historyItem.addActionListener(e -> new RecordManager().setVisible(true));
        historyItem.setIcon(new ImageIcon(imgHistory));
        exitMenu.add(historyItem);
        
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(null, "Confirm if you want to close", "Login System", JOptionPane.YES_NO_OPTION);
            if (a == 0) {
                setVisible(false);
                new AddAdmin().setVisible(true);
            }
        });
        logoutItem.setIcon(new ImageIcon(imgLogout));
        exitMenu.add(logoutItem);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 435, 850, 54); // Adjust size and position as needed
        contentPane.add(footerPanel);
        
        JLabel backgroundLabel = new JLabel("");
        backgroundLabel.setIcon(new ImageIcon(imgBackground));
        backgroundLabel.setBounds(0, -13, 834, 527);
        contentPane.add(backgroundLabel);
    }

    private Image loadImage(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
