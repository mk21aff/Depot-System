package mubeenkhan.depot.controller;

import mubeenkhan.depot.model.Customer;
import mubeenkhan.depot.model.Parcel;
import java.util.*;

public class QueueManager {
    private List<Customer> customerQueue = new ArrayList<>();
    private Map<String, Parcel> parcelDepot = new HashMap<>();
    private int queueCounter = 1;

    public void addCustomerToQueue(String name, String parcelID) {
        customerQueue.add(new Customer(queueCounter++, name, parcelID));
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
