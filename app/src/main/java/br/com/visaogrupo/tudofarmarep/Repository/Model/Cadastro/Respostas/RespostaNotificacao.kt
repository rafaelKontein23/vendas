package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

import java.util.Date


data class RespostaNotificacaoDados(
    val Dados: ArrayList<RespostaNotificacao>,
    val Mensagem: String,
    val Valido: String,
    val Resultado : String

)
data class RespostaNotificacao(
     val Representante_id: Int,
     val Titulo: String,
     val Mensagem: String,
     val Categoria:String,
     val Push_id:Long,
     val Lido : Boolean,
    val DtPush:String
)
