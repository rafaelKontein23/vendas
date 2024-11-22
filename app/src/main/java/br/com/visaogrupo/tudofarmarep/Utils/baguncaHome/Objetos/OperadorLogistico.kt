package br.com.visaogrupo.tudofarmarep.Objetos


data class OperadorLogistico(
    val OperadorLogistico_Grupo_ID: Int,
    val OperadorLogistico_ID: Int,
    val Nome: String,
    var itemSelcionado : Boolean = false,
    var principal: Boolean = false

)