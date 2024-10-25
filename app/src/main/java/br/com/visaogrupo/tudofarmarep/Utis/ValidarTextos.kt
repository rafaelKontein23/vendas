package br.com.visaogrupo.tudofarmarep.Utis

import java.util.InputMismatchException



class ValidarTextos {
    companion object{
        fun isCNPJ( CNPJ: String): Boolean {
            var CNPJ = CNPJ.replace(".", "").replace("-", "").replace("/", "")

            if (CNPJ == "00000000000000" || CNPJ == "11111111111111" || CNPJ == "22222222222222" || CNPJ == "33333333333333" || CNPJ == "44444444444444" || CNPJ == "55555555555555" || CNPJ == "66666666666666" || CNPJ == "77777777777777" || CNPJ == "88888888888888" || CNPJ == "99999999999999" ||
                (CNPJ.length != 14)
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

                    num = (CNPJ[i].code - 48)
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
                    num = (CNPJ[i].code - 48)
                    sm = sm + (num * peso)
                    peso = peso + 1
                    if (peso == 10) peso = 2
                    i--
                }

                r = sm % 11
                dig14 = if ((r == 0) || (r == 1)) '0'
                else ((11 - r) + 48).toChar()
                return if ((dig13 == CNPJ[12]) && (dig14 == CNPJ[13])) true
                else false
            } catch (erro: InputMismatchException) {
                return (false)
            }
        }

    }
}