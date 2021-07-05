package com.example.buzz.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.buzz.R

class DashboardActivity : AppCompatActivity() {
    lateinit var audio:CardView
    lateinit var video:CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        audio=findViewById(R.id.audio_card)
        video=findViewById(R.id.video_card)
        audio.setOnClickListener{
            val intent1= Intent(
                    this@DashboardActivity,
                    AudioRoomActivity::class.java
            )
            startActivity(intent1)
        }
        video.setOnClickListener{
            val intent2 =Intent(
                    this@DashboardActivity,
                    VideoRoomActivity::class.java
            )
            startActivity(intent2)
        }
    }
}