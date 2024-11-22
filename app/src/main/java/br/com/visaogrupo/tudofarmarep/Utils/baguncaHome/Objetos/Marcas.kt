package br.com.visaogrupo.tudofarmarep.Objetos
data class MarcasResponse(
    val Marcas: List<Marcas>
)
data class Marcas(
    val Banco: String,
    val Logotipo: String,
    val Marca_ID: Int,
    val Nome: String,
    val Status_Cod: Int,
    val URL: String
)