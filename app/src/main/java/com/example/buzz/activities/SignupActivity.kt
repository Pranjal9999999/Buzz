package com.example.buzz.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.buzz.R
import com.example.buzz.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var signup: Button
    lateinit var name: EditText
    lateinit var loginBack:Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    var database = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        var backToLogin: Button = findViewById(R.id.back_to_login)
        progressBar = findViewById(R.id.signUpProgressBar)
        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
        backToLogin.setOnClickListener {
            startActivity(intent)
        }
        auth = Firebase.auth
        loginBack = findViewById(R.id.back_to_login)
        email = findViewById(R.id.email_signup)
        password = findViewById(R.id.Password_signup)
        signup = findViewById(R.id.create_account)
        name = findViewById(R.id.name)
        database = FirebaseFirestore.getInstance()



        signup.setOnClickListener {
            var mail1: String = email.text.toString() //string variables to store the edit text values
            var pass: String = password.text.toString()
            var name: String = name.text.toString()
            var user1 = User()
            user1.setEmail(mail1)
            user1.setName(name)
            user1.setPassword(pass)
            signup.visibility = View.GONE
            progressBar.visibility = View.VISIBLE


            if (mail1.trim().isEmpty() || pass.trim().isEmpty() || name.trim().isEmpty()) {
                Toast.makeText(baseContext, "Enter all the credentials", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                signup.visibility = View.VISIBLE



            } else {


                auth.createUserWithEmailAndPassword(mail1, pass)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                // Log.d(TAG, "createUserWithEmail:success")
                                database.collection("Users").document().set(user1).addOnSuccessListener {
                                    progressBar.visibility = View.GONE
                                    signup.visibility = View.VISIBLE
                                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                                Toast.makeText(
                                        baseContext, "Signed Up Successfully",
                                        Toast.LENGTH_SHORT
                                ).show()


                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                progressBar.visibility = View.GONE
                                signup.visibility = View.VISIBLE
                                Toast.makeText(
                                        this@SignupActivity, task.getException()?.getLocalizedMessage(),
                                        Toast.LENGTH_SHORT
                                ).show()


                            }
                        }
            }
            loginBack.setOnClickListener {
                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                startActivity(intent)
            }


        }



    }
}
