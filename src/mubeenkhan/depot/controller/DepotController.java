package mubeenkhan.depot.controller;

import mubeenkhan.depot.model.Parcel;
import mubeenkhan.depot.model.Customer;
import mubeenkhan.depot.view.ReportGenerator;
import java.util.*;

public class DepotController {
    private Map<String, Parcel> parcelDepot = new HashMap<>(); // Stores parcels by their ID
    private Queue<Customer> customerQueue = new LinkedList<>(); // Queue to store customers
    private List<Parcel> collectedParcels = new ArrayList<>();  // List to store collected parcels
    private ReportGenerator reportGenerator = new ReportGenerator(); // Report generator for final output

    // Constructor that initializes the depot with sample data for parcels and customers
    public DepotController() {
        initParcels();  // Initializes parcels with sample data
        initCustomers(); // Initializes customers with sample data
    }

    // Method to initialize the parcel depot with some example parcels
    private void initParcels() {
        parcelDepot.put("C45", new Parcel("C45", 1, 24, "4x1x2"));
        parcelDepot.put("C101", new Parcel("C101", 2, 10, "2x5x2"));
        parcelDepot.put("C105", new Parcel("C105", 10, 32, "3x3x3"));
        parcelDepot.put("C200", new Parcel("C200", 5, 8, "6x2x2"));
        parcelDepot.put("X59", new Parcel("X59", 1, 5, "4x1x2"));
    }

    // Method to initialize the customer queue with some sample customers
    private void initCustomers() {
        customerQueue.add(new Customer(1, "John Brown", "C101"));
        customerQueue.add(new Customer(2, "Mary Smith", "C200"));
        customerQueue.add(new Customer(3, "Sue Jones", "X59"));
        customerQueue.add(new Customer(4, "Tim Baker", "C44"));
        customerQueue.add(new Customer(5, "Brian Hunter", "C105"));
    }

    // Method to process all customers in the queue
    public void processCustomers() {
        // Process customers one by one
        while (!customerQueue.isEmpty()) {
            Customer customer = customerQueue.poll(); // Get the next customer from the queue
            String parcelId = customer.getParcelId(); // Get the parcel ID the customer wants

            // Check if the parcel exists and hasn't been collected yet
            Parcel parcel = parcelDepot.get(parcelId);
            if (parcel != null && !parcel.isCollected()) {
                parcel.setCollected(true); // Mark the parcel as collected
                collectedParcels.add(parcel); // Add the parcel to the collected list
                reportGenerator.generateCollectedReport(parcel, customer); // Generate a report for this parcel
            } else {
                reportGenerator.generateUncollectedReport(parcel); // Generate a report if the parcel isn't found or already collected
            }
        }

        // After processing all customers, generate the final report
        reportGenerator.generateFinalReport(collectedParcels, parcelDepot);
    }

    // Method to add a new parcel to the depot
    public void addParcel(Parcel parcel) {
        parcelDepot.put(parcel.getId(), parcel);
    }

    // Method to find a parcel by its ID
    public Parcel findParcelById(String id) {
        return parcelDepot.get(id);
    }
}
