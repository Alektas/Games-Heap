package alektas.gamesheap.common.utils

import android.util.Log

class Parser {
    companion object {
        fun parseInt(string: String): Int {
            return try {
                string.toInt()
            } catch (e: java.lang.NumberFormatException) {
                -1
            }
        }

        /**
         * @date Date format: yyyy-MM-dd kk:mm:ss, for example: 1992-02-29 00:00:00
         * @return Parsed year
         */
        fun parseYearFromDate(date: String): Int {
            return try {
                Integer.parseInt(date.substringBefore("-"))
            } catch (e: NumberFormatException) {
                Log.e("Parser","Date format is not valid. It should be in that format: " +
                        "yyyy-MM-dd kk:mm:ss, for example: 1992-02-29 00:00:00", e)
                0
            }
        }
    }
}