package br.com.visaogrupo.tudofarmarep.Domain.UseCase.Cadastro

import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.AreaAtuacaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.Cidade
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.Mesorregiao
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.MessoRegiaoRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaAreaAtuacaoCadastrais
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaAreaAtuacaoCadastraisDados
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Respostas.RespostaCidades
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
        val item = RespostaMessoRegiao(0, "Todas", "Todas")

        if (lista != null) {
            lista.add(0, item)

            lista.forEach { item ->
                if (listaRespostaMessoRegiao.none { it.Mesorregiao_Nome == item.Mesorregiao_Nome }) {
                    listaRespostaMessoRegiao.add(item)
                }
                val cidadeResposta = RespostaCidades(item.Mesorregiao_id, item.Municipio)
                listaCidades.add(cidadeResposta)
            }
            return Triple(listaRespostaMessoRegiao, listaCidades, lista)
        }else{
            return Triple(null, null, null)
        }
    }
    suspend fun recuperaDadosCadastraisAreaAtuacao(representanteId:Int):ArrayList<RespostaAreaAtuacaoCadastrais>{
        val areaAtuacaoRequest = AreaAtuacaoRequest(representanteId)
        val listaAreaAtuacao = areaDeAtuacaoRepository.recuperaDadosCastraisAreaAtuacao(areaAtuacaoRequest)
        return listaAreaAtuacao

    }
    fun converterParaEstado(
        uf: String,
        listaMesorregioes: List<RespostaMessoRegiao>,
        listaCidades: List<RespostaCidades>
    ): CadastroRequestAreaAtuacal {
        val mesorregioesMapeadas = listaMesorregioes.groupBy { it.Mesorregiao_id }.map { (mesoId, municipios) ->
            val mesorregiaoNome = municipios.first().Mesorregiao_Nome
            val cidadees  = ArrayList<Cidade>()
            listaCidades.forEach {
                if (it.ID == mesoId) {
                    val cidade = Cidade(it.Cidade)
                    cidadees.add(cidade)
                }
            }
            Mesorregiao(
                Mesorregiao = mesorregiaoNome,
                Mesorregiao_id = mesoId,
                Cidades = cidadees
            )
        }
        if(uf.length >2){
            val ufFormat = uf.split(" - ").last()

            return CadastroRequestAreaAtuacal(
                UF = ufFormat,
                Mesorregioes = mesorregioesMapeadas
            )
        }

        return CadastroRequestAreaAtuacal(
            UF = uf,
            Mesorregioes = mesorregioesMapeadas
        )
    }
}