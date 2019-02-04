package com.gmail.mtswetkov.ocrraces.utils

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.RelativeLayout.LayoutParams
import android.widget.RelativeLayout
import android.widget.TextView

class ActionBarModifier {


    fun modify(actionBar: android.support.v7.app.ActionBar?, context: Context, titleText: String) {

        val ab = actionBar

        val tv = TextView(context)
        val lp = RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, // Width of TextView
                LayoutParams.WRAP_CONTENT) // Height of TextView

        tv.setLayoutParams(lp)

        // Set text to display in TextView

        tv.setText(titleText)

        tv.setTextSize(20f)
        tv.setTypeface(null, Typeface.BOLD);

        // Set the text color of TextView
        tv.setTextColor(Color.RED)

        //tv.setBackgroundColor(R.attr.window)

        // Set TextView text alignment to center
        tv.setGravity(Gravity.CENTER)
        tv.setPadding(0, 0, 100,0)

        // Set the ActionBar display option
        ab!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)

        // Finally, set the newly created TextView as ActionBar custom view
        ab.setDisplayHomeAsUpEnabled(true)
        ab.setCustomView(tv)

    }
}
