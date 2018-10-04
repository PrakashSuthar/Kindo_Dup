package com.example.prakash.kindo_puzz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Puzzle3 extends AppCompatActivity {
    Puzzle3_Layout puzzle3_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        puzzle3_layout=new Puzzle3_Layout(this);
        setContentView(puzzle3_layout);
        System.out.println("Acticity 3 Started");
    }

    @Override
    protected void onResume() {
        super.onResume();
        puzzle3_layout.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        puzzle3_layout.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
