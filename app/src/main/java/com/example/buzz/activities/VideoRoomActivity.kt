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

class VideoRoomActivity : AppCompatActivity() {
    lateinit var code: EditText
    lateinit var join: Button
    lateinit var name: EditText
    lateinit var progress_bar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_creator)
        code=findViewById(R.id.meeting_code)
        join=findViewById(R.id.join)
        name=findViewById(R.id.name_notsignedup_user)
        progress_bar=findViewById(R.id.JoinProgressBar)
        if(!AppPreferences.isLogin)
            name.visibility= View.VISIBLE
        else
            name.visibility= View.GONE


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
                .setRoom(code.text.toString()).setWelcomePageEnabled(false).build()
            JitsiMeetActivity.launch(this@VideoRoomActivity,options)
            finish()

        }
    }
}
