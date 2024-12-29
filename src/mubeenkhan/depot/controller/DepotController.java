package mubeenkhan.depot.controller;

import mubeenkhan.depot.view.DepotSystemGUI;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DepotController {
    private Worker worker;
    private DepotSystemGUI gui;

    public DepotController(Worker worker, DepotSystemGUI gui) {
        this.worker = worker;
        this.gui = gui;

        // Set listeners for each button
        gui.setAddParcelListener(e -> gui.showParcelForm()); // Show form when clicked
       // gui.setViewParcelsListener(e -> worker.viewParcels());
        gui.setDisplayParcelsListener(e -> worker.viewParcels());

        // Updated Add Customer Listener
        gui.setAddCustomerListener(e -> {
            // Create a custom panel for customer input
            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
            JTextField customerNameField = new JTextField(15);
            panel.add(new JLabel("Customer Name:"));
            panel.add(customerNameField);
            JButton submitButton = new JButton("Submit");
            panel.add(submitButton);

            // Create a new JFrame for the form
            JFrame customerFormFrame = new JFrame("Add Customer to Queue");
            customerFormFrame.setSize(300, 150);
            customerFormFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            customerFormFrame.add(panel);
            customerFormFrame.setVisible(true);

            // Attach an action listener to the submit button
            submitButton.addActionListener(submitEvent -> {
                String customerName = customerNameField.getText().trim();
                if (!customerName.isEmpty()) {
                    boolean isAdded = worker.addCustomerToQueue(customerName);
                    if (isAdded) {
                        JOptionPane.showMessageDialog(
                                gui.getFrame(),
                                "Customer " + customerName + " has been added to the queue.",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Clear the customer name field to add another customer
                        customerNameField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(
                                gui.getFrame(),
                                "No uncollected parcel found for customer: " + customerName,
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            gui.getFrame(),
                            "Customer name cannot be empty.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        });


        // Add listener for the submit button
//        gui.setSubmitListener(e -> {
//            try {
//                // Retrieve input values and handle the submission logic
//                String parcelID = gui.getParcelID();
//                String customerName = gui.getCustomerName();
//                double length = gui.getLength();
//                double width = gui.getWidth();
//                double height = gui.getHeight();
//                double weight = gui.getWeight();
//                int daysInDepot = gui.getDaysInDepot();
//                boolean isCollected = gui.isCollected();
//
//                // Simulate adding the parcel (Replace with actual method call to Worker)
//                boolean isAdded = true; // Assume success for demonstration
//
//                if (isAdded) {
//                    JOptionPane.showMessageDialog(gui.getFrame(), 
//                        "Parcel added successfully!", 
//                        "Success", JOptionPane.INFORMATION_MESSAGE);
//                }
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(gui.getFrame(), 
//                    "Error: Invalid input. Please check your values.\n" + ex.getMessage(), 
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        });

        gui.setDisplayParcelsListener(e -> displayCSVContent("Parcels.csv"));
        gui.setDisplayQueueListener(e -> displayCSVContent("Queue.csv"));
    }

    // Reads and displays CSV file content in the text area
    private void displayCSVContent(String fileName) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }
            gui.displayData(data.toString());
        } catch (IOException ex) {
            gui.displayData("Error: Unable to read " + fileName);
        }
    }

    public void start() {
        gui.show(); // Start the main GUI (with buttons)
    }
}
