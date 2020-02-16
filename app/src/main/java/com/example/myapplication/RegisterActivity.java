package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button register;
    TextView login;
    private ProgressBar loading;
    private static String URL_REGIST=HostName_Interface.link+"/user/signup";
    private EditText editTextEmail, editTextPassword, editTextName, editTextfirst, editTexttel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = (TextView) findViewById(R.id.link_login);
        login.setOnClickListener(this);

        register = (Button) findViewById(R.id.btn_signup);
        register.setOnClickListener(this);

        editTextEmail = findViewById(R.id.input_email);
        editTextPassword = findViewById(R.id.input_password);
        editTextName = findViewById(R.id.input_lastname);
        editTextfirst = findViewById(R.id.input_Firstnamed);
        editTexttel = findViewById(R.id.input_tel);
        loading=findViewById(R.id.loading);
        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

    }


    private void Register(){



        final String firstname=this.editTextName.getText().toString().trim();

        final String lastname=this.editTextfirst.getText().toString().trim();

        final String email=this.editTextEmail.getText().toString().trim();

        final String phone_number=this.editTexttel.getText().toString().trim();

        final String password=this.editTextPassword.getText().toString().trim();


        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("email", email);
            jsonBodyObj.put("firstname", firstname);
            jsonBodyObj.put("lastname", lastname);
            jsonBodyObj.put("phone_number", phone_number);
            jsonBodyObj.put("password", password);
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();

        StringRequest Stringrequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.print(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String isCreated =  jsonObject.getString("isCreated");
                            if (isCreated.equals("true")){
                                Toast.makeText(RegisterActivity.this,"Register Success",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this,"Register already registred !",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this,"Register Error" + e.toString() ,Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.GONE);
                            register.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"ister Error" + error.toString() ,Toast.LENGTH_LONG).show();
                loading.setVisibility(View.GONE);
                register.setVisibility(View.VISIBLE);
            }
        })

        {
            @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>params =new HashMap<>();
                    params.put("firstname",firstname);
                    params.put("lastname",lastname);
                    params.put("email",email);
                    params.put("phone_number",phone_number);
                    params.put("password",password);
                return super.getParams();
            }
            public Map getHeaders() throws AuthFailureError
            {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-Key", "eiWee8ep9due4deeshoa8Peichai8Eih");
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

            private boolean userSignUp() {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String name = editTextName.getText().toString().trim();
                String tel = editTexttel.getText().toString().trim();
                String first = editTextfirst.getText().toString().trim();

                boolean tr = true;

                if (name.length() < 1) {
                    editTextfirst.setError("First Name required");
                    editTextfirst.requestFocus();
                    tr = false;
                }

                if (name.length() < 1) {
                    editTextName.setError("Last Name required");
                    editTextName.requestFocus();
                    tr = false;
                }


                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    tr = false;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError("Enter a valid email");
                    editTextEmail.requestFocus();
                    tr = false;
                }


                if (tel.length() != 8) {
                    editTexttel.setError("Tel required");
                    editTexttel.requestFocus();
                    tr = false;
                }


                if (password.isEmpty()) {
                    editTextPassword.setError("Password required");
                    editTextPassword.requestFocus();
                    tr = false;
                }

                if (password.length() < 6) {
                    editTextPassword.setError("Password should be atleast 6 character long");
                    editTextPassword.requestFocus();
                    tr = false;
                }
                return tr;

            }


            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.link_login:

                        Intent intentMain = new Intent(RegisterActivity.this,
                                LoginActivity.class);
                        RegisterActivity.this.startActivity(intentMain);
                        break;

                    case R.id.btn_signup:
                        if (userSignUp()) {
                            Register();
                        }
                        break;
                    case R.id.back:
                        onBackPressed();
                        break;
                    default:

                }
            }
        }