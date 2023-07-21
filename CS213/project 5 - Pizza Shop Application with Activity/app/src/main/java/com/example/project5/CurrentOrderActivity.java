package com.example.project5;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity class to control the Current Order window.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class CurrentOrderActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private TextView orderID,subtotal,tax,total;
    private ListView listview;
    private ArrayAdapter<Pizza> orderAdapter;
    private int clickedPosition=-1;
    Toast toast;


    /**
     * Create and initialize the window.
     * @param savedInstanceState Required.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Current Order");
        orderID = findViewById(R.id.orderNum);
        orderID.setText(String.valueOf(MainActivity.idx));
        listview = findViewById(R.id.orderList);
        orderAdapter = new ArrayAdapter<Pizza>(this,android.R.layout.simple_list_item_1,MainActivity.currentOrder.orderGetter());
        listview.setOnItemClickListener(this);
        listview.setAdapter(orderAdapter);
        subtotal=findViewById(R.id.txt_subtotal);
        tax=findViewById(R.id.txt_tax);
        total=findViewById(R.id.txt_total);
        subtotal.setText(String.format("%,.2f", MainActivity.currentOrder.getPizzaCost()));
        tax.setText(String.format("%,.2f", MainActivity.currentOrder.getPizzaTax()));
        total.setText(String.format("%,.2f", MainActivity.currentOrder.getTotal()));
    }

    /**
     * Method that works when the listView item is clicked.
     * Tracks the which one is clicked.
     * @param adapterView Required.
     * @param view Required.
     * @param i Required.
     * @param l Required.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        clickedPosition = i;
    }

    /**
     * Remove the selected pizza.
     * @param view required to communicate with xml.
     */
    public void removePizza(View view) {
        if(clickedPosition>=0 && clickedPosition < MainActivity.currentOrder.getSize()) {
            MainActivity.currentOrder.remove(MainActivity.currentOrder.getPizza(clickedPosition));
            orderAdapter.notifyDataSetChanged();
            toastMessage("Pizza Removed");
            subtotal.setText(String.format("%,.2f", MainActivity.currentOrder.getPizzaCost()));
            tax.setText(String.format("%,.2f", MainActivity.currentOrder.getPizzaTax()));
            total.setText(String.format("%,.2f", MainActivity.currentOrder.getTotal()));

        }
        else {
            toastMessage("Pizza is Not Selected");
        }
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
     * Cancel the current order.
     * @param view required to communicate with xml.
     */
    public void orderCancel(View view) {
        MainActivity.currentOrder.clearOrder();
        orderAdapter.notifyDataSetChanged();
        subtotal.setText(String.format("%,.2f", MainActivity.currentOrder.getPizzaCost()));
        tax.setText(String.format("%,.2f", MainActivity.currentOrder.getPizzaTax()));
        total.setText(String.format("%,.2f", MainActivity.currentOrder.getTotal()));
        toastMessage("Order Canceled");
    }

    /**
     * Place the current order to the store.
     * @param view Required to communicate with xml.
     */
    public void placeOrder(View view) {
        if(MainActivity.currentOrder.getSize()!=0)
        {
            MainActivity.storeOrders.add(MainActivity.currentOrder);
            MainActivity.idx = MainActivity.idx + 1;
            MainActivity.currentOrder = new Order(MainActivity.idx);
            toastMessage("Order is Completed");
            this.finish();
        }
        else
        {
            toastMessage("Order is Empty");
        }
    }
}
