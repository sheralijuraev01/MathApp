package com.sherali.mathapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.sherali.mathapp.databinding.ActivityMenuBinding
import com.sherali.mathapp.util.Functions


class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // menyuga qaytilganda yoki qayta kirilganda ma'lumotlarni init qilish
        initGamerInfo()
        //menyuga kirilganda yoki qaytilganda rekordni yangilash
        updateHighScores()


    }

    // tanlangan kategoriyani tagi orqali aytilgan oynaga  o'tish funkiyasi
    fun onClikcCategory(view: View) {
        val cardView = view as CardView
        val level = cardView.tag.toString()
        /**
         * agar tag 1 oson
         * agar tag 2 o'rta
         * agar tag 3 qiyin
         * agar tag 5 bo'lsa bu profileSetting oynasiga o'tadi
         * */
        if (level.toInt() == 5) {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("level", level)
            startActivity(intent)
            finish()
        }


    }

    // menyuga qaytilganda yoki qayta kirilganda ma'lumotlarni init qilish
    private fun initGamerInfo() {
        val icons = arrayOf(
            R.drawable.gamer1,
            R.drawable.gamer2,
            R.drawable.gamer3,
            R.drawable.gamer4,
            R.drawable.user,
        )
        val sharedPreferences = getSharedPreferences("gamerInfo", Context.MODE_PRIVATE)

        var gamerName: String = sharedPreferences.getString("gamerName", "").toString()
        val chooseImage = sharedPreferences.getInt("icon", 4)

        binding.profileImage.setImageResource(icons[chooseImage])
        gamerName = gamerNameStatus(gamerName)
        binding.gamerName.text = gamerName.trim()


    }

    private fun gamerNameStatus(name: String): String {
        return if (Functions.checkStandardLength(name)) {
            name
        } else name.substring(0, 5) + "..."

    }

    //menyuga kirilganda yoki qaytilganda rekordni yangilash
    private fun updateHighScores() {
        //rekor saqalangan sharedPreferences
        val sharedPreferences = getSharedPreferences("recordSave", Context.MODE_PRIVATE)

        // saqlangan rekorlarni olish string toifasida
        val easy45 = sharedPreferences.getString("easy45", "").toString()
        val easy60 = sharedPreferences.getString("easy60", "").toString()
        val easy90 = sharedPreferences.getString("easy90", "").toString()
        val medium45 = sharedPreferences.getString("medium45", "").toString()
        val medium60 = sharedPreferences.getString("medium60", "").toString()
        val medium90 = sharedPreferences.getString("medium90", "").toString()
        val hard45 = sharedPreferences.getString("hard45", "").toString()
        val hard60 = sharedPreferences.getString("hard60", "").toString()
        val hard90 = sharedPreferences.getString("hard90", "").toString()

        // har bir levelning eng katta rekordini topish
        //easy
        val maxEasy = maxABC(stringToInt(easy45), stringToInt(easy60), stringToInt(easy90))
        //meium
        val maxMedium = maxABC(stringToInt(medium45), stringToInt(medium60), stringToInt(medium90))
        //hard
        val maxHard = maxABC(stringToInt(hard45), stringToInt(hard60), stringToInt(hard90))

        //umumiy eng katta rekorni topish
        val maxAllLevel = maxABC(maxEasy, maxMedium, maxHard)


        // topilgan rekordlarni yozish
        binding.gamerHighScore.text = maxAllLevel.toString()
        binding.easyBestScory.text = "Best Score:$maxEasy"
        binding.mediumBestScory.text = "Best Score:$maxMedium"
        binding.hardBestScory.text = "Best Score:$maxHard"

    }


    // string rekordni int ga o'tkazish
    private fun stringToInt(str: String): Int {
        return if (str != "") Integer.parseInt(str) else 0
    }


    // 3 ta int toifasidagi sonni kattasini topish
    private fun maxABC(a: Int, b: Int, c: Int): Int {

        if (a > b && a > c) return a
        else if (b > a && b > c) return b
        else return c
    }


}