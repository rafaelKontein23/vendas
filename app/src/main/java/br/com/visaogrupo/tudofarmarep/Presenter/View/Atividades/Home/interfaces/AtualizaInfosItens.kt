package br.com.visaogrupo.tudofarmarep.Carga.interfaces

import br.com.visaogrupo.tudofarmarep.Objetos.Produtos

interface AtualizaInfosItens {
   fun atualizaTopo(lojaId: Int, produtos: Produtos? = null, paraLoppin: Boolean = false)
   fun atualizaItensCarrinho()
}