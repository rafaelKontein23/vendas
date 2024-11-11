package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.MessoRegiaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.AreaDeAtuacaoRepository

class AreaDeAtuacaoUseCase(
    val areaDeAtuacaoRepository: AreaDeAtuacaoRepository
) {
    fun recuperaDadosMesorregiao(UF: String): List<RespostaMessoRegiao>? {
        val mesoRegiaoRequest = MessoRegiaoRequest(UF)
        return areaDeAtuacaoRepository.recuperaDadosMesorregiao(mesoRegiaoRequest)
    }
}