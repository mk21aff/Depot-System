package mubeenkhan.depot.controller;

import mubeenkhan.depot.utils.Helper;

import mubeenkhan.depot.view.SimpleCustomerQueueGUI;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class ServeCustomersController {
    private final SimpleCustomerQueueGUI gui;
    private final Queue<Map<String, String>> customerQueue;
    private final LogsController logsController;
    
    public ServeCustomersController(SimpleCustomerQueueGUI gui) {
        this.gui = gui;
        this.customerQueue = new LinkedList<>();
        this.logsController = new LogsController();
        loadCustomerQueue();
        attachListeners();
    }

    private void loadCustomerQueue() {
        File file = new File("Queue.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(gui, "Queue.csv file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            logsController.writeLog("Queue.csv file not found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip the header row
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String parcelID = parts[2];

                    // Use Helper method to check if the parcel is collected
                    if (!Helper.isParcelCollected(parcelID)) {
                        Map<String, String> customer = new HashMap<>();
                        customer.put("queueNumber", parts[0]);
                        customer.put("customerName", parts[1]);
                        customer.put("parcelID", parts[2]);
                        customerQueue.add(customer);
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(gui, "Error reading Queue.csv: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logsController.writeLog("Error reading Queue.csv: " + ex.getMessage());
        }
    }


    private void attachListeners() {
        gui.setServeCustomerListener(e -> serveFirstCustomer());
        gui.setNextButtonListener(e -> serveNextCustomer());
    }

    private void serveFirstCustomer() {
        if (customerQueue.isEmpty()) {
            JOptionPane.showMessageDialog(gui, "No customers in the queue.", "Info", JOptionPane.INFORMATION_MESSAGE);
            gui.appendToDisplay("No customers to display.");
            gui.enableNextButton(false);
            logsController.writeLog("Attempted to serve first customer, but the queue is empty.");
            return;
        }

        // Display the first customer
        Map<String, String> customer = customerQueue.peek(); // Don't remove the first customer
        if (customer != null) {
            String queueNumber = customer.get("queueNumber");
            String customerName = customer.get("customerName");
            String parcelID = customer.get("parcelID");

            // Fetch parcel details
            String parcelDetails = fetchParcelDetails(parcelID);

            // Display customer and parcel details
            gui.appendToDisplay("Queue Number: " + queueNumber + "\n" +
                                "Customer Name: " + customerName + "\n" +
                                "Parcel ID: " + parcelID + "\n" +
                                "Parcel Details:\n" + parcelDetails);

            gui.enableNextButton(!customerQueue.isEmpty()); // Enable Next if there are more customers
            logsController.writeLog("Served first customer: " + customerName + " with Parcel ID: " + parcelID);
        }

        // Disable the Load Queue button after it is clicked once
        gui.loadQueueButton.setEnabled(false); // Disable the Load Queue button
        
    }



    private void serveNextCustomer() {
        if (customerQueue.isEmpty()) {
            JOptionPane.showMessageDialog(gui, "No more customers in the queue.", "Info", JOptionPane.INFORMATION_MESSAGE);
            gui.appendToDisplay("No more customers to display.");
            gui.enableNextButton(false);
            gui.loadQueueButton.setEnabled(false); // Disable Load Queue button
            logsController.writeLog("Attempted to serve next customer, but the queue is empty.");
            return;
           
        }

        Map<String, String> customer = customerQueue.poll();
        if (customer != null) {
            String queueNumber = customer.get("queueNumber");
            String customerName = customer.get("customerName");
            String parcelID = customer.get("parcelID");

            // Fetch parcel details
            String parcelDetails = fetchParcelDetails(parcelID);

            // Update parcel status to 'isCollected' in Parcels.csv
            markParcelAsCollected(parcelID);

            // Display customer and parcel details
            gui.appendToDisplay("Queue Number: " + queueNumber + "\n" +
                                "Customer Name: " + customerName + "\n" +
                                "Parcel ID: " + parcelID + "\n" +
                                "Parcel Details:\n" + parcelDetails);
            logsController.writeLog("Served next customer: " + customerName + " with Parcel ID: " + parcelID);
        }
    }

    private void markParcelAsCollected(String parcelID) {
        File file = new File("Parcels.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(gui, "Parcels.csv file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            logsController.writeLog("Parcels.csv file not found.");
            return;
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[0].equals(parcelID)) {
                    // Update the 'isCollected' field to true
                    parts[1] = "true";  // Assuming the second column represents 'isCollected'
                    line = String.join(",", parts);
                }
                lines.add(line);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(gui, "Error reading Parcels.csv: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logsController.writeLog("Error reading Parcels.csv: " + ex.getMessage());
            return;
        }

        // Rewrite the file with updated content
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(gui, "Error updating Parcels.csv: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logsController.writeLog("Error updating Parcels.csv: " + ex.getMessage());
        }
    }


    
    private String fetchParcelDetails(String parcelID) {
        File file = new File("Parcels.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(gui, "Parcels.csv file not found.", "Error", JOptionPane.ERROR_MESSAGE);
            logsController.writeLog("Parcels.csv file not found while fetching details for Parcel ID: " + parcelID);
            return "Parcel details not available.";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Skip the header row
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[0].equals(parcelID)) {
                    double length = Double.parseDouble(parts[3]);
                    double width = Double.parseDouble(parts[4]);
                    double height = Double.parseDouble(parts[5]);
                    double weight = Double.parseDouble(parts[6]);
                    int daysInDepot = Integer.parseInt(parts[7]);

                    double volume = calculateParcelVolume(length, width, height);
                  //  double collectionFee = calculateCollectionFee(volume, weight, daysInDepot);
                    double collectionFee = Helper.calculateCollectionFee(volume, weight, daysInDepot);
                   // double discountedFee = applyDiscount(parcelID, collectionFee);
                    double discountedFee = Helper.applyDiscount(parcelID, collectionFee);
                    logsController.writeLog("Fetched parcel details for Parcel ID: " + parcelID);
                    return String.format("Parcel ID: %s\nIs Collected: %s\nCustomer Name: %s\nLength: %.2f\nWidth: %.2f\nHeight: %.2f\nWeight: %.2f\nDays in Depot: %d\nVolume: %.2f cubic inches\nOriginal Fee: $%.2f\nDiscounted Fee: $%.2f",
                            parts[0], parts[1], parts[2], length, width, height, weight, daysInDepot, volume, collectionFee, discountedFee);
                }
            }
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui, "Error reading Parcels.csv: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logsController.writeLog("Error reading Parcels.csv for Parcel ID: " + parcelID + " - " + ex.getMessage());
        }

        return "Parcel details not found.";
    }

    private double calculateParcelVolume(double length, double width, double height) {
        return length * width * height;
    }

    


}
