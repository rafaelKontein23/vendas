package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaLoginDados (
    val Dados : List<RespostaLogin>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)

data class RespostaLogin(
    val status_cod:Int,
    val Mesnsagem:String
)