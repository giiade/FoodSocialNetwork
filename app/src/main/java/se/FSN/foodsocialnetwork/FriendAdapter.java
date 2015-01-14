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

import java.util.ArrayList;
import java.util.List;

import se.FSN.foodsocialnetwork.utils.AppController;

/**
 * Created by JulioLopez on 14/1/15.
 */
public class FriendAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Friend> friendItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private ArrayList<String> myFriendsIds = new ArrayList<>();


    public FriendAdapter(Activity activity, List<Friend> friendsItems) {
        this.activity = activity;
        this.friendItems = friendsItems;

    }

    public void swapItems(List<Friend> items) {
        this.friendItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return friendItems.size();
    }

    @Override
    public Object getItem(int position) {
        return friendItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
        /*Friend f = friendItems.get(position);
        Long id = Long.valueOf(f.getID());
        return id;
        */
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.friend_adapter, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.image_friend);

        TextView titleTxt = (TextView) convertView.findViewById(R.id.name_friend);
        TextView mailTxt = (TextView) convertView.findViewById(R.id.mail_friend);
        TextView countryTxt = (TextView) convertView.findViewById(R.id.country_friends);

        // getting movie data for the row
        Friend f = friendItems.get(position);

        // thumbnail image
        String imageUrl = f.getImgUrl();
        //Log.i("IMAGEURL",imageUrl);
        thumbNail.setImageUrl(imageUrl, imageLoader);
        //thumbNail.setImageUrl("http://www.online-image-editor.com//styles/2014/images/example_image.png", imageLoader);


        // title
        titleTxt.setText(f.getUsername());

        // Mail
        mailTxt.setText(f.getEmail());

        // Country
        countryTxt.setText(f.getCountry());

        convertView.setTag(f.getEmail());


        return convertView;
    }


}
