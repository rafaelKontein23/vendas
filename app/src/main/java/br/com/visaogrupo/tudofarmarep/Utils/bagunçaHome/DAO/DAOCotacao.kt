package br.com.visaogrupo.tudofarmarep.DAO

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import br.com.visaogrupo.tudofarmarep.Objetos.Cotacao
import br.com.visaogrupo.tudofarmarep.Objetos.CotacaoCarrinho

class DAOCotacao {
    fun insereCotacao(db: SQLiteDatabase, cotacao: Cotacao){
        try {
            var contentValue = ContentValues()
            val operador1 = cotacao.listaOperadoreslooping[0]
            val operador2 = cotacao.listaOperadoreslooping[1]
            val operador3 = cotacao.listaOperadoreslooping[2]
            val operador4 = cotacao.listaOperadoreslooping[3]
            val operador5 = cotacao.listaOperadoreslooping[4]
            contentValue.apply {
                put("Loja_ID", cotacao.lojaID)
                put("cnpj", cotacao.CNPJ)
                put("carrinhoID", cotacao.CarrinhoId)
                put("formaDePagamentoMarcaID", cotacao.formaPagamentoMarcasID)
                put("OperadorLogistico1", operador1)
                put("OperadorLogistico2", operador2)
                put("OperadorLogistico3", operador3)
                put("OperadorLogistico4", operador4)
                put("OperadorLogistico5", operador5)
                db.insertOrThrow("TB_CotacoesCarrinho", null, contentValue)

            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    fun selectCotacao(db:SQLiteDatabase,carrinhoId:Int):CotacaoCarrinho?{
        val query = "SELECT * FROM TB_CotacoesCarrinho WHERE carrinhoID = $carrinhoId"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            val cotacao = CotacaoCarrinho(
                formaDePagamentoMarcaID =  cursor.getInt(3),
                operadorLogistico1 = cursor.getInt(4),
                operadorLogistico2 = cursor.getInt(5),
                operadorLogistico3 = cursor.getInt(6),
                operadorLogistico4 = cursor.getInt(7),
                operadorLogistico5 = cursor.getInt(8)

            )
            return cotacao
        }
        return  null

    }
    fun deleteCotacao(db:SQLiteDatabase,carrinhoId: Int){
        val query = "DELETE FROM TB_CotacoesCarrinho WHERE carrinhoID = $carrinhoId"
        db.execSQL(query)
    }


}