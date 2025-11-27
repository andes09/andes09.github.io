import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;
import org.junit.Test;
import questions.Question;
import questions.YesNoQuestion;


/**
 * Tests for the YesNoQuestion specifically.
 */
public class YesNoQuestionTest {
  private String longRandom;
  private final String[] validAnswers;

  /**
   * Initializes the test with a random string to create question text.
   */
  public YesNoQuestionTest() {
    longRandom = "aosdifjaso oifhas;ldihv;al skdfha;osidghv;osiadhvbasdjkhvn";
    validAnswers = new String[] {"yes", "no" };
  }

  @Test
  public void testCreateValidYesNoQuestion() {
    Random r = new Random(200);
    for (int i = 0; i < 1000; i++) {
      int start = r.nextInt(longRandom.length() - 1);
      int end = start + r.nextInt(longRandom.length() - start - 1) + 1;
      String questionText = longRandom.substring(start, end);
      Question q = new YesNoQuestion(questionText + "?",validAnswers);
      assertEquals(questionText + "?", q.getQuestionText());
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateYesNoQuestionNoText() {
    new YesNoQuestion("",validAnswers);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateYesNoQuestionNoQuestionMark() {
    new YesNoQuestion("Is this fun", validAnswers);
  }

  @Test
  public void testAnswerCorrectly() {
    String[] answers = {"yes", "Yes",
        "YEs", "YeS", "YES", "yEs", "yES", "yeS", "no", "No", "nO", "NO"};
    for (String answer : answers) {
      Question q = new YesNoQuestion("Is this a trick question?", validAnswers);
      assertFalse(q.hasBeenAnswered());

      q.answer(answer);
      assertEquals(answer.toLowerCase(), q.getEnteredAnswer());
      assertTrue(q.hasBeenAnswered());
    }
  }

  @Test
  public void testAnswerInCorrectly() {
    String[] answers = {"yess", ""};
    for (String answer : answers) {
      Question q = new YesNoQuestion("Is this a trick question?",answers);
      assertFalse(q.hasBeenAnswered());

      try {
        q.answer(answer);
        fail("Yes No question accepted an invalid answer");
      } catch (IllegalArgumentException e) {
        assertFalse(q.hasBeenAnswered());
      }
    }
  }


}