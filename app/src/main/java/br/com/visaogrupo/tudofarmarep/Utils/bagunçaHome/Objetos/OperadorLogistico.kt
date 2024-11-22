package br.com.visaogrupo.tudofarmarep.Objetos

import br.com.visaogrupo.tudofarmarep.Views.Activitys.ActivityPrincipal

data class OperadorLogistico(
    val OperadorLogistico_Grupo_ID: Int,
    val OperadorLogistico_ID: Int,
    val Nome: String,
    var itemSelcionado : Boolean = false,
    var principal: Boolean = false

)