package br.com.visaogrupo.tudofarmarep.Utils

import java.util.InputMismatchException



class ValidarTextos {
    companion object{
        fun isCNPJ( CNPJ: String): Boolean {
            var cnpjFormatado = CNPJ.replace(".", "").replace("-", "").replace("/", "")

            if (cnpjFormatado == "00000000000000" || cnpjFormatado == "11111111111111" || cnpjFormatado == "22222222222222" || cnpjFormatado == "33333333333333" || cnpjFormatado == "44444444444444" || cnpjFormatado == "55555555555555" || cnpjFormatado == "66666666666666" || cnpjFormatado == "77777777777777" || cnpjFormatado == "88888888888888" || cnpjFormatado == "99999999999999" ||
                (cnpjFormatado.length != 14)
            ) return (false)

            val dig13: Char
            val dig14: Char
            var sm: Int
            var i: Int
            var r: Int
            var num: Int
            var peso: Int

            try {
                sm = 0
                peso = 2
                i = 11
                while (i >= 0) {

                    num = (cnpjFormatado[i].code - 48)
                    sm = sm + (num * peso)
                    peso = peso + 1
                    if (peso == 10) peso = 2
                    i--
                }

                r = sm % 11
                dig13 = if ((r == 0) || (r == 1)) '0'
                else ((11 - r) + 48).toChar()

                sm = 0
                peso = 2
                i = 12
                while (i >= 0) {
                    num = (cnpjFormatado[i].code - 48)
                    sm = sm + (num * peso)
                    peso = peso + 1
                    if (peso == 10) peso = 2
                    i--
                }

                r = sm % 11
                dig14 = if ((r == 0) || (r == 1)) '0'
                else ((11 - r) + 48).toChar()
                return if ((dig13 == cnpjFormatado[12]) && (dig14 == cnpjFormatado[13])) true
                else false
            } catch (erro: InputMismatchException) {
                return (false)
            }
        }
    }
}