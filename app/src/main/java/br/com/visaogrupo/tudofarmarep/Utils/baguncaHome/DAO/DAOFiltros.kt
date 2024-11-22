package br.com.visaogrupo.tudofarmarep.DAO

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.com.visaogrupo.tudofarmarep.Objetos.FiltroProduto

class DAOFiltros(context: Context) {

    fun inserir(db: SQLiteDatabase, filtro: FiltroProduto) {
        db.beginTransaction()
        try {
            val valorFiltroProduto = ContentValues()
            valorFiltroProduto.put("Categoria_id", filtro.Categoria_id)
            valorFiltroProduto.put("Atributo_id", filtro.Atributo_id)
            valorFiltroProduto.put("Barra", filtro.Barra)

            db.insertOrThrow("TB_FILTRO_PODUTO", null, valorFiltroProduto)
            db.setTransactionSuccessful()

        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            db.endTransaction()
        }
    }

}