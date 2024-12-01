package com.example.urbanmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.room.Database
import com.example.urbanmessenger.auth.AuthActivity
import com.example.urbanmessenger.chats.MainActivity
import com.example.urbanmessenger.databinding.ActivitySingleChatBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.AppValueEventListener
import com.google.firebase.database.DatabaseReference

class SingleChatActivity : AppCompatActivity() {

    private var _binding: ActivitySingleChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var mListenerInfoToolbar: AppValueEventListener
    private lateinit var mReceivingUser: UserData

    private lateinit var mRefUser: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        _binding = ActivitySingleChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SINGLE_CHAT_ACTIVITY = this

        setSupportActionBar(binding.singleChatActivityToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbarArrowBackIcon.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }


        initFirebase()
        initUser()
    }

    override fun onResume() {
        super.onResume()
        mListenerInfoToolbar = AppValueEventListener{
            mReceivingUser = it.getUserDataModel()
            initInfoToolbar()
        }

        mRefUser = DATA_BASE_ROOT.child(NODE_USERS).child(CONTACT.id)
        mRefUser.addValueEventListener(mListenerInfoToolbar)
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

    override fun onPause() {
        super.onPause()
        mRefUser.removeEventListener(mListenerInfoToolbar)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initInfoToolbar(){

        if (mReceivingUser.firstname.isEmpty() && mReceivingUser.lastname.isEmpty()){
            binding.toolbarUserFullName.text = mReceivingUser.email
        } else {
            binding.toolbarUserFullName.text = "${mReceivingUser.firstname} ${mReceivingUser.lastname}"
        }
        binding.toolbarUserStatus.text = mReceivingUser.state
    }

}
