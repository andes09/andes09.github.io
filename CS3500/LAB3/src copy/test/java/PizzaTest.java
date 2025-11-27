import static org.junit.Assert.assertEquals;

import betterpizza.ObservablePizza;
import org.junit.Before;
import org.junit.Test;
import betterpizza.AlaCartePizza;
import pizza.CheesePizza;
import pizza.Crust;
import pizza.Pizza;
import pizza.Size;
import pizza.ToppingName;
import pizza.ToppingPortion;


/**
 * The test class for pizzas.
 */
public class PizzaTest {
  private Pizza alacarte;
  private Pizza cheese;
  private Pizza halfCheese;

  /**
   * Create the types of pizzas for the tests.
   */
  @Before
  public void setup() {
    ObservablePizza alacarte = new AlaCartePizza.AlaCartePizzaBuilder()
        .crust(Crust.Classic)
        .size(Size.Medium)
        .addTopping(ToppingName.Cheese, ToppingPortion.Full)
        .addTopping(ToppingName.Sauce, ToppingPortion.Full)
        .addTopping(ToppingName.GreenPepper, ToppingPortion.Full)
        .addTopping(ToppingName.Onion, ToppingPortion.Full)
        .addTopping(ToppingName.Jalapeno, ToppingPortion.LeftHalf)
        .build();

    cheese = new CheesePizza(Size.Large, Crust.Thin);

    halfCheese = new CheesePizza(Size.Large, Crust.Thin);
    //put cheese only on left half
    halfCheese.addTopping(ToppingName.Cheese, ToppingPortion.LeftHalf);
  }

  @Test
  public void testCost() {
    assertEquals(8.25, alacarte.cost(), 0.01);
//    assertEquals(9, cheese.cost(), 0.01);
//    assertEquals(8.5, halfCheese.cost(), 0.01);

  }



}