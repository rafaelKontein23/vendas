package br.com.visaogrupo.tudofarmarep.Utils.Views

import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.Calendar

class Alertas {

    companion object{
        private var activeDialog: AlertDialog? = null

        fun alertaErro(context: Context, mensagem: String, titulo: String, nao:String = "", sim:String = "Ok", function: () -> Unit){
            if (activeDialog?.isShowing == true) {
                return
            }

            val builder = AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setCancelable(false)
                .setNegativeButton(nao) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(sim) { dialog, _ ->
                    function()
                    dialog.dismiss()
                }

            activeDialog = builder.create()
            activeDialog?.show()

            activeDialog?.setOnDismissListener {
                activeDialog = null
            }
        }
        fun alertaAviso(context: Context, mensagem: String, nao:String = "", sim:String = "Ok", function: () -> Unit){
            if (activeDialog?.isShowing == true) {
                return
            }

            val builder = AlertDialog.Builder(context)
                .setTitle("Loiu informa")
                .setMessage(mensagem)
                .setCancelable(false)
                .setNegativeButton(nao) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(sim) { dialog, _ ->
                    function()
                    dialog.dismiss()
                }

            activeDialog = builder.create()
            activeDialog?.show()

            activeDialog?.setOnDismissListener {
                activeDialog = null
            }
        }
         fun showDatePickerDialog(textView: EditText, context: Context) {
             val calendar = Calendar.getInstance()

             val currentYear = calendar.get(Calendar.YEAR)
             val currentMonth = calendar.get(Calendar.MONTH)
             val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

             calendar.set(Calendar.YEAR, currentYear - 18)
             val initialYear = calendar.get(Calendar.YEAR)
             val initialMonth = calendar.get(Calendar.MONTH)
             val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

             val datePickerDialog = DatePickerDialog(
                 context,
                 { _, selectedYear, selectedMonth, selectedDay ->
                     val dia = if (selectedDay < 10) "0$selectedDay" else selectedDay.toString()
                     val mes = if (selectedMonth < 10) "0${selectedMonth + 1}" else (selectedMonth + 1).toString()
                     val formattedDate = "$dia/$mes/$selectedYear"
                     textView.text = Editable.Factory.getInstance().newEditable(formattedDate)
                     textView.validaError(false, context)
                 },
                 initialYear,
                 initialMonth,
                 initialDay
             )

             datePickerDialog.datePicker.maxDate = calendar.timeInMillis

             datePickerDialog.show()

         }
    }
}