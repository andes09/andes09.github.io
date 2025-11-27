package questions;

/**
 * This interface represents a single question in an online quiz.
 * Each instance of a question is assumed to be part of a quiz assigned to a particular student.
 * In other words, one Question object per student.
 */
public interface Question {

  /**
   * Get the text of this question.
   *
   * @return the text of this question as a string
   */
  String getQuestionText();

  /**
   * Enter an answer to this question. Specific implementations may enforce constraints
   * on what a valid answer can be.
   *
   * @param enteredAnswer the answer entered for this question by a student
   */
  void answer(String enteredAnswer);

  /**
   * Returns whether this question has been answered by the student.
   *
   * @return true if the question has been answered, false otherwise
   */
  boolean hasBeenAnswered();

  /**
   * Return the answer entered by the student, if it has been entered.
   *
   * @return the answer entered by the student
   * @throws IllegalStateException if the question has not been answered yet
   */
  String getEnteredAnswer() throws IllegalStateException;
}
