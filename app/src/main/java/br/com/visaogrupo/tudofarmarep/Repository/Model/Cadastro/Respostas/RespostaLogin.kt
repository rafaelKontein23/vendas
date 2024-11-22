package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaLoginDados (
    val Dados : List<RespostaLogin>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)

data class RespostaLogin(
    val Representante_ID: Int,
    val Nome: String,
    val Celular:String,
    val CNPJ:String,
    val Teste:Boolean,
    val FotoPerfil:String,
    val FotoDocumento:String,
    val Hash:String,
    val Mensagem:String,
    val Status_Cod:Int,
)
data class RespostaLoginErro(
    val status_cod:Int,
    val Mesnsagem:String
)