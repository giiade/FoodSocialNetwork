package se.FSN.foodsocialnetwork;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import se.FSN.foodsocialnetwork.utils.AppController;

/**
 * This will provide data to the ListView. It will render the row layout we have made pre-filling the appropriate information
 */
public class ListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Recipe> recipeItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ListAdapter(Activity activity, List<Recipe> recipeItems) {
        this.activity = activity;
        this.recipeItems = recipeItems;
    }

    public void swapItems(List<Recipe> items) {
        this.recipeItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recipeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        Recipe r = recipeItems.get(position);
        Long id = Long.valueOf(r.getID());
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_pattern, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.image);
        TextView title = (TextView) convertView.findViewById(R.id.recipeTitle);
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView difficult = (TextView) convertView.findViewById(R.id.difficult);

        // getting movie data for the row
        Recipe r = recipeItems.get(position);

        // thumbnail image
        String imageUrl = r.getImageUrl();
        thumbNail.setImageUrl(imageUrl, imageLoader);


        // title
        title.setText(r.getTitle());

        // difficult
        //category.setText(String.valueOf(r.getTime()));

        // Category
        /*String catStr = "";
        for (String str : r.getCategories()) {
            catStr += str + ", ";
        }
        catStr = catStr.length() > 0 ? catStr.substring(0,
                catStr.length() - 2) : catStr;
        category.setText(catStr);
        */


        return convertView;
    }
}

