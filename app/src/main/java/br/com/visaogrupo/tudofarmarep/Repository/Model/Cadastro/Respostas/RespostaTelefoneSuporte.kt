package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas

data class RespostaApi(
    val Dados: List<RespostaTelefoneSuporte>,
    val Mensagem: String,
    val Valido: Int,
    val Resultado: String
)

data class RespostaTelefoneSuporte(
    val ContatoWhatsApp: String,
    val LinkZap: String
)