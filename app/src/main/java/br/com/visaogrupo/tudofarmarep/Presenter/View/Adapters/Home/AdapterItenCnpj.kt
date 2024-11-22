package br.com.visaogrupo.tudofarmarep.Adapter

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.impl.utils.ContextUtil.getBaseContext
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.ActLojaPadrao
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskConstroiHash
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoItemCotacao
import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Views.Fragments.FragmentHome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable


class AdapterItenCnpj ( listaCnpj: ArrayList<Cnpj>,
                        val dialog:Dialog,
                        val  isVendaemota:Boolean = false,
                        val representanteId:Int,
                        val iniciaLoja:Int,
                        val atividade:Activity
     ) : RecyclerView.Adapter<AdapterItenCnpj.itensCnpjViewHolder>() {
     var listaCnpj = listaCnpj
    var isProximos = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itensCnpjViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_cnpj, parent, false)
        return itensCnpjViewHolder(view)
    }


    override fun onBindViewHolder(holder: itensCnpjViewHolder, position: Int) {
        val cnpjItem = listaCnpj[position]
        holder.constrainItem.setOnClickListener{

                val prefs = PreferenceManager.getDefaultSharedPreferences(holder.recyIconesCnpj.context)
                val fazendoCarga = prefs.getBoolean("fazendoCarga", false)
                if(fazendoCarga){
                    Alertas.alertaErro(holder.recyIconesCnpj.context, "Os dados estão sendo atualizados, tente novamente mais tarde.", "Ops!"){

                    }
                }else{
                    val intent = Intent(holder.constrainItem.context, ActLojaPadrao::class.java)

                    val bundle = Bundle()
                    val bundleListaCarrinho = Bundle()
                    val bundleCotacaoOperador = Bundle()
                    val bundleNomeOperador = Bundle()
                    bundleNomeOperador.putString("NomeOperadorBundle","")
                    intent.putExtra("NomeOperadorBundle","")
                    bundleCotacaoOperador.putInt("operadorCotacaoBundle", 0)

                    bundle.putSerializable("cnpjSelecionado", cnpjItem as Serializable)
                    intent.putExtra("cnpjSelecionadoBundle", bundle)
                    intent.putExtra("operadorCotacaoBundle", bundleCotacaoOperador)

                    val listaCarrinho = ArrayList<CarrinhoItemCotacao>()
                    bundleListaCarrinho.putSerializable("listaCarrinho", listaCarrinho)
                    intent.putExtra("listaCarrinhoBundle", bundleListaCarrinho)
                    atividade.startActivityForResult(intent, iniciaLoja )
                    dialog.dismiss()

                }



        }
        val textoRazaoEndereco = "${cnpjItem.RazaoSocial}\n"+
                "${cnpjItem.Endereco}, ${cnpjItem.Numero}, ${cnpjItem.Complemento}, ${cnpjItem.Cidade}/${cnpjItem.UF} - ${cnpjItem.bairro} "
        holder.cnpjText.text = FormatarTexto().formatCNPJ(cnpjItem.CNPJ)
        holder.razaoSocialText.text = textoRazaoEndereco
        val metrosDouble = cnpjItem.Distancia.toDouble()
        if (metrosDouble > 1){
            holder.metros.text = "${String.format("%.2f", metrosDouble)} km".replace(",",".")
        }else{
                holder.metros.text = "${String.format("%.3f", metrosDouble)} m".replace("0,","")

        }
        holder.recyIconesCnpj.adapter = AdapterAtributoCnpj(cnpjItem.atributos)
        holder.recyIconesCnpj.layoutManager = LinearLayoutManager(holder.recyIconesCnpj.context, RecyclerView.HORIZONTAL, false)
        holder.recyIconesCnpj.setHasFixedSize(true)
        if(cnpjItem.Distancia != "0"){
            holder.metros.isVisible = true

        }else{
            holder.metros.isVisible = false

        }
    }
    override fun getItemCount(): Int {
        return listaCnpj.size
    }
    class itensCnpjViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val constrainItem = itemView.findViewById<ConstraintLayout>(R.id.constrainItem)
       val cnpjText = itemView.findViewById<TextView>(R.id.cnpjText)
       val razaoSocialText = itemView.findViewById<TextView>(R.id.razaoSocialText)
       val metros = itemView.findViewById<TextView>(R.id.metros)
       val recyIconesCnpj = itemView.findViewById<RecyclerView>(R.id.RecyIconesCnpj)

    }

}