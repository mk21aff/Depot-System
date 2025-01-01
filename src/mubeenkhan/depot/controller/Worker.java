package mubeenkhan.depot.controller;

import mubeenkhan.depot.utils.CSVReader;
import mubeenkhan.depot.utils.Helper;
import mubeenkhan.depot.model.Customer;
import mubeenkhan.depot.model.Parcel;

import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Worker {

    private List<Parcel> parcels; // List to store all parcels
    private QueueManager queueManager = new QueueManager(); // Manages customer queue

    // Constructor initializes the parcels list
    public Worker() {
        parcels = new ArrayList<>();
    }

    /**
     * Initializes parcel and customer data by reading from CSV files.
     * Adds parcels to the QueueManager's parcelDepot and customers to the queue.
     */
    public void initializeData() {
        if (parcels.isEmpty()) {
            // Read parcels and customers from CSV files
            parcels = CSVReader.readParcelsFromCSV("Parcels.csv");
            List<Customer> customers = CSVReader.readCustomersFromCSV("Queue.csv");

            // Add parcels to the QueueManager's parcelDepot
            for (Parcel parcel : parcels) {
                queueManager.addParcel(parcel); // Adds parcel to the depot
            }

            // Add customers to the queue
            for (Customer customer : customers) {
                queueManager.addCustomerToQueue(customer.getName(), customer.getParcelID());
            }
        }
    }

    /**
     * Adds a customer to the queue by finding a matching uncollected parcel.
     *
     * @param customerName The name of the customer to be added to the queue.
     * @return True if the customer is added successfully; false otherwise.
     */
    public boolean addCustomerToQueue(String customerName) {
        // Find the first uncollected parcel by the given customer name
        Parcel matchingParcel = parcels.stream()
            .filter(parcel -> parcel.getCustomerName().equalsIgnoreCase(customerName) && !parcel.isCollected())
            .findFirst().orElse(null);

        if (matchingParcel == null) {
            // If no uncollected parcel is found for the customer, show an error message
            JOptionPane.showMessageDialog(null, "No uncollected parcel found for customer: " + customerName);
            return false;
        }

        String parcelID = matchingParcel.getParcelID();

        // Check if the parcel is already in the queue
        if (Helper.isParcelInQueue(parcelID, "Queue.csv")) {
            JOptionPane.showMessageDialog(null, "This parcel is already in the queue.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Add the customer to the queue
        queueManager.addCustomerToQueue(customerName, parcelID);

        // Save the customer and their parcel to the Queue.csv
        int queueNumber = queueManager.getCustomerQueue().size();
        Helper.saveCustomerToQueueCSV(customerName, parcelID, "Queue.csv", queueNumber);
        return true;
    }

    /**
     * Processes the customer queue by serving customers in order.
     */
    public void processQueue() {
        queueManager.processCustomerQueue();
    }

    /**
     * Adds a new parcel to the parcels list and writes it to the parcels CSV file.
     * Performs validation on the parcel ID.
     *
     * @param parcelID     The unique ID of the parcel.
     * @param isCollected  Whether the parcel has been collected.
     * @param customerName The name of the customer associated with the parcel.
     * @param length       Length of the parcel.
     * @param width        Width of the parcel.
     * @param height       Height of the parcel.
     * @param weight       Weight of the parcel.
     * @param daysInDepot  Number of days the parcel has been in the depot.
     */
//    public void addNewParcel(String parcelID, boolean isCollected, String customerName, 
//                             double length, double width, double height, double weight, int daysInDepot) {
//        if (!Helper.isValidParcelID(parcelID)) {
//            JOptionPane.showMessageDialog(null, "Invalid Parcel ID. Must start with C or X and be followed by two digits.");
//            return;
//        }
//
//        String parcelData = String.join(",", parcelID, String.valueOf(isCollected), customerName, 
//                                        String.valueOf(length), String.valueOf(width), 
//                                        String.valueOf(height), String.valueOf(weight), 
//                                        String.valueOf(daysInDepot));
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("parcels.csv", true))) {
//            writer.write(parcelData);
//            writer.newLine();
//        } catch (IOException e) {
//            System.out.println("Error writing to CSV file: " + e.getMessage());
//        }
//    }
    
    public void addNewParcel(String parcelID, boolean isCollected, String customerName, 
            double length, double width, double height, double weight, int daysInDepot) {
// Validate Parcel ID
if (!Helper.isValidParcelID(parcelID)) {
JOptionPane.showMessageDialog(null, "Invalid Parcel ID. Must start with C or X and be followed by two digits.");
return;
}

// Create a Parcel object for the new parcel
Parcel newParcel = new Parcel(parcelID, isCollected, customerName, length, width, height, weight, daysInDepot);

// Add the parcel to the parcel depot (QueueManager)
queueManager.addParcel(newParcel);

// Write parcel data to CSV
String parcelData = String.join(",", parcelID, String.valueOf(isCollected), customerName, 
                      String.valueOf(length), String.valueOf(width), 
                      String.valueOf(height), String.valueOf(weight), 
                      String.valueOf(daysInDepot));

try (BufferedWriter writer = new BufferedWriter(new FileWriter("parcels.csv", true))) {
writer.write(parcelData);
writer.newLine();
} catch (IOException e) {
System.out.println("Error writing to CSV file: " + e.getMessage());
}

System.out.println("Parcel added successfully: " + parcelID);
}


    /**
     * Adds a customer to the queue by finding a matching parcel associated with the customer.
     * Ensures that the parcel is not already in the queue.
     *
     * @param customerName The name of the customer to be added to the queue.
     * @return True if the customer is added successfully; false otherwise.
     */
//    public boolean addCustomerToQueue(String customerName) {
//        Parcel matchingParcel = parcels.stream()
//            .filter(parcel -> parcel.getCustomerName().equalsIgnoreCase(customerName) && !parcel.isCollected())
//            .findFirst().orElse(null);
//
//        if (matchingParcel == null) {
//            return false;
//        }
//
//        String parcelID = matchingParcel.getParcelID();
//        if (Helper.isParcelInQueue(parcelID, "Queue.csv")) {
//            JOptionPane.showMessageDialog(null, "This parcel is already in the queue.", "Error", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//
//        queueManager.addCustomerToQueue(customerName, parcelID);
//        int queueNumber = queueManager.getCustomerQueue().size() + 1;
//        Helper.saveCustomerToQueueCSV(customerName, parcelID, "Queue.csv", queueNumber);
//        return true;
//    }

    /**
     * Reads data from the parcels CSV file and returns it as a string.
     *
     * @return Data from the parcels CSV file.
     */
    public String getParcelsData() {
        return Helper.readCSV("Parcels.csv");
    }

    /**
     * Reads data from the queue CSV file and returns it as a string.
     *
     * @return Data from the queue CSV file.
     */
    public String getQueueData() {
        return Helper.readCSV("Queue.csv");
    }
    
    // Method to view all parcels in the list
    public void viewParcels() {
        if (parcels.isEmpty()) {
            System.out.println("No parcels available.");
        } else {
            System.out.println("List of Parcels:");
            for (Parcel parcel : parcels) {
                System.out.println(parcel);
            }
        }
    }
    public String searchParcelByID(String parcelID) {
        // Search through the parcel list or a file (replace with actual logic)
        for (Parcel parcel : parcels) {
            if (parcel.getParcelID().equals(parcelID)) {
                return parcel.toString(); // Return the parcel details as a string
            }
        }
        return "Parcel with ID " + parcelID + " not found.";
    }


}
