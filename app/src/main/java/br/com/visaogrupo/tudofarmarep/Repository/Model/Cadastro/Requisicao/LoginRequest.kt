package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao

data class LoginRequest(
    val CNPJ:String,
    val Celular:String,
    val DeviceToken:String,
    val Plataforma:String = "Android",
    val UDID:String,
    val VersaoApp:String,
    val  Dispositivo:String

    )
