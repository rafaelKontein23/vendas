package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaExcluirCotacao(
    val Dados : List<RespostaDadosExcluirCotacao>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)

data class RespostaDadosExcluirCotacao(
    val Mensagem:String,
    val Sucesso:Boolean
)
