package com.google.bitfit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.bitfit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
        val fragmentTrack: Fragment = Track()
        val fragmentAnalysis: Fragment = Analysis()
        val fragmentNotification: Fragment = Notification()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener {item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.track -> fragment = fragmentTrack
                R.id.analysis -> fragment = fragmentAnalysis
                R.id.notifications -> fragment = fragmentNotification
            }
            replaceFragment(fragment)
            true
        }
        bottomNavigationView.selectedItemId = R.id.track
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)

        fragmentTransaction.commit()
    }
}