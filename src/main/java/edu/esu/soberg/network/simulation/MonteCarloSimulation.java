package edu.esu.soberg.network.simulation;

import edu.esu.soberg.network.Network;
import edu.esu.soberg.network.Node;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MonteCarloSimulation implements NetworkSimulation {
    private static final int MAX_DEGREES = 360;

    private final Random random;

    private final int numberOfNodes;
    private final int networkWidth;
    private final int networkHeight;
    private final double transmissionRange;
    private final double transmissionRate;
    private final double totalWormSize;

    private final double averageSpeed;
    private final double avgSpeedStdDev;

    private final long simulatedTimeInterval;

    private MonteCarloSimulation(Builder builder) {
        this.random = new Random();

        this.networkWidth = builder.networkWidth;
        this.networkHeight = builder.networkHeight;
        this.numberOfNodes = builder.numberOfNodes;
        this.transmissionRange = builder.transmissionRange;
        this.transmissionRate = builder.transmissionRate;
        this.totalWormSize = builder.totalWormSize;

        this.averageSpeed = builder.avgSpeed;
        this.avgSpeedStdDev = builder.avgSpeedStdDev;

        this.simulatedTimeInterval = builder.simulatedTimeInterval;
    }

    @Override
    public SimulationResult runSimulation() {
        Network network = buildRandomNetwork();

        // Infect a random node.
        int position = random.nextInt(network.size());
        network.infectNodeAtPosition(position);

        int i = 0;
        long executionTimeBefore = System.currentTimeMillis();
        while (!network.isNetworkCompletelyInfected()) {
            runInfectionRound(network);
            moveNodes(network);
            i++;
            checkForIncrementalResultPrint(i, network);
        }
        long timeToExecute = System.currentTimeMillis() - executionTimeBefore;

        return new SimulationResult(timeToExecute, simulatedTimeInterval * i);
    }

    private static final long TIME_BETWEEN_INCREMENTAL_PRINTS = TimeUnit.SECONDS.toMillis(5);

    private long lastPrintTimeMillis;

    private void checkForIncrementalResultPrint(int round, Network network) {
        if (System.currentTimeMillis() - lastPrintTimeMillis > TIME_BETWEEN_INCREMENTAL_PRINTS) {
            List<Node> infectedList = network.getInfectedNodes();
            List<Node> uninfectedList = network.getUninfectedNodes();
            System.out.println("At simulated time (" + getSimulatedTimeStringForRound(round) + "), " + infectedList.size() + " nodes are infected and " + uninfectedList.size() + " are not. ");

            lastPrintTimeMillis = System.currentTimeMillis();
        }
    }

    private String getSimulatedTimeStringForRound(int round) {
        long seconds = (simulatedTimeInterval * round) / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        return (days + " days, " + hours % 24 + " hours, " + minutes % 60 + " minutes, " + seconds % 60 + " seconds");
    }

    private Network buildRandomNetwork() {
        Network network = new Network(networkWidth, networkHeight);
        for (int i = 0; i < numberOfNodes; i++) {
            network.addNode(createRandomNode());
        }

        return network;
    }

    private Node createRandomNode() {
        int x = random.nextInt(networkWidth);
        int y = random.nextInt(networkHeight);

        return new Node(transmissionRange, totalWormSize, x, y);
    }

    private void runInfectionRound(Network network) {
        List<Node> infectedList = network.getInfectedNodes();
        List<Node> uninfectedList = network.getUninfectedNodes();

        for (Node infectedNode : infectedList) {
            for (Node uninfectedNode : uninfectedList) {
                if (!uninfectedNode.isCompletelyInfected()
                        && infectedNode.isNodeWithinRangeOfMe(uninfectedNode)) {
                    uninfectedNode.infect(transmissionRate, simulatedTimeInterval);
                }
            }
        }
    }

    private void moveNodes(Network network) {
        for (Node node : network) {
            boolean minus = random.nextBoolean();
            double change = random.nextDouble() * avgSpeedStdDev;

            double speed = minus ? averageSpeed - change : averageSpeed + change;
            int direction = random.nextInt(MAX_DEGREES);
            node.moveWithinBounds(speed, direction, simulatedTimeInterval, networkWidth, networkHeight);
        }
    }

    public static class Builder {
        private final int numberOfNodes;

        private int networkWidth;
        private int networkHeight;
        private double transmissionRange;
        private double transmissionRate;
        private double totalWormSize;

        private double avgSpeed;
        private double avgSpeedStdDev;

        private long simulatedTimeInterval;

        public Builder(int numberOfNodes) {
            this.numberOfNodes = numberOfNodes;
        }

        /**
         * Network size, width and height in meters.
         */
        public Builder setNetworkSize(int width, int height) {
            this.networkWidth = width;
            this.networkHeight = height;
            return this;
        }

        /**
         * Transmission range, in meters.
         */
        public Builder setTransmissionRange(double transmissionRange) {
            this.transmissionRange = transmissionRange;
            return this;
        }

        /**
         * Transmission rate, in Bytes per second.
         */
        public Builder setTransmissionRate(double transmissionRate) {
            this.transmissionRate = transmissionRate;
            return this;
        }

        /**
         * Total worm size, in Bytes.
         */
        public Builder setTotalWormSize(double totalWormSize) {
            this.totalWormSize = totalWormSize;
            return this;
        }

        /**
         * Time interval for simulations, in milliseconds.
         */
        public Builder setSimulatedTimeInterval(long simulatedTimeInterval) {
            this.simulatedTimeInterval = simulatedTimeInterval;
            return this;
        }

        /**
         * Speed, given in average meters per second and deviation meters per second.
         */
        public Builder setSpeed(double averageSpeed, double avgSpeedStdDev) {
            this.avgSpeed = averageSpeed;
            this.avgSpeedStdDev = avgSpeedStdDev;
            return this;
        }

        public MonteCarloSimulation build() {
            return new MonteCarloSimulation(this);
        }
    }
}
