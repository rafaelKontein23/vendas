package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaApiDadosToken (
    val Dados: List<RespostaSolicitaToken>,
    val Mensagem: String,
    val Valido: Int,
    val Resultado: String

)
data class RespostaSolicitaToken(
    val mensagem: String,
    val  TempoTokenSegundos : Int,
    var whats:Int = 0
)