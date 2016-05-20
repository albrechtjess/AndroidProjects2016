package net.albrechtjess.albrechtjesslab6;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainFragment.CallBackInterface {
    String mTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!inLandScape())
        {
            if(savedInstanceState != null)
            {
                mTime = savedInstanceState.getString("currentTime");
                if(getSupportFragmentManager().findFragmentById(R.id.portHolder) == null) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.portHolder, new MainFragment())
                            .commit();
                }
                else
                {
                    DetailFragment detailFragment;
                    detailFragment =(DetailFragment) getSupportFragmentManager().findFragmentByTag("DETAIL_FRAGMENT");
                    if(detailFragment != null)
                    {
                        detailFragment.setTextView(savedInstanceState.getString("currentTime"));
                    }
                }
            }
            else
            {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.portHolder, new MainFragment())
                        .commit();
            }
        }

        else if (savedInstanceState != null){
            mTime = savedInstanceState.getString("currentTime");
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailHolder);
            detailFragment.setTextView(savedInstanceState.getString("currentTime"));
        }
        /*
        if (savedInstanceState == null) {
            Log.wtf("DEBUG", "OnCreate saveInstance is Null");
            if(!inLandScape()){
                if(getSupportFragmentManager().findFragmentById(R.id.portHolder) == null) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.portHolder, new MainFragment())
                            .commit();
                }
            }

        }

        else{
            mTime = savedInstanceState.getString("currentTime");
            DetailFragment detailFragment;
            if(inLandScape()){
                detailFragment =(DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailHolder);
                detailFragment.setTextView(savedInstanceState.getString("currentTime"));
            }
            else {
                detailFragment =(DetailFragment) getSupportFragmentManager().findFragmentByTag("DETAIL_FRAGMENT");
                if(detailFragment != null)
                {
                    detailFragment.setTextView(savedInstanceState.getString("currentTime"));
                }
            }
        }
        */

    }

    public boolean inLandScape()
    {
        return getResources().getBoolean(R.bool.dual_pane);
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
                    "Lab 6, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void callBack(String string) {
        DetailFragment detailFragment;
        mTime = string;
        if(inLandScape()) {
            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailHolder);
            detailFragment.setTextView(string);
        }
        else{
            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag("DETAIL_FRAGMENT");
            if(detailFragment != null)
            {
                Log.wtf("DEBUG", "Callback existing detail fragment was found");
                detailFragment.setTextView(string);
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.portHolder, detailFragment.newInstance(string),"DETAIL_FRAGMENT")
                        .commit();

                Log.wtf("DEBUG", "Callback new fragmant instantiated");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        Log.wtf("DEBUG", "saveInstanceState");
        savedInstanceState.putString("currentTime",mTime);
        super.onSaveInstanceState(savedInstanceState);
    }
}
