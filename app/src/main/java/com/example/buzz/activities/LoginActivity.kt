package com.example.buzz.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.buzz.R
import com.example.buzz.utilities.AppPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var SignUp: Button
    lateinit var email1: EditText
    lateinit var password1: EditText
    lateinit var login: Button
   // lateinit var logout:Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    lateinit var skip:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        SignUp = findViewById(R.id.signup)
        email1 = findViewById(R.id.email)
        password1 = findViewById(R.id.Password)
        login = findViewById(R.id.login)
        auth = Firebase.auth
        skip=findViewById(R.id.skip_login)
        //logout=findViewById(R.id.logout)
        progressBar=findViewById(R.id.login_progress_bar)
        AppPreferences.init(this)
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        SignUp.setOnClickListener {
            startActivity(intent)
        }
        skip.setOnClickListener {
            val intent=Intent(this@LoginActivity,VideoRoomActivity::class.java)
            startActivity(intent)
        }
        if (AppPreferences.isLogin) {
            email1.visibility = View.GONE
            password1.visibility = View.GONE
            login.visibility = View.GONE
            //logout.visibility=View.VISIBLE

        } else {
            email1.visibility = View.VISIBLE
            password1.visibility = View.VISIBLE
            login.visibility = View.VISIBLE
            //logout.visibility=View.GONE

            login.setOnClickListener {
                progressBar.visibility=View.VISIBLE
                login.visibility=View.GONE
                if(!email1.text.toString().isEmpty()&&!password1.text.toString().isEmpty()) {
                    auth.signInWithEmailAndPassword(email1.text.toString(), password1.text.toString())
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    AppPreferences.isLogin = true
                                    AppPreferences.email = email1.text.toString()
                                    AppPreferences.password = password1.text.toString()

                                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                                    Toast.makeText(
                                            baseContext, "Logged in successfully",
                                            Toast.LENGTH_SHORT
                                    ).show()

                                    val intent1 = Intent(this@LoginActivity, DashboardActivity::class.java)
                                startActivity(intent1)

                                } else {
                                    // If sign in fails, display a message to the user.
                                    progressBar.visibility = View.GONE
                                    login.visibility = View.VISIBLE
                                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                                    Toast.makeText(
                                            baseContext, task.getException()?.getLocalizedMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                }
                else
                { progressBar.visibility = View.GONE
                    login.visibility = View.VISIBLE
                    Toast.makeText(baseContext,"Enter all credentials",Toast.LENGTH_SHORT).show()

                }


            }
          /*  logout.setOnClickListener {

                email1.visibility = View.VISIBLE
                password1.visibility = View.VISIBLE
                login.visibility = View.VISIBLE
                logout.visibility=View.GONE
            }*/

        }
    }
}