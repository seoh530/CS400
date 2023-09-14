import java.util.LinkedList;
import java.util.Stack;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Red-Black Tree implementation with a Node inner class for representing the nodes of the tree.
 * Currently, this implements a Binary Search Tree that we will turn into a red black tree by
 * modifying the insert functionality. In this activity, we will start with implementing rotations
 * for the binary search tree insert algorithm.
 */
public class RedBlackTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {
  
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


  /**
   * This class represents a node holding a single value within a binary tree.
   */
  protected static class Node<T> {
    public T data;
    public int blackHeight;
    // The context array stores the context of the node in the tree:
    // - context[0] is the parent reference of the node,
    // - context[1] is the left child reference of the node,
    // - context[2] is the right child reference of the node.
    // The @SupressWarning("unchecked") annotation is used to supress an unchecked
    // cast warning. Java only allows us to instantiate arrays without generic
    // type parameters, so we use this cast here to avoid future casts of the
    // node type's data field.
    @SuppressWarnings("unchecked")
    public Node<T>[] context = (Node<T>[]) new Node[3];


    // black height only for
    // the current node:0=red,1=black,and 2=double-black.
    public Node(T data) {
      this.data = data;
      this.blackHeight = 0;
    }

    /**
     * @return true when this node has a parent and is the right child of that parent, otherwise
     *         return false
     */
    public boolean isRightChild() {
      return context[0] != null && context[0].context[2] == this;
    }


  }

  protected Node<T> root; // reference to root node of tree, null when empty
  protected int size = 0; // the number of values in the tree


  /**
   * Performs a naive insertion into a binary search tree: adding the input data value to a new node
   * in a leaf position within the tree. After this insertion, no attempt is made to restructure or
   * balance the tree. This tree will not hold null references, nor duplicate data values.
   * 
   * @param data to be added into this binary search tree
   * @return true if the value was inserted, false if not
   * @throws NullPointerException     when the provided data argument is null
   * @throws IllegalArgumentException when data is already contained in the tree
   */
  public boolean insert(T data) throws NullPointerException, IllegalArgumentException {
    // null references cannot be stored within this tree
    if (data == null)
      throw new NullPointerException("This RedBlackTree cannot store null references.");

    Node<T> newNode = new Node<>(data);
    if (this.root == null) {
      // add first node to an empty tree
      root = newNode;
      size++;
      enforceRBTreePropertiesAfterInsert(newNode);
      return true;
    } else {
      // insert into subtree
      Node<T> current = this.root;
      while (true) {
        int compare = newNode.data.compareTo(current.data);
        if (compare == 0) {
          throw new IllegalArgumentException(
              "This RedBlackTree already contains value " + data.toString());
        } else if (compare < 0) {
          // insert in left subtree
          if (current.context[1] == null) {
            // empty space to insert into
            current.context[1] = newNode;
            newNode.context[0] = current;
            this.size++;
            enforceRBTreePropertiesAfterInsert(newNode);
            return true;
          } else {
            // no empty space, keep moving down the tree
            current = current.context[1];
          }
        } else {
          // insert in right subtree
          if (current.context[2] == null) {
            // empty space to insert into
            current.context[2] = newNode;
            newNode.context[0] = current;
            this.size++;
            enforceRBTreePropertiesAfterInsert(newNode);
            return true;
          } else {
            // no empty space, keep moving down the tree
            current = current.context[2];
          }
        }
      }
    }
  }

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a left child of the provided parent, this method will perform a right rotation. When the
   * provided child is a right child of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will throw
   * an IllegalArgumentException.
   * 
   * @param child  is the node being rotated from child to parent position (between these two node
   *               arguments)
   * @param parent is the node being rotated from parent to child position (between these two node
   *               arguments)
   * @throws IllegalArgumentException when the provided child and parent node references are not
   *                                  initially (pre-rotation) related that way
   */
  private void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {

    // 1) Check if child and parent nodes are related in a valid way
    if (parent.context[1] != child && parent.context[2] != child) {
      throw new IllegalArgumentException(
          "The provided child and parent nodes are not related for rotation.");
    }

    // 2) Determine the rotation type
    if (parent.context[2] == child) {
      // right child of the parent --> Left rotation
      parent.context[2] = child.context[1];
      if (child.context[1] != null) {
        child.context[1].context[0] = parent;
      }
      child.context[1] = parent;
    } else if (child == parent.context[1]) {
      // left child of the parent --> Right rotation
      parent.context[1] = child.context[2];
      if (child.context[2] != null) {
        child.context[2].context[0] = parent;
      }
      child.context[2] = parent;
    } else {
      throw new IllegalArgumentException("Child and parent nodes are not related for rotation.");
    }

    // Update the grandparent reference
    Node<T> grandparent = parent.context[0];
    if (grandparent != null) {
      if (grandparent.context[1] == parent) {
        grandparent.context[1] = child;
      } else {
        grandparent.context[2] = child;
      }
    }

    // Update the child's parent reference
    child.context[0] = grandparent;

    // Update the root reference if necessary
    if (parent == root) {
      root = child;
    }
  }

  /**
   * Get the size of the tree (its number of nodes).
   * 
   * @return the number of nodes in the tree
   */
  public int size() {
    return size;
  }

  /**
   * Method to check if the tree is empty (does not contain any node).
   * 
   * @return true of this.size() return 0, false if this.size() > 0
   */
  public boolean isEmpty() {
    return this.size() == 0;
  }

  /**
   * Removes the value data from the tree if the tree contains the value. This method will not
   * attempt to rebalance the tree after the removal and should be updated once the tree uses
   * Red-Black Tree insertion.
   * 
   * @return true if the value was remove, false if it didn't exist
   * @throws NullPointerException     when the provided data argument is null
   * @throws IllegalArgumentException when data is not stored in the tree
   */
  public boolean remove(T data) throws NullPointerException, IllegalArgumentException {
    // null references will not be stored within this tree
    if (data == null) {
      throw new NullPointerException("This RedBlackTree cannot store null references.");
    } else {
      Node<T> nodeWithData = this.findNodeWithData(data);
      // throw exception if node with data does not exist
      if (nodeWithData == null) {
        throw new IllegalArgumentException(
            "The following value is not in the tree and cannot be deleted: " + data.toString());
      }
      boolean hasRightChild = (nodeWithData.context[2] != null);
      boolean hasLeftChild = (nodeWithData.context[1] != null);
      if (hasRightChild && hasLeftChild) {
        // has 2 children
        Node<T> successorNode = this.findMinOfRightSubtree(nodeWithData);
        // replace value of node with value of successor node
        nodeWithData.data = successorNode.data;
        // remove successor node
        if (successorNode.context[2] == null) {
          // successor has no children, replace with null
          this.replaceNode(successorNode, null);
        } else {
          // successor has a right child, replace successor with its child
          this.replaceNode(successorNode, successorNode.context[2]);
        }
      } else if (hasRightChild) {
        // only right child, replace with right child
        this.replaceNode(nodeWithData, nodeWithData.context[2]);
      } else if (hasLeftChild) {
        // only left child, replace with left child
        this.replaceNode(nodeWithData, nodeWithData.context[1]);
      } else {
        // no children, replace node with a null node
        this.replaceNode(nodeWithData, null);
      }
      this.size--;
      return true;
    }
  }

  /**
   * Checks whether the tree contains the value *data*.
   * 
   * @param data the data value to test for
   * @return true if *data* is in the tree, false if it is not in the tree
   */
  public boolean contains(T data) {
    // null references will not be stored within this tree
    if (data == null) {
      throw new NullPointerException("This RedBlackTree cannot store null references.");
    } else {
      Node<T> nodeWithData = this.findNodeWithData(data);
      // return false if the node is null, true otherwise
      return (nodeWithData != null);
    }
  }

  /**
   * Helper method that will replace a node with a replacement node. The replacement node may be
   * null to remove the node from the tree.
   * 
   * @param nodeToReplace   the node to replace
   * @param replacementNode the replacement for the node (may be null)
   */
  protected void replaceNode(Node<T> nodeToReplace, Node<T> replacementNode) {
    if (nodeToReplace == null) {
      throw new NullPointerException("Cannot replace null node.");
    }
    if (nodeToReplace.context[0] == null) {
      // we are replacing the root
      if (replacementNode != null)
        replacementNode.context[0] = null;
      this.root = replacementNode;
    } else {
      // set the parent of the replacement node
      if (replacementNode != null)
        replacementNode.context[0] = nodeToReplace.context[0];
      // do we have to attach a new left or right child to our parent?
      if (nodeToReplace.isRightChild()) {
        nodeToReplace.context[0].context[2] = replacementNode;
      } else {
        nodeToReplace.context[0].context[1] = replacementNode;
      }
    }
  }

  /**
   * Helper method that will return the inorder successor of a node with two children.
   * 
   * @param node the node to find the successor for
   * @return the node that is the inorder successor of node
   */
  protected Node<T> findMinOfRightSubtree(Node<T> node) {
    if (node.context[1] == null && node.context[2] == null) {
      throw new IllegalArgumentException("Node must have two children");
    }
    // take a steop to the right
    Node<T> current = node.context[2];
    while (true) {
      // then go left as often as possible to find the successor
      if (current.context[1] == null) {
        // we found the successor
        return current;
      } else {
        current = current.context[1];
      }
    }
  }

  /**
   * Helper method that will return the node in the tree that contains a specific value. Returns
   * null if there is no node that contains the value.
   * 
   * @return the node that contains the data, or null of no such node exists
   */
  protected Node<T> findNodeWithData(T data) {
    Node<T> current = this.root;
    while (current != null) {
      int compare = data.compareTo(current.data);
      if (compare == 0) {
        // we found our value
        return current;
      } else if (compare < 0) {
        // keep looking in the left subtree
        current = current.context[1];
      } else {
        // keep looking in the right subtree
        current = current.context[2];
      }
    }
    // we're at a null node and did not find data, so it's not in the tree
    return null;
  }

  /**
   * This method performs an inorder traversal of the tree. The string representations of each data
   * value within this tree are assembled into a comma separated string within brackets (similar to
   * many implementations of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
   * 
   * @return string containing the ordered values of this tree (in-order traversal)
   */
  public String toInOrderString() {
    // generate a string of all values of the tree in (ordered) in-order
    // traversal sequence
    StringBuffer sb = new StringBuffer();
    sb.append("[ ");
    if (this.root != null) {
      Stack<Node<T>> nodeStack = new Stack<>();
      Node<T> current = this.root;
      while (!nodeStack.isEmpty() || current != null) {
        if (current == null) {
          Node<T> popped = nodeStack.pop();
          sb.append(popped.data.toString());
          if (!nodeStack.isEmpty() || popped.context[2] != null)
            sb.append(", ");
          current = popped.context[2];
        } else {
          nodeStack.add(current);
          current = current.context[1];
        }
      }
    }
    sb.append(" ]");
    return sb.toString();
  }

  /**
   * This method performs a level order traversal of the tree. The string representations of each
   * data value within this tree are assembled into a comma separated string within brackets
   * (similar to many implementations of java.util.Collection). This method will be helpful as a
   * helper for the debugging and testing of your rotation implementation.
   * 
   * @return string containing the values of this tree in level order
   */
  public String toLevelOrderString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[ ");
    if (this.root != null) {
      LinkedList<Node<T>> q = new LinkedList<>();
      q.add(this.root);
      while (!q.isEmpty()) {
        Node<T> next = q.removeFirst();
        if (next.context[1] != null)
          q.add(next.context[1]);
        if (next.context[2] != null)
          q.add(next.context[2]);
        sb.append(next.data.toString());
        if (!q.isEmpty())
          sb.append(", ");
      }
    }
    sb.append(" ]");
    return sb.toString();
  }

  public String toString() {
    return "level order: " + this.toLevelOrderString() + "\nin order: " + this.toInOrderString();
  }


  protected void enforceRBTreePropertiesAfterInsert(Node<T> newNode) {

    Node<T> parent = newNode.context[0];

    // Case 1: The newly inserted node is the root
    if (parent == null) {
      newNode.blackHeight = 1; // Set the black height to 1 for the root
      return;
    }

    // Case 2: The parent is black (NO property violation)
    if (parent.blackHeight == 1) {
      return;
    }

    // Case 3: The parent is red, and the uncle is red (recoloring)
    Node<T> grandparent = parent.context[0];
    Node<T> uncle = (parent.isRightChild()) ? grandparent.context[1] : grandparent.context[2];

    // uncle is red node
    if (uncle != null && uncle.blackHeight == 0) {
      parent.blackHeight = 1;
      uncle.blackHeight = 1;
      grandparent.blackHeight = 0;
      enforceRBTreePropertiesAfterInsert(grandparent); // Recursively check for violations up the
                                                       // tree
      return;
    }

    // Case 4: The parent is red, the uncle is black or null (rotation)

    // Case 4-1: when the parent and newnode are in the same direction
    if ((newNode.isRightChild() && parent.isRightChild())
        || (!newNode.isRightChild() && !parent.isRightChild())) {
      rotate(parent, grandparent);
      parent.blackHeight = 1;
      grandparent.blackHeight = 0;
    }


    // Case 4-1: when the parent and newnode are in the opposite direction
    else {
      rotate(newNode, parent);
      rotate(newNode, grandparent);
      newNode.blackHeight = 1;
      grandparent.blackHeight = 0;

    }

    root.blackHeight = 1;
  }
}


