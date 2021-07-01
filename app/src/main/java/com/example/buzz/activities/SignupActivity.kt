package com.example.buzz.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.buzz.R

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        var backToLogin:Button = findViewById(R.id.back_to_login)
        val intent= Intent(this@SignupActivity, LoginActivity::class.java)
        backToLogin.setOnClickListener{
            startActivity(intent)
        }
    }
}