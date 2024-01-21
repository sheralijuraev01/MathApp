package com.sherali.mathapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
 import com.sherali.mathapp.R
import com.sherali.mathapp.data.local.room.entity.ScoreEntity


class HighScoreAdapter(private val context: Context, private val list: MutableList<ScoreEntity>):BaseAdapter (){

    private val inflater:LayoutInflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
       return position.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val listItem=inflater.inflate(R.layout.list_item_for_high_score,parent,false)
        val placeScore=listItem.findViewById<TextView>(R.id.placeScore)
        val level=listItem.findViewById<TextView>(R.id.levelUp)
        val time=listItem.findViewById<TextView>(R.id.levelOfTime)
        val score=listItem.findViewById<TextView>(R.id.levelOfScore)

        placeScore.text=(position+1).toString()
        level.text=list[position].levelName
        time.text=list[position].time
        score.text=list[position].score.toString()

        return listItem
    }
}