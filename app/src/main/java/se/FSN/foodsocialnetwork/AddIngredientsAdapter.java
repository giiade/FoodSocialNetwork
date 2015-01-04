package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JulioLopez on 15/12/14.
 */
public class AddIngredientsAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Ingredient> ingredientsItems;


    public AddIngredientsAdapter(Activity activity, List<Ingredient> ingredientsItems) {
        this.activity = activity;
        this.ingredientsItems = ingredientsItems;
    }

    @Override
    public int getCount() {
        return ingredientsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredientsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.ingredientslist_pattern, null);


        TextView title = (TextView) convertView.findViewById(R.id.ingredientTitle);
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
        TextView optional = (TextView) convertView.findViewById(R.id.optionalTxt);

        // getting movie data for the row
        Ingredient i = ingredientsItems.get(position);

        // title
        title.setText(i.getTitle());

        // quantity
        quantity.setText("Quantity: " + i.getQuantity() + " " + i.getInputType());

        //Optional
        if (i.isOptional()) {
            optional.setText("Optional");
        } else {
            optional.setText("Mandatory");
        }

        return convertView;
    }

    ;
}

