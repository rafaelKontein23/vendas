package br.com.visaogrupo.tudofarmarep.Views.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import br.com.visaogrupo.tudofarmarep.ActLojaPadrao
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterItenCnpj
import br.com.visaogrupo.tudofarmarep.DAO.DAOCarrinho
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoItemCotacao
import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.Objetos.Cotacao
import br.com.visaogrupo.tudofarmarep.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class DialogCarrinhoAberto {
    fun dialogDialogAberto(context: Context,listaCarrinho:ArrayList<CarrinhoItemCotacao>, cotacao: Cotacao,    atividade: Activity, inciaLoja:Int){


        val dialogCarrinhoAberto = Dialog(context)
        dialogCarrinhoAberto.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogCarrinhoAberto.setContentView(R.layout.dialog_carrinho_aberto)

        dialogCarrinhoAberto.show()
        dialogCarrinhoAberto.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogCarrinhoAberto.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogCarrinhoAberto.window!!.attributes.windowAnimations = R.style.animacaoDialog
        dialogCarrinhoAberto.window!!.setGravity(Gravity.BOTTOM)
        val fechar = dialogCarrinhoAberto.findViewById<ImageView>(R.id.fechar)
        val fechaModalCarrinho = dialogCarrinhoAberto.findViewById<TextView>(R.id.fechaModalCarrinho)
        val limparCarrinho = dialogCarrinhoAberto.findViewById<TextView>(R.id.limparCarrinho)
        fechar.setOnClickListener {
            dialogCarrinhoAberto.dismiss()
        }
        fechaModalCarrinho.setOnClickListener {
            dialogCarrinhoAberto.dismiss()
        }

        limparCarrinho.setOnClickListener {
            MainScope().launch {
                withContext(Dispatchers.IO){
                    val DAOHelper = DAOHelper(context)
                    val db = DAOHelper.writableDatabase
                    val DAOCarrinho = DAOCarrinho()
                    DAOCarrinho.deletaCarrinhoEspecifico(db, cotacao.lojaID, cotacao.CNPJ)
                    MainScope().launch {
                        Toast.makeText(context, "Carrinho Limpo com sucesso", Toast.LENGTH_SHORT).show()
                        dialogCarrinhoAberto.dismiss()

                        val intent = Intent(context, ActLojaPadrao::class.java)

                        val bundle = Bundle()
                        val bundleListaCarrinho = Bundle()
                        val bundleCotacaoOperador = Bundle()
                        val bundleCotacao = Bundle()

                        val bundleNomeOperador = Bundle()
                        bundleNomeOperador.putString("NomeOperadorBundle", cotacao.nomeOperadpr)
                        intent.putExtra("NomeOperadorBundle",bundleNomeOperador)
                        bundleCotacao.putSerializable("cotacao", cotacao as Serializable)

                        val cnpjItem = Cnpj(cotacao.CNPJ, "", "", "", "", "", cotacao.RazaoSocial, cotacao.UF)
                        bundle.putSerializable("cnpjSelecionado", cnpjItem as Serializable)
                        bundleListaCarrinho.putSerializable("listaCarrinho", listaCarrinho as Serializable)
                        bundleCotacaoOperador.putInt("operadorCotacao", cotacao.operedorGrupoId)
                        intent.putExtra("operadorCotacaoBundle", bundleCotacaoOperador)
                        intent.putExtra("cnpjSelecionadoBundle", bundle)
                        intent.putExtra("cotacaoBundle", bundleCotacao)


                        intent.putExtra("listaCarrinhoBundle", bundleListaCarrinho)
                        atividade.startActivityForResult(intent, inciaLoja)
                    }
                }
            }
        }
    }
}