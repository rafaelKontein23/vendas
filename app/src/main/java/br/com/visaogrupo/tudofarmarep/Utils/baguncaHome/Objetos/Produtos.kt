package br.com.visaogrupo.tudofarmarep.Objetos

data class Produtos(
    val Barra: String,
    val Marca_ID: Int,
    val Nome: String,
    val Imagem: String,
    var lojaID: Int,
    val listaProgressiva:ArrayList<Progressiva> = arrayListOf(),
    val cnpj: String = "",
    var progressiva: Progressiva? = null,
    var quantidadeAdicionada: Int = 0,
    var valorProdutoTotal: Double = 0.0,
    val isCarrinho:Int = 0,
    var isCarrinhoOperdor:Int = 0,
    var idProgressiva: Int = 0,
    var valorMaximo : Double = 0.0,

    )