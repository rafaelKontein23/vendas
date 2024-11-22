package br.com.visaogrupo.tudofarmarep.Views.dialogs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaInfosItens
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Lojas.ProdutoDetalheDialogFragment

class DialogProdutoDetalhe {

    fun dialogPrudutoDetalhe(context: Context, produto: Produtos, atualizaValores:AtualizaInfosItens, loja:Lojas, carrinho:Boolean = false, razaoSocial:String) {
        val fragment = ProdutoDetalheDialogFragment( produto, atualizaValores, loja, carrinho, razaoSocial)
        fragment.show((context as AppCompatActivity).supportFragmentManager, fragment.tag)
    }


}