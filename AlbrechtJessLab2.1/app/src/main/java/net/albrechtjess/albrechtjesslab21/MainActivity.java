package net.albrechtjess.albrechtjesslab21;

import android.content.res.TypedArray;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null)
        {
            ImageView visibleImage = (ImageView)findViewById(R.id.imageView);
            visibleImage.setImageResource(savedInstanceState.getInt("currentImage"));
            visibleImage.setTag(savedInstanceState.getInt("currentImage"));
        }
        ListView listView = (ListView) findViewById(R.id.left_drawer);

        String[] names = getResources().getStringArray(R.array.photosArray);
        ArrayAdapter<String> imageArrayAdapter = new ArrayAdapter<String>(this, R.layout.nav_list_row, R.id.textView, names);
        listView.setAdapter(imageArrayAdapter);
        listView.setOnItemClickListener(this);

        final String drawer_hidden = getTitle().toString();
        final String drawer_exposed = "Select a Page";

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_exposed, R.string.drawer_hidden) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(drawer_hidden);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(drawer_exposed);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem aboutMe = menu.findItem(R.id.action_about);
        if(mDrawerLayout.isDrawerOpen(findViewById(R.id.left_drawer)))
        {
            aboutMe.setVisible(false);
        }
        else{
            aboutMe.setVisible(true);
        }
        return  super.onPrepareOptionsMenu(menu);
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
                    "Lab 2, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TypedArray typedImgs = getResources().obtainTypedArray(R.array.photosArray);
        Integer[] images = new Integer[typedImgs.length()] ;
        for (int i=0 ; i<typedImgs.length() ; i++)
            images[i] = typedImgs.getResourceId(i, 0) ;
        ImageView visibleImage = (ImageView)findViewById(R.id.imageView);
        visibleImage.setImageResource(images[position]);
        visibleImage.setTag(images[position]);
        typedImgs.recycle();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        ImageView visibleImage = (ImageView)findViewById(R.id.imageView);
        savedInstanceState.putInt("currentImage",(Integer)visibleImage.getTag());
        super.onSaveInstanceState(savedInstanceState);
    }

}
