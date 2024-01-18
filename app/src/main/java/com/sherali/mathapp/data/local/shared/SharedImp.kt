package com.sherali.mathapp.data.local.shared

import android.content.Context
import com.sherali.mathapp.util.Constants.MATH_SHARED
import com.sherali.mathapp.util.Constants.USER_ICON
import com.sherali.mathapp.util.Constants.USER_LOGIN
import com.sherali.mathapp.util.Constants.USER_NAME
import javax.inject.Inject

class SharedImp (context: Context) : SharedPref {

    private val sharedPreference = context.getSharedPreferences(MATH_SHARED, Context.MODE_PRIVATE)

    override suspend fun  saveName(name: String) {
        sharedPreference.edit().putString(USER_NAME, name).apply()
    }

    override suspend fun getName(): String {
        return sharedPreference.getString(USER_NAME, "") ?: ""
    }

    override suspend fun saveIconIndex(index: Int) {
        sharedPreference.edit().putInt(USER_ICON, index).apply()
    }

    override suspend fun getIconIndex(): Int {
        return sharedPreference.getInt(USER_ICON, 4)
    }

    override suspend fun saveLogin(status: Boolean) {
        sharedPreference.edit().putBoolean(USER_LOGIN, status).apply()

    }

    override suspend fun getLogin(): Boolean {
        return sharedPreference.getBoolean(USER_LOGIN, false)

    }
}