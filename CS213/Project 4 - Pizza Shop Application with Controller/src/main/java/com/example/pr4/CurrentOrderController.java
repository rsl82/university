package com.example.pr4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * This class is the controller for the CurrentOrderView.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class CurrentOrderController {

    @FXML
    private TextField orderNumber;
    @FXML
    private TextField subTotal;
    @FXML
    private TextField salesTax;
    @FXML
    private TextField orderTotal;
    @FXML
    private ListView orderList;
    private MainViewController mainViewController;

    /**
     * This method initializes the CurrentOrderView.
     * @param mainViewController the controller to use MainViewController methods in this controller.
     */
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        ObservableList<Pizza> observableList = FXCollections.observableArrayList(mainViewController.orderSetter());
        orderList.setItems(observableList);
        orderNumber.setEditable(false);
        subTotal.setEditable(false);
        salesTax.setEditable(false);
        orderTotal.setEditable(false);
        orderNumber.setText(String.valueOf(mainViewController.getCurrentOrderId()));
        setCosts();
    }

    /**
     * Set the costs for the current order.
     */
    public void setCosts() {
        double[] cost = mainViewController.orderCost();
        subTotal.setText(String.format("%,.2f", cost[0]));
        salesTax.setText(String.format("%,.2f", cost[1]));
        orderTotal.setText(String.format("%,.2f", cost[2]));
    }

    /**
     * Clear the current order.
     * @param actionEvent It recognizes if the user click the clear order button.
     */
    @FXML
    public void clearOrder(ActionEvent actionEvent) {
        mainViewController.clearCurrentOrder();
        ObservableList<Pizza> observableList = FXCollections.observableArrayList(mainViewController.orderSetter());
        orderList.setItems(observableList);
        setCosts();
    }

    /**
     * It removes the selected pizza from the current order.
     * @param actionEvent It recognizes if the user click the remove pizza button.
     */
    @FXML
    public void removePizza(ActionEvent actionEvent) {
        if(orderList.getSelectionModel().getSelectedItem() != null) {
            final int selectedBox = orderList.getSelectionModel().getSelectedIndex();
            mainViewController.removePizzaFromCurrentOrder(selectedBox);
            ObservableList<Pizza> observableList = FXCollections.observableArrayList(mainViewController.orderSetter());
            orderList.setItems(observableList);
            setCosts();
        }
    }

    /**
     * Place the current order to the store.
     * It checks if the current order is empty.
     * @param actionEvent It recognizes if the user click the place order button.
     */
    @FXML
    public void placeOrder(ActionEvent actionEvent) {
        if (mainViewController.orderSetter().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Placing Order");
            alert.setContentText("Order is empty!");
            alert.showAndWait();
            return;
        }
        // send list of order to store order controller
        mainViewController.placeOrder();
        mainViewController.clearCurrentOrder();
        ObservableList<Pizza> observableList = FXCollections.observableArrayList(mainViewController.orderSetter());
        orderList.setItems(observableList);
        setCosts();
        orderNumber.setText(null);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFORMATION");
        alert.setHeaderText("Placing Order");
        alert.setContentText("Order added!");
        alert.showAndWait();
    }
}
