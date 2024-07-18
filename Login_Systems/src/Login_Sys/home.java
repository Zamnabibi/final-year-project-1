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

public class home extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                home frame = new home();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
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
        
        // Donor Menu
        JMenu donorMenu = new JMenu("Donor");
        Image imgDonor = loadImage("/add donor.png");
        donorMenu.setIcon(new ImageIcon(imgDonor));
        menuBar.add(donorMenu);
        
        JMenuItem addNewDonorItem = new JMenuItem("Add New Donor");
        addNewDonorItem.addActionListener(e -> new addNewDonor().setVisible(true));
        Image imgAddDonor = loadImage("/add new.png");
        addNewDonorItem.setIcon(new ImageIcon(imgAddDonor));
        donorMenu.add(addNewDonorItem);
        
        JMenuItem updateDonorItem = new JMenuItem("Update Donor Details");
        updateDonorItem.addActionListener(e -> new updateDetailsDonor().setVisible(true));
        Image imgUpdateDonor = loadImage("/update.png");
        updateDonorItem.setIcon(new ImageIcon(imgUpdateDonor));
        donorMenu.add(updateDonorItem);
        
        JMenuItem donorDetailsItem = new JMenuItem("Donor Details");
        donorDetailsItem.addActionListener(e -> new allDonorDetails().setVisible(true));
        Image imgDonorDetails = loadImage("/details.png");
        donorDetailsItem.setIcon(new ImageIcon(imgDonorDetails));
        donorMenu.add(donorDetailsItem);
        
        // Search Blood Menu
        JMenu searchBloodMenu = new JMenu("Search Blood");
        Image imgSearchBlood = loadImage("/search user.jpg");
        searchBloodMenu.setIcon(new ImageIcon(imgSearchBlood));
        menuBar.add(searchBloodMenu);
        
        JMenuItem searchByLocationItem = new JMenuItem("Location");
        searchByLocationItem.addActionListener(e -> new searchBloodDonorLocation().setVisible(true));
        Image imgSearchLocation = loadImage("/location.jpg");
        searchByLocationItem.setIcon(new ImageIcon(imgSearchLocation));
        searchBloodMenu.add(searchByLocationItem);
        
        JMenuItem searchByBloodGroupItem = new JMenuItem("Blood Group");
        searchByBloodGroupItem.addActionListener(e -> new searchBloodDonorBloodGroup().setVisible(true));
        Image imgSearchBloodGroup = loadImage("/blood group.png");
        searchByBloodGroupItem.setIcon(new ImageIcon(imgSearchBloodGroup));
        searchBloodMenu.add(searchByBloodGroupItem);
        
        // Giving Blood Menu
        JMenu givingBloodMenu = new JMenu("Patient");
        //Image imgGivingBlood = loadImage("/giving blood.png");
       // givingBloodMenu.setIcon(new ImageIcon(imgGivingBlood));
        menuBar.add(givingBloodMenu);
        
        JMenuItem addNewPatientItem = new JMenuItem("Add New Patient");
        addNewPatientItem.addActionListener(e -> new addNewPatient().setVisible(true));
        Image imgAddPatient = loadImage("/add new.png");
        addNewPatientItem.setIcon(new ImageIcon(imgAddPatient));
        givingBloodMenu.add(addNewPatientItem);
        
        JMenuItem updatePatientItem = new JMenuItem("Update Patient Details");
        updatePatientItem.addActionListener(e -> new updateDetailsPatient().setVisible(true));
        Image imgUpdatePatient = loadImage("/update.png");
        updatePatientItem.setIcon(new ImageIcon(imgUpdatePatient));
        givingBloodMenu.add(updatePatientItem);
        
        JMenuItem patientDetailsItem = new JMenuItem("Patient Details");
        patientDetailsItem.addActionListener(e -> new allPatientDetails().setVisible(true));
        Image imgPatientDetails = loadImage("/details.png");
        patientDetailsItem.setIcon(new ImageIcon(imgPatientDetails));
        givingBloodMenu.add(patientDetailsItem);
        
        // Stock Menu
        JMenu stockMenu = new JMenu("Stock");
        Image imgStock = loadImage("/stock.png");
        stockMenu.setIcon(new ImageIcon(imgStock));
        menuBar.add(stockMenu);
        
        JMenuItem addUnitsItem = new JMenuItem("Add Units");
        addUnitsItem.addActionListener(e -> new stockIncrease().setVisible(true));
        Image imgAddUnits = loadImage("/inc.png");
        addUnitsItem.setIcon(new ImageIcon(imgAddUnits));
        stockMenu.add(addUnitsItem);
        
        JMenuItem deleteUnitsItem = new JMenuItem("Delete Units");
        deleteUnitsItem.addActionListener(e -> new stockDecrese().setVisible(true));
        Image imgDeleteUnits = loadImage("/dec.png");
        deleteUnitsItem.setIcon(new ImageIcon(imgDeleteUnits));
        stockMenu.add(deleteUnitsItem);
        
        JMenuItem stockDetailsItem = new JMenuItem("Details");
        stockDetailsItem.addActionListener(e -> new stockDetails().setVisible(true));
        Image imgStockDetails = loadImage("/details.png");
        stockDetailsItem.setIcon(new ImageIcon(imgStockDetails));
        stockMenu.add(stockDetailsItem);
        
        // Delete Donor Menu
        JMenu deleteDonorMenu = new JMenu("Delete Donor");
        Image imgDeleteDonor = loadImage("/delete.png");
        deleteDonorMenu.setIcon(new ImageIcon(imgDeleteDonor));
        menuBar.add(deleteDonorMenu);
        
        JMenuItem deleteDonorItem = new JMenuItem("Delete Donor");
        deleteDonorItem.addActionListener(e -> new deleteDonor().setVisible(true));
        Image imgDeleteDonorItem = loadImage("/delete donor.jpg");
        deleteDonorItem.setIcon(new ImageIcon(imgDeleteDonorItem));
        deleteDonorMenu.add(deleteDonorItem);
        
        JMenuItem deletePatientItem = new JMenuItem("Delete Patient");
        deletePatientItem.addActionListener(e -> new deletePatient().setVisible(true));
        Image imgDeletePatientItem = loadImage("/delete donor.jpg");
        deletePatientItem.setIcon(new ImageIcon(imgDeletePatientItem));
        deleteDonorMenu.add(deletePatientItem);
        
        // Exit Menu
        JMenu exitMenu = new JMenu("Exit");
        Image imgExit = loadImage("/exit.png");
        exitMenu.setIcon(new ImageIcon(imgExit));
        menuBar.add(exitMenu);
        
        JMenuItem historyItem = new JMenuItem("History");
        Image imgHistory = loadImage("/close application.png");
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
        Image imgLogout = loadImage("/logout.jpg");
        logoutItem.setIcon(new ImageIcon(imgLogout));
        exitMenu.add(logoutItem);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel backgroundLabel = new JLabel("");
        Image imgBackground = loadImage("/back.jpg");
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
