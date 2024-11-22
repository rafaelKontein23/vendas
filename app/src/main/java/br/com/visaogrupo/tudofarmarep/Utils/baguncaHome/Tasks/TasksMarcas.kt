package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Objetos.Marcas
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitCarga
import okio.IOException

class TasksMarcas {

    fun buscaMarcas(context: Context):List<Marcas>{
        try {

            var requestMarcas = RetrofitCarga().createService(Isync::class.java)
            val  request  = requestMarcas.getMarcas()
            val response=  request.execute()
            var list :List<Marcas> = emptyList()

            if(response.isSuccessful){
                list = response.body()?.Marcas!!
                Log.d("teste",list.toString())
            }else{
                AlertDialog.Builder(context).setTitle("Atenção")
                    .setMessage("não foi possivel carregar as marcas")
                    .setPositiveButton("OK",null)
                    .create().show()
            }

            return list;
        }catch (e:Exception){
           e.printStackTrace()
            return emptyList()

        }catch (e:IOException){
            e.printStackTrace()
            return emptyList()
        }
    }


}