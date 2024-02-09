package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Scheduler {
    public void roundRobin(String path, int timeQuantum) throws Exception {
        // read csv file to create processes
        ArrayList<Process> processes = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            Process process = new Process();
            process.setPid(Integer.parseInt(values[0]));
            process.setArrive(Integer.parseInt(values[1]));
            process.setBurst(Integer.parseInt(values[2]));
            processes.add(process);
        }
        br.close();
        // sort processes based on arrival time
        ArrayList<Process> sortProcesses = new ArrayList<>(processes);
        for (int i = 0; i < sortProcesses.size(); i++) {
            for (int j = i + 1; j < sortProcesses.size(); j++) {
                if (sortProcesses.get(i).getArrive() > sortProcesses.get(j).getArrive()) {
                    Process temp = sortProcesses.get(i);
                    sortProcesses.set(i, sortProcesses.get(j));
                    sortProcesses.set(j, temp);
                }
            }
        }
        // create ready queue
        Queue<Process> queue = new LinkedList<>(sortProcesses);
        int n = queue.size();
        double[] turnAroundTime = new double[n];
        double[] waitTime = new double[n];
        for (int i = 0; i < n; i++) {
            Process process = queue.poll();
            if (process != null) {
                int remainingTime = process.getBurst();
                process.setRemainingTime(remainingTime);
                queue.add(process);
            }
        }
        // calculate required information
        double contextSwitchTime = 0;
        double contextSwitches = 0;
        double completionTime = 0;
        double completedProcesses = 0;
        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            int remainingTime = currentProcess.getRemainingTime();
            int executionTime = Math.min(remainingTime, timeQuantum);
            completionTime = completionTime + executionTime;
            currentProcess.setRemainingTime(remainingTime - executionTime);
            if (currentProcess.getRemainingTime() > 0) {
                queue.add(currentProcess);
            } else {
                turnAroundTime[currentProcess.getPid() - 1] = completionTime - currentProcess.getArrive();
                waitTime[currentProcess.getPid() - 1] = turnAroundTime[currentProcess.getPid() - 1] - currentProcess.getBurst();
                completedProcesses++;
            }
            if (!queue.isEmpty()) {
                contextSwitches++;
                completionTime = completionTime + contextSwitchTime;
            }
        }
        double totalTurnAroundTime = 0;
        double totalWaitTime = 0;
        for (int i = 0; i < n; i++) {
            totalTurnAroundTime = totalTurnAroundTime + turnAroundTime[i];
            totalWaitTime = totalWaitTime + waitTime[i];
        }
        double averageTurnAroundTime = totalTurnAroundTime / n;
        double averageTotalWaitTime = totalWaitTime / n;
        double throughput = completedProcesses / completionTime;
        double cpuUtilization = 1 - ((contextSwitchTime * contextSwitches) / completionTime);
        System.out.println("Average Turn Around Time : " + averageTurnAroundTime);
        System.out.println("Average Wait Time : " + averageTotalWaitTime);
        System.out.println("Throughput : " + throughput);
        System.out.println("CPU Utilization : " + cpuUtilization);
    }
}
