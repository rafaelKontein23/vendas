package br.com.visaogrupo.tudofarmarep.Presenter.View.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import br.com.visaogrupo.tudofarmarep.R

class SpinnerAdapter (context: Context,
                      private val itemList: List<String>,

) : ArrayAdapter<String>(context, 0, itemList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.celula_spiner, parent, false)

        val item = getItem(position)
        val textView = view.findViewById<TextView>(R.id.item_text)

        textView.text = item
        return view
    }
}