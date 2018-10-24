package com.gmail.mtswetkov.ocrraces

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.city_list_layout.view.*

class CityAndCounryList : DialogFragment() {

    var list: MutableList<String> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater?, container:
    ViewGroup?, savedInstanceState: Bundle?): View {

        val myView = inflater!!.inflate(R.layout.city_list_layout
                , container, false)

        val mNum = getArguments().getInt("num");
        val ll: LinearLayout = myView.city_country_list_layout
        if (mNum == 1) {
            list = ExtendedMenuActivity.cities
        } else list = ExtendedMenuActivity.countries
        val choosenList: MutableList<String> = mutableListOf()

        list.sort()
        for (l in list) {
            val item = CheckBox(activity)
            item.setText(l)
            item.setOnClickListener {
                if (item.isChecked) {
                    choosenList.add(item.text.toString())
                } else {
                    choosenList.remove(item.text.toString())
                }
            }
            val line = TextView(activity)
            line.textSize = 1f
            line.setBackgroundResource(R.color.lightGray)
            ll.addView(item)
            ll.addView(line)
        }

        val confBtn = myView.confBtn
        confBtn.setOnClickListener {
            if (mNum == 1) {
                ExtendedMenuActivity.choosenCity = choosenList
            } else {
                ExtendedMenuActivity.choosenCountry = choosenList
            }
            ExtendedMenuActivity.mOnChange.setBoo(true)
            dialog.cancel()
        }
        return myView
    }

}