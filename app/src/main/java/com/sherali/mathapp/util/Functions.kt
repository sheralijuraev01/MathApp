package com.sherali.mathapp.util

class Functions {
    companion object {
        fun checkNameStatus(name: String, chooseItem: Int): String {
            return   if (name.isEmpty()) "The name must not be empty"
            else if (chooseItem == -1) "Please select icon for profile"
            else if (name.length > 14) "The name is too long"
            else if (name.length < 3) "The name is too short"
            else ""
        }

    }
}