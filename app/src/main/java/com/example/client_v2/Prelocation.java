package com.example.client_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Prelocation extends AppCompatActivity {

    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelocation);
        Intent intent=  getIntent();
        message=intent.getStringExtra("EXTRA_MESSAGE");
        TextView view=findViewById(R.id.location);
        view.setText(message);

    }
    public void transferlocation(View view)
    {
        Intent intent=new Intent(this, Location1.class);
        intent.putExtra("EXTRA_MESSAGE",message);
        startActivity(intent);
    }
    public void transferlocation1(View view)
    {
        Intent intent=new Intent(this, Location2.class);
        intent.putExtra("EXTRA_MESSAGE",message);
        startActivity(intent);
    }
}
