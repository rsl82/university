package com.example.pr4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Class that controls StoreOrdersView.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class StoreOrdersController {
    @FXML
    private ComboBox orderNumberList;
    @FXML
    private TextField orderTotal;
    @FXML
    private ListView orderList;
    private MainViewController mainViewController;

    /**
     * This method initializes the storeOrdersView.
     * @param mainViewController the controller to use MainViewController methods in this controller.
     */
    public void setMainController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        // get all the order numbers
        ObservableList<Integer> fxOrderNumberList= FXCollections.observableArrayList(this.mainViewController.getOrderIdList());
        orderNumberList.setItems(fxOrderNumberList);
    }

    /**
     * Display the selected order from the orderList.
     * @param event Recognizes if the order number is selected.
     */
    @FXML
    public void displayOrder(ActionEvent event) {
        if (orderNumberList.getSelectionModel().getSelectedItem() != null) {
            int selectedId = Integer.parseInt(orderNumberList.getSelectionModel().getSelectedItem().toString());
            ObservableList<Pizza> fxOrderList= FXCollections.observableArrayList(
                    (this.mainViewController.getOrder(selectedId)).orderGetter());
            orderList.setItems(fxOrderList);
            orderTotal.setEditable(false);
            setCosts(this.mainViewController.getOrder(selectedId));
        }
    }

    /**
     * Get the cost of the specific order.
     * @param order Order to get cost.
     */
    public void setCosts(Order order) {
        if (order == null) {
            orderTotal.setText(String.format("%,.2f", 0.00));
            return;
        }
        orderTotal.setText(String.format("%,.2f", order.getTotal()));
    }

    /**
     * Cancel the order.
     * @param event Recognizes if the cancel order button is clicked.
     */
    @FXML
    public void cancelOrder(ActionEvent event) {
        if (orderNumberList.getSelectionModel().getSelectedItem() != null) {
            int selectedId = Integer.parseInt(orderNumberList.getSelectionModel().getSelectedItem().toString());
            mainViewController.cancelOrder(selectedId);
            orderList.setItems(null);
            orderTotal.setEditable(false);
            orderNumberList.setItems(null);
            setCosts(null);
            ObservableList<Integer> fxOrderNumberList= FXCollections.observableArrayList(this.mainViewController.getOrderIdList());
            orderNumberList.setItems(fxOrderNumberList);
        }
    }



    /**
     * Recognizes the export button click and do the sutffs
     * @param event Recognizes if export the orders button is clicked.
     */
    @FXML
    void exportStoreOrder(ActionEvent event) {
        if(mainViewController.getStoreOrderList().size() != 0) {
            mainViewController.exportOrders();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("No order exists");
            alert.setContentText("Place an order before export.");
            alert.showAndWait();
        }

    }
}
