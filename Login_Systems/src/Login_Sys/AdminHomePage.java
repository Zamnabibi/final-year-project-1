package Login_Sys;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@SuppressWarnings("serial")
public abstract class AdminHomePage extends JFrame {
    protected JButton acceptButton;
    protected JButton rejectButton;

    protected JTable requestsTable;
    protected DefaultTableModel tableModel;

    public AdminHomePage(String title) {
        setTitle(title);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create buttons
        acceptButton = new JButton("Accept");
        rejectButton = new JButton("Reject");
       
        // Set up table model and table
        tableModel = new DefaultTableModel(new Object[]{"UserID", "UserName", "Password", "Email", "FullName", "Status", "Type"}, 0);
        requestsTable = new JTable(tableModel);

        // Set up scroll pane
        JScrollPane scrollPane = new JScrollPane(requestsTable);

        // Layout setup
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);
       
        panel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Load initial requests
        loadRequests();

        // Add action listeners
        addListeners();
    }

    protected abstract void loadRequests();

    protected abstract void addListeners();
    
    public static void main(String[] args) {
        
        
    }
}
