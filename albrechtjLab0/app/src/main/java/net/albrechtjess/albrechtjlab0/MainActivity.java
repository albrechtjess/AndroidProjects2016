package net.albrechtjess.albrechtjlab0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.Snackbar ;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        ///*
        if (id == R.id.action_about) {
            Toast.makeText(this,
                    "Lab 0, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
            return true;
        }
        //*/
        /*
        View parentLayout = findViewById(R.id.rootLayout);
        Snackbar.make(parentLayout, "Lab 0, Fall 2015, Paul H Schimpf",
                Snackbar.LENGTH_SHORT).show();
        //*/
        return super.onOptionsItemSelected(item);
    }


}
