/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;
import org.junit.Test;

public abstract class GraphInstanceTest {

    // This method will be implemented in subclasses
    protected abstract Graph<String> emptyInstance();

    // Test: new graph should have no vertices
    @Test
    public void testInitialVerticesEmpty() {
        Graph<String> graph = emptyInstance();
        assertTrue("Expected no vertices in a new graph", graph.vertices().isEmpty());
    }

    // Test: adding vertices
    @Test
    public void testAddVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("Expected vertex addition to return true", graph.add("A"));
        assertTrue("Graph should contain added vertex 'A'", graph.vertices().contains("A"));
    }

    // Test: adding duplicate vertices
    @Test
    public void testAddDuplicateVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertFalse("Adding duplicate vertex should return false", graph.add("A"));
        assertEquals("Graph should contain only one 'A' vertex", 1, graph.vertices().size());
    }

    // Test: adding edges and verifying weights
    @Test
    public void testSetEdge() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        assertEquals("Expected initial weight of 0 when adding a new edge", 0, graph.set("A", "B", 5));
        assertEquals("Expected weight of 5 after setting edge", 5, graph.set("A", "B", 5));
    }

    // Test: removing vertices
    @Test
    public void testRemoveVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        assertTrue("Expected vertex removal to return true", graph.remove("A"));
        assertFalse("Removed vertex 'A' should no longer be in graph", graph.vertices().contains("A"));
        assertEquals("Removing 'A' should also remove related edges", 0, graph.sources("B").size());
    }

    // Test: checking sources and targets of directed edges
    @Test
    public void testSourcesAndTargets() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.add("C");
        graph.set("A", "B", 5);
        graph.set("C", "B", 3);
        
        assertEquals("Source 'A' should have edge weight of 5 to 'B'", 5, graph.sources("B").get("A").intValue());
        assertEquals("Source 'C' should have edge weight of 3 to 'B'", 3, graph.sources("B").get("C").intValue());
        
        assertEquals("Target 'B' from 'A' should have edge weight of 5", 5, graph.targets("A").get("B").intValue());
    }
}

