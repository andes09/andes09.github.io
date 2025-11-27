import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.*;
import org.junit.Before;
import org.junit.Test;

import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.SingleCard;
import klondike.view.KlondikeTextualView;
import klondike.controller.KlondikeController;
import klondike.controller.KlondikeTextualController;
import klondike.view.TextualView;
import klondike.view.KlondikeTextualView;
import java.io.IOException;


/**
 * Tests the KlondikeModelController class.
 */
public class KlondikeModelControllerTest {
  private Readable rd;
  private Appendable ap;
  private KlondikeModel<SingleCard> model;
  private KlondikeController kc;
  private TextualView tv;

  public KlondikeController setUp(Readable rd, Appendable ap) {
    this.rd = rd;
    this.ap = ap;
    this.model = new BasicKlondike();
    this.kc = new KlondikeTextualController(rd, ap);
    this.tv = new KlondikeTextualView(model);
    return kc;
  }

  @Test
  public void invalidReadableAndAppendable() {

    assertThrows(IllegalArgumentException.class,
        () -> setUp(null, new StringBuilder()));
    assertThrows(IllegalArgumentException.class,
        () -> setUp(new StringReader(" "), null));
  }

  @Test
  public void invalidModel() {
    String input = "test";
    StringReader rd = new StringReader(input);
    setUp(rd, new StringBuilder());
    assertThrows(IllegalArgumentException.class,
        () -> kc.playGame(null, model.createNewDeck(), false, 7, 3));

  }

  @Test
  public void validCommand() {
 
    String input = "mpf 1 1 mpp 2 1 1 mpp 2 1 1 mpp 4 1 3 mpp 3 2 4 md 4 mdf 2 dd q";
    StringReader rd = new StringReader(input);
    StringBuilder ap = new StringBuilder();
    setUp(rd, ap);
    kc.playGame(model, testDeck(), false, 4, 3);
    String output = ap.toString();
    System.out.println(output);
    assertTrue(output.contains("Move pile to foundation pile"));
    assertTrue(output.contains("Move pile to cascade pile"));
    assertTrue(output.contains("Move draw to cascade pile"));
    assertTrue(output.contains("Move draw to foundation pile"));
    assertTrue(output.contains("Discard draw"));
    assertTrue(output.contains("Game quit!"));
  }

  @Test
  public void invalidCommand() {
    String input = "invalid q";
    StringReader rd = new StringReader(input);
    StringBuilder ap = new StringBuilder();
    setUp(rd, ap);
    kc.playGame(model, testDeck(), false, 4, 3);
    String output = ap.toString();
    System.out.println(output);
    assertTrue(output.contains("Invalid move. Play again. Unknown command"));
    assertTrue(output.contains("Game quit!"));
  }


  @Test
  public void testQuitAtStart() {
    String input = "q";
    StringReader rd = new StringReader(input);
    StringBuilder ap = new StringBuilder();
    setUp(rd, ap);
    kc.playGame(model, testDeck(), false, 4, 3);
    String output = ap.toString();
    System.out.println(output);
    assertTrue(output.contains("Game quit!"));
  }

  @Test
  public void testQuitInMiddle() {
    String input = "mpf 1 q";
    StringReader rd = new StringReader(input);
    StringBuilder ap = new StringBuilder();
    setUp(rd, ap);
    kc.playGame(model, testDeck(), false, 4, 3);
    String output = ap.toString();
    System.out.println(output);
    assertTrue(output.contains("Game quit!"));
  }

  @Test
  public void testCaseSensitivity(){
    String input = "Q";
    StringReader rd = new StringReader(input);
    StringBuilder ap = new StringBuilder();
    setUp(rd, ap);
    kc.playGame(model, testDeck(), false, 4, 3);
    String output = ap.toString();
    System.out.println(output);
    assertTrue(output.contains("Game quit!"));
  }

  /**
   * carefully curated deck for testing purposes.
   * @return deck for testing purposes.
   */
  private List<SingleCard> testDeck(){
    List<SingleCard> deck = new ArrayList<>();
    // important part
    deck.add(new SingleCard(1, "hearts"));
    deck.add(new SingleCard(12, "hearts"));
    deck.add(new SingleCard(3, "spades"));
    deck.add(new SingleCard(3, "hearts"));
    deck.add(new SingleCard(13, "spades"));
    deck.add(new SingleCard(2, "spades"));
    deck.add(new SingleCard(2, "hearts"));
    deck.add(new SingleCard(10, "hearts"));
    deck.add(new SingleCard(11,"spades"));
    deck.add(new SingleCard(9, "spades"));
    deck.add(new SingleCard(8, "hearts"));
    deck.add(new SingleCard(1, "spades"));

    // rest of deck
    deck.add(new SingleCard(4, "spades"));
    deck.add(new SingleCard(4, "hearts"));
    deck.add(new SingleCard(5, "spades"));
    deck.add(new SingleCard(5, "hearts"));
    deck.add(new SingleCard(6, "spades"));
    deck.add(new SingleCard(6, "hearts"));
    deck.add(new SingleCard(7, "spades"));
    deck.add(new SingleCard(7, "hearts"));
    deck.add(new SingleCard(8, "spades"));
    deck.add(new SingleCard(9, "hearts"));
    deck.add(new SingleCard(10, "spades"));
    deck.add(new SingleCard(11, "hearts"));
    deck.add(new SingleCard(12, "spades"));
    deck.add(new SingleCard(13, "hearts"));
    return deck;
  }
}
