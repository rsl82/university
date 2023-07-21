package com.example.project5;

import java.util.ArrayList;

/**
 * Class that stores the orders in the store.
 * @author Ryan S. Lee, Songyuan Zhang
 */
public class StoreOrders implements Customizable{

    private ArrayList<Order> storeOrdersList;

    /**
     * Constructor that initialize StoreOrders object.
     */
    public StoreOrders() {
        this.storeOrdersList = new ArrayList<>();
    }

    /**
     * Add the order to the storeOrdersList.
     * @param obj Order to be added.
     * @return true if it is added, false if it is failed.
     */
    @Override
    public boolean add(Object obj) {
        if(obj instanceof Order) {
            this.storeOrdersList.add((Order) obj);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Remove the order to the storeOrdersList.
     * @param obj Order to be removed.
     * @return true if it is removed, false if it is failed.
     */
    @Override
    public boolean remove(Object obj) {
        if (obj instanceof Order) {
            this.storeOrdersList.remove((Order) obj);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Get the List of the orders numbers in the storeOrderList.
     * @return ArrayList of the order numbers in the storeOrderList.
     */
    public ArrayList<Integer> getOrderIdList() {
        ArrayList<Integer> orderIdList= new ArrayList<>();
        for(Order a:this.storeOrdersList) {
            orderIdList.add(a.getOrderId());
        }
        return orderIdList;
    }

    /**
     * Find the order by index.
     * @param id index to find the order.
     * @return Order with the corresponding index, null if not exists.
     */
    public Order getOrder(int id) {
        for(Order a:this.storeOrdersList) {
            if(a.getOrderId() == id) {
                return a;
            }
        }

        return null;
    }

}
