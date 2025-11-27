import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import model.Card;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Card.
 */
public class CardTest {

  private boolean[][] testInfluence;
  private Card testCard;

  /**
   * Set up test fixtures.
   */
  @Before
  public void setUp() {
    testInfluence = new boolean[5][5];
    testInfluence[1][2] = true;  // North
    testInfluence[3][2] = true;  // South
    testInfluence[2][1] = true;  // West
    testInfluence[2][3] = true;  // East
    
    testCard = new Card("TestCard", 2, 3, testInfluence);
  }

  @Test
  public void testConstructor() {
    assertNotNull(testCard);
    assertEquals("TestCard", testCard.getName());
    assertEquals(2, testCard.getCost());
    assertEquals(3, testCard.getValue());
  }

  @Test
  public void testGetName() {
    assertEquals("TestCard", testCard.getName());
  }

  @Test
  public void testGetCost() {
    assertEquals(2, testCard.getCost());
  }

  @Test
  public void testGetValue() {
    assertEquals(3, testCard.getValue());
  }

  @Test
  public void testGetInfluence() {
    boolean[][] influence = testCard.getInfluence();
    assertNotNull(influence);
    assertEquals(5, influence.length);
    assertEquals(5, influence[0].length);
    
    // Check pattern is preserved
    assertTrue(influence[1][2]);
    assertTrue(influence[3][2]);
    assertTrue(influence[2][1]);
    assertTrue(influence[2][3]);
  }

  @Test
  public void testIsValidCorrectParameters() {
    assertTrue(testCard.isValid(1, 1, testInfluence));
    assertTrue(testCard.isValid(2, 5, testInfluence));
    assertTrue(testCard.isValid(3, 10, testInfluence));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsValidInvalidCost() {
    testCard.isValid(0, 2, testInfluence);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsValidNegativeCost() {
    testCard.isValid(-1, 2, testInfluence);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testIsValidZeroValue() {
    testCard.isValid(2, 0, testInfluence);
  }

  
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCostTooLarge() {
    testCard.isValid(10, 5, testInfluence);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValueNegative() {
    testCard.isValid(2, -5, testInfluence);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInfluenceTooSmall() {
    boolean[][] badInfluence = new boolean[3][3];
    testCard.isValid(2, 3, badInfluence);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInfluenceTooLarge() {
    boolean[][] badInfluence = new boolean[6][6];
    testCard.isValid(2, 3, badInfluence);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInfluenceWrongColumns() {
    boolean[][] badInfluence = new boolean[5][3];
    testCard.isValid(2, 3, badInfluence);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInfluenceNull() {
    boolean[][] badInfluence = new boolean[5][];
    badInfluence[0] = new boolean[5];
    badInfluence[1] = null;
    badInfluence[2] = new boolean[5];
    badInfluence[3] = new boolean[5];
    badInfluence[4] = new boolean[5];
    testCard.isValid(2, 3, badInfluence);
  }
}
