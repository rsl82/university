package com.example.pr4;

import java.util.ArrayList;

/**
 * This class makes Deluxe Pizza.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class Deluxe extends Pizza{

    /**
     * Constructor with super class.
     * @param toppings Toppings included in the pizza.
     * @param crust Type of the crust for the pizza.
     * @param size Size of the pizza.
     */
    public Deluxe(ArrayList<Topping> toppings, Crust crust, Size size) {
        super(toppings, crust, size);
    }

    /**
     * Overriding add method, but it is not used for Deluxe Pizza.
     * @param obj Topping to add.
     * @return It is not used, so it will just return false.
     */
    @Override
    public boolean add(Object obj) {
        return false;
    }

    /**
     * Overriding remove method, but it is not used for Deluxe Pizza.
     * @param obj Topping to remove.
     * @return It is not used, so it will just return false.
     */
    @Override
    public boolean remove(Object obj) {
        return false;
    }

    /**
     * Overriding price method for Deluxe Pizza.
     * @return It returns the corresponding price for the pizza.
     */
    @Override
    public double price() {
        if(this.sizeGetter() == Size.SMALL) {
            return 14.99;
        }
        else if(this.sizeGetter() == Size.MEDIUM) {
            return 16.99;
        }
        else if(this.sizeGetter() == Size.LARGE) {
            return 18.99;
        }
        return -1;
    }

    /**
     * Overriding toString method.
     * @return Returns the pizza with String format.
     */
    @Override
    public String toString() {
        return "Deluxe " + super.toString();
    }
}
