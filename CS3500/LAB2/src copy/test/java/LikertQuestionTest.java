import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;
import org.junit.Test;
import questions.LikertQuestion;
import questions.Question;

/**
 * Tests for the LikertQuestion specifically.
 */
public class LikertQuestionTest {
  private String longRandom;
  private final String[] validAnswers;

  /**
   * Initializes the test with a random string to create question text.
   */
  public LikertQuestionTest() {
    longRandom = "aosdifjaso oifhas;ldihv;al skdfha;osidghv;osiadhvbasdjkhvn";
    validAnswers = new String[] {"strongly agree",
        "agree",
        "neither agree nor disagree",
        "disagree",
        "strongly disagree"};
  }

  @Test
  public void testCreateValidLikertQuestion() {
    Random r = new Random(200);
    for (int i = 0; i < 1000; i++) {
      int start = r.nextInt(longRandom.length() - 1);
      int end = start + r.nextInt(longRandom.length() - start - 1) + 1;
      String questionText = longRandom.substring(start, end);
      Question q = new LikertQuestion(questionText + "?", validAnswers);
      assertEquals(questionText + "?", q.getQuestionText());
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateLikertQuestionNoText() {
    new LikertQuestion("", validAnswers);
  }


  @Test
  public void testAnswerCorrectly() {
    String[] answers = {"strongly agree",
        "agree",
        "neither agree nor disagree",
        "disagree",
        "strongly disagree"};
    for (String answer : answers) {
      Question q = new LikertQuestion("Is this a trick question?", answers);
      assertFalse(q.hasBeenAnswered());

      q.answer(answer);
      assertEquals(answer.toLowerCase(), q.getEnteredAnswer());
      assertTrue(q.hasBeenAnswered());
    }
  }

  @Test
  public void testAnswerInCorrectly() {
    String[] answers = {"weakly disagree", ""};
    for (String answer : answers) {
      Question q = new LikertQuestion("Is this a trick question?", answers);
      assertFalse(q.hasBeenAnswered());

      try {
        q.answer(answer);
        fail("Likert question accepted an invalid answer");
      } catch (IllegalArgumentException e) {
        assertFalse(q.hasBeenAnswered());
      }
    }
  }


}