package com.gmail.mtswetkov.ocrraces.btnAction

import android.content.Context
import android.widget.ImageButton
import com.gmail.mtswetkov.ocrraces.R
import com.gmail.mtswetkov.ocrraces.ShowSingleRaceActivity
import com.gmail.mtswetkov.ocrraces.model.Event
import com.gmail.mtswetkov.ocrraces.model.SharedPrefWorker

class FavoritBtnClick {

    private var favList: MutableList<Int> = mutableListOf(0)

    fun click(event: Event, btn : ImageButton, context: Context, favList: MutableList<Int>, imageTipe : Int){
        this.favList = favList
        if (event.favourite) {
            when(imageTipe){
                1 -> btn.setImageResource(R.drawable.starsm)
                2 -> btn.setImageResource(R.drawable.star)
            }
                        event.favourite = false
            favList.remove(event.id)
        } else {
            when(imageTipe){
                1 -> btn.setImageResource(R.drawable.starrsm)
                2 -> btn.setImageResource(R.drawable.starr)
            }
            event.favourite = true
            favList.add(event.id)
        }
        SharedPrefWorker(context).setFavoritsList(favList)

    }
}