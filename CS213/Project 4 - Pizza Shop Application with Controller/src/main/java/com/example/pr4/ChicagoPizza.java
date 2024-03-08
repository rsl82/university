package com.example.pr4;

import javafx.collections.ObservableList;
import java.util.ArrayList;

/**
 * Class that makes Chicago pizza with corresponding flavor.
 * Chicago pizza will be made only in this class.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class ChicagoPizza implements PizzaFactory {

    /**
     * Overriding method that makes new Deluxe flavored pizza.
     * @param size Size of the pizza.
     * @param toppings Toppings of the pizza.
     * @return new deluxe pizza object.
     */
    @Override
    public Pizza createDeluxe(Size size, ObservableList<Topping> toppings) {
        ArrayList<Topping> toppingList = new ArrayList<>();
        toppingList.addAll(toppings);

        return new Deluxe(toppingList,Crust.DEEP_DISH,size);

    }

    /**
     * Overriding method that makes new Meatzza flavored pizza.
     * @param size Size of the pizza.
     * @param toppings Toppings of the pizza.
     * @return new meatzza pizza object.
     */
    @Override
    public Pizza createMeatzza(Size size,ObservableList<Topping> toppings) {
        ArrayList<Topping> toppingList = new ArrayList<>();
        toppingList.addAll(toppings);

        return new Meatzza(toppingList,Crust.STUFFED,size);
    }

    /**
     * Overriding method that makes new BBQ Chicken flavored pizza.
     * @param size Size of the pizza.
     * @param toppings Toppings of the pizza.
     * @return new BBQ chicken pizza object.
     */
    @Override
    public Pizza createBBQChicken(Size size,ObservableList<Topping> toppings) {
        ArrayList<Topping> toppingList = new ArrayList<>();
        toppingList.addAll(toppings);

        return new BBQChicken(toppingList,Crust.PAN,size);
    }

    /**
     * Overriding method that makes new BYO flavored pizza.
     * @param size Size of the pizza.
     * @return new BYO pizza object.
     */
    @Override
    public Pizza createBuildYourOwn(Size size) {
        ArrayList<Topping> toppingList = new ArrayList<>();
        return new BuildYourOwn(toppingList,Crust.PAN,size);
    }
}
