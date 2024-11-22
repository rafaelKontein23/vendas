package br.com.visaogrupo.tudofarmarep.Carga.Tasks

import android.content.Context
import android.util.Log
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.Isync
import br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.RetrofitCliente
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class TaskZip {


    suspend fun buscaZipProdutos(context: Context):String {
        try {
            try {
                val downloadService = RetrofitCliente().createService(Isync::class.java)
                val request = downloadService.downloadFile("Cargas/Padrao/Produtos.zip")
                val response = request.execute()  // Fazendo a chamada de forma síncrona
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        val path = saveToDisk(body, context)
                       return path
                    }
                } else {
                    Log.e("Download", "Failed to download file: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }catch (e:IOException){
                e.printStackTrace()
                return ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

      fun saveToDisk(body: ResponseBody, context: Context):String {
        var path = ""
        try {
            File("/data/data/" + context.packageName + "/carga").mkdir() // Para criar a pasta!
            var destinationFile = File("/data/data/" + context.packageName + "/carga/cargadiaria.zip")
            path= destinationFile.path


            var `is`: InputStream? = null
            var os: OutputStream? = null
            try {
                Log.d("sa", "File Size=" + body.contentLength())
                `is` = body.byteStream()
                os = FileOutputStream(destinationFile)
                val data = ByteArray(4096)
                var count: Int
                var progress = 0
                while (`is`.read(data).also { count = it } != -1) {
                    os.write(data, 0, count)
                    progress += count
                    Log.d(
                        "Salavndo zip...",
                        "Progress: " + progress + "/" + body.contentLength() + " >>>> " + progress.toFloat() / body.contentLength()
                    )
                }
                os.flush()
                Log.d("Task_CargaDiaria", "Zip Salvo com sucesso")
            } catch (e: IOException) {
                e.printStackTrace()

                return ""
            } finally {

                    if (`is` != null) `is`.close()
                    if (os != null) os.close()
            }

        } catch (e: IOException) {
            e.printStackTrace()

            Log.d("Task_CargaDiaria", "Failed to save the file!")
            return ""
        }
        return path
    }

}