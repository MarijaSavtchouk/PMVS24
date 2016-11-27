package com.bsu.mariacco.gesturerecognizerapp;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {
    private GestureLibrary gLib;
    private GestureOverlayView gestures;
    private TextView tvOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLib.load()) {
            finish();
        }
        gestures = (GestureOverlayView) findViewById(R.id.gestureView);
        gestures.addOnGesturePerformedListener(this);
        tvOut = (TextView)findViewById(R.id.textView);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gLib.recognize(gesture);
        if (predictions.size() > 0)
        {
            Prediction prediction = predictions.get(0);
            if (prediction.score > 1.0) {
                if (prediction.name.equals(getString(R.string.one)))
                    tvOut.setText("1");
                else if (prediction.name.equals(getString(R.string.stop)))
                    tvOut.setText(R.string.stop);
                else if (prediction.name.equals(getString(R.string.two)))
                    tvOut.setText("2");
            } else {
                tvOut.setText(getString(R.string.Unrecognized));
            }
        }
    }
}
