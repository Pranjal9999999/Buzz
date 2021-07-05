package com.example.buzz.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.buzz.R
import com.example.buzz.utilities.AppPreferences
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.net.MalformedURLException
import java.net.URL

class AudioRoomActivity : AppCompatActivity() {
    lateinit var code: EditText
    lateinit var join: Button
    lateinit var name:EditText
    lateinit var progress_bar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_creator)
        progress_bar=findViewById(R.id.JoinProgressBar)
        progress_bar.visibility=View.GONE
        code=findViewById(R.id.meeting_code)
        join=findViewById(R.id.join)
        name=findViewById(R.id.name_notsignedup_user)
        if(!AppPreferences.isLogin)
            name.visibility= View.VISIBLE
        else
            name.visibility=View.GONE


        val url_server: URL
        try { url_server= URL("https://meet.jit.si")
            var   defaultOptions: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder().
            setServerURL(url_server).setWelcomePageEnabled(false).build()
            JitsiMeet.setDefaultConferenceOptions(defaultOptions)
        }
        catch (e: MalformedURLException)
        {
            e.printStackTrace();
        }

        join.setOnClickListener{
            progress_bar.visibility= View.VISIBLE

            var options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
                .setRoom(code.text.toString()).setAudioOnly(true).setWelcomePageEnabled(false).build()
            JitsiMeetActivity.launch(this@AudioRoomActivity,options)
            finish()

        }
    }
}