package com.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        happyButton.setOnClickListener {
            emotionalFaceView.happinessState = EmotionalFaceView.HAPPY
        }
        sadButton.setOnClickListener {
            emotionalFaceView.happinessState = EmotionalFaceView.SAD
        }
    }
}
