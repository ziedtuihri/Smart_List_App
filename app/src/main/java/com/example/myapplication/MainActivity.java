package com.example.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, LocationListener  {

    private static boolean Check_GPS = false;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    SessionManager sessionManager;
   SwipeRefreshLayout   swipeLayout ;
    Dialog myDialog;
    ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private List<ListProduct> ProductList =new ArrayList<ListProduct>() ;
    private GridView gridView;
    private CustomListofListAsapter adapter;
    Toolbar toolbar ;
    NotificationCompat.Builder notification;
    private static final int uniqueid=45612;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // add for function location GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);








        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ScrollView mainScrollView = (ScrollView)findViewById(R.id.scroll);
        MyDBHandlerlist dbHandler = new MyDBHandlerlist(this, null, null, 1);
        MyDBHandlerlistHistorique dbHandlerhisto = new MyDBHandlerlistHistorique(this, null, null, 1);
        List<ListProduct> lp =dbHandler.fetchlist();
        List<ListProductHistorique>  lph;
        long[] v = {500,1000};
    for(int c=0;c<lp.size();c++){
    String name=lp.get(c).getName();
    lph=dbHandlerhisto.fetchlistdetail(name);
    if(lph.isEmpty()){
        notification =new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.notification);
        notification.setWhen(System.currentTimeMillis());
        notification.setVibrate(v);
        notification.setContentTitle("SmartList Notification");
        notification.setContentText("This list is not buyed for now"+" "+name);
        NotificationManager nm=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueid,notification.build());
    }
}

        sessionManager =new SessionManager(this);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        gridView = (GridView) findViewById(R.id.listoflist);

        adapter = new CustomListofListAsapter(this, ProductList);

        gridView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        getdata2();
        pDialog.hide();



        sessionManager =new SessionManager(this);

        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                ListProduct pro=(ListProduct)arg0.getItemAtPosition(pos);

                ShowPopupdelete(pro.getName());

                return true;
            }
        });


        LinearLayout prof =(LinearLayout) findViewById(R.id.ic_profile);

        LinearLayout lists =(LinearLayout) findViewById(R.id.listes);

        LinearLayout groupes =(LinearLayout) findViewById(R.id.groupes);

        LinearLayout histo =(LinearLayout) findViewById(R.id.histori);

        prof.setOnClickListener(this);

        lists.setOnClickListener(this);

        groupes.setOnClickListener(this);

        histo.setOnClickListener(this);



        myDialog = new Dialog(this);
        mainScrollView.fullScroll(ScrollView.FOCUS_UP);

        ImageView menu=(ImageView)findViewById(R.id.menu);
        ImageView notification=(ImageView)findViewById(R.id.notification);
        menu.setOnClickListener(this);
        notification.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    public void newList (String name) {
        MyDBHandlerlist dbHandler = new MyDBHandlerlist(this, null, null, 1);


        ListProduct list =
                new ListProduct(0,name,null);

        dbHandler.addList(list);
    }
    MyDBHandlerlist dbHandler = new MyDBHandlerlist(this, null, null, 1);

    public void ShowPopupdelete(final String name) {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.delete);
        txtclose =(TextView) myDialog.findViewById(R.id.close);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        Button create=(Button) myDialog.findViewById(R.id.delete);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteList(name);
                Toast.makeText(MainActivity.this, "Your List are Deleted", Toast.LENGTH_SHORT).show();
                getdata2();
                myDialog.hide();
            }
        });
        Button cancel=(Button) myDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.hide();
            }
        });

    }
    //pupup of create group
    public void ShowPopup() {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.activity_create__list);
        txtclose =(TextView) myDialog.findViewById(R.id.close);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        Button create=(Button) myDialog.findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText t=(EditText) myDialog.findViewById(R.id.List_name);
                String name =t.getText().toString();
                newList(name);
                System.out.println(name);
                Toast.makeText(MainActivity.this, "Your List are created", Toast.LENGTH_SHORT).show();
                getdata2();
                myDialog.hide();
            }
        });

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

        }

        return super.onOptionsItemSelected(item);
    }
    // button click slide bar
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_lists) {
            // Handle the camera action
        } else if (id == R.id.nav_historique) {

            Intent intentMain = new Intent(MainActivity.this ,
                    historiquelistActivity.class);
            MainActivity.this.startActivity(intentMain);

        } else if (id == R.id.nav_profile) {


            if(sessionManager.isLoggin()) {
                Intent intentMain = new Intent(MainActivity.this,
                        Activity_Profile.class);
                MainActivity.this.startActivity(intentMain);
            }
            else{
                Intent intentMain = new Intent(MainActivity.this,
                        LoginActivity.class);
                MainActivity.this.startActivity(intentMain);
            }

        } else if (id == R.id.nav_tools) {


        } else if (id == R.id.nav_Groups) {

            if(sessionManager.isLoggin()) {
                Intent intentMain = new Intent(MainActivity.this ,
                        GroupsActivity.class);
                MainActivity.this.startActivity(intentMain);
            }
            else{
                Intent intentMain = new Intent(MainActivity.this,
                        LoginActivity.class);
                MainActivity.this.startActivity(intentMain);
                Toast.makeText(MainActivity.this,"To aceess to the groups you have to login "  ,Toast.LENGTH_LONG).show();

            }



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }



    public void getdata2(){
        ProductList.clear();
        adapter.notifyDataSetChanged();
        MyDBHandlerlist dbHandler = new MyDBHandlerlist(this, null, null, 1);
       List<ListProduct> list = dbHandler.fetchlist();
        ListProduct p =new ListProduct();
        p.setName("ADD NEW LIST");
        ProductList.add(p);
       for(int i=0;i<list.size();i++){
            System.out.println(list.get(i).getName());
           ProductList.add(list.get(i));
       }
        adapter.notifyDataSetChanged();
    }





    public void getdata(){
        final HashMap<String, String> user= sessionManager.getUserDetail();
        final String token=user.get(sessionManager.TOKEN);



        StringRequest Stringrequest = new StringRequest(Request.Method.GET, HostName_Interface.link+"/user/groups",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        hidePDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray names =  jsonObject.getJSONArray("groups names");

                            int i=jsonObject.getInt("count");

                            for (int j = 0; j < i; j++) {
                                Movie movie = new Movie();
                                String ch=  names.getString(j);
                                movie.setTitle( ch);
                                movieList.add(movie);
                            }


                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"fetch GROUP ERROR" + e.toString() ,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"FETCH GROUP ERROR" + error.toString() ,Toast.LENGTH_LONG).show();
                hidePDialog();
            }
        })

        {
            @Override
            public Map getHeaders() throws AuthFailureError
            {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "eiWee8ep9due4deeshoa8Peichai8Eih");
                headers.put("x-access-token", token);
                return headers;
            }




        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(Stringrequest);


    }





    //all button click
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_create:

                ShowPopup();
                break;

            case R.id.ic_profile:
                if(sessionManager.isLoggin()) {
                    Intent intentMain = new Intent(MainActivity.this,
                            Activity_Profile.class);
                    MainActivity.this.startActivity(intentMain);
                }
                else{
                    Intent intentMain = new Intent(MainActivity.this,
                            LoginActivity.class);
                    MainActivity.this.startActivity(intentMain);
                }

                break;

            case R.id.histori:

                Intent i = new Intent(MainActivity.this ,
                        historiquelistActivity.class);
                MainActivity.this.startActivity(i);

                break;

            case R.id.groupes:
                if(sessionManager.isLoggin()) {
                    Intent intentMain = new Intent(MainActivity.this ,
                            GroupsActivity.class);
                    MainActivity.this.startActivity(intentMain);
                }
                else{
                    Intent intentMain = new Intent(MainActivity.this,
                            LoginActivity.class);
                    MainActivity.this.startActivity(intentMain);
                    Toast.makeText(MainActivity.this,"To aceess to your groups you have to login"  ,Toast.LENGTH_LONG).show();

                }


                break;

            case R.id.listes:
                Intent it = new Intent(MainActivity.this ,
                        MainActivity.class);
                MainActivity.this.startActivity(it);

                break;
            case R.id.menu:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.notification:
                Intent nt = new Intent(MainActivity.this ,
                        Notificaton.class);
                MainActivity.this.startActivity(nt);
                break;

            default:

        }



    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object selectedItem =  gridView.getItemAtPosition(position);
        ListProduct website = (ListProduct) selectedItem;
        if(website.getName()=="ADD NEW LIST"){
            ShowPopup();
        }else {
            ListProduct c = website;
            Intent i = new Intent(MainActivity.this, SimpleAndroidSearchViewExample.class);
            i.putExtra("title", c.getName().toString());
            startActivity(i);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
    if(Check_GPS == false)
    {
        Toast.makeText(MainActivity.this,"a : "+location.getLatitude()+" b : "+location.getLongitude(),Toast.LENGTH_SHORT).show();
        Check_GPS = true ;
    }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
