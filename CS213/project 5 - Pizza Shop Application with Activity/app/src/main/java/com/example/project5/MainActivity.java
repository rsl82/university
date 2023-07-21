package com.example.project5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity class to control the Main window.
 * @author Ryan S. Lee, Songyuan Zhang
 */
public class MainActivity extends AppCompatActivity {

    public static int idx = 0;
    public static Order currentOrder = new Order(idx);
    public static StoreOrders storeOrders = new StoreOrders();

    /**
     * Create and initialize the window.
     * @param savedInstanceState Required.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Open the chicago pizza order window.
     * @param view Required to communicate with xml.
     */
    public void showChicago(View view) {
        Intent intent = new Intent(MainActivity.this,ChicagoActivity.class);
        startActivity(intent);
    }

    /**
     * Open the New York pizza order window.
     * @param view Required to communicate with xml.
     */
    public void showNY(View view) {
        Intent intent = new Intent(MainActivity.this,NYActivity.class);
        startActivity(intent);
    }

    /**
     * Open the current order window.
     * @param view Required to communicate with xml.
     */
    public void showCurrent(View view) {
        Intent intent = new Intent(MainActivity.this,CurrentOrderActivity.class);
        startActivity(intent);
    }

    /**
     * Open the store order window.
     * @param view Required to communicate with xml.
     */
    public void showStoreOrder(View view) {
        Intent intent = new Intent(MainActivity.this,StoreOrdersActivity.class);
        startActivity(intent);
    }

}