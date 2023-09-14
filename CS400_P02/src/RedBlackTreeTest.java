import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


  /**
   * This is a JUnit test class for the RedBlackTree class. In these three test methods, the main
   * focus is on testing the Insert method. In other words, the goal is to verify that the insertion
   * is functioning correctly without violating the red-black tree properties.
   * 
   * @author seyeongoh
   *
   */
  @Nested
  public class RedBlackTreeTest {

    @Test
    /**
     * This is a tester to test case 3: When the uncle node is red and parent node is red. In this
     * case, there is no contradiction, just recoloring occurs
     */
    public void RedUncleTest() {

      // I skipped the case 1 and 2 because both cases don't have violation
      // case 3: when the parent and uncle are both red.
      RedBlackTree<Integer> tree = new RedBlackTree<>();

      tree.insert(23);
      tree.insert(7);
      tree.insert(41);
      tree.insert(37);

      // test1: check the level order. In this case, the order is not changed.
      assertEquals(tree.toLevelOrderString(), "[ 23, 7, 41, 37 ]");
      
      // test2: check the black height of each node
      assertEquals(getBlackHeight(tree.root), 1); // root always black.
      assertEquals(getBlackHeight(tree.findNodeWithData(7)), 1);
      assertEquals(getBlackHeight(tree.findNodeWithData(41)), 1);
      assertEquals(getBlackHeight(tree.findNodeWithData(23)), 1);
      assertEquals(getBlackHeight(tree.findNodeWithData(37)), 0);
   
    }

    @Test
    /**
     * This is a tester to test case 4: When the uncle node is Black and parent node is red,
     * especially case 4 which parent and new node are on the same direction.
     */
    public void BlackUncleInSameDirectionTest() {

      RedBlackTree<Integer> tree = new RedBlackTree<>();

      // case 4-1: parent and new node are both right children
      tree.insert(3);
      tree.insert(4); // parent
      tree.insert(1); // uncle
      setUncleBlack(tree.findNodeWithData(1));
      tree.insert(5); // new node

      // test1: check the level order.
      assertEquals(tree.toLevelOrderString(), "[ 4, 3, 5, 1 ]");
      
      // test2: check the black height of each node
      assertEquals(getBlackHeight(tree.findNodeWithData(3)), 0); 
      assertEquals(getBlackHeight(tree.findNodeWithData(4)), 1); 
      assertEquals(getBlackHeight(tree.findNodeWithData(5)), 0);
      assertEquals(getBlackHeight(tree.findNodeWithData(1)), 1);


      RedBlackTree<Integer> tree2 = new RedBlackTree<>();

      // case 4-2: parent and new node are both left children
      tree2.insert(74);
      tree2.insert(26);
      tree2.insert(100); 
      setUncleBlack(tree2.findNodeWithData(100));
      tree2.insert(13);

      // test1: check the level order. Uncle is null in this case
      assertEquals(tree2.toLevelOrderString(), "[ 26, 13, 74, 100 ]");
      

      // test2: check the black height of each node
      assertEquals(getBlackHeight(tree2.root), 1); // root always black.
      assertEquals(getBlackHeight(tree2.findNodeWithData(13)), 0);
      assertEquals(getBlackHeight(tree2.findNodeWithData(26)), 1);
      assertEquals(getBlackHeight(tree2.findNodeWithData(74)), 0);
      assertEquals(getBlackHeight(tree2.findNodeWithData(100)), 1); //uncle is leaf node
    }

    @Test
    /**
     * This is a tester to test case 4: When the uncle node is Black and parent node is red,
     * especially 4-3 and 4-4 cases which parent and new node are on the opposite direction.
     */
    public void BlackUncleInOppositeDirectionTest() {

      RedBlackTree<Integer> tree = new RedBlackTree<>();

      // case 4-3: parent is left child and new node is right child
      tree.insert(45);
      tree.insert(23); // parent
      tree.insert(50); // uncle
      setUncleBlack(tree.findNodeWithData(50));
      tree.insert(30); // new node (right child)

      // test1: check the level order.
      assertEquals(tree.toLevelOrderString(), "[ 30, 23, 45, 50 ]");

      // test2: check the black height of each node
      assertEquals(getBlackHeight(tree.root), 1); // root always black.
      assertEquals(getBlackHeight(tree.findNodeWithData(23)), 0);
      assertEquals(getBlackHeight(tree.findNodeWithData(30)), 1);
      assertEquals(getBlackHeight(tree.findNodeWithData(45)), 0);
      assertEquals(getBlackHeight(tree.findNodeWithData(50)), 1);

      RedBlackTree<Integer> tree2 = new RedBlackTree<>();

      // case 4-4: parent is right child and new node is left child
      tree2.insert(20);
      tree2.insert(40);// parent
      tree2.insert(15); // uncle
      setUncleBlack(tree2.findNodeWithData(15));
      tree2.insert(30);

      // test1: check the level order. Uncle is null in this case
      assertEquals(tree2.toLevelOrderString(), "[ 30, 20, 40, 15 ]");

      // test2: check the black height of each node
      assertEquals(getBlackHeight(tree2.root), 1); // root always black.
      assertEquals(getBlackHeight(tree2.findNodeWithData(20)), 0);
      assertEquals(getBlackHeight(tree2.findNodeWithData(30)), 1);
      assertEquals(getBlackHeight(tree2.findNodeWithData(40)), 0);
      assertEquals(getBlackHeight(tree2.findNodeWithData(15)), 1); // uncle is leaf node
      
    }

    // Helper method to check the color of node
    private int getBlackHeight(RedBlackTree.Node<Integer> node) {
      return node.blackHeight;
    }

    // Helper method to set the color to black
    private void setUncleBlack(RedBlackTree.Node<Integer> node) {
      node.blackHeight = 1;
    }

  }
