package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import model.Card;
import model.Cell;
import model.SanguineModelInterface;
import view.SanguineFrame;

/**
 * Stub controller for testing the view.
 * Prints messages when user interactions occur.
 */
public class StubController implements MouseListener, KeyListener {
  private final SanguineModelInterface<Card, Cell<Card>> model;
  private final SanguineFrame view;

  /**
   * Constructs a new StubController with the given model and view.
   *
   * @param model the model of the game
   * @param view  the view of the game
   */
  public StubController(SanguineModelInterface<Card, Cell<Card>> model, SanguineFrame view) {
    this.model = model;
    this.view = view;

    view.addMouseListener(this);
    view.addKeyListener(this);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // Check if click was in hand panel
    if (e.getSource() == view.getHandPanel()) {
      int cardIndex = view.getHandPanel().getCardIndexAt(e.getX(), e.getY());
      if (cardIndex != -1) {
        if (view.getHandPanel().getSelectedCardIndex() == cardIndex) {
          // Deselect if clicking same card
          view.getHandPanel().setSelectedCard(-1);
          System.out.println("Card deselected");
        } else {
          view.getHandPanel().setSelectedCard(cardIndex);
          String player = model.getCurrentPlayer().getPlayerColor().equals(java.awt.Color.RED)
              ? "Red" : "Blue";
          System.out.println(
              "Card selected: index " + cardIndex + " from " + player + " player's hand");
        }
      }
    }

    if (e.getSource() == view.getBoardPanel()) {
      int[] cell = view.getBoardPanel().getCellAt(e.getX(), e.getY());
      if (cell != null) {
        int row = cell[0];
        int col = cell[1];
        if (view.getBoardPanel().getSelectedRow() == row
            && view.getBoardPanel().getSelectedCol() == col) {
          // Deselect if clicking same cell
          view.getBoardPanel().setSelectedCell(-1, -1);
          System.out.println("Cell deselected");
        } else {
          view.getBoardPanel().setSelectedCell(row, col);
          System.out.println("Cell selected at coordinates: (" + row + ", " + col + ")");
        }
      }
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      System.out.println("Key press: CONFIRM move");
      confirmMove();
    } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
      System.out.println("Key press: PASS turn");
      passMove();
    }
  }

  /**
   * Confirms and executes the currently selected move.
   */
  private void confirmMove() {
    int cardIndex = view.getHandPanel().getSelectedCardIndex();
    int row = view.getBoardPanel().getSelectedRow();
    int col = view.getBoardPanel().getSelectedCol();

    // Check if both card and cell are selected
    if (cardIndex == -1) {
      System.out.println("ERROR: No card selected. Please select a card first.");
      return;
    }

    if (row == -1 || col == -1) {
      System.out.println("ERROR: No cell selected. Please select a cell first.");
      return;
    }

    if (!model.isLegalMove(row, col, cardIndex)) {
      System.out.println("ERROR: Illegal move! Cannot play that card at (" 
          + row + ", " + col + ")");
      return;
    }

    try {
      String player = model.getCurrentPlayer().getPlayerColor()
          .equals(java.awt.Color.RED) ? "Red" : "Blue";
      System.out.println(player + " plays card " + cardIndex + " at (" 
          + row + ", " + col + ")");
      
      model.playCardFromHand(row, col, cardIndex);

      // Clear selections
      view.getHandPanel().setSelectedCard(-1);
      view.getBoardPanel().setSelectedCell(-1, -1);

      // Refresh the view
      view.refresh();

      // Check if game is over
      if (model.isGameOver()) {
        System.out.println("GAME OVER!");
        var winner = model.getWinner();
        if (winner != null) {
          String winnerName = winner.getPlayerColor()
              .equals(java.awt.Color.RED) ? "Red" : "Blue";
          System.out.println(winnerName + " wins!");
        } else {
          System.out.println("It's a tie!");
        }
      } else {
        String nextPlayer = model.getCurrentPlayer().getPlayerColor()
            .equals(java.awt.Color.RED) ? "Red" : "Blue";
        System.out.println("Now it's " + nextPlayer + "'s turn");
      }

    } catch (Exception ex) {
      System.out.println("ERROR executing move: " + ex.getMessage());
    }
  }

  /**
   * Passes the current player's turn.
   */
  private void passMove() {
    String player = model.getCurrentPlayer().getPlayerColor()
        .equals(java.awt.Color.RED) ? "Red" : "Blue";
    System.out.println(player + " passes their turn");

    model.pass();

    // Clear selections
    view.getHandPanel().setSelectedCard(-1);
    view.getBoardPanel().setSelectedCell(-1, -1);

    // Refresh the view
    view.refresh();

    String nextPlayer = model.getCurrentPlayer().getPlayerColor()
        .equals(java.awt.Color.RED) ? "Red" : "Blue";
    System.out.println("Now it's " + nextPlayer + "'s turn");
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
