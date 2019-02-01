package com.gmail.mtswetkov.ocrraces.Utils


class SharingReceiverLinkDispatcher {

    private var outcomesStr: String = "0"

    fun linkProcess(str: String?): Int {
        if (str != null) {
            outcomesStr = str.substring(39)
        }
        return outcomesStr.toInt()
    }
}