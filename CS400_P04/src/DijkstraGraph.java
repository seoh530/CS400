// --== CS400 File Header Information ==--
// Name: Seyeong Oh
// Email: soh87@wisc.edu
// Group and Team: None
// Group TA: None
// Lecturer: Peyman Morteza

import java.util.PriorityQueue;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class extends the BaseGraph data structure with additional methods for computing the total
 * cost and list of node data along the shortest path connecting a provided starting to ending
 * nodes. This class makes use of Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number> extends BaseGraph<NodeType, EdgeType>
    implements GraphADT<NodeType, EdgeType> {

  /**
   * While searching for the shortest path between two nodes, a SearchNode contains data about one
   * specific path between the start node and another node in the graph. The final node in this path
   * is stored in it's node field. The total cost of this path is stored in its cost field. And the
   * predecessor SearchNode within this path is referened by the predecessor field (this field is
   * null within the SearchNode containing the starting node in it's node field).
   *
   * SearchNodes are Comparable and are sorted by cost so that the lowest cost SearchNode has the
   * highest priority within a java.util.PriorityQueue.
   */
  protected class SearchNode implements Comparable<SearchNode> {
    public Node node;
    public double cost;
    public SearchNode predecessor;

    public SearchNode(Node node, double cost, SearchNode predecessor) {
      this.node = node;
      this.cost = cost;
      this.predecessor = predecessor;
    }

    public int compareTo(SearchNode other) {
      if (cost > other.cost)
        return +1;
      if (cost < other.cost)
        return -1;
      return 0;
    }
  }

  /**
   * This helper method creates a network of SearchNodes while computing the shortest path between
   * the provided start and end locations. The SearchNode that is returned by this method is
   * represents the end of the shortest path that is found: it's cost is the cost of that shortest
   * path, and the nodes linked together through predecessor references represent all of the nodes
   * along that shortest path (ordered from end to start).
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return SearchNode for the final end node within the shortest path
   * @throws NoSuchElementException when no path from start to end is found or when either start or
   *                                end data do not correspond to a graph node
   */
  protected SearchNode computeShortestPath(NodeType start, NodeType end) {
    if (!containsNode(start) || !containsNode(end)) {
      throw new NoSuchElementException("Start or end node not found in the graph.");
    }

    Hashtable<NodeType, Double> costs = new Hashtable<>();
    Hashtable<NodeType, SearchNode> searchNodes = new Hashtable<>();
    PriorityQueue<SearchNode> priorityQueue = new PriorityQueue<>();

    // Initialize costs to infinity for all nodes except the start node
    for (NodeType node : nodes.keySet()) {
      if (node.equals(start)) {
        costs.put(node, 0.0);
      } else {
        costs.put(node, Double.POSITIVE_INFINITY);
      }
    }

    // Create search nodes and add the start node to the priority queue
    for (NodeType node : nodes.keySet()) {
      SearchNode searchNode = new SearchNode(nodes.get(node), costs.get(node), null);
      searchNodes.put(node, searchNode);

      if (node.equals(start)) {
        priorityQueue.add(searchNode);
      }
    }

    while (!priorityQueue.isEmpty()) {
      SearchNode current = priorityQueue.remove();

      if (current.node.data.equals(end)) {
        // Found the end node, return the search node
        return current;
      }

      // Explore the outgoing edges from the current node
      for (Edge outgoingEdge : current.node.edgesLeaving) {
        Node successorNode = outgoingEdge.successor;

        double newCost = current.cost + outgoingEdge.data.doubleValue();

        if (newCost < costs.get(successorNode.data)) {
          // Found a shorter path to the successor node
          costs.put(successorNode.data, newCost);
          SearchNode successorSearchNode = searchNodes.get(successorNode.data);
          successorSearchNode.cost = newCost;
          successorSearchNode.predecessor = current;
          priorityQueue.add(successorSearchNode);
        }
      }
    }

    throw new NoSuchElementException("No path from start to end found.");
  }

  /**
   * Returns the list of data values from nodes along the shortest path from the node with the
   * provided start value through the node with the provided end value. This list of data values
   * starts with the start value, ends with the end value, and contains intermediary values in the
   * order they are encountered while traversing this shorteset path. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return list of data item from node along this shortest path
   */
  public List<NodeType> shortestPathData(NodeType start, NodeType end) {
    SearchNode searchNode = computeShortestPath(start, end);
    List<NodeType> path = new LinkedList<>();

    while (searchNode != null) {
      path.add(0, searchNode.node.data);
      searchNode = searchNode.predecessor;
    }

    return path;
  }

  /**
   * Returns the cost of the path (sum over edge weights) of the shortest path freom the node
   * containing the start data to the node containing the end data. This method uses Dijkstra's
   * shortest path algorithm to find this solution.
   *
   * @param start the data item in the starting node for the path
   * @param end   the data item in the destination node for the path
   * @return the cost of the shortest path between these nodes
   */
  public double shortestPathCost(NodeType start, NodeType end) {
    SearchNode searchNode = computeShortestPath(start, end);
    return searchNode.cost;
  }


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
