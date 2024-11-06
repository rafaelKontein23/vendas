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
        fun isCPF(cpf: String): Boolean {
            val cpfFormatado = cpf.replace(".", "").replace("-", "")
            if (cpfFormatado == "00000000000" || cpfFormatado == "11111111111" || cpfFormatado == "22222222222" ||
                cpfFormatado == "33333333333" || cpfFormatado == "44444444444" || cpfFormatado == "55555555555" ||
                cpfFormatado == "66666666666" || cpfFormatado == "77777777777" || cpfFormatado == "88888888888" ||
                cpfFormatado == "99999999999" || cpfFormatado.length != 11) {
                return false
            }

            return try {
                // Cálculo do primeiro dígito verificador
                var sm = 0
                var peso = 10
                for (i in 0 until 9) {
                    val num = cpfFormatado[i].digitToInt()
                    sm += num * peso
                    peso -= 1
                }

                var r = 11 - (sm % 11)
                val dig10 = if (r == 10 || r == 11) '0' else (r + '0'.code).toChar()

                // Cálculo do segundo dígito verificador
                sm = 0
                peso = 11
                for (i in 0 until 10) {
                    val num = cpfFormatado[i].digitToInt()
                    sm += num * peso
                    peso -= 1
                }

                r = 11 - (sm % 11)
                val dig11 = if (r == 10 || r == 11) '0' else (r + '0'.code).toChar()

                // Verifica se os dígitos calculados são iguais aos informados
                dig10 == cpfFormatado[9] && dig11 == cpfFormatado[10]
            } catch (e: Exception) {
                false
            }
        }

        

    fun isCelular(celular: String): Boolean {
             val validaCelular = celular.length < 14
            return validaCelular
        }

    }
}