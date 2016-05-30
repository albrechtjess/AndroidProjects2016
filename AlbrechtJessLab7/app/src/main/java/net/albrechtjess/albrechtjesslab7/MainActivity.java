package net.albrechtjess.albrechtjesslab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // filename and parameter keys:
    private static final String SETTINGS = "SETTINGS" ;
    public static final String FIRST_USE = "FIRST_USE" ;
    public GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Button startStopBtn = (Button) findViewById(R.id.startStopBtn);

        /*
        SharedPreferences prefs = getSharedPreferences(SETTINGS,
                Context.MODE_PRIVATE) ;
        PreferenceManager.getDefaultSharedPreferences(SETTINGS,
                Context.MODE_PRIVATE);
        // default value if never been set is true:
        boolean firstUse = prefs.getBoolean(FIRST_USE, true) ;
        if (firstUse) {
            // say hello or do something special for the first run, then ...
            prefs.edit().putBoolean(FIRST_USE,false).commit() ;
        }
        */
        //if(savedInstanceState != null){startStopBtn.setText("Stop");}
        gameView = (GameView) findViewById(R.id.gameView);
        startStopBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(startStopBtn.getText().equals("Start"))
                {
                    startStopBtn.setText("Stop");
                    gameView.resumeGame();
                }
                else
                {
                    startStopBtn.setText("Start");
                    gameView.pauseGame();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Double springStiffness = Double.parseDouble(pref.getString("springStiffness", "1.5"));
        Integer displacement = pref.getInt("displacement", 11),
                numCoils = pref.getInt("numCoils", 11);
        String shape = pref.getString("massShape", "Rectangle");
        gameView.setPreferences(springStiffness, numCoils, displacement, shape);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class );
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.action_about) {
            Toast.makeText(this,
                    "Lab 7, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
