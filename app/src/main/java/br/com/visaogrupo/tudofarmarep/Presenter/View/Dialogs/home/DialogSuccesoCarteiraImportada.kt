package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Home.ActHome
import br.com.visaogrupo.tudofarmarep.R

class DialogSuccesoCarteiraImportada {
     companion object{
         var abreDialogCarteira = false
     }
    fun dialogSuccesoCarteiraImportada(context: Context, listaTotal:MutableList<String>,listaNaoEntrados:ArrayList<String>, listaSituacaoCadastral:ArrayList<String>, quantidadeCorretos:Int ) {
        val  dialog =  Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_importacao_sucesso);

        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);
        val fecharDialogModelo = dialog.findViewById<ImageView>(R.id.fecharDialogModelo);
        val quantidadeArquivo = dialog.findViewById<TextView>(R.id.quantidadeArquivo);
        val quantidadeInaptos = dialog.findViewById<TextView>(R.id.quantidadeInaptos);
        val quantidadeIndisponivel = dialog.findViewById<TextView>(R.id.quantidadeIndisponivel);
        val quantidadeArquivoCorretos = dialog.findViewById<TextView>(R.id.quantidadeArquivoCorretos);
        quantidadeArquivoCorretos.text = quantidadeCorretos.toString();
        val  concluir = dialog.findViewById<TextView>(R.id.concluir);
        quantidadeArquivo.text = listaTotal.size.toString();
        quantidadeInaptos.text = listaNaoEntrados.size.toString();
        quantidadeIndisponivel.text = listaSituacaoCadastral.size.toString();
        concluir.setOnClickListener {
            dialog.dismiss();
            val intent = Intent(context, ActHome::class.java);
            abreDialogCarteira = true
            context.startActivity(intent);
        }
        fecharDialogModelo.setOnClickListener {
            dialog.dismiss();
        }


    }
}