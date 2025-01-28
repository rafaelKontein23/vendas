package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao



data class ConfirmaTokenRequest (
    val Celular:String,
    val Token:String,
    val CNPJ:String,
    val DeviceToken:String,
    val Plataforma:String,
    val UDID:String,
    val VersaoApp:String,
    val Dispositivo:String,
    val SistemaOperacional:String

)