package com.example.project5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/**
 * Adapter class to use the recycler view for the toppings.
 * @author Ryan S. Lee, Songyuan Zhang
 */

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Topping> toppings;
    private boolean isClicked = false;

    /**
     * Constructor for ToppingAdapter.
     * @param context context of the window.
     * @param toppings toppings to print.
     */
    public ToppingAdapter(Context context, ArrayList<Topping> toppings) {
        this.context = context;
        this.toppings = toppings;
    }

    /**
     * Disable or Enable the click for the MOVE button.
     * @param change state to change.
     */
    public void changeClick(boolean change) {
        this.isClicked = change;
    }

    /**
     * This method will inflate the row layout for the items in the RecyclerView.
     * @param parent Required.
     * @param viewType Required.
     * @return ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_topping,parent,false);

        return new ViewHolder(view).linkAdapter(this);
    }

    /**
     * Assign data values for each row according to their "position" (index) when the item becomes
     * visible on the screen.
     * @param holder the instance of ItemsHolder
     * @param position the index of the item in the list of items
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_topping.setText(toppings.get(position).toString());
    }

    /**
     * Get the number of items in the ArrayList.
     * @return the number of items in the list.
     */
    @Override
    public int getItemCount() {
        return toppings.size();
    }


    /**
     * Get the views from the row layout file, similar to the onCreate() method.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_topping;
        private Button btn_addordelete;
        private ToppingAdapter adapter;
        /**
         * Constructor for the ViewHolder.
         * Includes OnclickListener.
         * @param itemView Required.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_topping = itemView.findViewById(R.id.txt_topping);
            btn_addordelete = itemView.findViewById(R.id.btn_addordelete);
            btn_addordelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(adapter.isClicked == false) {
                        Toast toast = Toast.makeText(adapter.context.getApplicationContext(),"Can't Add except BYO Pizza",Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    if(getAdapterPosition() >=0 && getAdapterPosition()<adapter.toppings.size()) {
                        Topping topping = adapter.toppings.get(getAdapterPosition());
                        if(adapter.context instanceof ChicagoActivity) {
                            if(((ChicagoActivity) adapter.context).ifMaximum(topping)) {
                                return;
                            }
                            adapter.toppings.remove(getAdapterPosition());
                            adapter.notifyItemRemoved(getAdapterPosition());
                            ((ChicagoActivity) adapter.context).addBYOTopping(topping);
                        }
                        else if(adapter.context instanceof NYActivity) {
                            if(((NYActivity) adapter.context).ifMaximum(topping)) {
                                return;
                            }
                            adapter.toppings.remove(getAdapterPosition());
                            adapter.notifyItemRemoved(getAdapterPosition());
                            ((NYActivity) adapter.context).addBYOTopping(topping);
                        }
                    }
                }
            });
        }

        /**
         * Link the adapter between activity and the viewholder.
         * @param adapter adapter to link.
         * @return this adapter.
         */
        public ViewHolder linkAdapter(ToppingAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }
}
