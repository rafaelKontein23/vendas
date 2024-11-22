package br.com.visaogrupo.tudofarmarep.Objetos

data class GraficoMarca(
    val CorMarca: String,
    val Marca: String,
    var PedidosRealizados: Int,
    var PedidoRealizadosOriginal:Int = 0
)