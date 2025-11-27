package questions;

abstract class AbstractQuestion implements Question {
  protected String questionText;
  protected String enteredAnswer;
  protected final String[] validAnswers;


  protected AbstractQuestion(String text, String[] validAnswers) {
    if ((text.length() == 0) || (text.charAt(text.length() - 1) != '?')) {
      throw new IllegalArgumentException("Invalid question text");
    }
    this.questionText = text;
    this.enteredAnswer = "";
    this.validAnswers = validAnswers;
  }

  public void answer(String enteredAnswer) {
    boolean present = false;
    for(String validAnswer : validAnswers) {
      if (enteredAnswer.equals(validAnswer)) {
        this.enteredAnswer = enteredAnswer;
        return;
      }
    }
    throw new IllegalArgumentException("Invalid answer: " + enteredAnswer);
  }

  public String getQuestionText() {
    return questionText;
  }

  public boolean hasBeenAnswered(){
    for(String validAnswer : validAnswers) {
      if (enteredAnswer.equals(validAnswer)) {
        return true;
      }
    }
    return false;
  }
  public String getEnteredAnswer() {
    if (!hasBeenAnswered()) {
      throw new IllegalStateException("solution.Question not attempted yet!");
    } else {
      return enteredAnswer;
    }
  }

}
