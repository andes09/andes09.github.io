package questions;

/**
 * This class represents a yes/no question.
 * The possible answers are either yes or no, and it ignores capitalization.
 */
public class YesNoQuestion extends AbstractQuestion {

  /**
   * Creates a question with the given text.
   *
   * @param text the question text itself
   * @throws IllegalArgumentException if the text is empty or does not end with a ?
   */
  public YesNoQuestion(String text, String[] validAnswers) throws IllegalArgumentException {
    super(text, validAnswers);
  }

}
