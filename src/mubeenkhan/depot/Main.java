package mubeenkhan.depot;

import mubeenkhan.depot.controller.Worker;
import mubeenkhan.depot.controller.DepotController;
import mubeenkhan.depot.view.DepotSystemGUI;

public class Main {
    public static void main(String[] args) {
        // Instantiate the model (Worker)
        Worker worker = new Worker();
        
        // Initialize data from files
        worker.initializeData();

        // Instantiate the view (GUI)
        DepotSystemGUI gui = new DepotSystemGUI();

        // Instantiate the controller and link Worker and GUI
        DepotController controller = new DepotController(worker, gui);

        // Start the application
        controller.start(); // This will show the GUI
    }
}
