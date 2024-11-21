package br.com.visaogrupo.tudofarmarep.Utils.Views

import com.vicmikhailau.maskededittext.MaskedEditText
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher

class FormataTextos {
    companion object{
        fun colocaMascaraInput(editText: MaskedEditText, formato: String) {
            val formatter = MaskedFormatter(formato)
            editText .addTextChangedListener(MaskedWatcher(formatter,  editText))
        }
        fun removeMascaraCelular(celular: String):String {
            val celularSemFormatacao = celular.replace("(", "").replace(")", "").replace(" ", "").replace("-", "")
            return celularSemFormatacao
        }
        fun removeMascaraCNPJ(cnpj: String):String {
            val cnpjSemFormatacao = cnpj.replace(".", "").replace("/", "").replace("-", "")
            return cnpjSemFormatacao
        }
        fun removeMascaraTelefone(telefone: String):String {
            val telefoneSemFormatacao = telefone.replace(" ", "").replace("-", "")
            return telefoneSemFormatacao
        }

        fun String.aplicarMascaraTelefone(): String {
            if (this.length != 11) return this
            return buildString {
                append(this@aplicarMascaraTelefone.substring(0, 2))
                append(" ")
                append(this@aplicarMascaraTelefone.substring(2, 3))
                append(" ")
                append(this@aplicarMascaraTelefone.substring(3, 7))
                append("-")
                append(this@aplicarMascaraTelefone.substring(7))
            }
        }
        fun String.iniciaisNome(): String {
            try {
                val nomes = this.split(" ")
                val iniciais = nomes[0].first().toString() + nomes[1].first().toString()
                return iniciais
            }catch (e:Exception){
                return this.first().toString()
            }

        }
        fun String.aplicarMascaraCnpj(): String {
            if (this.length != 14) return this
            return buildString {
                append(this@aplicarMascaraCnpj.substring(0, 2))
                append(".")
                append(this@aplicarMascaraCnpj.substring(2, 5))
                append(".")
                append(this@aplicarMascaraCnpj.substring(5, 8))
                append("/")
                append(this@aplicarMascaraCnpj.substring(8, 12))
                append("-")
                append(this@aplicarMascaraCnpj.substring(12))
            }
        }
        fun String.aplicarMascaraCep(): String {
            if (this.length != 8) return this
            return buildString {
                append(this@aplicarMascaraCep.substring(0, 5))
                append("-")
                append(this@aplicarMascaraCep.substring(5))
            }
        }

        fun String.obterNomeCompletoUF(sigla: String):String{
            val estadosMap = mapOf(
                "AC" to "Acre",
                "AL" to "Alagoas",
                "AP" to "Amapá",
                "AM" to "Amazonas",
                "BA" to "Bahia",
                "CE" to "Ceará",
                "DF" to "Distrito Federal",
                "ES" to "Espírito Santo",
                "GO" to "Goiás",
                "MA" to "Maranhão",
                "MT" to "Mato Grosso",
                "MS" to "Mato Grosso do Sul",
                "MG" to "Minas Gerais",
                "PA" to "Pará",
                "PB" to "Paraíba",
                "PR" to "Paraná",
                "PE" to "Pernambuco",
                "PI" to "Piauí",
                "RJ" to "Rio de Janeiro",
                "RN" to "Rio Grande do Norte",
                "RS" to "Rio Grande do Sul",
                "RO" to "Rondônia",
                "RR" to "Roraima",
                "SC" to "Santa Catarina",
                "SP" to "São Paulo",
                "SE" to "Sergipe",
                "TO" to "Tocantins"
            )

            val nomeEstado = estadosMap[sigla] ?: "Estado Desconhecido"
            return "$nomeEstado - $sigla"
        }

        fun  String.formataTextoGrandes(texto: String, limite: Int): String{
            if(texto.length > limite){
                return texto.substring(0, limite) + "..."
            }else{
                return texto
            }
        }
    }
}