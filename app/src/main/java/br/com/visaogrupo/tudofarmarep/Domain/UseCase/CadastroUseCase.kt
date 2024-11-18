package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import android.content.Context
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.CadastroRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.URLs
import br.com.visaogrupo.tudofarmarep.Utils.ImagensUltis
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils

class CadastroUseCase(
      val  cadastroRepository: CadastroRepository,
      val preferenciasUtils: PreferenciasUtils
) {
     fun enviaCadastro(){
        cadastroRepository.enviaCadastro()
    }
     fun enviaCadastroFinal():Boolean{
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