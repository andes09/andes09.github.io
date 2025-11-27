import static org.junit.Assert.assertEquals;

import creditcard.BasicCreditCardAccount;
import org.junit.Before;
import org.junit.Test;

/**
 * Tester Class to test BasicCreditCardAccount.
 * Test the methods for all possible cases.
 * There is no test for the status function
 * as each status is tested in other test functions.
 */
public class BasicCreditCardAccountTest {
  private BasicCreditCardAccount bccAcc;

  /**
   * sets up basic credit card with an initial 200 dollar expense.
   */
  @Before
  public void setUp() {
    bccAcc = new BasicCreditCardAccount(1000, 23, 50);
    bccAcc.expense(200);
  }

  @Test
  public void testExpense() {
    bccAcc.expense(100);
    assertEquals(300, bccAcc.balance(), 0.001);
    assertEquals(bccAcc.status(), "OK: balance within limit");

    bccAcc.expense(901);
    assertEquals(300, bccAcc.balance(), 0.001);
    assertEquals(bccAcc.status(), "ERR: cannot expense beyond limit");

    bccAcc.expense(-100);
    assertEquals(300, bccAcc.balance(), 0.001);
    assertEquals(bccAcc.status(), "ERR: cannot input negative money");


  }

  @Test
  public void testPayOff() {
    bccAcc.payoff(75);
    assertEquals(125, bccAcc.balance(), 0.001);
    assertEquals(bccAcc.status(), "OK: balance within limit");

    bccAcc = bccAcc.payoff(800);
    assertEquals(125, bccAcc.balance(), 0.001);
    assertEquals(bccAcc.status(), "ERR: cannot decrease below 0");

    bccAcc = bccAcc.payoff(-100);
    assertEquals(125, bccAcc.balance(), 0.001);
    assertEquals(bccAcc.status(), "ERR: cannot input negative money");
  }

  @Test
  public void testMinimumPayment() {
    assertEquals(0, bccAcc.minimumPayment(), 0.001); // initial minimum payment
    //no late fee
    bccAcc.processCycle();
    assertEquals(.02 * bccAcc.balance(), bccAcc.minimumPayment(), 0.001);
   //with late fee
    bccAcc.processCycle();
    System.out.println(bccAcc.balance());
    assertEquals(((bccAcc.balance() / 1.23)* bccAcc.apr() / 100) + (bccAcc.balance() * 0.02) + 50,
        bccAcc.minimumPayment(), 0.001);

  }

  @Test
  public void testProcessCycleAndIncreaseLimit() {
    bccAcc.processCycle();

    assertEquals(200 * (1 + bccAcc.apr() / 100), bccAcc.balance(), 0.001);

    assertEquals("ERR: limit cannot increase", bccAcc.increaseLimit().status());

    bccAcc.payoff(bccAcc.minimumPayment());
    bccAcc.processCycle();
    bccAcc.payoff(bccAcc.minimumPayment());
    bccAcc.processCycle();

    assertEquals("OK: limit increased", bccAcc.increaseLimit().status());
    assertEquals(1500, bccAcc.creditLimit(), 0.001);

  }
}
