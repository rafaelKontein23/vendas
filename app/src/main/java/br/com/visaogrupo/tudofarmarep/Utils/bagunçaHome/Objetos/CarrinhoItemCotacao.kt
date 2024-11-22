package br.com.visaogrupo.tudofarmarep.Objetos

import java.io.Serializable

data class CarrinhoItemCotacao(
    val carrinhoId: Int,
    val representanteId: Int,
    val operadorId: String,
    val cnpj: String,
    val lojaId: Int,
    val marcaId: Int,
    val nome: String,
    val barra: String,
    val quantidade: Int,
    val valorUnitario: Double,
    val valorTotal: Double,
    val desconto: Double
) :Serializable
