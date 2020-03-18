package alektas.gamesheap.utils

import android.util.Log

class StringUtils {

    companion object {
        private const val TAG = "StringUtils"
        /**
         * @date Date format: yyyy-MM-dd kk:mm:ss, for example: 1992-02-29 00:00:00
         * @return Parsed year
         */
        fun parseYear(date: String): Int {
            try {
                return Integer.parseInt(date.substringBefore("-"))
            } catch (e: NumberFormatException) {
                Log.e(TAG,"Date format is not valid. It should be in that format: " +
                        "yyyy-MM-dd kk:mm:ss, for example: 1992-02-29 00:00:00", e)
                return 0
            }
        }

        fun parseInt(intString: String): Int {
            return try {
                intString.toInt()
            } catch (e: java.lang.NumberFormatException) {
                0
            }
        }
    }
}