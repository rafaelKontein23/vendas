package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaCodigoIndicacaoDados (
    val Dados : List<RespostaCodigoIndicacao>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)

data class RespostaCodigoIndicacao(
    val CNPJ:String,
    val Nome:String,
    val Telefone:String
)