package net.albrechtjess.albrechtjesslab5;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Button startStopBtn = (Button) findViewById(R.id.startStopBtn);
        //if(savedInstanceState != null){startStopBtn.setText("Stop");}
        final GameView gameView = (GameView) findViewById(R.id.gameView);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Toast.makeText(this,
                    "Lab 5, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        GameView gameView = (GameView) findViewById(R.id.gameView);
        gameView.pauseGame();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        GameView gameView = (GameView) findViewById(R.id.gameView);
        gameView.resumeGame();
    }
}
