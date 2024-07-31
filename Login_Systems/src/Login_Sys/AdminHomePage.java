package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public abstract class AdminHomePage extends JFrame {
    protected JButton acceptButton;
    protected JButton rejectButton;
    protected JButton btnClose;
    protected JLabel timeLabel;
    protected JTable requestsTable;
    protected DefaultTableModel tableModel;
    protected Container contentPane;

    public AdminHomePage(String title) {
        setTitle(title);
        setSize(800, 727);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        contentPane = getContentPane();

        initializeUI();
        startClock();
        loadRequests();
        addListeners();
    }
    private void initializeUI() {
        // Set the layout manager to null for absolute positioning
        contentPane.setLayout(null);

        // Set the background color to pink
        contentPane.setBackground(Color.PINK);

        // Set up table model and table
        tableModel = new DefaultTableModel(new Object[]{"UserID", "UserName", "Password", "Email", "FullName", "Status", "Type"}, 0);
        requestsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(requestsTable);
        scrollPane.setBounds(35, 68, 720, 540);
        contentPane.add(scrollPane);

        // Add footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 648, 784, 40);
        contentPane.add(footerPanel);

        // Create and set up buttons
        acceptButton = new JButton("Accept");
        acceptButton.setBounds(204, 28, 119, 29);
        contentPane.add(acceptButton);
        acceptButton.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/OK-icon.png")).getImage()));

        rejectButton = new JButton("Reject");
        rejectButton.setBounds(395, 28, 120, 29);
        contentPane.add(rejectButton);
        rejectButton.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/close.png")).getImage()));

        btnClose = new JButton("Close");
        btnClose.setBounds(601, 26, 120, 31);
        contentPane.add(btnClose);

        // Add close button action listener
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnClose.addActionListener(e -> setVisible(false));
        btnClose.setIcon(new ImageIcon(new ImageIcon(this.getClass().getResource("/close.png")).getImage()));

        // Create and set up time label
        timeLabel = new JLabel();
        timeLabel.setBounds(24, 11, 152, 17);
        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(timeLabel);
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        updateTime();
    }

    protected void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
    }

    protected abstract void loadRequests();

    protected abstract void addListeners();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminHomePage("Admin Home Page") {
            @Override
            protected void loadRequests() {
                // Implementation to be provided by subclasses
            }

            @Override
            protected void addListeners() {
                // Implementation to be provided by subclasses
            }
        }.setVisible(true));
    }
}
