package com.ke.consultant;

public class StudentReplicate {
    public static void main(String[] args) {
        Producer producer1 = new Producer();
        Producer producer2 = new Producer();

        Consumer consumer1 = new Consumer();
        Consumer consumer2 = new Consumer();

        producer1.start();
        producer2.start();

        consumer1.start();
        consumer2.start();
    }


}
