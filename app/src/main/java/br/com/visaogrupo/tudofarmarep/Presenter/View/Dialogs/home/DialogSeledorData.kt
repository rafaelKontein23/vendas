package br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import br.com.visaogrupo.tudofarmarep.Carga.interfaces.AtualizaMesResumo
import br.com.visaogrupo.tudofarmarep.R
import java.util.Calendar

class DialogSeledorData (){
    fun SeletorData(context: Context,atualizaMesResumo: AtualizaMesResumo){
        val  dialog =  Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_seletor_data);

        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.setGravity(Gravity.CENTER);

        val fecharCalender = dialog.findViewById<ImageView>(R.id.fecharCalender)
        val numberPickerMonth = dialog.findViewById<NumberPicker>(R.id.numberPickerMonth)
        val numberPickerYear = dialog.findViewById<NumberPicker>(R.id.numberPickerYear)
        val selecionar = dialog.findViewById<TextView>(R.id.selecionar)
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) // 0-indexed (Jan is 0)
        val currentYear = calendar.get(Calendar.YEAR)
        setupNumberPickers(numberPickerMonth, numberPickerYear, currentMonth, currentYear)


        fecharCalender.setOnClickListener {
            dialog.dismiss()
        }

        selecionar.setOnClickListener {

            val selectedMonth = numberPickerMonth.value +1
            val selectedYear = numberPickerYear.displayedValues[numberPickerYear.value].toInt()

            val formattedMonth = if (selectedMonth < 10) "0$selectedMonth" else "$selectedMonth"
            val monthNames = arrayOf(
                "janeiro", "fevereiro", "março", "abril", "maio", "junho",
                "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"
            )
            val data = "$selectedYear-$formattedMonth-01"
            val selectedMonthExtenso = numberPickerMonth.value

            atualizaMesResumo.atualizaMesResumo(data,monthNames[selectedMonthExtenso], selectedYear.toString() )
            dialog.dismiss()

        }
    }
    private fun setupNumberPickers(monthPicker: NumberPicker, yearPicker: NumberPicker, currentMonth: Int, currentYear: Int) {
        val months = arrayOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")
        monthPicker.minValue = 0
        monthPicker.maxValue = months.size - 1
        monthPicker.displayedValues = months

        val years = Array(currentYear - 2022 + 1) { i -> (2022 + i).toString() }
        yearPicker.minValue = 0
        yearPicker.maxValue = years.size - 1
        yearPicker.displayedValues = years

        yearPicker.value = years.indexOf(currentYear.toString())
        monthPicker.value = currentMonth

        yearPicker.setOnValueChangedListener { _, _, newValue ->
            val selectedYear = years[newValue].toInt()
            if (selectedYear == currentYear) {
                monthPicker.maxValue = currentMonth
            } else {
                monthPicker.maxValue = months.size - 1
            }
        }
    }

}