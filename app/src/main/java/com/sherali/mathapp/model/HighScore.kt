package com.sherali.mathapp

data class HighScore(val level: String, val time: Int, val score: Int) : Comparable<HighScore> {
    override fun compareTo(other: HighScore): Int {
        return if (this.score < other.score)
            1
        else -1
    }

}


