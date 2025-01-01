package mubeenkhan.depot;

import mubeenkhan.depot.controller.Worker;

//import javax.swing.SwingUtilities;
//import javax.swing.*;

import mubeenkhan.depot.controller.DepotController;
//import mubeenkhan.depot.controller.ServeCustomersController;

import mubeenkhan.depot.view.DepotSystemGUI;
//import mubeenkhan.depot.view.SimpleCustomerQueueGUI;

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


//public class Main {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SimpleCustomerQueueGUI gui = new SimpleCustomerQueueGUI();
//            new ServeCustomersController(gui); // Instantiate the controller with the GUI
//            gui.setVisible(true); // Display the GUI
//        });
//    }
//}