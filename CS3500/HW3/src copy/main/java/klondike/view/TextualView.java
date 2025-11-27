package klondike.view;

import java.io.IOException;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.SingleCard;

/**
 * Textual view interface.
 * empty for now
 */
public interface TextualView {

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;
}


