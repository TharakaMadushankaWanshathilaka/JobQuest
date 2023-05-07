package com.example.jobquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.jobquest.fragments.ReceivedJobApplicationsFragment
import com.example.jobquest.fragments.SentApplicationsFragment
import com.example.jobquest.fragments.JobApplicationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        val homeFragment= SentApplicationsFragment()

        val homeFragment2= JobApplicationFragment()

        val homeFragment3= ReceivedJobApplicationsFragment()

        setCurrentFragment(homeFragment2)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.profileNav->setCurrentFragment(homeFragment)
                R.id.exploreNav->setCurrentFragment(homeFragment3)
                //R.id.profileNav->setCurrentFragment(profileFragment)
                //R.id.homeNav->setCurrentFragment(testFragment)

                R.id.homeNav->setCurrentFragment(homeFragment2)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainContainer,fragment)
            commit()
        }
}