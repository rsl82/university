package com.example.pr4;

import java.util.ArrayList;

/**
 * This is the abstract class for the pizza flavors.
 * @author Ryan S. Lee, Songyuan Zhang
 */
public abstract class Pizza implements Customizable {
    private ArrayList<Topping> toppings;
    private Crust crust;
    private Size size;

    /**
     * Abstract method for the price.
     * @return price with double format.
     */
    public abstract double price();

    /**
     * Constructor of the pizza.
     * @param toppings Toppings included in the pizza.
     * @param crust Type of the crust for the pizza.
     * @param size Size of the pizza.
     */
    public Pizza(ArrayList<Topping> toppings, Crust crust, Size size) {
        this.toppings = toppings;
        this.crust = crust;
        this.size = size;
    }


    /**
     * Get the size of the pizza.
     * @return size of the pizza.
     */
    public Size sizeGetter() {
        return this.size;
    }

    /**
     * Get the crust of the pizza.
     * @return Crust of the pizza.
     */
    public Crust crustGetter() {
        return this.crust;
    }

    /**
     * Add topping to the pizza.
     * @param topping Topping to be added.
     */
    protected void addTopping(Topping topping) {
        this.toppings.add(topping);
    }

    /**
     * Remove the topping from the pizza.
     * @param topping Topping to be removed.
     */
    protected void removeTopping(Topping topping) {
        this.toppings.remove(topping);
    }

    /**
     * return the number of the toppings.
     * @return Number of the toppings in the pizza.
     */
    protected int toppingCount() {
        return this.toppings.size();
    }

    /**
     * toString method that convert Pizza object to the string form.
     * @return String of the information of the pizza object.
     */
    @Override
    public String toString() {
        String temp = "";
        if(this.crust == Crust.PAN || this.crust== Crust.DEEP_DISH ||  this.crust == Crust.STUFFED) {
            temp = "(Chicago Style - " + this.crust + ")";

        }
        else {
            temp = "(New York Style - " + this.crust + ")";
        }
        String toppings = "";
        for(Topping i: this.toppings) {
            toppings = toppings + ", "+i.toString();
        }
        return temp + toppings + ", " + this.size + ", $" + String.format("%,.2f", this.price());
    }
}