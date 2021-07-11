package com.example.buzz.activities

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buzz.R
import com.example.buzz.utilities.AppPreferences
import com.example.buzz.utilities.ChatMessage
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
class ChatLogActivity : AppCompatActivity() {
    lateinit var start_call: Button
    lateinit var video_call: RelativeLayout
    private var mRtcEngine: RtcEngine? = null
    lateinit var send: FloatingActionButton
    lateinit var message_edit: EditText
    lateinit var recycler_view: RecyclerView
   //var fromId = AppPreferences.id
    //var toId= AppPreferences.toid
    var database = Firebase.firestore

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {

        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread { setupRemoteVideo(uid) }
        }


        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread { onRemoteUserLeft() }
        }


        override fun onUserMuteVideo(uid: Int, muted: Boolean) {
            runOnUiThread { onRemoteUserVideoMuted(uid, muted) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        database = FirebaseFirestore.getInstance()
        send = findViewById(R.id.send_button_chat_log)
        message_edit = findViewById(R.id.edittext_chat_log)
        recycler_view = findViewById(R.id.recyclerView_chat)

        start_call = findViewById(R.id.make_video_call)
        video_call = findViewById(R.id.activity_video_chat_view)
        start_call.visibility = View.VISIBLE
        video_call.visibility = View.GONE
        //start_call.visibility=View.GONE
        display_chats()
       // randomButtonClick()
        send.setOnClickListener { send_message() }

    }






    private fun initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine()
        setupVideoProfile()
        setupLocalVideo()
        joinChannel()
    }








    override fun onDestroy() {
        super.onDestroy()

        leaveChannel()

        RtcEngine.destroy()
        mRtcEngine = null
    }

    fun onLocalVideoMuteClicked(view: View) {
        val iv = view as ImageView
        if (iv.isSelected) {
            iv.isSelected = false
            iv.clearColorFilter()
        } else {
            iv.isSelected = true
            iv.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        }


        mRtcEngine!!.muteLocalVideoStream(iv.isSelected)

        val container = findViewById(R.id.local_video_view_container) as FrameLayout
        val surfaceView = container.getChildAt(0) as SurfaceView
        surfaceView.setZOrderMediaOverlay(!iv.isSelected)
        surfaceView.visibility = if (iv.isSelected) View.GONE else View.VISIBLE
    }

    fun onLocalAudioMuteClicked(view: View) {
        val iv = view as ImageView
        if (iv.isSelected) {
            iv.isSelected = false
            iv.clearColorFilter()
        } else {
            iv.isSelected = true
            iv.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
        }

        // Stops/Resumes sending the local audio stream.
        mRtcEngine!!.muteLocalAudioStream(iv.isSelected)
    }

    fun onSwitchCameraClicked(view: View) {
        // Switches between front and rear cameras.
        mRtcEngine!!.switchCamera()
    }

    fun onEncCallClicked(view: View) {

        finish()
    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine =
                RtcEngine.create(baseContext, getString(R.string.agora_app_id), mRtcEventHandler)
        } catch (e: Exception) {
            Log.e(LOG_TAG, Log.getStackTraceString(e))

            throw RuntimeException(
                "NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(
                    e
                )
            )
        }
    }

    private fun setupVideoProfile() {

        mRtcEngine!!.enableVideo()

        mRtcEngine!!.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    private fun setupLocalVideo() {

        val container = findViewById(R.id.local_video_view_container) as FrameLayout
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        // Initializes the local video view.
        // RENDER_MODE_FIT: Uniformly scale the video until one of its dimension fits the boundary. Areas that are not filled due to the disparity in the aspect ratio are filled with black.
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    private fun joinChannel() {

        var token: String? = getString(R.string.agora_access_token)
        if (token!!.isEmpty()) {
            token = null
        }
        mRtcEngine!!.joinChannel(
            token,
            "demoChannel1",
            "Extra Optional Data",
            0
        ) // if you do not specify the uid, we will generate the uid for you
    }

    private fun setupRemoteVideo(uid: Int) {

        val container = findViewById(R.id.remote_video_view_container) as FrameLayout

        if (container.childCount >= 1) {
            return
        }


        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        container.addView(surfaceView)
        // Initializes the video view of a remote user.
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))

        surfaceView.tag = uid // for mark purpose

    }

    private fun leaveChannel() {
        mRtcEngine?.leaveChannel()
    }

    private fun onRemoteUserLeft() {
        val container = findViewById(R.id.remote_video_view_container) as FrameLayout
        container.removeAllViews()


    }

    private fun onRemoteUserVideoMuted(uid: Int, muted: Boolean) {
        val container = findViewById(R.id.remote_video_view_container) as FrameLayout

        val surfaceView = container.getChildAt(0) as SurfaceView

        val tag = surfaceView.tag
        if (tag != null && tag as Int == uid) {
            surfaceView.visibility = if (muted) View.GONE else View.VISIBLE
        }
    }




    override fun onResume() {
        super.onResume()
        randomButtonClick()

    }
    /*

    override fun onStart() {
        super.onStart()
        randomButtonClick()
    }*/

    private fun randomButtonClick() {
        start_call.setOnClickListener {
            video_call.visibility=View.VISIBLE
            start_call.visibility=View.GONE
            initAgoraEngineAndJoinChannel()
        }


    }






    private fun display_chats() {

        var database = Firebase.firestore
        var fromId = FirebaseAuth.getInstance().uid ?: return
        var toId= AppPreferences.toid

        val query: Query =
            database.collection("user-messages/$fromId/$toId").orderBy("timestamp")
        val options: FirestoreRecyclerOptions<ChatMessage> =
            FirestoreRecyclerOptions.Builder<ChatMessage>().setQuery(query, ChatMessage::class.java)
                .setLifecycleOwner(this).build()
        val adapter = object : FirestoreRecyclerAdapter<ChatMessage, ChatViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {

               // if (toId === AppPreferences.id) {
                    val view: View =
                        LayoutInflater.from(this@ChatLogActivity)
                            .inflate(R.layout.message_receive, parent, false)

                    return ChatViewHolder(view)
               /* } else {
                    val view: View =
                        LayoutInflater.from(this@ChatLogActivity)
                            .inflate(R.layout.message_sent, parent, false)

                    return ChatViewHolder(view)
                }*/
              /*  else {
                    val view: View =
                        LayoutInflater.from(this@ChatLogActivity)
                            .inflate(R.layout.empty_messages, parent, false)

                    return ChatViewHolder(view)
                }*/
            }


            override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: ChatMessage) {


                val time: TextView = holder.itemView.findViewById(R.id.from_msg_time)
                val message: TextView = holder.itemView.findViewById(R.id.textview_from_row)
                message.text= model.text
                time.text=model.timestamp.toString()


            }
        }
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)

    }

    private fun send_message() {
        val text = message_edit.text.toString()
        if (text.isEmpty()) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        var fromId = FirebaseAuth.getInstance().uid ?: return
        var toId= AppPreferences.toid
        var database = Firebase.firestore
       // var database = Firebase.firestore
        val chatMessage = ChatMessage( text, fromId, toId, System.currentTimeMillis() / 1000)
         database.collection("/user-messages/$fromId/$toId").document().set(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message:")

                //recycler_view.smoothScrollToPosition(adapter.itemCount - 1)
            }.addOnFailureListener { e ->
                 Log.w(TAG, "Error adding document", e)
             }
        database.collection("/user-messages/$toId/$fromId").document().set(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved our chat message:")
                    message_edit.text.clear()
                    //recycler_view.smoothScrollToPosition(adapter.itemCount - 1)
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }



            }
    companion object {
        private val LOG_TAG = ChatLogActivity::class.java.simpleName
        //private const val CHECK_VIDEO_TIME_OUT =15000
    }
    }


