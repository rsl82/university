package com.example.pr4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Arrays;

/**
 * This class is the controller for NewYorkStylePizzaView.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class NewYorkStylePizzaController {
    @FXML
    private TextField priceText;
    @FXML
    private TextField crustText;
    @FXML
    private Button rightTopping;
    @FXML
    private Button leftTopping;
    @FXML
    private ImageView imageView;
    @FXML
    private ComboBox flavorBox;
    @FXML
    private ComboBox sizeBox;
    private MainViewController mainViewController;
    private Pizza currentPizza;
    private ObservableList<Topping> currentToppings = FXCollections.observableArrayList();
    private ObservableList<Topping> leftToppings = FXCollections.observableArrayList();
    @FXML
    private ListView<Topping> currentToppingList;
    @FXML
    private ListView<Topping> leftToppingList;

    private static final int DELUXE = 1;
    private static final int BBQ = 2;
    private static final int MEATZZA = 3;
    private static final int BYO = 4;

    /**
     * This method initializes the NewYorkStylePizzaView.
     * @param mainViewController the controller to use MainViewController methods in this controller.
     */
    public void setMainController(MainViewController mainViewController) {
        imageView.setImage(new Image(getClass().getResourceAsStream("ny_pizza_main.jpg")));
        imageView.setCache(true);
        this.mainViewController = mainViewController;
        String[] flavorList = {"Deluxe","BBQ Chicken","Meatzza","Build Your Own"};
        ObservableList<String> fxFlavorList = FXCollections.observableArrayList(flavorList);
        flavorBox.setItems(fxFlavorList);
        String[] sizeList = {"Small","Medium","Large"};
        ObservableList<String> fxSizeList = FXCollections.observableArrayList(sizeList);
        sizeBox.setItems(fxSizeList);
        leftToppings.addAll(Arrays.asList(Topping.class.getEnumConstants()));
        leftToppingList.setItems(leftToppings);
        rightTopping.setDisable(true);
        leftTopping.setDisable(true);
        crustText.setEditable(false);
        priceText.setEditable(false);
    }

    /**
     * This method makes the pizza for selected options and update the price and corresponding details.
     */
    @FXML
    public void makePizza() {
        if(flavorBox.getSelectionModel().getSelectedItem()!=null) {
            changeImage();
            if(sizeBox.getSelectionModel().getSelectedItem()!= null) {
                PizzaFactory pizzaFactory = new NYPizza();
                rightTopping.setDisable(true);
                leftTopping.setDisable(true);
                if(flavorBox.getSelectionModel().getSelectedItem().toString().equals("Deluxe")) {
                    adjustTopping(DELUXE);
                    currentPizza = pizzaFactory.createDeluxe(Size.getSize(sizeBox.getSelectionModel().getSelectedItem().toString()),currentToppings);
                }
                else if(flavorBox.getSelectionModel().getSelectedItem().toString().equals("BBQ Chicken")) {
                    adjustTopping(BBQ);
                    currentPizza = pizzaFactory.createBBQChicken(Size.getSize(sizeBox.getSelectionModel().getSelectedItem().toString()),currentToppings);
                }
                else if(flavorBox.getSelectionModel().getSelectedItem().toString().equals("Meatzza")) {
                    adjustTopping(MEATZZA);
                    currentPizza = pizzaFactory.createMeatzza(Size.getSize(sizeBox.getSelectionModel().getSelectedItem().toString()),currentToppings);
                }
                else if(flavorBox.getSelectionModel().getSelectedItem().toString().equals("Build Your Own")) {
                    adjustTopping(BYO);
                    rightTopping.setDisable(false);
                    leftTopping.setDisable(false);
                    currentPizza = pizzaFactory.createBuildYourOwn(Size.getSize(sizeBox.getSelectionModel().getSelectedItem().toString()));
                }
                crustText.setText(currentPizza.crustGetter().toString());
                priceText.setText(String.format("%,.2f", currentPizza.price()));
            }

        }
    }

    /**
     * This method changes the image with the selected flavor.
     */
    private void changeImage() {
        if(flavorBox.getSelectionModel().getSelectedItem().toString().equals("Deluxe")) {
            imageView.setImage(new Image(getClass().getResourceAsStream("ny_deluxe.jpeg")));
        }
        else if(flavorBox.getSelectionModel().getSelectedItem().toString().equals("BBQ Chicken")) {
            imageView.setImage(new Image(getClass().getResourceAsStream("ny_bbqchicken.jpeg")));

        }
        else if(flavorBox.getSelectionModel().getSelectedItem().toString().equals("Meatzza")) {
            imageView.setImage(new Image(getClass().getResourceAsStream("ny_meatzza.jpeg")));

        }
        else if(flavorBox.getSelectionModel().getSelectedItem().toString().equals("Build Your Own")) {
            imageView.setImage(new Image(getClass().getResourceAsStream("ny_build_your_own.jpeg")));
        }
    }

    /**
     * This method adjusts the list of toppings to be included for the flavor.
     * @param index Flavor of the pizza.
     */
    public void adjustTopping(int index) {
        currentToppings.clear();
        leftToppings.addAll(Arrays.asList(Topping.class.getEnumConstants()));
        if(index==DELUXE) {
            currentToppings.addAll(Topping.SAUSAGE,Topping.PEPPERONI,Topping.GREEN_PEPPER,Topping.ONION,Topping.MUSHROOM);
        }
        else if(index==BBQ) {
            currentToppings.addAll(Topping.BBQ_CHICKEN,Topping.GREEN_PEPPER,Topping.PROVOLONE,Topping.CHEDDAR);
        }
        else if(index==MEATZZA) {
            currentToppings.addAll(Topping.SAUSAGE, Topping.PEPPERONI,Topping.BEEF,Topping.HAM);
        }
        leftToppings.removeAll(currentToppings);
        leftToppingList.setItems(leftToppings);
        currentToppingList.setItems(currentToppings);
    }

    /**
     * Add the topping for the pizza.
     * It will be only used for BYO pizza.
     * It will check if the number of the toppings does not exceed the 7.
     * @param actionEvent It recognizes if the user click the add topping button.
     */
    @FXML
    public void addTopping(ActionEvent actionEvent) {
        Topping additionalTopping = leftToppingList.getSelectionModel().getSelectedItem();
        if (currentToppings.size() < 7 && additionalTopping!= null) {
            final int selectedBox = leftToppingList.getSelectionModel().getSelectedIndex();
            currentToppings.add(leftToppings.get(selectedBox));
            leftToppings.remove(selectedBox);
            leftToppingList.setItems(leftToppings);
            currentToppingList.setItems(currentToppings);
            currentPizza.add(additionalTopping);
            priceText.setText(String.format("%,.2f", currentPizza.price()));
        }
        else if (currentToppings.size() >= 7) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Maximum Number of Toppings Exceeded");
            alert.setContentText("Maximum 7 toppigs only");
            alert.showAndWait();
        }
    }

    /**
     * Remove the topping for the pizza.
     * It will be only used for BYO pizza.
     * @param actionEvent It recognizes if the user click the remove topping button.
     */
    @FXML
    public void removeTopping(ActionEvent actionEvent) {
        Topping removedTopping = currentToppingList.getSelectionModel().getSelectedItem();
        if(removedTopping !=null) {
            final int selectedBox = currentToppingList.getSelectionModel().getSelectedIndex();
            leftToppings.add(currentToppings.get(selectedBox));
            currentToppings.remove(selectedBox);
            leftToppingList.setItems(leftToppings);
            currentToppingList.setItems(currentToppings);
            currentPizza.remove(removedTopping);
            priceText.setText(String.format("%,.2f", currentPizza.price()));
        }
    }

    /**
     * Add the selected pizza to the order.
     * It will check if the pizza is not selected properly.
     * @param actionEvent It recognizes if the user click the Add to Order button.
     */
    @FXML
    public void addOrder(ActionEvent actionEvent) {
        if(currentPizza != null) {
            mainViewController.addPizzaToCurrentOrder(currentPizza);
            currentToppings.clear();
            leftToppings.addAll(Arrays.asList(Topping.class.getEnumConstants()));
            leftToppingList.setItems(leftToppings);
            currentToppingList.setItems(currentToppings);
            flavorBox.getSelectionModel().clearSelection();
            sizeBox.getSelectionModel().clearSelection();
            priceText.clear();
            crustText.clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INFORMATION");
            alert.setHeaderText("Add to Order");
            alert.setContentText("Pizza Added to Orders");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("WARNING");
            alert.setHeaderText("No Pizza Selected");
            alert.setContentText("Select proper options before add the order.");
            alert.showAndWait();
        }

    }
}
