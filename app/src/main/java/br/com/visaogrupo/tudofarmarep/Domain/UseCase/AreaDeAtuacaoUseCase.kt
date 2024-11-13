package br.com.visaogrupo.tudofarmarep.Domain.UseCase

import android.icu.text.UFormat
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CidadesRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.MessoRegiaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCidades
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCidadesDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaMessoRegiao
import br.com.visaogrupo.tudofarmarep.Repository.RequestsApi.Cadastro.AreaDeAtuacaoRepository

class AreaDeAtuacaoUseCase(
    val areaDeAtuacaoRepository: AreaDeAtuacaoRepository
) {
    suspend fun recuperaDadosMesorregiao(UF: String): Triple<ArrayList<RespostaMessoRegiao>?, ArrayList<RespostaCidades>?, ArrayList<RespostaMessoRegiao>?>  {
        val ufFormat = UF.split(" - ").last()
        val mesoRegiaoRequest = MessoRegiaoRequest(ufFormat)
        val lista = areaDeAtuacaoRepository.recuperaDadosMesorregiao(mesoRegiaoRequest)
        val listaRespostaMessoRegiao = ArrayList<RespostaMessoRegiao>()
        val listaCidades = ArrayList<RespostaCidades>()
        val item = RespostaMessoRegiao(0, "Todos", "Todos")

        if (lista != null) {
            lista.add(0, item)

            lista.forEach { item ->
                if (listaRespostaMessoRegiao.none { it.Mesorregiao_Nome == item.Mesorregiao_Nome }) {
                    listaRespostaMessoRegiao.add(item)
                }
                val cidadeResposta = RespostaCidades(0, item.Municipio)
                listaCidades.add(cidadeResposta)
            }
            return Triple(listaRespostaMessoRegiao, listaCidades, lista)
        }else{
            return Triple(null, null, null)
        }
    }
}