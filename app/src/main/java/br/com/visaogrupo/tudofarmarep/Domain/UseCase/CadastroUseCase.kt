package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro

class CadastroUseCase(
      val  cadastroRepository: CadastroRepository
) {
    suspend fun enviaCadastro(){
        cadastroRepository.enviaCadastro()
    }

}