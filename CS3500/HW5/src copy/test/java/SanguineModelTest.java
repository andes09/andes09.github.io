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
import model.SanguineModel;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SanguineModel.
 */
public class SanguineModelTest {

  private Player redPlayer;
  private Player bluePlayer;
  private SanguineModel model;
  private boolean[][] emptyInfluence;

  /**
   * Set up test fixtures.
   */
  @Before
  public void setUp() {
    redPlayer = new Player(Color.RED);
    bluePlayer = new Player(Color.BLUE);
    model = new SanguineModel(redPlayer, bluePlayer, 3, 5);
    emptyInfluence = new boolean[5][5];
  }

  @Test
  public void testConstructor() {
    assertNotNull(model);
    assertNotNull(model.getBoard());
    assertEquals(3, model.getBoard().length);
    assertEquals(5, model.getBoard()[0].length);
  }

  @Test
  public void testInitialBoardSetup() {
    Cell[][] board = model.getBoard();
    
    // First column should have red pawns
    for (int i = 0; i < 3; i++) {
      assertEquals(1, board[i][0].getNumPawns());
      assertSame(redPlayer, board[i][0].getOwner());
    }
    
    // Last column should have blue pawns
    for (int i = 0; i < 3; i++) {
      assertEquals(1, board[i][4].getNumPawns());
      assertSame(bluePlayer, board[i][4].getOwner());
    }
    
    // Middle cells should be empty
    for (int i = 0; i < 3; i++) {
      for (int j = 1; j < 4; j++) {
        assertTrue(board[i][j].isEmpty());
      }
    }
  }

  @Test
  public void testGetBoard() {
    Cell[][] board = model.getBoard();
    assertNotNull(board);
    assertEquals(3, board.length);
  }

  @Test
  public void testUpdateBoard() {
    Cell[][] newBoard = new Cell[3][5];
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 5; j++) {
        newBoard[i][j] = new Cell();
      }
    }
    
    model.updateBoard(newBoard);
    assertSame(newBoard, model.getBoard());
  }

  @Test
  public void testIsGameOver() {
    assertFalse(model.isGameOver());
  }

  @Test
  public void testPass() {
    model.pass();
    assertFalse(model.isGameOver());
  }

  @Test
  public void testPlaceCard() {
    Card card = new Card("Test", 1, 2, emptyInfluence);
    model.placeCard(0, 0, card);
    
    Cell[][] board = model.getBoard();
    assertNotNull(board[0][0].getCard());
    assertEquals("Test", board[0][0].getCard().getName());
  }

  @Test
  public void testPlaceCardRemovesPawns() {
    Card card = new Card("Test", 1, 1, emptyInfluence);
    Cell[][] board = model.getBoard();
    
    assertEquals(1, board[0][0].getNumPawns());
    model.placeCard(0, 0, card);
    assertEquals(0, board[0][0].getNumPawns());
  }

  @Test
  public void testInfluenceCreatesNewPawn() {
    boolean[][] influence = new boolean[5][5];
    influence[1][2] = true;  // North
    
    Cell[][] board = model.getBoard();
    board[1][1].updatePawns(1);
    board[1][1].setOwner(redPlayer);
    
    final Card card = new Card("Test", 1, 1, influence);
    model.placeCard(1, 1, card);
    
    // Cell to the north should have a pawn
    assertEquals(1, board[0][1].getNumPawns());
  }

  @Test
  public void testInfluenceIncrementsPawn() {
    boolean[][] influence = new boolean[5][5];
    influence[2][1] = true;  // West
    
    Cell[][] board = model.getBoard();
    board[1][0].updatePawns(1);
    board[1][0].setOwner(redPlayer);
    board[1][1].updatePawns(1);
    board[1][1].setOwner(redPlayer);
    
    final Card card = new Card("Test", 1, 1, influence);
    model.placeCard(1, 1, card);
    
    // West cell should increment
    assertEquals(2, board[1][0].getNumPawns());
  }

  @Test
  public void testInfluenceCapturesPawn() {
    boolean[][] influence = new boolean[5][5];
    influence[2][1] = true;
    
    Cell[][] board = model.getBoard();
    board[1][0].updatePawns(2);
    board[1][0].setOwner(bluePlayer);
    board[1][1].updatePawns(1);
    board[1][1].setOwner(redPlayer);
    
    final Card card = new Card("Test", 1, 1, influence);
    model.placeCard(1, 1, card);
    
    // Should capture blue's pawns
    assertSame(redPlayer, board[1][0].getOwner());
  }

  @Test
  public void testInfluenceMaxPawns() {
    boolean[][] influence = new boolean[5][5];
    influence[2][1] = true;
    
    Cell[][] board = model.getBoard();
    board[1][0].updatePawns(3);
    board[1][0].setOwner(redPlayer);
    board[1][1].updatePawns(1);
    board[1][1].setOwner(redPlayer);
    
    final Card card = new Card("Test", 1, 1, influence);
    model.placeCard(1, 1, card);
    
    // Should not exceed 3
    assertEquals(3, board[1][0].getNumPawns());
  }

  @Test
  public void testCalculateRowScore() {
    Cell[] row = new Cell[5];
    for (int i = 0; i < 5; i++) {
      row[i] = new Cell();
    }
    
    // Add cards with values for the current player (red)
    Card card1 = new Card("Card1", 1, 3, emptyInfluence);
    row[0].setCard(card1);
    row[0].setOwner(redPlayer);
    
    Card card2 = new Card("Card2", 1, 5, emptyInfluence);
    row[2].setCard(card2);
    row[2].setOwner(redPlayer);
    
    // Add a card for blue player (should not be counted for red)
    Card card3 = new Card("Card3", 1, 10, emptyInfluence);
    row[4].setCard(card3);
    row[4].setOwner(bluePlayer);
    
    int score = model.calculateRowScore(row);
    // Current player is red, should count cards 1 and 2: 3 + 5 = 8
    assertEquals(8, score);
  }

  


  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardOnEmptyCell() {
    Card card = new Card("Test", 1, 1, emptyInfluence);
    model.placeCard(1, 1, card);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardOnOpponentCell() {
    Card card = new Card("Test", 1, 1, emptyInfluence);
    model.placeCard(0, 4, card);
    
    Cell[][] board = model.getBoard();

  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceCardInsufficientPawns() {
    Card expensiveCard = new Card("Expensive", 3, 5, emptyInfluence);
    model.placeCard(0, 0, expensiveCard);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testPlaceCardOutOfBounds() {
    Card card = new Card("Test", 1, 1, emptyInfluence);
    model.placeCard(10, 10, card);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCard() {
    model.placeCard(0, 0, null);
  }


  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testNegativeRowIndex() {
    Card card = new Card("Test", 1, 1, emptyInfluence);
    model.placeCard(-1, 0, card);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testNegativeColumnIndex() {
    Card card = new Card("Test", 1, 1, emptyInfluence);
    model.placeCard(0, -1, card);
  }

  @Test
  public void testCalculateRowScoreEmptyRow() {
    Cell[] emptyRow = new Cell[5];
    for (int i = 0; i < 5; i++) {
      emptyRow[i] = new Cell();
    }
    assertEquals(0, model.calculateRowScore(emptyRow));
  }

}
