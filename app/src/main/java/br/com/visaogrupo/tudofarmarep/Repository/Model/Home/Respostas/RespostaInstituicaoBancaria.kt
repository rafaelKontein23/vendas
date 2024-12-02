package br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas

data class RespostaInstituicaoBancariaDados (
    val Dados : List<RespostaInstituicaoBancaria>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)

data class RespostaInstituicaoBancaria(
    val Codigo:Int,
    val Descricao:String,
    val Site:String,
    val HabilitaCodigo:Boolean
)