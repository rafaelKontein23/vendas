package br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home

import android.util.Log
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaNotificacao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request.NotificacaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.NotificacaoRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class NotificacaoUseCase (val notificacaoRepository: NotificacaoRepository,
   val preferenciasUtils: PreferenciasUtils) {

    fun buscaNotificacao():ArrayList<RespostaNotificacao>{

            val representanteID = preferenciasUtils.recuperarInteiro(ProjetoStrings.reprenteID, 0)
            val notificacaoRquest = NotificacaoRequest(representanteID)
            val resultadoNotificacao = notificacaoRepository.buscaNotificacoes(notificacaoRquest)
            return resultadoNotificacao


    }
}