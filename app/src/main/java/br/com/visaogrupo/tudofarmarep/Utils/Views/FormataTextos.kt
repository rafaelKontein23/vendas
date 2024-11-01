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

    }
}