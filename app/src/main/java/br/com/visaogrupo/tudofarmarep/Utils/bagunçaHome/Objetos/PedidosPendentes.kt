package br.com.visaogrupo.tudofarmarep.Objetos

data class PedidosPendentes(
    val CNPJ: String,
    val DtPedido: String,
    val Marca: String,
    val Marca_ID: Int,
    val Pedido_ID: Int,
    val TotalPedido: Double
)