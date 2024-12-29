package mubeenkhan.depot.view;

import javax.swing.*;

import mubeenkhan.depot.controller.Worker;

import java.awt.*;
import java.awt.event.ActionListener;

public class DepotSystemGUI {
    private JFrame frame;  // Main window
    private JTextField parcelIDField;
    private JTextField customerNameField;
    private JTextField lengthField;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField weightField;
    private JTextField daysInDepotField;
    private JCheckBox isCollectedCheckBox;
    private JButton addParcelButton;
  //  private JButton viewParcelsButton;
    private JButton addCustomerButton;
    private JButton submitButton;
    private JButton displayParcelsButton; // New button for displaying parcels
    private JButton displayQueueButton;  // New button for displaying queue
    private JTextArea displayArea;       // Area to display CSV content

    public DepotSystemGUI() {
        frame = new JFrame("Depot System");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout and buttons
        frame.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2, 10, 10));

        addParcelButton = new JButton("Add New Parcel");
      //  viewParcelsButton = new JButton("View All Parcels");
        addCustomerButton = new JButton("Add Customer to Queue");
        displayParcelsButton = new JButton("Display Parcels"); // New button
        displayQueueButton = new JButton("Display Queue");    // New button
      //  submitButton = new JButton("Submit"); // Initialize the submit button here

        // Add buttons to the panel
        buttonPanel.add(addParcelButton);
       // buttonPanel.add(viewParcelsButton);
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(displayParcelsButton);
        buttonPanel.add(displayQueueButton);
      //  buttonPanel.add(submitButton);  // Add the button to the panel

        // Create a text area for displaying data
        displayArea = new JTextArea();
        displayArea.setEditable(false);  // Read-only
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Add components to the frame
        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
    }
 // Make sure the submit button is initialized
    public JButton getSubmitButton() {
        return submitButton;
    }

    // Show form for adding a new parcel
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
        panel.add(submitButton);  // Button added here

        JFrame parcelFormFrame = new JFrame("Add New Parcel");
        parcelFormFrame.setSize(400, 400);
        parcelFormFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        parcelFormFrame.add(panel);
        parcelFormFrame.setVisible(true);

        // Action listener for Submit Button
        submitButton.addActionListener(e -> {
            try {
                // Retrieve input values
                String parcelID = getParcelID();
                String customerName = getCustomerName();
                double length = getLength();
                double width = getWidth();
                double height = getHeight();
                double weight = getWeight();
                int daysInDepot = getDaysInDepot();
                boolean isCollected = isCollected();

                // Add the parcel using Worker class
                Worker worker = new Worker(); // Assuming Worker is initialized correctly elsewhere
                worker.addNewParcel(parcelID, isCollected, customerName, length, width, height, weight, daysInDepot);

                // Show success message
                JOptionPane.showMessageDialog(parcelFormFrame, "Parcel added successfully!");

                // Reset fields
                resetFormFields();
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


    // Display data in the text area
    public void displayData(String data) {
        displayArea.setText(data);
    }

    // Setters for listeners
    public void setAddParcelListener(ActionListener listener) {
        addParcelButton.addActionListener(listener);
    }

  //  public void setViewParcelsListener(ActionListener listener) {
   //    viewParcelsButton.addActionListener(listener);
   // }

    public void setAddCustomerListener(ActionListener listener) {
        addCustomerButton.addActionListener(listener);
    }

    public void setDisplayParcelsListener(ActionListener listener) {
        displayParcelsButton.addActionListener(listener);
    }

    public void setDisplayQueueListener(ActionListener listener) {
        displayQueueButton.addActionListener(listener);
    }

  //  public void setSubmitListener(ActionListener listener) {
    //    submitButton.addActionListener(listener);
  //  }

    public void show() {
        frame.setVisible(true);
    }
    // Getters for form fields
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
        // Create panel with grid layout
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

        // Create text field for customer name
        JTextField customerNameField = new JTextField(15);

        // Create Submit button
        JButton submitButton = new JButton("Submit");

        // Add components to the panel
        panel.add(new JLabel("Customer Name:"));
        panel.add(customerNameField);
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(submitButton);

        // Create JFrame for the customer form
        JFrame customerFormFrame = new JFrame("Add Customer to Queue");
        customerFormFrame.setSize(300, 150);
        customerFormFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        customerFormFrame.add(panel);
        customerFormFrame.setVisible(true);

        // Make the submit button the default button
        customerFormFrame.getRootPane().setDefaultButton(submitButton);

        // Attach action listener to the submit button
        submitButton.addActionListener(e -> {
            String customerName = customerNameField.getText().trim();

            // Validate the input (make sure customer name isn't empty)
            if (!customerName.isEmpty()) {
                submitListener.actionPerformed(e); // Notify the controller

                // Show success message
                JOptionPane.showMessageDialog(customerFormFrame,
                        "Customer " + customerName + " has been added to the queue.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Reset the customer name field after submission
                customerNameField.setText("");
            } else {
                // Show error message if customer name is empty
                JOptionPane.showMessageDialog(customerFormFrame,
                        "Error: Customer name cannot be empty.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Make sure Enter key triggers the submit button action
        customerFormFrame.getRootPane().setDefaultButton(submitButton);
    }



    public JFrame getFrame() {
        return frame;
    }
}
