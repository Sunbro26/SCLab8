import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConcreteEdgesGraph implements Graph<String> {
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();

    // Adds a new vertex if it doesn’t already exist
    @Override
    public boolean add(String vertex) {
        boolean added = vertices.add(vertex); // true if added, false if already exists
        checkRep();
        return added;
    }

    // Adds or updates a directed edge from source to target with the specified weight
    @Override
    public int set(String source, String target, int weight) {
        if (!vertices.contains(source)) vertices.add(source);
        if (!vertices.contains(target)) vertices.add(target);

        for (Edge edge : edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                int previousWeight = edge.getWeight();
                if (weight == 0) edges.remove(edge);
                else edges.set(edges.indexOf(edge), new Edge(source, target, weight));
                checkRep();
                return previousWeight;
            }
        }

        if (weight > 0) edges.add(new Edge(source, target, weight));
        checkRep();
        return 0;
    }

    // Removes a vertex and all associated edges
    @Override
    public boolean remove(String vertex) {
        boolean removed = vertices.remove(vertex);
        edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
        checkRep();
        return removed;
    }

    // Returns an unmodifiable view of the vertices
    @Override
    public Set<String> vertices() {
        return Collections.unmodifiableSet(vertices);
    }

    // Returns a map of sources pointing to the target vertex
    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getTarget().equals(target)) {
                sources.put(edge.getSource(), edge.getWeight());
            }
        }
        return sources;
    }

    // Returns a map of targets that the source vertex points to
    @Override
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> targets = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(source)) {
                targets.put(edge.getTarget(), edge.getWeight());
            }
        }
        return targets;
    }

    // Checks representation invariants
    private void checkRep() {
        assert vertices != null : "Vertices set must not be null";
        assert edges != null : "Edges list must not be null";
        for (Edge edge : edges) {
            assert vertices.contains(edge.getSource()) : "Edge source must be in vertices";
            assert vertices.contains(edge.getTarget()) : "Edge target must be in vertices";
            assert edge.getWeight() > 0 : "Edge weight must be positive";
        }
    }

    // String representation of the graph
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Vertices: " + vertices + "\nEdges:\n");
        for (Edge edge : edges) {
            sb.append(edge.toString()).append("\n");
        }
        return sb.toString();
    }
}

public class Edge {
    private final String source;
    private final String target;
    private final int weight;

    public Edge(String source, String target, int weight) {
        if (source == null || target == null || weight < 0)
            throw new IllegalArgumentException("Invalid edge parameters");

        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    // Getters
    public String getSource() { return source; }
    public String getTarget() { return target; }
    public int getWeight() { return weight; }

    // Checks representation invariants
    private void checkRep() {
        assert source != null : "Source vertex must not be null";
        assert target != null : "Target vertex must not be null";
        assert weight >= 0 : "Weight must be non-negative";
    }

    // String representation of the edge
    @Override
    public String toString() {
        return source + " -> " + target + " (weight " + weight + ")";
    }
}

