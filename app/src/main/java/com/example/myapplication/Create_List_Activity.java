package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Create_List_Activity extends AppCompatActivity implements View.OnClickListener {
    EditText namelist;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__list);
        namelist=(EditText) findViewById(R.id.List_name);
        create=(Button) findViewById(R.id.button_create);
        create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
