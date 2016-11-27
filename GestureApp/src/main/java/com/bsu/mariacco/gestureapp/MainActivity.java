package com.bsu.mariacco.gestureapp;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener
{
    private GestureDetectorCompat mDetector;
    private TextView tvOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetectorCompat(this,this);
        mDetector.setOnDoubleTapListener(this);
        tvOut = (TextView)findViewById(R.id.textViewShow);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        tvOut.setText("onSingleTapConfirmed(: " +  motionEvent.toString());
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        tvOut.setText("onDoubleTap: " +  motionEvent.toString());
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        tvOut.setText("onDoubleTapEvent: " +  motionEvent.toString());
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        tvOut.setText("onDown: " +  motionEvent.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        tvOut.setText("onShowPress: " +  motionEvent.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        tvOut.setText("onSingleTapUp: " +  motionEvent.toString());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        tvOut.setText("onScroll: " +  motionEvent.toString()+motionEvent1.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        tvOut.setText(" onLongPress: " +  motionEvent.toString());
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        tvOut.setText("onFling: " +  motionEvent.toString()+motionEvent1.toString());
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
