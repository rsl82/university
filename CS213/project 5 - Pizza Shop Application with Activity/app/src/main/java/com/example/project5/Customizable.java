package com.example.project5;

/**
 * Interface that has add and remove methods.
 * It will be used in pizza classes, Order class, and StoreOrders class.
 * @author Ryan S. Lee, Songyuan Zhang
 */
public interface Customizable {

    /**
     * Add the object.
     * @param obj object to be added.
     * @return true if it is added successfully, false if it fails.
     */
    boolean add(Object obj);

    /**
     * Remove the object.
     * @param obj object to be removed.
     * @return true if it is removed successfully, false if it fails.
     */
    boolean remove(Object obj);
}
