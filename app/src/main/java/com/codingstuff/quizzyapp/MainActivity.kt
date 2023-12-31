package com.codingstuff.quizzyapp

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.codingstuff.quizzyapp.views.StudyFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity() {
    private var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find the bottom navigation view by its ID
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bot_nav_view_main)
        bottomNavView.setOnNavigationItemSelectedListener { item: MenuItem ->
            val itemId = item.itemId
            if (itemId == R.id.menu_study) {
                navController!!.navigate(R.id.studyFragment)
                return@setOnNavigationItemSelectedListener true
            } else if (itemId == R.id.menu_stats) {
                navController!!.navigate(R.id.statsFragment)
                return@setOnNavigationItemSelectedListener true
            } else if (itemId == R.id.menu_review) {
                navController!!.navigate(R.id.reviewFragment)
                return@setOnNavigationItemSelectedListener true
            } else if (itemId == R.id.menu_settings) {
                //navController!!.navigate(R.id.settingFragment)
                navController!!.navigate(R.id.admin_Fragment)
                return@setOnNavigationItemSelectedListener true
            }
            false
        }

        /////////////////////////////////////////////
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_2) as NavHostFragment?

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.studyFragment) {
                // Call the refresh method of StudyFragment here
                val studyFragment = supportFragmentManager.findFragmentByTag("StudyFragment") as StudyFragment?
                studyFragment?.refreshFragment()
            }
        }
        //navigateToAnotherFragment();
        navController = navHostFragment!!.navController

    }

    override fun onBackPressed() {
        if (navController!!.currentDestination!!.id == R.id.studyFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    } /*
    public void navigateToAnotherFragment() {
        navController.navigate(R.id.splashFragment);
    }
*/
}