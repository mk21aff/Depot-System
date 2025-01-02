package mubeenkhan.depot.controller;

import mubeenkhan.depot.utils.CSVReader;
import mubeenkhan.depot.model.Logs;
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

    private static List<Parcel> parcels; // List to store all parcels
    private static QueueManager queueManager = new QueueManager(); // Manages customer queue
    private static LogsController logsController;

    // Constructor initializes the parcels list
    public Worker() {
        parcels = new ArrayList<>();
        this.logsController = new LogsController();
    }

    /**
     * Initializes parcel and customer data by reading from CSV files.
     * Adds parcels to the QueueManager's parcelDepot and customers to the queue.
     */
    public static void initializeData() {
        if (parcels.isEmpty()) {
            // Log the initialization process
            logsController.writeLog("Initializing data: Reading parcels and customers from CSV.");

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

            // Log successful initialization
            logsController.writeLog("Data initialization successful.");
        }
    }

    /**
     * Adds a customer to the queue by finding a matching uncollected parcel.
     *
     * @param customerName The name of the customer to be added to the queue.
     * @return True if the customer is added successfully; false otherwise.
     */
    public boolean addCustomerToQueue(String customerName) {
        // Log the method call
        logsController.writeLog("Adding customer to queue: " + customerName);

        // Find the first uncollected parcel by the given customer name
        Parcel matchingParcel = parcels.stream()
            .filter(parcel -> parcel.getCustomerName().equalsIgnoreCase(customerName) && !parcel.isCollected())
            .findFirst().orElse(null);

        if (matchingParcel == null) {
            // Log the failure case
            logsController.writeLog("No uncollected parcel found for customer: " + customerName);
            // If no uncollected parcel is found for the customer, show an error message
            JOptionPane.showMessageDialog(null, "No uncollected parcel found for customer: " + customerName);
            return false;
        }

        String parcelID = matchingParcel.getParcelID();

        // Check if the parcel is already in the queue
        if (Helper.isParcelInQueue(parcelID, "Queue.csv")) {
            logsController.writeLog("Parcel " + parcelID + " is already in the queue.");
            JOptionPane.showMessageDialog(null, "This parcel is already in the queue.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Add the customer to the queue
        queueManager.addCustomerToQueue(customerName, parcelID);

        // Save the customer and their parcel to the Queue.csv
        int queueNumber = queueManager.getCustomerQueue().size();
        Helper.saveCustomerToQueueCSV(customerName, parcelID, "Queue.csv", queueNumber);

        // Log the successful addition
        logsController.writeLog("Customer " + customerName + " added to the queue with parcel ID: " + parcelID);

        return true;
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
    public void addNewParcel(String parcelID, boolean isCollected, String customerName, 
            double length, double width, double height, double weight, int daysInDepot) {
        // Log the method call
        logsController.writeLog("Adding new parcel with ID: " + parcelID);

        // Validate Parcel ID
        if (!Helper.isValidParcelID(parcelID)) {
            // Log the error
            logsController.writeLog("Invalid Parcel ID: " + parcelID);
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
            logsController.writeLog("Parcel added to CSV: " + parcelID);
        } catch (IOException e) {
            // Log the error
            logsController.writeLog("Error writing parcel to CSV: " + e.getMessage());
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }

        System.out.println("Parcel added successfully: " + parcelID);

        // Reload data from CSV
        Worker.initializeData();
    }

    /**
     * Reads data from the parcels CSV file and returns it as a string.
     *
     * @return Data from the parcels CSV file.
     */
    public String getParcelsData() {
        // Log the action
        logsController.writeLog("Fetching parcels data.");
        return Helper.readCSV("Parcels.csv");
    }

    /**
     * Reads data from the queue CSV file and returns it as a string.
     *
     * @return Data from the queue CSV file.
     */
    public String getQueueData() {
        // Log the action
        logsController.writeLog("Fetching queue data.");
        return Helper.readCSV("Queue.csv");
    }
    
    // Method to view all parcels in the list
    public void viewParcels() {
        if (parcels.isEmpty()) {
            logsController.writeLog("No parcels available to view.");
            System.out.println("No parcels available.");
        } else {
            logsController.writeLog("Viewing all parcels.");
            System.out.println("List of Parcels:");
            for (Parcel parcel : parcels) {
                System.out.println(parcel);
            }
        }
    }

    public String searchParcelByID(String parcelID) {
        // Log the action
        logsController.writeLog("Searching for parcel with ID: " + parcelID);

        // Search through the parcel list or a file (replace with actual logic)
        for (Parcel parcel : parcels) {
            if (parcel.getParcelID().equals(parcelID)) {
                logsController.writeLog("Parcel found: " + parcelID);
                return parcel.toString(); // Return the parcel details as a string
            }
        }
        logsController.writeLog("Parcel with ID " + parcelID + " not found.");
        return "Parcel with ID " + parcelID + " not found.";
    }

    public List<Parcel> getParcels() {
        return parcels; // Assuming parcels is a list of all parcels
    }
}
