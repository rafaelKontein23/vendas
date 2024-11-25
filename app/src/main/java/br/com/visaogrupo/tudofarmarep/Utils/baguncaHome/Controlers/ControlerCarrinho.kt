package br.com.visaogrupo.tudofarmarep.Controlers

import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskEnviaPedido
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.IVoltaLoja
import br.com.visaogrupo.tudofarmarep.Carga.ultis.CapturaIP
import br.com.visaogrupo.tudofarmarep.DAO.DAOCarrinho
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper

import br.com.visaogrupo.tudofarmarep.Objetos.Empresa
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.OperadorLogistico
import br.com.visaogrupo.tudofarmarep.Objetos.Operadores
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Objetos.UsuarioLoiu
import br.com.visaogrupo.tudofarmarep.Utils.Constantes.ProjetoStrings
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ControlerCarrinho {
    @RequiresApi(Build.VERSION_CODES.O)
    fun enviaPedido(listaProdutos:ArrayList<Produtos>, observacao:String,
                    numeroPedido:String,
                    listaOperadoresSelecionados: ArrayList<OperadorLogistico>,
                    formaDePagamento:String,
                    loja:Lojas,
                    empresa: Empresa,
                    context: Context,
                    voltaLoja:IVoltaLoja,
                    constrainCarregando:ConstraintLayout,
                    valorTotalStCarrinho: Double
    ){

        CoroutineScope(Dispatchers.IO).launch {
            val  jsonPedido = montaJsonPedido(listaProdutos,observacao,numeroPedido,listaOperadoresSelecionados,formaDePagamento, loja, empresa,context, valorTotalStCarrinho)
            val taskEnviaPedido = TaskEnviaPedido()
            val (valido, mensagem) = taskEnviaPedido.enviaPedido(jsonPedido)
            MainScope().launch {
                if(valido){
                    constrainCarregando.visibility = android.view.View.GONE
                    Alertas.alertaErro(context,mensagem,"Sucesso",){
                        CoroutineScope(Dispatchers.IO).launch {
                            val daoHelper = DAOHelper(context).writableDatabase
                            val daoCarrinho = DAOCarrinho()
                            for(produto in listaProdutos){
                                daoCarrinho.deletarTbCarrinho(daoHelper,produto)
                            }
                            MainScope().launch {
                                voltaLoja.voltaLoja()
                            }
                        }


                    }
                }else{
                    Alertas.alertaErro(context,mensagem,"Ops!"){

                    }
                    constrainCarregando.visibility = android.view.View.GONE
                }
            }

        }

    }
   @RequiresApi(Build.VERSION_CODES.O)
   suspend fun montaJsonPedido(listaProdutos:ArrayList<Produtos>, observacao:String,
                               numeroPedido:String,
                               listaOperadoresSelecionados: ArrayList<OperadorLogistico>,
                               formaDePagamento:String,
                               loja:Lojas    /*usuario:UsuarioLoiu*/, empresa: Empresa, context: Context,
                               valorTotalStCarrinho: Double):String{

        val pedidoId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        var operadoresStr = ""
        listaOperadoresSelecionados.forEachIndexed { index, operador ->
            operadoresStr +=   "\"OperadorLogistico${index+1}\": ${operador.OperadorLogistico_ID},\n"
        }

        var jsonProdutos = ""
        listaProdutos.forEachIndexed { index, produto ->
            jsonProdutos += "{\n" +
                    "\"Comissao\": ${produto.progressiva?.comissaoTotal},\n" +
                    "\"ComissaoPorcentagem\": ${produto.progressiva?.comissao},\n" +
                    "\"ComissaoUnitaria\": ${produto.progressiva?.comissaoTotal!! / produto.quantidadeAdicionada},\n" +
                    "\"MarcasXComissoes_id\": 0,\n" +
                    "\"Produto_codigo\": 0,\n" +
                    "\"Barra\": \"${produto.Barra}\",\n" +
                    "\"Quantidade\": ${produto.quantidadeAdicionada},\n" +
                    "\"stUnitaria\": ${produto.progressiva!!.stUnitario},\n" +
                    "\"stTotal\": ${produto.progressiva!!.stUnitario * produto.quantidadeAdicionada},\n" +
                    "\"Kit_Cod\": 0,\n" +
                    "\"Kit_Qtd\": 0,\n" +
                    "\"PF\": ${produto.progressiva?.valorUnitario},\n" +
                    "\"Valor\": ${produto.progressiva?.valorUnitarioDesconto},\n" +
                    "\"ValorOriginal\": ${produto.valorProdutoTotal},\n" +
                    "\"Desconto\": ${produto.progressiva?.desconto},\n" +
                    "\"Desconto_Original\": ${produto.progressiva?.desconto},\n" +
                    "\"ST\": 0,\n" +
                    "\"Formalizacao\": 0,\n" +
                    "\"CODLISTAPRECOSYNC\": 0,\n" +
                    "\"Apontador_Codigo\": 0,\n" +
                    "\"ArquivoPreco\": \"${produto.progressiva?.arquivoPreco}\"\n" +
                    "}"
            if (index != listaProdutos.size - 1) {
                jsonProdutos += ",\n"
            }
        }

        val ip = CapturaIP.pegaIP()
       val prefs = PreferenceManager.getDefaultSharedPreferences(context)
       val reprsentanteID = prefs.getInt("reprsentante_id", 0)
        val jsonPedido = "[{\n" +
                "\"Pedido_id\": $pedidoId,\n" +
                "\"Representante_id\": $reprsentanteID,\n" +
                "\"LojaId\": ${loja.Loja_ID},\n" +
                "\"TotalSt\": ${valorTotalStCarrinho},\n" +
                "\"TipoLoja\": ${loja.LojaTipo_ID},\n" +
                "\"MarcaID\": ${loja.marcaID},\n" +
                operadoresStr +
                "\"SequenciaLooping\": \"\",\n" +
                "\"CarrinhoId\": \"${loja.carrinhoID}\",\n" +
                "\"CNPJ\": \"${empresa.cnpj}\",\n" +
                "\"IP\": \"$ip\",\n" +
                "\"Observacao\": \"$observacao\",\n" +
                "\"NumeroPedido\": \"$numeroPedido\",\n" +
                "\"Teste\": true,\n" +
                "\"UF\": \"${empresa.uf}\",\n" +
                "\"Dispositivo\": \"Android\",\n" +
                "\"VersaoApp\": \"${ProjetoStrings.versapApp}\",\n" +
                "\"FormaPagamento\": \"$formaDePagamento\",\n" +
                "\"CodigoMGM\": \"\",\n" +
                "\"IdMobile\": \"\",\n" +
                "\"Produtos\": [\n$jsonProdutos\n]\n" +
                "}]"

        return jsonPedido

    }
}