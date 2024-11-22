package br.com.visaogrupo.tudofarmarep.Objetos

data class Cnpj(
    val CNPJ: String,
    val Cidade: String,
    val Complemento: String,
    val Distancia: String,
    val Endereco: String,
    val Numero: String,
    val RazaoSocial: String,
    val UF: String,
    val atributos: ArrayList<Atributo> = ArrayList(),
    val carteira : Boolean = false,
    val bairro : String = "",

    ):java.io.Serializable