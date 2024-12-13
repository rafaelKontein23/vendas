package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterItenCnpj
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskCnpjs
import br.com.visaogrupo.tudofarmarep.Carga.ultis.CapturaLongeLat
import br.com.visaogrupo.tudofarmarep.Objetos.Atributo
import br.com.visaogrupo.tudofarmarep.Objetos.Cnpj
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Views.Activitys.ActImportaCarteira
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogIconesCnpjs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class DialogCnpjs {
    var isMinhaCarteira = true
    var isProximos = false
    val listaAtributos = ArrayList<Atributo>()
    var listaCnpjsAux = ArrayList<Cnpj>()
    fun dialogCnpjs(context: Context, isVensaRemota: Boolean = false, iniciaLoja:Int, atividade:Activity ) {

        val listaCnpjAux = ArrayList<Cnpj>()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val reprsentanteID = prefs.getInt(ProjetoStrings.reprenteID, 0)


        val dialogSelecioneCnpj = Dialog(context)
        dialogSelecioneCnpj.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSelecioneCnpj.setContentView(R.layout.dialog_selecione_cnpj)


        var adapterItensCnpjs:AdapterItenCnpj? = null

        dialogSelecioneCnpj.show()
        dialogSelecioneCnpj.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogSelecioneCnpj.window?.let { window ->
            val layoutParams = window.attributes?.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = (context.resources.displayMetrics.heightPixels * 0.95).toInt()
            }
            window.attributes = layoutParams

        }
        dialogSelecioneCnpj.window!!.attributes.windowAnimations = R.style.animacaoDialog
        dialogSelecioneCnpj.window!!.setGravity(Gravity.BOTTOM)

        val recyclerCnpjs = dialogSelecioneCnpj.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerCnpjs)
        val fecharCnpjs = dialogSelecioneCnpj.findViewById<android.widget.ImageView>(R.id.fecharCnpjs)
        val totalCnpjs = dialogSelecioneCnpj.findViewById<android.widget.TextView>(R.id.totalCnpjs)
        val minhaCarteira = dialogSelecioneCnpj.findViewById<android.widget.TextView>(R.id.minhaCarteiraText)
        val proximos = dialogSelecioneCnpj.findViewById<android.widget.TextView>(R.id.proximosText)
        val linearLayout2 = dialogSelecioneCnpj.findViewById<android.widget.LinearLayout>(R.id.linearLayout2)
        val inputBuscaCliente = dialogSelecioneCnpj.findViewById<android.widget.EditText>(R.id.inputBuscaCliente)
        val descriacao = dialogSelecioneCnpj.findViewById<android.widget.TextView>(R.id.descriacao)
        val constraintImporta: ConstraintLayout = dialogSelecioneCnpj.findViewById(R.id.constrainImporta)
        val textoImportar = dialogSelecioneCnpj.findViewById<TextView>(R.id.improtaText)
        val infoIcones = dialogSelecioneCnpj.findViewById<ImageView>(R.id.infoIcones)
        val progressBarCnpj = dialogSelecioneCnpj.findViewById<android.widget.ProgressBar>(R.id.progressBarCnpj)
        val constrainBuscaErro = dialogSelecioneCnpj.findViewById<ConstraintLayout>(R.id.constrainBuscaErro)
        val textoBusca = dialogSelecioneCnpj.findViewById<TextView>(R.id.textoBusca)

        linearLayout2.isVisible = !isVensaRemota
        descriacao.isVisible = isVensaRemota
        totalCnpjs.isVisible = !isVensaRemota
        constraintImporta.isVisible = !isVensaRemota
        inputBuscaCliente.inputType = android.text.InputType.TYPE_CLASS_TEXT
        inputBuscaCliente.imeOptions = EditorInfo.IME_ACTION_SEARCH

        inputBuscaCliente.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pesquisaText = s.toString()
                if(adapterItensCnpjs != null){
                    if (!adapterItensCnpjs!!.isProximos){
                        if (pesquisaText.isEmpty()){
                            adapterItensCnpjs!!.listaCnpj = listaCnpjsAux
                            adapterItensCnpjs!!.notifyDataSetChanged()
                            if(adapterItensCnpjs!!.listaCnpj.isEmpty()){
                                constrainBuscaErro.isVisible = true
                                textoBusca.text = "Nenhum CNPJ encontrado"
                            }else{
                                constrainBuscaErro.isVisible = false

                            }
                        }else{
                            val listaFiltrada =listaCnpjsAux.filter {it -> it.RazaoSocial.contains(pesquisaText, ignoreCase = true)
                                    || it.CNPJ.contains(pesquisaText, ignoreCase = true) || it.Cidade.contains(pesquisaText, ignoreCase = true) || it.UF.contains(pesquisaText, ignoreCase = true)
                                    || it.Endereco.contains(pesquisaText, ignoreCase = true)}
                            adapterItensCnpjs!!.listaCnpj = listaFiltrada as ArrayList<Cnpj>
                            adapterItensCnpjs!!.notifyDataSetChanged()

                            if(adapterItensCnpjs!!.listaCnpj.isEmpty()){
                                constrainBuscaErro.isVisible = true
                                textoBusca.text =  "Nenhum PDV encontrado com o termo\"" + pesquisaText + "\""
                            }else{
                                constrainBuscaErro.isVisible = false

                            }
                        }
                    }else if(pesquisaText.isEmpty() && adapterItensCnpjs!!.isProximos){
                        adapterItensCnpjs!!.listaCnpj = listaCnpjsAux
                        totalCnpjs.text = "${listaCnpjsAux.size} CNPJs proximos a você"
                        adapterItensCnpjs!!.notifyDataSetChanged()
                    }
                }



            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        inputBuscaCliente.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val inputText = inputBuscaCliente.text.toString()
                if(adapterItensCnpjs != null){
                    if (adapterItensCnpjs!!.isProximos){

                        if (inputText.isEmpty()){
                            buscaProximos(context, reprsentanteID, totalCnpjs, inputBuscaCliente, adapterItensCnpjs!!, recyclerCnpjs, progressBarCnpj)


                        }else{
                            CoroutineScope(Dispatchers.IO).launch {
                                MainScope().launch {
                                    recyclerCnpjs.isVisible = false
                                    progressBarCnpj.isVisible = true
                                }

                                val taskCnpjs = TaskCnpjs()
                                val capturaLongeLat = CapturaLongeLat()
                                val (lat,  long) = capturaLongeLat.capturaLogeLat(context)


                                val (listaCnpjs, listaAtributo) =  taskCnpjs.buscaCnpjs(reprsentanteID, isBuscaLocal =  true, buscastr = inputBuscaCliente.text.toString(), lat = lat, long = long)
                                listaAtributos.clear()
                                listaAtributos.addAll(listaAtributo)
                                MainScope().launch{
                                    totalCnpjs.text = "${listaCnpjs.size} CNPJs na Base Geral"

                                    adapterItensCnpjs?.listaCnpj = listaCnpjs
                                    adapterItensCnpjs?.isProximos = true
                                    adapterItensCnpjs?.notifyDataSetChanged()
                                    recyclerCnpjs.isVisible = true
                                    progressBarCnpj.isVisible = false
                                    val inputText = inputBuscaCliente.text.toString()
                                    if(adapterItensCnpjs!!.listaCnpj.isEmpty()){
                                        constrainBuscaErro.isVisible = true
                                        "Nenhum PDV encontrado com o termo\"" + inputText + "\""
                                    }else{
                                        constrainBuscaErro.isVisible = false

                                    }

                                }
                            }
                        }

                    }

                }else{
                    Toast.makeText(context, "Nenhum CNPJ encontrado", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
        constraintImporta.setOnClickListener {
            val intent = Intent(context, ActImportaCarteira::class.java)
            context.startActivity(intent)
        }
        animaConstrain(
            constraintLayout = constraintImporta,
            initialWidthDp = 48f,
            expandedWidthDp = 125f,
            textoImportar = textoImportar

        )

        minhaCarteira.setOnClickListener {
            trocaSelecao(minhaCarteira, proximos, context)
            val inputBuscaClienteCap = inputBuscaCliente.text.toString()

            if (!isMinhaCarteira){
                CoroutineScope(Dispatchers.IO).launch {
                    MainScope().launch {
                        recyclerCnpjs.isVisible = false
                        progressBarCnpj.isVisible = true

                    }

                    val taskCnpjs = TaskCnpjs()
                    val capturaLongeLat = CapturaLongeLat()

                    val (lat,  long) = capturaLongeLat.capturaLogeLat(context)
                    val (listaCnpjs, listaAtributo) =  taskCnpjs.buscaCnpjs(representanteID = reprsentanteID, minhaCarteira = true, lat =  lat, long = long)
                    listaCnpjsAux.clear()
                    listaCnpjsAux.addAll(listaCnpjs)
                    listaAtributos.clear()
                    listaAtributos.addAll(listaAtributo)
                    MainScope().launch {
                        totalCnpjs.text = "${listaCnpjs.size} CNPJs salvos na carteira"
                        inputBuscaCliente.hint = "Buscar por CNPJ, Razão Social ou Cidade"

                        adapterItensCnpjs?.listaCnpj = listaCnpjs
                        adapterItensCnpjs?.isProximos = false
                        if(inputBuscaClienteCap.isNotEmpty()){
                            val listaFiltrada =listaCnpjsAux.filter {it -> it.RazaoSocial.contains(inputBuscaClienteCap, ignoreCase = true)
                                    || it.CNPJ.contains(inputBuscaClienteCap, ignoreCase = true) || it.Cidade.contains(inputBuscaClienteCap, ignoreCase = true) || it.UF.contains(inputBuscaClienteCap, ignoreCase = true)
                                    || it.Endereco.contains(inputBuscaClienteCap, ignoreCase = true)}
                            adapterItensCnpjs!!.listaCnpj = listaFiltrada as ArrayList<Cnpj>

                        }
                        adapterItensCnpjs?.notifyDataSetChanged()
                        if(adapterItensCnpjs!!.listaCnpj.isEmpty()){
                            constrainBuscaErro.isVisible = true
                            "Nenhum PDV encontrado com o termo\"" + inputBuscaClienteCap + "\""
                        }else{
                            constrainBuscaErro.isVisible = false

                        }


                        constraintImporta.isVisible = true
                        animaConstrain(
                            constraintLayout = constraintImporta,
                            initialWidthDp = 48f,
                            expandedWidthDp = 125f,
                            textoImportar = textoImportar
                        )


                        recyclerCnpjs.isVisible = true
                        progressBarCnpj.isVisible = false
                    }
                }
                isMinhaCarteira = true
                isProximos = false

            }



        }
        proximos.setOnClickListener {
            if(adapterItensCnpjs != null){
                trocaSelecao(proximos, minhaCarteira, context)
                val inputText = inputBuscaCliente.text.toString()

                if (inputText.isEmpty()){
                    buscaProximos(context, reprsentanteID, totalCnpjs, inputBuscaCliente, adapterItensCnpjs!!, recyclerCnpjs, progressBarCnpj)

                }else{
                    CoroutineScope(Dispatchers.IO).launch {
                        MainScope().launch {
                            recyclerCnpjs.isVisible = false
                            progressBarCnpj.isVisible = true
                        }

                        val taskCnpjs = TaskCnpjs()
                        val capturaLongeLat = CapturaLongeLat()
                        val (lat,  long) = capturaLongeLat.capturaLogeLat(context)


                        val (listaCnpjs, listaAtributo) =  taskCnpjs.buscaCnpjs(reprsentanteID, isBuscaLocal =  true, buscastr = inputBuscaCliente.text.toString(), lat = lat, long = long)
                        listaAtributos.clear()
                        listaAtributos.addAll(listaAtributo)
                        MainScope().launch{
                            totalCnpjs.text = "${listaCnpjs.size} CNPJs na Base Geral"

                            adapterItensCnpjs?.listaCnpj = listaCnpjs
                            adapterItensCnpjs?.isProximos = true
                            adapterItensCnpjs?.notifyDataSetChanged()
                            recyclerCnpjs.isVisible = true
                            progressBarCnpj.isVisible = false
                            isMinhaCarteira = false

                        }
                    }
                }
                constraintImporta.isVisible = false
                if(adapterItensCnpjs != null){
                    if(adapterItensCnpjs!!.listaCnpj.isEmpty()){
                        constrainBuscaErro.isVisible = true
                        textoBusca.text = "Nenhum PDV encontrado com o termo\"" + inputText + "\""
                    }else{
                        constrainBuscaErro.isVisible = false

                    }
                }

            }

        }
        fecharCnpjs.setOnClickListener {
            dialogSelecioneCnpj.dismiss()
        }
        infoIcones.setOnClickListener {
            val dialogIcones = DialogIconesCnpjs()
            dialogIcones.dialogIcones(context,listaAtributos )
        }

        CoroutineScope(Dispatchers.IO).launch{

            val taskCnpjs = TaskCnpjs()
            val capturaLongeLat = CapturaLongeLat()

            val (lat,  long) = capturaLongeLat.capturaLogeLat(context)
            val ( listaCnpjs, listaAtributo) =  taskCnpjs.buscaCnpjs(representanteID = reprsentanteID, minhaCarteira = true, lat =  lat, long = long)
            listaCnpjsAux.clear()
            listaCnpjsAux.addAll(listaCnpjs)
            listaAtributos.addAll(listaAtributo)
            MainScope().launch {
                totalCnpjs.text = "${listaCnpjs.size} CNPJs salvos na carteira"
                infoIcones.isVisible = !isVensaRemota
                adapterItensCnpjs = AdapterItenCnpj(listaCnpjs,dialogSelecioneCnpj,  isVensaRemota, representanteId = reprsentanteID, iniciaLoja =  iniciaLoja, atividade = atividade)
                val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                recyclerCnpjs.layoutManager = linearLayoutManager
                recyclerCnpjs.adapter = adapterItensCnpjs
            }
        }

    }

    fun trocaSelecao(textSelecionado :TextView, textDeselecionado:TextView, context: Context){
        textSelecionado.backgroundTintList = null
        textSelecionado.background = null
        textSelecionado.background = context.getDrawable(R.drawable.bordas_8dp_branca)

        textSelecionado.setTextColor(ContextCompat.getColor(context, R.color.blue500))
        textDeselecionado.backgroundTintList = null
        textDeselecionado.background = null
        textDeselecionado.setTextColor(ContextCompat.getColor(context, R.color.gray400))
    }

    fun buscaProximos(context: Context, reprsentanteID:Int, totalCnpjs:TextView, inputBuscaCliente:EditText, adapterItensCnpjs : AdapterItenCnpj,recyclerCnpjs:RecyclerView, progressBarCnpj: ProgressBar, ){
        if (!isProximos){
            CoroutineScope(Dispatchers.IO).launch {
                MainScope().launch {
                    recyclerCnpjs.isVisible = false
                    progressBarCnpj.isVisible = true

                }
                val taskCnpjs = TaskCnpjs()
                val capturaLongeLat = CapturaLongeLat()

                val (lat,  long) = capturaLongeLat.capturaLogeLat(context)
                val (listaCnpjs, listaAtributo) =  taskCnpjs.buscaCnpjs(reprsentanteID, lat = lat , long = long, isProximo = true)
                listaAtributos.clear()
                listaAtributos.addAll(listaAtributo)
                MainScope().launch{
                    totalCnpjs.text = "${listaCnpjs.size} CNPJs proximos a você"
                    listaCnpjsAux.clear()
                    listaCnpjsAux.addAll(listaCnpjs)
                    inputBuscaCliente.hint = "Buscar por CNPJ na base"

                    adapterItensCnpjs?.listaCnpj = listaCnpjs
                    adapterItensCnpjs?.isProximos = true
                    adapterItensCnpjs?.notifyDataSetChanged()
                    recyclerCnpjs.isVisible = true
                    progressBarCnpj.isVisible = false


                }
            }
            isMinhaCarteira = false
            isProximos = true
        }
    }
    fun animaConstrain(
        constraintLayout: ConstraintLayout,
        initialWidthDp: Float,
        expandedWidthDp: Float,
        duration: Long = 500,
        delayBeforeCollapse: Long = 5000,
        textoImportar:TextView
    ) {
        val initialWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, initialWidthDp, constraintLayout.resources.displayMetrics
        ).toInt()

        val expandedWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, expandedWidthDp, constraintLayout.resources.displayMetrics
        ).toInt()


        val delayBeforeExpand = 1300L

        val expandAnimator = ValueAnimator.ofInt(initialWidth, expandedWidth)
        expandAnimator.duration = duration
        expandAnimator.interpolator = AccelerateDecelerateInterpolator()
        expandAnimator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = constraintLayout.layoutParams
            layoutParams.width = animatedValue
            constraintLayout.layoutParams = layoutParams
        }
        textoImportar.isVisible = false
        val collapseAnimator = ValueAnimator.ofInt(expandedWidth, initialWidth)
        collapseAnimator.duration = duration
        collapseAnimator.interpolator = AccelerateDecelerateInterpolator()
        collapseAnimator.addUpdateListener { valueAnimator ->
            val animatedValue = valueAnimator.animatedValue as Int
            val layoutParams = constraintLayout.layoutParams
            layoutParams.width = animatedValue
            constraintLayout.layoutParams = layoutParams
        }

        constraintLayout.layoutParams.width = initialWidth
        constraintLayout.requestLayout()

        constraintLayout.postDelayed({
            expandAnimator.start()
        }, delayBeforeExpand)

        expandAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                textoImportar.isVisible = true
                constraintLayout.postDelayed({
                    collapseAnimator.start()
                    textoImportar.isVisible = false
                }, delayBeforeCollapse)
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }
}