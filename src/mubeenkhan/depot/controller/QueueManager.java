package mubeenkhan.depot.controller;

import mubeenkhan.depot.model.Customer;
import mubeenkhan.depot.model.Parcel;
import java.util.*;

public class QueueManager {
    private List<Customer> customerQueue = new ArrayList<>();
    private Map<String, Parcel> parcelDepot = new HashMap<>();
    private int queueCounter = 1;

    // Modify this method to accept name and parcelID arguments
    public void addCustomerToQueue(String name, String parcelID) {
        // Ensure the parcel exists in the depot
        Parcel parcel = parcelDepot.get(parcelID);
        if (parcel != null) {
            customerQueue.add(new Customer(queueCounter++, name, parcelID));
            System.out.println("Customer " + name + " added to the queue with Parcel ID: " + parcelID);
        } else {
            System.out.println("No parcel found with ID: " + parcelID);
        }
    }

    public void processCustomerQueue() {
        for (Customer customer : customerQueue) {
            String parcelID = customer.getParcelID();
            Parcel parcel = parcelDepot.get(parcelID);
            if (parcel != null && !parcel.isCollected()) {
                parcel.setCollected(true);
                System.out.println("Collected Parcel: " + parcel + " - Customer: " + customer);
            }
        }
    }

    public void addParcel(Parcel parcel) {
        parcelDepot.put(parcel.getParcelID(), parcel);
    }

    public List<Customer> getCustomerQueue() {
        return customerQueue;
    }

    public Map<String, Parcel> getParcelDepot() {
        return parcelDepot;
    }
}
