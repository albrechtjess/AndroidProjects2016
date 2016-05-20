package net.albrechtjess.albrechtjesslab5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Button;

/**
 * Created by Jess on 5/3/2016.
 */
public class GameView extends SurfaceView implements Runnable {
    Thread myThread = null;
    volatile boolean mIsRunning = false; // for pausing and starting
    int mScreenW, mScreenH; // actual screen dimensions
    int realToLogical = 25; // 25 pixels/meter
    float mDimX = 600, mDimY = 800; // logical dimensions
    double g = 9.80665; //gravity
    double mass = 1.0; //kg
    double k = 1.5; //kg/sec^2
    double y = 0; //position
    double ypp; //acceleration
    double velocity = 0;
    double last_time ;
    double current_time = System.currentTimeMillis();
    double Delta_T = 0;
    float ylogical;
    // constructor
    public GameView(Context context) {
        super(context);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void run() {
        last_time = System.currentTimeMillis();
        while (mIsRunning) {
            float xScaleFactor = this.mScreenW / mDimX;
            float yScaleFactor = this.mScreenH / mDimY;
            // Do your physics update here. Can make calls to
            // System.currentTimeMillis() if it needs to be
            //curr_time = system.currenttimemils
            //Delta_T = (current -last)*1000
            //last_time = current_time
            // based on elapsed time
            ypp = g - ((y * k)/mass);
            //Log.wtf("DEBUG", "ypp: " + ypp) ;
            current_time = System.currentTimeMillis();
            Delta_T = ( current_time - last_time)/1000;
            last_time = current_time;
            //Log.wtf("DEBUG", "Delta_T: " + Delta_T) ;
            velocity = velocity + (ypp * Delta_T);
            //Log.wtf("DEBUG", "velocity: " + velocity) ;
            y = y + (velocity * Delta_T);

            ylogical = (float)y*realToLogical;

            if (getHolder().getSurface().isValid()) {
                Canvas canvas = getHolder().lockCanvas();
                // Do your drawing on that canvas here
                Paint paint = new Paint();
                canvas.scale(xScaleFactor, yScaleFactor);
                //drawRect
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.rgb(255,255,255));
                canvas.drawRect(0,0,mScreenW, mScreenH, paint);

                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.rgb(0,0,0));
                canvas.drawRect(200f,400f + ylogical ,400f,500f + ylogical ,paint);
                float r = (400f + ylogical)/9;
                float top = 0.0f;
                for(int i = 0; i < 8; i++)
                {
                    RectF rect = new RectF(280f, top , 320f, top + 2*r);
                    canvas.drawOval(rect, paint);
                    top+= r;
                }


                getHolder().unlockCanvasAndPost(canvas);
            }
        }
        //sleep(100) <- delta_T
        //myThread.sleep();
    }



    public void pauseGame() {
        mIsRunning = false;
        try { // kill thread and block until it is done
            myThread.join();
        } catch (InterruptedException e) {
            Log.e("ERROR", "On Killing Thread");
        }
    }

    public void resumeGame() {
        mIsRunning = true;
        myThread = new Thread(this); // this points to the run method
        myThread.start();
        //
    }

    // other overrides are likely appropriate, such as:
// onSizeChanged to set mScreenW and mScreenH
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mScreenW = w;
        this.mScreenH = h;
    }
// onSaveInstanceState to store game state on config changes

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle segmentBundle = new Bundle();
        segmentBundle.putParcelable("instanceState", super.onSaveInstanceState());
        return segmentBundle;
    }

    // maybe onTouchEvent for user interaction with screen
// maybe onMeasure to control your aspect ratio
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float allocatedwidth = MeasureSpec.getSize(widthMeasureSpec),
            allocatedheight = MeasureSpec.getSize(heightMeasureSpec),
            aspect_ratio =  800.0f / 600.0f,
            potentialHeight = allocatedwidth / aspect_ratio,
            potentialWidth = allocatedheight * aspect_ratio;
        if (potentialHeight > allocatedheight) potentialHeight = allocatedheight;
        if (potentialWidth > allocatedwidth) potentialWidth = allocatedwidth;

        setMeasuredDimension((int)potentialWidth, (int)potentialHeight);
    }
}