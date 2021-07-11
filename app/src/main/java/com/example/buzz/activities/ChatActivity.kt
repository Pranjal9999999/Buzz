package com.example.buzz.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.buzz.R
import com.example.buzz.utilities.AppPreferences
import com.example.buzz.utilities.User
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
class ChatActivity : AppCompatActivity() {
    lateinit var log_out: ImageView
    lateinit var group_call: ImageView
    lateinit var recycler_view: RecyclerView

    //lateinit var swipe_refresh: SwipeRefreshLayout
    var database = Firebase.firestore
    //lateinit var user_card: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        log_out = findViewById(R.id.log_out)
        recycler_view = findViewById(R.id.recyclerview_newmessage)
        //swipe_refresh = findViewById(R.id.swiperefresh)
        database = FirebaseFirestore.getInstance()
        group_call = findViewById(R.id.group_call)
        //user_card = findViewById(R.id.user_card_chat)

        val query: Query = database.collection("Users").limit(50)
        val options: FirestoreRecyclerOptions<User> =
            FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java)
                .setLifecycleOwner(this).build()
        val adapter = object : FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val view: View =
                    LayoutInflater.from(this@ChatActivity).inflate(R.layout.user_row, parent, false)
                return UserViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
                val card:CardView=holder.itemView.findViewById(R.id.user_card_chat)
                val name: TextView = holder.itemView.findViewById(R.id.username_chat)
               // val time: TextView = holder.itemView.findViewById(R.id.time_stamp)
                //val message: TextView = holder.itemView.findViewById(R.id.latest_message)

                AppPreferences.id = FirebaseAuth.getInstance().uid ?: return
                name.text = model.name



                card.setOnClickListener {
                    if (checkSelfPermission(
                                    Manifest.permission.RECORD_AUDIO,
                                    PERMISSION_REQ_ID_RECORD_AUDIO
                            ) && checkSelfPermission(
                                    Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA
                            )
                    ) {

                        //randomButtonClick()

                        //initAgoraEngineAndJoinChannel()
                        AppPreferences.toid=model.id


                        val intent=Intent(this@ChatActivity,ChatLogActivity::class.java)

                        startActivity(intent)
                    } else {
                        Toast.makeText(
                                this@ChatActivity,
                                "Please allow permissions to access the video call",
                                Toast.LENGTH_SHORT
                        )


                    }



                }

            }

        }
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)




    group_call.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
        log_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            AppPreferences.isLogin = false
            AppPreferences.username = ""
            AppPreferences.email = ""
            AppPreferences.password = ""
            AppPreferences.id=""
            AppPreferences.toid=""
            val intent = Intent(this, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)


        }
    }
    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        Log.i(LOG_TAG, "checkSelfPermission $permission $requestCode")
        if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(permission),
                    requestCode
            )
            return false
        }
        return true
    }
    private fun showLongToast(msg: String) {
        this.runOnUiThread { Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show() }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(LOG_TAG, "onRequestPermissionsResult ${grantResults[0]} $requestCode"  )

        when (requestCode) {
            PERMISSION_REQ_ID_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)
                } else {
                    showLongToast("No permission for ${Manifest.permission.RECORD_AUDIO} ")
                    finish()
                }
            }
            PERMISSION_REQ_ID_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(this,ChatLogActivity::class.java))
                } else {
                    showLongToast("No permission for ${Manifest.permission.CAMERA}" )
                    finish()
                }
            }
        }
    }


            companion object {

        private val LOG_TAG = ChatActivity::class.java.simpleName

        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1
    }
}



