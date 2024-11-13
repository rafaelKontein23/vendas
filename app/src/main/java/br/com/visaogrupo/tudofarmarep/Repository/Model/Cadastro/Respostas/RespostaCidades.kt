package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaCidadesDados(
    val Dados: ArrayList<RespostaCidades>,
    val Mensagem: String,
    val Valido: Int,
    val Resultado:String

)
data class RespostaCidades(
    val ID: Int,
    val Cidade: String
)