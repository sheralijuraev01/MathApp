package com.sherali.mathapp


import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color.TRANSPARENT
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.sherali.mathapp.databinding.ActivityMainBinding


import java.util.*

class MainActivity : AppCompatActivity() {

    private var timerChoose = (45000).toLong()
    private var score = 0
    private var questionNumber = 1
    private var minBound = 0
    private var maxBound = 0
    private var random = Random()
    private var answer: Int = 0
    private var answerLocation = -1
    private val option: MutableList<Int> = ArrayList()
    private var highScoreSharedPreferences = 0
    private var incorrectanswer: Int = 0
    private lateinit var countDownTimer: CountDownTimer
    private var level: String = ""
    private var currentLevel = ""
    private var progressTimeCounter = (timerChoose / 1000).toInt()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Kategoriya tanlanganda o'yin oynada levelni olib yozish
        takeGameCondition()
        // levelga mos ma'lumotlarni yozish
        init()
        // vaqtni o'zgartirish
        spinner()

        // orqa tugma bosilishi
        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }


    }

    // levelga mos ma'lumotlarni yozish
    private fun init() {
        binding.scoreAndQuestionsNumber.text = "$score/$questionNumber"
        generateQuestions()
        startTime(timerChoose)
        initSaveHighScore()

    }

    // savollarni generatsiya qilib beradigan funksiya
    private fun generateQuestions() {
        option.clear()

        val a: Int = random.nextInt(maxBound) + minBound
        val b: Int = random.nextInt(maxBound) + minBound
        binding.questions.text = "$a+$b=?"
        answer = a + b
        answerLocation = random.nextInt(4)

        for (i in 0 until 4) {
            if (i == answerLocation) option.add(answer)
            else {
                incorrectanswer = random.nextInt(maxBound * 2) + minBound
                while (incorrectanswer == answer || incorrectanswer in option) {
                    incorrectanswer = random.nextInt(maxBound * 2) + minBound
                }
                option.add(incorrectanswer)
            }
        }

        findViewById<Button>(R.id.option1).text = option[0].toString()
        findViewById<Button>(R.id.option2).text = option[1].toString()
        findViewById<Button>(R.id.option3).text = option[2].toString()
        findViewById<Button>(R.id.option4).text = option[3].toString()
    }

    // vaqt boshlanish funksiyasi
    private fun startTime(timer: Long) {

        binding.proggresBar.max = progressTimeCounter
        countDownTimer = object : CountDownTimer((timer + 100).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressTimeCounter--
                binding.proggresBar.progress = progressTimeCounter
            }

            override fun onFinish() {
                saveRecord()
                alertDialogGameOver()
                clearData()
            }

        }.start()
    }

    // variant tanlanganda javob to'gri yoki xato ekanligini aniqlab beruvchi funksiya
    fun selectOption(view: View) {

        if (view.tag.toString() == answerLocation.toString()) {
            score++
            answerToast(true)
            //agar o'yinchini bali rekordan katta bo'lsa o'yin oynada rekorni o'zgartirish
            if (score > highScoreSharedPreferences) {
                highScoreSharedPreferences++
                binding.highScore.text = highScoreSharedPreferences.toString()
            }
        } else {
            answerToast(false)
        }
        questionNumber++
        // keyingi savolga o'tish
        generateQuestions()

        binding.scoreAndQuestionsNumber.text = "$score/$questionNumber"


    }

    // javob to'g'ri yoki xatoligini bildirib turuvchi funksiya
    private fun answerToast(isCorrect: Boolean) {

        object : CountDownTimer(700, 1000) {
            @SuppressLint("ResourceAsColor")
            override fun onTick(millisUntilFinished: Long) {
                binding.answerCondition.visibility = View.VISIBLE
                if (isCorrect) {
                    binding.answerCondition.text = "Correct"
                    binding.answerCondition.setTextColor(getResources().getColor(R.color.correct))
                } else {
                    binding.answerCondition.text = "Wrong"
                    binding.answerCondition.setTextColor(getResources().getColor(R.color.wrong))
                }
            }

            override fun onFinish() {
                binding.answerCondition.visibility = View.INVISIBLE
            }


        }.start()
    }


    // o'yin tugaganda chiqadigan alaert dialog
    @SuppressLint("ResourceType")
    fun alertDialogGameOver() {
        val view = layoutInflater.inflate(R.layout.game_over_alert_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val restart = view.findViewById<ImageView>(R.id.restart)
        val home = view.findViewById<ImageView>(R.id.home)
        val info = view.findViewById<TextView>(R.id.info_game)
        val levelUp = view.findViewById<TextView>(R.id.level)
        val allQuestions = view.findViewById<TextView>(R.id.questions)
        val gamerScore = view.findViewById<TextView>(R.id.gamer_score)
        val lottiAnimation = view.findViewById<LottieAnimationView>(R.id.lottiAnimation)

        /**
         * goodScore-bu foiz, qaysiki jami savoldan foydalanuvchi topgan savollar foizi,
         * agar 60 dan yuqori bo'lsa
        natija good(yaxshi), 60 fozidan kichik bo'lsa bad(yomon),
        agarda score rekordan baland bo'lsa High score(Yuqori natija)
         */
        val goodScore = (score * 100) / questionNumber
        if (score >= highScoreSharedPreferences && score != 0) {
            lottiAnimation.setAnimation(R.raw.best)
            info.text = "High Score"
        } else if (goodScore > 60) {
            lottiAnimation.setAnimation(R.raw.good)
            info.text = "Good"

        } else {
            lottiAnimation.setAnimation(R.raw.bad)
            info.text = "Bad"
        }

        levelUp.text = "Level\n$currentLevel"
        allQuestions.text = "Questions\n$questionNumber"
        gamerScore.text = "Score\n$score"


        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)

        // qayta o'yinni boshlash
        restart.setOnClickListener {
            restart()
            dialog.dismiss()
        }
        //menu oygana qaytish
        home.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            dialog.dismiss()
            finish()
        }
    }

    //qayta o'yin boshlanishi
    internal fun restart() {
        progressTimeCounter = (timerChoose / 1000).toInt()
        binding.proggresBar.max = progressTimeCounter
        questionNumber = 1
        score = 0
        binding.option1.isEnabled = true
        binding.option2.isEnabled = true
        binding.option3.isEnabled = true
        binding.option4.isEnabled = true
        init()
    }

    // o'yin tugaganda generation bo'lgan savollarni va boshqa itemlarni tozalash
    fun clearData() {
        binding.option1.text = ""
        binding.option2.text = ""
        binding.option3.text = ""
        binding.option4.text = ""
        binding.option1.isEnabled = false
        binding.option2.isEnabled = false
        binding.option3.isEnabled = false
        binding.option4.isEnabled = false


    }

    //spinner : vaqt uchun
    private fun spinner() {
        val timer = arrayOf("45", "60", "90")
        val adapter = ArrayAdapter(
            this, R.layout.spinner_item, timer
        )
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                saveRecord()
                countDownTimer.cancel()


                val choose: String = timer[position]

                //spinner ochilib spinner itemlaridan tanlangan vaqt
                timerChoose = (Integer.parseInt(choose) * 1000).toLong()

                progressTimeCounter = (timerChoose / 1000).toInt()

                restart()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    //Menu oynadan kelgan level bo'yicha ma'lumotlarni init qilish
    private fun takeGameCondition() {
        level = intent.getStringExtra("level").toString()

        when (level) {
            "1" -> {
                minBound = 10
                maxBound = 50
                currentLevel = "Easy"
            }
            "2" -> {
                minBound = 51
                maxBound = 100
                currentLevel = "Medium"
            }
            else -> {
                minBound = 101
                maxBound = 150
                currentLevel = "Hard"
            }
        }

        binding.categoryName.text = currentLevel
    }

    // Rekordni olish, level tartib raqami va tanlangan vaqt bo'yicha rekord olinadi
    private fun getHighScore(level: String, time: String): String {

        var result = ""
        val sharedPreferences = getSharedPreferences("recordSave", MODE_PRIVATE)

        when (level) {
            "1" -> {
                result = when (time) {
                    "45" -> {
                        sharedPreferences.getString("easy45", "").toString()
                    }
                    "60" -> {
                        sharedPreferences.getString("easy60", "").toString()
                    }
                    else -> {
                        sharedPreferences.getString("easy90", "").toString()
                    }
                }
            }
            "2" -> {
                result = when (time) {
                    "45" -> {
                        sharedPreferences.getString("medium45", "").toString()
                    }
                    "60" -> {
                        sharedPreferences.getString("medium60", "").toString()
                    }
                    else -> {
                        sharedPreferences.getString("medium90", "").toString()
                    }
                }
            }
            else -> {
                result = when (time) {
                    "45" -> {
                        sharedPreferences.getString("hard45", "").toString()
                    }
                    "60" -> {
                        sharedPreferences.getString("hard60", "").toString()
                    }
                    else -> {
                        sharedPreferences.getString("hard90", "").toString()
                    }
                }
            }
        }

        return result
    }

    // Rekordni saqlash, amaldagi level va vaqtda saqlaydi
    private fun putHighScore(level: String, time: String, record: Int) {
        val sharedPreferences = getSharedPreferences("recordSave", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        when (level) {
            "1" -> {
                when (time) {
                    "45" -> {
                        editor.putString("easy45", record.toString())
                    }
                    "60" -> {
                        editor.putString("easy60", record.toString())
                    }
                    else -> {
                        editor.putString("easy90", record.toString())
                    }
                }
            }
            "2" -> {
                when (time) {
                    "45" -> {
                        editor.putString("medium45", record.toString())
                    }
                    "60" -> {
                        editor.putString("medium60", record.toString())
                    }
                    else -> {
                        editor.putString("medium90", record.toString())
                    }
                }
            }
            else -> {
                when (time) {
                    "45" -> {
                        editor.putString("hard45", record.toString())
                    }
                    "60" -> {
                        editor.putString("hard60", record.toString())
                    }
                    else -> {
                        editor.putString("hard90", record.toString())
                    }
                }
            }
        }
        editor.apply()
    }

    // Rekorni sqalash funksiyasi
    fun saveRecord() {
        // score rekorddan katta bo'lsagina u share preferences ga saqlaydi
        if (score >= highScoreSharedPreferences) {
            putHighScore(level, (timerChoose / 1000).toString(), highScoreSharedPreferences)
        }
    }


    // Rekordni amaldagi levelga yozish
    private fun initSaveHighScore() {
        val str = getHighScore(level, (timerChoose / 1000).toString())
        highScoreSharedPreferences = if (str != "") {
            str.toInt()
        } else 0

        binding.highScore.text = highScoreSharedPreferences.toString()
    }

    // pausa bo'lganda rekordni saqlash
    override fun onPause() {
        super.onPause()
        saveRecord()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@MainActivity, MenuActivity::class.java))
        finish()
    }

}