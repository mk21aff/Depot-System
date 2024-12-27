package mubeenkhan.depot.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class DepotSystemGUI {
    private JFrame frame;  // Declare JFrame for the main window
    private JTextField parcelIDField;
    private JTextField customerNameField;
    private JTextField lengthField;
    private JTextField widthField;
    private JTextField heightField;
    private JTextField weightField;
    private JTextField daysInDepotField;
    private JCheckBox isCollectedCheckBox;
    private JButton addParcelButton;
    private JButton viewParcelsButton;
    private JButton addCustomerButton;
    private JButton submitButton;  // Declare the Submit button

    // Constructor where fields are initialized, but frame is not shown immediately
    public DepotSystemGUI() {
        frame = new JFrame("Depot System");  // Initialize the frame
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Close the application when the frame is closed

        // Initialize buttons
        addParcelButton = new JButton("Add New Parcel");
        viewParcelsButton = new JButton("View All Parcels");
        addCustomerButton = new JButton("Add Customer to Queue");
        submitButton = new JButton("Submit");  // Initialize the Submit button

        // Set layout for the frame
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Add the main buttons for the user to interact with
        frame.add(addParcelButton);
        frame.add(viewParcelsButton);
        frame.add(addCustomerButton);
    }

    // Method to show the parcel form when the button is clicked
    public void showParcelForm() {
        // Create and show the form for adding a new parcel when the button is clicked
        JPanel panel = new JPanel();
        parcelIDField = new JTextField(15);
        customerNameField = new JTextField(15);
        lengthField = new JTextField(15);
        widthField = new JTextField(15);
        heightField = new JTextField(15);
        weightField = new JTextField(15);
        daysInDepotField = new JTextField(15);
        isCollectedCheckBox = new JCheckBox("Is Collected");

        // Add the input fields to the panel
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

        // Add the Submit button to the panel
        panel.add(submitButton);

        // Create a new window to show the form and add the panel to it
        JFrame parcelFormFrame = new JFrame("Add New Parcel");
        parcelFormFrame.setSize(400, 400);
        parcelFormFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        parcelFormFrame.add(panel);
        parcelFormFrame.setVisible(true);  // Show the form when the button is clicked
    }

    // Method to view all parcels (for testing, you can add the appropriate action)
    public void viewParcels() {
        // Logic to view parcels, this might involve showing another window or displaying data
        JOptionPane.showMessageDialog(frame, "Viewing all parcels...");
    }

    // Method to add customer to queue (for testing, you can add the appropriate action)
    public void addCustomerToQueue() {
        // Logic to add a customer to the queue
        JOptionPane.showMessageDialog(frame, "Adding customer to queue...");
    }

    // Getter methods to retrieve values entered by the user in the parcel form
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

    // Setters for the listeners
    public void setAddParcelListener(ActionListener listener) {
        addParcelButton.addActionListener(listener);  // Set the listener for "Add New Parcel" button
    }

    public void setViewParcelsListener(ActionListener listener) {
        viewParcelsButton.addActionListener(listener);  // Set the listener for "View Parcels" button
    }

    public void setAddCustomerListener(ActionListener listener) {
        addCustomerButton.addActionListener(listener);  // Set the listener for "Add Customer" button
    }

    // Setter for the Submit button action listener
    public void setSubmitListener(ActionListener listener) {
        submitButton.addActionListener(listener);  // Set the listener for the Submit button
    }

    // Show the main frame initially (this does not display the form immediately)
    public void show() {
        frame.setVisible(true);
    }
}
