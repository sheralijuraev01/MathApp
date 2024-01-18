package com.sherali.mathapp.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sherali.mathapp.R
import com.sherali.mathapp.data.local.room.entity.ScoreEntity
import com.sherali.mathapp.databinding.FragmentGameBinding
import com.sherali.mathapp.ui.dialogs.GameOverDialog
import com.sherali.mathapp.util.Constants.EASY
import com.sherali.mathapp.util.Constants.EASY_45
import com.sherali.mathapp.util.Constants.EASY_60
import com.sherali.mathapp.util.Constants.EASY_90
import com.sherali.mathapp.util.Constants.HARD
import com.sherali.mathapp.util.Constants.HARD_45
import com.sherali.mathapp.util.Constants.HARD_60
import com.sherali.mathapp.util.Constants.HARD_90
import com.sherali.mathapp.util.Constants.MEDIUM
import com.sherali.mathapp.util.Constants.MEDIUM_45
import com.sherali.mathapp.util.Constants.MEDIUM_60
import com.sherali.mathapp.util.Constants.MEDIUM_90
import com.sherali.mathapp.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random


@AndroidEntryPoint
class GameFragment : Fragment(), OnClickListener {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()


    private var timerChoose = (45000).toLong()
    private var score = 0
    private var questionNumber = 1
    private var minBound = 0
    private var maxBound = 0
    private var random = Random()
    private var answer: Int = 0
    private var answerLocation = -1
    private val option: MutableList<Int> = ArrayList()
    private var highScore = 0
    private var inCorrectAnswer: Int = 0
    private lateinit var countDownTimer: CountDownTimer
    private var level: String = ""
    private var currentLevel = ""
    private var progressTimeCounter = (timerChoose / 1000).toInt()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        level = arguments?.getString("level").toString()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Kategoriya tanlanganda o'yin oynada levelni olib yozish
        takeGameCondition()
        // levelga mos ma'lumotlarni yozish
        init()
        // vaqtni o'zgartirish
        spinner()

        // orqa tugma bosilishi
        binding.backButton.setOnClickListener {
//            startActivity(Intent(this, MenuActivity::class.java))
//            finish()
        }

        binding.option1.setOnClickListener(this)
        binding.option2.setOnClickListener(this)
        binding.option3.setOnClickListener(this)
        binding.option4.setOnClickListener(this)

    }

    // levelga mos ma'lumotlarni yozish
    private fun init() {
        binding.scoreAndQuestionsNumber.text = "$score/$questionNumber"
        generateQuestions()
        startTime(timerChoose)
        getHighScore((timerChoose / 1000).toString())

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
                inCorrectAnswer = random.nextInt(maxBound * 2) + minBound
                while (inCorrectAnswer == answer || inCorrectAnswer in option) {
                    inCorrectAnswer = random.nextInt(maxBound * 2) + minBound
                }
                option.add(inCorrectAnswer)
            }
        }
//
        binding.option1.text = option[0].toString()
        binding.option2.text = option[1].toString()
        binding.option3.text = option[2].toString()
        binding.option4.text = option[3].toString()

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

    override fun onClick(v: View?) {
        if (view?.tag.toString() == answerLocation.toString()) {
            score++
            answerToast(true)
            //agar o'yinchini bali rekordan katta bo'lsa o'yin oynada rekorni o'zgartirish
            if (score > highScore) {
                highScore++
                binding.highScore.text = highScore.toString()
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
        val dialogOver = GameOverDialog(
            binding.root.context,
            score,
            highScore,
            questionNumber,
            level
        ) {
            if (it) {
                restart()
            } else {
                findNavController().popBackStack()
            }
        }

        dialogOver.show()

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
            binding.root.context, R.layout.spinner_item, timer
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
        when (level) {
            "1" -> {
                minBound = 10
                maxBound = 50
                currentLevel = EASY
            }

            "2" -> {
                minBound = 51
                maxBound = 100
                currentLevel = MEDIUM
            }

            else -> {
                minBound = 101
                maxBound = 150
                currentLevel = HARD
            }
        }

        binding.categoryName.text = currentLevel
    }

    // Rekordni olish, level tartib raqami va tanlangan vaqt bo'yicha rekord olinadi
    private fun getHighScore(time: String) {

        var key = ""


        when (level) {
            "1" -> {
                key = when (time) {
                    "45" -> {
                        EASY_45
                    }

                    "60" -> {
                        EASY_60
                    }

                    else -> {
                        EASY_90
                    }
                }
            }

            "2" -> {
                key = when (time) {
                    "45" -> {
                        MEDIUM_45
                    }

                    "60" -> {
                        MEDIUM_60
                    }

                    else -> {
                        MEDIUM_90
                    }
                }
            }

            else -> {
                key = when (time) {
                    "45" -> {
                        HARD_45
                    }

                    "60" -> {
                        HARD_60
                    }

                    else -> {
                        HARD_90
                    }
                }
            }
        }

        observeGetScore(key)
    }

    private fun observeGetScore(key: String) {
        mainViewModel.getScore(key).observe(requireActivity()) {
            if (it != null) {
                highScore = it.score
                binding.highScore.text = highScore.toString()
            }
        }
    }

    // Rekordni saqlash, amaldagi level va vaqtda saqlaydi
    private fun putHighScore(time: String) {


        var userScore: ScoreEntity? = null
        when (level) {
            "1" -> {
                userScore = when (time) {
                    "45" -> {
                        ScoreEntity(EASY_45, currentLevel, "Easy", time, highScore)
                    }

                    "60" -> {
                        ScoreEntity(EASY_60, currentLevel, "Easy", time, highScore)
                    }

                    else -> {
                        ScoreEntity(EASY_90, currentLevel, "Easy", time, highScore)
                    }
                }
            }

            "2" -> {
                userScore = when (time) {
                    "45" -> {
                        ScoreEntity(MEDIUM_45, currentLevel, "Medium", time, highScore)
                    }

                    "60" -> {
                        ScoreEntity(MEDIUM_60, currentLevel, "Medium", time, highScore)
                    }

                    else -> {
                        ScoreEntity(MEDIUM_90, currentLevel, "Medium", time, highScore)
                    }
                }
            }

            else -> {
                userScore = when (time) {
                    "45" -> {
                        ScoreEntity(HARD_45, currentLevel, "Hard", time, highScore)
                    }

                    "60" -> {
                        ScoreEntity(HARD_60, currentLevel, "Hard", time, highScore)
                    }

                    else -> {
                        ScoreEntity(HARD_90, currentLevel, "Hard", time, highScore)
                    }
                }
            }
        }


        mainViewModel.saveScore(userScore)

    }

    // Rekorni saqlash funksiyasi
    fun saveRecord() {
        // score rekorddan katta bo'lsagina u share preferences ga saqlaydi
        if (score >= highScore) {
            putHighScore((timerChoose / 1000).toString())
        }
    }


    // Rekordni amaldagi levelga yozish
//    private fun initSaveHighScore() {
//        val str = getHighScore((timerChoose / 1000).toString())
//        highScore = if (str != "") {
//            str.toInt()
//        } else 0
//
//        binding.highScore.text = highScore.toString()
//    }

    // pausa bo'lganda rekordni saqlash
    override fun onPause() {
        super.onPause()
        saveRecord()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}