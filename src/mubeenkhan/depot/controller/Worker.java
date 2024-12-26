package mubeenkhan.depot.controller;

import mubeenkhan.depot.controller.QueueManager;
import mubeenkhan.depot.utils.CSVReader;
import mubeenkhan.depot.model.Customer;
import mubeenkhan.depot.model.Parcel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

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

    public void addNewParcel() {
        try (Scanner scanner = new Scanner(System.in)) {
            // Ensure data is initialized
            if (parcels.isEmpty()) {
                initializeData();
            }

            // Debug: Print current size of the list
            System.out.println("Current number of parcels: " + parcels.size());

            // Get parcel details from user input
            System.out.print("Enter Parcel ID: ");
            String parcelID = scanner.nextLine();

            System.out.print("Is the parcel collected (true/false): ");
            boolean isCollected = scanner.nextBoolean();
            scanner.nextLine(); // Consume the newline

            System.out.print("Enter Customer Name: ");
            String customerName = scanner.nextLine();

            System.out.print("Enter Length: ");
            double length = scanner.nextDouble();

            System.out.print("Enter Width: ");
            double width = scanner.nextDouble();

            System.out.print("Enter Height: ");
            double height = scanner.nextDouble();

            System.out.print("Enter Weight: ");
            double weight = scanner.nextDouble();

            System.out.print("Enter Days in Depot: ");
            int daysInDepot = scanner.nextInt();

            // Create a new Parcel object
            Parcel newParcel = new Parcel(parcelID, isCollected, customerName, length, width, height, weight, daysInDepot);

            // Add the new parcel to the list
            parcels.add(newParcel);

            // Debug: Print updated size of the list
            System.out.println("Updated number of parcels: " + parcels.size());

            // Save the updated parcels list back to the CSV file
            saveParcelsToCSV();

            System.out.println("New Parcel added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while adding the parcel.");
        }
    }

    

    // Method to save the parcels list to CSV file
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
    public void addCustomerToQueue() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();

        // Search for a parcel that matches the customer name
        Parcel matchingParcel = null;
        for (Parcel parcel : parcels) {
            if (parcel.getCustomerName().equalsIgnoreCase(customerName) && !parcel.isCollected()) {
                matchingParcel = parcel;
                break; // Exit the loop once a matching parcel is found
            }
        }

        // If no matching parcel is found
        if (matchingParcel == null) {
            System.out.println("No uncollected parcel found for customer: " + customerName);
            return;
        }

        // If a matching parcel is found, get the Parcel ID
        String parcelID = matchingParcel.getParcelID();

        // Add the customer to the queue with the corresponding Parcel ID
        queueManager.addCustomerToQueue(customerName, parcelID);
        System.out.println("Customer " + customerName + " has been added to the queue with Parcel ID: " + parcelID);

        // Change the parcel's isCollected status to true
        matchingParcel.setCollected(true);

        // Save the updated list of parcels to Parcels.csv
        saveParcelsToCSV();

        // Save the customer to Queue.csv
        saveCustomerToQueueCSV(customerName, parcelID);
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

}