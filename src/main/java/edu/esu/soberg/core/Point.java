package edu.esu.soberg.core;

public class Point {

    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return Moves the point the given distance in the given direction.
     */
    public final Point travelTowardsBearing(double distance, float direction) {
        final double theta = Math.toRadians(direction);
        final double deltaX = (distance * Math.cos(theta));
        final double deltaY = (distance * Math.sin(theta));

        int newX = (x + (int) Math.round(deltaX));
        int newY = (y + (int) Math.round(deltaY));
        return new Point(newX, newY);
    }

    /**
     * @return true if the specified point p is within the specified circle, false otherwise.
     */
    public final boolean isPointInCircle(Point center, double radius) {
        return (Math.pow((x - center.x), 2) / Math.pow(radius, 2)
                + Math.pow((y - center.y), 2) / Math.pow(radius, 2)) <= 1;
    }
}
