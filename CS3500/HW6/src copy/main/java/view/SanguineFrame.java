package view;


import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import model.Card;
import model.Cell;
import model.SanguineModelInterface;

/**
 * Main frame for the Sanguine GUI.
 * Contains panel for hand and board.
 */
public class SanguineFrame extends JFrame implements SanguineGuiViewInterface {
  private final SanguineModelInterface<Card, Cell<Card>> model;
  private final SanguineHandPanel handPanel;
  private final SanguineBoardPanel boardPanel;

  /**
   * Sets up the frame layout with hand and board panel.
   *
   * @param model the model
   */
  public SanguineFrame(SanguineModelInterface<Card, Cell<Card>> model) {
    super("Sanguine Game");
    this.model = model;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    handPanel = new SanguineHandPanel(model);
    boardPanel = new SanguineBoardPanel(model);
    add(handPanel, BorderLayout.SOUTH);
    add(boardPanel, BorderLayout.CENTER);

    pack();
    setLocationRelativeTo(null);

  }

  /**
   * refreshes the GUI.
   */
  public void refresh() {
    handPanel.repaint();
    boardPanel.repaint();
  }

  @Override
  public void addMouseListener(MouseListener listener) {
    handPanel.addMouseListener(listener);
    boardPanel.addMouseListener(listener);
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    super.addKeyListener(listener);
    handPanel.addKeyListener(listener);
    boardPanel.addKeyListener(listener);
    setFocusable(true);
  }

  @Override
  public SanguineHandPanel getHandPanel() {
    return handPanel;
  }

  @Override
  public SanguineBoardPanel getBoardPanel() {
    return boardPanel;
  }


}
