package com.sherali.mathapp.ui.splash

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sherali.mathapp.R
import com.sherali.mathapp.databinding.FragmentSplashBinding
import com.sherali.mathapp.ui.dialogs.ProfileEditDialog
import com.sherali.mathapp.vm.MainViewModel
import dagger.hilt.android.HiltAndroidApp
import kotlin.properties.Delegates

@HiltAndroidApp
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()

    internal var isLogIn by Delegates.notNull<Boolean>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel.getStatus().observe(requireActivity()) {
            if (it != null) {
                isLogIn = it
            }
        }




        object : CountDownTimer(1000, 1000) {
            override fun onTick(l: Long) {

            }

            override fun onFinish() {
                if (!isLogIn) {
                    alertDialogEditName()
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_menuFragment)
                }

            }
        }.start()


    }

    fun alertDialogEditName() {
        var name = "User"
        var index = 4
        var message = ""
        var status = false
        val dialog = ProfileEditDialog(
            binding.root.context,
            name,
            index,
            { newName ->
                name = newName
            },
            { newIndex ->
                index = newIndex
            },
            { editStatus ->
                status = editStatus
            },
            { messageInfo ->
                message = messageInfo
            }

        )

        if (status){
            saveUserLogIn(name, index)

        }
        else Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
        dialog.show()
    }


    private fun saveUserLogIn(name: String, index: Int) {
        mainViewModel.saveStatus(true)
        mainViewModel.saveName(name)
        mainViewModel.saveIndex(index)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}