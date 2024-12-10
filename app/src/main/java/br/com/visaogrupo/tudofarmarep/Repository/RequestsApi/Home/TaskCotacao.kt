package br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome

import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoItemCotacao
import br.com.visaogrupo.tudofarmarep.Objetos.Cotacao
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Criptho
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitWS
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.Support

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.IOException
import org.json.JSONObject

class TaskCotacao {

    fun buscaCotacao(representanteID:Int):ArrayList<Cotacao>{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val jsonObject = JSONObject()
            jsonObject.put("Representante_ID",representanteID)
            val body = Support.CRIPTHO.encode(jsonObject.toString(), Criptho.BASE64_MODE)
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_ListaCotacoes(requestBody).execute()
            val listaCotacao = ArrayList<Cotacao>()
            if (request.isSuccessful){
                val responseBody = request.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                val json = jsonResponse.getJSONArray("Dados")
                for (i in 0 until json.length()){
                    val jsonCotacao = json.getJSONObject(i)
                    val CNPJ = jsonCotacao.getString("CNPJ")
                    val CarrinhoId = jsonCotacao.getInt("CarrinhoId")
                    val Hash = jsonCotacao.getString("Hash")
                    val Marca_ID = jsonCotacao.getInt("Marca_ID")
                    val Nome = jsonCotacao.getString("Nome")
                    val RazaoSocial = jsonCotacao.getString("RazaoSocial")
                    val TotalPedido = jsonCotacao.getDouble("TotalPedido")
                    val nomeLoja = jsonCotacao.getString("NomeLoja")
                    val dataPedido = jsonCotacao.getString("DataPedido")
                    val ImagemMarca = jsonCotacao.getString("ImagemMarca")
                    val lojaId = jsonCotacao.getInt("Loja_Id")
                    val operedorGrupoId = jsonCotacao.getInt("OperadorLogistico_Grupo_ID")
                    val uf = jsonCotacao.getString("UF")
                    val cidade = jsonCotacao.getString("Cidade")
                    val bairro = jsonCotacao.getString("Bairro")
                    val endereco = jsonCotacao.getString("Endereco")
                    val numero = jsonCotacao.getString("Numero")
                    val cep = jsonCotacao.getString("CEP")
                    val nomeOperador = jsonCotacao.getString("NomeOperador")
                    val formaPagamentoMarcasID = jsonCotacao.getInt("FormaPagamentoMarcas_ID")
                    val  OperadorLogistico1 = jsonCotacao.getString("OperadorLogistico1")?: ""
                    val  OperadorLogistico2 = jsonCotacao.getString("OperadorLogistico2")?: ""
                    val  OperadorLogistico3 = jsonCotacao.getString("OperadorLogistico3")?: ""
                    val  OperadorLogistico4 = jsonCotacao.getString("OperadorLogistico4") ?:""
                    val  OperadorLogistico5 = jsonCotacao.getString("OperadorLogistico5") ?: ""

                    val cotacao = Cotacao(CNPJ,CarrinhoId,Hash,Marca_ID,Nome,RazaoSocial,TotalPedido,nomeLoja,dataPedido,ImagemMarca,lojaId, operedorGrupoId, uf,
                        cidade, bairro, endereco, numero, cep, nomeOperador, formaPagamentoMarcasID =  formaPagamentoMarcasID)
                    cotacao.listaOperadoreslooping.add(OperadorLogistico1)
                    cotacao.listaOperadoreslooping.add(OperadorLogistico2)
                    cotacao.listaOperadoreslooping.add(OperadorLogistico3)
                    cotacao.listaOperadoreslooping.add(OperadorLogistico4)
                    cotacao.listaOperadoreslooping.add(OperadorLogistico5)

                    listaCotacao.add(cotacao)
                }
                return listaCotacao

            }else{
                return ArrayList()
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ArrayList()

        }catch (i:IOException){
            i.printStackTrace()
            return ArrayList()
        }
    }

    fun buscaCarrinhoCotacao(representanteID:Int, CarrinhoId:Int):ArrayList<CarrinhoItemCotacao>{
        try {
            val isync = RetrofitWS().createService(Isync::class.java)
            val jsonObject = JSONObject()
            jsonObject.put("Representante_ID",representanteID)
            jsonObject.put("CarrinhoId",CarrinhoId)
            val body = Support.CRIPTHO.encode(jsonObject.toString(), Criptho.BASE64_MODE)
            val mediaType =  "application/json".toMediaTypeOrNull()
            val requestBody = RequestBody.create(mediaType, body)
            val request = isync.P_ListaProdutosCotacao(requestBody).execute()
            val listaCarrinhoCotacao = ArrayList<CarrinhoItemCotacao>()
            if (request.isSuccessful){
                val responseBody = request.body()!!.string()
                val jsonResponse = JSONObject(Support.CRIPTHO.decode(responseBody, Criptho.BASE64_MODE))
                val json = jsonResponse.getJSONArray("Dados")
                for (i in 0 until json.length()){
                    val jsonCotacaoCarrinho = json.getJSONObject(i)
                    val carrinho = CarrinhoItemCotacao(
                        carrinhoId = jsonCotacaoCarrinho.getInt("CarrinhoId"),
                        representanteId = jsonCotacaoCarrinho.getInt("Representante_id"),
                        operadorId = jsonCotacaoCarrinho.getString("OperadorId"),
                        cnpj = jsonCotacaoCarrinho.getString("CNPJ"),
                        lojaId = jsonCotacaoCarrinho.getInt("Loja_Id"),
                        marcaId = jsonCotacaoCarrinho.getInt("Marca_Id"),
                        nome = jsonCotacaoCarrinho.getString("Nome"),
                        barra = jsonCotacaoCarrinho.getString("Barra"),
                        quantidade = jsonCotacaoCarrinho.getInt("Quantidade"),
                        valorUnitario = jsonCotacaoCarrinho.getDouble("ValorUnitario"),
                        valorTotal = jsonCotacaoCarrinho.getDouble("ValorTotal"),
                        desconto = jsonCotacaoCarrinho.getDouble("Desconto")
                    )
                    listaCarrinhoCotacao.add(carrinho)
                }
                return listaCarrinhoCotacao

            }else{
                return ArrayList()
            }
        }catch (e:Exception){
            e.printStackTrace()
            return ArrayList()

        }catch (i:IOException){
            i.printStackTrace()
            return ArrayList()
        }
    }
}