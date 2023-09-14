import org.junit.Test;
import org.junit.Assert;
import java.util.NoSuchElementException;

public class HashtableMapTests {

  @Test
  /**
   * Test case for putting and getting a key-value pair. It verifies that the put method correctly
   * adds the pair to the hashtable, and the get method retrieves the value associated with the key.
   */
  public void testPutAndGet() {
    
    HashtableMap<String, Integer> hashtable = new HashtableMap<>();

    hashtable.put("A", 1);
    hashtable.put("B", 2);
    hashtable.put("C", 3);

    Assert.assertEquals(1, hashtable.get("A").intValue());
    Assert.assertEquals(2, hashtable.get("B").intValue());
    Assert.assertEquals(3, hashtable.get("C").intValue());

    //Test NoSuchElementException
    try {
      hashtable.get("D");
      hashtable.get(null);
      Assert.fail("Expected NoSuchElementException to be thrown");
    } catch (NoSuchElementException e) {
      // Exception was thrown as expected
    }

  }

  @Test
  /**
   * Test case for removing a key-value pair. It verifies that the remove method removes the pair
   * from the hashtable and returns the correct value associated with the key.
   */
  public void testRemove() {
    
    HashtableMap<String, Integer> hashtable = new HashtableMap<>();

    hashtable.put("A", 1);
    hashtable.put("B", 2);
    hashtable.put("C", 3);

    int removedValue = hashtable.remove("B");

    Assert.assertEquals(2, removedValue);
    Assert.assertFalse(hashtable.containsKey("B"));
    Assert.assertEquals(2, hashtable.getSize());
  }

  @Test
  /**
   * Tests the resizeTable method.
   * It verifies that the table capacity is correctly doubled after resizing.
   */
  public void testResizeTable() {
    
      HashtableMap<String, Integer> hashtable = new HashtableMap<>(5);

      hashtable.put("A", 1);
      hashtable.put("B", 2);
      hashtable.put("C", 3);
      
      //lower than 0.7, no change
      Assert.assertEquals(3, hashtable.getSize());
      Assert.assertEquals(5, hashtable.getCapacity());
      
      hashtable.put("D", 4);
      
     //reach 0.7. the capacity should be doubled(10)
      hashtable.put("E", 5);
      Assert.assertEquals(5, hashtable.getSize());
      Assert.assertEquals(10, hashtable.getCapacity());
     
      hashtable.put("F", 6);
      
      //reach 0.7 again. the capcaity should be doubled again (20)
      hashtable.put("G", 7);
      Assert.assertEquals(7, hashtable.getSize());
      Assert.assertEquals(20, hashtable.getCapacity());
     
      
      hashtable.put("H", 8);
      
      Assert.assertEquals(8, hashtable.getSize());
      Assert.assertEquals(20, hashtable.getCapacity());

  }
  


}
