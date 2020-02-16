package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


public class CustomListAdapterListProduct_course_mode extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Product> movieItems ;

    public CustomListAdapterListProduct_course_mode(Activity activity, List<Product> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }
    public void remove(int position) {
        movieItems.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_product_mode_course, null);


        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
        Product m = movieItems.get(position);

        // thumbnail image

        // title
        title.setText(m.get_productname());

        // rating
        rating.setText("Price: " + String.valueOf(m.getPrix()*m.getQuantite()));


        // release year
        year.setText(String.valueOf(String.valueOf( m.getQuantite())+" "+m.getUnite()));
        genre.setText("");

        return convertView;
    }

}