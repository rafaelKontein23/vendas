package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao

data class ConfirmaTokenRequest (

    val email:String,
    val telefone:String,
    val token:String,
    val udid:String,
    val cnpj:String

)