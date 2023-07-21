package com.example.project5;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

/**
 * Activity class to control Store Order window.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class StoreOrdersActivity extends AppCompatActivity {

    private Spinner spn_ID;
    private ArrayAdapter idAdapter;
    private ArrayList<Integer> storeIDList;
    private ListView storeListView;
    private ArrayAdapter<Pizza> orderAdapter;
    private ArrayList<Pizza> currentID;
    private TextView totalPrice;
    private Toast toast;

    /**
     * Create and initialize the window.
     * @param savedInstanceState Required.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeorders);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Store Order");
        initializeSpinner();
        initializeListView();
        initializePrice();
    }

    /**
     * Method to initialize instances related to price.
     */
    private void initializePrice() {
        totalPrice = findViewById(R.id.totalPrice);
        if(spn_ID.getSelectedItem() != null) {
            totalPrice.setText(String.format("%,.2f", MainActivity.storeOrders.getOrder((Integer) (spn_ID.getSelectedItem())).getTotal()));
        }

    }

    /**
     * Method to initialize instances related to listview.
     */
    private void initializeListView() {
        storeListView = findViewById(R.id.storeListView);
        currentID = new ArrayList<>();
        if(spn_ID.getSelectedItem() != null) {
            currentID.addAll(MainActivity.storeOrders.getOrder((Integer) (spn_ID.getSelectedItem())).orderGetter());
        }
        orderAdapter = new ArrayAdapter<Pizza>(this,android.R.layout.simple_list_item_1,currentID);
        storeListView.setAdapter(orderAdapter);
    }

    /**
     * Method to initialize instances related to spinner.
     */
    private void initializeSpinner() {
        spn_ID = findViewById(R.id.spinner);
        storeIDList = MainActivity.storeOrders.getOrderIdList();
        idAdapter =new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,storeIDList);
        spn_ID.setAdapter(idAdapter);
        spn_ID.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                currentID.clear();
                currentID.addAll(MainActivity.storeOrders.getOrder((Integer) (spn_ID.getSelectedItem())).orderGetter());
                orderAdapter.notifyDataSetChanged();
                totalPrice.setText(String.format("%,.2f", MainActivity.storeOrders.getOrder((Integer) (spn_ID.getSelectedItem())).getTotal()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentID.clear();
                orderAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Method to print toast message.
     * @param string message to print.
     */
    public void toastMessage(String string) {
        if(toast!=null)
        {
            toast.cancel();
        }
        toast = Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Cancel the selected order.
     * @param view Required to communicate with xml.
     */
    public void cancelOrder(View view) {
        if(spn_ID.getSelectedItem() == null) {
            toastMessage("Select Order Number First");
        }
        else {
            Order temp = MainActivity.storeOrders.getOrder((Integer) (spn_ID.getSelectedItem()));
            MainActivity.storeOrders.remove(temp);
            storeIDList.clear();
            storeIDList.addAll(MainActivity.storeOrders.getOrderIdList());
            idAdapter.notifyDataSetChanged();
            if(storeIDList.size() > spn_ID.getSelectedItemPosition() && spn_ID.getSelectedItem() != null && MainActivity.storeOrders.getOrder((Integer) (spn_ID.getSelectedItem())) != null) {
                currentID.clear();
                currentID.addAll(MainActivity.storeOrders.getOrder((Integer) (spn_ID.getSelectedItem())).orderGetter());
                orderAdapter.notifyDataSetChanged();
                totalPrice.setText(String.format("%,.2f", MainActivity.storeOrders.getOrder((Integer) (spn_ID.getSelectedItem())).getTotal()));
            }

            else {
                totalPrice.setText(null);
            }
            toastMessage("Order Canceled");

        }
    }
}
