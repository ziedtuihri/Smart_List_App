package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    TextView register;
    Button login ;
    private EditText editTextEmail, editTextPassword;
    private RequestQueue mQueue;
   public SessionManager sessionManager;
    private ProgressBar loading;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




         sessionManager = new   SessionManager(this);




        register = (TextView) findViewById(R.id.l1);
        register.setOnClickListener(this);
        login = (Button) findViewById(R.id.button_signin);
        login.setOnClickListener(this);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        SharedPreferences prefs = getSharedPreferences("med", MODE_PRIVATE);
        String restoredText = prefs.getString("email", null);
        if (restoredText != null) {
            editTextEmail.setText(prefs.getString("email", ""));//"No name defined" is the default value.
            editTextPassword.setText(prefs.getString("password", "")); //0 is the default value.
        }

        mQueue = Volley.newRequestQueue(this);
        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        loading=findViewById(R.id.loading);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
         pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request

    }
    private void login() {

        CheckBox remember=(CheckBox)findViewById(R.id.remember);

        final String email = this.editTextEmail.getText().toString().trim();


        final String password = this.editTextPassword.getText().toString().trim();

        if (remember.isChecked()) {
            SharedPreferences.Editor editor = getSharedPreferences("med", MODE_PRIVATE).edit();
            editor.clear();
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();
        }

        if (userLogin()) {

            pDialog.setMessage("Loading...");
            pDialog.show();




            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("email", email);
                jsonBodyObj.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = jsonBodyObj.toString();

            StringRequest Stringrequest = new StringRequest(Request.Method.POST, HostName_Interface.link+"/user/login",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.print(response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                System.out.print(response);
                                String isCreated = jsonObject.getString("isLogged");
                                if (isCreated.equals("true"))
                                {
                                    String email = jsonObject.getString("email");
                                    String firstname = jsonObject.getString("firstname");
                                    String lastname = jsonObject.getString("lastname");
                                    String tel = jsonObject.getString("phone_number");
                                    String token = jsonObject.getString("token");
                                    String adresse = jsonObject.getString("adresse");
                                    String dateOfBirth = jsonObject.getString("dateOfBirth");

                                    sessionManager.createSession( firstname,email , lastname,token,tel, dateOfBirth, adresse);
                                    Intent i =new Intent(LoginActivity.this,Activity_Profile.class);
                                    startActivity(i);
                                    finish();
                                    hidePDialog();

                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Login Error"  , Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "login Error" + e.toString(), Toast.LENGTH_LONG).show();
                                loading.setVisibility(View.GONE);
                                register.setVisibility(View.VISIBLE);
                                hidePDialog();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "login Error" + error.toString(), Toast.LENGTH_LONG).show();
                    loading.setVisibility(View.GONE);
                    register.setVisibility(View.VISIBLE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", email);
                    params.put("password", password);
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
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(Stringrequest);

        }
    }




    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }





    private boolean userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        boolean tr=true;

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            tr=false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            tr=false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password required");
            editTextPassword.requestFocus();
            tr=false;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be atleast 6 character long");
            editTextPassword.requestFocus();
            tr=false;
        }
        return tr;

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.l1:

                Intent intentMain = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                LoginActivity.this.startActivity(intentMain);
                finish();
                break;

            case R.id.button_signin:

                break;
            case R.id.back:
                finish();
                onBackPressed();

                break;

            default:

        }
    }
}
