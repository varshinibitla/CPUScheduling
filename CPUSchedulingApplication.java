package main.java;

import java.util.Scanner;

public class CPUSchedulingApplication {
    public static void main(String[] args) throws Exception {
        String path = "src/main/resources/processes.csv";
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please Enter Time Quantum : ");
        int timeQuantum = scanner.nextInt();
        scanner.close();
        Scheduler scheduler = new Scheduler();
        // Pass parameters "csv file path" and "time quantum"
        scheduler.roundRobin(path, timeQuantum);
    }
}
