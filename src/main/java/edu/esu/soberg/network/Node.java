package edu.esu.soberg.network;

import edu.esu.soberg.core.Point;

public class Node {
    /**
     * Effective transmission range of this node.
     */
    private final double transmissionRange;
    /**
     * Total worm size, in Bytes.
     */
    private final double totalWormSize;

    /**
     * Current location of this node.
     */
    private Point currentLocation;

    private double proportionInfected;

    public Node(double transmissionRange, double totalWormSize, int startX, int startY) {
        this.transmissionRange = transmissionRange;
        this.totalWormSize = totalWormSize;
        this.currentLocation = new Point(startX, startY);
    }

    //========================================
    // Infection
    //========================================

    /**
     * @return true if the node is completely infected, false otherwise.
     */
    public boolean infect(double transmissionRate, long timeInfectedMillis) {
        double newPercentInfected = ((transmissionRate / 1000) * timeInfectedMillis) / totalWormSize;
        proportionInfected = Math.max(1, (proportionInfected + newPercentInfected));
        return isCompletelyInfected();
    }

    /**
     * Completely infect the node.
     */
    void setInfected() {
        proportionInfected = 1;
    }

    /**
     * @return true if this node is completely infected, false otherwise.
     */
    public boolean isCompletelyInfected() {
        return (proportionInfected >= 1);
    }

    //========================================
    // Movement
    //========================================

    public void moveWithinBounds(double speed, float direction, long timeMovedMillis, int networkWidth, int networkHeight) {
        double distance = ((speed / 1000) * timeMovedMillis);

        Point moved = currentLocation.travelTowardsBearing(distance, direction);
        if (currentLocation.x <= networkWidth && currentLocation.y <= networkHeight) {
            currentLocation = moved;
        }
    }

    /**
     * @return true if the specified node is within range of this node, false otherwise.
     */
    public boolean isNodeWithinRangeOfMe(Node other) {
        return other.currentLocation.isPointInCircle(currentLocation, transmissionRange);
    }
}
