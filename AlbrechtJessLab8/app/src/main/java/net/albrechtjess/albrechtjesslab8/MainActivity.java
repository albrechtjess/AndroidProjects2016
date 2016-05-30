package net.albrechtjess.albrechtjesslab8;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView xImage = (ImageView) findViewById(R.id.xImage),
                oImage = (ImageView) findViewById(R.id.oImage),
                upperLeft = (ImageView) findViewById(R.id.upperLeft),
                upperMiddle = (ImageView) findViewById(R.id.upperMiddle),
                upperRight = (ImageView) findViewById(R.id.upperRight),
                middleLeft = (ImageView) findViewById(R.id.middleLeft),
                middleMiddle = (ImageView) findViewById(R.id.middleMiddle),
                middleRight = (ImageView) findViewById(R.id.middleRight),
                lowerLeft= (ImageView) findViewById(R.id.lowerLeft),
                lowerMiddle= (ImageView) findViewById(R.id.lowerMiddle),
                lowerRight= (ImageView) findViewById(R.id.lowerRight);

        xImage.setOnTouchListener(this);
        xImage.setTag(R.drawable.x);
        oImage.setOnTouchListener(this);
        oImage.setTag(R.drawable.o);

        if(savedInstanceState != null)
        {
            upperLeft.setOnDragListener(this);
            upperLeft.setImageResource(savedInstanceState.getInt("upperLeft"));
            upperLeft.setTag(savedInstanceState.getInt("upperLeft"));
            upperMiddle.setOnDragListener(this);
            upperMiddle.setImageResource(savedInstanceState.getInt("upperMiddle"));
            upperMiddle.setTag(savedInstanceState.getInt("upperMiddle"));
            upperRight.setOnDragListener(this);
            upperRight.setImageResource(savedInstanceState.getInt("upperRight"));
            upperRight.setTag(savedInstanceState.getInt("upperRight"));
            middleLeft.setOnDragListener(this);
            middleLeft.setImageResource(savedInstanceState.getInt("middleLeft"));
            middleLeft.setTag(savedInstanceState.getInt("middleLeft"));
            middleMiddle.setOnDragListener(this);
            middleMiddle.setImageResource(savedInstanceState.getInt("middleMiddle"));
            middleMiddle.setTag(savedInstanceState.getInt("middleMiddle"));
            middleRight.setOnDragListener(this);
            middleRight.setImageResource(savedInstanceState.getInt("middleRight"));
            middleRight.setTag(savedInstanceState.getInt("middleRight"));
            lowerLeft.setOnDragListener(this);
            lowerLeft.setImageResource(savedInstanceState.getInt("lowerLeft"));
            lowerLeft.setTag(savedInstanceState.getInt("lowerLeft"));
            lowerMiddle.setOnDragListener(this);
            lowerMiddle.setImageResource(savedInstanceState.getInt("lowerMiddle"));
            lowerMiddle.setTag(savedInstanceState.getInt("lowerMiddle"));
            lowerRight.setOnDragListener(this);
            lowerRight.setImageResource(savedInstanceState.getInt("lowerRight"));
            lowerRight.setTag(savedInstanceState.getInt("lowerRight"));
        }
        else{
            upperLeft.setOnDragListener(this);
            upperLeft.setTag(R.drawable.blank);
            upperMiddle.setOnDragListener(this);
            upperMiddle.setTag(R.drawable.blank);
            upperRight.setOnDragListener(this);
            upperRight.setTag(R.drawable.blank);
            middleLeft.setOnDragListener(this);
            middleLeft.setTag(R.drawable.blank);
            middleMiddle.setOnDragListener(this);
            middleMiddle.setTag(R.drawable.blank);
            middleRight.setOnDragListener(this);
            middleRight.setTag(R.drawable.blank);
            lowerLeft.setOnDragListener(this);
            lowerLeft.setTag(R.drawable.blank);
            lowerMiddle.setOnDragListener(this);
            lowerMiddle.setTag(R.drawable.blank);
            lowerRight.setOnDragListener(this);
            lowerRight.setTag(R.drawable.blank);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt("upperLeft", (int)findViewById(R.id.upperLeft).getTag());
        savedInstanceState.putInt("upperMiddle",(int)findViewById(R.id.upperMiddle).getTag());
        savedInstanceState.putInt("upperRight", (int)findViewById(R.id.upperRight).getTag());
        savedInstanceState.putInt("middleLeft", (int)findViewById(R.id.middleLeft).getTag());
        savedInstanceState.putInt("middleMiddle", (int)findViewById(R.id.middleMiddle).getTag());
        savedInstanceState.putInt("middleRight",(int)findViewById( R.id.middleRight).getTag());
        savedInstanceState.putInt("lowerLeft", (int)findViewById(R.id.lowerLeft).getTag());
        savedInstanceState.putInt("lowerMiddle", (int)findViewById(R.id.lowerMiddle).getTag());
        savedInstanceState.putInt("lowerRight",(int)findViewById( R.id.lowerRight).getTag());
        super.onSaveInstanceState(savedInstanceState);
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

        if (id == R.id.action_about) {
            Toast.makeText(this,
                    "Lab 8, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == event.ACTION_DOWN)
        {
            View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null,dragShadowBuilder,v, 0);

            Log.wtf("Debug", "onTouchDown");
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        Log.wtf("Debug", "onDrag");
        if(event.getAction() == event.ACTION_DROP)
        {
            if((int)v.getTag() == R.drawable.blank) {
                ImageView image = (ImageView) event.getLocalState();
                ((ImageView) v).setImageResource((int)image.getTag());
                v.setTag(image.getTag());
                Log.wtf("Debug", "onDrop");
            }
            else
            {
                Toast.makeText(this,
                        "Cannot play there, piece already present", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return true;
    }
}
