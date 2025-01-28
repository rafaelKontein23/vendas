package br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters.Home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.databinding.ItemLojaKitBinding

class AdapterProdutoKit : RecyclerView.Adapter<AdapterProdutoKit.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLojaKitBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }
    class ViewHolder(itemView: ItemLojaKitBinding) : RecyclerView.ViewHolder(itemView.root) {
        
        
    }

    
}