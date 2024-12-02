package br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home

import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request.DadosBancariosRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaDadosBancarios
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancaria
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaInstituicaoBancariaDados
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.DadosBancariosRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class DadosBancariosUseCase(
    private val repository: DadosBancariosRepository,
    private val preferenciasUtils: PreferenciasUtils
) {

    fun dadosBancarios() : RespostaDadosBancarios?{
        val representanteID = preferenciasUtils.recuperarInteiro(ProjetoStrings.reprenteID, 0)
        val dadosBancariosRequest = DadosBancariosRequest(representanteID)
        val dadosBancarios = repository.buscaDadosBancarios(dadosBancariosRequest)
        return dadosBancarios
    }
    fun dadosInstituicaoBancaria() : ArrayList< RespostaInstituicaoBancaria?> {
       return repository.buscaInstituicaoBancaria()
    }
}