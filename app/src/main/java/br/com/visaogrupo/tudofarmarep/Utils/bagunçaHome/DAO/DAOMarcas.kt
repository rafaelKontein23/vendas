package br.com.visaogrupo.tudofarmarep.DAO

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.com.visaogrupo.tudofarmarep.Objetos.Marcas

class DAOMarcas (context: Context){

    fun inserir(db: SQLiteDatabase, marcas: Marcas){
        db.beginTransaction()
        try {
            val valuesMarcas = ContentValues()
            valuesMarcas.put("marca_ID", marcas.Marca_ID)
            valuesMarcas.put("nome", marcas.Nome)
            valuesMarcas.put("logoTipo", marcas.Logotipo)
            valuesMarcas.put("banco", marcas.Banco)
            valuesMarcas.put("url", marcas.URL)
            valuesMarcas.put("status", marcas.Status_Cod)

            db.insertOrThrow("TB_MARCAS", null, valuesMarcas)
            db.setTransactionSuccessful()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            db.endTransaction()
        }

    }

}