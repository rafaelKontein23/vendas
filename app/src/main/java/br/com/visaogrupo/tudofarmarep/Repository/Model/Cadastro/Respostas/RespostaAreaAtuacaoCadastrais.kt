package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaAreaAtuacaoCadastraisDados(
    val Dados : List<RespostaAreaAtuacaoCadastrais>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)

data class RespostaAreaAtuacaoCadastrais(
    val UF:String,
    val Cidade:String,
    val Mesorregiao:String,
    val Mesorregiao_id:Int
)