package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaCadastroDados (
val Dados: ArrayList<RespostaCadastro>,
val Mensagem: String,
val Valido: Int,
val Resultado:String

)
data class  RespostaCadastro(
    val Sucesso: Int,
    val Mensagem: String,
)