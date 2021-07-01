package com.example.buzz.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.buzz.R

class LoginActivity : AppCompatActivity() {
    lateinit var SignUp:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        SignUp=findViewById(R.id.signup)
        val intent= Intent(this@LoginActivity, SignupActivity::class.java)
        SignUp.setOnClickListener{
           startActivity(intent)
        }
    }
}