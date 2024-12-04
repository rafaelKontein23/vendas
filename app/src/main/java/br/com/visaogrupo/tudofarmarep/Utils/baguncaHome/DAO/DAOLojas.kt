package br.com.visaogrupo.tudofarmarep.DAO

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Objetos.CarrinhoItemCotacao
import br.com.visaogrupo.tudofarmarep.Objetos.LojaComparador
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DAOLojas (context: Context) {

    fun deletar(db: SQLiteDatabase) {
        db.beginTransaction()
        try {
            Log.d("DatabaseOperation", "Starting deletion of tables")

            // Execute DELETE commands
            db.execSQL("DELETE FROM TB_PRODUTOS")
            db.execSQL("DELETE FROM TB_MARCAS")
            db.execSQL("DELETE FROM TB_FILTRO_PODUTO")
            db.execSQL("DELETE FROM TB_LOJAS")
            db.execSQL("DELETE FROM TB_PROGRESSIVA")
            db.execSQL("DELETE FROM TB_FORMA_PAGAMENTO")

            // Commit the transaction
            db.setTransactionSuccessful()
            Log.d("DatabaseOperation", "Tables cleared successfully")

        } catch (e: Exception) {
            Log.e("DatabaseOperation", "Error during deletion: ${e.message}")
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }

    }

    fun inserir (db: SQLiteDatabase, loja:Lojas){
        db.beginTransaction()
        try {
            val valoresLojas = ContentValues()
            valoresLojas.put("Loja_ID", loja.Loja_ID)
            valoresLojas.put("Nome", loja.Nome)
            valoresLojas.put("LojaTipo_ID", loja.LojaTipo_ID)
            valoresLojas.put("ValorMinimo", loja.ValorMinimo)
            valoresLojas.put("ValorMaximo", loja.ValorMaximo)
            valoresLojas.put("QtdeMinOperador", loja.QtdeMinOperador)
            valoresLojas.put("QtdeMaxOperador", loja.QtdeMaxOperador)
            valoresLojas.put("QtdeMaxVendas", loja.QtdeMaxVendas)
            valoresLojas.put("Loja_id_Portal", loja.Loja_id_Portal)
            valoresLojas.put("MarcaID", loja.marcaID)
            db.insertOrThrow("TB_LOJAS", null, valoresLojas)
            db.setTransactionSuccessful()
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            db.endTransaction()

        }

    }


    fun select(
        db: SQLiteDatabase,
        listaFiltro: ArrayList<Lojas>,
        listaPrimeiraFiltros: ArrayList<Pair<Int, Any>>,
        cnpj: String,
        listaComparador: ArrayList<LojaComparador>, operadorCotacao: Int =0, listaCarrinho: ArrayList<CarrinhoItemCotacao>, nomeOperador: String
    ): ArrayList<Pair<Int, Any>> {
        val queryLojas = """
            SELECT DISTINCT loja.*, marcas.logoTipo, OperadorProgressiva.*
            FROM TB_LOJAS AS loja 
            INNER JOIN TB_PRODUTOS Produtos ON loja.MarcaID = Produtos.marca_ID
            LEFT JOIN TB_MARCAS marcas ON marcas.marca_ID = loja.MarcaID
            LEFT JOIN (
                        SELECT DISTINCT 
                                Progressiva.loja_id
                                , IFNULL(MAX(Carrinho.operadorID), MIN(operadorlogisticogrupoid)) AS operadorlogisticogrupoid
                                , IFNULL(max(Carrinho.nomeOperador), MIN(Progressiva.nome)) AS nome
                        FROM TB_PROGRESSIVA Progressiva
                        LEFT JOIN TB_CARRINHO Carrinho on Progressiva.loja_ID = Carrinho.Loja_ID
                                                        and Progressiva.marca_ID = Carrinho.marca_ID
                                                        and Progressiva.operadorLogisticoGrupoID = Carrinho.operadorID
                                                        and Carrinho.cnpj = '${cnpj}'
                        GROUP BY Progressiva.loja_id
            ) AS OperadorProgressiva ON OperadorProgressiva.loja_id = loja.Loja_ID
            WHERE loja.LojaTipo_ID IN (8,6,4,17)
            order by loja.Nome DESC
    """

        val listLojas = ArrayList<Pair<Int, Any>>()

        db.beginTransaction()
        val cursor = db.rawQuery(queryLojas, null)
        try {
            //var  lojaCarrinho = false
            while (cursor.moveToNext()) {
                val temNaLista = listaComparador.find { it.lojaID == cursor.getInt(0) }
                if (temNaLista == null) continue
                var operadorSelecionado = 0
                var nomeOperadorOl = ""
                var carrinhoID = 0
                if (!listaCarrinho.isEmpty()){
                     operadorSelecionado = if(operadorCotacao != 0 && cursor.getInt(0) == listaCarrinho.first().lojaId) operadorCotacao  else cursor.getInt(12)
                     nomeOperadorOl = if(operadorCotacao != 0 && cursor.getInt(0) == listaCarrinho.first().lojaId) nomeOperador  else cursor.getString(13) ?: ""
                    carrinhoID = listaCarrinho.first().carrinhoId

                }else{
                     operadorSelecionado = cursor.getInt(12)
                     nomeOperadorOl  = cursor.getString(13) ?: ""
                }



                val loja = Lojas(
                    cursor.getInt(2), cursor.getInt(0), cursor.getInt(8),
                    cursor.getString(1), cursor.getInt(6), cursor.getInt(7),
                    cursor.getInt(5), cursor.getDouble(3),cursor.getDouble(4),
                    cursor.getInt(9), imagem = cursor.getString(10),
                    operadorIDSelecionado = operadorSelecionado,
                    nomeOperadorSelecionado = nomeOperadorOl,
                    isFiltro = temNaLista.filtro,
                    urlLogin =  temNaLista.urlLoginPortal,
                    carrinhoID = carrinhoID
                )


                listLojas.add(0 to loja)

                val daoProdutos = DAOProdutos()
                daoProdutos.select(db, listLojas, loja, operadorSelecionado, cnpj)

                val daoCarrinho = DAOCarrinho()
                val (valorTotal, valorTotalSt) = daoCarrinho.buscaValorTotal(db, loja, cnpj)
                loja.valorTotal = valorTotal
                loja.totalSt = valorTotalSt

                listaFiltro.add(loja)
                listaPrimeiraFiltros.add(0 to loja)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor.close()
            db.endTransaction()
        }

        return listLojas
    }


}