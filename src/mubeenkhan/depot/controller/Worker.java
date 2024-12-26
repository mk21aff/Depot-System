package mubeenkhan.depot.controller;

import mubeenkhan.depot.controller.QueueManager;
import mubeenkhan.depot.utils.CSVReader;
import mubeenkhan.depot.model.Customer;
import mubeenkhan.depot.model.Parcel;

import java.util.*;

public class Worker {

    private QueueManager queueManager = new QueueManager();

    public void initializeData() {
        List<Parcel> parcels = CSVReader.readParcelsFromCSV("Parcels.csv");
        List<Customer> customers = CSVReader.readCustomersFromCSV("Queue.csv");

        for (Parcel parcel : parcels) {
            queueManager.addParcel(parcel);
        }

        for (Customer customer : customers) {
            queueManager.addCustomerToQueue(customer.getName(), customer.getParcelID());
        }
    }

    public void processQueue() {
        queueManager.processCustomerQueue();
    }
}
