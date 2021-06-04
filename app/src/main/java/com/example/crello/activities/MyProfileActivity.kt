package com.example.crello.activities

import android.os.Bundle
import android.text.Editable
import com.bumptech.glide.Glide
import com.example.crello.R
import com.example.crello.databinding.ActivityMyProfileBinding
import com.example.crello.firebase.FirestoreClass
import com.example.crello.models.Users

class MyProfileActivity : BaseActivity() {

    lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()

        FirestoreClass().loadUserData(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarMyProfile)
        val actionBar = supportActionBar

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        binding.toolbarMyProfile.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setUserDataUpInUI(user: Users) {
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .into(binding.myProfileImage)

        binding.etNameProfile.setText(user.name)
        binding.etEmailProfile.setText(user.email)

        if(user.phoneNum != 0L) {
            binding.etPhoneProfile.setText(user.phoneNum.toString())
        }
    }
}