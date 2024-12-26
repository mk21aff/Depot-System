package mubeenkhan.depot.model;

public class Customer {
    private int queueNumber;
    private String name;
    private String parcelID;

    public Customer(int queueNumber, String name, String parcelID) {
        this.queueNumber = queueNumber;
        this.name = name;
        this.parcelID = parcelID;
    }

    // Getters and Setters
    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParcelID() {
        return parcelID;
    }

    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
    }

    @Override
    public String toString() {
        return "Customer [Queue No=" + queueNumber + ", Name=" + name + ", Parcel ID=" + parcelID + "]";
    }
}
