package creditcard;

/**
 * This is the Basic Credit Card Account class.
 * Allows users to simulate a credit card by adding and paying off expenses.
 * Credit card will also be processed very cycle.
 * Users are required to pay a minimum payment each cycle.
 * Otherwise, they will face interest and late fees.
 */
public class BasicCreditCardAccount implements CreditCardAccount {
  private double creditLimit;
  private double balance;
  private final int lateFee;
  private final double apr;
  private boolean paidPreviousCycle;
  private boolean paidCurrentCycle;
  private double previousBalance;
  private double numPaidProcessedCycles;
  private BasicCreditCardAccount account;
  private String state;
  private double minPayment;
  private boolean initCycle;


  /**
   * Sets up basic credit card account with late fee, APR, credit Limit.
   *
   * @param creditLimit max spending amount - must be above 0
   * @param apr         Annual Percentage Rate - must be between 0 and 100
   * @param lateFee     additional fee if payments is late - must be between 0 and late fee * APR
   */
  public BasicCreditCardAccount(double creditLimit, double apr, int lateFee) {
    if (creditLimit <= 0) {
      throw new IllegalArgumentException("Credit limit must be greater than 0");
    } else {
      this.creditLimit = creditLimit;
    }
    if (apr <= 0 || apr > 100) {
      throw new IllegalArgumentException("apr must be between 0 and 100");
    } else {
      this.apr = apr;
    }
    if (lateFee <= 0 || lateFee > creditLimit * (apr / 100)) {
      throw new IllegalArgumentException("LateFee must be between 0 and 100");
    } else {
      this.lateFee = lateFee;
    }
    balance = 0;
    paidPreviousCycle = true;
    paidCurrentCycle = true;
    previousBalance = 0;
    numPaidProcessedCycles = 0;
    state = "IN LIM";
    account = this;
    minPayment = 0;
    initCycle = true;
  }

  /**
   * Displays the state of the account based on last action taken.
   * EXCEEDED LIM - recent expense has surpassed credit limit.
   * IN LIM - Recent expense or payoff maintains balance between 0 and credit limit.
   * NEG AMT - number entered was negative and invalid.
   * BEL 0 - Recent payoff will cause balance to go below 0.
   * LIM+ - credit limit increased.
   * FAIl LIM+ - failed  to increase credit limit.
   *
   * @return the state of account
   */
  @Override
  public String status() {
    return switch (state) {
      case "ERR EXCEEDED LIM" -> "ERR: cannot expense beyond limit";
      case "EXCEEDED LIM" -> "OK: balance exceeds limit";
      case "IN LIM" -> "OK: balance within limit";
      case "NEG AMT" -> "ERR: cannot input negative money";
      case "BEL 0" -> "ERR: cannot decrease below 0";
      case "LIM+" -> "OK: limit increased";
      case "FAIL LIM+" -> "ERR: limit cannot increase";
      default -> "";
    };
  }

  /**
   * returns the credit limit.
   *
   * @return credit limit
   */
  @Override
  public double creditLimit() {
    return creditLimit;
  }

  /**
   * returns APR as a percentage.
   *
   * @return apr/100
   */
  @Override
  public double apr() {
    return apr;
  }

  /**
   * calculates the minimum payment due.
   * if balance < 50 minimum payment is the balance.
   * if balance > 50 and previous cycle is paid then minimum payment is 2% of the balance.
   * else interest and late fee will be added to 2% of balance.
   *
   * @return minimum payment
   */
  @Override
  public double minimumPayment() {
    if (initCycle) {
      return 0;
    }
    if (balance <= 50) {
      return balance;
    } else if (balance > 50 && paidPreviousCycle) {
      return balance * .02;
    } else {
      return (balance * .02) + (previousBalance * (apr() / 100)) + lateFee;
    }
  }

  /**
   * The balance of the account.
   *
   * @return account balance
   */
  @Override
  public double balance() {
    return balance;
  }

  /**
   * returns account with updated balance after new expense (if applicable).
   *
   * @param amount number to be added to the balance
   * @return account with new balance
   */
  @Override
  public BasicCreditCardAccount expense(double amount) {
    if (amount < 0) {
      state = "NEG AMT";
      System.out.println(status());
      return account;
    }
    if (balance + amount > creditLimit) {
      state = "ERR EXCEEDED LIM";
      System.out.println(status());
      return account;
    }
    state = "IN LIM";
    this.balance += amount;
    System.out.println(status());
    return account;
  }

  /**
   * takes in a non-negative number, that is also no greater than the current balance.
   * subtracts amount from the current balance.
   * also checks if amount is greater than the minimum payment required.
   *
   * @param amount - amount to be removed from balance
   * @return the account itself
   */
  @Override
  public BasicCreditCardAccount payoff(double amount) {
    if (amount < 0) {
      state = "NEG AMT";
      System.out.println(status());
      return account;
    }
    if (balance - amount < 0) {
      state = "BEL 0";
      System.out.println(status());
      return account;
    }
    if (amount >= minimumPayment()) {
      paidCurrentCycle = true;
    }
    balance -= amount;
    state = "IN LIM";
    System.out.println(status());
    return account;
  }

  /**
   * increased the credit limit by 500 dollars.
   *
   * @return the account itself
   */
  @Override
  public BasicCreditCardAccount increaseLimit() {
    if (balance < (.5 * creditLimit) && numPaidProcessedCycles == 3) {
      state = "LIM+";
      System.out.println(status());
      numPaidProcessedCycles = 0;
      creditLimit += 500;
    } else {
      state = "FAIL LIM+";
      System.out.println(status());
    }

    return account;
  }

  /**
   * processes the cerdit card cycle.
   * adjusts balance.
   * checks if the current cycle has been paid and how many cycles have been paid and.
   * adjusts minimum payment and credit limit accordingly.
   *
   * @return account itself
   */
  @Override
  public BasicCreditCardAccount processCycle() {
    initCycle = false;
    previousBalance = balance;
    balance *= (1 + (apr() / 100));
    if (paidCurrentCycle || balance == 0) {
      previousBalance = 0;
      numPaidProcessedCycles++;
      paidPreviousCycle = true;
    } else {
      paidPreviousCycle = false;
      numPaidProcessedCycles = 0;
    }
    paidCurrentCycle = false;
    minPayment = minimumPayment();

    if (balance > creditLimit) {
      state = "EXCEEDED LIM";
    } else {
      state = "IN LIM";
    }
    return account;
  }
}