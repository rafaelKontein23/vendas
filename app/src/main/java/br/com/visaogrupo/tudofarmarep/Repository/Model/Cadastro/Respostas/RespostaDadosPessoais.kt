package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaDadosPessoaisDados(
    val Dados : List<RespostaDadosPessoais>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)

data class RespostaDadosPessoais(
    val Nome:String,
    val Sobrenome:String,
    val CPF:String,
    val DataNascimento:String,
    val TelefoneComercial:String,
    val email:String,
)