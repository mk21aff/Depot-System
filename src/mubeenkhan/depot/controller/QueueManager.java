package mubeenkhan.depot.controller;

import mubeenkhan.depot.model.Customer;
import mubeenkhan.depot.model.Parcel;
import java.util.*;

public class QueueManager {

    private List<Customer> customerQueue = new ArrayList<>(); // List to manage customers in the queue
    private Map<String, Parcel> parcelDepot = new HashMap<>(); // Map to store parcels by Parcel ID
    private int queueCounter = 1; // Counter to track queue number

    // Method to add a customer to the queue, using customer name and parcel ID
    public void addCustomerToQueue(String name, String parcelID) {
        // Ensure the parcel exists in the depot
        Parcel parcel = parcelDepot.get(parcelID);
        if (parcel != null && !parcel.isCollected()) {
            // Create a new customer and add to the queue
            customerQueue.add(new Customer(queueCounter++, name, parcelID));
            System.out.println("Customer " + name + " added to the queue with Parcel ID: " + parcelID);
        } else {
            // Parcel is either not found or already collected
            System.out.println("No uncollected parcel found with ID: " + parcelID);
        }
    }

    // Method to process the customer queue and mark parcels as collected
    public void processCustomerQueue() {
        for (Customer customer : customerQueue) {
            String parcelID = customer.getParcelID();
            Parcel parcel = parcelDepot.get(parcelID);
            if (parcel != null && !parcel.isCollected()) {
                // Mark the parcel as collected
                parcel.setCollected(true);
                System.out.println("Collected Parcel: " + parcel + " - Customer: " + customer);
            }
        }
    }

    // Method to add a parcel to the parcel depot (parcels list)
    public void addParcel(Parcel parcel) {
        parcelDepot.put(parcel.getParcelID(), parcel);
        System.out.println("Parcel added to depot: " + parcel.getParcelID());
    }

    // Method to get the customer queue
    public List<Customer> getCustomerQueue() {
        return customerQueue;
    }

    // Method to get the parcel depot (the map of parcels)
    public Map<String, Parcel> getParcelDepot() {
        return parcelDepot;
    }

    // Method to check if a parcel is in the queue
    public boolean isParcelInQueue(String parcelID) {
        for (Customer customer : customerQueue) {
            if (customer.getParcelID().equals(parcelID)) {
                return true; // Parcel is already in the queue
            }
        }
        return false; // Parcel is not in the queue
    }
}
