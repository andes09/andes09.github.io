import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.SingleCard;
import klondike.view.KlondikeTextualView;
import org.junit.Before;
import org.junit.Test;

/**
 * A class for testing the KlondikeModel. All tests
 * in this class cannot create Card type objects. Instead,
 * the tests use the createNewDeck method to help create
 * example games.
 */
public class KlondikeModelTests {
  KlondikeModel<SingleCard> model;
  private List<SingleCard> standardDeck;

  /**
   * Sets
   */
  @Before
  public void setUp() {
    model = new BasicKlondike();
    standardDeck = model.createNewDeck(); // 52-card deck: A-K in 4 suits
  }

  @Test
  public void testStartGameWithNullDeck() {
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> model.startGame(null, false, 7, 3)
    );
    assertEquals("Invalid Parameters", ex.getMessage());
  }

  @Test
  public void testStartGameWithNullCardInDeck() {
    standardDeck.set(10, null);
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 7, 3)
    );
    assertEquals("Card is null", ex.getMessage());
  }

  @Test
  public void testStartGameWithNonPositiveNumPiles() {
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 0, 3));
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, -5, 3));
  }

  @Test
  public void testStartGameWithNonPositiveNumDraw() {
    assertThrows(IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 7, -1));
  }

  @Test
  public void testStartGameWithMissingAce() {
    standardDeck.set(0, new SingleCard(2, "clubs"));
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> model.startGame(standardDeck, false, 7, 3)
    );
    assertEquals("Invalid Parameters", ex.getMessage());
  }

  @Test
  public void testStartGameWithTooSmallDeck() {
    // Create minimal deck with 4 aces but not enough for cascade
    List<SingleCard> tinyDeck = Arrays.asList(
        new SingleCard(1, "hearts"),
        new SingleCard(1, "clubs"),
        new SingleCard(1, "diamonds"),
        new SingleCard(1, "spades")
    );
    IllegalArgumentException ex = assertThrows(
        IllegalArgumentException.class,
        () -> model.startGame(tinyDeck, false, 7, 3)
    );
    assertEquals("Deck to small", ex.getMessage());
  }

  @Test
  public void testStartGameWhenAlreadyStarted() {
    model.startGame(standardDeck, false, 7, 3);
    IllegalStateException ex = assertThrows(
        IllegalStateException.class,
        () -> model.startGame(standardDeck, false, 7, 3)
    );
    assertEquals("Game already started", ex.getMessage());
  }

  @Test
  public void testStartGameWithStandardDeck() {
    model.startGame(standardDeck, false, 7, 3);
    assertEquals(7, model.getNumPiles());
    assertEquals(4, model.getNumFoundations());
    assertEquals(3, model.getNumDraw());
    assertEquals(0, model.getScore());
    assertFalse(model.isGameOver());
    for (int i = 0; i < 7; i++) {
      assertEquals(i + 1, model.getPileHeight(i));
    }
    for (int pile = 0; pile < 7; pile++) {
      for (int cardIndex = 0; cardIndex < pile; cardIndex++) {
        assertFalse(model.isCardVisible(pile, cardIndex));
      }
    }
    for (int f = 0; f < 4; f++) {
      assertNull(model.getCardAt(f));
    }
    assertEquals(3, model.getDrawCards().size());
    for (SingleCard card : model.getDrawCards()) {
      assertTrue(card.isFaceUp());
    }
  }

  @Test
  public void testMovePileBeforeGameStart() {
    assertThrows(IllegalStateException.class,
        () -> model.movePile(0, 1, 1));
  }

  @Test
  public void testMovePileSameSourceAndDest() {
    model.startGame(standardDeck, false, 7, 3);
    assertThrows(IllegalArgumentException.class,
        () -> model.movePile(0, 1, 0));
  }

  @Test
  public void testMovePileInvalidNumCards() {
    model.startGame(standardDeck, false, 7, 3);
    assertThrows(IllegalArgumentException.class,
        () -> model.movePile(0, 0, 1));
    assertThrows(IllegalArgumentException.class,
        () -> model.movePile(0, 2, 1));
  }

  @Test
  public void testMoveDrawBeforeGameStart() {
    assertThrows(IllegalStateException.class,
        () -> model.moveDraw(0));
  }

  @Test
  public void testMoveDrawInvalidDestPile() {
    model.startGame(standardDeck, false, 7, 3);
    assertThrows(IllegalArgumentException.class,
        () -> model.moveDraw(-1));
    assertThrows(IllegalArgumentException.class,
        () -> model.moveDraw(7));
  }

  @Test
  public void testToStringWithEmptyModel() {
    BasicKlondike model = new BasicKlondike(); // Assumes it starts empty or initialized
    model.startGame(standardDeck, false, 7, 3);
    KlondikeTextualView view = new KlondikeTextualView(model);

    String output = view.toString();
    assertNotNull(output);

    assertTrue(output.contains("Draw:"));
    assertTrue(output.contains("Foundation:"));

    assertTrue(output.contains("  ?"));
    assertTrue(output.contains("  X"));
  }
}
