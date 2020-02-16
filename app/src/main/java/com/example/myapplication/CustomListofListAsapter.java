package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListofListAsapter  extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ListProduct> movieItems;

    public CustomListofListAsapter(Activity activity, List<ListProduct> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
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
            convertView = inflater.inflate(R.layout.list_of_lists, null);


        TextView title = (TextView) convertView.findViewById(R.id.title);
        ImageView img =(ImageView)  convertView.findViewById(R.id.img);

        // getting movie data for the row
        ListProduct m = movieItems.get(position);
        img.setImageResource(R.drawable.list);

        // title
        title.setText(m.getName());
        if(m.getName()=="ADD NEW LIST"){
            img.setImageResource(R.drawable.ic_add_black_24dp);
            title.setTextSize(13);

        }


        return convertView;
    }

}