package com.cumpatomas.brunosrecipes.ui.mainactivity

import  android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.cumpatomas.brunosrecipes.R
import com.cumpatomas.brunosrecipes.databinding.ActivityMainBinding
import com.cumpatomas.brunosrecipes.manualdi.ApplicationModule

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val model: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ApplicationModule.initialiseApplicationContext(this.application)
        setSupportActionBar(binding.toolBar)
        getSupportActionBar()?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar()?.setCustomView(R.layout.home_fragment);
        setNavHostFragment()
        setAppBarConfig()
        setupActionBarWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        setNavigationIcon() // not needed since with the setAppBarConfig function it sets the homeFragment as Home


    }


    private fun setNavigationIcon() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (!appBarConfiguration.topLevelDestinations.contains(destination.id)) {
                binding.bottomNavigation.isGone = true
            } else {
                binding.bottomNavigation.isVisible = true
            }
        }
    }

    private fun setAppBarConfig() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.recipesListFragment,
                R.id.historialFragment,
                R.id.inputComposeFragment
            )
        )
    }

    private fun setNavHostFragment() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}



/*
Ideas:
-
- Present a photo of the dish in the Random option
- Make every item in the list clickable as an object to see ingredients and photos in a new UI
- The last option "Input Ingredients" is for the user to input the ingredients he/she has and get a list of possible recipes from the list
-How do I organize the files in the project folders?
commit test

*/
