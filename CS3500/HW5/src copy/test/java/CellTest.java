import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import model.Card;
import model.Cell;
import model.Player;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Cell.
 */
public class CellTest {

  private Cell cell;
  private Player redPlayer;
  private Player bluePlayer;

  /**
   * Set up test fixtures.
   */
  @Before
  public void setUp() {
    cell = new Cell();
    redPlayer = new Player(Color.RED);
    bluePlayer = new Player(Color.BLUE);
  }

  @Test
  public void testConstructor() {
    assertTrue(cell.isEmpty());
    assertEquals(0, cell.getNumPawns());
    assertNull(cell.getCard());
    assertNull(cell.getOwner());
  }

  @Test
  public void testUpdatePawns() {
    cell.updatePawns(2);
    assertEquals(2, cell.getNumPawns());
    assertFalse(cell.isEmpty());
  }

  @Test
  public void testUpdatePawnsToZero() {
    cell.updatePawns(2);
    cell.updatePawns(0);
    assertEquals(0, cell.getNumPawns());
    assertTrue(cell.isEmpty());
    assertNull(cell.getOwner());
  }

  @Test
  public void testSetCard() {
    boolean[][] influence = new boolean[5][5];
    Card card = new Card("Test", 1, 1, influence);
    
    cell.setCard(card);
    assertNotNull(cell.getCard());
    assertEquals("Test", cell.getCard().getName());
    assertFalse(cell.isEmpty());
  }

  @Test
  public void testSetOwner() {
    cell.setOwner(redPlayer);
    assertSame(redPlayer, cell.getOwner());
  }

  @Test
  public void testChangeOwner() {
    cell.updatePawns(2);
    cell.setOwner(redPlayer);
    
    cell.setOwner(bluePlayer);
    assertSame(bluePlayer, cell.getOwner());
    assertEquals(2, cell.getNumPawns());
  }

  @Test
  public void testCellWithPawnsAndOwner() {
    cell.updatePawns(3);
    cell.setOwner(redPlayer);
    
    assertEquals(3, cell.getNumPawns());
    assertSame(redPlayer, cell.getOwner());
    assertFalse(cell.isEmpty());
  }



  @Test
  public void testEmptyAfterClearingPawns() {
    cell.updatePawns(1);
    cell.setOwner(redPlayer);
    assertFalse(cell.isEmpty());
    
    cell.updatePawns(0);
    assertTrue(cell.isEmpty());
  }

  // Invalid Cases

  @Test
  public void testNegativePawns() {
    // Model doesn't validate negative pawns, but tests behavior
    cell.updatePawns(-1);
    assertEquals(-1, cell.getNumPawns());
  }


  @Test
  public void testNullCard() {
    cell.setCard(null);
    assertNull(cell.getCard());
  }

  @Test
  public void testNullOwner() {
    cell.setOwner(null);
    assertNull(cell.getOwner());
  }

  @Test
  public void testOwnerWithoutPawns() {
    // Can set owner without pawns
    cell.setOwner(redPlayer);
    assertEquals(0, cell.getNumPawns());
    assertSame(redPlayer, cell.getOwner());
  }

  @Test
  public void testCardWithoutOwner() {
    boolean[][] influence = new boolean[5][5];
    Card card = new Card("Test", 1, 1, influence);
    
    cell.setCard(card);
    assertNotNull(cell.getCard());
    assertNull(cell.getOwner());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testMultipleOwnerChanges() {
    cell.updatePawns(2);
    cell.setOwner(redPlayer);
    cell.setOwner(bluePlayer);
    cell.setOwner(redPlayer);
    
    assertSame(redPlayer, cell.getOwner());
    assertEquals(2, cell.getNumPawns());
  }
}
