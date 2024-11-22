package br.com.visaogrupo.tudofarmarep.Carga.interfaces

import br.com.visaogrupo.tudofarmarep.Objetos.Produtos

interface AtualizaValores {
    fun atualizaValores(Loja: Int, produto:Produtos, removeItem: Boolean = false)
}