package com.sherali.mathapp.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sherali.mathapp.R
import com.sherali.mathapp.databinding.FragmentMenuBinding
import com.sherali.mathapp.util.Constants.EASY
import com.sherali.mathapp.util.Constants.HARD
import com.sherali.mathapp.util.Constants.MEDIUM
import com.sherali.mathapp.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MenuFragment : Fragment()  {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainViewModel.getUserData().observe(requireActivity()) {
            if (it != null) {
                initGamerInfo(it.name, it.index)
            }
        }

        //menyuga kirilganda yoki qaytilganda rekordni yangilash

        mainViewModel.getMaxScore().observe(requireActivity()) {
            if (it != null) {
                binding.gamerHighScore.text = it.toString()
            }

        }

        mainViewModel.getMaxCategory(EASY).observe(requireActivity()) {
            if (it != null) {
                binding.easyBestScory.text = it.toString()
            }

        }

        mainViewModel.getMaxCategory(MEDIUM).observe(requireActivity()) {
            if (it != null) {
                binding.mediumBestScory.text = it.toString()
            }

        }

        mainViewModel.getMaxCategory(HARD).observe(requireActivity()) {
            if (it != null) {
                binding.hardBestScory.text = it.toString()
            }

        }

        binding.profile.setOnClickListener{
            findNavController().navigate(R.id.action_menuFragment_to_settingFragment)
        }
        binding.cardEasy.setOnClickListener{
            navigateFragment("1")
        }
        binding.cardMedium.setOnClickListener {
            navigateFragment("2")

        }
        binding.cardHard.setOnClickListener{
            navigateFragment("3")
        }
    }
    private fun initGamerInfo(name: String, index: Int) {
        val icons = arrayOf(
            R.drawable.gamer1,
            R.drawable.gamer2,
            R.drawable.gamer3,
            R.drawable.gamer4,
            R.drawable.user,
        )

        binding.profileImage.setImageResource(icons[index])
        binding.gamerName.text = gamerNameStatus(name).trim()
    }

    private fun gamerNameStatus(name: String): String {
        return if (name.length < 9) {
            name
        } else name.substring(0, 5) + "..."

    }
    private fun navigateFragment(level: String) {
        val bundle = Bundle()
        bundle.putString("level", level)
        findNavController().navigate(R.id.action_menuFragment_to_gameFragment, bundle)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}