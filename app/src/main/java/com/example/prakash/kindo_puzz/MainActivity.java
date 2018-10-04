package com.example.prakash.kindo_puzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void callPuzzle1(View v){
        Intent intent=new Intent(this,Puzzle1.class);
        startActivity(intent);
    }
}
