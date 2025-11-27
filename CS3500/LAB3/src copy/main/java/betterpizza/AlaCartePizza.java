package betterpizza;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import pizza.Crust;
import pizza.Pizza;
import pizza.Size;
import pizza.ToppingName;
import pizza.ToppingPortion;

/**
 * This class represents an ala carte pizza (i.e. a pizza that can
 * have an arbitrary number of ingredients.
 */
public class AlaCartePizza implements ObservablePizza {
  private Crust crust;
  private Size size;
  private Map<ToppingName, ToppingPortion> toppings;

  /**
   * Create a pizza given its crust type, size and toppings.
   */
  private AlaCartePizza(AlaCartePizzaBuilder builder) throws IllegalArgumentException {

    this.crust = builder.crust;
    this.size = builder.size;
    this.toppings = builder.toppings;
    if (this.crust == null || this.size == null || this.toppings == null) {
      throw new IllegalArgumentException("Empty Requirements");
    }
  }

  @Override
  public ToppingPortion hasTopping(ToppingName name) {
    return this.toppings.getOrDefault(name, null);
  }

  @Override
  public double cost() {
    double cost = 0.0;
    for (Map.Entry<ToppingName, ToppingPortion> item : this.toppings.entrySet()) {
      cost += item.getKey().getCost() * item.getValue().getCostMultiplier();
    }
    return cost + this.size.getBaseCost();
  }


  public static class AlaCartePizzaBuilder extends PizzaBuilder {

    private  Size size;
    private  Crust crust;
    private  Map<ToppingName, ToppingPortion> toppings ;

    public AlaCartePizzaBuilder crust(Crust crust) {
      this.crust = crust;
      return this;
    }
    public AlaCartePizzaBuilder size(Size size) {
      this.size = size;
      return this;
    }
    public AlaCartePizzaBuilder addTopping(ToppingName name, ToppingPortion portion) {
      this.toppings.put(name, portion);
      return this;
    }
    public AlaCartePizza build() throws IllegalArgumentException {
      return new AlaCartePizza(this);
    }

  }
}


