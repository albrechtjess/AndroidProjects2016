package net.albrechtjess.albrechtjesslab1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent nameFromMain = getIntent();
        String name = nameFromMain.getStringExtra("str_name");
            if(name == null)name = "";
        TextView helloName = (TextView)findViewById(R.id.helloName);
            if(helloName != null)helloName.setText("Hello " + name);

        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
            if(btnSubmit == null)Toast.makeText(SurveyActivity.this, "Submit button not found", Toast.LENGTH_SHORT).show();
            else{
            final EditText etAge = (EditText) findViewById(R.id.inAge);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String inAge = etAge.getText().toString();
                    if (inAge.equals("") || inAge.equals(null)) {
                        Toast.makeText(SurveyActivity.this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
                    } else {
                        Integer age = Integer.valueOf(inAge);
                        Intent submitAge = new Intent(SurveyActivity.this, MainActivity.class);
                        submitAge.putExtra("int_age", age);
                        setResult(Activity.RESULT_OK, submitAge);
                        finish();
                    }
                }
                });
            }//end else
    }

}
