package com.example.prakash.kindo_puzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Puzzle2 extends AppCompatActivity {
    Puzzle2_Layout puzzle2_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        puzzle2_layout=new Puzzle2_Layout(this);
        setContentView(puzzle2_layout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        puzzle2_layout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        puzzle2_layout.resume();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
