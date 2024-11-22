package br.com.visaogrupo.tudofarmarep.Objetos

data class CnpjsImportados(
    val CNPJ: String,
    val Encontrado: Boolean,
    val QtdImportada: Int,
    val SituacaoCadastral: Boolean
)