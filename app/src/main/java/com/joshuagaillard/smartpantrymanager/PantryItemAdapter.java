package com.joshuagaillard.smartpantrymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PantryItemAdapter extends ArrayAdapter<PantryItem> {

    public PantryItemAdapter(
            Context context,
            List<PantryItem> pantryItems) {

        super(
                context,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                pantryItems
        );
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    android.R.layout.simple_list_item_2,
                    parent,
                    false
            );
        }

        PantryItem pantryItem = getItem(position);

        TextView text1 =
                view.findViewById(android.R.id.text1);

        TextView text2 =
                view.findViewById(android.R.id.text2);

        if (pantryItem != null) {

            text1.setText(
                    pantryItem.getIngredientName()
            );

            text2.setText(
                    "Quantity: "
                            + pantryItem.getQuantity()
                            + "\nUnit: "
                            + pantryItem.getUnit()
            );
        }

        return view;
    }
}