package com.example.pr4;

import javafx.collections.ObservableList;

/**
 * Interface of the pizza factory that is used in ChicagoPizza and NYPizza.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public interface PizzaFactory {

    /**
     * Method that makes new Deluxe flavored pizza.
     * @param size Size of the pizza.
     * @param toppings Toppings of the pizza.
     * @return new deluxe pizza object.
     */
    Pizza createDeluxe(Size size, ObservableList<Topping> toppings);

    /**
     * Method that makes new Meatzza flavored pizza.
     * @param size Size of the pizza.
     * @param toppings Toppings of the pizza.
     * @return new deluxe pizza object.
     */
    Pizza createMeatzza(Size size, ObservableList<Topping> toppings);

    /**
     * Method that makes new BBQ flavored pizza.
     * @param size Size of the pizza.
     * @param toppings Toppings of the pizza.
     * @return new deluxe pizza object.
     */
    Pizza createBBQChicken(Size size, ObservableList<Topping> toppings);

    /**
     * Mtehod that makes new BYO flavored pizza.
     * @param size Size of the pizza.
     * @return new deluxe pizza object.
     */
    Pizza createBuildYourOwn(Size size);
}
