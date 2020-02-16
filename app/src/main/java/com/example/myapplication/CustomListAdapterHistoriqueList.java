package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CustomListAdapterHistoriqueList extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ListProductHistorique> movieItems;

    public CustomListAdapterHistoriqueList(Activity activity, List<ListProductHistorique> movieItems) {
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
            convertView = inflater.inflate(R.layout.list_historique_item, null);


        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView price = (TextView) convertView.findViewById(R.id.price);



        // getting movie data for the row
        ListProductHistorique m = movieItems.get(position);



        // title
        long time=Long.parseLong(m.getDate());
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String dat = DateFormat.format("dd-MM-yyyy", cal).toString();

        title.setText(m.getName());

        date.setText(dat);
price.setText(m.getPrice().toString()+" DT");

        return convertView;
    }

}