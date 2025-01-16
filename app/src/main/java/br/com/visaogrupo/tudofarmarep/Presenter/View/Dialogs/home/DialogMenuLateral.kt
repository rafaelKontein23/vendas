package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home

import FormularioCadastro
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Outline
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.view.isVisible
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.ActCameraGaleria
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.MainActivity
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.DialogContrato
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.ViewModelActHome
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.DataUltis
import br.com.visaogrupo.tudofarmarep.Utils.Enuns.EnumMenu
import br.com.visaogrupo.tudofarmarep.Utils.IntentUtils
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.databinding.DialogMenuLateralBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.chrisbanes.photoview.PhotoView
import jp.wasabeef.blurry.Blurry

class DialogMenuLateral {

    fun dialogMenu(context: Context,  nomeRepresetanteText : String, cnpjRepresentanteText : String, viewModelActHome: ViewModelActHome) {
        var nomeRepresetanteTextFormat = nomeRepresetanteText
        val dialogMenuLateral = Dialog(context)
        val binding = DialogMenuLateralBinding.inflate(LayoutInflater.from(context))

        dialogMenuLateral.apply {
            setContentView(binding.root)

            window?.let { window ->
                val layoutParams = window.attributes?.apply {
                    width = (context.resources.displayMetrics.widthPixels * 0.80).toInt() // Corrigido para largura
                    height = WindowManager.LayoutParams.MATCH_PARENT
                }
                window.attributes = layoutParams
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes?.windowAnimations = R.style.DialoAnimationMenu
                window.setGravity(Gravity.LEFT)
            }
            show()
        }
        binding.linearGestaoComiisao.isVisible = FormularioCadastro.featureFlagMeuTime
        binding.linearExtratoComissao.isVisible = FormularioCadastro.featureFlagMeuTime
        binding.linearMerchan.isVisible = FormularioCadastro.featureFlagMerchan
        binding.tituloTime.isVisible = FormularioCadastro.featureFlagMeuTime
        binding.tituloAgenda.isVisible = FormularioCadastro.featureFlagAgenda
        binding.linearRoteiro.isVisible = FormularioCadastro.featureFlagAgenda
        binding.linearCalendario.isVisible = FormularioCadastro.featureFlagAgenda
        binding.linearVisitas.isVisible = FormularioCadastro.featureFlagAgenda
        binding.linearProgramaIncentivo.isVisible = FormularioCadastro.featureFlagProgramaIncentivo
        binding.linearTreinamentos.isVisible = FormularioCadastro.featureFlagTreinamentos

        binding.linearClubedeCompras.isVisible = FormularioCadastro.featureFlagClubeCompras
        binding.tituloVendaRemotra.isVisible = FormularioCadastro.featureFlagVendaRemota
        binding.linearTeleVendas.isVisible = FormularioCadastro.featureFlagVendaRemota
        binding.linearLinkExclusivo.isVisible = FormularioCadastro.featureFlagVendaRemota
        binding.linearPedidoSugestao.isVisible = FormularioCadastro.featureFlagVendaRemota

        binding.linearMerchan.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.Merchandising), ProjetoStrings.dashMinhasAcoes)
            dialogMenuLateral.dismiss()
        }
        binding.linearLinkExclusivo.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.home), EnumMenu.REMOTO)
            dialogMenuLateral.dismiss()
        }
        binding.linearProgramaIncentivo.setOnClickListener {
            Alertas.alertaErro(context, "Fique atento aos nossos canais de comunicação. Novos programas de incentivo serão disponibilizados em breve.", "Loiu informa"){}
        }

        binding.linearTreinamentos.setOnClickListener {
            Alertas.alertaErro(context, "Em breve", "Loiu informa")
            {}
        }
        binding.linearTeleVendas.setOnClickListener {
            Alertas.alertaErro(context, "Funcionalidade disponível apenas para Usuário PRO", "Loiu informa") {}
        }
        binding.linearPedidoSugestao.setOnClickListener {
            Alertas.alertaErro(context, "Em breve", "Loiu informa") {}
        }
        binding.linearClubedeCompras.setOnClickListener {
            Alertas.alertaErro(context, "Funcionalidade disponível apenas para Usuário PRO", "Loiu informa") {}
        }

        binding.linearGestaoComiisao.setOnClickListener {
            val data = DataUltis.obtemDataAtual()

            viewModelActHome.atualizaWebView(context.getString(R.string.GestaoDeComisso), "${ProjetoStrings.relotorio}/${data}")
            dialogMenuLateral.dismiss()
        }
        binding.linearExtratoComissao.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.ExtratoDeComissões), ProjetoStrings.dashExtrato)
            dialogMenuLateral.dismiss()
        }

        if (nomeRepresetanteTextFormat.length >= 15){
            nomeRepresetanteTextFormat = nomeRepresetanteTextFormat.substring(0,14) + "..."
        }

        binding.nomeRepresentante.text = nomeRepresetanteTextFormat
        binding.cnpjRepresentante.text = FormatarTexto().formatCNPJ(cnpjRepresentanteText)
        binding.linearDadosCadastrais.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.DadosCadastrais), EnumMenu.DADOSCADASTRAIS)
            dialogMenuLateral.dismiss()
        }
        if (FormularioCadastro.fotoPerfilUrl.isNotEmpty()){
            Glide.with(context)
                .load(FormularioCadastro.fotoPerfilUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.fotoPerfil)

        }
        binding.constraintLayout21.setOnClickListener {
            val intent = Intent(context, ActCameraGaleria::class.java)
            intent.putExtra("viraCamera", true)

            (context as Activity).startActivityForResult(intent, 1)
            dialogMenuLateral.dismiss()
        }
        binding.fotoPerfil.setOnLongClickListener {
            val drawable = binding.fotoPerfil.drawable
            if (drawable is BitmapDrawable) {
                showImagePreview(binding.fotoPerfil, drawable.bitmap, context)
            }
            true
        }
        binding.fotoPerfil.setOnClickListener {
            val intent = Intent(context, ActCameraGaleria::class.java)
            intent.putExtra("viraCamera", true)

            (context as Activity).startActivityForResult(intent, 1)
            dialogMenuLateral.dismiss()
        }
        binding.linearDadosBancarios.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.dadosBancarios), EnumMenu.DADOSBANCARIOS)
            dialogMenuLateral.dismiss()
        }
        binding.linearCargas.setOnClickListener {
            val status = viewModelActHome.recuperaStatusCarga()
            if (status){
                dialogMenuLateral.dismiss()
            }else{
                viewModelActHome.atualizaFragmentHome("", EnumMenu.CARGAS)
                dialogMenuLateral.dismiss()
            }
        }
        binding.linearRoteiro.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.Roteiro), ProjetoStrings.roteiro)
            dialogMenuLateral.dismiss()
        }
        binding.linearCalendario.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.calendario), ProjetoStrings.calendario)
            dialogMenuLateral.dismiss()
        }
        binding.linearVisitas.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.visitas), ProjetoStrings.visitas)
            dialogMenuLateral.dismiss()
        }

        binding.linearIniciarVenda.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.home), EnumMenu.INICIARVENDAS)
            dialogMenuLateral.dismiss()
        }
        binding.linearInstagram.setOnClickListener {
            IntentUtils.mandaParaInstagram(context, ProjetoStrings.urlInstagram)
        }

        binding.linearLinkeDin.setOnClickListener {
            IntentUtils.mandaParaLinkeDin(context, ProjetoStrings.urlLinkDin)
        }


        binding.linearRelatorios.setOnClickListener {
            Alertas.alertaErro(context, "Em breve", "Loiu informa"){

            }
            dialogMenuLateral.dismiss()
        }

        binding.linearPoliticaPrivacidade.setOnClickListener {
            dialogMenuLateral.dismiss()
            val dialogAssinarContratoBinding = DialogContrato(
                context
            )
            dialogAssinarContratoBinding.dialogContratoPolitica(context.getString(R.string.politicaPrivacidade), context.getString(R.string.politica))
        }

        binding.linearTermosdeUso.setOnClickListener {
            dialogMenuLateral.dismiss()
            val dialogAssinarContratoBinding = DialogContrato(
                context
            )
            dialogAssinarContratoBinding.dialogContratoPolitica(context.getString(R.string.termosDeUso), "${context.getString(R.string.temoDeuso)}  ${context.getString(R.string.segundaParteTermos)}")
        }
        binding.linearVendas.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.dadosBancarios), EnumMenu.VENDAS)
            dialogMenuLateral.dismiss()
        }
        binding.linearAreaAruacao.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.DadosAreaAtuacao), EnumMenu.DADOSAREATUACAO)
            dialogMenuLateral.dismiss()
        }


        binding.linearSair.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

    }
    fun showImagePreview(imageView: ImageView, bitmap: Bitmap, context: Context) {
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_imagem)

        val blurryBackground = dialog.findViewById<ImageView>(R.id.blurry_background)
        val photoView = dialog.findViewById<PhotoView>(R.id.photo_view)

        // Aplica o blur no fundo
        Blurry.with(context)
            .radius(25) // Ajusta o nível de desfoque
            .sampling(2) // Ajusta o desempenho
            .capture(imageView)
            .into(blurryBackground)

        // Define a imagem na PhotoView
        photoView.setImageBitmap(bitmap)

        // Exibe o diálogo
        dialog.show()

        // Fecha ao tocar fora
        photoView.setOnClickListener {
            dialog.dismiss()
        }
    }
}