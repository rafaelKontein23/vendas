package br.com.visaogrupo.tudofarmarep.Objetos

data  class Progressiva (val lojaID: Int,
                         val barra: String,
                         val quantidadePedido: Int,
                         val valorUnitario: Double,
                         val desconto: Double,
                         val valorUnitarioDesconto: Double,
                         val valorTotalDesconto: Double,
                         val marcaID: Int,
                         val lojaIDPortal: Int,
                         val quantidadeMaxima: Int,
                         val uf: String,
                         val origem: String,
                         val arquivoPreco: String,
                         val operadorLogisticoGrupoID:Int,
                         val imagem :String,
                         val nome :String,
                         val comissao:Double,
                         val id:Int =0,
                         var comissaoTotal:Double,
                         var stUnitario:Double =0.0
    )