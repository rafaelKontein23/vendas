package br.com.visaogrupo.tudofarmarep.Objetos

data class Menulateral(
    val Agrupado: Boolean,
    val Clicavel: Int,
    val Funcionalidade_id: Int,
    val IconeClass: String,
    val Link: String,
    val Mensagem: String,
    val Ordenacao: Int,
    val Status_cod: Int,
    val Titulo: String,
    val Agrupados: ArrayList<Agrupado> = ArrayList()
    )