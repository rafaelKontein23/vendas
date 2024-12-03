package br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.DadosPessoaisRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaDadosPessoais
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.DadosPessoaisRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.ImagensUltis
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class CadastroUseCase(
      val  cadastroRepository: CadastroRepository,
      val preferenciasUtils: PreferenciasUtils,
      val sistemaUtils: SistemaUtils,
      val dadosPessoaisRepository: DadosPessoaisRepository? = null
) {
     fun enviaCadastro(): Boolean{
         val representanteId = preferenciasUtils.recuperarInteiro(ProjetoStrings.reprenteID, 0)
         val editaCadastro =  cadastroRepository.enviaCadastro(representanteId)
         return editaCadastro
    }
     fun enviaCadastroFinal():Boolean{

         FormularioCadastro.cadastro.DeviceToken = SistemaUtils.deviceToken
         FormularioCadastro.cadastro.UDID = sistemaUtils.recuperaUdid()
         FormularioCadastro.cadastro.VersaoaAPP = ProjetoStrings.versapApp
         FormularioCadastro.cadastro.Dispositivo = sistemaUtils.recuperaNomeDispositivo()
         FormularioCadastro.cadastro.SistemaOperacional = sistemaUtils.recuperaSO()
         val nomeArquivo = ProjetoStrings.strDocumeto + FormularioCadastro.cadastro.CNPJ+".jpeg"
         FormularioCadastro.cadastro.FotoDocumento = nomeArquivo
         FormularioCadastro.cadastro.ImagemAssinatura =  "${FormularioCadastro.cadastro.CNPJ}/${FormularioCadastro.cadastro.celular}.jpeg"

        val cadastro = cadastroRepository.enviaCadastroFinal()
        preferenciasUtils.salvarBool(cadastro, ProjetoStrings.casdastro)
        return cadastro
    }
    fun buscaDadosPessoaisCadastrais(): RespostaDadosPessoais? {

        val representanteId = preferenciasUtils.recuperarInteiro(ProjetoStrings.reprenteID, 0)
        val dadosPessoaisRequest = DadosPessoaisRequest(representanteId)
        val dadosPessoais = dadosPessoaisRepository!!.buscaDadosPessoaisCadastrais(dadosPessoaisRequest)
        return dadosPessoais

    }
     fun mandaFotoCadastro(): Boolean{
        var base64 = ""
        if (FormularioCadastro.base64Galeria.isEmpty()){
            base64 = ImagensUltis.uriToBase64WithResolver(cadastroRepository.contextUser, FormularioCadastro.fotoDocumeto).toString()

        }else{
            base64 = FormularioCadastro.base64Galeria
        }
        val nomeArquivo = ProjetoStrings.strDocumeto + FormularioCadastro.cadastro.CNPJ+".jpeg"
        return  cadastroRepository.envioFotoDocumentoBase64(base64, nomeArquivo)

    }
    fun enviaAssinatura(): Boolean{
        val base64 = FormularioCadastro.base64Assinatura
        val nomeArquivo = "${FormularioCadastro.cadastro.CNPJ}/${FormularioCadastro.cadastro.celular}.jpeg"
        return  cadastroRepository.enviaAssinatura(base64, nomeArquivo)
    }
}