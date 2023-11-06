package com.sherali.mathapp.util

class Functions {
    companion object {
        private fun checkMaxLength(name: String): Boolean {
            return name.length > 14
        }

        fun checkStandardLength(name: String): Boolean {
            return name.length < 9
        }

        private fun checkMinLength(name: String): Boolean {
            return name.length < 3
        }

        fun checkNameCondition(name: String): String {
            return if (checkMaxLength(name)) "The name is too long"
            else if (checkMinLength(name)) "the name is too short"
            else ""
        }
    }
}