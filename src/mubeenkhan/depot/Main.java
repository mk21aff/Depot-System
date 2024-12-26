package mubeenkhan.depot;

import mubeenkhan.depot.controller.Worker;

public class Main {
    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.initializeData();
        worker.processQueue();
     //worker.addNewParcel();
    //View all parcels
     // worker.viewParcels();
     // Add a new customer to the queue (prompts for name and assigns a parcel)
     worker.addCustomerToQueue();
    }
}
