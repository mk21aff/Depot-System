package mubeenkhan.depot.model;

public class Parcel {
    private String parcelID;
    private boolean isCollected;
    private String customerName;
    private double length;
    private double width;
    private double height;
    private double weight;
    private int daysInDepot;

    // Constructor
    public Parcel(String parcelID, boolean isCollected, String customerName, double length, double width, double height, double weight, int daysInDepot) {
        this.parcelID = parcelID;
        this.isCollected = isCollected;
        this.customerName = customerName;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
    }

    // Getters and setters
    public String getParcelID() {
        return parcelID;
    }

    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getDaysInDepot() {
        return daysInDepot;
    }

    public void setDaysInDepot(int daysInDepot) {
        this.daysInDepot = daysInDepot;
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "parcelID='" + parcelID + '\'' +
                ", isCollected=" + isCollected +
                ", customerName='" + customerName + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                ", daysInDepot=" + daysInDepot +
                '}';
    }
}
