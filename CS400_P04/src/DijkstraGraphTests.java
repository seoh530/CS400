import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class DijkstraGraphTests {

  /**
   * JUnit test to compare the computed shortest path with the expected path for a known example
   * graph.
   */
  @Test
  public void testShortestPathDataExampleGraph() {

    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertEdge("A", "B", 6.0);
    graph.insertEdge("A", "D", 1.0);
    graph.insertEdge("D", "E", 1.0);
    graph.insertEdge("D", "B", 2.0);
    graph.insertEdge("B", "E", 2.0);
    graph.insertEdge("B", "C", 5.0);
    graph.insertEdge("E", "C", 5.0);

    // Compute the shortest path from A to E
    List<String> shortestPath = graph.shortestPathData("A", "C");
    
    // Define the expected path
    List<String> expectedPath = List.of("A", "D", "E", "C");

    // Compare the computed path with the expected path
    assertEquals(expectedPath, shortestPath);
    
    // Compute the cost of the shortest path from A to E
    double shortestPathCost = graph.shortestPathCost("A", "C");

    // Define the expected cost
    double expectedCost = 7.0;

    // Compare the computed cost with the expected cost
    assertEquals(expectedCost, shortestPathCost);
  }

  /**
   * JUnit test to check the cost and sequence of data along the shortest path between different
   * start and end nodes in the example graph.
   */
  @Test
  public void testShortestPathDataDifferentStartEnd() {
    
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertNode("E");
    graph.insertEdge("A", "B", 6.0);
    graph.insertEdge("A", "D", 1.0);
    graph.insertEdge("D", "E", 1.0);
    graph.insertEdge("D", "B", 2.0);
    graph.insertEdge("B", "E", 2.0);
    graph.insertEdge("B", "C", 5.0);
    graph.insertEdge("E", "C", 5.0);

    // Compute the shortest path from B to D
    List<String> shortestPath = graph.shortestPathData("A", "E");

    // Define the expected path
    List<String> expectedPath = List.of("A", "D", "E");

    // Compare the computed path with the expected path
    assertEquals(expectedPath, shortestPath);

    // Compute the cost of the shortest path from A to E
    double shortestPathCost = graph.shortestPathCost("A", "E");

    // Define the expected cost
    double expectedCost = 2.0;

    // Compare the computed cost with the expected cost
    assertEquals(expectedCost, shortestPathCost);
  }

  /**
   * JUnit test to check the behavior when no path exists between two nodes in the graph.
   */
  @Test
  public void testShortestPathDataNoPath() {
 
    DijkstraGraph<String, Double> graph = new DijkstraGraph<>();
    graph.insertNode("A");
    graph.insertNode("B");
    graph.insertNode("C");
    graph.insertNode("D");
    graph.insertEdge("A", "B", 1.0);
    graph.insertEdge("C", "D", 2.0);


    // Try to compute the shortest path from A to C
    assertThrows(NoSuchElementException.class, () -> graph.shortestPathData("A", "C"));
  }

}
