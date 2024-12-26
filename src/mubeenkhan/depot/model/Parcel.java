package mubeenkhan.depot.model;

public class Parcel {
    private String id;           // The unique ID for the parcel
    private int daysInDepot;     // Number of days the parcel has been in the depot
    private double weight;       // The weight of the parcel
    private String dimensions;   // The dimensions (length x width x height) of the parcel
    private boolean isCollected; // A flag to indicate whether the parcel has been collected

    // Constructor to initialize a new Parcel object with necessary details
    public Parcel(String id, int daysInDepot, double weight, String dimensions) {
        this.id = id;
        this.daysInDepot = daysInDepot;
        this.weight = weight;
        this.dimensions = dimensions;
        this.isCollected = false; // Parcel is initially not collected
    }

    // Getters for the parcel properties
    public String getId() {
        return id;
    }

    public int getDaysInDepot() {
        return daysInDepot;
    }

    public double getWeight() {
        return weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public boolean isCollected() {
        return isCollected;
    }

    // Set the status of the parcel as collected
    public void setCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }

    // This method calculates the collection fee based on various factors
    public double calculateCollectionFee() {
        // Base fee calculated from weight and number of days in depot
        double fee = weight * 0.5 + daysInDepot * 1.0;

        // Apply a discount if the parcel's ID starts with 'C'
        if (id.startsWith("C")) {
            fee -= 2.0; // A discount for parcels starting with 'C'
        }
        return fee;
    }

    // Override toString() to provide a meaningful string representation of the Parcel
    @Override
    public String toString() {
        return "Parcel [ID=" + id + ", Days in Depot=" + daysInDepot + ", Weight=" + weight + ", Dimensions=" + dimensions + "]";
    }
}
