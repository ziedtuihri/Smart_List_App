package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
import java.util.Map;

public class editgroupActivity extends AppCompatActivity implements View.OnClickListener {

    Dialog myDialog;
    TextView listofusers, listoflists;
    ImageView imageView, back ,addlist;
    Spinner spinner;
    ArrayList<String> CountryName;
    SessionManager sessionManager;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editgroup);
        myDialog = new Dialog(this);
        Intent i = getIntent();
        final String title = i.getStringExtra("title");
        TextView name = (TextView) findViewById(R.id.namegroup);
        name.setText(title);
        listofusers = (TextView) findViewById(R.id.listofusers);
        listoflists = (TextView) findViewById(R.id.listoflists);
        imageView = (ImageView) findViewById(R.id.popadd);
        imageView.setOnClickListener(this);
        addlist = (ImageView) findViewById(R.id.addlist);
        addlist.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        sessionManager =new SessionManager(this);
        sessionManager.checkLogin();
        final HashMap<String, String> user= sessionManager.getUserDetail();
        final String token=user.get(sessionManager.TOKEN);

        System.out.print(title);
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, HostName_Interface.link+"/group/followers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                                JSONArray email = jsonObject.getJSONArray("follower name");
                            for (int j = 0; j<email.length(); j++) {
                                JSONArray em=email.getJSONArray(j);
                                for (int i = 0; i<em.length(); i++) {
                                    listofusers.append(em.getString(i)+"\r\n"+"\r\n");
                                }
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(editgroupActivity.this, "ADD USER IN YOUR GROUP", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(editgroupActivity.this, "ADD USER IN YOUR GROUP" , Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                return super.getParams();
            }
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "eiWee8ep9due4deeshoa8Peichai8Eih");
                headers.put("x-access-token", token);
                return headers;
            }
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(Stringrequest);

    }


    public void ShowPopup() {

        TextView txtclose;
        Button create;
        myDialog.setContentView(R.layout.activity_add_users);
        txtclose = (TextView) myDialog.findViewById(R.id.close);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        create = (Button) myDialog.findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.button_create:

                        final EditText user1 = (EditText) myDialog.findViewById(R.id.user1);
                        String email=user1.getText().toString();
                        adduser(email);
                        listofusers.append(user1.getText()+"\r\n");
                        myDialog.hide();
                        break;
                    default:
                }

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    public void adduser(final String email){
        final HashMap<String, String> user= sessionManager.getUserDetail();
        final String token=user.get(sessionManager.TOKEN);
        Intent i = getIntent();
        final String title = i.getStringExtra("title");


        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("title", title);
            jsonBodyObj.put("email", email);


        }catch (JSONException e){
            e.printStackTrace();
        }

        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, HostName_Interface.link+"/follow",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String isCreated =  jsonObject.getString("isAdded");
                            if (isCreated.equals("true")){
                                Toast.makeText(editgroupActivity.this,"New user added in the group",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(editgroupActivity.this,"user exist in the  group" + e.toString() ,Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(editgroupActivity.this,"ADD USER ERROR" + error.toString() ,Toast.LENGTH_LONG).show();
            }
        })

        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params =new HashMap<>();
                params.put("title",title);
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




    public void ShowPopuplist() {

        TextView txtclose;
        Button create;
        myDialog.setContentView(R.layout.activity_add_list);
        txtclose = (TextView) myDialog.findViewById(R.id.close);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        //charge de spinner

       /* String URL="http://techiesatish.com/demo_api/spinner.php";
        CountryName=new ArrayList<>();
        spinner=(Spinner) myDialog.findViewById(R.id.country_Name);
        loadSpinnerData(URL);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String country=   spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });
        */
        create = (Button) myDialog.findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.button_create:


                        myDialog.hide();
                        break;
                    default:
                }

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    //Get data of the spiner
/*
    private void loadSpinnerData(String url) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getInt("success")==1){
                        JSONArray jsonArray=jsonObject.getJSONArray("Name");
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String country=jsonObject1.getString("Country");
                            CountryName.add(country);
                        }
                    }
                    spinner.setAdapter(new ArrayAdapter<String>(editgroupActivity.this, android.R.layout.simple_spinner_dropdown_item, CountryName));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }*/






    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                Intent it = new Intent(editgroupActivity.this,
                        GroupsActivity.class);
                editgroupActivity.this.startActivity(it);
                finish();
                break;
            case R.id.popadd:
                ShowPopup();
                break;
            case R.id.addlist:
                ShowPopuplist();
                break;

        }
    }
}
