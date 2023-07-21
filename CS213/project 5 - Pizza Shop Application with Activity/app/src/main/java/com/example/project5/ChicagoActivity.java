package com.example.project5;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activity Class to control Chicago Pizza window.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class ChicagoActivity extends AppCompatActivity {
    private String[] flavor = {"BBQ Chicken","Deluxe","Meatzza","Build Your Own"};
    private String[] size = {"Small","Medium","Large"};
    private Spinner spn_flavor,spn_size;
    private ArrayAdapter flavorAdapter, sizeAdapter;
    private String currentFlavor,currentSize;
    private ArrayList<Topping> leftToppings,addedToppings;
    private RecyclerView leftToppingList,addedToppingList;
    private ToppingAdapter leftAdapter,addedAdapter;
    private Pizza currentPizza;
    private int leftToppingCount = 13;
    private int addedToppingCount = 0;
    private TextView price,txt_crust;
    private Toast toast;
    private ImageView img;

    /**
     * Create and initialize the window.
     * @param savedInstanceState Required.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicago);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Chicago Pizza Order");
        currentFlavor = "BBQ Chicken";
        currentSize = "Small";
        price = findViewById(R.id.txt_price);
        img = findViewById(R.id.imageView);
        initializeToppings();
        initializeFlavors();
        initializeSize();
        makePizza();
        txt_crust = findViewById(R.id.txt_crust);
        txt_crust.setText(currentPizza.crustGetter().toString());
    }

    /**
     * Add the Topping to BYO Pizza.
     * @param topping Topping to add.
     */
    public void addBYOTopping(Topping topping) {

        if(leftToppingCount != leftToppings.size()) {
            addedToppings.add(topping);
            addedAdapter.notifyDataSetChanged();
        }
        else if(addedToppingCount != addedToppings.size()) {
            leftToppings.add(topping);
            leftAdapter.notifyDataSetChanged();
        }
        leftToppingCount = leftToppings.size();
        addedToppingCount = addedToppings.size();
        currentPizza.changeTopping(addedToppings);
        updatePrice();
    }

    /**
     * Check if the maximum topping count is reached for BYO Pizza.
     * @param topping Topping to Add.
     * @return true if we cannot add the topping, false if we can.
     */
    public boolean ifMaximum(Topping topping) {
        if(leftToppings.indexOf(topping) != -1) {
            if(addedToppingCount >= 7) {
                if(toast!=null)
                {
                    toast.cancel();
                }
                toast = Toast.makeText(getApplicationContext(),"Maximum 7 toppings",Toast.LENGTH_SHORT);

                toast.show();
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Method to update the price.
     */
    public void updatePrice() {
        price.setText(String.format("%,.2f", currentPizza.price()));
    }

    /**
     * Method to change the topping as the change of the flavor.
     */
    public void adjustTopping() {
        addedToppings.clear();
        leftToppings.clear();
        leftToppings.addAll(Arrays.asList(Topping.class.getEnumConstants()));
        if(currentFlavor.equals("Deluxe")) {
            addedToppings.addAll(Arrays.asList(Topping.SAUSAGE,Topping.PEPPERONI,Topping.GREEN_PEPPER,Topping.ONION,Topping.MUSHROOM));
        }
        else if(currentFlavor.equals("BBQ Chicken")) {
            addedToppings.addAll(Arrays.asList(Topping.BBQ_CHICKEN,Topping.GREEN_PEPPER,Topping.PROVOLONE,Topping.CHEDDAR));
        }
        else if(currentFlavor.equals("Meatzza")) {
            addedToppings.addAll(Arrays.asList(Topping.SAUSAGE, Topping.PEPPERONI,Topping.BEEF,Topping.HAM));
        }
        leftToppings.removeAll(addedToppings);
    }

    /**
     * Make a pizza with selected options.
     */
    private void makePizza() {
        adjustTopping();
        PizzaFactory chicagoFactory = new ChicagoPizza();
        if(currentFlavor.equals("BBQ Chicken")) {
            leftAdapter.changeClick(false);
            addedAdapter.changeClick(false);
            currentPizza = chicagoFactory.createBBQChicken(Size.getSize(currentSize),addedToppings);
        }
        else if(currentFlavor.equals("Deluxe")) {
            leftAdapter.changeClick(false);
            addedAdapter.changeClick(false);
            currentPizza = chicagoFactory.createDeluxe(Size.getSize(currentSize),addedToppings);
        }
        else if(currentFlavor.equals("Meatzza")) {
            leftAdapter.changeClick(false);
            addedAdapter.changeClick(false);
            currentPizza = chicagoFactory.createMeatzza(Size.getSize(currentSize),addedToppings);
        }
        else {
            leftAdapter.changeClick(true);
            addedAdapter.changeClick(true);
            currentPizza = chicagoFactory.createBuildYourOwn(Size.getSize(currentSize));
            leftToppingCount = 13;
            addedToppingCount = 0;
        }
        leftAdapter.notifyDataSetChanged();
        addedAdapter.notifyDataSetChanged();
        updatePrice();
    }

    /**
     * Initialize the instances related with toppings.
     */
    private void initializeToppings() {
        leftToppings = new ArrayList<>();
        leftToppings.addAll(Arrays.asList(Topping.SAUSAGE,Topping.BEEF,Topping.HAM,Topping.PEPPERONI,Topping.ONION,
                Topping.MUSHROOM, Topping.PINEAPPLE, Topping.BLACKOLIVES, Topping.SPINACH));
        leftToppingList = findViewById(R.id.leftToppings);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        leftToppingList.setLayoutManager(layoutManager1);
        leftAdapter = new ToppingAdapter(this,leftToppings);
        leftToppingList.setAdapter(leftAdapter);

        addedToppings = new ArrayList<>();
        addedToppings.addAll(Arrays.asList(Topping.BBQ_CHICKEN,Topping.GREEN_PEPPER,Topping.PROVOLONE,Topping.CHEDDAR));
        addedToppingList = findViewById(R.id.addedToppings);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        addedToppingList.setLayoutManager(layoutManager2);
        addedAdapter = new ToppingAdapter(this,addedToppings);
        addedToppingList.setAdapter(addedAdapter);
    }

    /**
     * Initialize the instances related with size.
     */
    private void initializeSize() {
            spn_size = findViewById(R.id.spn_size);
            sizeAdapter = new ArrayAdapter(this, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item,size);
            sizeAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            spn_size.setAdapter(sizeAdapter);
            spn_size.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    currentSize = spn_size.getSelectedItem().toString();
                    makePizza();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
    }

    /**
     * onClick method when click place order button.
     * @param view Required to communicate with xml.
     */
    public void placeOrder(View view) {
        MainActivity.currentOrder.add(currentPizza);
        if(toast!=null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(),"Order Added",Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Change image as the flavor changes.
     */
    public void changeImage() {
        if(currentFlavor.equals("BBQ Chicken")) {
            img.setImageResource(R.drawable.chicago_bbqchicken);
        }
        else if(currentFlavor.equals("Deluxe")) {
            img.setImageResource(R.drawable.chicago_deluxe);
        }
        else if(currentFlavor.equals("Meatzza")) {
            img.setImageResource(R.drawable.chicago_meatzza);
        }
        else if(currentFlavor.equals("Build Your Own")) {
            img.setImageResource(R.drawable.chicago_build_your_own);
        }

    }

    /**
     * Initializes the instances related with flavor.
     */
    private void initializeFlavors() {
        spn_flavor = findViewById(R.id.spn_flavor);
        flavorAdapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,flavor);
        flavorAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spn_flavor.setAdapter(flavorAdapter);
        spn_flavor.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                currentFlavor = spn_flavor.getSelectedItem().toString();
                makePizza();
                changeImage();
                txt_crust.setText(currentPizza.crustGetter().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
