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
        fun formatarDataISO(isoDate: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                // Converte a data
                val date = inputFormat.parse(isoDate)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                "Formato inválido"
            }
        }
    }

}