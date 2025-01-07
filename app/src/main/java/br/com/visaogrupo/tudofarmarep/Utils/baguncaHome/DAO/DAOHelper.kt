package br.com.visaogrupo.tudofarmarep.DAO

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DAOHelper(context:Context):SQLiteOpenHelper (context, "loiu.db", null, 111){
    override fun onCreate(db: SQLiteDatabase?) {
        createbanco(db);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS TB_PRODUTOS")
        db?.execSQL("DROP TABLE IF EXISTS TB_MARCAS")
        db?.execSQL("DROP TABLE IF EXISTS TB_FILTRO_PODUTO")
        db?.execSQL("DROP TABLE IF EXISTS TB_LOJAS")
        db?.execSQL("DROP TABLE IF EXISTS TB_PROGRESSIVA")
        db?.execSQL("DROP TABLE IF EXISTS TB_CARRINHO")
        db?.execSQL("DROP TABLE IF EXISTS TB_FORMA_PAGAMENTO")
        db?.execSQL("DROP TABLE IF EXISTS TB_CotacoesCarrinho")
        db?.execSQL("DROP TABLE IF EXISTS Cadastro")
        db?.execSQL("DROP TABLE IF EXISTS CadastroAreaAtuacal")
        db?.execSQL("DROP TABLE IF EXISTS Mesorregiao")
        db?.execSQL("DROP TABLE IF EXISTS Cidade")
        db?.execSQL("DROP TABLE IF EXISTS ImagemCadastro")

        createbanco(db);
    }


    fun createbanco(db: SQLiteDatabase?){

        db?.execSQL("""
            CREATE TABLE Cadastro (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                possuiCoreText TEXT,
                possuiCore INTEGER,
                CNPJ TEXT,
                RazaoSocial TEXT,
                Fantasia TEXT,
                CEP TEXT,
                Endereco TEXT,
                Bairro TEXT,
                Cidade TEXT,
                UF TEXT,
                nome TEXT,
                sobrenome TEXT,
                cpf TEXT,
                dataNascimento TEXT,
                celular TEXT,
                telefoneComercial TEXT,
                email TEXT,
                hashIndicacao TEXT,
                isPoliticaPrivacidade INTEGER,
                isTermoPolitica INTEGER,
                isAssinaContrato INTEGER,
                DeviceToken TEXT,
                FotoPerfil TEXT,
                UDID TEXT,
                VersaoaAPP TEXT,
                Dispositivo TEXT,
                SistemaOperacional TEXT,
                Plataforma TEXT,
                FotoDocumento TEXT,
                ImagemAssinatura TEXT
            );
        """)
        db?.execSQL("""
            CREATE TABLE ImagemCadastro (
                 uriImagemDocumento TEXT NOT NULL
            )
            """);
        db?.execSQL("""
            CREATE TABLE CadastroAreaAtuacal (
                UF TEXT
            );
        """)
        db?.execSQL("""
            CREATE TABLE Mesorregiao (
                mesorregiao TEXT,
                mesorregiao_id INTEGER,
                cadastro_area_atuacal_id INTEGER,
                FOREIGN KEY(cadastro_area_atuacal_id) REFERENCES CadastroAreaAtuacal(id)
            );
        """)
        db?.execSQL("""
            CREATE TABLE Cidade (
                cidade TEXT,
                mesorregiao_id INTEGER,
                FOREIGN KEY(mesorregiao_id) REFERENCES Mesorregiao(id)
            );
        """)

        val  sqlProduto = "CREATE TABLE IF NOT EXISTS TB_PRODUTOS(" +
                "barra VARCHAR(20) PRIMARY KEY," +
                "marca_ID INTEGER," +
                "nome VARCHAR(500)," +
                "imagem Varchar(100)" +
                ")"



        val  sqlMarca = "CREATE TABLE IF NOT EXISTS TB_MARCAS(" +
                "marca_ID INTEGER PRIMARY KEY," +
                "nome VARCHAR(200)," +
                "logoTipo VARCHAR(200),"+
                "banco VARCHAR(100),"+
                "url VARCHAR(300),"+
                "status INTEGER"+
        ")"

        val sqlFiltro =  "CREATE TABLE IF NOT EXISTS TB_FILTRO_PODUTO (" +
                "Categorias_Atributos_Produtos INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Categoria_id INTEGER NOT NULL, " +
                "Atributo_id INTEGER NOT NULL, " +
                "Barra VARCHAR(15)" +
                ")"
        val sqlProgressiva = """
            CREATE TABLE IF NOT EXISTS TB_PROGRESSIVA (
                loja_ID INTEGER,
                barra VARCHAR(50),
                quantidadePedido INTEGER,
                valorUnitario REAL,
                desconto REAL,
                valorUnitarioDesconto REAL,
                valorTotalDesconto REAL,
                marca_ID INTEGER,
                loja_ID_Portal INTEGER,
                quantidadeMaxima INTEGER,
                uf VARCHAR(2),
                origem VARCHAR(100),
                arquivoPreco VARCHAR(200),
                operadorLogisticoGrupoID INTEGER,
                imagem VARCHAR(300),
                nome VARCHAR(200), 
                comissao REAL, 
                comissaoTotal REAL,
                stUnitario REAL,
                idProgressiva INTEGER PRIMARY KEY AUTOINCREMENT

            )
        """
         val sqlLojas = """
                CREATE TABLE IF NOT EXISTS TB_LOJAS (
                    Loja_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Nome TEXT,
                    LojaTipo_ID TEXT,
                    ValorMinimo REAL,
                    ValorMaximo REAL,
                    QtdeMinOperador INTEGER,
                    QtdeMaxOperador INTEGER,
                    QtdeMaxVendas INTEGER,
                    Loja_id_Portal INTEGER,
                    MarcaID INTEGER
                );
            """

        val sqlCarrinho = """
                CREATE TABLE IF NOT EXISTS TB_CARRINHO (
                    barra VARCHAR(50),
                    cnpj VARCHAR(50),
                    operadorID INTEGER,
                    marca_ID INTEGER,
                    Loja_ID INTEGER,
                    quantidade_adicionada INTEGER,
                    valorTotal REAL,
                    idProgressiva INTEGER,
                    nomeOperador VARCHAR(50),
                    TotalSt REAL,
                    razaoSocial VARCHAR(200),
                    PRIMARY KEY (barra, Loja_ID, cnpj)
              );
            """
        val sqlCotacaoCarrinho = """
                CREATE TABLE IF NOT EXISTS TB_CotacoesCarrinho (
                    Loja_ID INTEGER,
                    cnpj VARCHAR(50),
                    carrinhoID INTEGER,
                    formaDePagamentoMarcaID INTEGER,
                    OperadorLogistico1 INTEGER,
                    OperadorLogistico2 INTEGER,
                    OperadorLogistico3 INTEGER,
                    OperadorLogistico4 INTEGER,
                    OperadorLogistico5 INTEGER,
                    PRIMARY KEY (carrinhoID)
              );
            """
        val sqlFormaDePagamento = """
            CREATE TABLE IF NOT EXISTS TB_FORMA_PAGAMENTO (
               FormaPagamentoMarcas_ID INTEGER,
               Descricao VARCHAR(200),
               Loja_ID INTEGER,
               PRIMARY KEY (FormaPagamentoMarcas_ID, Loja_ID)
            )
        """.trimIndent()

        db?.execSQL(sqlProduto)
        db?.execSQL(sqlMarca)
        db?.execSQL(sqlLojas)
        db?.execSQL(sqlFiltro)
        db?.execSQL(sqlProgressiva)
        db?.execSQL(sqlCarrinho)
        db?.execSQL(sqlFormaDePagamento)
        db?.execSQL(sqlCotacaoCarrinho)

    }
}