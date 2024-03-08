package com.example.pr4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is the controller for MainView.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class MainViewController {

    private Order currentOrder;
    private StoreOrders storeOrders;
    private int currentOrderId;

    /**
     * Initializes the private instances for the class.
     */
    @FXML
    public void initialize() {
        this.currentOrderId = 0;
        this.currentOrder = new Order(currentOrderId);
        this.storeOrders = new StoreOrders();
    }

    /**
     * It removes the pizza from the currentOrder.
     * @param index index of the pizza to be deleted.
     */
    public void removePizzaFromCurrentOrder(int index) {
        currentOrder.remove(currentOrder.getPizza(index));
    }

    /**
     * Clear the currentOrder.
     */
    public void clearCurrentOrder() {
        currentOrder.clearOrder();
    }


    /**
     * Get the cost of the current order.
     * @return the double array which contains subtotal cost, tax of the order, and total cost.
     */
    public double[] orderCost() {
        double[] cost= {currentOrder.getPizzaCost(),currentOrder.getPizzaTax(),currentOrder.getTotal()};
        return cost;
    }

    /**
     * get the list of the currentOrder to use in CurrentOrderController.
     * @return currentOrder.
     */
    public ArrayList<Pizza> orderSetter() {
        return currentOrder.orderSetter();
    }

    /**
     * return the number of the storeOrders with the array form.
     * @return the Arraylist of the integer with the number of store orders filled.
     */
    public ArrayList<Integer> getOrderIdList() {
        return this.storeOrders.getOrderIdList();
    }

    /**
     * get the specific order from the storeOrders.
     * @param id order number to get.
     * @return Order object that has order number with id.
     */
    public Order getOrder(int id) {
        return this.storeOrders.getOrder(id);
    }

    /**
     * Add the currentOrder to the storeOrders.
     */
    public void placeOrder() {
        this.storeOrders.add(this.currentOrder);
        this.currentOrderId = this.currentOrderId +1;
        this.currentOrder = new Order(currentOrderId);
    }

    /**
     * remove the order from the storeOrders.
     * @param id index of the storeOrders to be removed.
     */
    public void cancelOrder(int id) {
        storeOrders.remove(storeOrders.getOrder(id));
    }

    /**
     * Pass the storeOrders to StoreOrders class.
     * @return Arraylist of the storeOrders.
     */
    public ArrayList<Order> getStoreOrderList() {
        return this.storeOrders.getStoreOrderList();
    }

    /**
     * Opens the store order window when the button is clicked.
     * @param actionEvent It recognizes if the store order button is clicked.
     * @throws IOException Required for the new window.
     */
    @FXML
    public void openStoreOrder (ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StoreOrdersView.fxml"));
        Parent root = fxmlLoader.load();
        StoreOrdersController storeOrdersController = fxmlLoader.getController();
        storeOrdersController.setMainController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Store Order");

        stage.show();
    }

    /**
     * Opens the Chicago style pizza window when the button is clicked.
     * @param actionEvent It recognizes if the Chicago style pizza button is clicked.
     * @throws IOException Required for the new window.
     */
    @FXML
    public void openChicagoPizza(ActionEvent actionEvent) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChicagoStylePizzaView.fxml"));
        Parent root = fxmlLoader.load();
        ChicagoStylePizzaController chicagoStylePizzaController = fxmlLoader.getController();
        chicagoStylePizzaController.setMainController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Chicago Pizza Order");

        stage.show();
    }

    /**
     * Add the pizza to the current order.
     * @param pizza pizza object to be added to the current order.
     */
    public void addPizzaToCurrentOrder(Pizza pizza) {
        currentOrder.add(pizza);
    }

    /**
     * Opens the current order window when the button is clicked.
     * @param actionEvent It recognizes if the current order button is clicked.
     * @throws IOException Required for the new window.
     */
    @FXML
    public void openCurrentOrder(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CurrentOrderView.fxml"));
        Parent root = fxmlLoader.load();
        CurrentOrderController currentOrderController = fxmlLoader.getController();
        currentOrderController.setMainController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Current Order");
        stage.show();
    }

    /**
     * Opens the New York style pizza window when the button is clicked.
     * @param actionEvent It recognizes if the New York style pizza button is clicked.
     * @throws IOException Required for the new window.
     */
    @FXML
    public void openNYPizza(ActionEvent actionEvent) throws  IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewYorkStylePizzaView.fxml"));
        Parent root = fxmlLoader.load();
        NewYorkStylePizzaController newYorkStylePizzaController = fxmlLoader.getController();
        newYorkStylePizzaController.setMainController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("New York Pizza Order");

        stage.show();
    }

    /**
     * gets the current serial ID for the order.
     * @return currentOrderId
     */
    public int getCurrentOrderId() {
        return this.currentOrderId;
    }

    /**
     * method to invoke exporting method in the StoreOrders class.
     */
    public void exportOrders() {
        this.storeOrders.saveStoreOrdersToFile();
    }
}
