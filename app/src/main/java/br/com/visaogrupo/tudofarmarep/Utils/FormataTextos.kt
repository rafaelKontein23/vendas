package br.com.visaogrupo.tudofarmarep.Utils

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

    }
}