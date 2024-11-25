package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao


data class SolicitaTokenRquest(
    val Celular:String,
    val UDID:String,
    val CNPJ :String,
    val DeviceToken :String,
)
