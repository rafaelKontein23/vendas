package br.com.visaogrupo.tudofarmarep.Adapter

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaWebView
import br.com.visaogrupo.tudofarmarep.Objetos.Menulateral
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Syncs.retrofit.URLs
import br.com.visaogrupo.tudofarmarep.Views.Activitys.ActWebViewPaginas
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogMenuLateral
import com.bumptech.glide.Glide

class AdapterMenuLateral(val listaMenuLateral: ArrayList<Menulateral>, val atualizaWebView: AtualizaWebView, val dialogMenuLateral: Dialog) : RecyclerView.Adapter<AdapterMenuLateral.ViewHolderMenuLateral>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMenuLateral {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.celula_menu_lateral,parent,false)
        return ViewHolderMenuLateral(view)
    }
    override fun onBindViewHolder(holder: ViewHolderMenuLateral, position: Int) {
        val menuLateral = listaMenuLateral[position]
        holder.menuItem.text = menuLateral.Titulo
        val urlImge = "${URLs.urlImagensCnpjs}${menuLateral.IconeClass}"

        Glide.with(holder.menuItem.context).load(urlImge).into(holder.iconeMenu)

        holder.menuItem.setOnClickListener {
            atualizaWebView.atualizaWebView(menuLateral.Titulo, menuLateral.Link)
            dialogMenuLateral.dismiss()
        }
    }
    override fun getItemCount(): Int {
        return  listaMenuLateral.size
    }
    open class  ViewHolderMenuLateral (itemView: View) : ViewHolder(itemView) {
        val   menuItem = itemView.findViewById<TextView>(R.id.menuItem)
        val   iconeMenu = itemView.findViewById<ImageView>(R.id.iconeMenu)

    }


}