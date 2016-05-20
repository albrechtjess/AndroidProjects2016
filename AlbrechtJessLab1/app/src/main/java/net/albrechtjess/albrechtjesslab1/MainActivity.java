package net.albrechtjess.albrechtjesslab1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends TracerActivity {

    Button btnSurvey, btnWebSite;
    boolean btnSurveyToggle=false, btnWebToggle=false;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent() ;
        if (intent!=null) {
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type.equals("text/plain")) {
                TextView resultsText = (TextView) findViewById(R.id.resultsText);
                resultsText.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            }
        }

        final EditText etName = (EditText)findViewById(R.id.nameText);
        btnSurvey = (Button) findViewById(R.id.surveyButton);
        btnSurvey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = String.valueOf(etName.getText());
                if(name.equals(null) || name.equals(""))
                    Toast.makeText(MainActivity.this,"Please enter a first name", Toast.LENGTH_SHORT).show();
                else{
                    Intent startSurvey = new Intent(MainActivity.this, SurveyActivity.class);
                    startSurvey.putExtra("str_name",name);
                    startActivityForResult(startSurvey,2);
                }

            }
        });

        btnWebSite = (Button) findViewById(R.id.webButton);
        btnWebSite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri paulsite = Uri.parse("https://sites.google.com/site/pschimpf99/") ;
                Intent intent = new Intent(Intent.ACTION_VIEW, paulsite) ;
                startActivity(intent) ;
            }
        });
        //*/
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
                    "Lab 1, Spring 2016, Jess Albrecht", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode==2 && resultCode== Activity.RESULT_OK) {
            Integer submittedAge = data.getIntExtra("int_age", 0) ;
            TextView resultsText = (TextView) findViewById(R.id.resultsText);
            if(submittedAge == 0 || submittedAge >= 40)
            {
                resultsText.setText("You’re NOT under 40, so you’re NOT trustworthy");
            }
            else resultsText.setText("You’re under 40, so you’re trustworthy");
        }
        super.onActivityResult(requestCode, resultCode, data) ;
    }

}
