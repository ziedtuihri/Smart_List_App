package com.example.myapplication;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class historiquelistActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, View.OnClickListener {
    SessionManager sessionManager;
    SwipeRefreshLayout   swipeLayout ;
    Dialog myDialog;
    ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private List<ListProductHistorique> ProductList =new ArrayList<ListProductHistorique>() ;
    private GridView gridView;
    private CustomListAdapterHistoriqueList adapter;
    ImageView notif,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historiquelist);
        notif=(ImageView)findViewById(R.id.notification);
        back=(ImageView)findViewById(R.id.back);
        notif.setOnClickListener(this);
        back.setOnClickListener(this);
        sessionManager =new SessionManager(this);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        gridView = (GridView) findViewById(R.id.listoflist);
        adapter = new CustomListAdapterHistoriqueList(this, ProductList);

        gridView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        getdata2();
        pDialog.hide();


        sessionManager =new SessionManager(this);

        gridView.setOnItemClickListener(this);






        myDialog = new Dialog(this);

        ImageView menu=(ImageView)findViewById(R.id.menu);
        ImageView notification=(ImageView)findViewById(R.id.notification);
    }



    public void getdata2(){
        ProductList.clear();
        adapter.notifyDataSetChanged();
        MyDBHandlerlistHistorique dbHandler = new MyDBHandlerlistHistorique(this, null, null, 1);
        List<ListProductHistorique> list = dbHandler.fetchlist();
        ListProductHistorique p =new ListProductHistorique();
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i).getName());
            ProductList.add(list.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.notification:


                Intent nt = new Intent(historiquelistActivity.this ,
                        historiquelistActivity.class);
                historiquelistActivity.this.startActivity(nt);

                break;
            case R.id.back:
onBackPressed();

                break;

        }
    }
}
