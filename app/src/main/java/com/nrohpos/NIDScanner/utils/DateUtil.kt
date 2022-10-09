package com.nrohpos.NIDScanner.utils

import java.text.SimpleDateFormat
import java.util.*


class DateUtil {
    companion object {
        fun formatDateToPicker(date: String): Long? {
            val formatDate = SimpleDateFormat("dd-MM-yy", Locale.ENGLISH).parse(date)//, Locale.US
            return (formatDate?.time?.plus(GregorianCalendar().timeZone.rawOffset))
        }

        fun formatPickerToDateExp(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("dd-MM-yy", Locale.ENGLISH)
            return format.format(date)
        }
    }
}