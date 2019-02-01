package com.gmail.mtswetkov.ocrraces


import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.city_list_layout.view.*


class CityAndCounryList : DialogFragment() {

    private var list: MutableList<String> = mutableListOf()
    private var choosenList: MutableList<String> = mutableListOf()


    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
            ExtendedMenuActivity.mOnChange.setBoo(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val myView = inflater.inflate(R.layout.city_list_layout
                , container, false)
        val mNum = arguments!!.getInt("num")
        val ll: LinearLayout = myView.city_country_list_layout
        val title: TextView = myView.cityOrCountyTitle
        val titleText = if (mNum == 1) {
            getString(R.string.chose_сity)
        } else getString(R.string.chose_country)
        title.text = titleText

        if (mNum == 1) {
            list = ExtendedMenuActivity.cities
            choosenList = ExtendedMenuActivity.choosenCity
        } else {
            list = ExtendedMenuActivity.countries
            choosenList = ExtendedMenuActivity.choosenCountry
        }



        list.sort()
        for (l in list) {
            val item = CheckBox(activity)
            item.text = l
            if (ExtendedMenuActivity.choosenCity.contains(l)) item.isChecked = true
            if (ExtendedMenuActivity.choosenCountry.contains(l)) item.isChecked = true
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
                Log.d("city_size", choosenList.size.toString())
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