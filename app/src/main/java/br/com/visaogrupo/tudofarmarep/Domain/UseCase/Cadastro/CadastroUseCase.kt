package br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro

import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.ImagensUltis
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class CadastroUseCase(
      val  cadastroRepository: CadastroRepository,
      val preferenciasUtils: PreferenciasUtils,
      val sistemaUtils: SistemaUtils
) {
     fun enviaCadastro(){
        cadastroRepository.enviaCadastro()
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