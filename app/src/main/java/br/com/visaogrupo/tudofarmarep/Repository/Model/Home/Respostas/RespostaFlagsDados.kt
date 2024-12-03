package br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas

data class RespostaFlagsDados(
    val Dados : ArrayList<RespostaFlags>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)

data class RespostaFlags(
    val FeatureFlag_ID:Int,
    val FeatureFlag_Nome:String,
    val Status_Cod:Int
)
