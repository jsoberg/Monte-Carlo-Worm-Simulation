# Monte-Carlo-Worm-Simulation
A rough simulation of worm propagation within a MANET, developed for an Introduction to Research graduate course.

## Expected parameters are as follows:
    1. Number of Nodes
    2. Network Size (in meters)
    3. Average Speed (in meters/second)
    4. Standard Deviation of speed (in meters/second)
    5. Simulation Time Interval (in milliseconds)
    6. Transmission Range (in meters)
    7. Transmission Speed (in Bytes/second)
    8. Total Worm Size (in Bytes)

## Example run:
- gradlew jar
- cd build/libs
- java -jar Monte-Carlo-Worm-Simulation-1.0.jar 2386 2589 2.24 1 5000 10 1000 2048
