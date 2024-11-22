package br.com.visaogrupo.tudofarmarep.Objetos

import java.io.Serializable

data class LojaResponse(
    val lojas: List<Lojas>
)
data class Lojas(
    val LojaTipo_ID: Int,
    val Loja_ID: Int,
    val Loja_id_Portal: Int,
    val Nome: String,
    val QtdeMaxOperador: Int,
    val QtdeMaxVendas: Int,
    val QtdeMinOperador: Int,
    val ValorMaximo: Double,
    val ValorMinimo: Double,
    val marcaID: Int,
    var operadorIDSelecionado: Int,
    val produtos: ArrayList<Produtos>  = arrayListOf(),
    val imagem:String  = "",
    var ProdutosAbertos:Boolean = false,
    var checada:Boolean = false,
    var valorTotal:Double = 0.0,
    var nomeOperadorSelecionado:String = "",
    var listaAtrinutos:ArrayList<Int> = arrayListOf(),
    var isFiltro:Boolean = true,
    var urlLogin: String =  "",
    var totalSt: Double = 0.0,
    val carrinhoID: Int = 0,
    var lojaFiltroSelecionado: Boolean = false
): Serializable