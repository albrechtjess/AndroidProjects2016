package net.albrechtjess.albrechtjesslab4;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
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

        final SevenSegment primaryDisplay = (SevenSegment) findViewById(R.id.primaryDisplay);
        final SevenSegment display1 = (SevenSegment) findViewById(R.id.sevenSegment1);
            display1.setCurrentSegmentNumber(0);
        final SevenSegment display2 = (SevenSegment) findViewById(R.id.sevenSegment2);
            display2.setCurrentSegmentNumber(1);
        final SevenSegment display3 = (SevenSegment) findViewById(R.id.sevenSegment3);
            display3.setCurrentSegmentNumber(2);

        Button plusButton = (Button) findViewById(R.id.plusBotton);
        if(plusButton != null){
            plusButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    updateNumber(primaryDisplay);
                    updateNumber(display1);
                    updateNumber(display2);
                    updateNumber(display3);
                }
            });

        }


    }

    public void updateNumber(SevenSegment segment)
    {
        int currentNumber = segment.getCurrentSegmentNumber();
        if(currentNumber < 10){
            segment.setCurrentSegmentNumber(currentNumber+1);
        }
        else {
            segment.setCurrentSegmentNumber(0);
        }
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
                    "Lab 4, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}
