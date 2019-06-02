package edu.esu.soberg;

import edu.esu.soberg.network.simulation.MonteCarloSimulation;
import edu.esu.soberg.network.simulation.NetworkSimulation;

/**
 * @author Joshua Soberg
 * April 30th, 2016
 */
public class RunSimulation {
    private static final int NUM_EXPECTED_PARAMS = 8;

    public static void main(String[] args) {
        NetworkParameters params = null;
        try {
            params = parseNetworkParameters(args);
        } catch (ArgumentException e) {
            System.err.println(e.getMessage());
            printExample();
            System.exit(1);
        }

        MonteCarloSimulation simulation = new MonteCarloSimulation.Builder(params.nodesOnNetwork)
                .setNetworkSize(params.networkSize, params.networkSize)
                .setSpeed(params.averageSpeed, params.standardDeviationSpeed)
                .setSimulatedTimeInterval(params.timeInMillis)
                .setTransmissionRange(params.transmissionRange)
                .setTransmissionRate(params.transmissionSpeed)
                .setTotalWormSize(params.totalWormSize)
                .build();

        NetworkSimulation.SimulationResult result = simulation.runSimulation();
        System.out.println("\n\nExecution time: " + result.executionTimeToComplete);
        System.out.println("Simulated time: " + getTimeString(result.simulatedTimeToComplete));
    }

    private static String getTimeString(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return (days + " days, " + hours % 24 + " hours, " + minutes % 60 + " minutes, " + seconds % 60 + " seconds");
    }

    private static void printExample() {
        StringBuilder b = new StringBuilder()
                .append("Expected parameters are as follows: ")
                .append("\n    Number of Nodes ")
                .append("\n    Network Size (in meters)")
                .append("\n    Average Speed (in meters/second)")
                .append("\n    Standard Deviation of speed (in meters/second)")
                .append("\n    Simulation Time Interval (in milliseconds)")
                .append("\n    Transmission Range (in meters)")
                .append("\n    Transmission Speed (in Bytes/second)")
                .append("\n    Total Worm Size (in Bytes)")
                .append("\n\nExample: java -jar RunSimulation.jar 2386 2589 2.24 1 5000 10 1000 2048");
        System.out.println(b.toString());
    }

    private static NetworkParameters parseNetworkParameters(String[] args) throws ArgumentException {
        if (args == null || args.length != NUM_EXPECTED_PARAMS) {
            throw new ArgumentException("Unexpected number of parameters");
        }

        NetworkParameters params = new NetworkParameters();
        try {
            params.nodesOnNetwork = Integer.parseInt(args[0]);
            params.networkSize = Integer.parseInt(args[1]);

            params.averageSpeed = Double.parseDouble(args[2]);
            params.standardDeviationSpeed = Double.parseDouble(args[3]);

            params.timeInMillis = Long.parseLong(args[4]);

            params.transmissionRange = Double.parseDouble(args[5]);
            params.transmissionSpeed = Double.parseDouble(args[6]);

            params.totalWormSize = Double.parseDouble(args[7]);
        } catch (Exception e) {
            throw new ArgumentException("Problem parsing argument", e);
        }

        return params;
    }

    private static class NetworkParameters {
        private int nodesOnNetwork;
        private int networkSize;

        private double averageSpeed;
        private double standardDeviationSpeed;

        private long timeInMillis;

        private double transmissionRange;
        private double transmissionSpeed;

        private double totalWormSize;
    }

    private static class ArgumentException extends Exception {
        ArgumentException(String message, Throwable e) {
            super(message, e);
        }

        ArgumentException(String message) {
            super(message);
        }
    }
}
