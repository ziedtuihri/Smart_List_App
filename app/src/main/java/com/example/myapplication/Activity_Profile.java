package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Activity_Profile extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    SessionManager sessionManager;
    ProgressDialog pDialog ;
    SwipeRefreshLayout  swipeLayout;
    Button update_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__profile);
        sessionManager =new SessionManager(this);
        sessionManager.checkLogin();
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        TextView   tname =(TextView) findViewById(R.id.name);
        TextView  ttel =(TextView) findViewById(R.id.age);
        TextView  tmail =(TextView) findViewById(R.id.mail);
        LinearLayout creategroup =(LinearLayout) findViewById(R.id.creategroup);


        update_profile = findViewById(R.id.update_profile);
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Profile.this, Update_profile.class) ;
                Activity_Profile.this.startActivity(intent);
            }
        });


        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        creategroup.setOnClickListener(this);


        HashMap<String, String> user= sessionManager.getUserDetail();
        String firstname=user.get(sessionManager.FIRSTNAME);
        String lastname=user.get(sessionManager.LASTNAME);
        String memail=user.get(sessionManager.EMAIL);
        String tel=user.get(sessionManager.TEL);



        tname.setText(firstname+" "+lastname);
        tmail.setText(memail);
        ttel.setText(tel);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    @Override
    public void onBackPressed() {
      Intent i=new Intent(Activity_Profile.this,MainActivity.class);
      startActivity(i);
    }



    private void logout() {
        pDialog = new ProgressDialog(this);



        HashMap<String, String> user= sessionManager.getUserDetail();
       final String memail=user.get(sessionManager.EMAIL);
        final String TOKEN=user.get(sessionManager.TOKEN);

            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("email", memail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = jsonBodyObj.toString();

            StringRequest Stringrequest = new StringRequest(Request.Method.POST, HostName_Interface.link+"/user/logout",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.print(response);
                            try {
                                // Showing progress dialog before making http request
                                pDialog.setMessage("Loading...");
                                pDialog.show();
                                JSONObject jsonObject = new JSONObject(response);
                                System.out.print(response);
                                String isCreated = jsonObject.getString("isLogged");
                                if (isCreated.equals("false"))
                                {
                                    Intent i =new Intent(Activity_Profile.this,LoginActivity.class);
                                    hidePDialog();
                                    startActivity(i);
                                }
                                else
                                {
                                    Toast.makeText(Activity_Profile.this, "Login Error"  , Toast.LENGTH_LONG).show();
                                    hidePDialog();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(Activity_Profile.this, "Register Error" + e.toString(), Toast.LENGTH_LONG).show();


                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Activity_Profile.this, "ister Error" + error.toString(), Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", memail);
                    return super.getParams();
                }
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("x-api-Key", "eiWee8ep9due4deeshoa8Peichai8Eih");
                    headers.put("x-access-token", TOKEN);
                    //headers.put("x-access-token",token );

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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                logout();
                sessionManager.logout();
                break;
            case R.id.creategroup:
                Intent intentMain = new Intent(Activity_Profile.this,
                        GroupsActivity.class);
                Activity_Profile.this.startActivity(intentMain);
                finish();
                break;
            case R.id.back:
                Intent it = new Intent(Activity_Profile.this,
                        MainActivity.class);
                Activity_Profile.this.startActivity(it);
                finish();
                break;
            default:
        }
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
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
}
