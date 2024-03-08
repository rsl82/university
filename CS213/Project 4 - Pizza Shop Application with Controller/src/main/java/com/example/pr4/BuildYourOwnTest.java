package com.example.pr4;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;

/**
 * JUnit test class for the BYO pizza.
 * @author Ryan S. Lee, Songyuan Zhang
 */
public class BuildYourOwnTest {
    Pizza currentPizza;
    PizzaFactory chicagoPizzaFactory = new ChicagoPizza();
    PizzaFactory nyPizzaFactory = new NYPizza();
    ArrayList<Topping> currentToppings = new ArrayList<>();

    /**
     * Method to add the topping to the BYO pizza.
     */
    public void addToppings() {
        if (currentToppings.size() == 0) return;
        for (int i = 0; i < currentToppings.size(); i ++) {
            currentPizza.add(currentToppings.get(i));
        }
    }

    /**
     * Test the small chicago pizza without any toppings.
     */
    @Test
    public void test_small_chicago_pizza_with_no_topping() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.SMALL);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 8.99;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the small chicago pizza with one topping.
     */
    @Test
    public void test_small_chicago_pizza_with_one_topping() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.SMALL);
        currentToppings.add(Topping.SAUSAGE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 10.58;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the small chicago pizza with three toppings.
     */
    @Test
    public void test_small_chicago_pizza_with_three_toppings() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.SMALL);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 13.76;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the small chicago pizza with full(seven) toppings.
     */
    @Test
    public void test_small_chicago_pizza_with_seven_toppings() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.SMALL);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        currentToppings.add(Topping.PEPPERONI);
        currentToppings.add(Topping.HAM);
        currentToppings.add(Topping.PROVOLONE);
        currentToppings.add(Topping.PINEAPPLE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 20.12;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the medium chicago pizza without any toppings.
     */
    @Test
    public void test_medium_chicago_pizza_with_no_topping() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.MEDIUM);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 10.99;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the medium chicago pizza with one topping.
     */
    @Test
    public void test_medium_chicago_pizza_with_one_topping() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.MEDIUM);
        currentToppings.add(Topping.SAUSAGE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 12.58;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the medium chicago pizza with three toppings.
     */
    @Test
    public void test_medium_chicago_pizza_with_three_toppings() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.MEDIUM);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 15.76;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the medium chicago pizza with full(seven) toppings.
     */
    @Test
    public void test_medium_chicago_pizza_with_seven_toppings() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.MEDIUM);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        currentToppings.add(Topping.PEPPERONI);
        currentToppings.add(Topping.HAM);
        currentToppings.add(Topping.PROVOLONE);
        currentToppings.add(Topping.PINEAPPLE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 22.12;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the large chicago pizza without any toppings.
     */
    @Test
    public void test_large_chicago_pizza_with_no_topping() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.LARGE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 12.99;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }


    /**
     * Test the large chicago pizza with one topping.
     */
    @Test
    public void test_large_chicago_pizza_with_one_topping() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.LARGE);
        currentToppings.add(Topping.SAUSAGE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 14.58;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the large chicago pizza with three toppings.
     */
    @Test
    public void test_large_chicago_pizza_with_three_toppings() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.LARGE);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 17.76;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the large chicago pizza with full(seven) toppings.
     */
    @Test
    public void test_large_chicago_pizza_with_seven_toppings() {
        currentPizza= chicagoPizzaFactory.createBuildYourOwn(Size.LARGE);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        currentToppings.add(Topping.PEPPERONI);
        currentToppings.add(Topping.HAM);
        currentToppings.add(Topping.PROVOLONE);
        currentToppings.add(Topping.PINEAPPLE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 24.12;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the small new york pizza without any toppings.
     */
    @Test
    public void test_small_ny_pizza_with_no_topping() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.SMALL);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 8.99;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the small new york pizza with one topping.
     */
    @Test
    public void test_small_ny_pizza_with_one_topping() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.SMALL);
        currentToppings.add(Topping.SAUSAGE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 10.58;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the small new york pizza with three toppings.
     */
    @Test
    public void test_small_ny_pizza_with_three_toppings() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.SMALL);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 13.76;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the small new york pizza with full(seven) toppings.
     */
    @Test
    public void test_small_ny_pizza_with_seven_toppings() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.SMALL);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        currentToppings.add(Topping.PEPPERONI);
        currentToppings.add(Topping.HAM);
        currentToppings.add(Topping.PROVOLONE);
        currentToppings.add(Topping.PINEAPPLE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 20.12;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the medium new york pizza without any toppings.
     */
    @Test
    public void test_medium_ny_pizza_with_no_topping() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.MEDIUM);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 10.99;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }


    /**
     * Test the medium new york pizza with one topping.
     */
    @Test
    public void test_medium_ny_pizza_with_one_topping() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.MEDIUM);
        currentToppings.add(Topping.SAUSAGE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 12.58;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the medium new york pizza with three toppings.
     */
    @Test
    public void test_medium_ny_pizza_with_three_toppings() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.MEDIUM);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 15.76;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the medium new york pizza with full(seven) toppings.
     */
    @Test
    public void test_medium_ny_pizza_with_seven_toppings() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.MEDIUM);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        currentToppings.add(Topping.PEPPERONI);
        currentToppings.add(Topping.HAM);
        currentToppings.add(Topping.PROVOLONE);
        currentToppings.add(Topping.PINEAPPLE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 22.12;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the large new york pizza without any toppings.
     */
    @Test
    public void test_large_ny_pizza_with_no_topping() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.LARGE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 12.99;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the large new york pizza with one topping.
     */

    @Test
    public void test_large_ny_pizza_with_one_topping() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.LARGE);
        currentToppings.add(Topping.SAUSAGE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 14.58;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the large new york pizza with three toppings.
     */
    @Test
    public void test_large_ny_pizza_with_three_toppings() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.LARGE);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 17.76;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }

    /**
     * Test the large new york pizza with full(seven) toppings.
     */
    @Test
    public void test_large_ny_pizza_with_seven_toppings() {
        currentPizza= nyPizzaFactory.createBuildYourOwn(Size.LARGE);
        currentToppings.add(Topping.SAUSAGE);
        currentToppings.add(Topping.BBQ_CHICKEN);
        currentToppings.add(Topping.GREEN_PEPPER);
        currentToppings.add(Topping.PEPPERONI);
        currentToppings.add(Topping.HAM);
        currentToppings.add(Topping.PROVOLONE);
        currentToppings.add(Topping.PINEAPPLE);
        this.addToppings();
        double actualOutput = this.currentPizza.price();
        double expectdOutput = 24.12;
        double delta = 0.01;
        assertEquals(actualOutput, expectdOutput, delta);
        currentToppings.clear();
    }
}