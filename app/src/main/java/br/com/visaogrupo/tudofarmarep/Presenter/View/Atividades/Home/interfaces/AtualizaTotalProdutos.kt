package br.com.visaogrupo.tudofarmarep.Carga.interfaces

import br.com.visaogrupo.tudofarmarep.Objetos.Lojas

interface AtualizaTotalProdutos {
    fun atualizaTotalProdutos(total:Int, loja:Lojas)
}