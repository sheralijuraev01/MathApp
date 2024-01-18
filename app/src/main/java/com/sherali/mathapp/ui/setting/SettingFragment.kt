package com.sherali.mathapp.ui.setting

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sherali.mathapp.HighScore
import com.sherali.mathapp.R
import com.sherali.mathapp.adapter.HighScoreAdapter
import com.sherali.mathapp.databinding.FragmentSettingBinding
import com.sherali.mathapp.util.Functions
import com.sherali.mathapp.vm.MainViewModel


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!


    private val mainViewModel: MainViewModel by viewModels()


    var list: MutableList<HighScore> = ArrayList()
    private lateinit var viewContainer: View
    private var chooseItem: Int = -1
    private lateinit var shPGamerInfo: SharedPreferences
    private var gamerNameStatus = false
    private lateinit var gridContainer: GridLayout
    private var gamerNameInit = ""
    private var chooseImageInit = -1

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
                na
                initGamerInfo(it.name, it.index)
            }
        }

        shPGamerInfo = getSharedPreferences("gamerInfo", Context.MODE_PRIVATE)

        //profile oynaga o'tilganda rekorlarni yangilash
        initGamerInfo()
        //profil oygana o'tganda rekordni saralash
        initHighScoreSort()
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

        val builder = AlertDialog.Builder(this)
        viewContainer = layoutInflater.inflate(R.layout.edit_name_alert_dialog, null)
        builder.setView(viewContainer)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)


        val gamerName = viewContainer.findViewById<EditText>(R.id.player_name)
        val save_btn = viewContainer.findViewById<TextView>(R.id.save)
        val cancel_btn = viewContainer.findViewById<TextView>(R.id.cancel)
        gamerName.setText(binding.userName.text)
        var gamer: String


        //alert dialog ochilganda eski tanlangan ramsni tanlanganini ko'rsatish
        gridContainer = viewContainer.findViewById<GridLayout>(R.id.grid_container)
        if (chooseImageInit != 4)
            gridContainer.getChildAt(chooseImageInit)
                .setBackgroundResource(R.drawable.selected_icon_background)
        chooseItem = chooseImageInit
        save_btn.setOnClickListener {
            gamer = gamerName.text.toString().trim()
            checkNameStatus(gamer)
            if (gamerNameStatus) {
                saveUserInfo(gamer, chooseItem)
//                initGamerInfo()
                dialog.dismiss()
            }
        }

        cancel_btn.setOnClickListener {
            saveUserInfo(gamerNameInit, chooseImageInit)
            initGamerInfo()
            dialog.dismiss()
        }


    }

    //  iconlar tanlanganda ularni qiymatini olish va tanlanganini bildirish
    fun onclickImageProfile(view: View) {
        gridContainer = viewContainer.findViewById<GridLayout>(R.id.grid_container)
        val imageView = view as ImageView
        chooseItem = Integer.parseInt(imageView.tag.toString())
        for (i in 0 until 4) {
            if (i == chooseItem) gridContainer.getChildAt(i)
                .setBackgroundResource(R.drawable.selected_icon_background)
            else gridContainer.getChildAt(i)
                .setBackgroundResource(R.drawable.noselected_icon_background)
        }

    }

    // profil oyna ochilganda sharepreference dagi rasm va ismni oluvchi va yozuvchi funksiya
    private fun initGamerInfo(name: String, index: Int) {
        val icons = arrayOf(
            R.drawable.gamer1,
            R.drawable.gamer2,
            R.drawable.gamer3,
            R.drawable.gamer4,
            R.drawable.user,
        )
        gamerNameInit = shPGamerInfo.getString("gamerName", " ").toString()
        chooseImageInit = shPGamerInfo.getInt("icon", 4)

        binding.gamerIcon.setImageResource(icons[chooseImageInit])
        binding.userName.text = gamerNameInit
    }

    // rekordni saralovchi va listViewga yozuvchi funksiya
    private fun initHighScoreSort() {
        val sharedPreferences = getSharedPreferences("recordSave", AppCompatActivity.MODE_PRIVATE)
        val easy45 = sharedPreferences.getString("easy45", "").toString()
        val easy60 = sharedPreferences.getString("easy60", "").toString()
        val easy90 = sharedPreferences.getString("easy90", "").toString()
        val medium45 = sharedPreferences.getString("medium45", "").toString()
        val medium60 = sharedPreferences.getString("medium60", "").toString()
        val medium90 = sharedPreferences.getString("medium90", "").toString()
        val hard45 = sharedPreferences.getString("hard45", "").toString()
        val hard60 = sharedPreferences.getString("hard60", "").toString()
        val hard90 = sharedPreferences.getString("hard90", "").toString()

        list.add(HighScore("Easy", 45, stringToInt(easy45)))
        list.add(HighScore("Easy", 60, stringToInt(easy60)))
        list.add(HighScore("Easy", 90, stringToInt(easy90)))
        list.add(HighScore("Medium", 45, stringToInt(medium45)))
        list.add(HighScore("Medium", 60, stringToInt(medium60)))
        list.add(HighScore("Medium", 90, stringToInt(medium90)))
        list.add(HighScore("Hard", 45, stringToInt(hard45)))
        list.add(HighScore("Hard", 60, stringToInt(hard60)))
        list.add(HighScore("Hard", 90, stringToInt(hard90)))

        list.sort()

        val listAdapter = HighScoreAdapter(this, list)
        binding.listView.adapter = listAdapter
    }

    // string bo'lib keladigan rekorni int toifasiga o'tkazish
    private fun stringToInt(str: String): Int {
        return if (str != "") Integer.parseInt(str) else 0
    }


    private fun saveUserInfo(name: String, index: Int) {
        mainViewModel.saveStatus(true)
        mainViewModel.saveName(name)
        mainViewModel.saveIndex(index)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}