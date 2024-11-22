package br.com.visaogrupo.tudofarmarep.Adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas

class AdapterFiltroLojasCheck (listaLojas:ArrayList<Lojas>):
    RecyclerView.Adapter<AdapterFiltroLojasCheck.viewHolderLojaFiltroCheck>() {


    class viewHolderLojaFiltroCheck(itemView: View) : AdapterRecyclerLojas.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderLojaFiltroCheck {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: viewHolderLojaFiltroCheck, position: Int) {
        TODO("Not yet implemented")
    }
}