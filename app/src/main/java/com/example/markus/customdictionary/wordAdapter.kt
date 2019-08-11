package com.example.markus.customdictionary

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.TextView

class wordAdapter(private var words: ArrayList<dictElement>):RecyclerView.Adapter<wordAdapter.wordHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): wordHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dictionaryitem, parent, false)
        return wordHolder(view)
    }

    override fun onBindViewHolder(wordHolder: wordHolder, position: Int) {
        wordHolder.word.text = words[position].entry.split(":")[0]
        wordHolder.meaning.text = words[position].entry.split(":")[1]

        wordHolder.selectedBox.setChecked(words[position].isSelected)
        Log.d("SELECTED POS: " ,"MESSAGE AT " + position + " is selected: " + words[position].isSelected)
        wordHolder.selectedBox.setOnCheckedChangeListener( CompoundButton.OnCheckedChangeListener { _, isChecked ->
            words[position].isSelected = isChecked
        })

        if(words.get(0).type == SortingType.FAMILIARITY){
            var avg = 0
            var sum = 0
            for (i in words.indices) {
                sum += words.get(i).getFamiliarity()
            }
            avg = sum / words.size
            val difference = Math.abs(avg - words.get(position).getFamiliarity())
            if (words.get(position).getFamiliarity() < avg && difference >= 5) {
                wordHolder.container.setBackgroundResource(R.drawable.corners_red)
            } else if (words.get(position).getFamiliarity() < avg && difference < 5) {
                wordHolder.container.setBackgroundResource(R.drawable.corners_orange)
            } else if (words.get(position).getFamiliarity() == avg) {
                wordHolder.container.setBackgroundResource(R.drawable.corners_yellow)
            } else if (words.get(position).getFamiliarity() > avg && difference < 10) {
                wordHolder.container.setBackgroundResource(R.drawable.corners_yellowgreen)
            } else if (words.get(position).getFamiliarity() > avg && difference >= 10) {
                wordHolder.container.setBackgroundResource(R.drawable.corners_green)
            }

        }else{
            wordHolder.container.setBackgroundResource(R.drawable.corners);
        }

    }


    override fun getItemCount(): Int {
        return words.size
    }

    class wordHolder(wordView: View) : RecyclerView.ViewHolder(wordView){
        val word = wordView.findViewById(R.id.word) as TextView
        val meaning = wordView.findViewById(R.id.meaning) as TextView
        var selectedBox = wordView.findViewById(R.id.list_item) as CheckBox
        val container = wordView.findViewById(R.id.container) as RelativeLayout


    }
    fun update(dictionaryItems:ArrayList<dictElement>){
        words = dictionaryItems
        this.notifyDataSetChanged()
    }
    fun getItems():ArrayList<dictElement>{
        return words
    }

}
