package br.com.visaogrupo.tudofarmarep.Utils

import android.content.Context
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

class ImagensUltis {

    companion object{
        fun uriToBase64WithResolver(context: Context, uri: Uri): String? {
            return try {
                val inputStream = getInputStreamFromUri(context, uri)
                inputStream?.use { stream ->
                    val buffer = ByteArrayOutputStream()
                    val data = ByteArray(1024)
                    var nRead: Int

                    while (stream.read(data).also { nRead = it } != -1) {
                        buffer.write(data, 0, nRead)
                    }

                    Base64.encodeToString(buffer.toByteArray(), Base64.NO_WRAP)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun getInputStreamFromUri(context: Context, uri: Uri): InputStream? {
            return try {
                context.contentResolver.openInputStream(uri)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        fun copyUriToInternalStorage(context: Context, uri: Uri): File? {
            return try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return null
                val file = File(context.cacheDir, "temp_image")
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                file
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        fun fileToBase64(file: File): String? {
            return try {
                val bytes = file.readBytes()
                Base64.encodeToString(bytes, Base64.NO_WRAP)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


}