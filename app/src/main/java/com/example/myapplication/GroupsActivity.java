package com.example.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    ImageView img;
    Dialog myDialog;
    EditText namegroup;
    SessionManager sessionManager;
    SwipeRefreshLayout swipeLayout;

    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = HostName_Interface.link+"/user/groups";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private GridView gridView;
    private CustomListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        myDialog = new Dialog(this);
        sessionManager =new SessionManager(this);
        sessionManager.checkLogin();

        namegroup=(EditText) findViewById(R.id.Group_name);

        img=(ImageView) findViewById(R.id.imageView3);
        img.setOnClickListener(this);
        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        gridView = (GridView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        gridView.setAdapter(adapter);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        getdata();

        gridView.setOnItemClickListener(this);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
                        Toast.makeText(GroupsActivity.this,"fetch GROUP ERROR" + e.toString() ,Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(GroupsActivity.this,"FETCH GROUP ERROR" + error.toString() ,Toast.LENGTH_LONG).show();
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



    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.imageView3:
                ShowPopup();
                break;
            case R.id.back:
                Intent it = new Intent(GroupsActivity.this,
                        MainActivity.class);
                GroupsActivity.this.startActivity(it);
                finish();
                break;
            default:

        }






    }


    public void ShowPopup() {

        TextView txtclose;
        Button create;

        myDialog.setContentView(R.layout.activity_create__group_);
        txtclose =(TextView) myDialog.findViewById(R.id.close);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        namegroup=(EditText) myDialog.findViewById(R.id.Group_name);
        create=(Button) myDialog.findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifgroup()){
                    Creategroup();
                }
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }



    private boolean verifgroup() {
        final String name8group = this.namegroup.getText().toString().trim();

        boolean tr = true;

        if (name8group.length() < 1) {
            namegroup.setError("First Name required");
            namegroup.requestFocus();
            tr = false;
        }

        return tr;
    }

    private void Creategroup(){
        final HashMap<String, String> user= sessionManager.getUserDetail();
        final String token=user.get(sessionManager.TOKEN);
        final String email=user.get(sessionManager.EMAIL);
        final String namegroup=this.namegroup.getText().toString().trim();



        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("title", namegroup);
            jsonBodyObj.put("email", email);


        }catch (JSONException e){
            e.printStackTrace();
        }

        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, HostName_Interface.link+"/group",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print(email);
                        System.out.print(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String isCreated =  jsonObject.getString("isCreated");
                            if (isCreated.equals("true")){
                                Toast.makeText(GroupsActivity.this,"The Group are Created",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(GroupsActivity.this,"CREATE GROUP ERROR" + e.toString() ,Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GroupsActivity.this,"CREATE GROUP ERROR" + error.toString() ,Toast.LENGTH_LONG).show();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params =new HashMap<>();
                params.put("title",namegroup);
                params.put("email",email);
                return super.getParams();
            }
            public Map getHeaders() throws AuthFailureError
            {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "eiWee8ep9due4deeshoa8Peichai8Eih");
                headers.put("x-access-token", token);
                return headers;
            }


            @Override    public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }



        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(Stringrequest);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object selectedItem =  gridView.getItemAtPosition(position);
        Movie website = (Movie) selectedItem;
        Movie c= website;
        Intent i=new Intent(GroupsActivity.this,editgroupActivity.class);
        i.putExtra("title", c.getTitle().toString());
        startActivity(i);
    }

    @Override
    public void onRefresh() {
        movieList.clear();
        getdata();
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }
}
