package br.com.visaogrupo.tudofarmarep.DAO

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import br.com.visaogrupo.tudofarmarep.Objetos.FormaPagamento

class DAOFormaPagamento(){
    fun inserirFormaPagamento(db:SQLiteDatabase,formaPagamento: FormaPagamento){
        val valoresFormaPagamento = ContentValues()
        db.beginTransaction()
        try {
            valoresFormaPagamento.put("FormaPagamentoMarcas_ID",formaPagamento.formaPagamentoMarcas_ID)
            valoresFormaPagamento.put("Descricao",formaPagamento.Descricao)
            valoresFormaPagamento.put("Loja_ID",formaPagamento.lojaID)
            db.insertOrThrow("TB_FORMA_PAGAMENTO",null,valoresFormaPagamento)
            db.setTransactionSuccessful()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            db.endTransaction()
        }

    }
    fun buscarFormaPagamento(db:SQLiteDatabase, lojaID:Int):ArrayList<FormaPagamento>{
        val listaFormaPagamento = ArrayList<FormaPagamento>()
        val sql = "SELECT * FROM TB_FORMA_PAGAMENTO Where Loja_ID = ${lojaID}"
        val cursor = db.rawQuery(sql,null)
        while (cursor.moveToNext()){
            val formaDePagamentoMarcaID = cursor.getInt(cursor.getColumnIndex("FormaPagamentoMarcas_ID"))
            val descricao = cursor.getString(cursor.getColumnIndex("Descricao"))
            val formaPagamento = FormaPagamento(formaDePagamentoMarcaID,descricao,lojaID)
            listaFormaPagamento.add(formaPagamento)
        }
        cursor.close()

        return listaFormaPagamento
    }
}
