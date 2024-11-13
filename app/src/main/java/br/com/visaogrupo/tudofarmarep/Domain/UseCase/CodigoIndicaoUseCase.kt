package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CodigoIndicacaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCodigoIndicacao
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CodigoIndicacaoRepository

class CodigoIndicaoUseCase (
    val codigoIndicacaoRepository: CodigoIndicacaoRepository
) {

    fun buscaInfoHash(codigoIndicacao: String):RespostaCodigoIndicacao? {
        val codigoIndicacao = CodigoIndicacaoRequest(codigoIndicacao)
        val respostaCodigoIndicacao = codigoIndicacaoRepository.cadastrarCodigoIndicacao(codigoIndicacao)
        return respostaCodigoIndicacao
    }

}