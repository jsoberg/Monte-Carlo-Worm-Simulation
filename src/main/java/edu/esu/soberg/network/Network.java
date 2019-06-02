package edu.esu.soberg.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Network implements Iterable<Node> {
    private final int networkWidth;
    private final int networkHeight;
    private final List<Node> nodes;

    public Network(int width, int height) {
        this.networkWidth = width;
        this.networkHeight = height;
        this.nodes = new ArrayList<>();
    }

    public int getNetworkWidth() {
        return networkWidth;
    }

    public int getNetworkHeight() {
        return networkHeight;
    }

    public void infectNodeAtPosition(int position) {
        nodes.get(position).setInfected();
    }

    public boolean isNetworkCompletelyInfected() {
        for (Node node : nodes) {
            if (!node.isCompletelyInfected()) {
                return false;
            }
        }

        return true;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public List<Node> getInfectedNodes() {
        return getNodesWithModifier(true);
    }

    public List<Node> getUninfectedNodes() {
        return getNodesWithModifier(false);
    }

    private List<Node> getNodesWithModifier(boolean infected) {
        List<Node> infectedNodes = new ArrayList<>();

        for (Node node : nodes) {
            if (node.isCompletelyInfected() == infected) {
                infectedNodes.add(node);
            }
        }
        return infectedNodes;
    }

    public int size() {
        return nodes.size();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }
}
