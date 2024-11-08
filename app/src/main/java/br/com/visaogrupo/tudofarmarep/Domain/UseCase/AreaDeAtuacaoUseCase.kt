package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.AreaDeAtuacaoRepository

class AreaDeAtuacaoUseCase(
    val areaDeAtuacaoRepository: AreaDeAtuacaoRepository
) {
    fun recuperaDadosMesorregiao(UF: String){
        areaDeAtuacaoRepository.recuperaDadosMesorregiao(UF)
    }


}