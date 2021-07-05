package com.example.buzz.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.buzz.R
import com.example.buzz.fragments.Fragment0
import com.example.buzz.fragments.Fragment1
import com.example.buzz.fragments.Fragment2
import com.example.buzz.fragments.Fragment3
import com.example.buzz.utilities.AppPreferences

class SplashActivity : AppCompatActivity() {
    private val NumPages: Int = 4
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: OnboardingPagerAdapter

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    internal val mRunnable: Runnable = Runnable {



    }
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
        AppPreferences.init(this)
        if(AppPreferences.isLogin)
        {
            val intent= Intent(this@SplashActivity,ShortSplashActivity::class.java)
            startActivity(intent)
            finish()

        }

        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        viewPager = findViewById(R.id.liquid_view_pager)
        adapter = OnboardingPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
}
    class OnboardingPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

        // 2
        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> {
                    val tab0 = Fragment0()
                    return tab0
                }
                1 -> {
                    val tab1 = Fragment1()
                    return tab1
                }
                2 -> {
                    val tab2 = Fragment2()
                    return tab2
                }
                3->{  val tab3 = Fragment3()
                    return tab3

                }
                else->{
                    val tab0= Fragment0()
                    return tab0
                }




            }



        }

        // 3
        override fun getCount(): Int {
            return 4
        }
    }}









