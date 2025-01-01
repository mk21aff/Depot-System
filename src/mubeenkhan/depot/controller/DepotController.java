package mubeenkhan.depot.controller;

import mubeenkhan.depot.model.Parcel;
import mubeenkhan.depot.utils.CSVReader;
import mubeenkhan.depot.view.DepotSystemGUI;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class DepotController {
    private Worker worker;
    private DepotSystemGUI gui;

    public DepotController(Worker worker, DepotSystemGUI gui) {
        this.worker = worker;
        this.gui = gui;

        // Set listeners for each button
//        gui.setAddParcelListener(e -> gui.showParcelForm()); // Show form when clicked
        
        gui.setAddParcelListener(e -> {
            gui.showParcelForm();
            refreshParcels(); // Refresh the displayed parcels after adding
        });
       // gui.setViewParcelsListener(e -> worker.viewParcels());
        gui.setDisplayParcelsListener(e -> worker.viewParcels());
        
       // gui.setDisplayQueueListener(e -> displayCSVContent("Queue.csv"));
        
        gui.setSearchParcelListener(e -> searchParcel());
        gui.setDisplayParcelsListener(e -> displayCSVContent("Parcels.csv"));
        gui.setDisplayQueueListener(e -> displayCSVContent("Queue.csv"));

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
    
    public void searchParcel() {
        String parcelID = gui.getSearchParcelID(); // Get the Parcel ID from the GUI
        if (parcelID != null && !parcelID.isEmpty()) {
            String result = worker.searchParcelByID(parcelID); // Call the worker method to search the parcel
            gui.displayData(result); // Display search result in the GUI
        } else {
            JOptionPane.showMessageDialog(gui.getFrame(), "Please enter a Parcel ID to search.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void sortParcelsByName() {
        // Read parcels using CSVReader
        List<Parcel> parcels = CSVReader.readParcelsFromCSV("Parcels.csv");

        // Sort parcels by customer name
        parcels.sort(Comparator.comparing(Parcel::getCustomerName));

        // Prepare display data
        StringBuilder sortedData = new StringBuilder("Sorted Parcels by Name:\n");
        for (Parcel parcel : parcels) {
            sortedData.append(parcel.toString()).append("\n");
        }

        if (parcels.isEmpty()) {
            sortedData.append("No parcels found.");
        }

        // Display sorted parcels in GUI
        gui.displayData(sortedData.toString());
    }    
    public void refreshParcels() {
        List<Parcel> parcels = CSVReader.readParcelsFromCSV("Parcels.csv"); // Reload parcels from the CSV
        StringBuilder data = new StringBuilder("All Parcels:\n");
        for (Parcel parcel : parcels) {
            data.append(parcel.toString()).append("\n");
        }
        gui.displayData(data.toString()); // Update the GUI display
    }

}
