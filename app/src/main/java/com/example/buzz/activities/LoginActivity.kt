package com.example.buzz.activities

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.buzz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var SignUp: Button
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var login: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        SignUp = findViewById(R.id.signup)
        email = findViewById(R.id.email)
        password = findViewById(R.id.Password)
        login = findViewById(R.id.login)
        auth = Firebase.auth
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        SignUp.setOnClickListener {
            startActivity(intent)
        }

        login.setOnClickListener {
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        Toast.makeText(
                            baseContext, "Logged in successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent1 = Intent(this@LoginActivity ,HomeActivity::class.java)
                        startActivity(intent1)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, task.getException()?.getLocalizedMessage(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }


        }
    }
}