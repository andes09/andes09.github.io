package klondike.view;

import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.Card;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.SingleCard;

/**
 * Shows a textual view of the Game you are playing.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel<?> model;

  /**
   * Establishes the model.
   *
   * @param model KlondikeModel
   */
  public KlondikeTextualView(KlondikeModel<?> model) {
    this.model = model;
  }

  /**
   * Creates the textual view.
   *
   * @return String of the board
   */
  public String toString() {
    // Handle case where game hasn't started
    if (model == null) {
      return "";
    }

    StringBuilder str = new StringBuilder();

    // Draw pile
    str.append("Draw: ");
    List<?> drawCards = model.getDrawCards();
    if (drawCards != null && !drawCards.isEmpty()) {
      for (int i = 0; i < drawCards.size(); i++) {
        str.append(drawCards.get(i).toString());
        if (i < drawCards.size() - 1) {
          str.append(", ");
        }
      }
    }
    str.append("\n");

    // Foundation piles
    str.append("Foundation: ");
    for (int foundation = 0; foundation < model.getNumFoundations(); foundation++) {
      Card foundationCard = model.getCardAt(foundation);
      if (foundationCard == null) {
        str.append("<none>");
      } else {
        str.append(foundationCard.toString());
      }
      if (foundation < model.getNumFoundations() - 1) {
        str.append(", ");
      }
    }
    str.append("\n");

    // Cascade piles
    int maxRows = model.getNumRows();
    if (maxRows == 0) {
      // All piles are empty
      for (int pile = 0; pile < model.getNumPiles(); pile++) {
        str.append("  X");
        if (pile < model.getNumPiles() - 1) {
          str.append(" ");
        }
      }
    } else {
      for (int row = 0; row < maxRows; row++) {
        for (int pile = 0; pile < model.getNumPiles(); pile++) {
          if (row < model.getPileHeight(pile)) {
            if (model.isCardVisible(pile, row)) {
              Card card = model.getCardAt(pile, row);
              String cardStr = card.toString();
              if (cardStr.length() == 1) {
                str.append("  ").append(cardStr);
              } else if (cardStr.length() == 2) {
                str.append(" ").append(cardStr);
              } else {
                str.append(cardStr);
              }
            } else {
              str.append("  ?");
            }
          } else if (row == 0 && model.getPileHeight(pile) == 0) {
            str.append("  X");
          } else {
            str.append("   ");
          }
        }
      }
    }
    return str.toString();
  }

}




