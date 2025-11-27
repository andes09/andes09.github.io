package view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Interface for the Sanguine GUI View.
 * provodes methods to display the game
 * and handle user input.
 */
public interface SanguineGuiViewInterface {

  /**
   * Sets the visibility of the GUI.
   *
   * @param visible true if the GUI should be visible, false otherwise
   */
  void setVisible(boolean visible);

  /**
   * Adds a mouse listener to handle mouse events.
   *
   * @param listener the mouse listener to add
   *
   */
  void addMouseListener(MouseListener listener);

  /**
   * Adds a key listener to handle key events.
   *
   * @param listener the key listener to add
   */
  void addKeyListener(KeyListener listener);

  /**
   * Gets the hand panel.
   *
   * @return the hand panel
   */
  public SanguineHandPanel getHandPanel();

  /**
   * Gets the board panel.
   *
   * @return the board panel
   */
  public SanguineBoardPanel getBoardPanel();
}
