package br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaAreaAtuacaoCadastrais

data class RespostaDadosBancariosDados (
    val Dados : List<RespostaDadosBancarios>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String

)

data class  RespostaDadosBancarios(
    val Banco:String,
    val Conta:String,
    val Agencia:String,
    val CNPJ:String,
    val RazaoSocial:String,
    val Status_cod:Int
)