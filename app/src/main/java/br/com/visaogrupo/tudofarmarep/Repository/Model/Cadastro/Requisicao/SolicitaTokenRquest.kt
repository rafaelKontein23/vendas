package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao


data class SolicitaTokenRquest(
    val Telefone:String,
    val UDID:String,
    val Email :String = "",
    val CNPJ :String,
    val CPF :String = "",
    val DeviceToken :String,

)
