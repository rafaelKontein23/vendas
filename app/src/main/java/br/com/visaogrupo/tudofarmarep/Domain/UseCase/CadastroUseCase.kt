package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository

class CadastroUseCase(
      val  cadastroRepository: CadastroRepository
) {
    suspend fun enviaCadastro(CadastroRequest: CadastroRequest){
        cadastroRepository.enviaCadastro(CadastroRequest)
    }

}