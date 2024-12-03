package br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request

data class DadosBancariosRequests (
    var Banco:String = "",
    var Agencia:String = "",
    var Conta:String = "",
    var CNPJ:String = ""
)