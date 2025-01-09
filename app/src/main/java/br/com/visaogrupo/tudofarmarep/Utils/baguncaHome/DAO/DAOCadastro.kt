package br.com.visaogrupo.tudofarmarep.Utils.baguncaHome.DAO

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import br.com.visaogrupo.tudofarmarep.Objetos.Lojas
import br.com.visaogrupo.tudofarmarep.Objetos.Produtos
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequest
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.CadastroRequestAreaAtuacal
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.Cidade
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.Mesorregiao

class DAOCadastro {
    fun inserirCadastro(db: SQLiteDatabase, cadastro: CadastroRequest, fotoDocumento:Uri = Uri.EMPTY) {
        db.delete("Cadastro", null, null)
        db.delete("ImagemCadastro", null, null)

        val values = ContentValues().apply {
            put("possuiCoreText", cadastro.possuiCoreText)
            put("CNPJ", cadastro.CNPJ)
            put("RazaoSocial", cadastro.RazaoSocial)
            put("Fantasia", cadastro.Fantasia)
            put("CEP", cadastro.CEP)
            put("Endereco", cadastro.Endereco)
            put("Bairro", cadastro.Bairro)
            put("Cidade", cadastro.Cidade)
            put("UF", cadastro.UF)
            put("nome", cadastro.nome)
            put("sobrenome", cadastro.sobrenome)
            put("cpf", cadastro.cpf)
            put("dataNascimento", cadastro.dataNascimento)
            put("celular", cadastro.celular)
            put("telefoneComercial", cadastro.telefoneComercial)
            put("email", cadastro.email)
            put("hashIndicacao", cadastro.hashIndicacao)
            put("isPoliticaPrivacidade", if (cadastro.isPoliticaPrivacidade) 1 else 0)
            put("isTermoPolitica", if (cadastro.isTermoPolitica) 1 else 0)
            put("isAssinaContrato", if (cadastro.isAssinaContrato) 1 else 0)
            put("DeviceToken", cadastro.DeviceToken)
            put("FotoPerfil", cadastro.FotoPerfil)
            put("UDID", cadastro.UDID)
            put("VersaoaAPP", cadastro.VersaoaAPP)
            put("Dispositivo", cadastro.Dispositivo)
            put("SistemaOperacional", cadastro.SistemaOperacional)
            put("Plataforma", cadastro.Plataforma)
            put("FotoDocumento", cadastro.FotoDocumento)
            put("ImagemAssinatura", cadastro.ImagemAssinatura)
        }
        if (fotoDocumento != Uri.EMPTY ) {
            val imagemValues = ContentValues().apply {
                put("uriImagemDocumento", fotoDocumento.toString())
            }
            db.insert("ImagemCadastro", null, imagemValues)
        }
        db.insert("Cadastro", null, values)

    }
    fun recuperarCadastro(db: SQLiteDatabase): CadastroRequest? {
        val cursor = db.query(
            "Cadastro",
            null,
            null,
            null,
            null,
            null,
            null
        )

        // Se houver resultados, cria um objeto CadastroRequest
        if (cursor != null && cursor.moveToFirst()) {
            val cadastro = CadastroRequest(
                possuiCoreText = cursor.getString(cursor.getColumnIndexOrThrow("possuiCoreText")),
                CNPJ = cursor.getString(cursor.getColumnIndexOrThrow("CNPJ")),
                RazaoSocial = cursor.getString(cursor.getColumnIndexOrThrow("RazaoSocial")),
                Fantasia = cursor.getString(cursor.getColumnIndexOrThrow("Fantasia")),
                CEP = cursor.getString(cursor.getColumnIndexOrThrow("CEP")),
                Endereco = cursor.getString(cursor.getColumnIndexOrThrow("Endereco")),
                Bairro = cursor.getString(cursor.getColumnIndexOrThrow("Bairro")),
                Cidade = cursor.getString(cursor.getColumnIndexOrThrow("Cidade")),
                UF = cursor.getString(cursor.getColumnIndexOrThrow("UF")),
                nome = cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                sobrenome = cursor.getString(cursor.getColumnIndexOrThrow("sobrenome")),
                cpf = cursor.getString(cursor.getColumnIndexOrThrow("cpf")),
                dataNascimento = cursor.getString(cursor.getColumnIndexOrThrow("dataNascimento")),
                celular = cursor.getString(cursor.getColumnIndexOrThrow("celular")),
                telefoneComercial = cursor.getString(cursor.getColumnIndexOrThrow("telefoneComercial")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                hashIndicacao = cursor.getString(cursor.getColumnIndexOrThrow("hashIndicacao")),
                isPoliticaPrivacidade = cursor.getInt(cursor.getColumnIndexOrThrow("isPoliticaPrivacidade")) == 1,
                isTermoPolitica = cursor.getInt(cursor.getColumnIndexOrThrow("isTermoPolitica")) == 1,
                isAssinaContrato = cursor.getInt(cursor.getColumnIndexOrThrow("isAssinaContrato")) == 1,
                DeviceToken = cursor.getString(cursor.getColumnIndexOrThrow("DeviceToken")),
                FotoPerfil = cursor.getString(cursor.getColumnIndexOrThrow("FotoPerfil")),
                UDID = cursor.getString(cursor.getColumnIndexOrThrow("UDID")),
                VersaoaAPP = cursor.getString(cursor.getColumnIndexOrThrow("VersaoaAPP")),
                Dispositivo = cursor.getString(cursor.getColumnIndexOrThrow("Dispositivo")),
                SistemaOperacional = cursor.getString(cursor.getColumnIndexOrThrow("SistemaOperacional")),
                Plataforma = cursor.getString(cursor.getColumnIndexOrThrow("Plataforma")),
                FotoDocumento = cursor.getString(cursor.getColumnIndexOrThrow("FotoDocumento")),
                ImagemAssinatura = cursor.getString(cursor.getColumnIndexOrThrow("ImagemAssinatura"))
            )

            cursor.close()
            return cadastro
        }

        cursor?.close()
        return null
    }
    fun deletarCadastro(db: SQLiteDatabase) {
        db.delete("Cadastro", null, null)
        db.delete("ImagemCadastro", null, null)
        db.delete("Mesorregiao", null, null)
        db.delete("Cidade", null, null)
        db.delete("CadastroAreaAtuacal", null, null)
    }
    fun recuperarFotos(db: SQLiteDatabase): Uri? {
        val query = "SELECT uriImagemDocumento FROM ImagemCadastro"
        val cursor = db.rawQuery(query, null)

        return if (cursor.moveToFirst()) {
            val fotoDocumentoUri = cursor.getString(cursor.getColumnIndexOrThrow("uriImagemDocumento"))
            cursor.close()
            Uri.parse(fotoDocumentoUri)
        } else {
            cursor.close()
            null
        }
    }
    fun inserirCadastroAreaAtuacao(
        db: SQLiteDatabase,
        cadastroAreaAtuacal: CadastroRequestAreaAtuacal
    ) {
        db.delete("CadastroAreaAtuacal", null, null)
        db.delete("Mesorregiao", null, null)
        db.delete("Cidade", null, null)
        val valuesCadastroArea = ContentValues().apply {
            put("UF", cadastroAreaAtuacal.UF)
        }
        val cadastroAreaAtuacalId = db.insert("CadastroAreaAtuacal", null, valuesCadastroArea)

        // Inserir Mesorregioes
        cadastroAreaAtuacal.Mesorregioes.forEach { mesorregiao ->
            val valuesMesorregiao = ContentValues().apply {
                put("mesorregiao", mesorregiao.Mesorregiao)
                put("mesorregiao_id", mesorregiao.Mesorregiao_id)
                put("cadastro_area_atuacal_id", cadastroAreaAtuacalId)
            }
            val mesorregiaoId = db.insert("Mesorregiao", null, valuesMesorregiao)

            mesorregiao.Cidades.forEach { cidade ->
                val valuesCidade = ContentValues().apply {
                    put("cidade", cidade.Cidade)
                    put("mesorregiao_id", mesorregiaoId)
                }
                db.insert("Cidade", null, valuesCidade)
            }
        }
    }
    fun recuperarCadastroAreaAtuacao(db: SQLiteDatabase): CadastroRequestAreaAtuacal? {
        // Consulta para obter a UF
        val cursorArea = db.query(
            "CadastroAreaAtuacal",
            arrayOf("id", "UF"),
            null,
            null,
            null,
            null,
            null
        )

        if (cursorArea.moveToFirst()) {
            val cadastroAreaAtuacalId = cursorArea.getInt(cursorArea.getColumnIndexOrThrow("id"))
            val uf = cursorArea.getString(cursorArea.getColumnIndexOrThrow("UF"))
            cursorArea.close()

            // Lista de Mesorregioes
            val mesorregioes = mutableListOf<Mesorregiao>()
            val cursorMesorregiao = db.query(
                "Mesorregiao",
                arrayOf("id", "mesorregiao", "mesorregiao_id"),
                "cadastro_area_atuacal_id = ?",
                arrayOf(cadastroAreaAtuacalId.toString()),
                null,
                null,
                null
            )

            while (cursorMesorregiao.moveToNext()) {
                val mesorregiaoId = cursorMesorregiao.getInt(cursorMesorregiao.getColumnIndexOrThrow("id"))
                val mesorregiaoNome = cursorMesorregiao.getString(cursorMesorregiao.getColumnIndexOrThrow("mesorregiao"))
                val mesorregiaoIdSistema = cursorMesorregiao.getInt(cursorMesorregiao.getColumnIndexOrThrow("mesorregiao_id"))

                // Lista de Cidades
                val cidades = mutableListOf<Cidade>()
                val cursorCidade = db.query(
                    "Cidade",
                    arrayOf("cidade"),
                    "mesorregiao_id = ?",
                    arrayOf(mesorregiaoId.toString()),
                    null,
                    null,
                    null
                )

                while (cursorCidade.moveToNext()) {
                    val cidadeNome = cursorCidade.getString(cursorCidade.getColumnIndexOrThrow("cidade"))
                    cidades.add(Cidade(Cidade = cidadeNome))
                }
                cursorCidade.close()

                mesorregioes.add(
                    Mesorregiao(
                        Mesorregiao = mesorregiaoNome,
                        Mesorregiao_id = mesorregiaoIdSistema,
                        Cidades = ArrayList(cidades)
                    )
                )
            }
            cursorMesorregiao.close()

            // Retorna o objeto final
            return CadastroRequestAreaAtuacal(
                UF = uf,
                Mesorregioes = mesorregioes
            )
        }
        cursorArea.close()
        return null
    }

}