package com.sherali.mathapp.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import com.sherali.mathapp.R
import com.sherali.mathapp.databinding.EditNameAlertDialogBinding
import com.sherali.mathapp.util.Functions.Companion.checkNameStatus



class ProfileEditDialog(
    context: Context,
    name: String,
    index: Int,
    callbacks: (newName: String,newIndex: Int,editStatus: Boolean,message: String)->Unit
) : BaseAlertDialog(context), OnClickListener {
    private val binding = EditNameAlertDialogBinding.inflate(LayoutInflater.from(context))
    private var gamer = name

    private var iconIndex = index

    init {

        binding.playerName.setText(gamer)
        binding.iconOne.setOnClickListener(this)
        binding.iconTwo.setOnClickListener(this)
        binding.iconThree.setOnClickListener(this)
        binding.iconFour.setOnClickListener(this)




        binding.save.setOnClickListener {
            gamer = binding.playerName.text.toString().trim()

            if (checkNameStatus(gamer, iconIndex) == "") {
                callbacks(gamer,iconIndex,true,"")

                this.dismiss()
            } else {
                callbacks(gamer,iconIndex,false,checkNameStatus(gamer, iconIndex))
                this.dismiss()
            }
        }

        //cancel bosilganda   o'zini default icon va ismini oladi
        binding.cancel.setOnClickListener {
            callbacks(gamer,iconIndex,true,"")
            this.dismiss()

        }
        this.setView(binding.root)
    }

    override fun onClick(view: View?) {
        val gridContainer = binding.gridContainer
        val imageView = view as ImageView
        iconIndex = Integer.parseInt(imageView.tag.toString())
        for (i in 0 until 4) {
            if (i == iconIndex) gridContainer.getChildAt(i)
                .setBackgroundResource(R.drawable.selected_icon_background)
            else gridContainer.getChildAt(i)
                .setBackgroundResource(R.drawable.noselected_icon_background)
        }
    }
}