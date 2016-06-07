package net.albrechtjess.albrechtjessmisslecommand;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    // filename and parameter keys:
    private static final String SETTINGS = "SETTINGS" ;
    public static final String FIRST_USE = "FIRST_USE" ;
    public GameView gameView;
    Button startStopBtn, restartBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startStopBtn = (Button) findViewById(R.id.startStopBtn);
        restartBtn = (Button) findViewById(R.id.restartBtn);

        gameView = (GameView) findViewById(R.id.gameView);
        if(gameView != null) {
            gameView.setOnTouchListener(this);
             if(startStopBtn != null){
                 startStopBtn.setOnClickListener(new View.OnClickListener() {
                     public void onClick(View v) { //sets button text and changes game state accordingly
                         if (!gameView.m_isRunning) {
                             startStopBtn.setText("Stop");
                             gameView.resumeGame();
                         } else {
                             startStopBtn.setText("Start");
                             gameView.pauseGame();
                         }

                     }
                 });
             }
            if(restartBtn != null){//restart btn
                restartBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        gameView.pauseGame();
                        recreate();
                    }
                });
            }
        }
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

        startStopBtn = (Button) findViewById(R.id.startStopBtn);
        startStopBtn.setText("Start"); //sets button text to start since it pauses on exit

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this); //gets pref's and
        Integer scudSpeed = Integer.valueOf(pref.getString("scudSpeed","5")),
                scudsPerRound = Integer.valueOf(pref.getString("scudsPerRound", "20")),
                missileSpeed = Integer.valueOf(pref.getString("missileSpeed", "25")),
                explosionSpeed = Integer.valueOf(pref.getString("explosionSpeed", "2")),
                explosionRadius = Integer.valueOf(pref.getString("explosionRadius", "50")),
                missilesPerRound = Integer.valueOf(pref.getString("missilesPerRound", "30")),
                timePerScud = Integer.valueOf(pref.getString("timePerScud", "30"));
        gameView.setPreferences(scudSpeed, scudsPerRound, missileSpeed, explosionSpeed,
                explosionRadius, missilesPerRound, timePerScud);
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
                    "Missile Command, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == event.ACTION_DOWN && gameView.m_isRunning)
        {
            float   x = event.getAxisValue(MotionEvent.AXIS_X),
                    y = event.getAxisValue(MotionEvent.AXIS_Y);
            gameView.addMissile(x, y);

            return true;
        }
        return false;
    }
}

