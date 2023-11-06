package com.sherali.mathapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sherali.mathapp.databinding.ActivitySplashBinding
import com.sherali.mathapp.util.Functions
import kotlin.properties.Delegates


class SplashActivity : AppCompatActivity() {

    private lateinit var viewContainer:View
    private var chooseItem: Int = -1
    private lateinit var shPGamerInfo: SharedPreferences
    private lateinit var shPLogInStatus: SharedPreferences
    private var isLogIn by Delegates.notNull<Boolean>()
    private lateinit var binding: ActivitySplashBinding
    private var gamerNameStatus = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserLogInStatus()




        object : CountDownTimer(1000, 1000) {
            override fun onTick(l: Long) {

            }

            override fun onFinish() {
                if (!isLogIn) {
                    alertDialogEditName()
                } else {
                    startActivity(Intent(applicationContext, MenuActivity::class.java))
                    finish()
                }

            }
        }.start()
    }

    fun alertDialogEditName() {
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
        var gamer: String

        save_btn.setOnClickListener {
            gamer = gamerName.text.toString().trim()
            checkNameStatus(gamer)
            if (gamerNameStatus) {
                saveUserInfo(gamer, chooseItem)
                startActivity(Intent(applicationContext, MenuActivity::class.java))
                finish()
            }
        }

        //cancel bosilganda agarda hech qanaqangi qiymat tanlamasa va ism yozmasa o'zini default icon va ismini oladi
        cancel_btn.setOnClickListener {
            saveUserInfo("User", 4)
            startActivity(Intent(applicationContext, MenuActivity::class.java))
            finish()
        }


    }

    //  iconlar tanlanganda ularni qiymatini olish va tanlanganini bildirish
    fun onclickImageProfile(view: View) {

        val gridContainer = viewContainer.findViewById<GridLayout>(R.id.grid_container)
        val imageView = view as ImageView
        chooseItem = Integer.parseInt(imageView.tag.toString())
        for (i in 0 until 4) {
            if (i == chooseItem) gridContainer.getChildAt(i)
                .setBackgroundResource(R.drawable.selected_icon_background)
            else gridContainer.getChildAt(i)
                .setBackgroundResource(R.drawable.noselected_icon_background)
        }

    }

    private fun checkNameStatus(name: String) {
        if (name.isEmpty()) {
            gamerNameStatus = false
            Toast.makeText(
                this@SplashActivity,
                "The name must not be empty",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        else if(chooseItem==-1){
            gamerNameStatus = false
            Toast.makeText(
                this@SplashActivity,
                "Please select icon for profile",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val status = Functions.checkNameCondition(name)
        if (status == "") {
            gamerNameStatus = true

        } else {
            gamerNameStatus = false
            Toast.makeText(
                this@SplashActivity,
                status,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun saveUserLogIn() {
        val editor = shPLogInStatus.edit()
        // berilgan oynaga ma'lumotlar yozildi, shuning uchun isLoggin true qilamiz,
        // va qayta kirilganida yana ism va icon tanlaydigan alert dialog chiqmasligi uchun
        editor.putBoolean("isLogIn", true)
        editor.apply()
    }

    private fun saveUserInfo(gamerName: String, iconPosition: Int) {
        shPGamerInfo = getSharedPreferences("gamerInfo", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = shPGamerInfo.edit()
        editor.putString("gamerName", gamerName)
        editor.putInt("icon", iconPosition)
        editor.apply()
        saveUserLogIn()
    }

    private fun getUserLogInStatus() {
        // oldin ism va icon tanlangan yoki yo'qligini bilish , agar true bo'lsa oldin kirilgan, false bo'lsa oldin kirilmagan
        shPLogInStatus = getSharedPreferences("isLogInStatus", Context.MODE_PRIVATE)
        isLogIn = shPLogInStatus.getBoolean("isLogIn", false)
    }

}


