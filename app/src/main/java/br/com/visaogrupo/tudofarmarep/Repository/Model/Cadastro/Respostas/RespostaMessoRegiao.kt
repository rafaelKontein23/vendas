package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaMessoRegiaoDados(
    val Dados: List<RespostaMessoRegiao>,
    val Mensagem: String,
    val Valido: String,
    val Resultado : String

)

data class RespostaMessoRegiao(
    val Mesorregiao_id: Int,
    val Mesorregiao_Nome: String,
    val Municipio: String
)
