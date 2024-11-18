package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RepostaCnpj
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CnpjRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class CnpjUseCase (
    val cnpjRepository: CnpjRepository,
    val preferenciasUtils: PreferenciasUtils
){
    suspend fun buscaDadosCnpjUseCase(): RepostaCnpj?{
        val cnpj = preferenciasUtils.recuperarTexto(ProjetoStrings.cnpjCadastro)
        val  responseCnpj = cnpjRepository.buscaDadosCnpj(cnpj!!)
        return responseCnpj
    }
}