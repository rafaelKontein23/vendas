package br.com.visaogrupo.tudofarmarep.DAO

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.com.visaogrupo.tudofarmarep.Objetos.Operadores
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Objetos.Progressiva

class DAOProgressiva {

    fun inserir(db:SQLiteDatabase,progressiva: Progressiva){
        db.beginTransaction()
        try {
            val valuesProgressiva = ContentValues().apply {
                put("loja_ID", progressiva.lojaID)
                put("barra", progressiva.barra)
                put("quantidadePedido", progressiva.quantidadePedido)
                put("valorUnitario", progressiva.valorUnitario)
                put("desconto", progressiva.desconto)
                put("valorUnitarioDesconto", progressiva.valorUnitarioDesconto)
                put("valorTotalDesconto", progressiva.valorTotalDesconto)
                put("marca_ID", progressiva.marcaID)
                put("loja_ID_Portal", progressiva.lojaIDPortal)
                put("quantidadeMaxima", progressiva.quantidadeMaxima)
                put("uf", progressiva.uf)
                put("origem", progressiva.origem)
                put("arquivoPreco", progressiva.arquivoPreco)
                put("operadorLogisticoGrupoID", progressiva.operadorLogisticoGrupoID)
                put("imagem", progressiva.imagem)
                put("nome", progressiva.nome)
                put("comissao", progressiva.comissao)
                put("comissaoTotal", progressiva.comissaoTotal)
                put("stUnitario", progressiva.stUnitario)
            }

            db.insertOrThrow("TB_PROGRESSIVA", null, valuesProgressiva)
            db.setTransactionSuccessful()

        }catch (e:Exception){
            e.printStackTrace()

        }finally {
            db.endTransaction()
        }



    }
    fun buscaProgressiva(db:SQLiteDatabase,produto:Produtos, operadorID:Int):ArrayList<Progressiva>{
        val listaProgressiva = ArrayList<Progressiva>()
        try {
            val queryProgressiva = "SELECT * FROM TB_PROGRESSIVA WHERE TB_PROGRESSIVA.barra = " +
                    "'${produto.Barra}' and  TB_PROGRESSIVA.loja_ID = ${produto.lojaID} " +
                    "and TB_PROGRESSIVA.marca_ID = ${produto.Marca_ID} and TB_PROGRESSIVA.operadorLogisticoGrupoID = $operadorID"

            val cursor = db.rawQuery(queryProgressiva,null)

            while (cursor.moveToNext()){
                val lojaID = cursor.getInt(0)
                val barra = cursor.getString(1)
                val quantidadePedido = cursor.getInt(2)
                val valorUnitario = cursor.getDouble(3)
                val desconto = cursor.getDouble(4)
                val valorUnitarioDesconto = cursor.getDouble(5)
                val valorTotalDesconto = cursor.getDouble(6)
                val marcaID = cursor.getInt(7)
                val lojaIDPortal = cursor.getInt(8)
                val quantidadeMaxima = cursor.getInt(9)
                val uf = cursor.getString(10)
                val origem = cursor.getString(11)
                val arquivoPreco = cursor.getString(12)
                val operadorLogisticoGrupoID = cursor.getInt(13)
                val imagem = cursor.getString(14)
                val nome = cursor.getString(15)
                val comissao = cursor.getDouble(16)
                val comissaoTotal = cursor.getDouble(17)
                val  stUnitario = cursor.getDouble(18)
                val id = cursor.getInt(19)
                val progressiva = Progressiva(lojaID, barra, quantidadePedido, valorUnitario, desconto, valorUnitarioDesconto,
                    valorTotalDesconto, marcaID, lojaIDPortal, quantidadeMaxima, uf, origem, arquivoPreco, operadorLogisticoGrupoID, imagem, nome, comissao, id,comissaoTotal, stUnitario = stUnitario)
                listaProgressiva.add(progressiva)
            }
            cursor.close()
        }catch (e:Exception){
            e.printStackTrace()
        }

        return listaProgressiva
    }
    fun buscaopls(lojaId:Int, context: Context):ArrayList<Operadores>{
        val daoHellper = DAOHelper(context)

        val queryOperadores = "SELECT DISTINCT nome, imagem, operadorlogisticogrupoid FROM TB_PROGRESSIVA WHERE loja_id = ${lojaId}"
        val  cursor = daoHellper.writableDatabase.rawQuery(queryOperadores, null)
        val listaOperadores = ArrayList<Operadores>()
        while (cursor.moveToNext()){
            val operador = cursor.getString(0)
            val imagem = cursor.getString(1)
            val operadorID = cursor.getInt(2)
            listaOperadores.add(Operadores(operador, imagem, operadorID))
        }
        cursor.close()

        return listaOperadores
    }

}