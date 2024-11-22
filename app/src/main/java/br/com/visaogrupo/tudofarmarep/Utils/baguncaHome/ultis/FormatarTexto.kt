package br.com.visaogrupo.tudofarmarep.Carga.ultis

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatarTexto {

    fun formatarParaMoeda(numero: Number): String {
        val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR")) // Define o formato como moeda brasileira
        return formatoMoeda.format(numero)
    }
    fun formatCNPJ(cnpj: String): String {
        // Remove qualquer caractere que não seja número
        val cleanCNPJ = cnpj.filter { it.isDigit() }

        // Adiciona a máscara ao CNPJ
        return if (cleanCNPJ.length == 14) {
            "${cleanCNPJ.substring(0, 2)}.${cleanCNPJ.substring(2, 5)}.${cleanCNPJ.substring(5, 8)}/${cleanCNPJ.substring(8, 12)}-${cleanCNPJ.substring(12, 14)}"
        } else {
            cnpj
        }
    }
    fun formataDataMesAno(dataOriginal: String): String {
        val formatoOrigem = SimpleDateFormat("yyyy-MM-dd")
        val data: Date = formatoOrigem.parse(
            dataOriginal
        )

        val formatoDestino = SimpleDateFormat("dd/MM")
        val formatodiaMesano: String = formatoDestino.format(data)
        return formatodiaMesano
    }
}