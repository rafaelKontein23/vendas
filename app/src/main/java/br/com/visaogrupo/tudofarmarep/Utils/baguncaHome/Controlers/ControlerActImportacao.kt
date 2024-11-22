package br.com.visaogrupo.tudofarmarep.Controlers

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import br.com.visaogrupo.tudofarmarep.Utils.ValidarTextos
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory

import java.io.InputStream

class ControlerActImportacao {

    fun processarArquivoExcel(uri: Uri, context: Context):Pair<MutableList<String>, MutableList<String>> {
        val listaCnpj = mutableListOf<String>()
        val listaCnpjErro  = mutableListOf<String>()
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.let { stream ->

                val workbook = WorkbookFactory.create(stream)

                val sheet = workbook.getSheetAt(0)

                for (row in sheet) {
                    val rowData = StringBuilder()

                    for (cell in row) {
                        when (cell.cellType) {
                            CellType.STRING -> {
                                rowData.append(cell.stringCellValue)
                            }
                            CellType.NUMERIC -> {
                                val numericValue = cell.numericCellValue
                                val formattedValue = String.format("%.0f", numericValue)
                                rowData.append(formattedValue)
                            }
                            CellType.BOOLEAN -> {
                                rowData.append(cell.booleanCellValue)
                            }
                            CellType.FORMULA -> {
                                rowData.append(cell.cellFormula)
                            }
                            else -> {
                                rowData.append("N/A")
                            }
                        }
                    }
                    val iscnpj =  ValidarTextos.isCNPJ(rowData.toString())
                    if(iscnpj){
                        listaCnpj.add(rowData.toString())
                    }else{
                        listaCnpjErro.add(rowData.toString())
                    }
                    println("Linha: $rowData")
                }

                workbook.close()
                return Pair(listaCnpj, listaCnpjErro)
            }
        } catch (e: Exception) {

            return Pair(ArrayList(), ArrayList())
        }
        return Pair(ArrayList(), ArrayList())
    }
    public  fun getFileName(uri: Uri, context: Context): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(nameIndex)
            }
        }

        return fileName
    }
}