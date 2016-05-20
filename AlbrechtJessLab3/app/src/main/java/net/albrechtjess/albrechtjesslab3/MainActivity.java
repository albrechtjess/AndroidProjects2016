package net.albrechtjess.albrechtjesslab3;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {

    ArrayList<Manufacturer>  collection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collection = new ArrayList<>();
        if(savedInstanceState == null){
            if(!this.parseFile("makesModels.txt")){
                Toast.makeText(MainActivity.this,"File Not Parsed", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            collection = new ArrayList<>((ArrayList<Manufacturer>)savedInstanceState.getSerializable("currentCol"));
        }
        MyListAdapter adapter = new MyListAdapter(this, collection);
        ExpandableListView list = (ExpandableListView) findViewById(R.id.expandableListView);
        list.setAdapter(adapter);
        list.setOnChildClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Toast.makeText(this,
                    "Lab 3, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putSerializable("currentCol", collection);
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean parseFile(String filename){
        AssetManager manager =  this.getResources().getAssets();
        try {
            Scanner inf = new Scanner(manager.open(filename));
            int fileIndex = 0;
            while (inf.hasNextLine()) {
                String curLine = inf.nextLine();
                String[] curLineSplit = curLine.split(",");
                Manufacturer currentMan = new Manufacturer(curLineSplit[0]);
                for (int x = 1; x < curLineSplit.length; x++) {
                    currentMan.addModel(curLineSplit[x]);
                }
                collection.add(currentMan);
            }
            return true;
        }
        catch (Exception e)
        {
            Log.d("Exception", e.getMessage());
            return false;
        }
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Toast.makeText(MainActivity.this,collection.get(groupPosition).getName() + "," + collection.get(groupPosition).getModelNameatPosition(childPosition), Toast.LENGTH_SHORT).show();
        return true;
    }
}
