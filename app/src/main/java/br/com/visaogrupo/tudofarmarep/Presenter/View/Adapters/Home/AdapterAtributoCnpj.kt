package br.com.visaogrupo.tudofarmarep.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Objetos.Atributo
import br.com.visaogrupo.tudofarmarep.R
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdapterAtributoCnpj (val listaAtributos: ArrayList<Atributo>): RecyclerView.Adapter<AdapterAtributoCnpj.ViewHolderAtributo>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAtributo {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.celula_icone_cnpj, parent, false)
        return ViewHolderAtributo(view)
    }
    override fun onBindViewHolder(holder: ViewHolderAtributo, position: Int) {
        val atributo = listaAtributos[position]
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                Picasso.get().load(atributo.imagem).into(holder.cnpjIcone)

            }
        }
    }

    override fun getItemCount(): Int {
        return listaAtributos.size
    }

    class ViewHolderAtributo(itemView: View) : RecyclerView.ViewHolder(itemView){
         val cnpjIcone = itemView.findViewById<ImageView>(R.id.cnpjIcone)
    }
}