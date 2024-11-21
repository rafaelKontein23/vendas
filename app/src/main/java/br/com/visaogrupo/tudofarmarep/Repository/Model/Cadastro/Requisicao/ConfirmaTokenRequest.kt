package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao

data class ConfirmaTokenRequest (

    val Email:String,
    val Telefone:String,
    val Token:String,
    val UDID:String,
    val CNPJ:String

)