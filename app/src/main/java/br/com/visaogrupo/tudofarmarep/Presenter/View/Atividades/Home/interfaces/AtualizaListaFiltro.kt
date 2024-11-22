package br.com.visaogrupo.tudofarmarep.Carga.interfaces

import br.com.visaogrupo.tudofarmarep.Objetos.Lojas

interface AtualizaListaFiltro {

    fun atualizaListaLojas(listaFiltroLojas:ArrayList<Pair<Int,Any>>)
}