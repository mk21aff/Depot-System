package mubeenkhan.depot;

import mubeenkhan.depot.controller.Worker;

public class Main {
    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.initializeData();
        worker.processQueue();
    }
}
