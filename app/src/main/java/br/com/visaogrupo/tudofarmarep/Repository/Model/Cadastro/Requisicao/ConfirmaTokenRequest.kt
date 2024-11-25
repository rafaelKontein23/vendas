package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao

data class ConfirmaTokenRequest (
    val Celular:String,
    val Token:String,
    val CNPJ:String,
    val DeviceToken:String

)