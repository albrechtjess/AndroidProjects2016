package net.albrechtjess.albrechtjesslab1;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Iterator;
import java.util.Set;

public class TracerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null)notify("onCreate NO state");
        else
        {
            notify("onCreate WITH state");
            Set<String> keys = savedInstanceState.keySet() ;
            Iterator iter = keys.iterator();
            while (iter.hasNext())
                notify("key:" + (String)iter.next()) ;
        }
        setContentView(R.layout.activity_tracer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        notify("onPause Method");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        notify("onRestart Method");

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        notify("onStart Method");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        notify("onSavedInstanceState Method");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        notify("onStop Method");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        notify("onActivityResult Method");
    }
    private void notify(String msg) {
        String strClass = this.getClass().getName() ;
        String[] strings = strClass.split("\\.") ;
        Notification.Builder notibuild = new Notification.Builder(this) ;
        notibuild.setContentTitle(msg) ;
        notibuild.setAutoCancel(true).setSmallIcon(R.mipmap.ic_launcher) ;
        notibuild.setContentText(strings[strings.length - 1]) ;
        Notification noti = notibuild.build() ;
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE) ;
        notificationManager.notify((int) System.currentTimeMillis(), noti) ;
    }


}
