package br.com.visaogrupo.tudofarmarep.Carga.interfaces

import br.com.visaogrupo.tudofarmarep.Objetos.MarcasResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

interface Isync {
    @GET("cargas/Padrao/marcas.json")
     fun getMarcas(): Call<MarcasResponse>

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl:String): Call<ResponseBody>

    @GET("cargas/Padrao/Lojas.json")
    fun getLoja():Call<ResponseBody>

    @GET("Cargas/Padrao/LojasXFormaPagamento.json")
    fun getLojaXFormaPagamento():Call<ResponseBody>


    @POST("ws/filtros/produtos")
    fun P_Loja_Filtros_Lista(@Body request: RequestBody) : Call<ResponseBody>

   @POST("ws/operador/loja")
   fun P_Carrinho_OperadoresXLojas (@Body request: RequestBody) : Call<ResponseBody>

   @POST("ws/envia/pedido/mobile")
   fun P_Pedido_EntradaMobile(@Body request: RequestBody) : Call<ResponseBody>

    @POST("ws/Banner/home")
    fun P_AppHomeBanner(@Body request: RequestBody) : Call<ResponseBody>

    @POST("ws/grafico/home")
    fun P_AppHomeGraficoMarcas(@Body request: RequestBody) : Call<ResponseBody>

    @POST("ws/Pedidos/Pendentes")
    fun P_AppHomePedidosPendentesAprovacao(@Body request: RequestBody) : Call<ResponseBody>



    @POST("ws/resumo/ganhos")
    fun P_AppHomeResumoMes(@Body request: RequestBody) : Call<ResponseBody>

    @POST("ws/lista/cnpjs")
    fun P_ListaEmpresas(@Body request: RequestBody) : Call<ResponseBody>

    @POST("ws/loiu/menu/lateral/itens")
    fun P_Portal_Funcionalidades_Lista(@Body request: RequestBody):Call<ResponseBody>

    @POST("ws/loiu/loja/consulta")
    fun P_Loja_Consulta (@Body request: RequestBody):Call<ResponseBody>

    @POST("ws/atualiza/carga")
    fun P_ListaCarga (@Body request: RequestBody):Call<ResponseBody>

    @POST("ws/links/detalhe/produto")
    fun P_ListaProdutosLinks (@Body request: RequestBody):Call<ResponseBody>

    @POST("ws/importar/carteira")
    fun P_MinhaCarteira_Importa(@Body request: RequestBody):Call<ResponseBody>


    @POST("ws/lista/cotacao")
    fun P_ListaCotacoes(@Body request: RequestBody):Call<ResponseBody>


    @POST("ws/lista/carrinho/cotacao")
    fun P_ListaProdutosCotacao(@Body request: RequestBody):Call<ResponseBody>

    @POST("ws/adicionar/carteira")
    fun P_GerenciaCarteira(@Body request: RequestBody):Call<ResponseBody>

    @POST("ws/tudofarmarep/dados/empresa")
    fun P_Portal_Cadastro_Consulta(@Body bodyCrypt: RequestBody?): Call<ResponseBody?>?
}
