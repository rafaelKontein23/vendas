package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas


data class RespostaNotificacaoDados(
    val Dados: ArrayList<RespostaNotificacao>,
    val Mensagem: String,
    val Valido: String,
    val Resultado : String

)
data class RespostaNotificacao(
     val Representante_id: Int,
    val titulo: String,
     val mensagem: String,
    val categoria:String
)
