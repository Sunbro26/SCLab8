/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConcreteVerticesGraph implements Graph<String> {
    private final List<Vertex> vertices = new ArrayList<>();

    // Adds a new vertex if it doesn’t already exist
    @Override
    public boolean add(String vertex) {
        for (Vertex v : vertices) {
            if (v.getLabel().equals(vertex)) return false;
        }
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }

    // Adds or updates a directed edge from source to target with the specified weight
    @Override
    public int set(String source, String target, int weight) {
        Vertex sourceVertex = findOrAddVertex(source);
        Vertex targetVertex = findOrAddVertex(target);
        int previousWeight = sourceVertex.getEdgeWeight(target);
        if (weight == 0) sourceVertex.removeEdge(target);
        else sourceVertex.addEdge(target, weight);
        checkRep();
        return previousWeight;
    }

    // Removes a vertex and all associated edges
    @Override
    public boolean remove(String vertex) {
        for (Iterator<Vertex> it = vertices.iterator(); it.hasNext();) {
            Vertex v = it.next();
            if (v.getLabel().equals(vertex)) {
                it.remove();
                for (Vertex other : vertices) other.removeEdge(vertex);
                checkRep();
                return true;
            }
        }
        return false;
    }

    // Returns an unmodifiable view of the vertices
    @Override
    public Set<String> vertices() {
        Set<String> result = new HashSet<>();
        for (Vertex v : vertices) result.add(v.getLabel());
        return Collections.unmodifiableSet(result);
    }

    // Returns sources pointing to target vertex
    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        for (Vertex v : vertices) {
            int weight = v.getEdgeWeight(target);
            if (weight > 0) sources.put(v.getLabel(), weight);
        }
        return sources;
    }

    // Returns targets from source vertex
    @Override
    public Map<String, Integer> targets(String source) {
        Vertex sourceVertex = findVertex(source);
        return sourceVertex == null ? Collections.emptyMap() : sourceVertex.getEdges();
    }

    // Helper to find a vertex by label, or create if not found
    private Vertex findOrAddVertex(String label) {
        Vertex vertex = findVertex(label);
        if (vertex == null) {
            vertex = new Vertex(label);
            vertices.add(vertex);
        }
        return vertex;
    }

    private Vertex findVertex(String label) {
        for (Vertex v : vertices) {
            if (v.getLabel().equals(label)) return v;
        }
        return null;
    }

    private void checkRep() {
        Set<String> labels = new HashSet<>();
        for (Vertex v : vertices) {
            assert labels.add(v.getLabel()) : "Duplicate vertex labels are not allowed";
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Vertices:\n");
        for (Vertex v : vertices) sb.append(v.toString()).append("\n");
        return sb.toString();
    }
}

public class Vertex {
    private final String label;
    private final Map<String, Integer> edges; // Maps target vertex labels to edge weights

    // Constructor for Vertex
    public Vertex(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Vertex label cannot be null");
        }
        this.label = label;
        this.edges = new HashMap<>();
        checkRep();
    }

    // Returns the label of the vertex
    public String getLabel() {
        return label;
    }

    // Adds or updates an edge to a target vertex with the specified weight
    public void addEdge(String target, int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Edge weight must be positive");
        }
        edges.put(target, weight);
        checkRep();
    }

    // Removes an edge to the specified target vertex
    public void removeEdge(String target) {
        edges.remove(target);
        checkRep();
    }

    // Returns the weight of the edge to the specified target vertex, or 0 if no edge exists
    public int getEdgeWeight(String target) {
        return edges.getOrDefault(target, 0);
    }

    // Returns a copy of the edges map (to ensure immutability)
    public Map<String, Integer> getEdges() {
        return new HashMap<>(edges);
    }

    // Checks the representation invariants
    private void checkRep() {
        assert label != null : "Vertex label cannot be null";
        for (Integer weight : edges.values()) {
            assert weight > 0 : "Edge weight must be positive";
        }
    }

    // Returns a string representation of the vertex and its edges
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(label + " -> { ");
        for (Map.Entry<String, Integer> entry : edges.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        if (!edges.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove last comma and space
        }
        sb.append(" }");
        return sb.toString();
    }
}