package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao


data class CadastroRequest(
    var possuiCoreText: String = "",
    var possuiCore:Boolean = false,
    var CNPJ: String = "",
    var UF: String = "",
    var nome:String = "",
    var sobrenome:String = "",
    var cpf :String = "",
    var dataNascimento:String = "",
    var celular: String = "",
    var telefoneComercial: String = "",
    var email: String = "",
    var hashIndicacao: String = "",
    var isPoliticaPrivacidade:Boolean = false,
    var isTermoPolitica :Boolean = false,
    var isAssinaContrato:Boolean = false,
    var DeviceToken: String = "",
    var UDID:String = "",
    var VersaoaAPP:String = "",
    var Dispositivo :String = "",
    var SistemaOperacional :String = "", // versão do android
    var Plataforma :String = "Android",
    var FotoDocumento :String = "",
    var ImagemAssinatura:String = ""


)


data class CadastroRequestAreaAtuacal(
    var UF: String = "",
    var Mesorregioes: List<Mesorregiao> = emptyList()
)

data class Mesorregiao(
    val Mesorregiao: String,
    val Mesorregiao_id: Int,
    val Cidades: ArrayList<Cidade>
)

data class Cidade(
    val Cidade: String
)

