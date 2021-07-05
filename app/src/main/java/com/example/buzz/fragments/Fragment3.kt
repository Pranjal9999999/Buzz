package com.example.buzz.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.buzz.R
import com.example.buzz.activities.LoginActivity

class Fragment3: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_onboarding3, container, false)

        var getStarted: Button = view.findViewById(R.id.get_started_button)
        getStarted.setOnClickListener{
            val intent=Intent(this@Fragment3.context, LoginActivity::class.java)
            startActivity(intent)


        }
        return view
    }

}