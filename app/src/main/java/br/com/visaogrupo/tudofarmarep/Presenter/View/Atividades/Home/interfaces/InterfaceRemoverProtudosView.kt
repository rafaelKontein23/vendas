package br.com.visaogrupo.tudofarmarep.Carga.interfaces

import br.com.visaogrupo.tudofarmarep.Objetos.Lojas

interface InterfaceRemoverProtudosView {
    fun removeItens(lojaId:Int)
    fun addItensItens(loja:Lojas, position:Int)
}