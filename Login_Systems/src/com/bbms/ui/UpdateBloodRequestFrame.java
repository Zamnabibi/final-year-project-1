package com.bbms.ui;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import net.proteanit.sql.DbUtils;

@SuppressWarnings("serial")
public class UpdateBloodRequestFrame extends JFrame {
    private JComboBox<Integer> requestIdComboBox;
    private JComboBox<Integer> patientIdComboBox;
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JComboBox<String> groupNameComboBox;
    private JTextField requestDateField;
    private JComboBox<String> requestStatusComboBox;
    private JButton updateButton;
    private JButton closeButton;
    private JTable bloodRequestTable;
    private JScrollPane tableScrollPane;

    public UpdateBloodRequestFrame() {
        setTitle("Update Blood Request");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set the background color of the frame
        getContentPane().setBackground(Color.pink);

        JPanel formPanel = new JPanel(new GridLayout(8, 2));
        formPanel.setBackground(Color.pink); // Set background color of form panel
        add(formPanel, BorderLayout.NORTH);

        formPanel.add(new JLabel("BloodRequestId:"));
        requestIdComboBox = new JComboBox<>();
        formPanel.add(requestIdComboBox);

        formPanel.add(new JLabel("PatientId:"));
        patientIdComboBox = new JComboBox<>();
        formPanel.add(patientIdComboBox);

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupIdComboBox = new JComboBox<>();
        formPanel.add(bloodGroupIdComboBox);

        formPanel.add(new JLabel("GroupName:"));
        groupNameComboBox = new JComboBox<>();
        formPanel.add(groupNameComboBox);

        formPanel.add(new JLabel("RequestDate (YYYY-MM-DD):"));
        requestDateField = new JTextField();
        formPanel.add(requestDateField);

        formPanel.add(new JLabel("RequestStatus:"));
        requestStatusComboBox = new JComboBox<>(new String[]{"Pending", "Approved", "Rejected"});
        formPanel.add(requestStatusComboBox);

        updateButton = new JButton("Update");
        formPanel.add(updateButton);

        closeButton = new JButton("Close");
        formPanel.add(closeButton);

        bloodRequestTable = new JTable();
        tableScrollPane = new JScrollPane(bloodRequestTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Load ComboBox Data
        loadComboBoxData();

        // Load Table Data
        loadTableData();

        updateButton.addActionListener(e -> updateBloodRequest());
        closeButton.addActionListener(e -> dispose());
    }

    private void loadComboBoxData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load BloodRequestId
            String requestQuery = "SELECT BloodRequestId FROM BloodRequest";
            PreparedStatement requestStatement = connection.prepareStatement(requestQuery);
            ResultSet requestResultSet = requestStatement.executeQuery();
            while (requestResultSet.next()) {
                requestIdComboBox.addItem(requestResultSet.getInt("BloodRequestId"));
            }

            // Load PatientId
            String patientQuery = "SELECT PatientId FROM Patient";
            PreparedStatement patientStatement = connection.prepareStatement(patientQuery);
            ResultSet patientResultSet = patientStatement.executeQuery();
            while (patientResultSet.next()) {
                patientIdComboBox.addItem(patientResultSet.getInt("PatientId"));
            }

            // Load BloodGroupId
            String bloodGroupQuery = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            while (bloodGroupResultSet.next()) {
                bloodGroupIdComboBox.addItem(bloodGroupResultSet.getInt("BloodGroupId"));
            }

            // Load GroupName
            String groupNameQuery = "SELECT DISTINCT GroupName FROM BloodGroup";
            PreparedStatement groupNameStatement = connection.prepareStatement(groupNameQuery);
            ResultSet groupNameResultSet = groupNameStatement.executeQuery();
            while (groupNameResultSet.next()) {
                groupNameComboBox.addItem(groupNameResultSet.getString("GroupName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading combo box data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedRequest() {
        int selectedRow = bloodRequestTable.getSelectedRow();
        if (selectedRow >= 0) {
            Integer requestId = (Integer) bloodRequestTable.getValueAt(selectedRow, 0);
            if (requestId != null) {
                try (Connection connection = DatabaseConnection.getConnection()) {
                    String query = "SELECT * FROM BloodRequest WHERE BloodRequestId = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, requestId);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        patientIdComboBox.setSelectedItem(resultSet.getInt("PatientId"));
                        bloodGroupIdComboBox.setSelectedItem(resultSet.getInt("BloodGroupId"));
                        groupNameComboBox.setSelectedItem(resultSet.getString("GroupName"));
                        requestDateField.setText(resultSet.getDate("RequestDate").toString());
                        requestStatusComboBox.setSelectedItem(resultSet.getString("RequestStatus"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error loading request data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void updateBloodRequest() {
        Integer requestId = (Integer) requestIdComboBox.getSelectedItem();
        Integer patientId = (Integer) patientIdComboBox.getSelectedItem();
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        String groupName = (String) groupNameComboBox.getSelectedItem();
        String requestDateText = requestDateField.getText();

        if (requestId == null || patientId == null || bloodGroupId == null || groupName == null || requestDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE BloodRequest SET PatientId = ?, BloodGroupId = ?, GroupName = ?, RequestDate = ?, RequestStatus = ? WHERE BloodRequestId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, patientId);
            statement.setInt(2, bloodGroupId);
            statement.setString(3, groupName);
            statement.setDate(4, java.sql.Date.valueOf(requestDateText));
            statement.setString(5, (String) requestStatusComboBox.getSelectedItem());
            statement.setInt(6, requestId);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blood request updated successfully.");
            loadTableData(); // Reload table data after updating
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating blood request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodRequestId, PatientId, BloodGroupId, GroupName, RequestDate, RequestStatus FROM BloodRequest";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            bloodRequestTable.setModel(DbUtils.resultSetToTableModel(resultSet));

            // Add ListSelectionListener to table
            bloodRequestTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    loadSelectedRequest();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading table data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateBloodRequestFrame().setVisible(true));
    }
}
