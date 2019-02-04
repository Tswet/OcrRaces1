package com.gmail.mtswetkov.ocrraces.utils

class MonthNameFormater{

    var monthName : String = ""

   fun formater(monthInt: Int) : String{
       when(monthInt){
           0 -> monthName = "ЯНВ"
           1 -> monthName = "ФЕВ"
           2 -> monthName = "МАР"
           3 -> monthName = "АПР"
           4 -> monthName = "МАЙ"
           5 -> monthName = "ИЮН"
           6 -> monthName = "ИЮЛ"
           7 -> monthName = "АВГ"
           8 -> monthName = "СЕН"
           9 -> monthName = "ОКТ"
           10 -> monthName = "НОЯ"
           11 -> monthName = "ДЕК"
       }
       return monthName
   }

    fun formaterText(monthInt: Int) : String{
        when(monthInt){
            0 -> monthName = "Января"
            1 -> monthName = "Февраля"
            2 -> monthName = "Марта"
            3 -> monthName = "Апреля"
            4 -> monthName = "Мая"
            5 -> monthName = "Июня"
            6 -> monthName = "Июля"
            7 -> monthName = "Августа"
            8 -> monthName = "Сентября"
            9 -> monthName = "Октября"
            10 -> monthName = "Ноября"
            11 -> monthName = "Декабря"
        }
        return monthName
    }


}