package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.ConfirmaTokenRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.SolicitaTokenRquest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaConfirmaToken
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaSolicitaToken
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.TokenRepository
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.PreferenciasUtils
import br.com.visaogrupo.tudofarmarep.Utils.SistemaUtils

class TokenUseCase(
    private val tokenRepository: TokenRepository,
    private val preferenciasUtils: PreferenciasUtils,
    private val sistemaUtils: SistemaUtils
) {

    suspend fun solicitaToken(telefone: String): RespostaSolicitaToken? {
        val solicitaTokenRequest = SolicitaTokenRquest("", telefone)
        return tokenRepository.solicitaTokenReposiory(solicitaTokenRequest)
    }

    suspend fun confirmaToken(token: String): RespostaConfirmaToken? {
        val cnpj = preferenciasUtils.recuperarTexto(ProjetoStrings.cnpjCadastro) ?: ""
        val celular = preferenciasUtils.recuperarTexto(ProjetoStrings.celular) ?: ""
        val udid = sistemaUtils.recuperaUdid()

        val confirmaTokenRequest = ConfirmaTokenRequest("", celular, token, udid, cnpj)
        return tokenRepository.confirmaTokenRepository(confirmaTokenRequest)
    }
}