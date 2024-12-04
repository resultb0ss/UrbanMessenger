package com.example.urbanmessenger

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.urbanmessenger.data.network.AUTHFIREBASE
import com.example.urbanmessenger.data.network.initFirebase
import com.example.urbanmessenger.data.network.initUser
import com.example.urbanmessenger.databinding.ActivityMainBinding
import com.example.urbanmessenger.utils.AppStates
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var searchItem = false


    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.mainActivityToolbar)


    }

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY = this
        AppStates.updateState(AppStates.ONLINE, this)

    }

    override fun onResume() {
        super.onResume()
        initNavigation()
        lifecycleScope.launch { initFirebase() }
        lifecycleScope.launch { initUser() }


    }


    private fun initNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        val builder = AppBarConfiguration.Builder(navController.graph)
        builder.setOpenableLayout(binding.drawerLayout)
        val appBarConfiguration = builder.build()
        binding.mainActivityToolbar.setupWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)

    }

    fun searchEnable() {
        if (!searchItem) {
            binding.toolbarSearchField.visibility = View.VISIBLE
            searchItem = true
        } else {
            binding.toolbarSearchField.visibility = View.GONE
            searchItem = false
        }
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE, this)
    }


    private fun onNavClick() {
        binding.navView.setNavigationItemSelectedListener { item ->
            val id = item.itemId
            if (id == R.id.signOutItem) {
                AUTHFIREBASE.signOut()
                startActivity(Intent(this, AuthActivity::class.java))
                this.finish()
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun updateToolbarTitle(newTitle: String) {
        binding.mainActivityToolbar.setTitle(newTitle)
    }


}

