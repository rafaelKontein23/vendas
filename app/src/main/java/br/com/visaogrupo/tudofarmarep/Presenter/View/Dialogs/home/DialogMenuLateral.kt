package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.view.isVisible
import br.com.visaogrupo.tudofarmarep.Carga.ultis.FormatarTexto
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.MainActivity
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.DialogContrato
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Home.Atividades.ViewModelActHome
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Enuns.EnumMenu
import br.com.visaogrupo.tudofarmarep.databinding.DialogMenuLateralBinding

class DialogMenuLateral {

    fun dialogMenu(context: Context,  nomeRepresetanteText : String, cnpjRepresentanteText : String, viewModelActHome: ViewModelActHome) {
        var nomeRepresetanteTextFormat = nomeRepresetanteText
        val dialogMenuLateral = Dialog(context)
        val binding = DialogMenuLateralBinding.inflate(LayoutInflater.from(context))

        dialogMenuLateral.apply {
            setContentView(binding.root)

            window?.let { window ->
                val layoutParams = window.attributes?.apply {
                    width = (context.resources.displayMetrics.widthPixels * 0.70).toInt() // Corrigido para largura
                    height = WindowManager.LayoutParams.MATCH_PARENT
                }
                window.attributes = layoutParams
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes?.windowAnimations = R.style.DialoAnimationMenu
                window.setGravity(Gravity.LEFT)
            }

            show()
        }
        binding.linearCadastroDeProposto.isVisible = FormularioCadastro.featureFlagMeuTime
        binding.linearGestaoComiisao.isVisible = FormularioCadastro.featureFlagMeuTime
        binding.linearExtratoComissao.isVisible = FormularioCadastro.featureFlagMeuTime
        binding.linearMerchan.isVisible = FormularioCadastro.featureFlagMerchan

        binding.linearMerchan.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.Merchandising), ProjetoStrings.dashMinhasAcoes)
            dialogMenuLateral.dismiss()
        }
        binding.linearCadastroDeProposto.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.CadastroPreposto), ProjetoStrings.equipeGerenciar)
            dialogMenuLateral.dismiss()

        }
        binding.linearGestaoComiisao.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.GestaoDeComisso), ProjetoStrings.dashComissao)
            dialogMenuLateral.dismiss()
        }
        binding.linearExtratoComissao.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.ExtratoDeComissões), ProjetoStrings.dashExtrato)
            dialogMenuLateral.dismiss()
        }

        if (nomeRepresetanteTextFormat.length >= 15){
            nomeRepresetanteTextFormat = nomeRepresetanteTextFormat.substring(0,14) + "..."
        }
        binding.fecharMenu.setOnClickListener {
            dialogMenuLateral.dismiss()
        }
        binding.nomeRepresentante.text = nomeRepresetanteTextFormat
        binding.cnpjRepresentante.text = FormatarTexto().formatCNPJ(cnpjRepresentanteText)
        binding.linearMenuMeusDados.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.DadosCadastrais), EnumMenu.DADOSCADASTRAIS)
            dialogMenuLateral.dismiss()
        }

        binding.linearCargas.setOnClickListener {
            viewModelActHome.atualizaFragmentHome("", EnumMenu.CARGAS)
            dialogMenuLateral.dismiss()
        }

        binding.linearVendas.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.vendas), ProjetoStrings.dashVendas)
            dialogMenuLateral.dismiss()
        }
        binding.linearRelatorios.setOnClickListener {
            viewModelActHome.atualizaWebView(context.getString(R.string.relatorios), ProjetoStrings.dashComissao)
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
        binding.linearDadosBancarios.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.dadosBancarios), EnumMenu.DADOSBANCARIOS)
            dialogMenuLateral.dismiss()
        }
        binding.linearAreaAruacao.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.DadosAreaAtuacao), EnumMenu.DADOSAREATUACAO)
            dialogMenuLateral.dismiss()
        }

        binding.linearMenuHome.setOnClickListener {
            viewModelActHome.atualizaFragmentHome(context.getString(R.string.home), EnumMenu.HOME)
            dialogMenuLateral.dismiss()
        }

        binding.sair.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        binding.sair.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}