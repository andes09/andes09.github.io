package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import javax.swing.JPanel;
import model.Card;
import model.Cell;
import model.SanguineModelInterface;

/**
 * Panel creates hand panel.
 */
public class SanguineHandPanel extends JPanel {
  private final SanguineModelInterface<Card, Cell<Card>> model;
  private int selectedCardIndex;
  private static final int CARD_WIDTH = 100;
  private static final int CARD_HEIGHT = 150;
  private static final int CARD_SPACING = 10;
  private static final int PADDING = 20;

  /**
   * Constructs a new SanguineHandPanel.
   *
   * @param model the model of the game
   */
  public SanguineHandPanel(SanguineModelInterface<Card, Cell<Card>> model) {
    this.model = model;
    this.selectedCardIndex = -1;
    setBackground(Color.LIGHT_GRAY);
  }

  @Override
  public Dimension getPreferredSize() {
    List<Card> hand = model.getCurrentPlayerHand();
    int width = PADDING * 2 + hand.size() * (CARD_WIDTH + CARD_SPACING);
    return new Dimension(width, CARD_HEIGHT + PADDING * 2);

  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;


    Color playerColor = model.getCurrentPlayer().getPlayerColor();

    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, 16));

    if (playerColor.equals(Color.RED)) {
      g2d.drawString("Red Player's turn", PADDING, 15);
    } else {
      g2d.drawString("Blue Player's turn", PADDING, 15);
    }
    List<Card> hand = model.getCurrentPlayerHand();
    for (int i = 0; i < hand.size(); i++) {
      drawCard(g2d, hand.get(i), i, playerColor);
    }

  }

  private void drawCard(Graphics2D g2d, Card card, int index, Color playerColor) {
    int x = PADDING + index * (CARD_WIDTH + CARD_SPACING);
    int y = PADDING + 20;

    // Highlight if selected
    if (index == selectedCardIndex) {
      g2d.setColor(Color.CYAN);
    } else {
      g2d.setColor(Color.WHITE);
    }
    g2d.fillRect(x, y, CARD_WIDTH, CARD_HEIGHT);

    // Draw border
    g2d.setColor(Color.BLACK);
    g2d.drawRect(x, y, CARD_WIDTH, CARD_HEIGHT);

    // Draw card info
    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
    g2d.drawString(card.getName(), x + 5, y + 15);
    g2d.drawString("Cost: " + card.getCost(), x + 5, y + 30);
    g2d.drawString("Value: " + card.getValue(), x + 5, y + 45);

    // Draw influence grid
    drawInfluenceGrid(g2d, card, x + 10, y + 55, playerColor);
  }

  private void drawInfluenceGrid(Graphics2D g2d, Card card, int startX, int startY,
                                 Color playerColor) {
    boolean[][] influence = card.getInfluence();
    if (playerColor.equals(Color.BLUE)) {
      influence = mirrorInfluence(influence);
    }

    int cellSize = 15;
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        int x = startX + j * cellSize;
        int y = startY + i * cellSize;

        if (influence[i][j]) {
          g2d.setColor(Color.GREEN);
          g2d.fillRect(x, y, cellSize - 1, cellSize - 1);
        }
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, cellSize - 1, cellSize - 1);
      }
    }
  }

  private boolean[][] mirrorInfluence(boolean[][] influence) {
    boolean[][] mirrored = new boolean[5][5];
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        mirrored[i][j] = influence[i][4 - j];
      }
    }
    return mirrored;
  }

  /**
   * gets the card index given mouse coordinates.
   *
   * @param mouseX x coordinate of mouse
   * @param mouseY y coordinate of mouse
   * @return card index at mouse coordinates, -1 if no card found
   */
  public int getCardIndexAt(int mouseX, int mouseY) {
    int y = PADDING * 2;
    if (mouseY < y || mouseY > y + CARD_HEIGHT) {
      return -1;
    }
    List<Card> hand = model.getCurrentPlayerHand();
    for (int i = 0; i < hand.size(); i++) {
      int x = PADDING + i * (CARD_WIDTH + CARD_SPACING);
      if (mouseX >= x && mouseX <= x + CARD_WIDTH) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Sets the selected card index.
   *
   * @param index the card index to select, or -1 to deselect
   */
  public void setSelectedCard(int index) {
    this.selectedCardIndex = index;
    repaint();
  }

  /**
   * Gets the selected card index.
   *
   * @return the selected card index, or -1 if no card is selected
   */
  public int getSelectedCardIndex() {
    return selectedCardIndex;
  }
}
