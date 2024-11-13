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
    var hash: String = "",
)


data class CadastroRequestAreaAtuacal(
    val UF: String = "",
    val Mesorregioes: List<Mesorregiao> = emptyList()
)

data class Mesorregiao(
    val Mesorregiao: String,
    val Mesorregiao_id: Int,
    val Cidades: ArrayList<Cidade>
)

data class Cidade(
    val Cidade: String
)