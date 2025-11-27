package simon.view;

import javax.swing.JFrame;
import simon.model.ReadOnlySimon;

/**
 * A simple graphical view for the game of SimonSays. Allows for interactions
 * via the mouse.
 */
public class SimpleSimonView extends JFrame implements SimonView {
  private final SimonPanel panel;

  /**
   * Constructs a simple graphical view for the given model but does not
   * display it.
   *
   * @param model the model to be displayed
   */
  public SimpleSimonView(ReadOnlySimon model) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.panel = new SimonPanel(model);
    this.add(panel);
    this.pack();
  }

  @Override
  public void addFeatureListener(ViewFeatures features) {
    this.panel.addFeaturesListener(features);
  }

  @Override
  public void display(boolean show) {
    this.setVisible(show);
  }

  @Override
  public void advance() {
    this.panel.advance();
  }

  @Override
  public void error() {
    this.panel.error();
  }
}
