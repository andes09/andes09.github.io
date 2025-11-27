

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;
import klondike.model.hw02.SingleCard;
import klondike.model.hw04.WhiteheadKlondike;
import java.util.List;

/**
 * Tests the WhiteheadKlondike class.
 */
public class KlondikeWhiteheadTest {

  @Test
  public void testAllCardsDealtFaceUp() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    game.startGame(game.createNewDeck(), false, 7, 3);

    // Check all cards in all piles are visible
    for (int pile = 0; pile < game.getNumPiles(); pile++) {
      for (int card = 0; card < game.getPileHeight(pile); card++) {
        assertTrue(
            "Card at pile " + pile + " card " + card + " should be face up",
            game.isCardVisible(pile, card)
        );
      }
    }
  }

  @Test
  public void testSameColorBuildAllowed() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    List<SingleCard> deck = game.createNewDeck();
    game.startGame(deck, false, 3, 3);
    
    // All cards should be visible in Whitehead
    assertTrue(game.isCardVisible(0, 0));
    assertTrue(game.isCardVisible(1, 0));
    assertTrue(game.isCardVisible(2, 0));
  }

  @Test
  public void testMultipleCardsInPile() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    game.startGame(game.createNewDeck(), false, 7, 3);
    
    // Pile 6 should have 7 cards
    assertEquals(7, game.getPileHeight(6));
    
    // All should be visible in Whitehead (unlike BasicKlondike)
    for (int i = 0; i < 7; i++) {
      assertTrue(game.isCardVisible(6, i));
    }
  }

  @Test
  public void testStartGameWithNullDeck() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    
    assertThrows(IllegalArgumentException.class, () -> {
      game.startGame(null, false, 7, 3);
    });
  }

  @Test
  public void testStartGameWhenAlreadyStarted() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    List<SingleCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    
    assertThrows(IllegalStateException.class, () -> {
      game.startGame(deck, false, 7, 3);
    });
  }

  @Test
  public void testStartGameWithNonPositiveNumPiles() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    List<SingleCard> deck = game.createNewDeck();
    
    assertThrows(IllegalArgumentException.class, () -> {
      game.startGame(deck, false, 0, 3);
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
      game.startGame(deck, false, -5, 3);
    });
  }

  @Test
  public void testStartGameWithNegativeNumDraw() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    List<SingleCard> deck = game.createNewDeck();
    
    assertThrows(IllegalArgumentException.class, () -> {
      game.startGame(deck, false, 7, -1);
    });
  }

  @Test
  public void testMovePileBeforeGameStart() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    
    assertThrows(IllegalStateException.class, () -> {
      game.movePile(0, 1, 1);
    });
  }

  @Test
  public void testMovePileSameSourceAndDest() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    List<SingleCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    
    assertThrows(IllegalArgumentException.class, () -> {
      game.movePile(0, 1, 0);
    });
  }

  @Test
  public void testGetCardAtInvalidPileIndex() {
    WhiteheadKlondike game = new WhiteheadKlondike();
    List<SingleCard> deck = game.createNewDeck();
    game.startGame(deck, false, 7, 3);
    
    assertThrows(IllegalArgumentException.class, () -> {
      game.getCardAt(-1, 0);
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
      game.getCardAt(7, 0);
    });
  }

}