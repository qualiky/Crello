package com.example.crello.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.crello.R
import com.example.crello.databinding.ActivityMainBinding
import com.example.crello.firebase.FirestoreClass
import com.example.crello.models.Users
import com.example.crello.splash.IntroActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()
        binding.navView.setNavigationItemSelectedListener(this)
        FirestoreClass().loadUserData(this)
    }

    private fun setUpActionBar(){
        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMainActivity)
        toolBar.setNavigationIcon(R.drawable.ic_nav_icon_hamburger)
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackPressedToExit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.id_nav_myprofile -> {
                startActivity(Intent(this, MyProfileActivity::class.java))
            }
            R.id.id_nav_signout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(loggedUser: Users) {
        Glide
            .with(this)
            .load(loggedUser.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(findViewById(R.id.navUserImg))

        findViewById<TextView>(R.id.tvUsername).text = loggedUser.name
    }
}