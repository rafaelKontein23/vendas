package br.com.visaogrupo.tudofarmarep.Views.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterItenCnpj
import br.com.visaogrupo.tudofarmarep.Carga.ultis.BaixaArquivo
import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.R
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DialogModeloImportacao {
    @RequiresApi(Build.VERSION_CODES.O)
    fun dialogModeloImportacao(context: Context, activity: Activity) {


        val dialogModelo = Dialog(context)
        dialogModelo.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogModelo.setContentView(R.layout.dialog_modelo_importacao)

        dialogModelo.show()
        dialogModelo.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogModelo.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogModelo.window!!.attributes.windowAnimations = R.style.animacaoDialog
        dialogModelo.window!!.setGravity(Gravity.BOTTOM)
        val baixarModelo = dialogModelo.findViewById<TextView>(R.id.baixarModelo)
        val fecharDialog = dialogModelo.findViewById<ImageView>(R.id.fecharDialogModelo)
        fecharDialog.setOnClickListener {
            dialogModelo.dismiss()
        }
        baixarModelo.setOnClickListener {
            val url = "https://www.loiu.com.br/Docs/Excels/Modelo_Importacao_CNPJ.xlsx"
            val fileName = "Modelo_Importacao_CNPJ.xlsx"
            MainScope().launch {
                Toast.makeText(context, "Baixando arquivo...", Toast.LENGTH_SHORT).show()
                BaixaArquivo.downloadExcelFile(activity, url, fileName)
                Toast.makeText(activity, "baixado com sucesso", Toast.LENGTH_SHORT).show()
            }
        }
    }
}