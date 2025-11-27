package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import model.Card;
import model.Cell;
import model.SanguineModelInterface;

/**
 * Panel that displays the game board.
 */
public class SanguineBoardPanel extends JPanel {
  private final SanguineModelInterface<Card, Cell<Card>> model;
  private int selectedRow;
  private int selectedCol;
  private static final int CELL_SIZE = 50;
  private static final int PADDING = 20;

  /**
   * Constructs a new SaguineBoardPanel with the given model.
   *
   * @param model the model of the game
   */
  public SanguineBoardPanel(SanguineModelInterface<Card, Cell<Card>> model) {
    this.model = model;
    this.selectedRow = -1;
    this.selectedCol = -1;
    setBackground(Color.GRAY);
  }

  @Override
  public Dimension getPreferredSize() {
    int scoreWidth = 50;
    int width = PADDING * 2 + model.getNumCols() * CELL_SIZE + scoreWidth * 2;
    int height = PADDING * 2 + model.getNumRows() * CELL_SIZE;
    return new Dimension(width, height);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int scoreWidth = 50;

    // Score Cells
    for (int row = 0; row < model.getNumRows(); row++) {
      int y = PADDING + row * CELL_SIZE + CELL_SIZE / 2;

      int redScore = model.getRowScore(model.getRedPlayer(), row);
      g2d.setColor(Color.RED);
      g2d.setFont(new Font("Arial", Font.BOLD, 16));
      g2d.drawString(String.valueOf(redScore), scoreWidth, y);

      int blueScore = model.getRowScore(model.getBluePlayer(), row);
      g2d.setColor(Color.BLUE);
      int rightX = PADDING + scoreWidth + model.getNumCols() * CELL_SIZE + 10;
      g2d.drawString(String.valueOf(blueScore), rightX, y);
    }

    //Board Cells
    for (int row = 0; row < model.getNumRows(); row++) {
      for (int col = 0; col < model.getNumCols(); col++) {
        drawCell(g2d, row, col);
      }
    }

  }

  private void drawCell(Graphics2D g2d, int row, int col) {
    int scoreWidth = 50;
    int x = PADDING + scoreWidth + col * CELL_SIZE;
    int y = PADDING + row * CELL_SIZE;



    // Highlight if selected
    if (row == selectedRow && col == selectedCol) {
      g2d.setColor(Color.CYAN);
    } else {
      g2d.setColor(Color.WHITE);
    }
    g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
    g2d.setColor(Color.BLACK);
    g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
    Cell<Card> cell = model.getCellAt(row, col);
    if (cell.getCard() != null) {
      drawCardOnBoard(g2d, cell, x, y);
    } else if (!cell.isEmpty()) {
      drawPawns(g2d, cell, x, y);
    }
  }

  private void drawCardOnBoard(Graphics2D g2d, Cell<Card> cell, int x, int y) {
    Color ownerColor = cell.getOwner().getPlayerColor();
    g2d.setColor(ownerColor);
    g2d.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);

    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 14));
    String value = String.valueOf(cell.getCard().getValue());
    g2d.drawString(value, x + CELL_SIZE / 2 - 5, y + CELL_SIZE / 2);
  }

  private void drawPawns(Graphics2D g2d, Cell<Card> cell, int x, int y) {
    Color ownerColor = cell.getOwner().getPlayerColor();
    g2d.setColor(ownerColor);
    g2d.setFont(new Font("Arial", Font.BOLD, 20));
    String pawns = String.valueOf(cell.getNumPawns());
    g2d.drawString(pawns, x + CELL_SIZE / 2 - 5, y + CELL_SIZE / 2);
  }

  /**
   * gets the cell at the given mouse coordinates.
   *
   * @param mouseX x coordinate of mouse
   * @param mouseY y coordinate of mouse
   * @return array of row and column at mouse coordinates, null if no cell found
   */
  public int[] getCellAt(int mouseX, int mouseY) {
    int scoreWidth = 50;
    int col = (mouseX - PADDING - scoreWidth) / CELL_SIZE;
    int row = (mouseY - PADDING) / CELL_SIZE;
    if (row >= 0 && row < model.getNumRows() && col >= 0 && col < model.getNumCols()) {
      return new int[] {row, col};
    }
    return null;
  }

  /**
   * sets the selected cell.
   *
   * @param row row index of cell
   * @param col column index of cell
   */
  public void setSelectedCell(int row, int col) {
    this.selectedRow = row;
    this.selectedCol = col;
    repaint();
  }


  /**
   * gets the selected row.
   *
   * @return selected row index
   */
  public int getSelectedRow() {
    return selectedRow;
  }

  /**
   * gets the selected column.
   *
   * @return selected column index
   */
  public int getSelectedCol() {
    return selectedCol;
  }

}
