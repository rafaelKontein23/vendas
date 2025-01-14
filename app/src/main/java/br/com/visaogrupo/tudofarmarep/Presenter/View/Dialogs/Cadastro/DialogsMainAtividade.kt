package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.ActCelular
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.ActToken
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.MainActivity
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelMainActivity
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.DialogConfig
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCnpj
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraTelefone
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.iniciaisNome
import br.com.visaogrupo.tudofarmarep.databinding.DialogBiometriaBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogSenhaAmbienteBinding
import br.com.visaogrupo.tudofarmarep.databinding.DialogTrocaAmbienteBinding

class DialogsMainAtividade (private val activity: AppCompatActivity,
                            private val viewModel: ViewModelMainActivity
     ) {

     fun dialogSenha() {
          val dialogSenha = Dialog(activity)
          val binding = DialogSenhaAmbienteBinding.inflate(LayoutInflater.from(activity))



          dialogSenha.requestWindowFeature(Window.FEATURE_NO_TITLE)
          dialogSenha.setContentView(binding.root)

          dialogSenha.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
          dialogSenha.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          dialogSenha.window?.attributes?.windowAnimations = R.style.animacaoDialog
          dialogSenha.window?.setGravity(Gravity.BOTTOM)
          dialogSenha.show()
          binding.fecharModal.setOnClickListener {
               dialogSenha.dismiss()
          }

          viewModel.senhaVisualizar.observe(activity){ senhaVisualizar ->
               if (senhaVisualizar) {
                    binding.visualizarSenha.setImageResource(R.drawable.olho_off)
                    binding.inputSenha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
               } else {
                    binding.visualizarSenha.setImageResource(R.drawable.olho)
                    binding.inputSenha.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

               }
               binding.inputSenha.setSelection(binding.inputSenha.text?.length ?: 0)
          }

          binding.visualizarSenha.setOnClickListener {
                viewModel.alterarSenhaVisualizar()
          }

          viewModel.confereSenha.observe(activity){confereSenha ->
               if(confereSenha){
                    dialogSenha.dismiss()
                    dialogEscolhaAmbiente()
               }else{
                    Toast.makeText(activity, activity.getString(R.string.erroSenha), Toast.LENGTH_LONG).show()
                    binding.inputSenha.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_red500)
                    binding.inputSenha.setTextColor(activity.getColor(R.color.danger500))
               }
          }

          binding.btnConfereSenha.setOnClickListener {
               val senhaCap = binding.inputSenha.text.toString()
               viewModel.confereSenha(senhaCap)
          }

          binding.inputSenha.addTextChangedListener(object : TextWatcher{
               override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
               ) {
               }

               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    binding.inputSenha.setBackgroundResource(R.drawable.bordas_radius_8_stroke_1_black)
                    binding.inputSenha.setTextColor(activity.getColor(R.color.black))

               }

               override fun afterTextChanged(s: Editable?) {
               }

          })
     }

     fun dialogEscolhaAmbiente() {
          val dialogEscolhaAmbiente = Dialog(activity)
          val binding = DialogTrocaAmbienteBinding.inflate(LayoutInflater.from(activity))

          dialogEscolhaAmbiente.requestWindowFeature(Window.FEATURE_NO_TITLE)
          dialogEscolhaAmbiente.setContentView(binding.root)

          dialogEscolhaAmbiente.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
          dialogEscolhaAmbiente.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          dialogEscolhaAmbiente.window?.attributes?.windowAnimations = R.style.animacaoDialog
          dialogEscolhaAmbiente.window?.setGravity(Gravity.BOTTOM)
          dialogEscolhaAmbiente.show()
          binding.fecharModal.setOnClickListener {
               dialogEscolhaAmbiente.dismiss()
          }
          binding.btnAr.setOnClickListener {
               viewModel.trocaAmbienteModal(1)
               dialogEscolhaAmbiente.dismiss()
          }
          binding.btnStage.setOnClickListener {
               viewModel.trocaAmbienteModal(5)
               dialogEscolhaAmbiente.dismiss()
          }
          binding.btnQA.setOnClickListener {
               viewModel.trocaAmbienteModal(2)
               dialogEscolhaAmbiente.dismiss()
          }

          binding.btnE.setOnClickListener {
               viewModel.trocaAmbienteModal(3)
               dialogEscolhaAmbiente.dismiss()
          }
          binding.btnI.setOnClickListener {
               viewModel.trocaAmbienteModal(4)
               dialogEscolhaAmbiente.dismiss()
          }

     }


     fun dialogBiometria(viewModel: ViewModelMainActivity, context: MainActivity){
          val dialogBiometria = Dialog(activity)
          val binding = DialogBiometriaBinding.inflate(LayoutInflater.from(activity))
          dialogBiometria.requestWindowFeature(Window.FEATURE_NO_TITLE)
          dialogBiometria.setContentView(binding.root)
          DialogConfig.configuraDialog(dialogBiometria, activity)
          dialogBiometria.window?.let { window ->
               val layoutParams = window.attributes?.apply {
                    width = WindowManager.LayoutParams.MATCH_PARENT
                    height = WindowManager.LayoutParams.WRAP_CONTENT
               }
               window.attributes = layoutParams
               window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
               window.attributes?.windowAnimations = R.style.animacaoDialog
               window.setGravity(Gravity.BOTTOM)
          }
          dialogBiometria.setCancelable(false)
          binding.btnEntrar.setOnClickListener {
               if (viewModel.checkBiometricSupport(context)){
                    dialogBiometria.dismiss()
                    viewModel.showBiometricPrompt(context = context)

               }else{
                    Toast.makeText(context, context.getString(R.string.erroiometria), Toast.LENGTH_LONG).show()
               }
          }
          binding.btnAcessarOutraConta.setOnClickListener {
               var intent = Intent(context, ActCelular::class.java)
               context.startActivity(intent)
               dialogBiometria.dismiss()
          }
          binding.fecharModal.setOnClickListener {
               dialogBiometria.dismiss()
          }
          viewModel.celularUsuario.observe(context){
               binding.celularRepresentante.text = it.aplicarMascaraTelefone()
          }
          viewModel.nomeusaurio.observe(context){
               if(it.isNotEmpty()){
                    binding.nomeUsuario.text = it
                    binding.textoIniciais.text =  it.iniciaisNome()
               }
          }
          binding.btnEntrarComToken.setOnClickListener {
               dialogBiometria.dismiss()
               context.startActivity(Intent(context, ActToken::class.java))
          }

          viewModel.recuperaInformacoesUser()
          val  cnpj = viewModel.recuperaCnpj()
          if (cnpj != null){
               binding.cnpjRepresentante.text = cnpj.aplicarMascaraCnpj()

          }
          dialogBiometria.show()

     }
}