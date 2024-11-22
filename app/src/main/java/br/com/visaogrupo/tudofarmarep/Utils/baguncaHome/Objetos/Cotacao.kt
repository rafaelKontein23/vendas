package br.com.visaogrupo.tudofarmarep.Objetos

import java.io.Serializable

data class Cotacao(
    val CNPJ: String,
    val CarrinhoId: Int,
    val Hash: String,
    val Marca_ID: Int,
    val Nome: String,
    val RazaoSocial: String,
    val TotalPedido: Double,
    val NomeLoja: String,
    val DataPedido: String,
    val ImagemMarca: String,
    val lojaID:Int,
    val operedorGrupoId: Int,
    val UF:String,
    val cidade: String,
    val bairro: String,
    val endereco: String,
    val numero: String,
    val cep:String,
    val nomeOperadpr :String,
    val formaPagamentoMarcasID: Int,
    val listaOperadoreslooping : ArrayList<String> = ArrayList()


): Serializable