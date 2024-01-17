package com.sherali.mathapp.data.local.shared

interface SharedPref {
    suspend fun saveName(name: String)
    suspend fun getName(): String

    suspend fun saveIconIndex(index: Int)
    suspend fun getIconIndex(): Int

    suspend fun saveLogin(status: Boolean)
    suspend fun getLogin(): Boolean
}