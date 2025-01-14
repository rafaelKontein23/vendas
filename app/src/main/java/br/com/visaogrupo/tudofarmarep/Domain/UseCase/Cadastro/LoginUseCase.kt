package br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro

import FormularioCadastro
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.FlagsRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.LoginRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaLogin
import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Respostas.RespostaFlags
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.LoginRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.PushFirebase
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class LoginUseCase (
    val loginRepository: LoginRepository,
    val preferenciasUtils: PreferenciasUtils,
    val sistemaUtils: SistemaUtils
) {
    fun logaUsuario():RespostaLogin? {
        val cnpj = preferenciasUtils.recuperarTexto(ProjetoStrings.cnpjCadastro) ?:""
        val celular = preferenciasUtils.recuperarTexto(ProjetoStrings.celular) ?:""
        val udid = sistemaUtils.recuperaUdid()
        val dispositivo = sistemaUtils.recuperaNomeDispositivo()

        val loginRequest = LoginRequest(CNPJ = cnpj,Celular =  celular,DeviceToken = SistemaUtils.deviceToken,UDID = udid, VersaoApp = ProjetoStrings.versapApp, Dispositivo =  dispositivo)

        val respostaLogin  =  loginRepository.logaUsuario(loginRequest)


        if(respostaLogin != null){
            preferenciasUtils.salvarBool(respostaLogin.Teste, ProjetoStrings.isUsuarioTeste)
            if (respostaLogin.Representante_ID != 0){
                val listaFlags = buscaFlags(respostaLogin.Representante_ID)
                if (listaFlags.isNotEmpty()){
                    for (flags in listaFlags){
                        FormularioCadastro.atualizarFlags(flags)
                        FormularioCadastro.fotoPerfilUrl =respostaLogin.FotoPerfil ?: ""
                    }
                }
                preferenciasUtils.salvarTexto(respostaLogin.Hash ?: "",ProjetoStrings.hashLogin)
                preferenciasUtils.salvarTexto(respostaLogin.CNPJ, ProjetoStrings.cnpjLogin)
                preferenciasUtils.salvarTexto(respostaLogin.Celular,ProjetoStrings.celular)
            }
        }

        return respostaLogin
    }


    fun buscaFlags(representanteID:Int):ArrayList<RespostaFlags> {
        val  featFlags = loginRepository.buscaFeatFlags(FlagsRequest(Representante_ID = representanteID))
        return featFlags
    }

}