package com.example.urbanmessenger.chats

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.urbanmessenger.APP_ACTIVITY
import com.example.urbanmessenger.AUTHFIREBASE
import com.example.urbanmessenger.DATA_BASE_ROOT
import com.example.urbanmessenger.NODE_USERS
import com.example.urbanmessenger.R
import com.example.urbanmessenger.UID
import com.example.urbanmessenger.USER
import com.example.urbanmessenger.auth.AuthActivity
import com.example.urbanmessenger.databinding.ActivityMainBinding
import com.example.urbanmessenger.initFirebase
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.AppStates
import com.example.urbanmessenger.utils.AppValueEventListener

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initFirebase()

        setSupportActionBar(binding.mainActivityToolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()
        binding.mainActivityToolbar.setupWithNavController(navController, appBarConfiguration)

        initUser()

    }

    override fun onStart() {
        super.onStart()

        APP_ACTIVITY = this
        AppStates.updateState(AppStates.ONLINE, this)

    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuItemExit -> {
                AppStates.updateState(AppStates.OFFLINE, this)
                AUTHFIREBASE.signOut()
                startActivity(Intent(this, AuthActivity::class.java))
                return true
            }

            else -> {
                val navController = findNavController(R.id.nav_host_fragment_container)
                return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(
                    item
                )
            }
        }
    }

    private fun initUser(){
        DATA_BASE_ROOT.child(NODE_USERS).child(UID).addListenerForSingleValueEvent(
            AppValueEventListener{
                USER = it.getValue(UserData::class.java) ?: UserData()
            }
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun updateToolbarTitle(newTitle: String){
        binding.mainActivityToolbar.setTitle(newTitle)
    }

}

