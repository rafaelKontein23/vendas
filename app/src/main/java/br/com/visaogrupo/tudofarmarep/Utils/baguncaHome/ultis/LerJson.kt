package br.com.visaogrupo.tudofarmarep.Carga.ultis

import org.json.JSONObject
import java.util.zip.ZipFile

class LerJson {
        fun readTextFileFromZip(zipFilePath: String, fileName: String): String? {
            val zipFile = ZipFile(zipFilePath)
            val entry = zipFile.getEntry(fileName)
            if (entry == null || entry.isDirectory) {
                // O arquivo não existe ou é um diretório
                return null
            }
            val inputStream = zipFile.getInputStream(entry)
            val json = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
            zipFile.close()


            return json
        }

    fun readAllJsonFilesFromZip(zipFilePath: String): MutableList<JSONObject> {
        val zipFile = ZipFile(zipFilePath)
        val jsonContents = mutableListOf<JSONObject>()

        zipFile.use { zip ->
            val entries = zip.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (!entry.isDirectory && entry.name.endsWith(".json")) {
                    val inputStream = zip.getInputStream(entry)
                    val content = inputStream.bufferedReader().use { it.readText() }
                    inputStream.close()
                    val jSONObject = JSONObject(content)
                    jsonContents.add(jSONObject)
                }
            }
        }

        return jsonContents
    }
}