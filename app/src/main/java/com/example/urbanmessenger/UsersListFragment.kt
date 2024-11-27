package com.example.urbanmessenger

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.urbanmessenger.databinding.FragmentUsersListBinding
import com.example.urbanmessenger.models.UserData
import com.example.urbanmessenger.utils.myToast
import com.example.urbanmessenger.utils.updateToolbar


class UsersListFragment : Fragment() {


    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterUsersList: CustomAdapterUsersList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val usersList = mutableListOf<UserData>(
//
//            UserData("Виктор Иванов", "ivanov@gmail.com", R.drawable.man),
//            UserData("Екатерина Петрова", "petorva@mail.ru", R.drawable.man),
//            UserData("Сергей Иванченко", "iva288@mail.ru", R.drawable.man),
//            UserData("Петр Сигаев", "sigaev@gmail.com", R.drawable.man),
//
//            )
//
//        adapterUsersList = CustomAdapterUsersList(usersList)
//        binding.usersListRecyclerView.adapter = adapterUsersList
//        adapterUsersList.notifyDataSetChanged()
//
//        adapterUsersList.setUserClickListener(
//            object : CustomAdapterUsersList.OnUserClickListener {
//                override fun onUserClick(user: UserData, position: Int) {
//                    myToast("Вы нажали на сообщение ${user.name}", requireContext())
//                }
//            }
//        )

        Log.d("@@@", "Users list was created")
    }

    override fun onPause() {
        super.onPause()
        Log.d("@@@", "OnViewPause")
    }

    override fun onResume() {
        super.onResume()
        updateToolbar("Пользователи", requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("@@@", "OnDestroyView")
        _binding = null

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("@@@", "OnDestroy")
    }


}