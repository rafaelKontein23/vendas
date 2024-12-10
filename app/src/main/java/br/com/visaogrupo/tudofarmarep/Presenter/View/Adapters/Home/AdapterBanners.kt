package br.com.visaogrupo.tudofarmarep.Adapter


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Objetos.Banners
import br.com.visaogrupo.tudofarmarep.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class AdapterBanners (val listaBanners: MutableList<Banners>): RecyclerView.Adapter<AdapterBanners.BannersCorrosel>()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannersCorrosel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celula_banner, parent, false)
        return BannersCorrosel(view)
    }
    override fun onBindViewHolder(holder: BannersCorrosel, position: Int) {
        val bannerItem = listaBanners[position]
        if (position == 3){
            Log.d("da","affsa")
        }
        holder.bind(bannerItem, holder)
    }

    override fun getItemCount(): Int {
        return listaBanners.size
    }


    class BannersCorrosel(itemView: View) : RecyclerView.ViewHolder(itemView){
            val banner = itemView.findViewById<ImageView>(R.id.imgBanner)

        fun bind(bannerItem: Banners, holder: BannersCorrosel){
            val imagem ="https://"+bannerItem.imagem
            holder.banner.setOnClickListener {
                val url = bannerItem.link
                if(!url.isEmpty()){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    holder.banner.context.startActivity(intent)
                }

            }
            Glide.with(holder.banner.context)
                .load(imagem)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.banner)


        }

    }

}