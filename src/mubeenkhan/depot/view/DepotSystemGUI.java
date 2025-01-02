 package mubeenkhan.depot.view;

import javax.swing.*;

import mubeenkhan.depot.controller.DepotController;
import mubeenkhan.depot.controller.ServeCustomersController;
import mubeenkhan.depot.controller.Worker;
import mubeenkhan.depot.controller.LogsController;  // Ensure correct class name and package

import mubeenkhan.depot.model.Parcel;
import mubeenkhan.depot.utils.CSVReader;
import mubeenkhan.depot.utils.Helper;
import java.awt.*;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DepotSystemGUI {
    private JFrame frame;
    private JTextField parcelIDField;
    private JTextField customerNameField;
    private JTextField lengthField;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField weightField;
    private JTextField daysInDepotField;
    private JCheckBox isCollectedCheckBox;
    private JButton addParcelButton;
    private JButton addCustomerButton;
    private JButton submitButton;
    private JButton displayParcelsButton;
    private JButton displayQueueButton;
    private JButton serveCustomerButton;
    private JTextArea displayArea;

    private JButton searchParcelButton;
    private JTextField searchParcelField;
    
    private JButton sortParcelByNameButton;
    
    private JButton viewUncollectedParcelsButton;
    private JButton viewCollectedParcelsButton;
    private JButton viewLogsButton;
    
    private LogsController logsController;
    public JButton reportButton;
    private ReportWindow reportWindow; 

    public DepotSystemGUI() {
    	logsController = new LogsController();
        frame = new JFrame("Depot System");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 2, 10 ,5));  // Changed to 5 rows for the additional search section
       // buttonPanel.setPreferredSize(new Dimension(50, 300));

        addParcelButton = new JButton("Add New Parcel");
        addCustomerButton = new JButton("Add Customer to Queue");
        displayParcelsButton = new JButton("Display Parcels");
        displayQueueButton = new JButton("Display Queue");
        serveCustomerButton = new JButton("Serve Customers");

        searchParcelButton = new JButton("Search Parcel by ID");
        searchParcelField = new JTextField(5);

        sortParcelByNameButton = new JButton("Sort Parcel by Name");
        viewUncollectedParcelsButton = new JButton("View Uncollected Parcels");
        viewCollectedParcelsButton = new JButton("View Collected Parcels");
        viewLogsButton = new JButton("View Logs");
        reportButton = new JButton("Report");
        reportWindow = new ReportWindow();
        
        // Adding the regular buttons first
        buttonPanel.add(addParcelButton);
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(displayParcelsButton);
        buttonPanel.add(displayQueueButton);
        buttonPanel.add(serveCustomerButton);
        
        buttonPanel.add(sortParcelByNameButton);
        buttonPanel.add(viewUncollectedParcelsButton);
        buttonPanel.add(viewCollectedParcelsButton);
        buttonPanel.add(viewLogsButton);

        // Search Parcel section placed at the bottom
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Parcel ID to search:"));
        searchPanel.add(searchParcelField);
        searchPanel.add(searchParcelButton);
        searchPanel.add(reportButton);

        // Adding the search panel as the last item
        buttonPanel.add(searchPanel);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Adding the button panel to the top
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(searchPanel, BorderLayout.SOUTH);  // Place search section at the bottom

        serveCustomerButton.addActionListener(e -> openCustomerQueueGUI());
        viewLogsButton.addActionListener(e -> displayLogs());
        
    }

    public void setSearchParcelListener(ActionListener listener) {
        searchParcelButton.addActionListener(listener);
    }

    public String getSearchParcelID() {
        return searchParcelField.getText();
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public void showParcelForm() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));
        parcelIDField = new JTextField(15);
        customerNameField = new JTextField(15);
        lengthField = new JTextField(15);
        widthField = new JTextField(15);
        heightField = new JTextField(15);
        weightField = new JTextField(15);
        daysInDepotField = new JTextField(15);
        isCollectedCheckBox = new JCheckBox("Is Collected");
        submitButton = new JButton("Submit");

        panel.add(new JLabel("Parcel ID:"));
        panel.add(parcelIDField);
        panel.add(new JLabel("Customer Name:"));
        panel.add(customerNameField);
        panel.add(new JLabel("Length:"));
        panel.add(lengthField);
        panel.add(new JLabel("Width:"));
        panel.add(widthField);
        panel.add(new JLabel("Height:"));
        panel.add(heightField);
        panel.add(new JLabel("Weight:"));
        panel.add(weightField);
        panel.add(new JLabel("Days in Depot:"));
        panel.add(daysInDepotField);
        panel.add(isCollectedCheckBox);
        panel.add(submitButton);

        JFrame parcelFormFrame = new JFrame("Add New Parcel");
        parcelFormFrame.setSize(400, 400);
        parcelFormFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        parcelFormFrame.add(panel);
        parcelFormFrame.setVisible(true);

        submitButton.addActionListener(e -> {
            try {
                String parcelID = getParcelID();
                String customerName = getCustomerName();
                double length = getLength();
                double width = getWidth();
                double height = getHeight();
                double weight = getWeight();
                int daysInDepot = getDaysInDepot();
                boolean isCollected = isCollected();

                if (!Helper.isValidParcelID(parcelID)) {
                    JOptionPane.showMessageDialog(parcelFormFrame, 
                        "Invalid Parcel ID. Must start with C or X and be followed by two digits.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Worker worker = new Worker();
                worker.addNewParcel(parcelID, isCollected, customerName, length, width, height, weight, daysInDepot);

                JOptionPane.showMessageDialog(parcelFormFrame, "Parcel added successfully!");
                resetFormFields();
                
                // Refresh data in memory
                worker.initializeData(); // Reload customer queue and parcel data
             // Notify the controller to handle the addition and refresh
              //  controller.addNewParcelAndRefresh(parcelID, isCollected, customerName, length, width, height, weight, daysInDepot);

             //   gui.updateView(worker);  // Update GUI to reflect changes

                
//                // Trigger GUI refresh
//                DepotController controller = new DepotController(worker, this);
//                controller.refreshParcels(); // Refresh the displayed parcels
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parcelFormFrame, 
                    "Error: Invalid input. Please check your values.\n" + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void resetFormFields() {
        parcelIDField.setText("");
        customerNameField.setText("");
        lengthField.setText("");
        widthField.setText("");
        heightField.setText("");
        weightField.setText("");
        daysInDepotField.setText("");
        isCollectedCheckBox.setSelected(false);
    }

    public void displayData(String data) {
        displayArea.setText(data);
    }

    public void setAddParcelListener(ActionListener listener) {
        addParcelButton.addActionListener(listener);
    }

    public void setAddCustomerListener(ActionListener listener) {
        addCustomerButton.addActionListener(listener);
    }

    public void setDisplayParcelsListener(ActionListener listener) {
        displayParcelsButton.addActionListener(listener);
    }

    public void setDisplayQueueListener(ActionListener listener) {
        displayQueueButton.addActionListener(listener);
    }

    public void show() {
        frame.setVisible(true);
    }

    public String getParcelID() {
        return parcelIDField.getText();
    }

    public String getCustomerName() {
        return customerNameField.getText();
    }

    public double getLength() {
        return Double.parseDouble(lengthField.getText());
    }

    public double getWidth() {
        return Double.parseDouble(widthField.getText());
    }

    public double getHeight() {
        return Double.parseDouble(heightField.getText());
    }

    public double getWeight() {
        return Double.parseDouble(weightField.getText());
    }

    public int getDaysInDepot() {
        return Integer.parseInt(daysInDepotField.getText());
    }

    public boolean isCollected() {
        return isCollectedCheckBox.isSelected();
    }

    public void showCustomerForm(ActionListener submitListener) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField customerNameField = new JTextField(15);
        JButton submitButton = new JButton("Submit");

        panel.add(new JLabel("Customer Name:"));
        panel.add(customerNameField);
        panel.add(new JLabel());
        panel.add(submitButton);

        JFrame customerFormFrame = new JFrame("Add Customer to Queue");
        customerFormFrame.setSize(300, 150);
        customerFormFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        customerFormFrame.add(panel);
        customerFormFrame.setVisible(true);

        submitButton.addActionListener(e -> {
            String customerName = customerNameField.getText().trim();
            if (!customerName.isEmpty()) {
                submitListener.actionPerformed(e);
                JOptionPane.showMessageDialog(customerFormFrame,
                        "Customer " + customerName + " has been added to the queue.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                customerNameField.setText("");
            } else {
                JOptionPane.showMessageDialog(customerFormFrame,
                        "Error: Customer name cannot be empty.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void openCustomerQueueGUI() {
        SwingUtilities.invokeLater(() -> {
            SimpleCustomerQueueGUI gui = new SimpleCustomerQueueGUI();
            new ServeCustomersController(gui); // Instantiate the controller with the GUI
            gui.setVisible(true); // Display the GUI
        });
    }

    public JFrame getFrame() {
        return frame;
    }
    
    public void setSortParcelByNameListener(ActionListener listener) {
        sortParcelByNameButton.addActionListener(listener);
    }
    public void setViewUncollectedParcelsListener(ActionListener listener) {
        viewUncollectedParcelsButton.addActionListener(listener);
    }

   public void setViewCollectedParcelsListener(ActionListener listener) {
	   viewCollectedParcelsButton.addActionListener(listener);
   }


   public void displayLogs() {
	    // Create the text area to display logs
	    JTextArea logTextArea = new JTextArea();
	    logTextArea.setEditable(false);  // Make it read-only

	    // Create a JScrollPane for the text area to enable scrolling
	    JScrollPane scrollPane = new JScrollPane(logTextArea);
	    scrollPane.setPreferredSize(new Dimension(500, 300));  // Set preferred size for the scroll pane

	    // Create a panel to hold the refresh button
	    JPanel logPanel = new JPanel();
	    JButton refreshButton = new JButton("Refresh Logs");

	    // Set an action listener for the refresh button
	    refreshButton.addActionListener(e -> {
	        // Call the refreshLogs() method to reload the logs from the file
	        logsController.refreshLogs();  // Refresh the logs from the file

	        // Clear the existing content in the JTextArea
	        logTextArea.setText("");  

	        // Reload the logs from the LogsController and update the JTextArea
	        List<String> updatedLogs = logsController.getLogs();  // Get the updated logs
	        for (String log : updatedLogs) {
	            logTextArea.append(log + "\n");  // Append the logs to the JTextArea
	        }
	    });

	    // Initially load logs into JTextArea when the window is opened
	    List<String> initialLogs = logsController.getLogs();  // Fetch current logs
	    for (String log : initialLogs) {
	        logTextArea.append(log + "\n");  // Append the logs to the JTextArea
	    }

	    // Layout the panel with the refresh button and the scroll pane
	    logPanel.setLayout(new BorderLayout());
	    logPanel.add(refreshButton, BorderLayout.NORTH);  // Place the button at the top
	    logPanel.add(scrollPane, BorderLayout.CENTER);    // Place the scroll pane in the center

	    // Show the logs window with the refresh button and scroll pane
	    JOptionPane.showMessageDialog(frame, logPanel, "Logs", JOptionPane.INFORMATION_MESSAGE);
	}


   // Method to log an action (this could be called when performing an action in the app)
   public void logAction(String action) {
       logsController.logAction(action);
       displayLogs(); 
   }

}