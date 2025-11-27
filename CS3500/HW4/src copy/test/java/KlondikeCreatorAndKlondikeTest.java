import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import klondike.model.hw02.BasicKlondike;
import klondike.model.hw02.KlondikeModel;
import klondike.model.hw02.SingleCard;
import klondike.model.hw04.KlondikeCreator;
import klondike.model.hw04.WhiteheadKlondike;
import org.junit.Test;

/**
 * Tests for KlondikeCreator factory class.
 */
public class KlondikeCreatorAndKlondikeTest {

  @Test
  public void testCreateBasicGame() {
    KlondikeModel<SingleCard> model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    assertNotNull(model);
    assertTrue(model instanceof BasicKlondike);
  }

  @Test
  public void testCreateWhiteheadGame() {
    KlondikeModel<SingleCard> model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    assertNotNull(model);
    assertTrue(model instanceof WhiteheadKlondike);
  }

}
