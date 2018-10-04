package com.example.prakash.kindo_puzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Puzzle1 extends AppCompatActivity {
    Puzzle1_Layout puzzle1_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        puzzle1_layout=new Puzzle1_Layout(this);
        setContentView(puzzle1_layout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        puzzle1_layout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        puzzle1_layout.resume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
