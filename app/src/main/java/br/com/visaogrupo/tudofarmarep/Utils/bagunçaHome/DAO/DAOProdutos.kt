package br.com.visaogrupo.tudofarmarep.DAO

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Objetos.Progressiva

class DAOProdutos (){
    fun inserir(db: SQLiteDatabase, produtos: Produtos){
        db.beginTransaction()

        try {
            val valoresProdutos = ContentValues().apply {
                put("barra", produtos.Barra)
                put("marca_ID", produtos.Marca_ID)
                put("nome", produtos.Nome)
                put("imagem", produtos.Imagem)
            }
            db.insertOrThrow("TB_PRODUTOS",null,valoresProdutos)
            db.setTransactionSuccessful()

        }catch (e:Exception){
            e.printStackTrace();
        }finally {
            db.endTransaction()
        }


    }
    fun buscaProdutosOPLs(db: SQLiteDatabase, lojaID:Int, operadorLogisticoGrupoID:Int, cnpj:String, marcaID:Int,operadorNome:String,valorMaximo:Double):ArrayList<Pair<Int, Any>>{

        val query = "SELECT Produtos.* , CASE WHEN IFNULL(carrinho.valorTotal, 0) > 0 THEN 1 ELSE 0 END, " +
                "operadorlogisticogrupoid, carrinho.quantidade_adicionada, carrinho.valorTotal, carrinho.idProgressiva " +
                "FROM TB_PRODUTOS Produtos " +
                "INNER JOIN TB_PROGRESSIVA Progressiva ON Produtos.Barra = Progressiva.Barra " +
                "LEFT JOIN TB_CARRINHO carrinho ON carrinho.barra = Progressiva.barra AND carrinho.Loja_ID = Progressiva.loja_ID " +
                "AND carrinho.quantidade_adicionada > 0 AND carrinho.cnpj = '${cnpj}' " +
                "WHERE Progressiva.loja_ID = ${lojaID} AND operadorlogisticogrupoid = ${operadorLogisticoGrupoID} " +
                "AND Progressiva.marca_ID = ${marcaID} " +
                "UNION " +
                "SELECT Produtos.*, CASE WHEN IFNULL(carrinho.valorTotal, 0) > 0 THEN 1 ELSE 0 END, " +
                "carrinho.operadorID, carrinho.quantidade_adicionada, carrinho.valorTotal, carrinho.idProgressiva " +
                "FROM TB_PRODUTOS Produtos " +
                "INNER JOIN TB_PROGRESSIVA Progressiva ON Produtos.Barra = Progressiva.Barra " +
                "LEFT JOIN TB_CARRINHO carrinho ON carrinho.barra = Progressiva.barra AND carrinho.Loja_ID = Progressiva.loja_ID " +
                "AND carrinho.quantidade_adicionada > 0 AND carrinho.cnpj = '${cnpj}' " +
                "WHERE Progressiva.loja_ID = ${lojaID} AND CASE WHEN IFNULL(carrinho.valorTotal, 0) > 0 THEN 1 ELSE 0 END = 1 " +
                "AND Progressiva.marca_ID = ${marcaID}"

        Log.d("SQL_QUERY", query)

        val cursor = db.rawQuery(query, null)
        val listaBarra = ArrayList<String>()
        val listaProdutos = ArrayList<Pair<Int, Any>>()

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                try {
                    val barra = cursor.getString(0)
                    val marcaID = cursor.getInt(1)
                    val nome = cursor.getString(2)
                    val imagem = cursor.getString(3)
                    val isCarrinho = cursor.getInt(4)
                    var operadorLogisticoGrupoIdatual = cursor.getInt(5)
                    var quantidadeAdicionada = cursor.getInt(6)
                    var valorTotal = cursor.getDouble(7)
                    var idProgressiva = cursor.getInt(8)
                    var isCarrinhoOperdor = 0


                    val produtos = Produtos(barra,marcaID,
                        nome,
                        imagem,lojaID, isCarrinho = isCarrinho,
                        isCarrinhoOperdor = isCarrinhoOperdor,
                        quantidadeAdicionada = quantidadeAdicionada,cnpj= cnpj, valorProdutoTotal = valorTotal, valorMaximo =  valorMaximo)

                    val DaoProgressiva = DAOProgressiva()
                    val listaPRogressiva = DaoProgressiva.buscaProgressiva(db, produtos, operadorLogisticoGrupoID)
                    if (listaPRogressiva.isEmpty() && isCarrinho == 0) {
                        continue
                    }
                    if (listaPRogressiva.isEmpty() && isCarrinho == 1) {
                        isCarrinhoOperdor = 1
                    } else if (listaPRogressiva.isNotEmpty() && isCarrinho == 1 && operadorLogisticoGrupoIdatual != operadorLogisticoGrupoID) {
                        val daoCarrinho = DAOCarrinho()
                        produtos.idProgressiva = idProgressiva
                        daoCarrinho.uptadeCariinhoOperador(db, produtos, operadorLogisticoGrupoIdatual, operadorLogisticoGrupoID,operadorNome)
                    }

                    produtos.isCarrinhoOperdor = isCarrinhoOperdor
                    produtos.listaProgressiva.addAll(listaPRogressiva)

                    if (!listaBarra.contains(barra)) {
                        try {
                            listaProdutos.add(1 to produtos)
                            listaBarra.add(barra)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }

                    }

                } catch (e: Exception) {
                    Log.e("CursorError", "Erro ao processar o cursor: ${e.message}", e)
                }
            }
        }

        cursor?.close()
        return listaProdutos
    }
    fun select(
        db: SQLiteDatabase,
        listaLojas: ArrayList<Pair<Int, Any>>,
        loja: Lojas,
        operadorLogisticoGrupoID: Int,
        cnpj: String
    ) {
        val queryProdutos = """
     
SELECT DISTINCT
	Produtos.barra, 
    Produtos.nome, 
    Produtos.imagem, 
    Produtos.marca_ID, 
    Progressiva.*,
	CASE WHEN IFNULL(Carrinho.valorTotal, 0) > 0 THEN 1 ELSE 0 END as iscarrinho,
    CASE WHEN IFNULL(Progressiva.idProgressiva,0) > 0 THEN 1 ELSE 0 END AS IsProgressiva,
    Carrinho.quantidade_adicionada,
    Carrinho.operadorID
FROM (SELECT distinct ProdutosGeral.* 
      FROM TB_PRODUTOS ProdutosGeral 
      inner join TB_PROGRESSIVA ProgressivaProduto on ProgressivaProduto.barra = ProdutosGeral.barra 
      and loja_id = ${loja.Loja_ID}
      and ProgressivaProduto.marca_ID =  ${loja.marcaID} AND ProgressivaProduto.operadorLogisticoGrupoID = ${operadorLogisticoGrupoID}) Produtos
LEFT JOIN TB_CARRINHO Carrinho on Carrinho.marca_ID = Produtos.marca_ID 
								and Carrinho.barra = Produtos.barra 
                                and Carrinho.CNPJ = '${cnpj}'
                                and Carrinho.loja_ID = ${loja.Loja_ID}
inner join TB_PROGRESSIVA Progressiva on Progressiva.marca_ID  = Produtos.marca_ID
									and Progressiva.barra = Produtos.barra
                                    and Progressiva.loja_ID = ${loja.Loja_ID}
                                    and operadorlogisticogrupoid = ${operadorLogisticoGrupoID}
where Produtos.marca_id = ${loja.marcaID}

UNION

SELECT DISTINCT
	
	Produtos.barra, 
    Produtos.nome, 
    Produtos.imagem, 
    Produtos.marca_ID, 
    Progressiva.*,
	CASE WHEN IFNULL(Carrinho.valorTotal, 0) > 0 THEN 1 ELSE 0 END as iscarrinho,
    CASE WHEN IFNULL(Progressiva.idProgressiva,0) > 0 THEN 1 ELSE 0 END AS IsProgressiva,
    Carrinho.quantidade_adicionada,
    Carrinho.operadorID
FROM (SELECT distinct ProdutosGeral.* 
      FROM TB_PRODUTOS ProdutosGeral 
      inner join TB_PROGRESSIVA ProgressivaProduto on ProgressivaProduto.barra = ProdutosGeral.barra 
      and loja_id = ${loja.Loja_ID} 
      and ProgressivaProduto.marca_ID = ${loja.marcaID}
)  Produtos
LEFT JOIN TB_CARRINHO Carrinho on Carrinho.marca_ID = Produtos.marca_ID 
								and Carrinho.barra = Produtos.barra 
                                and Carrinho.CNPJ = '${cnpj}'
                                and Carrinho.loja_ID = ${loja.Loja_ID} 
left join TB_PROGRESSIVA Progressiva on Progressiva.marca_ID  = Produtos.marca_ID
									and Progressiva.barra = Produtos.barra
                                    and Progressiva.loja_ID = ${loja.Loja_ID} 
                                    and operadorlogisticogrupoid = ${operadorLogisticoGrupoID}                             
where Produtos.marca_id =  ${loja.marcaID}
order by Produtos.barra ASC
    
    """

        val cursor = db.rawQuery(queryProdutos, null)

        while (cursor.moveToNext()) {
            val barra = cursor.getString(0)
            val nomeProduto = cursor.getString(1)
            val imagemProduto = cursor.getString(2)
            val marcaID = cursor.getInt(3)
            val lojaID = loja.Loja_ID

            val quantidadePedido = cursor.getInt(6)
            val valorUnitario = cursor.getDouble(7)
            val desconto = cursor.getDouble(8)
            val valorUnitarioDesconto = cursor.getDouble(9)
            val valorTotalDesconto = cursor.getDouble(10)
            val lojaIDPortal = cursor.getInt(12)
            val quantidadeMaxima = cursor.getInt(13)
            val uf = cursor.getString(14)
            val origem = cursor.getString(15)
            val arquivoPreco = cursor.getString(16)
            val operadorLogisticoGrupoIDLeft = cursor.getInt(17)
            val nomeOperador = cursor.getString(19)
            val comissao = cursor.getDouble(20)
            val comissaoTotal = cursor.getDouble(21)
            val stUnitario = cursor.getDouble(22) ?: 0.0
            val id = cursor.getInt(23)
            var isCarrinho = cursor.getInt(24)
            var isProgressiva = cursor.getInt(25)
            val quantidadeAdicionada = cursor.getInt(26)
            val carrinhoOperador = cursor.getInt(27)

            val produtoExistente = listaLojas.find { it.second is Produtos && (it.second as Produtos).Barra == barra && (it.second as Produtos).lojaID == loja.Loja_ID }?.second as? Produtos

            if (produtoExistente != null) {
                val progressiva = Progressiva(
                    lojaID = lojaID,
                    barra = barra,
                    quantidadePedido = quantidadePedido,
                    valorUnitario = valorUnitario,
                    desconto = desconto,
                    valorUnitarioDesconto = valorUnitarioDesconto,
                    valorTotalDesconto = valorTotalDesconto,
                    lojaIDPortal = lojaIDPortal,
                    quantidadeMaxima = quantidadeMaxima,
                    uf = uf,
                    origem = origem,
                    arquivoPreco = arquivoPreco,
                    operadorLogisticoGrupoID = operadorLogisticoGrupoIDLeft,
                    imagem = imagemProduto,
                    nome = nomeOperador,
                    comissao = comissao,
                    comissaoTotal = comissaoTotal,
                    id = id,
                    marcaID = marcaID,
                    stUnitario = stUnitario
                )
                produtoExistente.listaProgressiva.add(progressiva)
            } else {
                val produto = Produtos(
                    Barra = barra,
                    Marca_ID = marcaID,
                    Nome = nomeProduto,
                    Imagem = imagemProduto,
                    lojaID = lojaID,
                    cnpj = cnpj,
                    quantidadeAdicionada = quantidadeAdicionada,
                    isCarrinho = isCarrinho,
                    idProgressiva = id,
                    valorMaximo =  loja.ValorMaximo
                )


                if (isCarrinho == 1 && isProgressiva == 0) {
                    produto.isCarrinhoOperdor = 1
                } else if (isCarrinho == 1 && isProgressiva == 1 ) {
                    produto.isCarrinhoOperdor = 0

                    if (carrinhoOperador != operadorLogisticoGrupoID) {
                        val daoCarrinho = DAOCarrinho()
                        produto.idProgressiva = id
                        daoCarrinho.uptadeCariinhoOperador(db, produto, carrinhoOperador, operadorLogisticoGrupoID, nomeOperador)
                    }
                }
                if(isProgressiva == 1){
                    val progressiva = Progressiva(
                        lojaID = lojaID,
                        barra = barra,
                        quantidadePedido = quantidadePedido,
                        valorUnitario = valorUnitario,
                        desconto = desconto,
                        valorUnitarioDesconto = valorUnitarioDesconto,
                        valorTotalDesconto = valorTotalDesconto,
                        lojaIDPortal = lojaIDPortal,
                        quantidadeMaxima = quantidadeMaxima,
                        uf = uf,
                        origem = origem,
                        arquivoPreco = arquivoPreco,
                        operadorLogisticoGrupoID = operadorLogisticoGrupoIDLeft,
                        imagem = imagemProduto,
                        nome = nomeOperador,
                        comissao = comissao,
                        comissaoTotal = comissaoTotal,
                        id = id,
                        marcaID = marcaID,
                        stUnitario = stUnitario
                    )
                    produto.listaProgressiva.add(progressiva)
                }
                if (isCarrinho == 1 || (isCarrinho == 0 && isProgressiva == 1)) {
                    listaLojas.add(1 to produto)
                } else {
                    continue
                }

            }
        }

        cursor.close()
    }

        fun filtraProdudos (db:SQLiteDatabase,listaFiltroAtribudos:ArrayList<Int>, operadorSelecionado:Int, buscaTotais:Boolean = false, lojaID: Int):ArrayList<String>{
         var atribudos = ""
         for ((position,atriboto) in listaFiltroAtribudos.withIndex()){
             if (position == listaFiltroAtribudos.size-1){
                 atribudos += atriboto.toString()
             }else{
                 atribudos += atriboto.toString() + ","
             }
         }
         var queryFiltro = ""
         if (buscaTotais){
             queryFiltro = "SELECT DISTINCT * FROM TB_PROGRESSIVA WHERE loja_id = ${lojaID} And operadorlogisticogrupoid = ${operadorSelecionado}\n"
         }else{
              queryFiltro = "SELECT DISTINCT filtroProdutos.Barra FROM TB_FILTRO_PODUTO filtroProdutos\n" +
                     "INNER JOIN TB_PROGRESSIVA Progressivas ON Progressivas.barra = filtroProdutos.Barra  AND Progressivas.operadorLogisticoGrupoID = ${operadorSelecionado} AND Progressivas.loja_ID = ${lojaID}\n"+
                     "WHERE atributo_id in (${atribudos})  "
         }

         val cursor = db.rawQuery(queryFiltro,null)
         val listaBarra = ArrayList<String>()
         while (cursor.moveToNext()){
             var barra = cursor.getString(0)
             listaBarra.add(barra)

         }
         cursor.close()
         return listaBarra
     }
    fun drop(db: SQLiteDatabase){
        db.execSQL("DROP TABLE IF EXISTS TB_PRODUTOS")
        val  sqlProduto =  "CREATE TABLE IF NOT EXISTS TB_PRODUTOS(" +
                "barra VARCHAR(20) PRIMARY KEY," +
                "marca_ID INTEGER," +
                "nome VARCHAR(500)," +
                "imagem Varchar(100)" +
                ")"
        db.execSQL(sqlProduto)
    }
}
