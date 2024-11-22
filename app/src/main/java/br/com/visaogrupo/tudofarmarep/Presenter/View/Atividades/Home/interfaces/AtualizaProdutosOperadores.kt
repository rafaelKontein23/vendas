package br.com.visaogrupo.tudofarmarep.Carga.interfaces

interface AtualizaProdutosOperadores {
    fun atualizaProdutosOperadores(operadorID:Int,lojaId:Int, nomeOperadores: String, marcaID:Int, valorMaximo:Double )
}