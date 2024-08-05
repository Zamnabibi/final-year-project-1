package Login_Sys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

@SuppressWarnings("serial")
public class HistoryFrame extends JFrame {
    private JTable historyTable;
    private Connection con;

    public HistoryFrame() {
        setTitle("History Records");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        try {
            // Initialize database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");

            // Create history table
            historyTable = new JTable();
            add(new JScrollPane(historyTable), BorderLayout.CENTER);
            JButton loadHistoryButton = new JButton("Load History");
            loadHistoryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadHistory();
                }
            });
            add(loadHistoryButton, BorderLayout.SOUTH);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHistory() {
        try {
            String query = "SELECT * FROM History";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            historyTable.setModel(buildTableModel(rs));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading history records.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Utility method to convert ResultSet to TableModel
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Column names
        Vector<String> columnNames = new Vector<>();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        // Data rows
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HistoryFrame frame = new HistoryFrame();
            frame.setVisible(true);
        });
    }
}
