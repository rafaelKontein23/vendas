package br.com.visaogrupo.tudofarmarep.Carga.ultis

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RecuperasDatas {
    companion object{
        fun recuperaDataFormat(mesAtualNome:String, anoAtual:String):String{


            val formatoEntrada = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

            val data = formatoEntrada.parse("$mesAtualNome $anoAtual")

            val formatoSaida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val dataFormatada = formatoSaida.format(data!!)

            return dataFormatada
        }

        fun recuperaAnoAtual():String{
            return  Calendar.getInstance().get(Calendar.YEAR).toString()
        }
        fun recuperaMesAtual():String{
            return  SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())

        }

    }

}