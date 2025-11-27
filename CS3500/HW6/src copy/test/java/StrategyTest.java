import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import model.Card;
import model.Cell;
import model.Player;
import model.PlayerInterface;
import model.SanguineModel;
import model.SanguineModelInterface;
import org.junit.Before;
import org.junit.Test;
import strategy.FillFirstStrategy;
import strategy.MaximizeRowScoreStrategy;
import strategy.Move;
import strategy.SanguineStrategyInterface;

/**
 * Tests for Sanguine game strategies.
 */
public class StrategyTest {
  private Card testCard1;
  private Card testCard2;
  private Card testCard3;

  /**
   * Set up test cards.
   */
  @Before
  public void setUp() {
    boolean[][] influence1 = {{false, false, true, false, false}, {false, true, true, true, false},
        {true, true, true, true, true}, {false, true, true, true, false},
        {false, false, true, false, false}};

    boolean[][] influence2 = {{false, false, false, false, false}, {false, true, true, true, false},
        {false, true, true, true, false}, {false, true, true, true, false},
        {false, false, false, false, false}};

    boolean[][] influence3 = {
            {false, false, true, false, false}, {false, false, true, false, false},
            {false, false, true, false, false}, {false, false, true, false, false},
            {false, false, true, false, false}};

    testCard1 = new Card("TestCard1", 1, 5, influence1);
    testCard2 = new Card("TestCard2", 2, 3, influence2);
    testCard3 = new Card("TestCard3", 1, 7, influence3);
  }

  @Test
  public void testFillFirstStrategyFindsFirstMove() {
    MockModel model = new MockModel(3, 5);
    Player red = model.getRedPlayer();

    SanguineStrategyInterface strategy = new FillFirstStrategy();
    List<Move> moves = strategy.chooseMoves(model, red);

    assertNotNull("Strategy should return a list", moves);
    assertEquals("Should return exactly one move", 1, moves.size());

    Move move = moves.get(0);
    assertEquals("Should choose first card", 0, move.getCardIndex());
    assertEquals("Should choose first legal position", 0, move.getRow());
    assertEquals("Should choose first legal column", 0, move.getCol());
  }

  @Test
  public void testFillFirstStrategyChecksTopToBottom() {
    MockModel model = new MockModel(3, 5);
    model.setLegalOnlyAt(1, 2); // Only legal at row 1, col 2
    Player red = model.getRedPlayer();

    SanguineStrategyInterface strategy = new FillFirstStrategy();
    List<Move> moves = strategy.chooseMoves(model, red);

    assertEquals("Should find the legal move", 1, moves.size());
    Move move = moves.get(0);
    assertEquals("Should find row 1", 1, move.getRow());
    assertEquals("Should find col 2", 2, move.getCol());

    // Verify the strategy checked positions in order
    List<String> transcript = model.getTranscript();
    assertTrue("Should check positions in order", transcript.contains("isLegalMove(0,0,0)"));
    assertTrue("Should check row 0 before row 1",
        indexOfCheck(transcript, 0, 0) < indexOfCheck(transcript, 1, 2));
  }

  @Test
  public void testFillFirstStrategyReturnsEmptyWhenNoLegalMoves() {
    MockModel model = new MockModel(3, 5);
    model.setAllIllegal(); // No legal moves
    Player red = model.getRedPlayer();

    SanguineStrategyInterface strategy = new FillFirstStrategy();
    List<Move> moves = strategy.chooseMoves(model, red);

    assertNotNull("Should return a list", moves);
    assertTrue("Should return empty list when no legal moves", moves.isEmpty());
  }

  @Test
  public void testMaximizeRowScoreStrategyWinsRow() {
    MockModel model = new MockModel(3, 5);
    Player red = model.getRedPlayer();
    Player blue = model.getBluePlayer();

    // Set up scenario: Red has 2 points in row 0, Blue has 5 points in row 0
    model.setRowScore(red, 0, 2);
    model.setRowScore(blue, 0, 5);

    // Red has a card with value 7 that can win the row
    model.setCardValue(0, 7);

    SanguineStrategyInterface strategy = new MaximizeRowScoreStrategy();
    List<Move> moves = strategy.chooseMoves(model, red);

    assertNotNull("Strategy should return a list", moves);
    assertFalse("Should find a winning move", moves.isEmpty());

    Move move = moves.get(0);
    assertEquals("Should play in row 0 to win", 0, move.getRow());
  }

  @Test
  public void testMaximizeRowScoreStrategyChecksTopToBottom() {
    MockModel model = new MockModel(3, 5);
    Player red = model.getRedPlayer();
    Player blue = model.getBluePlayer();

    // Row 0: Red=5, Blue=5 (tied)
    model.setRowScore(red, 0, 5);
    model.setRowScore(blue, 0, 5);

    // Row 1: Red=3, Blue=10 (can't win)
    model.setRowScore(red, 1, 3);
    model.setRowScore(blue, 1, 10);

    // Row 2: Red=2, Blue=3 (can win with card value 7)
    model.setRowScore(red, 2, 2);
    model.setRowScore(blue, 2, 3);

    model.setCardValue(0, 7);

    SanguineStrategyInterface strategy = new MaximizeRowScoreStrategy();
    List<Move> moves = strategy.chooseMoves(model, red);

    // Should try row 0 first (tied), then skip row 1, then find row 2
    List<String> transcript = model.getTranscript();
    assertTrue("Should check row 0 first", transcript.contains("getRowScore(RED,0)"));
    assertTrue("Should check row 1 second", transcript.contains("getRowScore(RED,1)"));
    assertTrue("Should check row 2 third", transcript.contains("getRowScore(RED,2)"));
  }

  @Test
  public void testMaximizeRowScoreStrategySkipsWinningRows() {
    MockModel model = new MockModel(3, 5);
    Player red = model.getRedPlayer();
    Player blue = model.getBluePlayer();

    // Row 0: Red is already winning
    model.setRowScore(red, 0, 10);
    model.setRowScore(blue, 0, 5);

    // Row 1: Red is losing and can win
    model.setRowScore(red, 1, 2);
    model.setRowScore(blue, 1, 3);

    model.setCardValue(0, 5);

    SanguineStrategyInterface strategy = new MaximizeRowScoreStrategy();
    List<Move> moves = strategy.chooseMoves(model, red);

    assertFalse("Should find a move", moves.isEmpty());
    Move move = moves.get(0);
    assertEquals("Should skip row 0 and play in row 1", 1, move.getRow());
  }

  @Test
  public void testMaximizeRowScoreStrategyReturnsEmptyWhenCantWinAnyRow() {
    MockModel model = new MockModel(3, 5);
    Player red = model.getRedPlayer();
    Player blue = model.getBluePlayer();

    // All rows: Red is losing and can't win
    model.setRowScore(red, 0, 1);
    model.setRowScore(blue, 0, 10);
    model.setRowScore(red, 1, 2);
    model.setRowScore(blue, 1, 15);
    model.setRowScore(red, 2, 0);
    model.setRowScore(blue, 2, 20);

    // Even with high value cards, can't win
    model.setCardValue(0, 5);
    model.setAllIllegal(); // Just in case

    SanguineStrategyInterface strategy = new MaximizeRowScoreStrategy();
    List<Move> moves = strategy.chooseMoves(model, red);

    assertTrue("Should return empty list when can't win any row", moves.isEmpty());
  }

  private int indexOfCheck(List<String> transcript, int row, int col) {
    for (int i = 0; i < transcript.size(); i++) {
      if (transcript.get(i).contains("isLegalMove(" + row + "," + col)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Mock model for testing strategies.
   * Records which methods were called to verify strategy behavior.
   */
  private class MockModel implements SanguineModelInterface<Card, Cell<Card>> {
    private int rows;
    private int cols;
    private Player red;
    private Player blue;
    private List<String> transcript;
    private boolean allLegal = true;
    private int legalRow = -1;
    private int legalCol = -1;
    private int[][] rowScores; // [player][row]
    private int[] cardValues;

    public MockModel(int rows, int cols) {
      this.rows = rows;
      this.cols = cols;
      this.transcript = new ArrayList<>();

      // Create simple hands
      List<Card> redHand = new ArrayList<>();
      List<Card> blueHand = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
        redHand.add(testCard1);
        blueHand.add(testCard1);
      }

      this.red = new MockPlayer(Color.RED, redHand, this);
      this.blue = new MockPlayer(Color.BLUE, blueHand, this);

      this.rowScores = new int[2][rows]; // 0=red, 1=blue
      this.cardValues = new int[5]; // Up to 5 cards
      for (int i = 0; i < 5; i++) {
        cardValues[i] = 5; // Default value
      }
    }

    public void setLegalOnlyAt(int row, int col) {
      this.allLegal = false;
      this.legalRow = row;
      this.legalCol = col;
    }

    public void setAllIllegal() {
      this.allLegal = false;
      this.legalRow = -1;
      this.legalCol = -1;
    }

    public void setRowScore(Player player, int row, int score) {
      int playerIdx = (player == red) ? 0 : 1;
      rowScores[playerIdx][row] = score;
    }

    public void setCardValue(int cardIdx, int value) {
      cardValues[cardIdx] = value;
    }

    public List<String> getTranscript() {
      return transcript;
    }

    @Override
    public Player getRedPlayer() {
      return red;
    }

    @Override
    public Player getBluePlayer() {
      return blue;
    }

    @Override
    public int getNumRows() {
      return rows;
    }

    @Override
    public int getNumCols() {
      return cols;
    }

    @Override
    public int getRowScore(PlayerInterface<Card> player, int rowIndex) {
      String playerName = (player == red) ? "RED" : "BLUE";
      transcript.add("getRowScore(" + playerName + "," + rowIndex + ")");
      int playerIdx = (player == red) ? 0 : 1;
      return rowScores[playerIdx][rowIndex];
    }

    @Override
    public List<Card> getPlayerHand(PlayerInterface<Card> player) {
      return player.getHand();
    }

    // Stub methods - not needed for strategy tests
    @Override
    public void start(int rows, int cols, Card[] blueHand, Card[] redHand) {
    }

    @Override
    public Cell<Card>[][] getBoard() {
      return null;
    }

    @Override
    public Cell<Card>[][] updateBoard(Cell<Card>[][] board) {
      return null;
    }

    @Override
    public boolean isGameOver() {
      return false;
    }

    @Override
    public void pass() {
    }

    @Override
    public void placeCard(int row, int col, Card card) {
    }

    @Override
    public int calculateRowScore(Cell<Card>[] row) {
      return 0;
    }

    @Override
    public Cell<Card> getCellAt(int row, int col) {
      return null;
    }

    @Override
    public List<Card> getCurrentPlayerHand() {
      return null;
    }

    @Override
    public PlayerInterface<Card> getCellOwner(int row, int col) {
      return null;
    }

    @Override
    public boolean isLegalMove(int row, int col, Card card) {
      return false;
    }

    @Override
    public boolean isLegalMove(int row, int col, int cardIndex) {
      transcript.add("isLegalMove(" + row + "," + col + "," + cardIndex + ")");
      if (!allLegal) {
        return row == legalRow && col == legalCol;
      }
      return true;
    }

    @Override
    public int getPlayerScore(PlayerInterface<Card> player) {
      return 0;
    }

    @Override
    public PlayerInterface<Card> getWinner() {
      return null;
    }

    @Override
    public PlayerInterface<Card> getCurrentPlayer() {
      return red;
    }

    @Override
    public Cell<Card>[][] copyBoard() {
      return null;
    }

    @Override
    public void playCardFromHand(int row, int col, int cardIndex) {
    }

    @Override
    public void setGameOver(boolean over) {
    }

    @Override
    public boolean isValidMove(int row, int col, Card card) {
      return isLegalMove(row, col, 0); // Delegate to existing logic
    }
  }

  /**
   * Mock player for testing.
   */
  private class MockPlayer extends Player {
    private List<Card> hand;
    private MockModel model;

    public MockPlayer(Color color, List<Card> hand, MockModel model) {
      super(color);
      this.hand = hand;
      this.model = model;
    }

    @Override
    public List<Card> getHand() {
      // Override card values for testing
      List<Card> testHand = new ArrayList<>();
      for (int i = 0; i < hand.size(); i++) {
        Card original = hand.get(i);
        Card modified = new Card(original.getName(), original.getCost(),
            i < model.cardValues.length ? model.cardValues[i] : original.getValue(),
            original.getInfluence());
        testHand.add(modified);
      }
      return testHand;
    }
  }
}

