package br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Utils.ConfiguracoesApi.RetrofitWs

class CodigoIndicacaoRepository(
    private val context: Context
) {
    val retrofitWs = RetrofitWs(context).createService(SincronoCadastro::class.java)

    fun cadastrarCodigoIndicacao(codigoIndicacao: String) {
        //val re
    }


}