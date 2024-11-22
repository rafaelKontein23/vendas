package br.com.visaogrupo.tudofarmarep.Views.Activitys

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TaskImportarCarteira
import br.com.visaogrupo.tudofarmarep.Carga.ultis.Alertas
import br.com.visaogrupo.tudofarmarep.Carga.ultis.BaixaArquivo
import br.com.visaogrupo.tudofarmarep.Controlers.ControlerActImportacao
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.home.DialogSuccesoCarteiraImportada
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.ValidarTextos
import br.com.visaogrupo.tudofarmarep.Views.dialogs.DialogModeloImportacao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

class ActImportaCarteira : AppCompatActivity() {
    val REQUEST_CODE_PICK_FILE = 1
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    private  lateinit var  constrainCarregando:ConstraintLayout
    val controlerActImportacao = ControlerActImportacao()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_act_importa_carteira)
        val baixarModelo = findViewById<TextView>(R.id.baixarModelo)
        val contrainsVisualizaModelo = findViewById<ConstraintLayout>(R.id.contrainsVisualizaModelo)
        val improtarArquivo = findViewById<ConstraintLayout>(R.id.improtarArquivo)
        val voltarImportar = findViewById<ImageView>(R.id.voltarImportar)
        constrainCarregando = findViewById<ConstraintLayout>(R.id.constrainCarregando)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        voltarImportar.setOnClickListener {
            finish()
        }
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                processarArquivoSelecionado(uri)
            } else {
                Toast.makeText(applicationContext, "Nenhum arquivo selecionado", Toast.LENGTH_SHORT).show()
            }
        }
        improtarArquivo.setOnClickListener {
            abrirSeletorDeArquivos()

        }
        contrainsVisualizaModelo.setOnClickListener {
            MainScope().launch {
                val dialogModeloImportacao = DialogModeloImportacao()
                dialogModeloImportacao.dialogModeloImportacao(this@ActImportaCarteira, activity = this@ActImportaCarteira)
            }
        }
        baixarModelo.setOnClickListener {
                val url = "https://www.loiu.com.br/Docs/Excels/Modelo_Importacao_CNPJ.xlsx"
                val fileName = "Modelo_Importacao_CNPJ.xlsx"
               MainScope().launch {
                    Toast.makeText(this@ActImportaCarteira, "Baixando arquivo...", Toast.LENGTH_SHORT).show()
                    BaixaArquivo.downloadExcelFile(this@ActImportaCarteira, url, fileName)
                   Toast.makeText(this@ActImportaCarteira, "baixado com sucesso", Toast.LENGTH_SHORT).show()
               }

        }

    }

    fun abrirSeletorDeArquivos() {
        val mimeTypes = arrayOf(
            "text/plain",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "text/csv",
            "text/comma-separated-values", // Tipo MIME alternativo para CSV
            "application/csv", // Outro tipo MIME alternativo
            "application/excel", // Tipo MIME mais antigo para planilhas
            "application/vnd.oasis.opendocument.spreadsheet" // ODS (opcional)
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 ou superior
            filePickerLauncher.launch(mimeTypes)
        } else {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, REQUEST_CODE_PICK_FILE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.let { uri ->
                processarArquivoSelecionado(uri)
            } ?: Toast.makeText(applicationContext, "Nenhum arquivo selecionado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processarArquivoSelecionado(uri: Uri) {
        constrainCarregando.isVisible = true
        val fileName =controlerActImportacao.getFileName(uri, applicationContext)

        if (fileName!!.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            val  (listaCorreta, listaComErro) = controlerActImportacao.processarArquivoExcel(uri, applicationContext)

            if (listaCorreta.isEmpty() && listaComErro.isEmpty()){
                constrainCarregando.isVisible = false
                Toast.makeText(applicationContext, "Erro ao processar o arquivo Excel", Toast.LENGTH_SHORT).show()
            }else {
                CoroutineScope(Dispatchers.IO).launch {
                    val TaskImportarCarteira = TaskImportarCarteira()
                    val (listaImportados, listaErrosEncontrado, listaErrosSituacaoCadastral) = TaskImportarCarteira.importarCarteira(
                        listaCorreta,
                        applicationContext
                    )

                    if (listaImportados.isEmpty()) {
                        MainScope().launch {
                            constrainCarregando.isVisible = false
                            Alertas.alertaErro(
                                this@ActImportaCarteira,
                                "Nenhum CNPJ foi importado",
                                "Ops!"
                            ) {
                            }
                        }
                    } else {
                        MainScope().launch {
                            constrainCarregando.isVisible = false
                            val dialogSuccesoCarteiraImportada = DialogSuccesoCarteiraImportada()
                            dialogSuccesoCarteiraImportada.dialogSuccesoCarteiraImportada(
                                this@ActImportaCarteira,
                                listaCorreta,
                                listaErrosEncontrado,
                                listaErrosSituacaoCadastral,listaImportados.size
                            )
                        }
                    }
                }

            }


        } else if( fileName.endsWith(".csv") ||  fileName.endsWith(".txt")) {
            try {
                val inputStream = applicationContext.contentResolver.openInputStream(uri)
                val listaCnpj = mutableListOf<String>()
                val listaCnpjErro  = mutableListOf<String>()
                inputStream?.let {
                    val content = it.bufferedReader().use { reader -> reader.readText() }.trimIndent()
                    val cnpjList = content.split("\n").map { it.trim() }

                    for (cnpj in cnpjList) {
                        if (ValidarTextos.isCNPJ(cnpj)) {
                            listaCnpj.add(cnpj)
                        } else {
                            listaCnpjErro.add(cnpj)
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch{
                        val  TaskImportarCarteira = TaskImportarCarteira()
                        val (listaImportados, listaErrosEncontrado, listaErrosSituacaoCadastral)  = TaskImportarCarteira.importarCarteira(listaCnpj, applicationContext)
                        if (listaImportados.isEmpty()){
                            MainScope().launch {
                                constrainCarregando.isVisible = false
                                Alertas.alertaErro(this@ActImportaCarteira, "Nenhum CNPJ foi importado", "Ops!"){
                                }
                            }
                        }else{
                            MainScope().launch {
                                constrainCarregando.isVisible = false
                                val dialogSuccesoCarteiraImportada = DialogSuccesoCarteiraImportada()
                                dialogSuccesoCarteiraImportada.dialogSuccesoCarteiraImportada(this@ActImportaCarteira, listaCnpj, listaErrosEncontrado, listaErrosSituacaoCadastral, listaImportados.size)
                            }
                        }


                    }
                    println("Conteúdo do arquivo: $content")
                }
            } catch (e: Exception) {
                constrainCarregando.isVisible = false
                Toast.makeText(applicationContext, "Erro ao processar o arquivo", Toast.LENGTH_SHORT).show()
            }
        }else{
            constrainCarregando.isVisible = false
            Toast.makeText(applicationContext, "Formato de arquivo não suportado", Toast.LENGTH_SHORT).show()
        }
    }
}