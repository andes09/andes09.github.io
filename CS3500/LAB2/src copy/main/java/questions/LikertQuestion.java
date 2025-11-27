package questions;

/**
 * This class represents a 5-scale likert question.
 * The scales are strongly agree, agree, neither agree nor disagree, disagree and strongly disagree.
 */
public class LikertQuestion extends AbstractQuestion {

  /**
   * Creates a valid question with the given text.
   *
   * @param text the question's text
   * @throws IllegalArgumentException if the text is the empty string
   */
  public LikertQuestion(String text, String[] validAnswers) throws IllegalArgumentException {
    super(text, validAnswers);
  }

}
