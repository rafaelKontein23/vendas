package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaConfirmaTokenDados (
    val Dados : List<RespostaConfirmaToken>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)

data class RespostaConfirmaToken(
    val Mensagem: String,
    val Sucesso: Boolean
)