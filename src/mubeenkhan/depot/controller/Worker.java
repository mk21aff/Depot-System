package mubeenkhan.depot.controller;

import mubeenkhan.depot.controller.QueueManager;
import mubeenkhan.depot.utils.CSVReader;
import mubeenkhan.depot.model.Customer;
import mubeenkhan.depot.model.Parcel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

public class Worker {

    private List<Parcel> parcels; // Declare parcels as an instance variable
    private QueueManager queueManager = new QueueManager();

    // Constructor to initialize the parcels list
    public Worker() {
        parcels = new ArrayList<>();  // Initialize parcels list
    }

    public void initializeData() {
        if (parcels.isEmpty()) {
            parcels = CSVReader.readParcelsFromCSV("Parcels.csv");
            List<Customer> customers = CSVReader.readCustomersFromCSV("Queue.csv");

            for (Parcel parcel : parcels) {
                queueManager.addParcel(parcel);
            }

            for (Customer customer : customers) {
                queueManager.addCustomerToQueue(customer.getName(), customer.getParcelID());
            }
        }
    }


    public void processQueue() {
        queueManager.processCustomerQueue();
    }

    public void addNewParcel(String parcelID, boolean isCollected, String customerName, 
            double length, double width, double height, double weight, int daysInDepot) {
// Create the CSV line with the new parcel data
String parcelData = String.join(",", parcelID, String.valueOf(isCollected),customerName, 
                        String.valueOf(length), String.valueOf(width), 
                        String.valueOf(height), String.valueOf(weight), 
                        String.valueOf(daysInDepot));

// Open the CSV file in append mode
try (BufferedWriter writer = new BufferedWriter(new FileWriter("parcels.csv", true))) {
// Append the new parcel data
writer.write(parcelData);
writer.newLine(); // Adds a new line after the data

} catch (IOException e) {
// Handle exception
System.out.println("Error writing to CSV file: " + e.getMessage());
}
}


    // Method to save the parcels list to CSV file
//  
    private void saveParcelsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Parcels.csv"))) {
            for (Parcel parcel : parcels) {
                writer.write(parcel.getParcelID() + "," +
                        parcel.isCollected() + "," +
                        parcel.getCustomerName() + "," +
                        parcel.getLength() + "," +
                        parcel.getWidth() + "," +
                        parcel.getHeight() + "," +
                        parcel.getWeight() + "," +
                        parcel.getDaysInDepot());
                writer.newLine();  // Write a new line for each parcel
            }
            System.out.println("Parcels data saved to CSV successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save parcels to CSV.");
        }
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
    
 // New method to add customer to the queue (with name input)
    public boolean addCustomerToQueue(String customerName) {
        Parcel matchingParcel = null;
        for (Parcel parcel : parcels) {
            if (parcel.getCustomerName().equalsIgnoreCase(customerName) && !parcel.isCollected()) {
                matchingParcel = parcel;
                break;
            }
        }

        if (matchingParcel == null) {
            return false; // No matching parcel found
        }

        String parcelID = matchingParcel.getParcelID();
        
        // Check if the parcelID is already in the queue
        if (isParcelInQueue(parcelID)) {
            // Display the message in the GUI
            JOptionPane.showMessageDialog(null, "This parcel is already in the queue.", "Error", JOptionPane.ERROR_MESSAGE);
            return false; // Parcel already in queue, do not add again
        }

        queueManager.addCustomerToQueue(customerName, parcelID);
        saveCustomerToQueueCSV(customerName, parcelID); // Save customer to the queue CSV
        return true;
    }

    // Helper method to check if the parcelID is already in the Queue.csv
    private boolean isParcelInQueue(String parcelID) {
        try (BufferedReader reader = new BufferedReader(new FileReader("Queue.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2 && parts[2].equals(parcelID)) {
                    return true; // Parcel ID found in the queue
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Parcel ID not found in the queue
    }

    private void saveCustomerToQueueCSV(String customerName, String parcelID) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Queue.csv", true))) {
            // Find the next available queue number
            int queueNumber = queueManager.getCustomerQueue().size() + 1;  // Increment for new entry

            // Write the customer data to the CSV file
            writer.write(queueNumber + "," + customerName + "," + parcelID);
            writer.newLine();  // New line for the next customer

            System.out.println("Customer " + customerName + " has been saved to Queue.csv.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save customer to Queue.csv.");
        }
    }
    
    public String getParcelsData() {
        // Read and return data from Parcels.csv
        return readCSV("Parcels.csv");
    }

    public String getQueueData() {
        // Read and return data from Queue.csv
        return readCSV("Queue.csv");
    }

    private String readCSV(String fileName) {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line).append("\n");
            }
        } catch (IOException e) {
            data.append("Error reading ").append(fileName);
        }
        return data.toString();
    }


}