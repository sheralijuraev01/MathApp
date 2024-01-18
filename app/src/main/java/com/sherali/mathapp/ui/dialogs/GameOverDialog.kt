package com.sherali.mathapp.ui.dialogs

import android.content.Context

import android.view.LayoutInflater

import com.sherali.mathapp.R
import com.sherali.mathapp.databinding.GameOverAlertDialogBinding

class GameOverDialog(
    context: Context,
    score: Int,
    highScore: Int,
    questionCount: Int,
    level: String,
    var clickStatus: (Boolean) -> Unit
) : BaseAlertDialog(context) {

    private val binding = GameOverAlertDialogBinding.inflate(LayoutInflater.from(context))

    init {
        /**
         * goodScore-bu foiz, qaysiki jami savoldan foydalanuvchi topgan savollar foizi,
         * agar 60 dan yuqori bo'lsa
        natija good(yaxshi), 60 fozidan kichik bo'lsa bad(yomon),
        agarda score rekordan baland bo'lsa High score(Yuqori natija)
         */
        val goodScore = (score * 100) / questionCount
        if (score >= highScore && highScore != 0) {
            binding.lottiAnimation.setAnimation(R.raw.best)
            binding.infoGame.text = "High Score"
        } else if (goodScore > 60) {
            binding.lottiAnimation.setAnimation(R.raw.good)
            binding.infoGame.text = "Good"

        } else {
            binding.lottiAnimation.setAnimation(R.raw.bad)
            binding.infoGame.text = "Bad"
        }

        binding.level.text = "Level\n$level"
        binding.questions.text = "Questions\n$questionCount"
        binding.gamerScore.text = "Score\n$score"




        // qayta o'yinni boshlash
      binding.restart.setOnClickListener {
            clickStatus(true)
            dismiss()
        }
        //menu oygana qaytish
        binding.home.setOnClickListener {
            clickStatus(false)
            dismiss()
        }
        setView(binding.root)
    }
}