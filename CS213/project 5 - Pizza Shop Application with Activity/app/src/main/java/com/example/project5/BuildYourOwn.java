package com.example.project5;

import java.util.ArrayList;

/**
 * This class makes Build Your Own Pizza.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class BuildYourOwn extends Pizza{

    /**
     * Constructor with super class.
     * @param toppings Toppings included in the pizza.
     * @param crust Type of the crust for the pizza.
     * @param size Size of the pizza.
     */
    public BuildYourOwn(ArrayList<Topping> toppings, Crust crust, Size size) {
        super(toppings, crust, size);
    }


    /**
     * Overriding method that adds the topping for the BYO pizza.
     * @param obj Topping to add.
     * @return If it is added, return true. If it failed, return false.
     */
    @Override
    public boolean add(Object obj) {
        if(obj instanceof Topping) {
            super.addTopping((Topping) obj);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Overriding method that removes the topping for the BYO pizza.
     * @param obj Topping to remove.
     * @return If it is removed, return true. If it failed, return false.
     */
    @Override
    public boolean remove(Object obj) {
        if(obj instanceof Topping) {
            super.removeTopping((Topping) obj);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Overriding price method for BYO Pizza.
     * @return It returns the corresponding price for the pizza.
     */
    @Override
    public double price() {
        if(this.sizeGetter() == Size.SMALL) {
            return 8.99 + toppingCount()*1.59;
        }
        else if(this.sizeGetter() == Size.MEDIUM) {
            return 10.99 + toppingCount()*1.59;
        }
        else if(this.sizeGetter() == Size.LARGE) {
            return 12.99 + toppingCount()*1.59;
        }
        return -1;
    }

    /**
     * Overriding toString method.
     * @return Returns the pizza with String format.
     */
    @Override
    public String toString() {
        return "Build Your Own " + super.toString();
    }
}
