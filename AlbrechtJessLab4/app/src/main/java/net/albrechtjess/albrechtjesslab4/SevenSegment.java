package net.albrechtjess.albrechtjesslab4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jess on 4/21/2016.
 */
public class SevenSegment extends View implements View.OnClickListener{
    private int currentSegmentNumber;
    private boolean[][] sevenSegmentsArray;
    int w, h;
    public SevenSegment(Context context) {
        super(context);
        initialize(context);
    }

    public SevenSegment(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public SevenSegment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public int getCurrentSegmentNumber(){return this.currentSegmentNumber;}
    public void setCurrentSegmentNumber(int i){this.currentSegmentNumber = i;
       this.invalidate();
    }

    private void initialize(Context context)
    {
        this.sevenSegmentsArray = new boolean[][]{
            // { top, top right, bottom right, bottom, bottom left, top left, middle }
            {true, true, true, true, true, true, false},//0
            {false, true, true, false, false, false, false},//1
            {true, true, false, true, true, false, true},//2
            {true, true, true, true, false, false, true},//3
            {false, true, true, false, false, true, true},//4
            {true, false, true, true, false, true, true},//5
            {true, false, true, true, true, true, true},//6
            {true, true, true, false, false, false, false},//7
            {true, true, true, true, true, true, true},//8
            {true, true, true, true, false, true, true},//9
            {false, false, false, false, false, false, false} //off

        };
        this.currentSegmentNumber = 10;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int allocatedwidth = MeasureSpec.getSize(widthMeasureSpec),
            allocatedheight = MeasureSpec.getSize(heightMeasureSpec),
            aspect_ratio = 16/9,
            potentialHeight = allocatedwidth/aspect_ratio,
            potentialWidth = allocatedheight * aspect_ratio;
        if(potentialHeight > allocatedheight)potentialHeight = allocatedheight;
        if(potentialWidth > allocatedwidth)potentialWidth = allocatedwidth;

        setMeasuredDimension(potentialWidth,potentialHeight);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }


    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        setLayerType(this.LAYER_TYPE_SOFTWARE, null) ;
        float xDesignDimension = 14;
        float yDesignDimension = 24;
        float xScaleFactor = this.w / xDesignDimension;
        float yScaleFactor = this.h / yDesignDimension;
        boolean[] segmentConfig = this.sevenSegmentsArray[this.getCurrentSegmentNumber()];
        int on = Color.rgb(255,0,0); // or Color.RED
        int off = Color.rgb(76,0,0);
        float coordinates[][] = new float[6][2];
            coordinates[0][0] = 2;
            coordinates[0][1] = 2;
            coordinates[1][0] = 3;
            coordinates[1][1] = 1;
            coordinates[2][0] = 11;
            coordinates[2][1] = 1;
            coordinates[3][0] = 12;
            coordinates[3][1] = 2;
            coordinates[4][0] = 11;
            coordinates[4][1] = 3;
            coordinates[5][0] = 3;
            coordinates[5][1] = 3;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL) ;
        Path path = new Path() ;
        path.moveTo(coordinates[0][0], coordinates[0][1]);
        path.lineTo(coordinates[1][0], coordinates[1][1]);
        path.lineTo(coordinates[2][0], coordinates[2][1]);
        path.lineTo(coordinates[3][0], coordinates[3][1]);
        path.lineTo(coordinates[4][0], coordinates[4][1]);
        path.lineTo(coordinates[5][0], coordinates[5][1]);
        path.close();
        canvas.scale(xScaleFactor, yScaleFactor);
        paint.setColor(segmentConfig[0] ? on : off);
        canvas.save();
        canvas.drawPath(path, paint) ;


        //top right
        canvas.restore();
        canvas.save();
        canvas.translate(14,0);
        canvas.rotate(90);
        paint.setColor(segmentConfig[1] ? on : off);
        canvas.drawPath(path, paint) ;

        //top left
        canvas.restore();
        canvas.save();
        canvas.translate(4,0);
        canvas.rotate(90);
        paint.setColor(segmentConfig[5] ? on : off);
        canvas.drawPath(path, paint) ;

        //middle
        canvas.restore();
        canvas.save();
        canvas.translate(0,10);
        paint.setColor(segmentConfig[6] ? on : off);
        canvas.drawPath(path, paint) ;

        //bottom right
        canvas.restore();
        canvas.save();
        canvas.translate(14,10);
        canvas.rotate(90);
        paint.setColor(segmentConfig[2] ? on : off);
        canvas.drawPath(path, paint) ;

        //bottom left
        canvas.restore();
        canvas.save();
        canvas.translate(4,10);
        canvas.rotate(90);
        paint.setColor(segmentConfig[4] ? on : off);
        canvas.drawPath(path, paint) ;

        //bottom
        canvas.restore();
        canvas.save();
        canvas.translate(0,20);
        paint.setColor(segmentConfig[3] ? on : off);
        canvas.drawPath(path, paint) ;
        //*/
    }

    @Override
    public void onClick(View v) {
        int currentNumber = this.getCurrentSegmentNumber();
        if(currentNumber%10 != 0){
            this.setCurrentSegmentNumber(currentNumber+1);
        }
        else {
            this.setCurrentSegmentNumber(0);
        }
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.setCurrentSegmentNumber(bundle.getInt("segment"));
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected Parcelable onSaveInstanceState()
    {
        Bundle segmentBundle = new Bundle();
        segmentBundle.putParcelable("instanceState", super.onSaveInstanceState());
        segmentBundle.putInt("segment", this.currentSegmentNumber);
        return segmentBundle;
    }
}
