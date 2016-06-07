package net.albrechtjess.albrechtjessmisslecommand;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Jess on 5/17/2016.
 */
public class GameView extends View implements MediaPlayer.OnCompletionListener {
    private Context context;
    private static final int TIMER_MSEC = 70; // or whatever
    protected boolean m_isRunning = false;
    private Handler m_handler;
    private Runnable m_timer;

    MediaPlayer mpExplosion, mpLaunch, mpGameOver;
    int mClipID;

    int mScreenW, mScreenH; // actual screen dimensions
    float mDimX = 600, mDimY = 800; // logical dimensions
    float xScaleFactor,yScaleFactor;

    boolean gameOver = false;
    float missileSpeed = 25, scudSpeed = 5, explosionSpeed = 2, explosionRadiusMax = 50;
    int scudsPerRound = 20, remainingScuds = scudsPerRound, deadScuds = 0,
        missilesPerRound = 30, remainingMissiles = missilesPerRound,
        timePerScud = 30,
        score = 0, level = 1;

    ArrayList<Missile> missileQ = new ArrayList<>();

    protected class Missile{
        float targetX, targetY, x, y, originX, originY, xVel, yVel, radius;
        boolean explode = false, exploded = false, scud = false, hitTurret = false, implosion = false;
        Missile(float x, float y)
        {
            x /= xScaleFactor; y /= yScaleFactor;
            this.targetX = x; this.targetY = y;
            this.originX = 45; this.originY = mDimY-50;
            this.y = this.originY;
            this.x = this.originX;
            float distY = this.targetY - this.originY, distX = this.targetX - this.originX, mag = (float)Math.sqrt((distX*distX) + (distY*distY));
            this.xVel = (distX/mag) * missileSpeed;
            this.yVel = (distY/mag) * missileSpeed;
            this.radius = 5f;
        }

        //scudd constructor
        Missile(float x, float y, float originX, float originY)
        {
            this.scud = true;
            this.targetX = x; this.targetY = y;
            this.originX = originX; this.originY = originY;
            this.y = this.originY;
            this.x = this.originX;
            float distY = this.targetY - this.originY, distX = this.targetX - this.originX, mag = (float)Math.sqrt((distX*distX) + (distY*distY));
            this.xVel = (distX/mag) * scudSpeed;
            this.yVel = (distY/mag) * scudSpeed;
            this.radius = 5f;
        }

        public void Move(){

            if(this.x > mDimX || this.y > mDimY || this.x < 0 || this.y < 0 || (this.scud && this.y > targetY) || (!this.scud && this.y < targetY))
            {
                if(!this.explode){
                    this.explode = true;
                    playClip(mpExplosion, R.raw.explosion);
                }
                //draw explosion
            }
            else
            {
                this.x += this.xVel;
                this.y += this.yVel;
            }
        }

        public void changeRadius()
        {
            if(this.radius > explosionRadiusMax || this.implosion == true)
            {
                this.implosion = true;
                this.radius -= explosionSpeed;
            }
            else if(this.implosion == false){
                this.radius += explosionSpeed;
            }
            if(this.radius < 0)
            {
                this.exploded = true;
            }
        }

        public void drawMissile(Canvas canvas, Paint paint)
        {
            paint.setColor(Color.BLACK);
            canvas.drawLine(this.originX, this.originY,this.x, this.y,paint);

            //pointer
            if(!scud) {
                paint.setColor(Color.RED);
                canvas.drawLine(this.targetX - 10, this.targetY, this.targetX + 10, this.targetY, paint);
                canvas.drawLine(this.targetX, this.targetY + 10, this.targetX, this.targetY - 10, paint);
            }
        }
    }

    protected void addMissile( float x, float y)
    {
        if(remainingMissiles > 0){
            missileQ.add(new Missile(x,y));
            remainingMissiles--;
            playClip(mpLaunch, R.raw.missile);
        }
    }

    public GameView(Context context) {
        super(context);
        this.context = context;
        commonInit();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        commonInit();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        commonInit();
    }

    public void setPreferences(int scudSpeed, int scudsPerRound, int missileSpeed,
                               int explosionSpeed, int explosionRadiusMax, int missilesPerRound, int timePerScud)
    {
       //set preferences here from mainActivity
        this.scudSpeed = scudSpeed;
        this.scudsPerRound = scudsPerRound;
        this.remainingScuds = scudsPerRound;
        this.missileSpeed = missileSpeed;
        this.explosionSpeed = explosionSpeed;
        this.explosionRadiusMax = explosionRadiusMax;
        this.missilesPerRound = missilesPerRound;
        this.remainingMissiles = missilesPerRound;
        this.timePerScud = timePerScud;
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

    int timerCount = 0; Random rand = new Random();
    protected void onTimer() {
// Do your physics update here. If it depends on time, use
// TIMER_MSEC as an approx for deltaT or make calls to
// System.currentTimeMillis()
// schedule a full or partial redraws as appropriate, e.g.:

        if(cities.size() == 0 && gameOver == false)
        {
            gameOver = true;
            playClip(mpGameOver,R.raw.levelup);
            this.pauseGame();
            final TextView gameOver = (TextView)((Activity)context).findViewById(R.id.gameOverText);
            gameOver.setVisibility(View.VISIBLE);
            Button startStopBtn = (Button) ((Activity)context).findViewById(R.id.startStopBtn);
            startStopBtn.setText("Try Again?");
            startStopBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    gameOver.setVisibility(View.INVISIBLE);
                    ((Activity)context).recreate();
                }
            });
        }
        //game has been won
        else if(deadScuds == scudsPerRound && cities.size() > 0)
        {
            score += ((remainingMissiles * 40) + (cities.size()*100));
            playClip(mpGameOver,R.raw.levelup);
            deadScuds = 0;
            scudsPerRound += 5;
            remainingScuds = scudsPerRound;
            missilesPerRound = 30;
            remainingMissiles = missilesPerRound;
            scudSpeed += 1;
            level++;
            Button startStopBtn = (Button) ((Activity)context).findViewById(R.id.startStopBtn);
            startStopBtn.setText("Start");
            pauseGame();
        }
        //update missile travel
        for (Iterator<Missile> it = missileQ.iterator(); it.hasNext(); ) {
            Missile cur = it.next();
            if(cur.exploded) {
                if(cur.scud)
                {
                    deadScuds++;
                    score += 20;
                }
                //Log.wtf("Debug", "DeadScuds: " + deadScuds);
                it.remove();
                //replace with explosion :D
            }
            else if(cur.explode)
            {
                cur.changeRadius();
            }
            else
            {
                cur.Move();
            }
        }

        //scud launcher
        timerCount++;
        if(timerCount > timePerScud)
        {
            timerCount = 0;
            if(remainingScuds > 0){
                missileQ.add(new Missile(rand.nextInt(((int)mDimX) + 1),mDimY,rand.nextInt(((int)mDimX) + 1),0));
                remainingScuds--;
            }
        }

        //udpate Menu
        //http://stackoverflow.com/questions/10996479/how-to-update-a-textview-of-an-activity-from-another-class
        TextView score = (TextView)((Activity)context).findViewById(R.id.score);
        score.setText("Score: " + this.score);
        TextView missiles = (TextView)((Activity)context).findViewById(R.id.numMissiles);
        missiles.setText("Missiles: " + this.remainingMissiles);
        TextView scuds = (TextView)((Activity)context).findViewById(R.id.numScuds);
        scuds.setText("Scuds: " + this.remainingScuds);
        TextView level = (TextView)((Activity)context).findViewById(R.id.levelNum);
        level.setText("Level: " + this.level);

        invalidate();
    }


    int citySpacing = 50;
    RectF   turretRec = new RectF(0,mDimY-50,50,mDimY),
            city1Rec = new RectF(75, mDimY-50, 125, mDimY),
            city2Rec = new RectF(165, mDimY-50, 215, mDimY),
            city3Rec = new RectF(255, mDimY-50, 305, mDimY),
            city4Rec = new RectF(345, mDimY-50, 395, mDimY),
            city5Rec = new RectF(435, mDimY-50, 485, mDimY),
            city6Rec = new RectF(525, mDimY-50, 575, mDimY);

    int[] cityXLocations = {25, 100, 190, 280, 370, 460, 550};

    Bitmap cityPic = BitmapFactory.decodeResource(getResources(),R.drawable.city2);
    Bitmap destroyedCityPic = BitmapFactory.decodeResource(getResources(),R.drawable.destroyed3);
    Bitmap turretPic = BitmapFactory.decodeResource(getResources(),R.drawable.turret);

    ArrayList<RectF> cities = new ArrayList<>(Arrays.asList(city1Rec,city2Rec,city3Rec,city4Rec,city5Rec,city6Rec));
    ArrayList<RectF> destroyedCities = new ArrayList<>();
    ArrayList<Missile> missileTurretList = new ArrayList<>();
    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.scale(xScaleFactor, yScaleFactor);

        //draw turret
        canvas.drawBitmap(turretPic,null,turretRec,paint);

        //draw alive cities
        for (Iterator<RectF> cityIt = cities.iterator(); cityIt.hasNext(); ) {
            RectF city = cityIt.next();
            canvas.drawBitmap(cityPic,null,city,paint);
        }
        //draw destroyed cities
        for (Iterator<RectF> cityIt = destroyedCities.iterator(); cityIt.hasNext(); ) {
            RectF city = cityIt.next();
            canvas.drawBitmap(destroyedCityPic,null,city,paint);
        }
        //check if destroyed
        //canvas.drawBitmap(turretPic,null, turretRec, paint);
        /*
        canvas.drawBitmap(cityPic,null, city1Rec, paint);
        canvas.drawBitmap(cityPic,null, city2Rec, paint);
        canvas.drawBitmap(cityPic,null, city3Rec, paint);
        canvas.drawBitmap(cityPic,null, city4Rec, paint);
        canvas.drawBitmap(cityPic,null, city5Rec, paint);
        canvas.drawBitmap(cityPic,null, city6Rec, paint);
         */

        //fire missile
        //possible queue of firing missles with x's and y's
        for (Iterator<Missile> it = missileQ.iterator(); it.hasNext(); ) {
            Missile cur = it.next();

            //check collisions
            for (Iterator<Missile> it2 = missileQ.iterator(); it2.hasNext(); ) {
                Missile other = it2.next();
                if(other != cur) {
                    //checks for collisions of circles
                    //http://stackoverflow.com/questions/8367512/algorithm-to-detect-if-a-circles-intersect-with-any-other-circle-in-the-same-pla
                    if (Math.pow((cur.radius - other.radius), 2) <= Math.pow((cur.x - other.x), 2) + Math.pow((cur.y - other.y), 2)
                            && Math.pow((cur.x - other.x), 2) + Math.pow((cur.y - other.y), 2) <= Math.pow((cur.radius + other.radius), 2)) {
                        if(!cur.explode || !other.explode) {
                            cur.explode = true;
                            other.explode = true;
                            playClip(mpExplosion, R.raw.explosion);
                        }
                    }
                }
            }

            //check city collisions
            for (Iterator<RectF> cityIt = cities.iterator(); cityIt.hasNext(); ) {
                RectF city = cityIt.next();
                //main idea from http://stackoverflow.com/questions/9333882/circle-and-rectangle-collision-android
                if(Math.abs(cur.y-city.top) < cur.radius || Math.abs(cur.y-city.bottom) < cur.radius)//
                {
                    if(Math.abs(cur.x-city.left) < cur.radius || Math.abs(cur.x-city.right) < cur.radius)
                    {
                        destroyedCities.add(city);
                        cur.explode = true;
                        playClip(mpExplosion, R.raw.explosion);
                        cityIt.remove();
                        score -= 100;
                    }
                }
            }

            //turret collision keeps a list of missiles that have hit turret to eliminate multiple reductions
            if(Math.abs(cur.y-turretRec.top) < cur.radius || Math.abs(cur.y-turretRec.bottom) < cur.radius)//
            {
                if(Math.abs(cur.x-turretRec.left) < cur.radius || Math.abs(cur.x-turretRec.right) < cur.radius)
                {
                    if(cur.hitTurret == false && cur.explode == true)
                    {
                        cur.hitTurret = true;
                        remainingMissiles -= missilesPerRound/3;
                        score -= ((missilesPerRound/3)*40);
                        if(remainingMissiles < 0)remainingMissiles = 0;
                        //Log.wtf("Debug", "RemainingMissiles: " + remainingMissiles);
                    }
                }
            }

            //draw missile or explosion
            if(cur.explode == true)
            {

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.YELLOW);
                canvas.drawCircle(cur.x,cur.y,cur.radius,paint);
            }
            else{
                cur.drawMissile(canvas,paint);
            }
        }
    }



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
        pauseGame();
        return segmentBundle;
    }


    private void playClip(MediaPlayer mp,int id) {
        if (mp!=null && id==mClipID) {
            mp.pause();
            mp.seekTo(0);
            mp.start();
        }
        else {
            if (mp!=null) mp.release() ;
            mClipID = id ;
            mp = MediaPlayer.create(getContext(), id) ;
            mp.setOnCompletionListener(this) ;
            if(mClipID == R.raw.explosion)mp.setVolume(0.9f,0.9f);
            else mp.setVolume(0.4f,0.4f);
            mp.start() ;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
        mp = null;
    }
}
// other overrides may be appropriate, such as:
// onSizeChanged to set actual screen width and height
// onSaveInstanceState to store game state on config changes
// onRestoreInstanceState to restore game state
// maybe onTouchEvent for user interaction with screen
// maybe onMeasure to control your aspect ratio
