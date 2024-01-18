package com.sherali.mathapp.ui.dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import androidx.appcompat.app.AlertDialog

open class BaseAlertDialog(context: Context) : AlertDialog(context) {
    init {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.attributes.gravity = Gravity.CENTER
        this.setCanceledOnTouchOutside(false)
        this.setCancelable(false)
    }
}