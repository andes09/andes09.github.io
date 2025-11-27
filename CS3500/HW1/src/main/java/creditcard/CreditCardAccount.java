package creditcard;

/**
 * Outline for how a credit card should work.
 */
public interface CreditCardAccount {
  /**
   * Outputs a positive or negative response based on the completion
   * a credit card operation.
   *
   * @return string of whether task was completed or not.
   */
  String status();

  /**
   * outputs the max amount of money a credit card user can use.
   *
   * @return the maximum amount of money a user can spend on a credit card
   */
  double creditLimit();

  /**
   * Yearly interest rate between 0-100 if payment is not made.
   *
   * @return yearly interest rate
   */
  double apr();

  /**
   * Minimum payment due by user to keep account in good standing.
   *
   * @return minimum payment
   */
  double minimumPayment();

  /**
   * balance that has accumulated  on the credit card.
   *
   * @returns the balance
   */
  double balance();

  /**
   * a non-negative purchase which is added to balance.
   * not applicable if balance exceed credit limit.
   *
   * @return new balance after expense,unless balance>credit limit-> returns original balance.
   */
  BasicCreditCardAccount expense(double amount);

  /**
   * subtracts amount paid off from balance.
   * not applicable if balance drops below 0.
   *
   * @return new lower balance, or the original balance if after pay off balance falls bewlow 0
   */
  BasicCreditCardAccount payoff(double amount);

  /**
   * increased max credit limit.
   *
   * @return new larger credit limit
   */
  BasicCreditCardAccount increaseLimit();

  /**
   * processes the current account cycle then returns the account itself.
   *
   * @return the acount status?
   */
  BasicCreditCardAccount processCycle();
}
