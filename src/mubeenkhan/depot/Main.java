package mubeenkhan.depot;

import mubeenkhan.depot.controller.DepotController;

public class Main {
    public static void main(String[] args) {
        // Start the application by creating a DepotController instance
        DepotController controller = new DepotController();
        
        // Process the customers to collect their parcels and generate the report
        controller.processCustomers();
    }
}
