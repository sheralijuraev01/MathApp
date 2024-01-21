package com.sherali.mathapp.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sherali.mathapp.R
import com.sherali.mathapp.adapter.HighScoreAdapter
import com.sherali.mathapp.data.local.room.entity.ScoreEntity
import com.sherali.mathapp.databinding.FragmentSettingBinding
import com.sherali.mathapp.ui.dialogs.ProfileEditDialog
import com.sherali.mathapp.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val list: MutableList<ScoreEntity> = ArrayList()

    private var gamerName = ""
    private var chooseIcon = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel.getUserData().observe(requireActivity()) {
            if (it != null) {
                gamerName = it.name
                chooseIcon = it.index
                initGamerInfo()
            }
        }



        mainViewModel.getAllScore().observe(requireActivity()) {
            if (it != null) {
                if(it.isNotEmpty()){
                    binding.listView.visibility=View.VISIBLE
                    binding.listTopItem.visibility=View.VISIBLE
                    binding.notFoundContainer.visibility=View.INVISIBLE

                    list.clear()
                    list.addAll(it)
                    initHighScoreSort()
                }else{
                    binding.listView.visibility=View.INVISIBLE
                    binding.listTopItem.visibility=View.INVISIBLE
                    binding.notFoundContainer.visibility=View.VISIBLE
                }

            }
        }

        // orqaga qaytish tugmasi
        binding.backProfileSetting.setOnClickListener {
            findNavController().popBackStack()
        }
        // tahrirlash tugmasi
        binding.setting.setOnClickListener {
            alertDialogEditName()
        }


    }


    // tahrirlash tugmasi bosilgandagi custom dialog
    private fun alertDialogEditName() {


        val dialog = ProfileEditDialog(
            binding.root.context,
            gamerName,
            chooseIcon
        )
        { newName, newIndex, editStatus, messageInfo ->
            if (editStatus) {
                gamerName = newName
                chooseIcon = newIndex
                saveUserInfo()
                initGamerInfo()
            } else Toast.makeText(binding.root.context, messageInfo, Toast.LENGTH_SHORT).show()

        }

        dialog.show()
    }


    // profil oyna ochilganda sharepreference dagi rasm va ismni oluvchi va yozuvchi funksiya
    private fun initGamerInfo() {
        val icons = arrayOf(
            R.drawable.gamer1,
            R.drawable.gamer2,
            R.drawable.gamer3,
            R.drawable.gamer4,
            R.drawable.user,
        )

        binding.gamerIcon.setImageResource(icons[chooseIcon])
        binding.userName.text = gamerName
    }

    // rekordni saralovchi va listViewga yozuvchi funksiya
    private fun initHighScoreSort() {

        val listAdapter = HighScoreAdapter(binding.root.context, list)
        binding.listView.adapter = listAdapter
    }

    // string bo'lib keladigan rekorni int toifasiga o'tkazish


    private fun saveUserInfo() {
        mainViewModel.saveName(gamerName)
        mainViewModel.saveIndex(chooseIcon)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}