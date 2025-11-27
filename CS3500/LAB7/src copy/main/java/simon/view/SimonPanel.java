package simon.view;




import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import simon.model.ColorGuess;
import simon.model.ReadOnlySimon;

/**
 * A JSimonPanel will draw all the colors, allow users to click on them,
 * and play the game.
 */
public class SimonPanel extends JPanel implements Panel {
  /**
   * Our view will need to display a model, so it needs to get the current sequence from the model.
   */
  private final ReadOnlySimon model;
  /**
   * We'll allow an arbitrary number of listeners for our events...even if
   * we happen right now to only expect a single listener.
   */
  private final List<ViewFeatures> featuresListeners;

  /**
   * INVARIANT: currentRoundOfColorGuesses is never empty.
   */
  private final Stack<ColorGuess> currentRoundOfColorGuesses;

  private boolean mouseIsDown;
  private ColorGuess activeColorGuess;

  private static final Map<ColorGuess, Point2D> CIRCLE_CENTERS = Map.of(
      ColorGuess.Red, new Point2D.Double(10, 0),
      ColorGuess.Yellow, new Point2D.Double(0, 10),
      ColorGuess.Green, new Point2D.Double(-10, 0),
      ColorGuess.Blue, new Point2D.Double(0, -10)
  );
  private static final Map<ColorGuess, Color> CIRCLE_COLORS = Map.of(
      ColorGuess.Red, Color.CYAN,
      ColorGuess.Blue, Color.MAGENTA,
      ColorGuess.Yellow, Color.ORANGE,
      ColorGuess.Green, Color.PINK
  );

  private static final double CIRCLE_RADIUS = 5;

  /**
   * Creates the panel for viewing the model and listening for mouse clicks
   * and motions.
   *
   * @param model the model to display
   */
  public SimonPanel(ReadOnlySimon model) {
    this.model = Objects.requireNonNull(model);
    this.featuresListeners = new ArrayList<>();
    this.currentRoundOfColorGuesses = new Stack<>();
    this.currentRoundOfColorGuesses.addAll(this.model.getCurrentSequence());
    MouseEventsListener listener = new MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);
  }

  /**
   * This method tells Swing what the "natural" size should be
   * for this panel.  Here, we set it to 400x400 pixels.
   *
   * @return Our preferred *physical* size.
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(350, 350);
  }

  /**
   * Conceptually, we can choose a different coordinate system
   * and pretend that our panel is 40x40 "cells" big. You can choose
   * any dimension you want here, including the same as your physical
   * size (in which case each logical pixel will be the same size as a physical
   * pixel, but perhaps your calculations to position things might be trickier)
   *
   * @return Our preferred *logical* size.
   */
  private Dimension getPreferredLogicalSize() {
    return new Dimension(40, 40);
  }

  @Override
  public void addFeaturesListener(ViewFeatures features) {
    this.featuresListeners.add(Objects.requireNonNull(features));
  }

  @Override
  public void advance() {
    System.err.println("Yay!");
    this.currentRoundOfColorGuesses.pop();
    if (this.currentRoundOfColorGuesses.isEmpty()) {
      this.currentRoundOfColorGuesses.addAll(this.model.getCurrentSequence());
    }
    this.repaint();
  }

  /**
   * Shows an error message for an incorrect guess and repaints the current
   * game sequence.
   */
  public void error() {
    JOptionPane.showMessageDialog(this,
        "Incorrect guess! Try again.",
        "Error",
        JOptionPane.ERROR_MESSAGE
    );
    this.currentRoundOfColorGuesses.clear();
    this.currentRoundOfColorGuesses.addAll(this.model.getCurrentSequence());
    this.repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setTransform(transformLogicalToPhysical());

    for (ColorGuess c : ColorGuess.values()) {
      boolean isActive = (c == activeColorGuess && mouseIsDown);
      drawCircle(g2d,
          CIRCLE_COLORS.get(c),
          CIRCLE_CENTERS.get(c).getX(),
          CIRCLE_CENTERS.get(c).getY(),
          isActive
      );
    }
  }

  @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
  private void drawCircle(Graphics2D g2d, Color color, double x, double y, boolean filled) {
    Color oldColor = g2d.getColor();
    AffineTransform oldTransform = g2d.getTransform();
    g2d.setColor(color);
    g2d.translate(x, y);
    Shape circle = new Ellipse2D.Double(
        -CIRCLE_RADIUS, -CIRCLE_RADIUS,
        2 * CIRCLE_RADIUS, 2 * CIRCLE_RADIUS
    );
    if (filled) {
      g2d.fill(circle);
    } else {
      g2d.draw(circle);
    }
    g2d.setTransform(oldTransform);
    g2d.setColor(oldColor);
  }


  /**
   * Computes the transformation that converts board coordinates
   * (with (0,0) in center, width and height our logical size)
   * into screen coordinates (with (0,0) in upper-left,
   * width and height in pixels).
   *
   * <p>This is the inverse of {@link SimonPanel#transformPhysicalToLogical()}.
   *
   * @return The necessary transformation
   */
  private AffineTransform transformLogicalToPhysical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.translate(getWidth() / 2., getHeight() / 2.);
    ret.scale(getWidth() / preferred.getWidth(), getHeight() / preferred.getHeight());
    ret.scale(1, -1);
    return ret;
  }

  /**
   * Computes the transformation that converts screen coordinates
   * (with (0,0) in upper-left, width and height in pixels)
   * into board coordinates (with (0,0) in center, width and height
   * our logical size).
   *
   * <p>This is the inverse of {@link SimonPanel#transformLogicalToPhysical()}.
   *
   * @return The necessary transformation
   */
  private AffineTransform transformPhysicalToLogical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(1, -1);
    ret.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    ret.translate(-getWidth() / 2., -getHeight() / 2.);
    return ret;
  }

  //We've chosen to make this an inner class instead of an outer class
  //just to have you see what happens. You could easily make this an outer
  //class, but it means your panel needs to expose observers for any fields.
  private class MouseEventsListener extends MouseInputAdapter {

    @Override
    public void mousePressed(MouseEvent e) {
      SimonPanel.this.mouseIsDown = true;
      this.mouseDragged(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      SimonPanel.this.mouseIsDown = false;
      if (SimonPanel.this.activeColorGuess != null) {
        for (ViewFeatures listener : SimonPanel.this.featuresListeners) {
          listener.selectedColor(SimonPanel.this.activeColorGuess);
        }
      }
      SimonPanel.this.activeColorGuess = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      // This point is measured in actual physical pixels
      Point physicalP = e.getPoint();
      // For us to figure out which circle it belongs to, we need to transform it
      // into logical coordinates
      Point2D logicalP = transformPhysicalToLogical().transform(physicalP, null);
      // TODO: Figure out whether this location is inside a circle, and if so, which one

      SimonPanel.this.activeColorGuess = null;
      for (ColorGuess c : ColorGuess.values()) {
        Point2D center = CIRCLE_CENTERS.get(c);
        double distance = center.distance(logicalP);
        if (distance <= CIRCLE_RADIUS) {
          SimonPanel.this.activeColorGuess = c;
          break;
        }
      }
      SimonPanel.this.repaint();
    }

  }
}
