package com.example.urbanmessenger.utilits

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.urbanmessenger.R
import okio.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.jvm.Throws

fun Fragment.myToast(text: String) {
    Toast.makeText(
        this.context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

fun AppCompatActivity.myToast(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

@Throws(IOException::class)
fun Uri.uriToByteArray(context: Context) =
    context.contentResolver.openInputStream(this)?.use { it.buffered().readBytes() }

fun myToast(text: String) {
    Toast.makeText(
        APP_ACTIVITY.applicationContext,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}


fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

fun AppCompatActivity.replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .addToBackStack(null)
        .replace(R.id.nav_host_fragment_container, fragment)
        .commit()
}

fun getNavController(): NavController {

    val navHostFragment = APP_ACTIVITY.supportFragmentManager
        .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
    val navController = navHostFragment.navController

    return navController
}

fun Fragment.replaceFragment(fragment: Fragment) {
    this.fragmentManager?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(R.id.nav_host_fragment_container, fragment)
        ?.commit()
}

