package br.com.visaogrupo.tudofarmarep.Utils.Views

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.Calendar

class Alertas {
    companion object{
        fun alertaErro(context: Context, mensagem: String, titulo: String, nao:String = "", sim:String = "Ok", function: () -> Unit){
            val builder = AlertDialog.Builder(context)
            builder.setTitle(titulo)
            builder.setMessage(mensagem)
            builder.setCancelable(false)
            builder.setNegativeButton(nao){ dialog, which ->
                dialog.dismiss()
            }
            builder.setPositiveButton(sim) { dialog, which ->
                function()
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
         fun showDatePickerDialog(textView: TextView, context: Context) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    textView.text = formattedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
}