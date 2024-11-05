package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao

data class CadastroRequest(
    var possuiCore: String = "",
    var CNPJ: String = "",
    var RazaoSocial: String = "",
    var Fantasia: String = "",
    var CEP: String = "",
    var Endereco: String = "",
    var Bairro: String = "",
    var Cidade: String = "",
    var UF: String = "",
    var nome:String = "",
    var sobrenome:String = "",
    var cpf :String = "",
    var dataNascimento:String = "",
    var celular: String = "",
    var telefoneComercial: String = "",
    var email: String = "",
)
