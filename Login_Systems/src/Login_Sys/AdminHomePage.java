package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public abstract class AdminHomePage extends JFrame {
    protected JButton acceptButton;
    protected JButton rejectButton;
    protected JLabel timeLabel;

    protected JTable requestsTable;
    protected DefaultTableModel tableModel;
    protected Container contentPane;

    public AdminHomePage(String title) {
        setTitle(title);
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize content pane
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Create buttons
        acceptButton = new JButton("Accept");
        rejectButton = new JButton("Reject");

        // Set up table model and table
        tableModel = new DefaultTableModel(new Object[]{"UserID", "UserName", "Password", "Email", "FullName", "Status", "Type"}, 0);
        requestsTable = new JTable(tableModel);

        // Set up scroll pane
        JScrollPane scrollPane = new JScrollPane(requestsTable);

        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        contentPane.add(footerPanel, BorderLayout.SOUTH);

        // Add time label
        timeLabel = new JLabel(); // Changed from JTextField to JLabel
        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        JPanel timePanel = new JPanel();
        timePanel.add(timeLabel);
        contentPane.add(timePanel, BorderLayout.NORTH);

        // Set the timer to update the JLabel every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();

        // Initial time update
        updateTime();

        // Layout setup
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

        contentPane.add(buttonPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Load initial requests
        loadRequests();

        // Add action listeners
        addListeners();
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

	protected void initializeUI() {
		// TODO Auto-generated method stub
		
	}
}
