package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import android.icu.text.UFormat
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.MessoRegiaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.AreaDeAtuacaoRepository

class AreaDeAtuacaoUseCase(
    val areaDeAtuacaoRepository: AreaDeAtuacaoRepository
) {
    fun recuperaDadosMesorregiao(UF: String): List<RespostaMessoRegiao>? {
        val ufFormat = UF.split(" - ").last()
        val mesoRegiaoRequest = MessoRegiaoRequest(ufFormat)
        return areaDeAtuacaoRepository.recuperaDadosMesorregiao(mesoRegiaoRequest)
    }
}