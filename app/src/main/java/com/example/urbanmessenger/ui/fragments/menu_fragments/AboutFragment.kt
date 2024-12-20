package com.example.urbanmessenger.ui.fragments.menu_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.databinding.FragmentAboutBinding
import com.example.urbanmessenger.ui.fragments.BaseFragment


class AboutFragment : BaseFragment<FragmentAboutBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater,container,false)
    }

}