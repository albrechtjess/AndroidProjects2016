package net.albrechtjess.albrechtjesslab7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jess on 5/17/2016.
 */
public class GameView extends View {
    private static final int TIMER_MSEC = 70; // or whatever
    private boolean m_isRunning = false;
    private Handler m_handler;
    private Runnable m_timer;

    int mScreenW, mScreenH; // actual screen dimensions
    int realToLogical = 25; // 25 pixels/meter
    float mDimX = 600, mDimY = 800; // logical dimensions
    double g = 9.80665; //gravity
    double mass = 1.0; //kg
    double k = 1.5; //kg/sec^2
    double y = 0; //position
    double ypp; //acceleration
    double velocity = 0;
    double last_time;
    double current_time = System.currentTimeMillis();
    double Delta_T = 0;
    float ylogical;
    float xScaleFactor;
    float yScaleFactor;
    Integer coils = 11;
    String massShape = "Rectangle";
    Integer displacement = 0;
    // constructor

    public GameView(Context context) {
        super(context);
        commonInit();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    public void setPreferences(Double stiffness, Integer springs, Integer displacement, String shape)
    {
        coils = springs;
        massShape = shape;
        this.displacement = displacement;
        k = stiffness;
        velocity = 0;
        y = 0;
    }

    private void commonInit() {
        m_handler = new Handler();
        m_timer = new Runnable() {
            @Override
            public void run() {
                onTimer();
                if (m_isRunning) m_handler.postDelayed(this, TIMER_MSEC);
            }
        };
    }

    public void resumeGame() {
        m_isRunning = true;
        m_handler.postDelayed(m_timer, TIMER_MSEC);
    }

    public void pauseGame() {
        m_isRunning = false;
        m_handler.removeCallbacks(m_timer);
    }

    protected void onTimer() {
// Do your physics update here. If it depends on time, use
// TIMER_MSEC as an approx for deltaT or make calls to
// System.currentTimeMillis()
// schedule a full or partial redraws as appropriate, e.g.:

        ypp = g - (((y  + this.displacement) * k) / mass);
        //Log.wtf("DEBUG", "ypp: " + ypp) ;
        Delta_T = TIMER_MSEC / 1000.0;
        //Log.wtf("DEBUG", "Delta_T: " + Delta_T) ;
        velocity = velocity + (ypp * Delta_T);
        //Log.wtf("DEBUG", "velocity: " + velocity) ;
        y = y + (velocity * Delta_T);

        ylogical = (float) y * realToLogical;

        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        canvas.scale(xScaleFactor, yScaleFactor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.rgb(0, 0, 0));
        if(massShape.equals("Rectangle"))
        {
            canvas.drawRect(200f, 400f + ylogical, 400f, 500f + ylogical, paint);
        }
        else if(massShape.equals("Rounded Circle"))
        {
            RectF oval = new RectF(200f, 400f + ylogical, 400f, 500f + ylogical);
            canvas.drawOval(oval, paint);
        }
        else if(massShape.equals("Circle"))
        {
            canvas.drawCircle(300f, 400f + ylogical, 50f, paint);
        }
        else if(massShape.equals("Picture"))
        {
            RectF pic = new RectF(200f, 400f + ylogical, 400f, 500f + ylogical);
            Bitmap picture = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
            canvas.drawBitmap(picture,null, pic, paint);
        }
        float r = ((400f + ylogical) / (coils + 1));
        float top = 0.0f;
        for (int i = 0; i < coils; i++) {
            RectF rect = new RectF(280f, top, 320f, top + 2 * r);
            canvas.drawOval(rect, paint);
            top += r;
        }
    }
// do your drawing here

    // onSizeChanged to set mScreenW and mScreenH
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mScreenW = w;
        this.mScreenH = h;
        xScaleFactor = this.mScreenW / mDimX;
        yScaleFactor = this.mScreenH / mDimY;
    }

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
}
// other overrides may be appropriate, such as:
// onSizeChanged to set actual screen width and height
// onSaveInstanceState to store game state on config changes
// onRestoreInstanceState to restore game state
// maybe onTouchEvent for user interaction with screen
// maybe onMeasure to control your aspect ratio
