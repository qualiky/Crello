package com.example.crello.splash

import android.content.Intent
import android.os.Bundle
import com.example.crello.activities.BaseActivity
import com.example.crello.authentication.LoginActivity
import com.example.crello.authentication.SignupActivity
import com.example.crello.databinding.ActivityIntroBinding
import com.example.crello.utils.Utilities

class IntroActivity : BaseActivity() {

    lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utilities().makeSplashScreenFullScreen(window)

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this@IntroActivity, SignupActivity::class.java))
        }
    }
}