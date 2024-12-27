package mubeenkhan.depot.controller;

import mubeenkhan.depot.view.DepotSystemGUI;

public class DepotController {
    private Worker worker;
    private DepotSystemGUI gui;

    public DepotController(Worker worker, DepotSystemGUI gui) {
        this.worker = worker;
        this.gui = gui;

        // Set listeners for each button
        gui.setAddParcelListener(e -> gui.showParcelForm());  // Show form when clicked
        gui.setViewParcelsListener(e -> worker.viewParcels());
        gui.setAddCustomerListener(e -> worker.addCustomerToQueue());
        
        // Set listener for the submit button in the form
        gui.setSubmitListener(e -> {
            // Handle form submission, e.g., get the values and add the parcel
            String parcelID = gui.getParcelID();
            String customerName = gui.getCustomerName();
            double length = gui.getLength();
            double width = gui.getWidth();
            double height = gui.getHeight();
            double weight = gui.getWeight();
            int daysInDepot = gui.getDaysInDepot();
            boolean isCollected = gui.isCollected();

            // Call worker to add the new parcel
            worker.addNewParcel(parcelID, isCollected, customerName, length, width, height, weight, daysInDepot);
        });
    }

    public void start() {
        gui.show();  // Start the main GUI (with buttons)
    }
}
