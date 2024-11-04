package br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas
data class RespostaCnpjDados (
    val Dados : List<RepostaCnpj>,
    val Mensagem : String,
    val Valido : Int,
    val Resultado : String
)
data class RepostaCnpj(
    val Bairro: String,
    val CEP: String,
    val CNPJ: String,
    val Cidade: String,
    val Complemento: String,
    val Email: String,
    val Endereco: String,
    val Fantasia: String,
    val Numero: String,
    val RazaoSocial: String,
    val Telefone: String,
    val UF: String
)