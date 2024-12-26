package mubeenkhan.depot.model;

public class Customer {
    private int seqNo;       // Sequence number of the customer in the queue
    private String name;     // Name of the customer
    private String parcelId; // The ID of the parcel that the customer wants to collect

    // Constructor to initialize a new Customer object
    public Customer(int seqNo, String name, String parcelId) {
        this.seqNo = seqNo;
        this.name = name;
        this.parcelId = parcelId;
    }

    // Getters for the customer properties
    public int getSeqNo() {
        return seqNo;
    }

    public String getName() {
        return name;
    }

    public String getParcelId() {
        return parcelId;
    }

    // Override toString() to provide a meaningful string representation of the Customer
    @Override
    public String toString() {
        return "Customer [Seq No=" + seqNo + ", Name=" + name + ", Parcel ID=" + parcelId + "]";
    }
}
