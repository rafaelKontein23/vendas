package br.com.visaogrupo.tudofarmarep.Utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataUltis {
    companion object{
        fun obtemDataAtual(): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, 1)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return dateFormat.format(calendar.time)
        }
    }

}