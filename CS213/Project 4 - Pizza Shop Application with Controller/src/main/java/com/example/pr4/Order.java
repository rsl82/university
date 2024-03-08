package com.example.pr4;

import java.util.ArrayList;

/**
 * This class contains the one order for the pizzas.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class Order implements Customizable{

    private ArrayList<Pizza> orderList;
    private int orderId;

    /**
     * Constructor for the Order class.
     */
    Order(int orderId) {
        this.orderList = new ArrayList<>();
        this.orderId = orderId;
    }

    /**
     * Get the order list from the order object to use in StoreOrders.
     * @return ArrayList of the pizzas.
     */
    public ArrayList<Pizza> orderGetter() {
        return this.orderList;
    }

    /**
     * Add the pizza to the order
     * @param obj object to be added.
     * @return true if it is added, false if it is failed.
     */
    @Override
    public boolean add(Object obj) {
        if(obj instanceof Pizza) {
            orderList.add((Pizza) obj);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Get subtotal for the order.
     * @return subtotal cost for the order.
     */
    public double getPizzaCost() {
        double total = 0;
        for(Pizza i: orderList) {
            total = i.price() + total;
        }
        return total;
    }

    /**
     * Get the tax from the order.
     * @return the tax from the order.
     */
    public double getPizzaTax() {
        double total = getPizzaCost();
        return total * 0.06625;
    }

    /**
     * Get the total cost of the order.
     * @return total cost of the order.
     */
    public double getTotal() {
        return getPizzaTax() + getPizzaCost();

    }

    /**
     * Remove the pizza from the order.
     * @param obj object to be removed.
     * @return true if it is removed, false if it is failed.
     */
    @Override
    public boolean remove(Object obj) {
        if (obj instanceof Pizza) {
            orderList.remove((Pizza) obj);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get specific pizza from the order.
     * @param index index of the pizza in the orderList.
     * @return found pizza from the orderList.
     */
    public Pizza getPizza(int index) {
        return this.orderList.get(index);
    }

    /**
     * Get the list of the pizza from the order.
     * @return orderList from the object.
     */
    public ArrayList<Pizza> orderSetter() {
        return this.orderList;
    }

    /**
     * Clear the current order.
     */
    public void clearOrder() {
        orderList.clear();
    }

    /**
     * Gets the order ID for the current order.
     * @return orderId variable for the object.
     */
    public int getOrderId() {
        return this.orderId;
    }
}
