package com.example.crello.splash

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.example.crello.activities.MainActivity
import com.example.crello.databinding.ActivitySplashBinding
import com.example.crello.firebase.FirestoreClass
import com.example.crello.utils.Utilities

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Utilities().makeSplashScreenFullScreen(window)

        Handler(Looper.myLooper()!!).postDelayed({
            var currentUserId = FirestoreClass().getCurrentUserId()
            if(!currentUserId.isEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, 2000)
    }

}