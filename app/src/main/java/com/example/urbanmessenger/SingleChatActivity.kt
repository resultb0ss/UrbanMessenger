package com.example.urbanmessenger

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.urbanmessenger.auth.AuthActivity
import com.example.urbanmessenger.chats.MainActivity
import com.example.urbanmessenger.databinding.ActivitySingleChatBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.AppValueEventListener

class SingleChatActivity : AppCompatActivity() {

    private var _binding: ActivitySingleChatBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySingleChatBinding.inflate(layoutInflater)
        setContentView(binding.root)



        SINGLE_CHAT_ACTIVITY = this


        setSupportActionBar(binding.singleChatActivityToolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container_single_chat_activity) as NavHostFragment
        val navController = navHostFragment.navController
        val builder = AppBarConfiguration.Builder(navController.graph)
        val appBarConfiguration = builder.build()
        binding.singleChatActivityToolbar.setupWithNavController(navController, appBarConfiguration)



        initFirebase()
        initUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_single_chat_toolbar, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toChatsActivity -> {
                startActivity(Intent(this, MainActivity::class.java))
                return true
            }
            else -> {
                val navController = findNavController(R.id.nav_host_fragment_container_single_chat_activity)
                return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(
                    item
                )
            }
        }
    }

    private fun initUser() {
        DATA_BASE_ROOT.child(NODE_USERS).child(UID).addListenerForSingleValueEvent(
            AppValueEventListener {
                USER = it.getValue(UserData::class.java) ?: UserData()
            }
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}