package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import static com.android.volley.Request.*;

public class Update_profile extends AppCompatActivity {

    private static String URL_REGIST=HostName_Interface.link+"/user/update";
    HashMap<String, String> user ;
    ImageView back  ;

    Button update, update_password;
    SessionManager sessionManager ;
    private EditText  editTextFirstName, editTextLastName, editTexttel, editDateOfBirth, editLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        back = (ImageView) findViewById(R.id.back_profile);
        editTextFirstName = findViewById(R.id.update_Firstnamed);
        editTextLastName = findViewById(R.id.update_lastname);
        editTexttel = findViewById(R.id.update_tel);
        editDateOfBirth = findViewById(R.id.update_DateofBirth);
        editLocation = findViewById(R.id.update_adress);
        update = findViewById(R.id.btn_update);
        update_password = findViewById(R.id.password_update);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Update_profile.this,Activity_Profile.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_password();
            }
        });

        getData();
    }




    public void getData(){
        sessionManager = new SessionManager(Update_profile.this) ;
        user= sessionManager.getUserDetail();
        editTextFirstName.setText(user.get(sessionManager.FIRSTNAME));
        editTextLastName.setText(user.get(sessionManager.LASTNAME));
        editTexttel.setText(user.get(sessionManager.TEL));
        editDateOfBirth.setText(user.get(sessionManager.DATEOFBIRTH));
        editLocation.setText(user.get(sessionManager.LOCATION));
    }

    public void update()
    {
        final String email = user.get(sessionManager.EMAIL);
        final String firstname = editTextFirstName.getText().toString().trim();
        final String lastname = editTextLastName.getText().toString().trim();
        final String phone_number = editTexttel.getText().toString().trim();
        final String DateofBirth = editDateOfBirth.getText().toString().trim();
        final String Location = editLocation.getText().toString().trim();


        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("email", email);
            jsonBodyObj.put("firstname", firstname);
            jsonBodyObj.put("lastname", lastname);
            jsonBodyObj.put("phone_number", phone_number);
            jsonBodyObj.put("DateofBirth", DateofBirth);
            jsonBodyObj.put("Location", Location);
        }catch (JSONException e){
            e.printStackTrace();
        }

        final String requestBody = jsonBodyObj.toString();

        StringRequest stringrequest = new StringRequest(Request.Method.PUT, HostName_Interface.link+"/user/update",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String isUpdated =  jsonObject.getString("isUpdated");
                            if (isUpdated.equals("true")){
                                Toast.makeText(Update_profile.this,"update Success",Toast.LENGTH_LONG).show();
                                String token = user.get(sessionManager.TOKEN);

                                sessionManager.editor.clear();
                                sessionManager.createSession( firstname,email , lastname,token,phone_number, DateofBirth, Location);
                                sessionManager.editor.commit();
                                Intent i = new Intent(Update_profile.this, Activity_Profile.class);
                                startActivity(i);
                                finish();

                            } else {
                                Toast.makeText(Update_profile.this,"Update failed !",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Update_profile.this," Error in PUT ++ "+e.toString() ,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Update_profile.this," Error in PUT ** " + error.toString() ,Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params =new HashMap<>();
                params.put("firstname",firstname);
                params.put("lastname",lastname);
                params.put("email",email);
                params.put("phone_number",phone_number);
                params.put("DateofBirth",DateofBirth);
                params.put("Location",Location);

                return super.getParams();
            }
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "eiWee8ep9due4deeshoa8Peichai8Eih");
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
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringrequest);

    }


    public void update_password(){
        Intent i = new Intent(Update_profile.this,Update_password.class);
        Update_profile.this.startActivity(i);
    }
}



