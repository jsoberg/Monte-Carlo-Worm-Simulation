package edu.esu.soberg.network.simulation;

public interface NetworkSimulation {
    SimulationResult runSimulation();

    class SimulationResult {
        public final long executionTimeToComplete;
        public final long simulatedTimeToComplete;

        SimulationResult(long executionTime, long simulationTime) {
            this.executionTimeToComplete = executionTime;
            this.simulatedTimeToComplete = simulationTime;
        }
    }
}
