package mubeenkhan.depot.view;

import javax.swing.*;
import mubeenkhan.depot.controller.ServeCustomersController;
import mubeenkhan.depot.controller.Worker;
import mubeenkhan.depot.utils.Helper;
import java.awt.*;
import java.awt.event.ActionListener;

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
    
  

    public DepotSystemGUI() {
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

        // Adding the regular buttons first
        buttonPanel.add(addParcelButton);
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(displayParcelsButton);
        buttonPanel.add(displayQueueButton);
        buttonPanel.add(serveCustomerButton);

        // Search Parcel section placed at the bottom
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Parcel ID to search:"));
        searchPanel.add(searchParcelField);
        searchPanel.add(searchParcelButton);

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
}
