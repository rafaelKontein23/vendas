package br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.LoginRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaLogin
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

        return respostaLogin
    }

}