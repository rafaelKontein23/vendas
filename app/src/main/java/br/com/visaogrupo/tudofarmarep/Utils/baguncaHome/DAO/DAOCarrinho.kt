package br.com.visaogrupo.tudofarmarep.DAO

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoAbertos
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Objetos.Progressiva

class DAOCarrinho {
   suspend fun buscarItens(db: SQLiteDatabase, produto: Produtos, loja: Lojas, razaoSocial:String){
        if(produto.quantidadeAdicionada == 0){
            deletarTbCarrinho(db, produto)
        }else{
            val queryItens = "SELECT * FROM TB_CARRINHO WHERE barra = '${produto.Barra}' AND loja_id = ${produto.lojaID} AND cnpj = '${produto.cnpj}'"
            val cursor = db.rawQuery(queryItens, null)
            if(cursor.moveToNext()){
                uptadeCariinho(db, produto, loja)
            }else{
                inserirTbCarrinho(db, produto, loja, razaoSocial)
            }

        }

    }

    fun buscaCarrinhoAbertos(db: SQLiteDatabase):ArrayList<CarrinhoAbertos>{
        val queryItens = "SELECT Carrinho.CNPJ, Marcas.nome, Lojas.Nome as Loja, SUM(Carrinho.valorTotal) AS TotalCarrinho, Carrinho.razaoSocial, Marcas.logoTipo, Lojas.Loja_ID\n" +
                "FROM TB_CARRINHO Carrinho\n" +
                "INNER JOIN TB_MARCAS Marcas on Marcas.marca_ID = Carrinho.marca_ID\n" +
                "INNER JOIN TB_LOJAS Lojas on Lojas.Loja_ID = Carrinho.Loja_ID\n" +
                "GROUP BY Carrinho.CNPJ, Marcas.nome, Lojas.Nome"
        val cursor = db.rawQuery(queryItens, null)
        val listaCarrinho = ArrayList<CarrinhoAbertos>()
        while (cursor.moveToNext()){
            val cnpj = cursor.getString(0)
            val marca = cursor.getString(1)
            val loja = cursor.getString(2)
            val valorTotal = cursor.getDouble(3)
            val razaoSocial = cursor.getString(4)
            val logoTipo = cursor.getString(5)
            val lojaId = cursor.getInt(6)
            val carrinhoAbertosr = CarrinhoAbertos(cnpj, marca, loja, valorTotal, razaoSocial, logoTipo, lojaId)
            listaCarrinho.add(carrinhoAbertosr)
        }
        return listaCarrinho
    }
    fun verificaCarrinho(db: SQLiteDatabase, lojaId:Int, cnpj: String, loja: Lojas):Boolean{
        val queryItens = "SELECT * FROM TB_CARRINHO WHERE loja_id = ${lojaId} AND cnpj = '${cnpj}' AND operadorID = ${loja.operadorIDSelecionado}"
        val cursor = db.rawQuery(queryItens, null)
        if(cursor.moveToNext()){
            return true
        }else{
            return false
        }
    }
    fun buscarItensCarrinho(db: SQLiteDatabase,loja: Lojas, cnpj: String):ArrayList<Produtos>{
        val queryItens = "SELECT Carrinho.barra, Carrinho.marca_ID, Produtos.nome, Produtos.imagem,Carrinho.Loja_ID, Carrinho.quantidade_adicionada, Carrinho.valorTotal, Carrinho.cnpj FROM TB_CARRINHO Carrinho\n" +
                "LEFT JOIN TB_PRODUTOS Produtos ON Produtos.barra = Carrinho.barra\n" +
                "WHERE  Carrinho.Loja_ID = ${loja.Loja_ID} AND Carrinho.cnpj = '${cnpj}' AND Carrinho.operadorID = ${loja.operadorIDSelecionado}"

        val cursor = db.rawQuery(queryItens, null)
        val listaProdutos = ArrayList<Produtos>()
        while(cursor.moveToNext()){
            val barra = cursor.getString(0)
            val marcaId = cursor.getInt(1)
            val nome = cursor.getString(2)
            val imagem = cursor.getString(3)
            val lojaId = cursor.getInt(4)
            val quantidadeAdicionada = cursor.getInt(5)
            val valorTotal = cursor.getDouble(6)
            val cnpj = cursor.getString(7)
            val produto = Produtos(barra, marcaId, nome, imagem, lojaId, cnpj = cnpj, quantidadeAdicionada = quantidadeAdicionada, valorProdutoTotal = valorTotal)
            val DaoProgressiva = DAOProgressiva()
            val listaProgressiva = DaoProgressiva.buscaProgressiva(db, produto,loja.operadorIDSelecionado)

            for(progressiva in listaProgressiva){
                if (produto.quantidadeAdicionada >= progressiva.quantidadePedido){
                    val resultadoComissao = (progressiva.valorUnitarioDesconto * progressiva.comissao) / 100.0
                    val valorTotalComissao = quantidadeAdicionada* resultadoComissao
                    progressiva.comissaoTotal = valorTotalComissao
                    produto.progressiva = progressiva
                }
            }
            listaProdutos.add(produto)
        }
        return listaProdutos
    }
    fun buscaValorTotal(db: SQLiteDatabase, loja: Lojas, cnpj: String):Pair<Double,Double>{
        var valorTotal = 0.0
        var valorTotalSt = 0.0
        db.beginTransaction()
        val queryValorTotal = "SELECT SUM(valortotal) AS valorTotaloja, SUM (TotalSt) AS valorTotalSt FROM TB_CARRINHO  WHERE" +
                " loja_id = ${loja.Loja_ID} AND cnpj = '${cnpj}' AND  operadorID = ${loja.operadorIDSelecionado}"
        val cursor = db.rawQuery(queryValorTotal, null)
        if(cursor.moveToNext()){
            valorTotal = cursor.getDouble(0)
            valorTotalSt =cursor.getDouble(1)?: 0.0
            db.endTransaction()
            return Pair(valorTotal, valorTotalSt)
        }else{
            db.endTransaction()
            return Pair(valorTotal, valorTotalSt)
        }
    }


    suspend  fun conferiCarrinhoLoja(lojaId: Int, cnpj: String, db: SQLiteDatabase):Boolean{
        val queryItens = "SELECT * FROM TB_CARRINHO WHERE loja_id = ${lojaId} AND cnpj = '${cnpj}'"
        val cursor = db.rawQuery(queryItens, null)
        db.beginTransaction()
        if(cursor.moveToNext()){
            db.endTransaction()
            return true
        }else{
            db.endTransaction()
            return false
        }
        cursor.close()
    }
    suspend fun  deletaCarrinhoEspecifico(db: SQLiteDatabase,loja: Int, cnpj: String){
        try {
            db.beginTransaction()
            val queryItens = "DELETE FROM TB_CARRINHO WHERE Loja_ID = $loja AND cnpj = '$cnpj'"
            db.execSQL(queryItens)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }

    }

    private  fun inserirTbCarrinho(db: SQLiteDatabase, produto: Produtos, loja: Lojas, razaoSocial: String){
        db.beginTransaction()
        try {
            val valorcarrinho = ContentValues()
            valorcarrinho.put("barra", produto.Barra)
            valorcarrinho.put("marca_ID", produto.Marca_ID)
            valorcarrinho.put("Loja_ID", produto.lojaID)
            valorcarrinho.put("operadorID", loja.operadorIDSelecionado)
            valorcarrinho.put("quantidade_adicionada", produto.quantidadeAdicionada)
            valorcarrinho.put("valorTotal", produto.valorProdutoTotal)
            valorcarrinho.put("cnpj", produto.cnpj)
            valorcarrinho.put("idProgressiva", produto.idProgressiva)
            valorcarrinho.put("nomeOperador", loja.nomeOperadorSelecionado)
            valorcarrinho.put("TotalSt", produto.progressiva!!.stUnitario * produto.quantidadeAdicionada)
            valorcarrinho.put("razaoSocial", razaoSocial)

            db.insertOrThrow("TB_CARRINHO", null, valorcarrinho)
            db.setTransactionSuccessful()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            db.endTransaction()
        }
    }

    fun deletarTbCarrinho(db: SQLiteDatabase, produto: Produtos){
        val sqlQuery = "DELETE FROM TB_CARRINHO WHERE barra = '${produto.Barra}' AND loja_id = ${produto.lojaID} AND cnpj = '${produto.cnpj}'"
        db.execSQL(sqlQuery)

    }

    fun uptadeCariinho(db: SQLiteDatabase, produto: Produtos,loja: Lojas){
        db.beginTransaction()
        try {

            val conteudos=  ContentValues()
            val whereClause = "barra = '${produto.Barra}' AND loja_id = ${produto.lojaID} AND cnpj = '${produto.cnpj}'"
            conteudos.put("quantidade_adicionada",produto.quantidadeAdicionada)
            conteudos.put("valorTotal",produto.valorProdutoTotal)
            conteudos.put("operadorID",loja.operadorIDSelecionado)
            conteudos.put("idProgressiva", produto.idProgressiva)
            conteudos.put("nomeOperador",loja.nomeOperadorSelecionado)
            conteudos.put("TotalSt", produto.progressiva!!.stUnitario * produto.quantidadeAdicionada)
            db.update("TB_CARRINHO",conteudos,
                whereClause,null)
            db.setTransactionSuccessful()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            db.endTransaction()
        }
    }

    fun uptadeCariinhoOperador(db: SQLiteDatabase, produto: Produtos,operadorIdAntigo:Int,operadorIDnovo:Int,operadorNome:String){
        db.beginTransaction()
        try {
            val query = "SELECT (valorunitariodesconto)  FROM TB_PROGRESSIVA Progressiva \n" +
                    "WHERE Progressiva.barra = '${produto.Barra}' and Progressiva.operadorLogisticoGrupoID = ${operadorIDnovo} AND loja_id = ${produto.lojaID} AND ${produto.quantidadeAdicionada}  >= Progressiva.quantidadePedido \n" +
                    "ORDER by Progressiva.quantidadePedido "
            val cursor = db.rawQuery(query, null)

            if(cursor.moveToNext()){
                val  valorUnitarioDesconto = cursor.getDouble(0)
                val  valorTotalNovo = valorUnitarioDesconto * produto.quantidadeAdicionada
                produto.valorProdutoTotal = valorTotalNovo
            }
            val conteudos=  ContentValues()
            val whereClause = "barra = '${produto.Barra}' AND loja_id = ${produto.lojaID} AND cnpj = '${produto.cnpj}' AND operadorID = ${operadorIdAntigo}"

            conteudos.put("quantidade_adicionada",produto.quantidadeAdicionada)
            conteudos.put("valorTotal",produto.valorProdutoTotal)
            conteudos.put("operadorID",operadorIDnovo)
            conteudos.put("nomeOperador",operadorNome)
            conteudos.put("idProgressiva", produto.idProgressiva)
            db.update("TB_CARRINHO",conteudos,
                whereClause,null)
            db.setTransactionSuccessful()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            db.endTransaction()
        }
    }
}