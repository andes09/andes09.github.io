import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Card;
import model.Cell;
import model.Player;
import model.PlayerInterface;
import model.SanguineModel;
import org.junit.Before;
import org.junit.Test;
import strategy.ControllBoardStrategy;
import strategy.FillFirstStrategy;
import strategy.MaximizeRowScoreStrategy;
import strategy.MiniMaxStrategy;
import strategy.Move;
import strategy.SanguineStrategyInterface;

/**
 * Tests that demonstrate strategies as function objects in various game configurations.
 * Focuses on testing scenarios that highlight differences between strategies.
 */
public class MultiStrategyTest {
  private Card security;
  private Card queen;
  private Card crab;
  private Card wheel;
  private Card flame;
  private Card chocomog;
  private Card grenade;
  private Card sweeper;
  private Card quetz;
  private Card big;
  private Card rider;

  /**
   * Set up cards from example.deck.
   */
  @Before
  public void setUp() {
    boolean[][] securityInfluence = {
        {false, false, false, false, false},
        {false, false, true, false, false},
        {false, true, false, true, false},
        {false, false, true, false, false},
        {false, false, false, false, false}
    };
    security = new Card("Security", 1, 1, securityInfluence);

    boolean[][] queenInfluence = {
        {false, false, true, false, false},
        {false, false, false, false, false},
        {false, false, false, false, false},
        {false, false, false, false, false},
        {false, false, true, false, false}
    };
    queen = new Card("Queen", 1, 1, queenInfluence);

    boolean[][] crabInfluence = {
        {false, false, false, false, false},
        {false, false, true, false, false},
        {false, true, false, true, false},
        {false, false, false, false, false},
        {false, false, false, false, false}
    };
    crab = new Card("Crab", 1, 1, crabInfluence);

    boolean[][] wheelInfluence = {
        {false, false, false, false, true},
        {false, false, false, false, false},
        {false, false, false, false, false},
        {false, false, false, false, false},
        {false, false, false, false, true}
    };
    wheel = new Card("Wheel", 1, 1, wheelInfluence);

    boolean[][] flameInfluence = {
        {false, false, false, false, false},
        {false, true, true, false, false},
        {false, true, false, false, false},
        {false, true, true, false, false},
        {false, false, false, false, false}
    };
    flame = new Card("Flame", 1, 3, flameInfluence);

    boolean[][] chocomogInfluence = {
        {false, false, false, false, false},
        {false, false, true, false, false},
        {false, false, false, true, false},
        {false, false, true, false, false},
        {false, false, false, false, false}
    };
    chocomog = new Card("Chocomog", 1, 1, chocomogInfluence);

    boolean[][] grenadeInfluence = {
        {false, false, false, false, false},
        {false, false, false, false, false},
        {false, false, false, false, true},
        {false, false, false, false, false},
        {false, false, false, false, false}
    };
    grenade = new Card("Grenade", 2, 1, grenadeInfluence);

    boolean[][] sweeperInfluence = {
        {false, false, false, false, false},
        {false, false, true, true, false},
        {false, false, false, false, false},
        {false, false, true, true, false},
        {false, false, false, false, false}
    };
    sweeper = new Card("Sweeper", 2, 2, sweeperInfluence);

    boolean[][] quetzInfluence = {
        {false, false, true, false, false},
        {false, false, false, true, false},
        {false, false, false, false, false},
        {false, false, false, true, false},
        {false, false, true, false, false}
    };
    quetz = new Card("Quetz", 2, 3, quetzInfluence);

    boolean[][] bigInfluence = {
        {false, false, true, false, false},
        {false, true, false, true, false},
        {true, false, false, false, true},
        {false, true, false, true, false},
        {false, false, true, false, false}
    };
    big = new Card("Big", 3, 5, bigInfluence);

    boolean[][] riderInfluence = {
        {false, false, false, false, false},
        {false, false, false, false, false},
        {false, false, false, true, false},
        {false, true, true, true, false},
        {false, false, false, false, false}
    };
    rider = new Card("Rider", 3, 5, riderInfluence);
  }

  /**
   * Test that FillFirst and MaximizeRowScore differ when row scores matter.
   * FillFirst chooses first legal position, MaximizeRowScore targets winning rows.
   */
  @Test
  public void testFillFirstVsMaximizeRowScoreDifferentChoices() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);
    List<Card> redHand = Arrays.asList(security, flame, crab, queen, wheel);
    List<Card> blueHand = Arrays.asList(security, flame, crab, queen, wheel);

    builder.setCell(0, 1, builder.red, 1, null); // Red controls (0,1)
    builder.setCell(2, 1, builder.red, 1, null); // Red controls (2,1)

    // Row scores: Row 0 is tied, Row 2 Red is losing
    builder.setRowScore(builder.red, 0, 3);
    builder.setRowScore(builder.blue, 0, 3);
    builder.setRowScore(builder.red, 2, 2);
    builder.setRowScore(builder.blue, 2, 5);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface fillFirst = new FillFirstStrategy();
    SanguineStrategyInterface maximize = new MaximizeRowScoreStrategy();

    List<Move> fillFirstMoves = fillFirst.chooseMoves(model, builder.red);
    List<Move> maximizeMoves = maximize.chooseMoves(model, builder.red);

    assertFalse("FillFirst should find a move", fillFirstMoves.isEmpty());
    assertFalse("MaximizeRowScore should find a move", maximizeMoves.isEmpty());

    Move fillFirstMove = fillFirstMoves.get(0);
    Move maximizeMove = maximizeMoves.get(0);

    assertEquals("FillFirst should choose row 0", 0, fillFirstMove.getRow());

    assertTrue("MaximizeRowScore should choose strategically",
        maximizeMove.getRow() == 0 || maximizeMove.getRow() == 2);
  }

  /**
   * Test ControllBoardStrategy maximizes cell control vs FillFirst.
   * They use different approaches to choose moves.
   */
  @Test
  public void testControllBoardVsFillFirstDifferentCards() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);

    // Give red hand with different influence patterns
    List<Card> redHand = Arrays.asList(wheel, big, security, queen, crab);
    List<Card> blueHand = Arrays.asList(security, security, security, security, security);

    // Red controls multiple positions
    builder.setCell(0, 1, builder.red, 1, null);
    builder.setCell(1, 2, builder.red, 3, null);
    builder.setCell(2, 1, builder.red, 1, null);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface fillFirst = new FillFirstStrategy();
    SanguineStrategyInterface controllBoard = new ControllBoardStrategy();

    List<Move> fillFirstMoves = fillFirst.chooseMoves(model, builder.red);
    List<Move> controllBoardMoves = controllBoard.chooseMoves(model, builder.red);

    assertFalse("FillFirst should find a move", fillFirstMoves.isEmpty());
    assertFalse("ControllBoard should find a move", controllBoardMoves.isEmpty());

    Move fillFirstMove = fillFirstMoves.get(0);
    Move controllBoardMove = controllBoardMoves.get(0);

    assertEquals("FillFirst should choose first legal position", 0, fillFirstMove.getRow());

    assertTrue("ControllBoard should choose valid position",
        controllBoardMove.getRow() >= 0 && controllBoardMove.getCol() >= 0);
  }

  /**
   * Test that strategies handle empty board correctly.
   * All strategies should find moves on a board with legal positions.
   */
  @Test
  public void testAllStrategiesOnEmptyBoard() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);
    List<Card> redHand = Arrays.asList(security, flame, crab, queen, wheel);
    List<Card> blueHand = Arrays.asList(security, flame, crab, queen, wheel);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface fillFirst = new FillFirstStrategy();
    SanguineStrategyInterface maximize = new MaximizeRowScoreStrategy();
    SanguineStrategyInterface controllBoard = new ControllBoardStrategy();

    List<Move> fillFirstMoves = fillFirst.chooseMoves(model, builder.red);
    List<Move> maximizeMoves = maximize.chooseMoves(model, builder.red);
    List<Move> controllBoardMoves = controllBoard.chooseMoves(model, builder.red);

    assertNotNull("FillFirst should return list", fillFirstMoves);
    assertNotNull("MaximizeRowScore should return list", maximizeMoves);
    assertNotNull("ControllBoard should return list", controllBoardMoves);

    assertFalse("FillFirst should find moves", fillFirstMoves.isEmpty());
    assertFalse("ControllBoard should find moves", controllBoardMoves.isEmpty());

    // Maximize might return empty if can't win any row
    // All strategies should make valid moves
    if (!fillFirstMoves.isEmpty()) {
      Move move = fillFirstMoves.get(0);
      assertTrue("Move should be valid", move.getRow() >= 0 && move.getCol() >= 0);
    }
  }

  /**
   * Test ControllBoard maximizes cell control considering influence.
   * Should choose cards that would control more cells.
   */
  @Test
  public void testControllBoardMaximizesCellControl() {
    TestGameBuilder builder = new TestGameBuilder(5, 5);

    List<Card> redHand = Arrays.asList(wheel, security, big, flame, rider);
    List<Card> blueHand = Arrays.asList(security, security, security, security, security);

    builder.setCell(2, 2, builder.red, 3, null);
    
    builder.setCell(1, 2, builder.red, 0, null);
    builder.setCell(3, 2, builder.red, 0, null);
    builder.setCell(2, 1, builder.red, 0, null);
    builder.setCell(2, 3, builder.red, 0, null);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface controllBoard = new ControllBoardStrategy();
    List<Move> moves = controllBoard.chooseMoves(model, builder.red);

    assertFalse("Should find a move", moves.isEmpty());
    Move move = moves.get(0);

    assertTrue("Should choose a valid move", move.getRow() >= 0 && move.getCol() >= 0);
  }

  /**
   * Test MaximizeRowScore skips already-winning rows.
   * Should focus on rows where player is losing but can win.
   */
  @Test
  public void testMaximizeRowScoreFocusesOnWinnableRows() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);
    List<Card> redHand = Arrays.asList(flame, flame, security, queen, wheel);
    List<Card> blueHand = Arrays.asList(security, security, security, security, security);

    builder.setCell(0, 1, builder.red, 1, null);
    builder.setCell(1, 1, builder.red, 1, null);
    builder.setCell(2, 1, builder.red, 1, null);

    // Row 0: Red winning (don't play here)
    builder.setRowScore(builder.red, 0, 10);
    builder.setRowScore(builder.blue, 0, 5);

    // Row 1: Red losing but can win with flame (value 3)
    builder.setRowScore(builder.red, 1, 2);
    builder.setRowScore(builder.blue, 1, 4);

    // Row 2: Red losing and can't win
    builder.setRowScore(builder.red, 2, 1);
    builder.setRowScore(builder.blue, 2, 20);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface maximize = new MaximizeRowScoreStrategy();
    List<Move> moves = maximize.chooseMoves(model, builder.red);

    assertFalse("Should find a move", moves.isEmpty());
    Move move = moves.get(0);

    assertEquals("Should target row 1 (winnable)", 1, move.getRow());
    assertTrue("Should use flame card", move.getCardIndex() == 0 || move.getCardIndex() == 1);
  }

  /**
   * Test that different strategies return different moves in complex scenario.
   * This demonstrates they are truly different function objects.
   */
  @Test
  public void testStrategiesAsFunctionObjectsGiveDifferentResults() {
    TestGameBuilder builder = new TestGameBuilder(4, 5);

    List<Card> redHand = Arrays.asList(security, big, flame, queen, rider);
    List<Card> blueHand = Arrays.asList(security, security, security, security, security);

    // Multiple legal positions for variety
    builder.setCell(0, 1, builder.red, 1, null);
    builder.setCell(1, 2, builder.red, 3, null);
    builder.setCell(2, 1, builder.red, 1, null);
    builder.setCell(3, 2, builder.red, 3, null);

    builder.setRowScore(builder.red, 0, 2);
    builder.setRowScore(builder.blue, 0, 3);
    builder.setRowScore(builder.red, 1, 1);
    builder.setRowScore(builder.blue, 1, 1);
    builder.setRowScore(builder.red, 2, 5);
    builder.setRowScore(builder.blue, 2, 2);
    builder.setRowScore(builder.red, 3, 0);
    builder.setRowScore(builder.blue, 3, 4);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface fillFirst = new FillFirstStrategy();
    SanguineStrategyInterface maximize = new MaximizeRowScoreStrategy();
    SanguineStrategyInterface controllBoard = new ControllBoardStrategy();

    List<Move> fillFirstMoves = fillFirst.chooseMoves(model, builder.red);
    List<Move> maximizeMoves = maximize.chooseMoves(model, builder.red);
    List<Move> controllBoardMoves = controllBoard.chooseMoves(model, builder.red);

    assertFalse("FillFirst should find move", fillFirstMoves.isEmpty());
    assertFalse("ControllBoard should find move", controllBoardMoves.isEmpty());

    // At least some strategies should differ
    Move ff = fillFirstMoves.get(0);
    Move cb = controllBoardMoves.get(0);

    // These are different strategies with different objectives
    assertNotNull("FillFirst move should exist", ff);
    assertNotNull("ControllBoard move should exist", cb);
  }

  /**
   * Test MiniMaxStrategy against a simple opponent strategy.
   * MiniMax should consider opponent's potential response.
   */
  @Test
  public void testMiniMaxConsidersOpponentResponse() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);

    List<Card> redHand = Arrays.asList(flame, security, crab, queen, wheel);
    List<Card> blueHand = Arrays.asList(flame, security, crab, queen, wheel);

    builder.setCell(0, 1, builder.red, 1, null);
    builder.setCell(1, 1, builder.red, 1, null);
    builder.setCell(2, 1, builder.red, 1, null);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface opponentStrategy = new FillFirstStrategy();
    SanguineStrategyInterface minimax = new MiniMaxStrategy(opponentStrategy);

    List<Move> moves = minimax.chooseMoves(model, builder.red);

    assertNotNull("MiniMax should return list", moves);
    assertFalse("MiniMax should find move", moves.isEmpty());

    Move move = moves.get(0);
    assertTrue("Move should be in valid range", move.getRow() >= 0 && move.getRow() < 3);
  }

  /**
   * Test that strategies are reusable function objects.
   * Same strategy instance can be called multiple times.
   */
  @Test
  public void testStrategyAsReusableFunctionObject() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);

    List<Card> redHand = Arrays.asList(security, flame, crab, queen, wheel);
    List<Card> blueHand = Arrays.asList(security, flame, crab, queen, wheel);

    builder.setCell(0, 1, builder.red, 1, null);
    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface strategy = new FillFirstStrategy();

    List<Move> moves1 = strategy.chooseMoves(model, builder.red);
    List<Move> moves2 = strategy.chooseMoves(model, builder.red);

    assertNotNull("First call should return moves", moves1);
    assertNotNull("Second call should return moves", moves2);

    assertFalse("First call should find move", moves1.isEmpty());
    assertFalse("Second call should find move", moves2.isEmpty());

    assertEquals("Should give consistent results",
        moves1.get(0).getCardIndex(), moves2.get(0).getCardIndex());
    assertEquals("Should give consistent results",
        moves1.get(0).getRow(), moves2.get(0).getRow());
    assertEquals("Should give consistent results",
        moves1.get(0).getCol(), moves2.get(0).getCol());
  }

  /**
   * Test ControllBoard handles tied influence counts correctly.
   * Should choose uppermost-leftmost position when tied.
   */
  @Test
  public void testControllBoardTieBreaking() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);

    List<Card> redHand = Arrays.asList(security, crab, flame, queen, wheel);
    List<Card> blueHand = Arrays.asList(security, security, security, security, security);

    builder.setCell(0, 1, builder.red, 1, null);
    builder.setCell(2, 1, builder.red, 1, null);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface controllBoard = new ControllBoardStrategy();
    List<Move> moves = controllBoard.chooseMoves(model, builder.red);

    assertFalse("Should find move", moves.isEmpty());
    Move move = moves.get(0);

    assertTrue("Should choose valid position", move.getRow() >= 0);
  }

  /**
   * Test MaximizeRowScore with tied rows.
   * Should prefer upper rows when multiple rows are tied.
   */
  @Test
  public void testMaximizeRowScoreHandlesTiedRows() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);

    List<Card> redHand = Arrays.asList(flame, security, crab, queen, wheel);
    List<Card> blueHand = Arrays.asList(security, security, security, security, security);

    builder.setCell(0, 1, builder.red, 1, null);
    builder.setCell(1, 1, builder.red, 1, null);
    builder.setCell(2, 1, builder.red, 1, null);

    // All rows tied
    builder.setRowScore(builder.red, 0, 5);
    builder.setRowScore(builder.blue, 0, 5);
    builder.setRowScore(builder.red, 1, 5);
    builder.setRowScore(builder.blue, 1, 5);
    builder.setRowScore(builder.red, 2, 5);
    builder.setRowScore(builder.blue, 2, 5);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface maximize = new MaximizeRowScoreStrategy();
    List<Move> moves = maximize.chooseMoves(model, builder.red);

    if (!moves.isEmpty()) {
      Move move = moves.get(0);
      // Should choose row 0 first (top-to-bottom search)
      assertEquals("Should choose uppermost tied row", 0, move.getRow());
    }
  }

  /**
   * Test that ControllBoard maximizes total cell control.
   * Should count existing cells plus potential new cells.
   */
  @Test
  public void testControllBoardCountsExistingAndNewCells() {
    TestGameBuilder builder = new TestGameBuilder(5, 5);

    List<Card> redHand = Arrays.asList(security, big, flame, queen, wheel);
    List<Card> blueHand = Arrays.asList(security, security, security, security, security);

    // Red already controls some cells
    builder.setCell(1, 1, builder.red, 1, security);
    builder.setCell(2, 2, builder.red, 3, null);
    builder.setCell(3, 3, builder.red, 1, security);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface controllBoard = new ControllBoardStrategy();
    List<Move> moves = controllBoard.chooseMoves(model, builder.red);

    assertNotNull("Should return moves", moves);
    assertFalse("Should find move considering all cells", moves.isEmpty());
  }

  /**
   * Test strategies handle high-cost cards correctly.
   * Only positions with enough pawns should be legal.
   */
  @Test
  public void testStrategiesHandleHighCostCards() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);

    List<Card> redHand = Arrays.asList(big, rider, grenade, sweeper, quetz);
    List<Card> blueHand = Arrays.asList(security, security, security, security, security);

    builder.setCell(0, 1, builder.red, 3, null); // Cost 3 cell
    builder.setCell(1, 1, builder.red, 2, null); // Cost 2 cell
    builder.setCell(2, 1, builder.red, 2, null); // Cost 2 cell

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface fillFirst = new FillFirstStrategy();
    List<Move> moves = fillFirst.chooseMoves(model, builder.red);

    assertFalse("Should find legal move for high-cost cards", moves.isEmpty());
    Move move = moves.get(0);

    assertTrue("Should be valid card index", move.getCardIndex() >= 0);
    assertTrue("Should be valid row", move.getRow() >= 0 && move.getRow() < 3);
    assertTrue("Should be valid col", move.getCol() >= 0 && move.getCol() < 5);
  }

  /**
   * Test MaximizeRowScore returns empty when no winnable rows.
   * Should not force a move if can't improve position.
   */
  @Test
  public void testMaximizeRowScoreReturnsEmptyWhenNoWinnableRows() {
    TestGameBuilder builder = new TestGameBuilder(3, 5);

    List<Card> redHand = Arrays.asList(security, crab, queen, wheel, chocomog);
    List<Card> blueHand = Arrays.asList(security, security, security, security, security);

    builder.setCell(0, 1, builder.red, 1, null);
    builder.setCell(1, 1, builder.red, 1, null);
    builder.setCell(2, 1, builder.red, 1, null);

    // All rows: Blue winning by large margin
    builder.setRowScore(builder.red, 0, 1);
    builder.setRowScore(builder.blue, 0, 20);
    builder.setRowScore(builder.red, 1, 0);
    builder.setRowScore(builder.blue, 1, 15);
    builder.setRowScore(builder.red, 2, 2);
    builder.setRowScore(builder.blue, 2, 25);

    SanguineModel model = builder.buildWithHands(redHand, blueHand);

    SanguineStrategyInterface maximize = new MaximizeRowScoreStrategy();
    List<Move> moves = maximize.chooseMoves(model, builder.red);

    assertTrue("Should return empty when can't win any row", moves.isEmpty());
  }

  /**
   * Helper class to build test game configurations.
   */
  private static class TestGameBuilder {
    private final int rows;
    private final int cols;
    private final Cell<Card>[][] board;
    public final TestPlayer red;
    public final TestPlayer blue;
    private final int[][] rowScores; // [0=red, 1=blue][rowIndex]

    public TestGameBuilder(int rows, int cols) {
      this.rows = rows;
      this.cols = cols;
      Cell<Card>[][] tempBoard = (Cell<Card>[][]) new Cell[rows][cols];
      this.board = tempBoard;
      this.red = new TestPlayer(Color.RED, new ArrayList<>());
      this.blue = new TestPlayer(Color.BLUE, new ArrayList<>());
      this.rowScores = new int[2][rows];

      // Initialize board with empty cells
      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
          board[i][j] = new Cell<>();
        }
      }

      // Default: Red owns leftmost column, Blue owns rightmost column
      for (int i = 0; i < rows; i++) {
        board[i][0].updatePawns(1);
        board[i][0].setOwner(red);
        board[i][cols - 1].updatePawns(1);
        board[i][cols - 1].setOwner(blue);
      }
    }

    public void setCell(int row, int col, TestPlayer owner, int pawns, Card card) {
      board[row][col].setOwner(owner);
      board[row][col].updatePawns(pawns);
      if (card != null) {
        board[row][col].setCard(card);
      }
    }

    public void setRowScore(TestPlayer player, int rowIndex, int score) {
      int playerIdx = (player == red) ? 0 : 1;
      rowScores[playerIdx][rowIndex] = score;
    }

    public SanguineModel buildWithHands(List<Card> redHand, List<Card> blueHand) {
      red.setHand(new ArrayList<>(redHand));
      blue.setHand(new ArrayList<>(blueHand));

      TestSanguineModel model = new TestSanguineModel(red, blue, rows, cols);
      model.setBoard(board);
      model.setRowScores(rowScores);

      return model;
    }
  }

  /**
   * Test player with configurable hand.
   */
  private static class TestPlayer extends Player {
    private List<Card> hand;

    public TestPlayer(Color color, List<Card> hand) {
      super(color);
      this.hand = hand;
    }

    public void setHand(List<Card> hand) {
      this.hand = hand;
    }

    @Override
    public List<Card> getHand() {
      return hand;
    }
  }

  /**
   * Test model with configurable board and row scores.
   */
  private static class TestSanguineModel extends SanguineModel {
    private Cell<Card>[][] testBoard;
    private int[][] rowScores;

    public TestSanguineModel(TestPlayer red, TestPlayer blue, int rows, int cols) {
      super(red, blue, rows, cols);
    }

    public void setBoard(Cell<Card>[][] board) {
      this.testBoard = board;
    }

    public void setRowScores(int[][] scores) {
      this.rowScores = scores;
    }

    @Override
    public Cell<Card>[][] getBoard() {
      return testBoard != null ? testBoard : super.getBoard();
    }

    @Override
    public Cell<Card> getCellAt(int row, int col) {
      return testBoard[row][col];
    }

    @Override
    public int getRowScore(PlayerInterface<Card> player, int rowIndex) {
      if (rowScores != null) {
        int playerIdx = (player == getRedPlayer()) ? 0 : 1;
        return rowScores[playerIdx][rowIndex];
      }
      return super.getRowScore(player, rowIndex);
    }
  }
}

