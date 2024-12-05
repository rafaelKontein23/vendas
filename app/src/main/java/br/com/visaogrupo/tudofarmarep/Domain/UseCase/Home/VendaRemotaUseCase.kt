package br.com.visaogrupo.tudofarmarep.Domain.UseCase.Home

import br.com.visaogrupo.tudofarmarep.Repository.Model.Home.Request.HashVendaRemotaRequest
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Home.VendaRemotaRepository

class VendaRemotaUseCase (
    private val vendaRemotaRepository: VendaRemotaRepository
) {
    fun buscaDadosVendaRemota(representaneterID: Int):String{
        val hashVendaRemotaRequest = HashVendaRemotaRequest(representaneterID)
        val hash = vendaRemotaRepository.constroiHash(hashVendaRemotaRequest)
        return hash
    }
}