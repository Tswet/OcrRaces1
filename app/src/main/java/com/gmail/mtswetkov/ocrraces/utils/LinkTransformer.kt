package com.gmail.mtswetkov.ocrraces.utils

import android.text.format.DateFormat
import java.util.*

class LinkTransformer {

    private val eventIdTransFormArray: Array<String> = arrayOf(
            "WncyDgkdp",
            "vb7ErOObd",
            "nVy9FFldU",
            "Be4nvGd0y",
            "vvU0drTiI",
            "bnYukdUil",
            "LoifTyrNv",
            "gh0nd6ty",
            "pOt0rtyRi",
            "0viiK1thi",
            "ngUirKmdY"
    )

    private val idTransformFromArray: Array<String> = arrayOf(
            "g",
            "R",
            "Y",
            "k",
            "o",
            "x",
            "M",
            "v",
            "A",
            "d"
    )

    private fun blaBlaChange(): String {

        val hour = DateFormat.format("hh", Calendar.getInstance().time).toString()
        return eventIdTransFormArray[hour.toInt()]

    }


    private fun idChange(str: String): String {

        val sb: StringBuilder = java.lang.StringBuilder()
        val id = str.substring(39).toCharArray()
        for(s in id) {
            sb.append(idTransformFromArray[s.toString().toInt()])
        }
        return sb.toString()
    }


    fun codLink(lnk: String) : String {
        val url = "https://sportcontest.ru/share?"

        return url + blaBlaChange() + idChange(lnk)

    }


    fun deCodLink(lnk: String): String {

        val sb: StringBuilder = java.lang.StringBuilder()
        val url = "https://sportcontest.ru/share?event_id="

        sb.append(url)
        val id = lnk.substring(39)
        for (s in id) {
            sb.append(idTransformFromArray.indexOf(s.toString()))
        }
        return sb.toString()
    }
}