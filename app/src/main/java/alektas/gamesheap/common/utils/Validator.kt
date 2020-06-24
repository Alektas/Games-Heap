package alektas.gamesheap.common.utils

class Validator {
    companion object {
        private const val MIN_VALID_YEAR = 1970
        private const val MAX_VALID_YEAR = 9999

        fun isValidPeriod(fromYear: Int, toYear: Int): Boolean {
            return isValidYear(fromYear) && isValidYear(toYear) && fromYear <= toYear
        }

        fun isValidYear(year: Int): Boolean = year in MIN_VALID_YEAR..MAX_VALID_YEAR
    }
}