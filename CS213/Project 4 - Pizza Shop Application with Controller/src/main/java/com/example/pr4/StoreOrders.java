package com.example.pr4;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
     * Get the List of the orders in the storeOrderList.
     * @return ArrayList of the orders in the storeOrderList.
     */
    public ArrayList<Order> getStoreOrderList() {
        return this.storeOrdersList;
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

    /**
     *
     * Save the orders with the string format in the file.
     *  The system shall be able to export the store orders and save them in a text file,
     * which includes a list of store orders. Each store order shall include the order
     *  number, the list of pizzas ordered, and the order total.
     */
    public void saveStoreOrdersToFile() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Target File for the Export");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File targetFile = chooser.showSaveDialog(stage); //get the reference of the target file
        //write code to write to the file.
        try {
            PrintWriter writer;
            writer = new PrintWriter(targetFile);
            for (int i = 0; i < this.storeOrdersList.size(); i ++) {
                Order order = this.storeOrdersList.get(i);
                writer.println("Order " + order.getOrderId() + ": ");
                ArrayList<Pizza> pizzaList = order.orderSetter();
                for (int j = 0; j < pizzaList.size(); j ++) {
                    Pizza pizza = pizzaList.get(j);
                    writer.println(pizza);
                }
                writer.println("Total order amount: $" + String.format("%.2f",order.getTotal()));
                writer.println();
            }
            writer.close();
        } catch (IOException ex) {
        }
    }
}
